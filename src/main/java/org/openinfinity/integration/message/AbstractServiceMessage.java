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

import org.openinfinity.core.exception.SystemException;
import org.openinfinity.core.util.ExceptionUtil;

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
public abstract class AbstractServiceMessage<Payload> {

	private static final String EXCEPTION_MESSAGE_GIVEN_NAME_DOES_NOT_EXIST = "Given name does not exist.";
	private static final String EXCEPTION_MESSAGE_HEADER_DOES_NOT_EXIST = "Message header does not exist: ";
	private static final String EXCEPTION_MESSAGE_HEADER_ALLREADY_ADDED = "Message header allready added.";
	private Payload payload;
	private Collection<Header<?>> messageHeaders = new ArrayList<Header<?>>();;
	
	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public Collection<Header<?>> getMessageHeaders() {
		return messageHeaders;
	}

	public void addMessageHeader(Header<?> messageHeader) throws SystemException {
		if(messageHeaders.contains(messageHeader)) {
			throw new SystemException(EXCEPTION_MESSAGE_HEADER_ALLREADY_ADDED);
		}
		this.messageHeaders.add(messageHeader);
	}
	
	public Header<?> getMessageHeaderByName(String name) throws SystemException {
		for(Header<?> header : messageHeaders) {
			if(header.getName().equalsIgnoreCase(name)) {
				return header;
			}
		}
		ExceptionUtil.throwSystemException(EXCEPTION_MESSAGE_HEADER_DOES_NOT_EXIST + name, new IllegalArgumentException(EXCEPTION_MESSAGE_GIVEN_NAME_DOES_NOT_EXIST));
		// Compiler does not understand that exception is thrown in the earlier method call.
		return null;
	}
	
}
