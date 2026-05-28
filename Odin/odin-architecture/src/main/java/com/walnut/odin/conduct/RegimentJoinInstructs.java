package com.walnut.odin.conduct;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegimentJoinInstructs implements Pinenut {

    public static final String Apoptosis = "[!Apoptosis]";

    public static boolean isApoptosis( String szMessage ) {
        if ( szMessage == null ) {
            return false;
        }
        return szMessage.trim().startsWith( Apoptosis );
    }

    public static String apoptosis( String szReason ) {
        if ( szReason == null || szReason.trim().isEmpty() ) {
            return Apoptosis;
        }
        if ( isApoptosis( szReason ) ) {
            return szReason;
        }
        return Apoptosis + " Reject for " + szReason;
    }

}
