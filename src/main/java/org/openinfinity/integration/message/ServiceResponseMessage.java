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
package org.openinfinity.integration.message;

import java.util.ArrayList;
import java.util.Collection;

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
public class ServiceResponseMessage<Payload> extends AbstractServiceMessage<Payload> {

	private Collection<ErrorHeader<?>> errorHeaders = new ArrayList<ErrorHeader<?>>();;
	
	
	public ServiceResponseMessage(Payload payload) {
		super.setPayload(payload);
	}


	public Collection<ErrorHeader<?>> getErrorHeaders() {
		return errorHeaders;
	}


	public void addErrorHeader(ErrorHeader<?> errorHeader) {
		this.errorHeaders.add(errorHeader);
	}


	public boolean containsErrors() {
		return this.errorHeaders.size()>0?Boolean.TRUE:Boolean.FALSE;
	}
	
}
