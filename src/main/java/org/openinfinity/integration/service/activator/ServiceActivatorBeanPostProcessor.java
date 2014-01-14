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
package org.openinfinity.integration.service.activator;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.openinfinity.core.exception.SystemException;
import org.openinfinity.integration.service.activator.proxy.ProxyFactory;
import org.openinfinity.integration.service.activator.proxy.ServiceActivatorConfiguration;
import org.openinfinity.integration.service.activator.proxy.ServiceActivatorInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * This class is responsible of autowiring service activator beans using @Autowired annotations.
 * 
 * @author Ilkka Leinonen
 * @version 1.0.0.RELEASE
 * @since 1.0.0.RELEASE
 */

public class ServiceActivatorBeanPostProcessor implements ApplicationContextAware, InstantiationAwareBeanPostProcessor, Ordered {

	
	
	private static int CALL_ORDER = 0;
	
	private int order = 0;
	
//	//@Autowired
//	//public ServiceActivatorRegistry<?> serviceActivatorRegistry;
////	
//	@Autowired
//	private ServiceActivatorConfiguration serviceActivatorConfiguration;
	
	public void setOrder(int order) {
		this.order = order;
	}

	@Autowired
	public ApplicationContext applicationContext;
	
	public ServiceActivatorBeanPostProcessor() {}
	
	@Autowired
	public DataSource dataSource;
//	
////	public void setServiceActivatorRegistry(ServiceActivatorRegistry<?> serviceActivatorRegistry) {
////		this.serviceActivatorRegistry = serviceActivatorRegistry;
////	}

//	public void setServiceActivatorConfiguration(ServiceActivatorConfiguration serviceActivatorConfiguration) {
//		this.serviceActivatorConfiguration = serviceActivatorConfiguration;
//	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	private void populateServiceActivatorRegistry(final ServiceActivatorRegistry<Proxy> serviceActivatorRegistry) {	
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		System.out.println("Populating Service Registry");
		//jdbcTemplate.setDataSource((DataSource)applicationContext.getBean(DataSource.class));
		jdbcTemplate.query(ServiceActivatorConfiguration.SERVICE_ACTIVATOR_CONFIGURATION_QUERY_SQL, new RowMapper<ServiceActivatorRegistry<?>>() {
			@Override
			public ServiceActivatorRegistry mapRow(ResultSet resultSet, int rowNum) throws SQLException, SystemException {
				//while (resultSet.next()) {
					//System.out.println("LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOP");
					try {
						String interfaceClass = resultSet.getString("INTERFACE");
						String qualifier = resultSet.getString("QUALIFIER");
						System.out.println("Interface : " + interfaceClass + " Qualifier: " +qualifier); 
						ServiceActivator serviceActivator = (ServiceActivator) applicationContext.getBean(qualifier);
						ServiceActivatorInvocationHandler invocationHandler = new ServiceActivatorInvocationHandler(serviceActivator, interfaceClass);
						Object object = ProxyFactory.newInstance(Class.forName(interfaceClass), invocationHandler);
						serviceActivatorRegistry.store((Proxy)object);	
					} catch (Throwable throwable) {
						// Fail fast if exception occurs.
						throwable.printStackTrace();
						SystemException systemException = new SystemException(throwable.toString());
						systemException.setStackTrace(throwable.getStackTrace());
						throw systemException;
					}
				//}
				return serviceActivatorRegistry;					
			}
		});
	}

	private void loadBeansFromServiceActivatorRegistry(final Object bean, final ServiceActivatorRegistry<Proxy> serviceActivatorRegistry) {
		//System.out.println("Processing bean : " + bean.getClass().getName());
		ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {	
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				//System.out.println("Field: " + field.getName());				
				// TODO add inject annotation
				//if (Autowired.class != null && field.isAnnotationPresent(Autowired.class)) {
				if (org.openinfinity.integration.annotation.ServiceActivator.class != null && 
					field.isAnnotationPresent(org.openinfinity.integration.annotation.ServiceActivator.class) ||
					(Autowired.class != null && field.isAnnotationPresent(Autowired.class))) {
					System.out.println("Autowired annotation found: " + field.getName());
					//AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
					for (Proxy serviceActivator : serviceActivatorRegistry.loadAll()){
						autowire(bean, field, serviceActivator);
					}
                }
			}
		});
	}
	
