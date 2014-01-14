package org.openinfinity.integration.service.activator;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class OpenInfinityBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements Ordered {

	private ServiceActivatorRegistry<Proxy> serviceActivatorRegistry;
	
	private int order = HIGHEST_PRECEDENCE;
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public void setServiceActivatorRegistry(ServiceActivatorRegistry<Proxy> serviceActivatorRegistry) {
		Assert.notNull(serviceActivatorRegistry,"Please define service activator registry.");
		this.serviceActivatorRegistry = serviceActivatorRegistry;
	}

	@Override
	public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {	
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				if (org.openinfinity.integration.annotation.ServiceActivator.class != null && 
					field.isAnnotationPresent(org.openinfinity.integration.annotation.ServiceActivator.class) ||
					(Autowired.class != null && field.isAnnotationPresent(Autowired.class))) {
					for (Proxy serviceActivator : serviceActivatorRegistry.loadAll()){
						autowire(bean, field, serviceActivator);
					}
                }
			}
		});
		return true;
	}
	
	private void autowire(final Object bean, Field field, Object serviceActivator) throws IllegalAccessException {
		Class<?>[] interfaces = ClassUtils.getAllInterfaces(serviceActivator);
		for (Class<?> interfaceFromProxy : interfaces) {
			System.out.println("interfaceFromProxy.getName(): " + interfaceFromProxy.getName() + " field.getType().getName(): " + field.getType().getName());
			if (interfaceFromProxy.getName().equals(field.getType().getName())) {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				field.set(bean, serviceActivator);
				break;
			}
		}
	}
	
	@Override
	public int getOrder() {
		return order;
	}

}
