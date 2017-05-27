/*
*  Copyright Beijing 58 Information Technology Co.,Ltd.
*
*  Licensed to the Apache Software Foundation (ASF) under one
*  or more contributor license agreements.  See the NOTICE file
*  distributed with this work for additional information
*  regarding copyright ownership.  The ASF licenses this file
*  to you under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.spat.wf.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class AnnotationUtils {

    /** The attribute name for annotations with a single element */
    static final String VALUE = "value";

    /**
     * 获取 给定{@link Class}的单个{@link Annotation Annotations}。
     * 当根据类型从方法中未找到符合条件的Annotation时，遍历该类的父类或者接口进行查找。
     * @param clazz 被查询的class
     * @param annotationType 查找的annotation类型
     * @return 查找到的annotation，或者<code>null</code>
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
    	
        Preconditions.checkNotNull(clazz, "Class must not be null");
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        if (!Annotation.class.isAssignableFrom(clazz)) {
            for (Annotation ann : clazz.getAnnotations()) {
                annotation = findAnnotation(ann.annotationType(), annotationType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass == Object.class) {
            return null;
        }
        return findAnnotation(superClass, annotationType);
    }

    /**
     * 获取 给定{@link java.lang.reflect.Method}的单个{@link java.lang.annotation.Annotation Annotations}。
     * 当根据类型从方法中未找到符合条件的Annotation时，查找该方法所在类所实现的接口，看接口是否存在注解。
     * @param method 被查询的method
     * @param annotationType 查找的annotation类型
     * @return 查找到的annotation
     */
    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
        
       return method.getAnnotation(annotationType);
    }
    
    @SuppressWarnings("unchecked")
	public static <A extends Annotation> List<A> filterAnnotation(Set<Annotation> annotations,Class<A> annotationType){
    	List<A> reslut = new ArrayList<A>();
    	for (Annotation annotation : annotations) {
			if(annotation.annotationType()==annotationType){
				reslut.add((A) annotation);
			}
		}
    	return reslut;
    }

}
