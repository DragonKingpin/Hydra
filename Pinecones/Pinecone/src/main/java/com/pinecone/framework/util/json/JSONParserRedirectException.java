package com.pinecone.framework.util.json;

public class JSONParserRedirectException extends JSONException {
    Object context;
    int type ;

    public JSONParserRedirectException    ( int type ) {
        super( "" );
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setContext( Object context ) {
        this.context = context;
    }

    public Object getContext() {
        return this.context;
    }
}
