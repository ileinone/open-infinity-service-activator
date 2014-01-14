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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

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
public class InterfaceToLocationMapperInMemoryImpl implements InterfaceToLocationMapper {

	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(Boolean.TRUE);
	private ReadLock readLock = readWriteLock.readLock();
	private WriteLock writeLock = readWriteLock.writeLock();
	
	private Map<String, String> locationToInterfaceMap = new HashMap<String, String>();
	
	public void setLocationToInterfaceMap(
			Map<String, String> locationToInterfaceMap) {
		this.locationToInterfaceMap = locationToInterfaceMap;
	}

	@Override
	public void addLocationToInterface(String clazz, String location) {
		try {
			writeLock.lock();
			if (locationToInterfaceMap.containsKey(clazz)) {
				ExceptionUtil.throwSystemException("Interface class [" + clazz + "] allready mapped to location [" + locationToInterfaceMap.get(clazz) + "]");
			}
			locationToInterfaceMap.put(clazz, location);
		} finally {
			writeLock.unlock();
		}
		
	}

	@Override
	public String getLocationForInterface(String clazz) {
		try {
			readLock.lock();
			if (! locationToInterfaceMap.containsKey(clazz)) {
				ExceptionUtil.throwSystemException("Interface class [" + clazz + "] not mapped to any location.");
			}
			return locationToInterfaceMap.get(clazz);
		} finally {
			readLock.unlock();
		}
	}

}
