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
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.openinfinity.core.exception.SystemException;
import org.openinfinity.integration.service.activator.InMemoryServiceActivatorRegistry;
import org.openinfinity.integration.service.activator.ServiceActivator;
import org.openinfinity.integration.service.activator.ServiceActivatorRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for:
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author Ilkka Leinonen
 * @version 1.0.0.RELEASE
 * @since 1.0.0.RELEASE
 */
//@Component
//@Qualifier("serviceActivatorRegistry")
//@Configuration
public class ServiceActivatorConfiguration implements ApplicationContextAware {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * Query sequel when executing queries.
	 */
	public static final String SERVICE_ACTIVATOR_CONFIGURATION_QUERY_SQL = "SELECT INTERFACE, QUALIFIER FROM SERVICE_ACTIVATOR_CONFIGURATION";
	
	/**
	 * JDBC template for accessing shared database.
	 */
	//private JdbcTemplate jdbcTemplate;
	
	/**
	 * Public constructor for the class.
	 * 
	 * @param dataSource Represents the shared database data source.
	 */
	//@Autowired
	public ServiceActivatorConfiguration() {
	//public ServiceActivatorConfiguration(DataSource dataSource) {
		System.out.println("Starting..");
		//Assert.notNull(dataSource, "Please define data source for reading service activator configuration from the data repository.");
		//DataSource dataSource = (DataSource)applicationContext.getBean("dataSource");
		//		System.out.println("!!!!!!!! Datasource is null? " + (dataSource == null));
				
		//this.jdbcTemplate = new JdbcTemplate();
		//this.jdbcTemplate = new JdbcTemplate(createTestDataSource());
	}
	
	/**
	 * Reads service activator configuration from the shared data source and creates in-memory service activator registry on the fly.
	 * 
	 * @return Properties Returns properties fetched from the shared database.
	 * @throws Throwable 
	 * 
	 */
//	public static @Bean @Qualifier("serviceActivatorRegistry") ServiceActivatorRegistry<?> readServiceActivatorMappingsFromDatasource() {
	@PostConstruct
	public ServiceActivatorRegistry<Proxy> readServiceActivatorMappingsFromDatasource() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource((DataSource)applicationContext.getBean("dataSource"));
		//final ServiceActivatorRegistry<Proxy> serviceActivatorRegistry = new MemoryServiceActivatorRegistry();

		//populateServiceActivatorRegistry(jdbcTemplate, serviceActivatorRegistry);
		
		
		
		// Working work-a-round
		
		final ServiceActivatorRegistry<Proxy> serviceActivatorRegistry = new InMemoryServiceActivatorRegistry();
	//	ServiceActivatorInvocationHandler invocationHandler = new ServiceActivatorInvocationHandler((ServiceActivator) applicationContext.getBean("accountServiceServiceActivator"));
	//	ServiceActivatorInvocationHandler invocationHandler2 = new ServiceActivatorInvocationHandler((ServiceActivator) applicationContext.getBean("serviceActivatorImpl"));
		
		//try {
	//		Proxy object = ProxyFactory.newInstance(Class.forName("org.openinfinity.integration.service.activator.stubs.AccountService"), invocationHandler);
//			Proxy object2 = ProxyFactory.newInstance(Class.forName("org.openinfinity.integration.service.activator.stubs.TestService"), invocationHandler2);
		//	serviceActivatorRegistry.store(object2);
			
	//		serviceActivatorRegistry.store(object);
		//} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		
		
		System.out.println("Size of serviceactivatorregistry" + serviceActivatorRegistry.getSize());
		return serviceActivatorRegistry;
	}

	private void populateServiceActivatorRegistry(JdbcTemplate jdbcTemplate,
			final ServiceActivatorRegistry<Proxy> serviceActivatorRegistry) {
		jdbcTemplate.query(SERVICE_ACTIVATOR_CONFIGURATION_QUERY_SQL, new RowMapper<ServiceActivatorRegistry<?>>() {
			@Override
			public ServiceActivatorRegistry mapRow(ResultSet resultSet, int rowNum) throws SQLException, SystemException {
				while (resultSet.next()) {
					try {
						String interfaceClass = resultSet.getString("INTERFACE");
						String qualifier = resultSet.getString("QUALIFIER");
						//ServiceActivatorInvocationHandler invocationHandler = new ServiceActivatorInvocationHandler((ServiceActivator) applicationContext.getBean(qualifier));
						//Object object = ProxyFactory.newInstance(Class.forName(interfaceClass), invocationHandler);
						//serviceActivatorRegistry.store((Proxy)object);	
					} catch (Throwable throwable) {
						// Fail fast if exception occurs.
						SystemException systemException = new SystemException(throwable.toString());
						systemException.setStackTrace(throwable.getStackTrace());
						throw systemException;
					}
				}
				return serviceActivatorRegistry;					
			}
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		System.out.println("!!!!!!!!!!!!!!!! Initializing applicationcontext");
		this.applicationContext = applicationContext;
	}
	
}