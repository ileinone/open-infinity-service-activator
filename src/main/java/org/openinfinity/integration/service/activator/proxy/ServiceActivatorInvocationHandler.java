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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.openinfinity.integration.command.ServiceActivatorCommand;
import org.openinfinity.integration.command.ServiceActivatorCommandImpl;
import org.openinfinity.integration.message.MessageHeader;
import org.openinfinity.integration.message.ServiceRequestMessage;
import org.openinfinity.integration.message.ServiceResponseMessage;
import org.openinfinity.integration.message.serializer.DefaultMessageSerializer;
import org.openinfinity.integration.message.serializer.MessageSerializer;
import org.openinfinity.integration.service.activator.ServiceActivator;
import org.openinfinity.integration.service.mapper.DefaultExceptionMapper;
import org.openinfinity.integration.service.mapper.DefaultServiceObjectMapper;
import org.openinfinity.integration.service.mapper.ExceptionMapper;
import org.openinfinity.integration.service.mapper.ServiceObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ServiceActivatorInvocationHandler implements InvocationHandler {
//public class ServiceActivatorInvocationHandler implements InvocationHandler, Callable<Object> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceActivatorInvocationHandler.class);
	
	private ServiceActivator serviceActivator;
	private ExceptionMapper exceptionMapper;
	private MessageSerializer messageSerializer;
	private ServiceObjectMapper serviceObjectMapper;
	private String interfaceClass; 
	
	public ServiceActivatorInvocationHandler(ServiceActivator serviceActivator, String interfaceClass) {
		this(serviceActivator, new DefaultServiceObjectMapper(), new DefaultExceptionMapper(), new DefaultMessageSerializer(), interfaceClass);
	}

	public ServiceActivatorInvocationHandler(ServiceActivator serviceActivator, ExceptionMapper exceptionMapper, String interfaceClass) {
		this(serviceActivator, new DefaultServiceObjectMapper(), exceptionMapper, new DefaultMessageSerializer(), interfaceClass);
	}
	
	public ServiceActivatorInvocationHandler(ServiceActivator serviceActivator, ExceptionMapper exceptionMapper, ServiceObjectMapper serviceObjectMapper, String interfaceClass) {
		this(serviceActivator, serviceObjectMapper, exceptionMapper, new DefaultMessageSerializer(), interfaceClass);
	}

	public ServiceActivatorInvocationHandler(ServiceActivator serviceActivator, ExceptionMapper exceptionMapper, MessageSerializer messageSerializer, String interfaceClass) {
		this(serviceActivator, new DefaultServiceObjectMapper(), exceptionMapper, messageSerializer, interfaceClass);		
	}
	
	public ServiceActivatorInvocationHandler(ServiceActivator serviceActivator, ServiceObjectMapper serviceObjectMapper, ExceptionMapper exceptionMapper, MessageSerializer messageSerializer, String interfaceClass) {
		this.serviceActivator = serviceActivator;
		this.exceptionMapper = exceptionMapper;
		this.messageSerializer = messageSerializer;
		this.serviceObjectMapper = serviceObjectMapper;
		this.interfaceClass = interfaceClass;
	}
	
	public ServiceActivator getServiceActivator() {
		return serviceActivator;
	}

	public void setServiceActivator(ServiceActivator serviceActivator) {
		this.serviceActivator = serviceActivator;
	}
	
	public ExceptionMapper getExceptionMapper() {
		return exceptionMapper;
	}

	public void setExceptionMapper(ExceptionMapper exceptionMapper) {
		this.exceptionMapper = exceptionMapper;
	}

	public MessageSerializer getMessageSerializer() {
		return messageSerializer;
	}

	public void setMessageSerializer(MessageSerializer messageSerializer) {
		this.messageSerializer = messageSerializer;
	}

	public ServiceObjectMapper getServiceObjectMapper() {
		return serviceObjectMapper;
	}

	public void setServiceObjectMapper(ServiceObjectMapper serviceObjectMapper) {
		this.serviceObjectMapper = serviceObjectMapper;
	}
	
	public String getInterfaceClass() {
		return interfaceClass;
	}

	private Object proxyInstance;
	private Method methodInstance;
	private Object[] argsInstance;

	
//	private ThreadLocal<Object> proxyInstance = new ThreadLocal<Object>();
//	private ThreadLocal<Method> methodInstance = new ThreadLocal<Method>();
//	private ThreadLocal<Object[]> argsInstance = new ThreadLocal<Object[]>();

	public Object getProxyInstance() {
		return proxyInstance;
	}

	public Method getMethodInstance() {
		return methodInstance;
	}

	public Object[] getArgsInstance() {
		return argsInstance;
	}

	private Object cachedResponseMessage;
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		LOGGER.debug("Calling method through Service Activator: " + method.getName());
		Long startTime = System.currentTimeMillis();
		try {
//			if(cachedResponseMessage != null) {
//				System.out.println("Response found from cache.");
//				return cachedResponseMessage;
//			} else {
			System.out.println("response not found from cache");
			proxyInstance = proxy;
			methodInstance = method;
			argsInstance = args;
			//ExecutorService executorService = Executors.newCachedThreadPool();
			System.out.println("Invoking protocol handler");
			CallableService service = new CallableService(proxyInstance, methodInstance, argsInstance);
			//cachedResponseMessage = service.call();
			System.out.println("Got response back.");
			return service.call();
			//return cachedResponseMessage;
			//}
			//			final Future<Object> future = (Future<Object>)executorService.submit(this);
//			final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
//			executorService.submit(new Runnable() {
//				public void run() {
//					try {
//						threadLocal.set(future.get());
//					} catch (InterruptedException throwable) {
//						// TODO Auto-generated catch block
//						throwable.printStackTrace();
//					} catch (ExecutionException throwable) {
//						// TODO Auto-generated catch block
//						throwable.printStackTrace();
//					}
//				}
//			});
//			return threadLocal.get();
//		
			
//			final Future<Object> future = (Future<Object>)executorService.submit(this);
//			final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
//			executorService.submit(new Runnable() {
//				public void run() {
//					try {
//						threadLocal.set(future.get());
//					} catch (InterruptedException throwable) {
//						// TODO Auto-generated catch block
//						throwable.printStackTrace();
//					} catch (ExecutionException throwable) {
//						// TODO Auto-generated catch block
//						throwable.printStackTrace();
//					}
//				}
//			});
//			return threadLocal.get();
//			
		} finally {
			eraseMemory();
			LOGGER.debug("Invoking actual service took: " + (System.currentTimeMillis() - startTime) + "ms.");
		}
	}

	private void eraseMemory() {
//		proxyInstance.remove();
//		methodInstance.remove();
//		argsInstance.remove();
//		proxyInstance.set(null);
//		methodInstance.set(null);
//		argsInstance.set(null);
	}

	@SuppressWarnings("unused")
	private class CallableService {

		private Object proxy;
		private Method method;
		private Object[] args;

		@SuppressWarnings("unused")
		public CallableService(Object proxy, Method method, Object[] args) {
			this.proxy = proxy;
			this.method = method;
			this.args = args;
		}

		public Object call() throws Exception {

			// TODO siirr‰ ServiceActivatorin sis‰‰n k‰sittely.
			MessageHeader<Object> messageHeaderProxy = new MessageHeader<Object>("proxy", proxy);
			MessageHeader<Method> messageHeaderMethod = new MessageHeader<Method>("method", method);
			ServiceRequestMessage<Object[]> serviceRequestMessage = new ServiceRequestMessage<Object[]>(args);
			serviceRequestMessage.addMessageHeader(messageHeaderProxy);
			serviceRequestMessage.addMessageHeader(messageHeaderMethod);
			serviceObjectMapper.setServiceRequestMessage(serviceRequestMessage);
			serviceObjectMapper.setSerializedRequestMessage(serviceRequestMessage);
			ServiceActivatorCommand serviceActivatorCommand = new ServiceActivatorCommandImpl(serviceActivator, serviceObjectMapper, messageSerializer, exceptionMapper);
			ServiceResponseMessage<?> serviceResponseMessage = serviceActivatorCommand.execute();

			// ServiceResponseMessage<?> serviceResponseMessage =
			// serviceActivator.activate(serviceRequestMessage);
			resolveErrors(serviceResponseMessage);
			// TODO error handling in service response object
			return serviceResponseMessage.getPayload();
		}

	}

	private void resolveErrors(ServiceResponseMessage<?> serviceResponseMessage) {
		if (serviceResponseMessage.containsErrors()) {
			// TODO T‰m‰ k‰sittely pit‰‰ siirt‰‰ commandiin.
		}
	}

