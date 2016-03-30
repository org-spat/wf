package org.spat.wf.mvc.initial.sys;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;
import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.WFController;
import org.spat.wf.mvc.action.Action;
import org.spat.wf.mvc.action.AntPathMatcher;
import org.spat.wf.mvc.action.MethodAction;
import org.spat.wf.mvc.action.ResourceAction;
import org.spat.wf.mvc.annotation.GET;
import org.spat.wf.mvc.annotation.POST;
import org.spat.wf.mvc.annotation.Path;
import org.spat.wf.utils.AnnotationUtils;
import org.spat.wf.utils.ClassUtils;
import org.spat.wf.utils.Pair;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ActionInit {

	private static Map<String, List<MethodAction>> ministyActions = Maps.newHashMap();

	private static Map<String, List<Action>> exactActions = Maps.newHashMap();

	public static Map<String, List<MethodAction>> getMinistyActions() {

		return ministyActions;
	}

	public static List<Action> getExactActions(String path) {

		return exactActions.get(path);
	}

	public static void init(ServletContext sc) throws Exception {
		List<MethodAction> methodActions = MethodActionInit.getMethodActions();
		for (MethodAction action : methodActions) {
			if (action.isPattern()) {
				if (!ministyActions.containsKey(action.path())) {
					ministyActions.put(action.path(),new ArrayList<MethodAction>());
				}
				ministyActions.get(action.path()).add(action);
			} else {
				if (!exactActions.containsKey(action.path())) {
					exactActions.put(action.path(), new ArrayList<Action>());
				}
				exactActions.get(action.path()).add(action);
			}
		}

		List<Action> resourceActions = ResourceActionInit.getResuourceActions(sc);

		for (Action action : resourceActions) {
			if (exactActions.containsKey(action.path())) {
				System.err.println("Exist same Action path :" + action.path());
				continue;
			}

			List<Action> actionList = new ArrayList<Action>();
			actionList.add(action);
			exactActions.put(action.path(), actionList);
		}
	}
}

class MethodActionInit {
	protected static ILogger log = LoggerFactory.getLogger(MethodActionInit.class);
	private static AntPathMatcher pathMatcher = new AntPathMatcher();
	
	private static Map<String, WFController> controllers = new HashMap<String, WFController>();

	static List<MethodAction> getMethodActions() throws Exception {
		Set<Class<? extends WFController>> controllerClasses = parseControllers(WFConfig.Instance().getNamespace(), ".*\\.controllers\\..*Controller");
		List<MethodAction> actions = Lists.newArrayList();
		for (Class<? extends WFController> controllerClazz : controllerClasses) {
			List<MethodAction> subActions = analyze(controllerClazz);
			actions.addAll(subActions);
		}
		return ImmutableList.copyOf(actions);
	}

	@SuppressWarnings("unchecked")
	private static Set<Class<? extends WFController>> parseControllers(String packagePrefix, String controllPattern) {
		Set<Class<?>> classSet = ClassUtils.getClasses(packagePrefix);
		Pattern controllerPattern = Pattern.compile(controllPattern);
		ImmutableSet.Builder<Class<? extends WFController>> builder = ImmutableSet.builder();
		for (Class<?> clazz : classSet){
			if (rules(clazz, controllerPattern)){
				builder.add((Class<? extends WFController>) clazz).build();
			}		
		}
		log.info("path:"+controllPattern);
		return builder.build();
	}

	private final static Predicate<Method> methodFilter = new Predicate<Method>() {
		@Override
		public boolean apply(Method method) {
			if (AnnotationUtils.findAnnotation(method, Path.class) == null){
				return false;
			}
			Class<?> returnType = method.getReturnType();
			return returnType != null
					&& ActionResult.class.isAssignableFrom(returnType)
					&& (!method.isBridge() // TODO: 是否需要处理
							&& method.getDeclaringClass() != Object.class && Modifier
								.isPublic(method.getModifiers()));
		}
	};

	private static String[] getPathurls(Path path) {
		String[] pathUrls = path == null ? new String[] { "" } : path.value();
		for (String pathUrl : pathUrls) {
			if (pathUrl.length() == 0 || pathUrl.charAt(0) != '/'){
				pathUrl = '/' + pathUrl;
			}
				
		}
		return pathUrls;
	}

	private static List<MethodAction> analyze(Class<? extends WFController> clazz) throws Exception {
		List<MethodAction> actions = Lists.newArrayList();
		Set<Method> sets = Sets.filter(Sets.newHashSet(clazz.getDeclaredMethods()), methodFilter);
		Path path = AnnotationUtils.findAnnotation(clazz, Path.class);
		String[] pathUrl = getPathurls(path);
		for (int i = 0; i < pathUrl.length; i++) {
			for (Method method : sets) {
				List<MethodAction> methodAction = getMethodAction(pathUrl[i],clazz, method);
				actions.addAll(methodAction);
			}
		}
		return actions;
	}

