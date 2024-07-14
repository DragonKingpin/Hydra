package com.pinecone.framework.util.template;

import com.pinecone.framework.system.ParseException;
import com.pinecone.framework.system.prototype.Objectom;
import com.pinecone.framework.system.prototype.PinenutTraits;
import com.pinecone.framework.util.CursorParser;
import com.pinecone.framework.util.GeneralStrings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *  Pinecone For Java TemplateParser [ Bean Nuts(R) Almond Dragon, Unify Template Language ]
 *  Copyright © Bean Nuts Foundation ( Dragon King ) All rights reserved. [Harold.E / JH.W]
 *  *****************************************************************************************
 *  ${xxx}, ${xxx.xxx}, ${xxx["xxx"]}, ${xxx[xxx]}
 *  *****************************************************************************************
 */
public class TemplateParser {
    protected static final String SYNTAX_ERROR  = "Syntax error !";

    //private   String            mszNowAt;  // For debug only, which is to indict the current parse-at point.
    //private   String            mszRaw;    // For debug only

    protected Reader            mReader;
    protected char              mcPrevious;
    protected long              mnCharacter;
    protected boolean           mbUsePrevious;
    protected int               mnParseAt ;
    protected int               mnLineAt;

    protected TokenType         mTokenType;
    protected StringBuilder     mCurrentToken;
    protected StringBuilder     mRendered;
    protected boolean           mbEvalMode;
    protected Objectom          mVariableMap;

    protected CursorParser      mThisCursor;

    enum TokenType {
        T_UNDEFINED, T_DELIMITER, T_IDENTIFIER, T_INTEGER, T_FLOAT, T_KEYWORD, T_TEMP, T_STRING, T_BLOCK, T_ENDLINE, T_UTL_TAG, T_PASS
    }

    public TemplateParser( Reader reader, Object valMap ) {
        this.mReader        = (Reader)(reader.markSupported() ? reader : new BufferedReader(reader));
        this.mVariableMap   = Objectom.wrap( valMap );
        this.mCurrentToken  = new StringBuilder();
        this.mRendered      = new StringBuilder();

        this.mThisCursor    = new TemplateCursorParser(this);
    }

    public TemplateParser( String raw, Object valMap ) {
        this( new StringReader(raw), valMap );
        //this.mszRaw = raw;
    }

    protected ParseException parseException( String message ) {
        return new ParseException( new ParseException( message + " at " + this.mnParseAt + " [character " + this.mnCharacter + " line " + this.mnLineAt + "]", (int)this.mnParseAt ) );
    }

    public void back() throws ParseException {
        if (!this.mbUsePrevious && this.mnParseAt > 0L) {
            --this.mnParseAt;
            --this.mnCharacter;
            this.mbUsePrevious = true;
            this.mTokenType = TokenType.T_UNDEFINED;
        }
        else {
            throw new ParseException( "Stepping back two steps is not supported" );
        }
    }


    public boolean end() {
        return this.mTokenType == TokenType.T_ENDLINE && !this.mbUsePrevious;
    }

    public char next() throws ParseException {
        int c;
        if ( this.mbUsePrevious ) {
            this.mbUsePrevious = false;
            c = this.mcPrevious;
        }
        else {
            try {
                c = this.mReader.read();
            }
            catch ( IOException e ) {
                throw new ParseException( e, this.mnParseAt );
            }

            if ( c <= 0 ) {
                this.mTokenType = TokenType.T_ENDLINE;
                c = 0;
            }
        }

        ++this.mnParseAt;
        if ( this.mcPrevious == '\r' ) {
            ++this.mnLineAt;
            this.mnCharacter = (long)(c == 10 ? 0 : 1);
        }
        else if ( c == '\n' ) {
            this.mnCharacter = 0L;
            ++this.mnLineAt;
        }
        else {
            ++this.mnCharacter;
        }

//        if ( c != 0 ) {
//            this.mszNowAt = this.mszRaw.substring(this.mnParseAt);
//        }
        this.mcPrevious = (char)c;
        return this.mcPrevious;
    }

    public String next(int n) throws ParseException {
        if ( n == 0 ) {
            return "";
        }
        else {
            char[] chars = new char[n];

            for( int pos = 0; pos < n; ++pos ) {
                chars[pos] = this.next();
                if ( this.end() ) {
                    throw this.parseException( "Error parser json string with substring bounds error." );
                }
            }

            return new String(chars);
        }
    }

