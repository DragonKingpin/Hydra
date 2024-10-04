package com.pinecone.hydra.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.pinecone.framework.util.CursorParser;
import com.pinecone.framework.util.GeneralStrings;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;


public class RegistryJPathSelector extends ReparseLinkSelector implements RegistrySelector {
    protected Reader                    mReader;
    protected char                      mcPrevious;
    protected long                      mnCharacter;
    protected boolean                   mbUsePrevious;
    protected int                       mnParseAt ;
    protected int                       mnLineAt;

    protected TokenType                 mTokenType;
    protected StringBuilder             mCurrentToken;
    protected DistributedRegistry       mRegistry;

    protected CursorParser              mThisCursor;
    protected List<RegistryTreeNode>    mQueriedList          ;

    enum TokenType {
        T_UNDEFINED, T_DELIMITER, T_IDENTIFIER, T_INTEGER, T_FLOAT, T_KEYWORD, T_TEMP, T_STRING, T_BLOCK, T_ENDLINE
    }

    public RegistryJPathSelector(Reader reader, PathResolver pathResolver, DistributedRegistry registry, GUIDNameManipulator dirMan, GUIDNameManipulator[] fileMans ) {
        super( pathResolver, registry.getMasterTrieTree(), dirMan, fileMans );

        this.mRegistry      = registry;

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

    protected void eval_root_point() {
        String szTemp = this.mCurrentToken.toString();

        List<GUID > guids;
        guids = this.searchDirAndLinksFirstCase( szTemp );


    }

    public Object eval() {
        int depth = 0;
        GUID parentGuid = null;

        do {
            this.getNextToken();
            if( this.mTokenType == TokenType.T_ENDLINE ) {
                break;
            }

            if( this.mTokenType == TokenType.T_DELIMITER ) {
                continue;
            }

            String currentPart = this.mCurrentToken.toString();
            List<GUID > guids;

            if ( depth == 0 ) {
                guids = this.fetchAllGuidsRootCase( currentPart );
            }
            else {
                // Case3: For middle and last parts, retrieve children GUIDs using distributedTrieTree
                guids = this.distributedTrieTree.getChildrenGuids( parentGuid );
            }

            if ( guids == null || guids.isEmpty() ) {
                continue;
            }

            this.getNextToken();
            for ( GUID guid : guids ) {
                List result = this.eval_entities( guid, currentPart );
                if ( result != null && !result.isEmpty() ) {
                    if ( this.mTokenType == TokenType.T_ENDLINE ) {
                        return result;
                    }

                    parentGuid = guid;
                }
            }
            this.back();

            ++depth;
        }
        while ( this.mTokenType != TokenType.T_ENDLINE );

        return null;
    }

    protected List eval_entities( GUID guid, String partName ) {
        // 在中间部分只匹配文件夹，最后一部分匹配文件和文件夹
        // In the last part, check both files and directories

        if ( this.mTokenType == TokenType.T_ENDLINE ) {
            RegistryTreeNode node = this.mRegistry.get( guid );
            if( !this.checkPartInAllManipulators( guid, partName ) ) {
                if( node instanceof Properties ) {
                    return List.of ( ((Properties) node).get( partName ) );
                }
            }
            return List.of ( node );
        }
        else {
            // Middle part: Directory only.
            //List<GUID > guids = this.dirManipulator.getGuidsByNameID( partName, guid );
            return this.searchDirAndLinks( guid, partName );
        }
    }
}