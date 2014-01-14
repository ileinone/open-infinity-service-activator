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

import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.SystemException;
import org.openinfinity.core.util.ExceptionUtil;
import org.openinfinity.integration.message.MessageHeader;
import org.openinfinity.integration.message.ServiceRequestMessage;
import org.openinfinity.integration.message.ServiceResponseMessage;
import org.openinfinity.integration.service.activator.ServiceActivator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
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
@Scope("singleton")
@Qualifier("serviceActivatorImpl")
public class ServiceActivatorImpl implements ServiceActivator {

	public ServiceActivator getServiceActivator() {
		return this;
	}

	public <T, V extends Object> V activate(T serviceRequestMessage)
			throws BusinessViolationException, SystemException,
			ApplicationException {
		System.out.println("ServiceActivatorImpl started: " + System.currentTimeMillis());
		
		if (!(serviceRequestMessage instanceof ServiceRequestMessage<?>)) {
			ExceptionUtil
					.throwSystemException("Instance not type of org.wide.engine.integration.message.ServiceRequestMessage, actual: "
							+ serviceRequestMessage.getClass().getName());
		}
		ServiceRequestMessage<Object[]> objects = (ServiceRequestMessage<Object[]>) serviceRequestMessage;
		if (objects.getPayload() != null) {
			for (Object o : objects.getPayload()) {
				System.out.println("message was: " + o.toString());
			}
		}
		String replyMessage = "<xml>This echo message from the service</xml>";
		MessageHeader<String> messageHeader = new MessageHeader<String>(
				"STATUS", "OK");
		ServiceResponseMessage<String> serviceResponseMessage = new ServiceResponseMessage<String>(
				replyMessage);
		serviceResponseMessage.addMessageHeader(messageHeader);
		
		System.out.println("ServiceActivatorImpl finished: " + System.currentTimeMillis());
		
		return (V) serviceResponseMessage;
	}

}
