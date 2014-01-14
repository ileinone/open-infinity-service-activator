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
package org.openinfinity.integration.command;

import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.SystemException;
import org.openinfinity.integration.message.serializer.DefaultMessageSerializer;
import org.openinfinity.integration.message.serializer.MessageSerializer;
import org.openinfinity.integration.service.activator.ServiceActivator;
import org.openinfinity.integration.service.mapper.DefaultExceptionMapper;
import org.openinfinity.integration.service.mapper.ExceptionMapper;
import org.openinfinity.integration.service.mapper.ServiceObjectMapper;

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
public class ServiceActivatorCommandImpl implements ServiceActivatorCommand {

    private ServiceObjectMapper serviceObjectMapper;
    private ServiceActivator serviceActivator;
    private MessageSerializer messageSerializer;
    private ExceptionMapper exceptionMapper;
    
    /**
     * Constructor with <code>org.openinfinity.integration.command.ServiceActivatorImpl</code>.
     * 
     * @param serviceActivator 
     */
    public ServiceActivatorCommandImpl(ServiceActivator serviceActivator, ServiceObjectMapper serviceObjectMapper) {
    	this(serviceActivator, serviceObjectMapper, new DefaultMessageSerializer(), new DefaultExceptionMapper());
    }
    
    public ServiceActivatorCommandImpl(ServiceActivator serviceActivator, ServiceObjectMapper serviceObjectMapper, ExceptionMapper exceptionMapper) {
     	this(serviceActivator, serviceObjectMapper, new DefaultMessageSerializer(), exceptionMapper);
    }
	
	public ServiceActivatorCommandImpl(ServiceActivator serviceActivator, ServiceObjectMapper serviceObjectMapper, MessageSerializer messageSerializer) {
	 	this(serviceActivator, serviceObjectMapper, new DefaultMessageSerializer(), new DefaultExceptionMapper());
	}

	public ServiceActivatorCommandImpl(ServiceActivator serviceActivator, ServiceObjectMapper serviceObjectMapper, MessageSerializer messageSerializer, ExceptionMapper exceptionMapper) {
        this.serviceObjectMapper = serviceObjectMapper;
        this.serviceActivator = serviceActivator;
        this.messageSerializer = messageSerializer;
        this.exceptionMapper = exceptionMapper;
    }

	/**
	 * Executes business logic by using specific <code>org.wide.engine.integration.activator.ServiceActivator</code>.
	 */
    public <T extends Object> T execute() throws BusinessViolationException, SystemException, ApplicationException {
        try {
        	validateAndMapRequest();
        	Object serializableRequestMessage = serviceObjectMapper.getSerializableRequestMessage();
        	Object serviceRequestMessage = messageSerializer.serialize(serializableRequestMessage);
            Object serviceReplyMessage = serviceActivator.activate(serviceRequestMessage);
        	Object serializableReplyMessage = this.messageSerializer.deSerialize(serviceReplyMessage);
        	mapAndValidateResponse(serializableReplyMessage);
            return this.serviceObjectMapper.getServiceReplyMessage();
        } catch(Throwable throwable) {
        	mapException(throwable);
        }       
		return null;
    }

	private void validateAndMapRequest() {
		if(serviceObjectMapper != null && serviceObjectMapper.getServiceReplyMessage() != null) {
			serviceObjectMapper.validateRequest();
			/*if(serviceObjectMapper.getSerializableRequestMessage() == null) {
				ExceptionUtil.throwApplicationException("Serializable request message is null.");
			}*/
			serviceObjectMapper.mapRequest();
		}
		
	}

	private void mapAndValidateResponse(Object serializableReplyMessage) {
		if(serviceObjectMapper != null && serializableReplyMessage != null) {
			serviceObjectMapper.mapResponse(serializableReplyMessage);
			serviceObjectMapper.validateResponse();
		}
	}
	
	private void mapException(Throwable throwable) throws BusinessViolationException, SystemException, ApplicationException {
		if(exceptionMapper != null) {
			exceptionMapper.mapException(throwable);
		} 
	}
	
}
