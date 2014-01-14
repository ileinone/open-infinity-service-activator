/*
 * Copyright (c) 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openinfinity.integration.service.activator.proxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for:
 * <ul>
 * <li>providing dynamic proxy object from the actual service interface, and</li>
 * <li>maintaining the semantic coherence to the actual service interface.</li>
 * </ul>
 *
 * @author Ilkka Leinonen
 * @version 1.0.0.RELEASE
 * @since 1.0.0.RELEASE
 */
@Component
public class ProxyFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyFactory.class);
	
	private static final String 
	CACHING_FLAG_NOTIFICATION = "Caching not used with proxy classes. Consider setting CACHE flag to true.",
	CURRENT_CACHE_SIZE = "Current cache size: ",
	ADDING_INSTANCE_TO_CACHE = "Adding dynamic proxy instance to cache: ",
	OBJECT_FOUND_FROM_CACHE = "Object found from cache, returning.";
	
	//private static ServiceActivatorInvocationHandler serviceActivatorInvocationHandler;
	
	
	private static Map<Class<?>, Object> CACHE;
	private static Boolean CACHING;
	
	static {
		CACHE = new HashMap<Class<?>, Object>();
		CACHING = Boolean.TRUE;
	}
	
	// TODO CACHE, ServiceActivatorInvocationHandler pit‰‰ saada toimimaan niin, ett‰ se injektoidaan kun sovellus nousee pystyyn.. Pit‰‰ harkita tarvitaanko cachea ollenkaan, koska voidaan hoitaa Spring beanin scopeilla
	@SuppressWarnings("unchecked")
	public static <T extends Object> T newInstance(Class<?> interfaceForProxy, ServiceActivatorInvocationHandler serviceActivatorInvocationHandler) {
		if(CACHING) {
			if (CACHE.containsKey(interfaceForProxy)) {
				LOGGER.debug(OBJECT_FOUND_FROM_CACHE);
				return (T)CACHE.get(interfaceForProxy);
			} else {
				T dynamicProxy = (T)Proxy.newProxyInstance(serviceActivatorInvocationHandler.getServiceActivator().getClass().getClassLoader(), new Class[]{interfaceForProxy}, serviceActivatorInvocationHandler);			
				CACHE.put(interfaceForProxy, dynamicProxy);
				LOGGER.debug(ADDING_INSTANCE_TO_CACHE + dynamicProxy.getClass().getName());
				LOGGER.debug(CURRENT_CACHE_SIZE + CACHE.size());
				return dynamicProxy;
			}
		} else {
			LOGGER.debug(CACHING_FLAG_NOTIFICATION);
			return (T)Proxy.newProxyInstance(serviceActivatorInvocationHandler.getServiceActivator().getClass().getClassLoader(), new Class[]{interfaceForProxy}, serviceActivatorInvocationHandler);			
		}
	}

}