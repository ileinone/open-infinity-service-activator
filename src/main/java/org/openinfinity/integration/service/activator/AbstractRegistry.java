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

import java.util.ArrayList;
import java.util.Collection;
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
public abstract class AbstractRegistry<T extends Object> implements Registry<T> {

	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(Boolean.TRUE);
	private ReadLock readLock = readWriteLock.readLock();
	private WriteLock writeLock = readWriteLock.writeLock();
	
	private ArrayList<T> cachedObjects = new ArrayList<T>();
		
	public void setServiceActivators(ArrayList<T> cachedObjects) {
		try {
			writeLock.lock();
			this.cachedObjects = cachedObjects;
		} finally {
			writeLock.unlock();
		}
	}
	
	public int getSize() {
		try {
			readLock.lock();
			return this.cachedObjects.size();
		} finally {
			readLock.unlock();
		}
	}

	public T load(String className) {
		try {
			readLock.lock();
			for(T cachedObject : cachedObjects) {
				if(className.equalsIgnoreCase(cachedObject.getClass().getName())) {
					return cachedObject;
				}
			}
			ExceptionUtil.throwSystemException("Object does not exist in the memory:" + className);
			return null;
		} finally {
			readLock.unlock();
		}
	}

	public Collection<T> loadAll() {
		try {
			readLock.lock();
			return cachedObjects;
		} finally {
			readLock.unlock();
		}
	}

	public void store(T cachedObject) {
		try {
			writeLock.lock();
			boolean containsObject = false;
			for(T co : cachedObjects) {
				if(cachedObject.getClass().getName().equalsIgnoreCase(co.getClass().getName())) {
					containsObject = true;
					ExceptionUtil.throwSystemException("Memory contains allready object: " + cachedObject.getClass().getName());
				}
			}
			if(!containsObject) {
				this.cachedObjects.add(cachedObject);
			}
		} finally {
			writeLock.unlock();
		}
	}

}
