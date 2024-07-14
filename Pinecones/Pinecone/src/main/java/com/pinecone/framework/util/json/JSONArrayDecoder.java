package com.pinecone.framework.util.json;

public abstract class JSONArrayDecoder implements JSONDecoder {
    protected abstract void add( Object self, Object val );

    @Override
    public void decode( Object self, ArchCursorParser x ) {
        if ( x.nextClean() != '[' ) {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        else if ( x.nextClean() != ']' ) {
            x.back();

            while( true ) {
                if ( x.nextClean() == ',' ) {
                    x.back();

                    this.add( self, JSON.NULL );
                }
                else {
                    x.back();
                    try {
                        this.add( self, x.nextValue( self ) );
                    }
                    catch ( JSONParserRedirectException e ) {
                        x.handleRedirectException( e );
                    }
                }

                switch( x.nextClean() ) {
                    case ',': {
                        if (x.nextClean() == ']') {
                            return;
                        }

                        x.back();
                        break;
                    }
                    case ']': {
                        return;
                    }
                    default: {
                        throw x.syntaxError("Expected a ',' or ']'");
                    }
                }
            }
        }
    }
}
