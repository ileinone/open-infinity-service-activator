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

/**
 * This class is responsible for:
 * <ul>
 * <li>for working as a passthrough mapper without serilization gateway.</li>
 * </ul>
 *
 * @author Ilkka Leinonen
 * @version 1.0.0.RELEASE
 * @since 1.0.0.RELEASE
 */
public class DefaultServiceObjectMapper extends AbstractServiceObjectMapper {
	
	
	public DefaultServiceObjectMapper() {}
	
	public DefaultServiceObjectMapper(Object serviceRequestMessage) {
		setServiceRequestMessage(serviceRequestMessage);
		setSerializedRequestMessage(serviceRequestMessage);
	}
	
	public DefaultServiceObjectMapper(Object serviceRequestMessage, Object serviceReplyMessage) {
		setServiceRequestMessage(serviceRequestMessage);
		setSerializedRequestMessage(serviceRequestMessage);
		setSerializableReplyMessage(serviceReplyMessage);
		setServiceReplyMessage(serviceReplyMessage);
	}

	@Override
	public <T> void setSerializedRequestMessage(T serializedRequestMessage) {
		super.setSerializedRequestMessage(serializedRequestMessage);
	}

	@Override
	public <T> T getSerializableReplyMessage() {
		return getServiceReplyMessage();
	}

	@Override
	public <T> T getSerializableRequestMessage() {
		return getServiceRequestMessage();
	}

	@Override
	public <T> void setSerializableReplyMessage(T serializableReplyMessage) {
		setServiceReplyMessage(serializableReplyMessage);
	}

	
	
/*	public void mapRequest() throws SystemException {
		// TODO aseta ServiceRequestMessage sellaisenaan SerializableRequestMessageen, ja poista turha logiikka ServiceActivatorCommandImpl luokasta
		// sama mapResponseen.
	//	super.serviceRequestMessage = serviceRequestMessage;
	}
*/
	/*public <T> void mapResponse(T serializableReplyMessage) throws SystemException {
		
	}*/
	
}