    protected void back_if_parenthesized(){
        if(  "]}".indexOf( this.mcPrevious ) >= 0 ) {
            this.back();
        }
    }

    protected void devourUntilEL( char nextChar ) throws ParseException {
        while ( nextChar != 0 ) {
            if ( nextChar == '$' ) {
                int nextNextChar = this.next();
                if ( nextNextChar == '{' ) {
                    this.mCurrentToken.append((char) nextChar);
                    this.mCurrentToken.append((char) nextNextChar);
                    this.mTokenType = TokenType.T_UTL_TAG;
                    this.mbEvalMode = true;
                    return;
                }
                else {
                    this.mRendered.append((char) nextChar);
                }
            }
            else {
                this.mRendered.append((char) nextChar);
            }
            nextChar = this.next();
        }
    }

    public void getNextToken() throws ParseException {
        this.mTokenType = TokenType.T_UNDEFINED;

        StringBuilder temp = this.mCurrentToken;
        temp.setLength(0);

        char nextChar = this.next();
        if ( this.end() ) {
            return;
        }

        if ( !this.mbEvalMode ) {
            this.devourUntilEL(nextChar);
            return;
        }

        while ( nextChar != 0 && Character.isWhitespace(nextChar) ) {
            nextChar = this.next();
        }

        while ( nextChar == '\r' ) {
            nextChar = this.next();
            if (nextChar == '\n') {
                nextChar = this.next();
            }
            while ( nextChar != 0 && Character.isWhitespace(nextChar) ) {
                nextChar = this.next();
            }
        }

        if ( this.mbEvalMode && nextChar == '}' ) {
            this.mCurrentToken.append((char) nextChar);
            this.mTokenType = TokenType.T_DELIMITER;
            this.mbEvalMode = false;
            return;
        }

        if ( nextChar == 0 ) {
            this.mTokenType = TokenType.T_ENDLINE;
            return;
        }


        boolean isDoubleQuote = true;
        if ( nextChar == '"' || nextChar == '\'' ) {
            if ( nextChar == '\'' ) {
                isDoubleQuote = false;
            }

            nextChar = this.next();
            while ( (isDoubleQuote && nextChar != '"') || (!isDoubleQuote && nextChar != '\'') && nextChar != '\r' && nextChar != 0 ) {
                if ( nextChar == '\\' ) {
                    nextChar = this.next();
                    GeneralStrings.transferCharParse( nextChar, this.mThisCursor, temp );
                }
                else {
                    this.mCurrentToken.append( nextChar );
                }

                nextChar = this.next();
            }
            if ( nextChar == '\r' || nextChar == 0 ) {
                throw this.parseException( "Unexpected End-line" );
            }

            this.mTokenType = TokenType.T_STRING;
            return;
        }

        if ( " .[]{}=$".indexOf(nextChar) >= 0 ) {
            temp.append((char) nextChar);
            if (nextChar == '{') {
                temp.append((char) nextChar);
                this.mTokenType = TokenType.T_UTL_TAG;
            }
            else {
                this.mTokenType = TokenType.T_DELIMITER;
            }
            return;
        }

        if ( Character.isLetter( nextChar ) || nextChar == '_' ) {
            while (!(" .[]{}=$".indexOf(nextChar) >= 0 || nextChar == '\r' || nextChar == '\t' || nextChar == '\n' || nextChar == 0)) {
                temp.append( nextChar );
                nextChar = this.next();
            }

            if( ".[]{}=$".indexOf(nextChar) >= 0 ){
                this.back();
            }

            this.mTokenType = TokenType.T_TEMP;
        }


        if ( Character.isDigit(nextChar) || nextChar == '-' || nextChar == '+' ) {
            int dotTimes = 0;
            int nScientificFlag = 0;
            while ( Character.isDigit(nextChar) || ".+-eE".indexOf(nextChar) >= 0 ) {
                if( nextChar == '.' ) {
                    ++dotTimes;
                }
                else if( nextChar == 'E' || nextChar == 'e' ) {
                    ++nScientificFlag;
                }
                temp.append(nextChar);
                nextChar = this.next();
            }

            if ( dotTimes > 0 || nScientificFlag > 0 ) {
                this.mTokenType = TokenType.T_FLOAT;
            }
            else {
                this.mTokenType = TokenType.T_INTEGER;
            }
            return;
        }

        String szCurrentToken = this.mCurrentToken.toString();
        if ( this.mTokenType == TokenType.T_TEMP ) {
            if (
                    szCurrentToken.equals("undefined") || szCurrentToken.equals("null") ||
                    szCurrentToken.equals("false")     || szCurrentToken.equals("true") ||
                    szCurrentToken.equals("this")
            ) {
                this.mTokenType = TokenType.T_KEYWORD;
            }
            else {
                this.mTokenType = TokenType.T_IDENTIFIER;
            }
        }

        if ( this.mTokenType == TokenType.T_UNDEFINED ) {
            throw this.parseException( "\nIllegal token found ! What-> \"" + this.mCurrentToken.toString() + "\"" );
        }
    }

