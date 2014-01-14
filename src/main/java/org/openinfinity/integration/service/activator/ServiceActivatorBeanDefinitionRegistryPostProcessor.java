package org.openinfinity.integration.service.activator;

import java.lang.reflect.Proxy;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.ClassUtils;

public class ServiceActivatorBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private ServiceActivatorRegistry<Proxy> serviceActivatorRegistry;
	
	public void setServiceActivatorRegistry(ServiceActivatorRegistry<Proxy> serviceActivatorRegistry) {
		this.serviceActivatorRegistry = serviceActivatorRegistry;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		if (serviceActivatorRegistry != null) {
			for (Proxy proxy : serviceActivatorRegistry.loadAll()) {
				//Object dynamicProxy = getTargetObject(proxy);
				Class<?>[] interfaces = ClassUtils.getAllInterfaces(proxy);
				for (Class<?> interfaceFromProxy : interfaces) {
					BeanDefinition beanDefinition = new RootBeanDefinition(interfaceFromProxy.getClass().getName());
					System.out.println("######################## Adding new bean definition: " + interfaceFromProxy.getClass().getName());
					beanDefinitionRegistry.registerBeanDefinition(interfaceFromProxy.getClass().getName(), beanDefinition);	
				}
			} 
		}
	}
	
	@SuppressWarnings({"unchecked"})
	protected <T> T getTargetObject(Object proxy, Class<T> targetClass) throws Exception {
	  if (AopUtils.isJdkDynamicProxy(proxy)) {
	    return (T) ((Advised)proxy).getTargetSource().getTarget();
	  } else {
	    return (T) proxy;
	  }
	}
	
}
