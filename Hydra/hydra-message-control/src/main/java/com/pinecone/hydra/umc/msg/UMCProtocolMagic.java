package com.pinecone.hydra.umc.msg;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.IOException;

public final class UMCProtocolMagic {
    public static final int MAGIC_SIZE = 3;
    public static final int FAMILY_AT = 0;
    public static final int VARIANT_AT = 1;
    public static final int VERSION_AT = 2;

    public static final byte FAMILY_UNIFORM_MESSAGE = 0x55; // 'U' -> Uniform

    public static final byte VARIANT_UMC  = 0x01;
    public static final byte VARIANT_UMCC = 0x02;
    public static final byte VARIANT_UMBP = 0x03;

    public static final byte VERSION_1 = 0x01;

    public static final byte[] UMC_V1 = new byte[]{
            FAMILY_UNIFORM_MESSAGE, VARIANT_UMC, VERSION_1
    };

    public static final byte[] UMCC_V1 = new byte[]{
            FAMILY_UNIFORM_MESSAGE, VARIANT_UMCC, VERSION_1
    };

    public static final byte[] UMBP_V1 = new byte[]{
            FAMILY_UNIFORM_MESSAGE, VARIANT_UMBP, VERSION_1
    };

    private UMCProtocolMagic() {}

    public static int length( byte[] magic ) {
        return magic.length;
    }

    public static void requireReadable( byte[] buf ) throws UMCProtocolMagicException {
        if ( buf == null ) {
            throw new UMCProtocolMagicException( "UMC protocol magic buffer is null." );
        }
        if ( buf.length < MAGIC_SIZE ) {
            throw new UMCProtocolMagicException(
                    "UMC protocol magic requires " + MAGIC_SIZE + " bytes, but only " + buf.length + " bytes found."
            );
        }
    }

    public static byte familyOf( byte[] buf ) throws UMCProtocolMagicException {
        requireReadable( buf );
        return buf[ FAMILY_AT ];
    }

    public static byte variantOf( byte[] buf ) throws UMCProtocolMagicException {
        requireReadable( buf );
        return buf[ VARIANT_AT ];
    }

    public static byte versionOf( byte[] buf ) throws UMCProtocolMagicException {
        requireReadable( buf );
        return buf[ VERSION_AT ];
    }

    public static boolean isUniformMessage( byte[] buf ) throws UMCProtocolMagicException {
        return familyOf( buf ) == FAMILY_UNIFORM_MESSAGE;
    }

    public static byte[] copyOf( byte[] magic ) {
        return Arrays.copyOf( magic, magic.length );
    }

    public static boolean matches( byte[] buf, byte[] magic ) {
        return buf.length >= magic.length && Arrays.equals( buf, 0, magic.length, magic, 0, magic.length );
    }

    public static void put( ByteBuffer byteBuffer, byte[] magic ) {
        byteBuffer.put( magic );
    }

    public static String stringify( byte[] magic ) {
        StringBuilder builder = new StringBuilder();
        for ( int i = 0; i < magic.length; ++i ) {
            if ( i > 0 ) {
                builder.append( ' ' );
            }
            builder.append( String.format( "%02X", magic[i] ) );
        }
        return builder.toString();
    }
}
