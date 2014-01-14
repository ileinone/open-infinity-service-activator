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
package org.openinfinity.integration.service.activator.stubs;

import java.util.HashMap;
import java.util.Map;

import org.openinfinity.integration.annotation.ServiceActivator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
/**
 * {@link ExampleService} with hard-coded input data.
 */
@Component
@Qualifier("exampleService")
public class ExampleServiceImpl implements ExampleService {
	
	//@Parallel(groups="parellelTest")
	//@Qualifier("testService1ProxyFactory")
	//@Autowired
	@ServiceActivator
	public TestService testService;
	
	//@Parallel(groups="parellelTest")
	//@Qualifier("testService2ProxyFactory")
	//@Autowired
	//private TestService testService2;
	
	//@Autowired
	//private FooService fooService;
	
	//@Parallel(groups="parellelTest")
	//@Autowired
	@ServiceActivator
	public AccountService accountService;
	
	//public ExampleService() {}
	
	public String getMessage() {
		Account account = new Account();
		account.setId(123);
		account.setName("Ilkka");
		account.setAddress("Heidehofinpolku");
		account = accountService.store(account);
		
		accountService.store(account);
		return testService.sayHi();	
	}

	//@Parallel(managedGroup="parellelTest")
	public Map<String, ?> getMultipleMessages() {
		Map<String, Object> model = new HashMap<String, Object>();
		String hiQ1 = testService.sayHi();
		//String hiQ2 = testService2.sayHi();
		Account account = new Account();
		account.setName("Ilkka");
		account.setAddress("Heidehofinpolku");
		account = accountService.store(account);
		model.put("account", account);
		model.put("valueFromTestService", hiQ1);
		//model.put("valueFromTestService2", hiQ2);
		return model;
	}
	
}
