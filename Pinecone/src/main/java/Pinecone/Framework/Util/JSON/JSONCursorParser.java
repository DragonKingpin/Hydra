package Pinecone.Framework.Util.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/**
 *  Pinecone For Java JSONCursorParser [ Bean Nuts Almond JSON For Pinecone Java ]
 *  Copyright © 2008 - 2024 Bean Nuts Foundation ( DR.Undefined ) All rights reserved. [Mr.A.R.B / WJH]
 *  Tip:
 *  *****************************************************************************************
 *  JSON Util Version Signature: Ver. 2.2 [Build 20210301] [Pinecone Ver.2.2]
 *  Author: undefined
 *  Last Modified Date: 2021-02-06
 *  *****************************************************************************************
 *  Principle : Bottom-up parsing implementation with recursive descendent method.
 *  [ Due to Java doesn't had fucking pointer, using cursor method can be better ]
 *  Reference[1]: https://www.json.org/json-en.html
 *  Reference[2]: https://spec.json5.org/
 *  Syntax: T-> "", T->[0-9] T-> '', T->[a-zA-Z]..., etc
 *          T-> { T : T }, T-> [ T ], etc
 *  Support: JSON, JSON5
 *  *****************************************************************************************
 *  Notice: This is high tolerance JSON parser, It does not fully comply with the JSON standard for error situations
 *  *****************************************************************************************
 */
public class JSONCursorParser {
    private long     mnCharacter;
    private boolean  mbIsEOF;
    private long     mnParseAt ;
    private long     mnLineAt;
    private char     mcPrevious;
    private Reader   mReader;
    private boolean  mbUsePrevious;

    public JSONCursorParser( Reader reader ) {
        this.mReader = (Reader)(reader.markSupported() ? reader : new BufferedReader(reader));
        this.mbIsEOF = false;
        this.mbUsePrevious = false;
        this.mcPrevious = 0;
        this.mnParseAt = 0L;
        this.mnCharacter = 1L;
        this.mnLineAt = 1L;
    }

    public JSONCursorParser( InputStream inputStream ) throws JSONException {
        this((Reader)(new InputStreamReader(inputStream)));
    }

    public JSONCursorParser( String s ) {
        this((Reader)(new StringReader(s)));
    }

