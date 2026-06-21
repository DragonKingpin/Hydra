package com.pinecone.hydra.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class UniformHashing {
    private static final ThreadLocal<MessageDigest> SHA256_DIGEST = ThreadLocal.withInitial( () -> {
        try {
            return MessageDigest.getInstance( "SHA-256" );
        } catch ( NoSuchAlgorithmException exception ) {
            throw new IllegalStateException( "SHA-256 algorithm is unavailable.", exception );
        }
    } );

    private UniformHashing() {
    }

    public static String sha256Hex( String text ) {
        return UniformHashing.sha256Hex( text.getBytes( StandardCharsets.UTF_8 ) );
    }

    public static String sha256Hex( byte[] bytes ) {
        MessageDigest digest = SHA256_DIGEST.get();
        digest.reset();
        return UniformHashing.hex( digest.digest( bytes ) );
    }

    public static String hex( byte[] bytes ) {
        StringBuilder builder = new StringBuilder( bytes.length * 2 );
        for ( byte value : bytes ) {
            builder.append( Character.forDigit( ( value >> 4 ) & 0xf, 16 ) );
            builder.append( Character.forDigit( value & 0xf, 16 ) );
        }
        return builder.toString();
    }
}
