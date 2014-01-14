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
package org.openinfinity.domain.entity;

import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.Email;
import org.openinfinity.core.annotation.NotScript;

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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@JsonAutoDetect
@JsonSerialize
//@Entity
//@Table(name="ACCOUNT")
@Data
@NoArgsConstructor
public class Account {

	//@GeneratedValue
	private Long id;

	@NotNull
	@NotScript
	@Size(min = 1, max = 70)
	//@Column(name="ORGANIZATION_ID")
	private String organizationId;
	
	@NotNull
	@NotScript
	@Size(min = 1, max = 25)
	//@Column(name="SSN")
	private String ssn;
	
	@NotNull
	@NotScript
	@Size(min = 1, max = 25)
	//@Column(name="NAME")
	private String name;

	@NotNull
	@NotScript
	@Size(min = 1, max = 70)
	//@Column(name="ADDRESS")
	private String address;

	@NotNull
	@NotScript
	@Email
	@Size(min = 3, max = 40)
	//@Column(name="EMAIL")
	private String email;
	 
	@NotNull
	@NotScript
	@Size(min = 8, max = 40)
	//@Column(name="PASSWORD")
	private String password;
	
	@NotNull
	@NotScript
	@Size(min = 1, max = 25)
	//@Column(name="NUMBER")
	private String number;
	
	@JsonIgnore
	public Long assignId() {
		this.id = idSequence.incrementAndGet();
		return id;
	}

	private static final AtomicLong idSequence = new AtomicLong();
	
}