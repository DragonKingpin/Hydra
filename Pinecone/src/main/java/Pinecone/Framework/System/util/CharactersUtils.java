package Pinecone.Framework.System.util;

public abstract class CharactersUtils {
    public static Character[] toObjects( char[] that ) {
        Character[] characters = new Character[ that.length ];
        for ( int i = 0; i < that.length; i++ ) {
            characters[i] = that[i];
        }
        return characters;
    }

    public static Character[] toObjects( String that ) { // No more array copy
        Character[] characters = new Character[ that.length() ];
        for ( int i = 0; i < that.length(); i++ ) {
            characters[i] = that.charAt(i);
        }
        return characters;
    }

    public static char[]      toChars  ( Character[] that ) {
        char[] characters = new char[ that.length ];
        for ( int i = 0; i < that.length; i++ ) {
            characters[i] = that[i];
        }
        return characters;
    }

    public static char[]      toChars  ( Object[] that ) {
        char[] characters = new char[ that.length ];
        for ( int i = 0; i < that.length; i++ ) {
            characters[i] = (char) that[i];
        }
        return characters;
    }

    public static boolean     regionMatches ( char c1, char c2 ) {
        c1 = Character.toUpperCase( c1 );
        c2 = Character.toUpperCase( c2 );
        if ( c1 == c2 ) {
            return true;
        }
        // Unfortunately, conversion to uppercase does not work properly
        // for the Georgian alphabet, which has strange rules about case
        // conversion.  So we need to make one last check before
        // exiting.
        /** I agree ! **/
        return Character.toLowerCase(c1) == Character.toLowerCase(c2);
    }

    public static int         compareTo  ( char[] hThis, int nThisFrom, int nThisTo, char[] that, int nThatFrom, int nThatTo, boolean bNoCase ) {
        // Fuck java, there is no FUCKING pointer !!
        nThisTo = nThisTo > hThis.length ? hThis.length : nThisTo;
        nThatTo = nThatTo > that.length  ? that.length  : nThatTo;
        nThisFrom = nThisFrom > 0 ? nThisFrom : 0;
        nThatFrom = nThatFrom > 0 ? nThatFrom : 0;

        int len1 = nThisTo - nThisFrom;
        int len2 = nThatTo - nThatFrom;
        int lim = Math.min( len1, len2 );

        int k = 0;
        while ( k < lim ) {
            char c1 = hThis[ k + nThisFrom ];
            char c2 = that [ k + nThatFrom ];
            if( bNoCase ){
                if( !CharactersUtils.regionMatches( c1, c2 ) ) {
                    return c1 - c2;
                }
            }
            else {
                if ( c1 != c2 ) {
                    return c1 - c2;
                }
            }
            k++;
        }
        return len1 - len2;
    }

    public static int         compareTo  ( char[] hThis, int nThisFrom, int nThisTo, char[] that, int nThatFrom, int nThatTo ) {
        return CharactersUtils.compareTo( hThis, nThisFrom, nThisTo, that, nThatFrom, nThatTo, false );
    }

    public static boolean     equals     ( char[] hThis, int nThisFrom, int nThisTo, char[] that, int nThatFrom, int nThatTo, boolean bNoCase ) {
        // Fuck java, there is no FUCKING pointer !!
        nThisTo = nThisTo > hThis.length ? hThis.length : nThisTo;
        nThatTo = nThatTo > that.length  ? that.length  : nThatTo;
        nThisFrom = nThisFrom > 0 ? nThisFrom : 0;
        nThatFrom = nThatFrom > 0 ? nThatFrom : 0;

        int len1  = nThisTo - nThisFrom;
        int len2  = nThatTo - nThatFrom;
        if ( len1 == len2 ) {
            int i = 0;
            while ( len1-- != 0 ) {
                char c1 = hThis[ i + nThisFrom ];
                char c2 = that [ i + nThatFrom ];

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
                i++;
            }
            return true;
        }

        return false;
    }

    public static boolean     equals     ( char[] hThis, int nThisFrom, int nThisTo, char[] that, int nThatFrom, int nThatTo ) {
        return CharactersUtils.equals( hThis, nThisFrom, nThisTo, that, nThatFrom, nThatTo, false );
    }



    public static char[] toLower( char[] arrThis ){
        for ( int i = 0; i < arrThis.length; i++ ) {
            arrThis[i] = Character.toLowerCase( arrThis[i] );
        }
        return arrThis;
    }

    public static char[] toUpper( char[] arrThis ){
        for ( int i = 0; i < arrThis.length; i++ ) {
            arrThis[i] = Character.toUpperCase( arrThis[i] );
        }
        return arrThis;
    }

}
