package org.spat.wf.mvc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.spat.dao.DBConfig;
import org.spat.wf.utils.ConvertUtil;

public class WFConfig {
	private String siteName;
	private String charset = "UTF-8";
	private String namespace;
	private String rootPath;
	private String mode; //ONLINE,OFFLINE
	private DAO dao = new  DAO();
	private Cache cache = new Cache();
	private Session session = new Session();
	private Trace trace = new Trace();
	private Logger logger = new Logger();
	private Properties properties = new Properties();
	
	private static WFConfig CONFIG = null;
	public static WFConfig Instance(){
		if(CONFIG==null){
			try {
				Init(BeatContext.current().getRequest().getServletContext().getRealPath("/"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return CONFIG;
	}
	
	@SuppressWarnings("unchecked")
	private WFConfig(String rootPath) throws Exception{
		File wfxml = new File( rootPath+"/wf.xml");
		SAXReader reader = new SAXReader(); 
		Document doc = reader.read(wfxml); 
		this.rootPath = rootPath;
		this.siteName = doc.selectSingleNode("/wf/SiteName").getText();
		this.namespace = doc.selectSingleNode("/wf/Namespace").getText();
		if(doc.selectSingleNode("/wf/Charset")!= null){
			this.charset = doc.selectSingleNode("/wf/Charset").getText();
		}
		this.mode = doc.selectSingleNode("/wf/Mode").getText();
		//dao
		List<Element> dbNodes = doc.selectNodes("/wf/DAO/database");
		for(Element dbNode : dbNodes){
			DBConfig database = new DBConfig();
			if(dbNode!=null){
				database.setName(dbNode.attributeValue("name"));
				database.setDbName(dbNode.attributeValue("dbname"));
				database.setHost(dbNode.attributeValue("host"));
				database.setHost2(dbNode.attributeValue("host2"));
				database.setDriversClass(dbNode.attributeValue("drivers"));
				database.setUserName(dbNode.attributeValue("username"));
				database.setPassWord(dbNode.attributeValue("passwd"));
				database.setTimeout(ConvertUtil.toInt(dbNode.attributeValue("timeout")));
				database.setEncoder(dbNode.attributeValue("encoder"));
				Element connpoolNode = (Element)dbNode.selectSingleNode("ConnPool");
				if(connpoolNode!=null){
					database.setMaxPoolSize(ConvertUtil.toInt(connpoolNode.attributeValue("maxPoolSize")));
					database.setMinPoolSize(ConvertUtil.toInt(connpoolNode.attributeValue("minPoolSize")));
					database.setIdleTimeout(ConvertUtil.toInt(connpoolNode.attributeValue("idleTimeout")));
					database.setAutoShrink(ConvertUtil.toBoolean(connpoolNode.attributeValue("autoShrink")));
				}
			}
			this.dao.databases.put(database.getName(), database);
		}
		List<Element> converterNodes = doc.selectNodes("/wf/DAO/converter");
		for(Element node : converterNodes){
			if(node!=null){
				String name = node.attributeValue("name");
				String clz = node.attributeValue("class");
				Class<?> c = Class.forName(clz);
				if(c!=null){
					this.dao.converters.put(name, c);
				}else{
					System.err.println("DAO.Converter config error,cannot find the class from the value.");
				}
			}
		}
		
		//cache
		Element cacheNode =  (Element)doc.selectSingleNode("/wf/Cache");
		if(cacheNode!=null){
			this.cache.setType(cacheNode.attributeValue("type").toLowerCase().trim());
			this.cache.setExpiry(ConvertUtil.toInt(cacheNode.attributeValue("expiry").trim()));
			this.cache.setEnable(cacheNode.attributeValue("enable").toLowerCase().trim().equals("true"));
			List<Element> properties = doc.selectNodes("/wf/Cache/Properties/item");
			if(properties!=null){
				for(Element property : properties){
					this.cache.getProperties().put(property.attributeValue("name"), property.attributeValue("value"));
				}
			}
		}
		
		//session
		Element sessionNode =  (Element)doc.selectSingleNode("/wf/Session");
		if(sessionNode!=null){
			this.session.setType(sessionNode.attributeValue("store").toLowerCase().trim());
			this.session.setExpiry(ConvertUtil.toInt(sessionNode.attributeValue("expiry").trim()));
			List<Element> properties = doc.selectNodes("/wf/Session/Properties/item");
			if(properties!=null){
				for(Element property : properties){
					this.session.getProperties().put(property.attributeValue("name"), property.attributeValue("value"));
				}
			}
		}
		
		//Trace
		Element traceNode =  (Element)doc.selectSingleNode("/wf/Trace");
		if(traceNode!=null){
			this.trace.setEnable(traceNode.attributeValue("enable").toLowerCase().trim().equals("true"));
			this.trace.setMode(traceNode.attributeValue("mode").toLowerCase().trim());
		}
		
		//Logger
		Element logNode =  (Element)doc.selectSingleNode("/wf/Logger");
		if(logNode!=null){
			if(logNode.attribute("type")!=null){
				this.logger.setType(logNode.attribute("type").getText());
			}
			if(logNode.attribute("level")!=null){
				this.logger.setLevel(logNode.attribute("level").getText());
			}
			List<Element> log_properties = doc.selectNodes("/wf/Logger/Properties/item");
			for(Element property : log_properties){
				this.logger.getProperties().put(property.attributeValue("name"), property.attributeValue("value"));
			}
		}
		
		
		//Properties
		List<Element> properties = doc.selectNodes("/wf/Properties/item");
		if(properties!=null){
			for(Element property : properties){
				this.setProperty(property.attributeValue("name"), property.attributeValue("value"));
			}
			
		}
	}
	
	public static void Init(String rootPath) throws Exception{
		CONFIG = new WFConfig(rootPath);
	}
	
	public String getSiteName() {
		return this.siteName;
	}
	
	public String getCharset() {
		return charset;
	}

	public String getNamespace() {
		return this.namespace;
	}
	
	public String getRootPath() {
		return this.rootPath;
	}
	
	public String getConfigPath(){
		return this.rootPath+"/cofig/";
	}
	
	public String getMode(){
		return this.mode;
	}

	public Object getProperty(String key) {
		return this.properties.get(key);
	}

	public void setProperty(String key, String value) throws Exception {
		this.properties.put(key, value);
	}
	
	public DBConfig getDatabase(String name) {
		if(name.equals("") && this.dao.databases.size()>0){
			return (DBConfig)this.dao.databases.values().toArray()[0];
		}
		return this.dao.databases.get(name);
	}
	
	public DAO getDao() {
		return dao;
	}

	public Session getSession() {
		return session;
	}

	public Cache getCache() {
		return cache;
	}

	public Trace getTrace() {
		return trace;
	}


	public Logger getLogger() {
		return logger;
	}


	public class Session{
		private String type ="default"; //local、memcached
		private int expiry ;//超时时间
		private HashMap<String,String> properties = new HashMap<String,String>();
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getExpiry() {
			return expiry;
		}
		public void setExpiry(int expiry) {
			this.expiry = expiry;
		}
		public HashMap<String,String> getProperties() {
			return properties;
		}	

	}
	
	public class Trace{
		private boolean enable = false;
		private String mode = "page";
		public boolean isEnable() {
			return enable;
		}
		public void setEnable(boolean enable) {
			this.enable = enable;
		}
		public String getMode() {
			return mode;
		}
		public void setMode(String mode) {
			this.mode = mode;
		}
		
	}
	
	public class Cache{
		private String type ="org.spat.wf.mvc.cache.Localcache";
		private int expiry ;//超时时间
		private boolean enable = true;
		private HashMap<String,String> properties = new HashMap<String,String>();
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getExpiry() {
			return expiry;
		}
		public void setExpiry(int expiry) {
			this.expiry = expiry;
		}
		public HashMap<String,String> getProperties() {
			return properties;
		}
		public boolean isEnable() {
			return enable;
		}
		public void setEnable(boolean enable) {
			this.enable = enable;
		}	
	}
	
	public class Logger{
		private String type = "org.spat.wf.log.logger4j";//log4j,udp
		private String level = "error";
		private HashMap<String,String> properties = new HashMap<String,String>();
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getLevel() {
			return level;
		}
		public void setLevel(String level) {
			this.level = level;
		}
		public HashMap<String,String> getProperties() {
			return properties;
		}	
	}
	public class DAO{
		private HashMap<String, DBConfig> databases = new  HashMap<String, DBConfig>();
		private HashMap<String, Class<?>> converters = new  HashMap<String, Class<?>>();
		
		public HashMap<String, DBConfig> getDatabases() {
			return databases;
		}
		public void setDatabases(HashMap<String, DBConfig> databases) {
			this.databases = databases;
		}
		public HashMap<String, Class<?>> getConverters() {
			return converters;
		}
		public void setConverters(HashMap<String, Class<?>> converters) {
			this.converters = converters;
		}
	}
	
}

