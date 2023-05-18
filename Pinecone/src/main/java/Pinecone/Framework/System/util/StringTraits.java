package Pinecone.Framework.System.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class StringTraits {
    // String like `CHO2OHC`
    public static boolean isChiralString ( String szThat, boolean bNoCase ) {
        int nMid   = szThat.length() / 2;

        for ( int i = 0; i < nMid; i++ ) {
            char c1 = szThat.charAt(i);
            char c2 = szThat.charAt( szThat.length() - i - 1 );

            if( bNoCase ){
                if( !CharactersUtils.regionMatches( c1, c2 ) ){
                    return false;
                }
            }
            else {
                if ( c1 != c2 ) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isChiralString ( String szThat ) {
        return StringTraits.isChiralString( szThat, true );
    }

    // String like `CHO2CHO`
    public static boolean isHomoString   ( String szThat, boolean bNoCase ) {
        int nBias = szThat.length() % 2 != 0 ? 1 : 0;
        int nMid   = szThat.length() / 2;

        for ( int i = 0; i < nMid; i++ ) {
            char c1 = szThat.charAt(i);
            char c2 = szThat.charAt( nMid + i + nBias );

            if( bNoCase ){
                if( !CharactersUtils.regionMatches( c1, c2 ) ){
                    return false;
                }
            }
            else {
                if ( c1 != c2 ) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isHomoString   ( String szThat ) {
        return StringTraits.isHomoString( szThat, true );
    }

    // String like `CHO2HCO` or `CHO2COH` etc.
    public static boolean isHeterString  ( String szThat, boolean bNoCase ) {
        int nBias = szThat.length() % 2 != 0 ? 1 : 0;
        int nMid  = szThat.length() / 2;

        char[] chars = szThat.toCharArray();
        Arrays.sort( chars, 0, nMid );
        Arrays.sort( chars, nMid + nBias, szThat.length() );

        return CharactersUtils.equals( chars, 0, nMid, chars, nMid + nBias, chars.length, bNoCase );
    }

    public static boolean isHeterString  ( String szThat ) {
        return StringTraits.isHeterString( szThat, true );
    }

}
