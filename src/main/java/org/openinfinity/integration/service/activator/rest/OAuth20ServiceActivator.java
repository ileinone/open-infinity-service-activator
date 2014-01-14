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
package org.openinfinity.integration.service.activator.rest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openinfinity.core.exception.ApplicationException;
import org.openinfinity.core.exception.BusinessViolationException;
import org.openinfinity.core.exception.SystemException;
import org.openinfinity.integration.message.MessageHeader;
import org.openinfinity.integration.message.ServiceRequestMessage;
import org.openinfinity.integration.message.ServiceResponseMessage;
import org.openinfinity.integration.service.activator.CRUDOperations;
import org.openinfinity.integration.service.activator.ServiceActivator;
import org.openinfinity.integration.service.activator.proxy.ServiceActivatorInvocationHandler;
import org.openinfinity.integration.service.mapper.InterfaceMethodToOperationMapper;
import org.openinfinity.integration.service.mapper.InterfaceToLocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

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
@Qualifier("oAuth20ServiceActivator")
@Scope("prototype")
public class OAuth20ServiceActivator implements ServiceActivator {

	@Autowired
	private InterfaceToLocationMapper interfaceToLocationMapper;

	@Autowired
	private InterfaceMethodToOperationMapper interfaceMethodToOperationMapper; 
	
	private String identifierField;
	
	@Override
	public ServiceActivator getServiceActivator() {
		return this;
	}

	@Override
	public <RequestMessage, ResponseMessage> ResponseMessage activate(final RequestMessage requestMessage) throws BusinessViolationException, SystemException, ApplicationException {
		ServiceActivatorUtil.activateAndCatchOnException(new Activator() {
			@Override
			public <T extends Object> T activate() {
				OAuth2RestTemplate restTemplate = initializeRestClient();
				return executeRestOperationAndMapURL(requestMessage, restTemplate);
			}
		}, new ExceptionCallback() {
			@Override
			public void callback(Throwable throwable) {
				throw new SystemException("Method not defined for rest object or id is missing.");	
			}
		});
		return null;
	}

	private <RequestMessage, ResponseMessage extends Object> ResponseMessage executeRestOperationAndMapURL(RequestMessage requestMessage, OAuth2RestTemplate restTemplate) {
		// Fetching the method information from the service request object
		ServiceRequestMessage<RequestMessage> serviceRequestMessage = (ServiceRequestMessage<RequestMessage>)requestMessage;
		MessageHeader<Method> methodHeader = (MessageHeader<Method>) serviceRequestMessage.getMessageHeaderByName("method");
		Method method = methodHeader.getValue();
		Class<?> returnType = method.getReturnType();
		
		MessageHeader<Object> messageHeaderForProxyInterface = (MessageHeader<Object>)serviceRequestMessage.getMessageHeaderByName("proxy");
		Proxy proxyInterface =  (Proxy)messageHeaderForProxyInterface.getValue();
		ServiceActivatorInvocationHandler serviceActivatorInvocationHandler = (ServiceActivatorInvocationHandler) Proxy.getInvocationHandler(proxyInterface);
		
		String baseLocation = interfaceToLocationMapper.getLocationForInterface(serviceActivatorInvocationHandler.getInterfaceClass());
		Method executedMethod = methodHeader.getValue();
		String methodName = executedMethod.getName();
		String url = baseLocation;
		Object actualMessage = null;
		if (serviceRequestMessage.getPayload() instanceof Object[]) {
			Object[] payloadObjects = (Object[])serviceRequestMessage.getPayload();
			if (payloadObjects.length == 1) {
				actualMessage = payloadObjects[0];
			} else {
				actualMessage = payloadObjects;
			}
		}
		Class<?> serializableClass = null;
		try {
			serializableClass = Class.forName(serviceActivatorInvocationHandler.getInterfaceClass());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (methodName.equalsIgnoreCase(CRUDOperations.CREATE.replaceAll("/", ""))) {
			url += CRUDOperations.CREATE;
			URI uri = restTemplate.postForLocation(url, actualMessage);
			String identifier = uri.toString();
			int index = identifier.lastIndexOf("/");
			identifier = identifier.substring(index+1);
			Object response = Long.parseLong(identifier);
			//Object response = restTemplate.getForObject(uri, returnType);
			ServiceResponseMessage<?> serviceResponseMessage = new ServiceResponseMessage<Object>(response);
			return (ResponseMessage)serviceResponseMessage;
		} else if (methodName.equalsIgnoreCase(CRUDOperations.UPDATE.replaceAll("/", ""))) {
			url += CRUDOperations.UPDATE;
			restTemplate.put(url, serviceRequestMessage.getPayload());
			ServiceResponseMessage<?> serviceResponseMessage = new ServiceResponseMessage<Object>(null);
			return (ResponseMessage)serviceResponseMessage;
		} else if (methodName.equalsIgnoreCase(CRUDOperations.LOAD_ALL.replaceAll("/", ""))) {
			url += CRUDOperations.LOAD_ALL;
			Object[] responseArray = restTemplate.getForObject(url, Object[].class);
			Collection<Object> responseObjectCollection = new ArrayList<Object>();
			for (Object o : responseArray) responseObjectCollection.add(o);
			ServiceResponseMessage<?> serviceResponseMessage = new ServiceResponseMessage<Object>(responseObjectCollection);
			return (ResponseMessage)serviceResponseMessage;
		} else if (methodName.equalsIgnoreCase(CRUDOperations.LOAD_BY_ID)) {
			url += CRUDOperations.LOAD_BY_ID + "/" + findIdentifier(serviceRequestMessage.getPayload());
			Object response = restTemplate.getForObject(url, returnType);
			ServiceResponseMessage<?> serviceResponseMessage = new ServiceResponseMessage<Object>(response);
			return (ResponseMessage)serviceResponseMessage;
		} else if (methodName.equalsIgnoreCase(CRUDOperations.DELETE)) {
			url += CRUDOperations.DELETE + "/" + findIdentifier(serializableClass);
			restTemplate.delete(url);	
			ServiceResponseMessage<?> serviceResponseMessage = new ServiceResponseMessage<Object>(null);
			return (ResponseMessage)serviceResponseMessage;
		}
		throw new SystemException("Method not defined for rest object or id is missing.");	
	}

	private OAuth2RestTemplate initializeRestClient() {
		// Setting up the RestTemplate
		AuthorizationCodeResourceDetails authorizationCodeResourceDetails = new AuthorizationCodeResourceDetails();	
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(authorizationCodeResourceDetails);
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new MappingJacksonHttpMessageConverter()); 
		restTemplate.setMessageConverters(converters);
		return restTemplate;
	}
	
	private String findIdentifier(Object payload) {
		String identifier = "id";
		if (identifierField != null) identifier = identifierField;
		Field stringField = ReflectionUtils.findField(String.class, identifier);
		if (stringField != null) return getValueForField(stringField, payload).toString();
		Field integerField = ReflectionUtils.findField(Integer.class, identifier);
		if (integerField != null) return getValueForField(integerField, payload).toString();
		Field longField = ReflectionUtils.findField(Long.class, identifier);
		if (longField != null) return getValueForField(longField, payload).toString();
		return null;
	}
	
	private Object getValueForField(Field field, Object payload) {
		if (field != null && !field.isAccessible()) {
			field.setAccessible(Boolean.TRUE);
			try {
				// TODO implement a cache.
				return field.get(payload);
			} catch (IllegalArgumentException e) {
				// TODO log to a file
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO log to a file
				e.printStackTrace();
			}
		}
		return null;
	}

}
