package Pinecone.Framework.Util.Summer;

import Pinecone.Framework.Util.JSON.JSONObject;
import Pinecone.Framework.Util.Summer.http.HttpMethod;

public class GetConnection extends ArchConnection {
    public GetConnection(ArchControlDispatcher dispatcher ) {
        super( dispatcher );
    }

    protected GetConnection(ArchControlDispatcher dispatcher, Connectiom connectiom ) {
        super( dispatcher, connectiom );
    }

    @Override
    protected ArchConnection apply(Connectiom connectiom) {
        super.apply(connectiom);

        this.mCurrentHttpMethod        = HttpMethod.GET;
        this.mGETMapContainer          = this.mHttpEntityParser.parseQueryString( this.mConnectiom.request.getQueryString(), false );
        this.mPOSTMapContainer         = new JSONObject();
        this.mGlobalParameterContainer = this.mHttpEntityParser.requestMapJsonify( this.mConnectiom.request, false );
        this.mCookiesContainer         = this.mHttpEntityParser.cookiesMapify( this.mCookiesContainer, this.mConnectiom.request );
        this.mMultipartFilesMaker.refresh();
        return this;
    }
}
