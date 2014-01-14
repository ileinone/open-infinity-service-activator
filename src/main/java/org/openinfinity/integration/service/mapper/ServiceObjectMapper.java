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
import org.openinfinity.core.exception.BusinessViolationException;
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
public interface ServiceObjectMapper {

    void validateRequest() throws ApplicationException;

    void validateResponse() throws BusinessViolationException;

    void mapRequest() throws SystemException;

    <T extends Object> void mapResponse(T serializableReplyMessage) throws SystemException;

    <T extends Object> T getServiceRequestMessage();

    <T extends Object> void setServiceRequestMessage(T serviceRequestMessage);

    <T extends Object> T getServiceReplyMessage();

    <T extends Object> void setServiceReplyMessage(T serviceReplyMessage);

    <T extends Object> T getSerializableRequestMessage();

    <T extends Object> void setSerializedRequestMessage(T serializableRequestMessage);

    <T extends Object> T getSerializableReplyMessage();

    <T extends Object> void setSerializableReplyMessage(T serializableReplyMessage);

}
