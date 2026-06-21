package com.pinecone.hydra.storage.file.transfer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UofsTransferPathHasher {
    public String hash( String path ) {
        try {
            MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
            byte[] bytes = digest.digest( path.getBytes( StandardCharsets.UTF_8 ) );
            StringBuilder builder = new StringBuilder( bytes.length * 2 );
            for ( byte value : bytes ) {
                builder.append( String.format( "%02x", value ) );
            }
            return builder.toString();
        }
        catch ( NoSuchAlgorithmException e ) {
            throw new IllegalStateException( "SHA-256 digest is not available.", e );
        }
    }
}
