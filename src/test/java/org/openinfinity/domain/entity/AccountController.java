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
package org.openinfinity.domain.entity;

import org.openinfinity.integration.annotation.ServiceActivator;
import org.openinfinity.integration.service.activator.stubs.AccountService;
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
@Component
@Qualifier("accountController")
public class AccountController {

	//@Autowired
	@ServiceActivator
	private AccountService accountService;
	
	public void create() {
		org.openinfinity.integration.service.activator.stubs.Account account = createAccount();
		accountService.store(account);
	}
	
	private org.openinfinity.integration.service.activator.stubs.Account createAccount() {
		org.openinfinity.integration.service.activator.stubs.Account expected = new org.openinfinity.integration.service.activator.stubs.Account();
//		expected.setAddress("testaddress");
//		expected.setEmail("test@address.com");
//		expected.setName("testname");
//		expected.setNumber("358402233222");
//		expected.setOrganizationId("testorganizaation");
//		expected.setPassword("testpassword");
//		expected.setSsn("testssn");
		return expected;
	}
	
}