    public void back() throws JSONException {
        if (!this.mbUsePrevious && this.mnParseAt > 0L) {
            --this.mnParseAt;
            --this.mnCharacter;
            this.mbUsePrevious = true;
            this.mbIsEOF = false;
        }
        else {
            throw new JSONException("Stepping back two steps is not supported");
        }
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        } else if (c >= 'A' && c <= 'F') {
            return c - 55;
        } else {
            return c >= 'a' && c <= 'f' ? c - 87 : -1;
        }
    }

    public boolean end() {
        return this.mbIsEOF && !this.mbUsePrevious;
    }

    public boolean more() throws JSONException {
        this.next();
        if (this.end()) {
            return false;
        } else {
            this.back();
            return true;
        }
    }

    public char next() throws JSONException {
        int c;
        if (this.mbUsePrevious) {
            this.mbUsePrevious = false;
            c = this.mcPrevious;
        } else {
            try {
                c = this.mReader.read();
            } catch ( IOException e ) {
                throw new JSONException(e);
            }

            if (c <= 0) {
                this.mbIsEOF = true;
                c = 0;
            }
        }

        ++this.mnParseAt;
        if (this.mcPrevious == '\r') {
            ++this.mnLineAt;
            this.mnCharacter = (long)(c == 10 ? 0 : 1);
        }
        else if (c == 10) {
            ++this.mnLineAt;
            this.mnCharacter = 0L;
        }
        else {
            ++this.mnCharacter;
        }

        this.mcPrevious = (char)c;
        return this.mcPrevious;
    }

    public char next(char c) throws JSONException {
        char n = this.next();
        if (n != c) {
            throw this.syntaxError("Error parser json string with expected '" + c + "' and instead saw '" + n + "'");
        } else {
            return n;
        }
    }

    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        } else {
            char[] chars = new char[n];

            for(int pos = 0; pos < n; ++pos) {
                chars[pos] = this.next();
                if (this.end()) {
                    throw this.syntaxError("Error parser json string with substring bounds error.");
                }
            }

            return new String(chars);
        }
    }



    public boolean skipComment( char cCurrentChar ){
        if( cCurrentChar == '/' ){
            char nextC = this.next();
            if( nextC == '*' ){
                while(true) {
                    char c = this.next();
                    if( c == '*' ){
                        c = this.next();

                        while ( c == '*' ) {
                            c = this.next();
                        }

                        if( c == '/' ){
                            return true;
                        }
                    }
                }
            }
            else if( nextC == '/' ){
                while(true) {
                    char c = this.next();
                    if( c == '\n' ){
                        return true;
                    }
                    else if( c == '\r' ){
                        c = this.next();
                        if( c == '\n' ){
                            return true;
                        }
                        this.back();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public char nextClean() throws JSONException {
        char c;
        do {
            c = this.next();
            if ( this.skipComment( c ) ){
                c = this.next();
            }
        }
        while( c != 0 && c <= ' ' );

        return c;
    }

    public String nextString(char quote) throws JSONException {
        StringBuilder sb = new StringBuilder();

        while( true ) {
            char c = this.next();
            switch(c) {
                case '\u0000': {
                    sb.append( '\0' );
                    continue;
                }
                case '\n': {
                    sb.append( '\n' );
                    continue;
                }
                case '\r': {
                    sb.append( '\r' );
                    continue;
                    //throw this.syntaxError("Error parser json string with unterminated string."); //What fucking ever, who care.
                }
                case '\\': {
                    c = this.next();
                    switch (c) {
                        case '"':
                        case '\'':
                        case '/':
                        case '\\': {
                            sb.append(c);
                            continue;
                        }
                        case 'b': {
                            sb.append('\b');
                            continue;
                        }
                        case 'f': {
                            sb.append('\f');
                            continue;
                        }
                        case 'n':
                            sb.append('\n');
                            continue;
                        case 'r': {
                            sb.append('\r');
                            continue;
                        }
                        case 't': {
                            sb.append('\t');
                            continue;
                        }
                        case 'x': {
                            // Notice: It is seem JSON not supported '\xFF' format in JSON standard, but who care.
                            sb.append( (char) Integer.parseInt(this.next( 2), 16) );
                            continue;
                        }
                        case 'u': {
                            sb.append((char) Integer.parseInt(this.next( 4), 16));
                            continue;
                        }
                        default: {
                            // Notice: In Pinecone CPP will keep the illegal escape, you can modified as strict [JSON standard NOT allowed].
                            sb.append( '\\' );
                            //throw this.syntaxError( "Error parser json string with illegal escape." );
                        }
                    }
                }
                default: {
                    if (c == quote) {
                        return sb.toString();
                    }

                    sb.append(c);
                }
            }
        }
    }

    public String nextTo(char delimiter) throws JSONException {
        StringBuffer sb = new StringBuffer();

        while(true) {
            char c = this.next();
            if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }

                return sb.toString().trim();
            }

            sb.append(c);
        }
    }

    public String nextTo(String delimiters) throws JSONException {
        StringBuffer sb = new StringBuffer();

        while(true) {
            char c = this.next();
            if (delimiters.indexOf(c) >= 0 || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }

                return sb.toString().trim();
            }

            sb.append(c);
        }
    }


    public Object nextValue() throws JSONException {
        char c = this.nextClean();
        switch(c) {
            case '"':
            case '\'': {
                return this.nextString(c);
            }
            case '[': {
                this.back();
                return new JSONArray( this );
            }
            case '{': {
                this.back();
                return new JSONObject( this );
            }
            default: {
                StringBuffer sb;
                for (sb = new StringBuffer(); c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0; c = this.next()) {
                    sb.append(c);
                }

                this.back();
                String string = sb.toString().trim();
                if ( string.isEmpty() ) {
                    throw this.syntaxError("Error parser json string missing value.");
                }
                else {
                    return JSONUtils.stringToValue(string);
                }
            }
        }
    }

    public char skipTo(char to) throws JSONException {
        char c;
        try {
            long startIndex = this.mnParseAt;
            long startCharacter = this.mnCharacter;
            long startLine = this.mnLineAt;
            this.mReader.mark(1000000);

            do {
                c = this.next();
                if (c == 0) {
                    this.mReader.reset();
                    this.mnParseAt = startIndex;
                    this.mnCharacter = startCharacter;
                    this.mnLineAt = startLine;
                    return c;
                }
            } while(c != to);
        }
        catch ( IOException e ) {
            throw new JSONException( e );
        }

        this.back();
        return c;
    }

    public JSONException syntaxError(String message) {
        return new JSONException( new ParseException( message + this.toString(), (int)this.mnParseAt ) );
    }

    public String toString() {
        return " at " + this.mnParseAt + " [character " + this.mnCharacter + " line " + this.mnLineAt + "]";
    }
}
