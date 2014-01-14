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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openinfinity.integration.service.activator.stubs.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/app-context.xml"})
//@ContextConfiguration(locations={"classpath:/META-INF/spring/app-context.xml"})
//@ContextConfiguration(locations={"classpath:/META-INF/spring/service-activator-context.xml"})
public class ServiceActivatorConfigurationTests extends AbstractJUnit4SpringContextTests  {
	
	//@Autowired
	//private FooService service;
	@Autowired
	@Qualifier("exampleService")
	ExampleService service;
	
	@Before
	public void setUp() throws Exception {
		//service = (ExampleService) applicationContext.getBean("exampleService");	
	}
	
	@Test
	public void testSimpleProperties() throws Exception {
//		assertNotNull(service);
		String expected = "<xml>This echo message from the service</xml>";
		String actual = service.getMessage();//(expected);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+actual);
		assertEquals(expected, actual);		
//		for(int i = 0 ; i < 100 ; i++) {
//			ServiceActivatorThread serviceActivatorThread = new ServiceActivatorThread();
//			Thread t = new Thread(serviceActivatorThread);
//			t.start();
//		}
	}
	
	private class ServiceActivatorThread implements Runnable {
		
		public void run() {
			String expected = "<xml>This echo message from the service</xml>"; // + System.currentTimeMillis();
			String actual = service.getMessage();//(expected);
			assertEquals(expected, actual);
		}
		
	}
	
}