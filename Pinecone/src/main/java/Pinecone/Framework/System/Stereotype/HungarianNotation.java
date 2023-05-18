package Pinecone.Framework.System.Stereotype;

import Pinecone.Framework.System.Functions.Executable;
import Pinecone.Framework.System.Prototype.Prototype;

import java.lang.reflect.Method;

public class HungarianNotation {
    public static final String S_PRE_STRING_ZERO = "sz";
    public static final String S_PRE_STRING      = "s";
    public static final String S_PRE_NUMBER      = "n";
    public static final String S_PRE_BOOLEAN     = "b";
    public static final String S_PRE_CHAR        = "c";
    public static final String S_PRE_FUNCTION    = "fn";

    public static final String S_PRE_MEMBER      = "m";
    public static final String S_PRE_HANDLE      = "h";


    public static String toUpperCaseFirst( String szProto ){
        StringBuilder sb = new StringBuilder();
        sb.append( szProto );
        sb.setCharAt( 0, Character.toUpperCase( sb.charAt(0) ) );
        return sb.toString();
    }

    public static String addPrefix( String szProto, Class<?> stereotype ) {
        String szRealName = HungarianNotation.toUpperCaseFirst( szProto );
        if( Prototype.isNumber( stereotype ) ){
            return HungarianNotation.S_PRE_NUMBER + szRealName;
        }
        else if ( stereotype == String.class ){
            return HungarianNotation.S_PRE_STRING_ZERO + szRealName;
        }
        else if ( stereotype == Boolean.class || stereotype == boolean.class ){
            return HungarianNotation.S_PRE_BOOLEAN + szRealName;
        }
        else if ( stereotype == Character.class || stereotype == char.class ){
            return HungarianNotation.S_PRE_CHAR + szRealName;
        }
        else if ( Executable.class.isAssignableFrom( stereotype ) || stereotype == Method.class ){
            return HungarianNotation.S_PRE_FUNCTION + szRealName;
        }
        return szProto;
    }

    public static String unPrefix( String szProto, Class<?> stereotype ) {
        StringBuilder sb = new StringBuilder();
        sb.append( szProto );

        if( Prototype.isNumber( stereotype ) ){
            sb.deleteCharAt( 0 );
        }
        else if ( stereotype == String.class ){
            sb.delete( 0, 1 );
        }
        else if ( stereotype == Boolean.class || stereotype == boolean.class ){
            sb.deleteCharAt( 0 );
        }
        else if ( stereotype == Character.class || stereotype == char.class ){
            sb.deleteCharAt( 0 );
        }
        else if ( Executable.class.isAssignableFrom( stereotype ) || stereotype == Method.class ){
            sb.delete( 0, 1 );
        }

        if( sb.length() != szProto.length() ){
            sb.setCharAt( 0, Character.toLowerCase( sb.charAt(0) ) );
            return sb.toString() ;
        }

        return szProto;
    }


}