	private static List<MethodAction> getMethodAction(String pathUrl,Class<? extends WFController> clazz, Method method)throws Exception {
		List<MethodAction> actions = new ArrayList<MethodAction>();
		Path pathAnnotation = AnnotationUtils.findAnnotation(method, Path.class);
		String[] paths = pathAnnotation.value();
		for (int j = 0; j < paths.length; j++) {
			String pathPattern = simplyPathPattern(pathUrl, paths[j]);
			ImmutableList<String> paramNames = ImmutableList.copyOf(ClassUtils.getMethodParamNames(clazz, method));
			List<Class<?>> paramTypes = ImmutableList.copyOf(method.getParameterTypes());
			Set<Annotation> annotationsOfClass = Sets.newHashSet(clazz.getAnnotations());
			Set<Annotation> annotationsOfMethod = Sets.newHashSet(method.getAnnotations());
			Builder<Annotation> builder = ImmutableSet.builder();
			builder.addAll(annotationsOfClass).addAll(annotationsOfMethod);
			Set<Annotation> annotations = builder.build();
			try {
				WFController controller = controllers.get(clazz.getPackage()+ clazz.getName());
				if (controller == null) {
					controller = clazz.newInstance();
					controllers.put(clazz.getPackage() + clazz.getName(),controller);
				}
				boolean[] httpSupport = pickUpHttpMethod(method, controller);
				actions.add(new MethodAction(controller, method, pathPattern,httpSupport[0], httpSupport[1], paramNames, paramTypes,annotations));
			} catch (Exception e) {
				throw new Exception("Build method action failed, controller: "+ clazz.getName() + " method: " + method, e);
			}
		}
		return actions;
	}

	private static String simplyPathPattern(String typePath, String methodPath) {
		String originPathPattern = combinePathPattern(typePath, methodPath);
		return simplyPathPattern(originPathPattern);
	}

	private static String combinePathPattern(String typePath, String methodPath) {
		return pathMatcher.combine(typePath, methodPath);
	}

	private static String simplyPathPattern(String combinedPattern) {
		if (combinedPattern.length() > 1){		
			if(!"/".equals(combinedPattern.substring(0, 1))){
				combinedPattern = "/" + combinedPattern;
			}
			/**
			 * 去掉末位"/"的清理功能，兼容老系统的路径规范
			 *
			if(combinedPattern.endsWith("/")){
				combinedPattern = combinedPattern.substring(0,combinedPattern.length() - 2);
			}
			
			*/
			combinedPattern = combinedPattern.replace("//", "/");
		}
		return combinedPattern;
	}

	private static boolean[] pickUpHttpMethod(Method method,WFController controller) {
		GET getAnnotaion = AnnotationUtils.findAnnotation(method, GET.class);
		POST posttAnnotaion = AnnotationUtils.findAnnotation(method, POST.class);
		if (getAnnotaion == null && posttAnnotaion != null){
			return new boolean[] { false, true };
		}
		if (getAnnotaion != null && posttAnnotaion == null){
			return new boolean[] { true, false };
		}
		return new boolean[] { true, true };
	}

	private static boolean rules(Class<?> clazz, Pattern controllerPattern) {
		return WFController.class.isAssignableFrom(clazz)
				&& controllerPattern.matcher(clazz.getName()).matches()
				&& !Modifier.isInterface(clazz.getModifiers())
				&& !Modifier.isAbstract(clazz.getModifiers())
				&& Modifier.isPublic(clazz.getModifiers());
	}
}

class ResourceActionInit {

	/**
	 * 静态文件名set
	 */
	private static Set<String> staticFiles = Sets.newHashSet();

	/**
	 * 不允许访问的文件或文件夹
	 */
	private static final Set<String> forbitPath = ImmutableSet.of("");

	static List<Action> getResuourceActions(ServletContext servletContext) {
		String resourceFolder = servletContext.getRealPath("/") + "/resources";
		final File staticResourcesFolder = new File(resourceFolder);
		List<Action> staticFileActions = new ArrayList<Action>();
		try {
			staticFiles = findFiles(staticResourcesFolder, staticFiles.size(),forbitPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String staticFile : staticFiles) {
			staticFileActions.add(new ResourceAction(staticFile));
		}
		return staticFileActions;
	}

	static Set<String> findFiles(File directory, int cap, Set<String> forbitPath)
			throws Exception {
		Set<String> staticFiles = new HashSet<String>(cap);
		Deque<Pair<File, String>> dirs = Lists.newLinkedList();
		dirs.add(Pair.build(directory, "/"));
		while (dirs.size() > 0) {
			Pair<File, String> pop = dirs.pop();

			File[] files = pop.getKey().listFiles();

			if (files == null){
				continue;
			}
				

			for (File file : files) {
				String name = pop.getValue() + file.getName();

				if (forbitPath.contains(name)){
					continue;
				}
					

				if (file.isDirectory()) {
					dirs.push(Pair.build(file, name + '/'));
					continue;
				}

				staticFiles.add(name);
			}
		}
		return staticFiles;
	}
}