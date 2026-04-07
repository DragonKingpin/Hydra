/*
 * MIT License
 * 
 * Copyright (c) 2018-2025 Fabio Lima
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.pinecone.ulf.util.guid.i128.enums;

public enum UuidLocalDomain {

	/**
	 * The principal domain, interpreted as POSIX UID domain on POSIX systems.
	 */
	LOCAL_DOMAIN_PERSON((byte) 0),
	/**
	 * The group domain, interpreted as POSIX GID domain on POSIX systems.
	 */
	LOCAL_DOMAIN_GROUP((byte) 1),
	/**
	 * The organization domain, site-defined.
	 */
	LOCAL_DOMAIN_ORG((byte) 2);

	private final byte value;

	UuidLocalDomain(byte value) {
		this.value = value;
	}

	/**
	 * Get the byte value.
	 * 
	 * @return a byte
	 */
	public byte getValue() {
		return this.value;
	}

	/**
	 * Get the enum value.
	 * 
	 * @param value a byte.
	 * @return the enum
	 */
	public static UuidLocalDomain getLocalDomain(byte value) {
		for (UuidLocalDomain domain : UuidLocalDomain.values()) {
			if (domain.getValue() == value) {
				return domain;
			}
		}
		return null;
	}
}
