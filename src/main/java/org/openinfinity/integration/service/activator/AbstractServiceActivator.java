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

import java.util.Map;

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
public abstract class AbstractServiceActivator implements ServiceActivator{
	
	private String baseUrl;
	
	private Map<String, String> methodToOperationMapping;
	
	public String getBaseUrl() {
		return baseUrl;
	}


	public Map<String, String> getMethodToOperationMapping() {
		return methodToOperationMapping;
	}


	public void setMethodToOperationMapping(
			Map<String, String> methodToOperationMapping) {
		this.methodToOperationMapping = methodToOperationMapping;
	}


	@Override
	public ServiceActivator getServiceActivator() {
		return this;
	}
	

}