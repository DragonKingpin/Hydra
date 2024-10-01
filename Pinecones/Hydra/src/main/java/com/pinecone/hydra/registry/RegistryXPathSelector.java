package com.pinecone.hydra.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.pinecone.framework.system.prototype.Objectom;
import com.pinecone.framework.util.CursorParser;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.GeneralStrings;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public class RegistryXPathSelector implements RegistrySelector {
    protected Reader            mReader;
    protected char              mcPrevious;
    protected long              mnCharacter;
    protected boolean           mbUsePrevious;
    protected int               mnParseAt ;
    protected int               mnLineAt;

    protected TokenType         mTokenType;
    protected StringBuilder     mCurrentToken;
    protected Registry          mRegistry;

    protected CursorParser      mThisCursor;

    enum TokenType {
        T_UNDEFINED, T_DELIMITER, T_IDENTIFIER, T_INTEGER, T_FLOAT, T_KEYWORD, T_TEMP, T_STRING, T_BLOCK, T_ENDLINE
    }

    public RegistryXPathSelector( Reader reader, Object valMap ) {
        this.mReader        = (Reader)(reader.markSupported() ? reader : new BufferedReader(reader));
        this.mCurrentToken  = new StringBuilder();

        this.mThisCursor    = new RegistrySelectorCursorParser( this );
    }

    protected SelectorParseException parseException( String message ) {
        return new SelectorParseException( message + " at " + this.mnParseAt + " [character " + this.mnCharacter + " line " + this.mnLineAt + "]", (int)this.mnParseAt );
    }

    public void back() throws SelectorParseException {
        if (!this.mbUsePrevious && this.mnParseAt > 0L) {
            --this.mnParseAt;
            --this.mnCharacter;
            this.mbUsePrevious = true;
            this.mTokenType = TokenType.T_UNDEFINED;
        }
        else {
            throw new SelectorParseException( "Stepping back two steps is not supported" );
        }
    }


    public boolean end() {
        return this.mTokenType == TokenType.T_ENDLINE && !this.mbUsePrevious;
    }

    public char next() throws SelectorParseException {
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
                throw new SelectorParseException( e, this.mnParseAt );
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

    public String next(int n) throws SelectorParseException {
        if ( n == 0 ) {
            return "";
        }
        else {
            char[] chars = new char[n];

            for( int pos = 0; pos < n; ++pos ) {
                chars[pos] = this.next();
                if ( this.end() ) {
                    throw this.parseException( "Error parser XPath string with substring bounds error." );
                }
            }

            return new String(chars);
        }
    }

    public void getNextToken() throws SelectorParseException {
        this.mTokenType = TokenType.T_UNDEFINED;

        StringBuilder temp = this.mCurrentToken;
        temp.setLength(0);

        char nextChar = this.next();
        if ( this.end() ) {
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
                throw this.parseException( "Unexpected End-line, with '\r' / '\0'." );
            }

            this.mTokenType = TokenType.T_STRING;
            return;
        }

        if ( "./".indexOf( nextChar ) >= 0 ) {
            temp.append((char) nextChar);
            this.mTokenType = TokenType.T_DELIMITER;
            return;
        }

        if ( Character.isLetter( nextChar ) || nextChar == '_' ) {
            while (!("./".indexOf(nextChar) >= 0 || nextChar == '\r' || nextChar == '\t' || nextChar == '\n' || nextChar == 0)) {
                temp.append( nextChar );
                nextChar = this.next();
            }

            if( "./".indexOf(nextChar) >= 0 ){
                this.back();
            }

            this.mTokenType = TokenType.T_TEMP;
        }

//        String szCurrentToken = this.mCurrentToken.toString();
//        if ( this.mTokenType == TokenType.T_TEMP ) {
//            this.mTokenType = TokenType.T_KEYWORD;
//        }

        if ( this.mTokenType == TokenType.T_UNDEFINED ) {
            throw this.parseException( "\nIllegal token found ! What-> \"" + this.mCurrentToken.toString() + "\"" );
        }
    }

    public Object eval() {
        do {
            this.getNextToken();

            String szTemp = this.mCurrentToken.toString();
            List<TreeNode > treeNodes = this.mRegistry.selectByName( szTemp );
            for ( int i = 0; i < treeNodes.size(); ++i ) {
                TreeNode node = treeNodes.get( i );


            }

            Debug.trace( this.mCurrentToken.toString() );

        }
        while ( this.mTokenType != TokenType.T_ENDLINE );

        return null;
    }
}
