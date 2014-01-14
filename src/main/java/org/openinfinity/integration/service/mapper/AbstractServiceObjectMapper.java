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
package org.openinfinity.integration.service.mapper;

import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.SystemException;

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
public abstract class AbstractServiceObjectMapper implements ServiceObjectMapper {

	protected Object 
	serviceRequestMessage, 
	serviceReplyMessage,
	serializableRequestMessage,
	serializableReplyMessage;
	
	
	public <T> T getServiceRequestMessage() {
		return (T)serializableRequestMessage;
	}

	public <T> T getServiceReplyMessage() {
		return (T)serializableReplyMessage;
	}
	
	public <T> T getSerializableReplyMessage() {
		return (T) serializableReplyMessage;
	}

	public <T> T getSerializableRequestMessage() {
		return (T) serializableRequestMessage;
	}

	public void mapRequest() throws SystemException {
	}
	
	public void validateRequest() throws ApplicationException {
	}

	public void validateResponse() throws ApplicationException {
	}

	public <T> void setServiceRequestMessage(T serviceRequestMessage) {
		this.serviceRequestMessage = serviceRequestMessage;
	}
	
	public <T> void mapResponse(T serializableReplyMessage) throws SystemException {
		this.serializableReplyMessage = serializableReplyMessage;
	}

	public <T> void setSerializableReplyMessage(T serializableReplyMessage) {
		this.serializableReplyMessage = serviceReplyMessage;
	}

	public <T> void setSerializedRequestMessage(T serializableRequestMessage) {		
		this.serializableRequestMessage = serviceRequestMessage;
	}
	

	public <T> void setServiceReplyMessage(T serviceReplyMessage) {		
		this.serviceReplyMessage = serviceReplyMessage;
	}
	
}
