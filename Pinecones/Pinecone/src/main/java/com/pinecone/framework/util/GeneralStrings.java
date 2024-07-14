package com.pinecone.framework.util;

public abstract class GeneralStrings {
    public static boolean transferCharParse ( char c, CursorParser cursor, StringBuilder sb ) {
        switch ( c ) {
            case '"':
            case '\'':
            case '/':
            case '\\': {
                sb.append(c);
                return true;
            }
            case 'b': {
                sb.append('\b');
                return true;
            }
            case 'f': {
                sb.append('\f');
                return true;
            }
            case 'n':
                sb.append('\n');
                return true;
            case 'r': {
                sb.append('\r');
                return true;
            }
            case 't': {
                sb.append('\t');
                return true;
            }
            case 'x': {
                // Notice: It is seem JSON not supported '\xFF' format in JSON standard, but who care.
                sb.append( (char) Integer.parseInt(cursor.next( 2), 16) );
                return true;
            }
            case 'u': {
                sb.append((char) Integer.parseInt(cursor.next( 4), 16));
                return true;
            }
            default: {
                // Notice: In Pinecone CPP will keep the illegal escape, you can modified as strict [JSON standard NOT allowed].
                sb.append( '\\' );
                //throw this.syntaxError( "Error parser json string with illegal escape." );
            }
        }

        return false;
    }
}
