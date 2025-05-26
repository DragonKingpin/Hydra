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

package com.pinecone.ulf.util.guid.i128.codec;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.i128.GUID128;
import com.pinecone.ulf.util.guid.i128.exception.InvalidUuidException;

import java.util.UUID;

/**
 * Interface to be implemented by all codecs of this package.
 * <p>
 * All implementations of this interface throw {@link InvalidUuidException} if
 * an invalid argument argument is given.
 * <p>
 * The {@link RuntimeException} cases that can be detected beforehand are
 * translated into an {@link InvalidUuidException}.
 * 
 * @param <T> the type encoded to and decoded from.
 * @see InvalidUuidException
 */
public interface GuidCodec<T> {

	/**
	 * Get a generic type from a UUID.
	 * 
	 * @param uuid a UUID
	 * @return a generic type
	 * @throws InvalidUuidException if the argument is invalid
	 */
	T encode(GUID128 uuid);

	/**
	 * Get a UUID from a generic type.
	 * 
	 * @param type a generic type
	 * @return a UUID
	 * @throws InvalidUuidException if the argument is invalid
	 */
	GUID128 decode(T type);
}