    protected void setPassToken() {
        this.mTokenType = TokenType.T_PASS;
    }

    public String eval() {
        do {
            this.getNextToken();

            if ( this.mTokenType == TokenType.T_UTL_TAG ) {
                Object[] refDummy = new Object[1];
                this.eval_anonymous_val(refDummy);

                Object dummy = refDummy[0];
                if( dummy == null ) {
                    this.mRendered.append( "null" );
                }
                else {
                    //this.mRendered.append( PinenutTraits.invokeToJSONString( dummy, "[object Unknown]") ); // Debug test
                    this.mRendered.append( PinenutTraits.invokeToString(dummy, "[object Unknown]") );
                }
            }

        }
        while ( this.mTokenType != TokenType.T_ENDLINE );

        return mRendered.toString();
    }

    public Object evalValue() {
        this.mbEvalMode = true;

        Object dummy;
        Object[] refDummy = new Object[1];
        do {
            this.getNextToken();
            if( this.mTokenType == TokenType.T_ENDLINE ) {
                break;
            }
            this.eval_exp_assign(refDummy);
        }
        while ( this.mTokenType != TokenType.T_ENDLINE && this.mTokenType != TokenType.T_DELIMITER );
        dummy = refDummy[0];

        return dummy;
    }

    private void eval_anonymous_val( Object[] jtVar ) {
        do {
            this.getNextToken();
            this.eval_exp_assign(jtVar);
        }
        while (
                this.mTokenType != TokenType.T_ENDLINE && !(this.mTokenType == TokenType.T_DELIMITER &&
                        ( this.mCurrentToken.length() > 0 && ( this.mCurrentToken.charAt(0) == '}' || this.mCurrentToken.charAt(0) == ']' ) ) )
        );
    }

    private void eval_exp_assign( Object[] jtVar ) {
        this.eval_exp_parenthesized(jtVar);
    }

    private void eval_exp_parenthesized( Object[] jtVar ) {
        if( this.eval_exp_obtain(jtVar) ){
            return;
        }

        if ( this.mCurrentToken.length() > 0 && this.mCurrentToken.charAt(0) == '(' ) {
            this.getNextToken();
            this.eval_exp_assign(jtVar);

            if ( this.mCurrentToken.length() > 0 &&  this.mCurrentToken.charAt(0) != ')' ) {
                throw this.parseException( "Syntax error Missing ')'." );
            }
        }
        else if ( this.mCurrentToken.length() > 0 && this.mCurrentToken.charAt(0) == '[' ) {
            Object[] dummyKey = new Object[1];
            this.eval_anonymous_val( dummyKey );

            if( dummyKey[0] == null ) {
                throw this.parseException( "Undefined key." );
            }
            if( jtVar[0] instanceof Map ) {
                Map m = (Map)jtVar[0];
                jtVar[0] = m.get(dummyKey[0].toString());
            }
            else if( jtVar[0] instanceof List ) {
                List m = (List)jtVar[0];
                int id;
                if( dummyKey[0] instanceof Number ) {
                    id = ( (Number)dummyKey[0] ).intValue();
                }
                else if( dummyKey[0] instanceof String  ) {
                    id = Integer.parseInt( (String) dummyKey[0] );
                }
                else {
                    id = Integer.parseInt( dummyKey[0].toString() );
                }
                jtVar[0] = m.get(id);
            }
            else {
                throw this.parseException( "Error variable status, should be Map." );
            }

            if ( this.mCurrentToken.length() > 0 && this.mCurrentToken.charAt(0) != ']' ) {
                throw this.parseException( "Syntax error Missing ']'." );
            }
            this.setPassToken();
        }
        else {
            this.variable_obtain(jtVar);
        }
    }

