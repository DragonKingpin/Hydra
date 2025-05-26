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

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.i128.GUID128;
import com.pinecone.ulf.util.guid.i128.UUID128;

public enum UuidNamespace {

	/**
	 * Name space to be used when the name string is a fully-qualified domain name.
	 */
	NAMESPACE_DNS(new UUID128(0x6ba7b8109dad11d1L, 0x80b400c04fd430c8L)),
	/**
	 * Name space to be used when the name string is a URL.
	 */
	NAMESPACE_URL(new UUID128(0x6ba7b8119dad11d1L, 0x80b400c04fd430c8L)),
	/**
	 * Name space to be used when the name string is an ISO OID.
	 */
	NAMESPACE_OID(new UUID128(0x6ba7b8129dad11d1L, 0x80b400c04fd430c8L)),
	/**
	 * Name space to be used when the name string is an X.500 DN (DER or text).
	 */
	NAMESPACE_X500(new UUID128(0x6ba7b8149dad11d1L, 0x80b400c04fd430c8L));

	private final GUID128 value;

	UuidNamespace(GUID128 value) {
		this.value = value;
	}

	/**
	 * Get the UUID value
	 * 
	 * @return a UUID
	 */
	public GUID128 getValue() {
		return this.value;
	}

	/**
	 * Get the enum value.
	 * 
	 * @param value a UUID.
	 * @return the enum
	 */
	public static UuidNamespace getNamespace(GUID value) {
		for (UuidNamespace namespace : UuidNamespace.values()) {
			if (namespace.getValue().equals(value)) {
				return namespace;
			}
		}
		return null;
	}
}
