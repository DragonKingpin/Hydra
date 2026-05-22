package com.pinecone.framework.system.construction.eval;

import com.pinecone.framework.system.prototype.Pinenut;

public class StructureValueExpression implements Pinenut {
    protected String  mszRaw;
    protected String  mszPath;
    protected String  mszDefaultValue;
    protected boolean mbExpression;
    protected boolean mbDefaultValuePresented;

    public StructureValueExpression( String szRaw ) {
        this.mszRaw = szRaw;
        this.parse( szRaw );
    }

    protected void parse( String szRaw ) {
        if ( szRaw == null ) {
            this.mszPath = "";
            return;
        }
        String szText = szRaw.trim();
        this.mbExpression = szText.startsWith( "${" ) && szText.endsWith( "}" );
        if ( !this.mbExpression ) {
            this.mszPath = szRaw;
            return;
        }

        String szBody = szText.substring( 2, szText.length() - 1 );
        int nIndex = szBody.indexOf( ':' );
        if ( nIndex < 0 ) {
            this.mszPath = szBody;
            return;
        }

        this.mszPath = szBody.substring( 0, nIndex );
        this.mszDefaultValue = szBody.substring( nIndex + 1 );
        this.mbDefaultValuePresented = true;
    }

    public String getRaw() {
        return this.mszRaw;
    }

    public String getPath() {
        return this.mszPath;
    }

    public String getDefaultValue() {
        return this.mszDefaultValue;
    }

    public boolean isExpression() {
        return this.mbExpression;
    }

    public boolean isDefaultValuePresented() {
        return this.mbDefaultValuePresented;
    }
}
