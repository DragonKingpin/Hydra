package com.pinecone.framework.util.rdb;

import com.pinecone.framework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class SQLStrings {
    public static String format( Object val, boolean bIncludeBool ) {
        String sz;
        if ( val == null ) {
            return "null";
        }
        else if ( val instanceof String ) {
            return "'" + StringUtils.addSlashes( (String) val ) + "'";
        }
        else if ( val instanceof Number ) {
            if( val instanceof BigDecimal || val instanceof BigInteger ){
                sz = val.toString();
            }
            else {
                return val.toString();
            }
        }
        else if ( val instanceof Boolean ) {
            if( bIncludeBool ){
                return val.toString();
            }
            else {
                return  ( (boolean) val ) ? "1" : "0";
            }
        }
        else {
            sz = val.toString();
        }

        return "'" + StringUtils.addSlashes( sz ) + "'";
    }

    public static String format( Object val ) {
        return SQLStrings.format( val, false );
    }
}
