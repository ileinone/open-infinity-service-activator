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

import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.SystemException;

/**
 * This class is responsible for:
 * <ul>
 * <li>s to hide the implementation of the actual service call with specific protocol.</li>
 * </ul>
 *
 * @author Ilkka Leinonen
 * @version 1.0.0.RELEASE
 * @since 1.0.0.RELEASE
 */
public interface ServiceActivator {
	
	/**
	 * Method is a getter for the actual implementation or proxy instance of the <code>org.hicon.wide.engine.integration.activator.ServiceActivator</code> interface.
	 * 
	 * @return <code>org.hicon.wide.engine.integration.activator.ServiceActivator</code> instance.
	 */
	public ServiceActivator getServiceActivator();
	
	/**
	 * Method handles the call to actual service.
	 * 
	 * @param serviceRequestMessage Represents the request wrapper message including message headers and the payload.
	 * @return serviceResponseMessage Represents the response wrapper message including message headers, errors and payload.
	 * @throws BusinessViolationException Represents exception thrown by the business rules.
	 * @throws SystemException Represents exception thrown by the system.
	 * @throws ApplicationException Represents exception thrown by the invalid parameters.
	 */
	public <ServiceRequestMessage, ServiceResponseMessage extends Object> ServiceResponseMessage activate(ServiceRequestMessage serviceRequestMessage) throws BusinessViolationException, SystemException, ApplicationException;

}
