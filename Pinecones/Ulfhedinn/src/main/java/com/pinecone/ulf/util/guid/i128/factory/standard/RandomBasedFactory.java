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

package com.pinecone.ulf.util.guid.i128.factory.standard;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.i128.GUID128;
import com.pinecone.ulf.util.guid.i128.enums.UuidVersion;
import com.pinecone.ulf.util.guid.i128.factory.AbstRandomBasedFactory;
import com.pinecone.ulf.util.guid.i128.util.internal.ByteUtil;

import java.util.Random;
import java.util.UUID;
import java.util.function.LongSupplier;

/**
 * Concrete factory for creating random-based unique identifiers (UUIDv4).
 */
public final class RandomBasedFactory extends AbstRandomBasedFactory {

	/**
	 * Default constructor.
	 */
	public RandomBasedFactory() {
		this(builder());
	}

	/**
	 * Constructor with a {@link Random} instance.
	 * 
	 * @param random a {@link Random} instance
	 */
	public RandomBasedFactory(Random random) {
		this(builder().withRandom(random));
	}

	/**
	 * Constructor with a function which returns random number.
	 * 
	 * @param randomSupplier a function
	 */
	public RandomBasedFactory(LongSupplier randomSupplier) {
		this(builder().withRandomFunction(randomSupplier));
	}

	private RandomBasedFactory(Builder builder) {
		super(UuidVersion.VERSION_RANDOM_BASED, builder);
	}

	/**
	 * Concrete builder for creating a random-based factory.
	 * 
	 * @see AbstRandomBasedFactory.Builder
	 */
	public static class Builder extends AbstRandomBasedFactory.Builder<RandomBasedFactory, Builder> {
		@Override
		public RandomBasedFactory build() {
			return new RandomBasedFactory(this);
		}
	}

	/**
	 * Returns a builder of random-based factory.
	 * 
	 * @return a builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Returns a random-based UUID.
	 * 
	 * ### RFC 9562 - 4.4. Algorithms for Creating a UUID from Truly Random or
	 * Pseudo-Random Numbers
	 * 
	 * (1) Set the two most significant bits (bits 6 and 7) of the
	 * clock_seq_hi_and_reserved to zero and one, respectively.
	 * 
	 * (2) Set the four most significant bits (bits 12 through 15) of the
	 * time_hi_and_version field to the 4-bit version number from Section 4.1.3.
	 * 
	 * (3) Set all the other bits to randomly (or pseudo-randomly) chosen values.
	 * 
	 * @return a random-based UUID
	 */
	@Override
	public GUID128 create() {
		lock.lock();
		try {
			if (this.random instanceof SafeRandom) {
				final byte[] bytes = this.random.nextBytes(16);
				final long msb = ByteUtil.toNumber(bytes, 0, 8);
				final long lsb = ByteUtil.toNumber(bytes, 8, 16);
				return toUuid(msb, lsb);
			} else {
				final long msb = this.random.nextLong();
				final long lsb = this.random.nextLong();
				return toUuid(msb, lsb);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public GUID nextGUID() {
		return null;
	}

	@Override
	public GUID nextGUID(Parameters parameters) {
		return null;
	}
}
