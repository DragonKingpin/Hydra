package com.pinecone.summer;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.summer.http.HttpMethod;

public class GetConnection extends ArchConnection {
    public GetConnection(ArchConnectDispatcher dispatcher ) {
        super( dispatcher );
    }

    protected GetConnection(ArchConnectDispatcher dispatcher, Connectiom connectiom ) {
        super( dispatcher, connectiom );
    }

    @Override
    protected ArchConnection apply(Connectiom connectiom) {
        super.apply(connectiom);

        this.mCurrentHttpMethod        = HttpMethod.GET;
        this.mGETMapContainer          = this.mHttpEntityParser.parseQueryString( this.mConnectiom.request.getQueryString(), false );
        this.mPOSTMapContainer         = new JSONMaptron();
        this.mGlobalParameterContainer = this.mHttpEntityParser.requestMapJsonify( this.mConnectiom.request, false );
        this.mCookiesContainer         = this.mHttpEntityParser.cookiesMapify( this.mCookiesContainer, this.mConnectiom.request );
        this.mMultipartFilesMaker.refresh();
        return this;
    }
}
