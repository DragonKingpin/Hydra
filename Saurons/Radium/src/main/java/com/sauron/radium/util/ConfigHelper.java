package com.sauron.radium.util;

import com.pinecone.framework.system.functions.Function;

public class ConfigHelper {
    public static final Function fnToSmallHumpName = (Object... arg )->{
        return ConfigHelper.toSmallHumpName( arg[0] );
    };

    public static final Function fnToBigHumpName = (Object... arg )->{
        return ConfigHelper.toBigHumpName( arg[0] );
    };


    public static String toSmallHumpName( String sz ) {
        StringBuilder sb = new StringBuilder();
        sb.append( sz );
        sb.setCharAt( 0, Character.toLowerCase( sb.charAt(0) ) );
        return sb.toString();
    }

    public static String toBigHumpName( String sz ) {
        StringBuilder sb = new StringBuilder();
        sb.append( sz );
        sb.setCharAt( 0, Character.toUpperCase( sb.charAt(0) ) );
        return sb.toString();
    }

    public static String toSmallHumpName( Object sz ) {
        return ConfigHelper.toSmallHumpName( (String) sz );
    }

    public static String toBigHumpName( Object sz ) {
        return ConfigHelper.toBigHumpName( (String) sz );
    }
}