    private boolean eval_exp_obtain( Object[] jtVar ) {
        if ( this.mTokenType == TokenType.T_DELIMITER ) {
            if ( this.mCurrentToken.toString().equals(".") ) {
                this.getNextToken();

                if ( this.mTokenType == TokenType.T_IDENTIFIER ) {
                    if( jtVar[0] instanceof Map ) {
                        Map m = (Map)jtVar[0];
                        jtVar[0] = m.get( this.mCurrentToken.toString() );
                    }
                    else if( jtVar[0] instanceof Objectom ) {
                        Objectom m = (Objectom)jtVar[0];
                        jtVar[0] = m.get( this.mCurrentToken.toString() );
                    }
                    else {
                        throw this.parseException( "Error variable status, should be Map." );
                    }
                }
                else {
                    throw this.parseException( "Illegal template offset" );
                }

                return true;
            }
        }

        return false;
    }

    private void variable_obtain( Object[] jtVar ) {
        int i;
        String szCurrentToken = this.mCurrentToken.toString();
        switch ( this.mTokenType ) {
            case T_IDENTIFIER : {
                if ( this.mVariableMap.containsKey(szCurrentToken) ) {
                    jtVar[0] = this.mVariableMap.get(szCurrentToken);
                }

                //this.getNextToken();
                break;
            }
            case T_INTEGER: {
                if (szCurrentToken.length() > 18) {
                    jtVar[0] = new BigInteger(szCurrentToken);
                }
                else {
                    jtVar[0] = Long.parseLong(szCurrentToken);
                }
                //this.getNextToken();
                this.back_if_parenthesized();
                break;
            }
            case T_FLOAT: {
                if ( szCurrentToken.length() > 18 ) {
                    jtVar[0] = new BigDecimal(szCurrentToken);
                }
                else {
                    double n;
                    if ( szCurrentToken.equals("-INF") || szCurrentToken.equals("-Infinity") ) {
                        n = Double.NEGATIVE_INFINITY;
                    }
                    else if (szCurrentToken.equals("+INF") || szCurrentToken.equals("+Infinity")) {
                        n = Double.POSITIVE_INFINITY;
                    }
                    else {
                        n = Double.parseDouble(szCurrentToken);
                    }

                    jtVar[0] = n;
                }
                //this.getNextToken();
                this.back_if_parenthesized();
                break;
            }
            case T_STRING: {
                jtVar[0] = szCurrentToken;
                //this.getNextToken();
                break;
            }
            case T_DELIMITER: {
                if (".)]}".indexOf(szCurrentToken.charAt(0)) != -1) {
                    break;
                }
                else {
                    throw parseException( TemplateParser.SYNTAX_ERROR );
                }
            }
            case T_KEYWORD: {
                if ( szCurrentToken.equalsIgnoreCase("true") ) {
                    jtVar[0] = true;
                }
                else if ( szCurrentToken.equalsIgnoreCase("false") ) {
                    jtVar[0] = false;
                }
                else if ( szCurrentToken.equalsIgnoreCase("null") || szCurrentToken.equalsIgnoreCase("undefined") ) {
                    jtVar[0] = null;
                }
                else if ( szCurrentToken.equalsIgnoreCase("this") ) {
                    if ( this.mVariableMap.containsKey("this") ) {
                        jtVar[0] = this.mVariableMap.get("this");
                    }
                    else {
                        jtVar[0] = this.mVariableMap;
                    }
                }
                else {
                    throw this.parseException( TemplateParser.SYNTAX_ERROR );
                }
                //this.getNextToken();
                break;
            }
            case T_UTL_TAG: {
                this.eval_anonymous_val(jtVar);
                jtVar[0] = this.mVariableMap.get(jtVar);
                this.mbEvalMode = true;
                this.getNextToken();
                if ( szCurrentToken.charAt(0) != '}' ) {
                    this.back();
                }
                break;
            }
            default: {
                throw this.parseException( TemplateParser.SYNTAX_ERROR );
            }
        }
    }
}