//	public Object call() throws Exception {
//		// TODO siirr‰ ServiceActivatorin sis‰‰n k‰sittely.
//		MessageHeader<Object> messageHeaderProxy = new MessageHeader<Object>("proxy", proxyInstance);
//		MessageHeader<Method> messageHeaderMethod = new MessageHeader<Method>("method", methodInstance);
//		ServiceRequestMessage<Object[]> serviceRequestMessage = new ServiceRequestMessage<Object[]>(argsInstance);
//		serviceRequestMessage.addMessageHeader(messageHeaderProxy);
//		serviceRequestMessage.addMessageHeader(messageHeaderMethod);
//		serviceObjectMapper.setServiceRequestMessage(serviceRequestMessage);
//		serviceObjectMapper.setSerializedRequestMessage(serviceRequestMessage);
//		ServiceActivatorCommand serviceActivatorCommand = new ServiceActivatorCommandImpl(serviceActivator, serviceObjectMapper, messageSerializer, exceptionMapper);
//		ServiceResponseMessage<?> serviceResponseMessage = serviceActivatorCommand.execute();
//
//		// ServiceResponseMessage<?> serviceResponseMessage =
//		// serviceActivator.activate(serviceRequestMessage);
//		resolveErrors(serviceResponseMessage);
//		// TODO error handling in service response object
//		return serviceResponseMessage.getPayload();
//	}
	