////	private void loadBeansFromApplicationContext(final Object bean) {
////		final String[] serviceActivatorProxies = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Proxy.class);
////		//final String[] serviceActivatorProxies = applicationContext.getBeanNamesForType(Proxy.class, true, true);
////		System.out.println("application context proxy size: " + serviceActivatorProxies.length);
////		ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {			
////			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
////				//System.out.println("Field: " + field.getName());				
////				if (Autowired.class != null && field.isAnnotationPresent(Autowired.class)) {
////					System.out.println("Autowired annotation found: " + field.getName());
////					for(String proxy : serviceActivatorProxies) {
////						Object proxyInstanceInstance = applicationContext.getBean(proxy);
////						autowire(bean, field, proxyInstanceInstance);
////					}
////				}
////			}
////		});
////	}
//
	private void autowire(final Object bean, Field field, Object serviceActivator) throws IllegalAccessException {
		Class<?>[] interfaces = ClassUtils.getAllInterfaces(serviceActivator);
		for(Class<?> interfaceFromProxy : interfaces) {
			System.out.println("if "+interfaceFromProxy.getName()+".equals("+field.getType().getName()+")" + interfaceFromProxy.getName().equals(field.getType().getName()));
			if (interfaceFromProxy.getName().equals(field.getType().getName())) {
				//System.out.println("Match, trying to autowire: " + field.getName());
				if(!field.isAccessible()) {
					field.setAccessible(true);
				}
				field.set(bean, serviceActivator);
				System.out.println("################################################# Service activator injected to: " + field.getName());
				break;
			}
		}
	}
	
	public Object postProcessBeforeInitialization(final Object bean, String name) throws BeansException {
		return bean;
	}

	public void postProcessBeforeDestruction(Object bean, String name) throws BeansException {
	}
	
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass,
			String beanName) throws BeansException {
		return null;
	}
	
	private ServiceActivatorRegistry<Proxy> serviceActivatorRegistry = null;
	
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		CALL_ORDER++;
		//System.out.println("?????????? CALL_ORDER=" + CALL_ORDER + " , method=postProcessAfterInstantiation");
		System.out.println("!!!!!!!!!! Method=postProcessAfterInstantiation, Reading data for bean: " + beanName);
		if (serviceActivatorRegistry == null) {
			serviceActivatorRegistry = new InMemoryServiceActivatorRegistry();
			populateServiceActivatorRegistry(serviceActivatorRegistry);
		}
		loadBeansFromServiceActivatorRegistry(bean, serviceActivatorRegistry);
		
		 ServiceActivatorConfiguration serviceActivatorConfiguration = new ServiceActivatorConfiguration();
			 //ServiceActivatorConfiguration serviceActivatorConfiguration = new ServiceActivatorConfiguration(dataSource);
		 ServiceActivatorRegistry serviceActivatorRegistry = serviceActivatorConfiguration.readServiceActivatorMappingsFromDatasource();
		 //ServiceActivatorRegistry<Proxy> serviceActivatorRegistry = applicationContext.getBean("serviceActivatorRegistry", ServiceActivatorRegistry.class);
		 //ServiceActivatorRegistry<Proxy> serviceActivatorRegistry = serviceActivatorConfiguration.readServiceActivatorMappingsFromDatasource();
		 //ServiceActivatorRegistry serviceActivatorRegistry = applicationContext.getBean("serviceActivatorRegistry", ServiceActivatorRegistry.class);
		// DataSource dataSource = applicationContext.getBean("dataSource", DataSource.class);
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		// TOIMII ALLA
//		 ServiceActivatorInvocationHandler invocationHandler = new ServiceActivatorInvocationHandler((ServiceActivator) applicationContext.getBean("accountServiceServiceActivator"));
//		 ServiceActivatorInvocationHandler invocationHandler2 = new ServiceActivatorInvocationHandler((ServiceActivator) applicationContext.getBean("serviceActivatorImpl"));
//		
//		 try {
//			Proxy object = ProxyFactory.newInstance(Class.forName("org.openinfinity.integration.service.activator.stubs.AccountService"), invocationHandler);
//			Proxy object2 = ProxyFactory.newInstance(Class.forName("org.openinfinity.integration.service.activator.stubs.TestService"), invocationHandler2);
//			serviceActivatorRegistry.store(object);
//			serviceActivatorRegistry.store(object2);
//			loadBeansFromServiceActivatorRegistry(bean, serviceActivatorRegistry);
//
//		 } catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		 }				
		return true;
	}
	
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs,
			PropertyDescriptor[] pds, Object bean, String beanName)
			throws BeansException {
		return pvs;
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return order;
	}

}