//	public Object invoke(Object proxy, Method method, Object[] arguments, ServiceActivator serviceActivator, ServiceObjectMapper serviceObjectMapper, MessageSerializer messageSerializer, ExceptionMapper exceptionMapper) throws Throwable {
//		LogUtil.debug(LOGGER, "Calling method through Service Activator: " + method.getName());
//		Long startTime = System.currentTimeMillis();
//		try {
//			// TODO siirr‰ ServiceActivatorin sis‰‰n k‰sittely.
//			MessageHeader<Object> messageHeaderProxy = new MessageHeader<Object>("proxy", proxy);
//			MessageHeader<Method> messageHeaderMethod = new MessageHeader<Method>("method", method);
//			ServiceRequestMessage<Object[]> serviceRequestMessage = new ServiceRequestMessage<Object[]>(arguments);
//			serviceRequestMessage.addMessageHeader(messageHeaderProxy);
//			serviceRequestMessage.addMessageHeader(messageHeaderMethod);
//			ServiceResponseMessage<?> serviceResponseMessage = serviceActivator.activate(serviceRequestMessage);
//			resolveErrors(serviceResponseMessage);
//			// TODO error handling in service response object
//			return serviceResponseMessage.getPayload();
//		} finally {
//			LogUtil.debug(LOGGER, "Invoking actual service took: " + (System.currentTimeMillis() - startTime) + "ms.");
//		}
//	}
	
	
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		LogUtil.debug(LOGGER, "Calling method through Service Activator: " + method.getName());
//		Long startTime = System.currentTimeMillis();
//		try {
//			// TODO siirr‰ ServiceActivatorin sis‰‰n k‰sittely.
//			MessageHeader<Object> messageHeaderProxy = new MessageHeader<Object>("proxy", proxy);
//			MessageHeader<Method> messageHeaderMethod = new MessageHeader<Method>("method", method);
//			ServiceRequestMessage<Object[]> serviceRequestMessage = new ServiceRequestMessage<Object[]>(args);
//			serviceRequestMessage.addMessageHeader(messageHeaderProxy);
//			serviceRequestMessage.addMessageHeader(messageHeaderMethod);
//			serviceObjectMapper.setServiceRequestMessage(serviceRequestMessage);
//			serviceObjectMapper.setSerializedRequestMessage(serviceRequestMessage);
//			ServiceActivatorCommand serviceActivatorCommand = new ServiceActivatorCommandImpl(serviceActivator, serviceObjectMapper, messageSerializer, exceptionMapper);
//			ServiceResponseMessage<?> serviceResponseMessage = serviceActivatorCommand.execute();
//			
//			//ServiceResponseMessage<?> serviceResponseMessage = serviceActivator.activate(serviceRequestMessage);
//			resolveErrors(serviceResponseMessage);
//			// TODO error handling in service response object
//			return serviceResponseMessage.getPayload();
//		} finally {
//			LogUtil.debug(LOGGER, "Invoking actual service took: " + (System.currentTimeMillis() - startTime) + "ms.");
//		}
//	}
//	
//	
//
//	private void resolveErrors(ServiceResponseMessage<?> serviceResponseMessage) {
//		if (serviceResponseMessage.containsErrors()) {
//			// TODO T‰m‰ k‰sittely pit‰‰ siirt‰‰ commandiin.
//		}
//	}

}
