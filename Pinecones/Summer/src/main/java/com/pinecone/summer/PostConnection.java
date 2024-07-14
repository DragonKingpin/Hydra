package com.pinecone.summer;

import com.pinecone.summer.http.HttpMethod;

public class PostConnection extends ArchConnection {
    public PostConnection( ArchConnectDispatcher dispatcher ) {
        super( dispatcher );
    }

    protected PostConnection(ArchConnectDispatcher dispatcher, Connectiom connectiom ) {
        super( dispatcher, connectiom);
    }

    @Override
    protected ArchConnection apply( Connectiom connectiom ) {
        super.apply(connectiom);

        this.mCurrentHttpMethod            = HttpMethod.POST;
        this.mGETMapContainer              = this.mHttpEntityParser.parseQueryString  ( this.mConnectiom.request.getQueryString(), false );
        this.mCookiesContainer             = this.mHttpEntityParser.cookiesMapify( this.mCookiesContainer, this.mConnectiom.request );
        this.mMultipartFilesMaker.refresh();
        /* Notice: 2020-12-25
         * Java Servlet abandoned multipart post.
         * Pinecone be forced to redefined $_POST.
         * **/
        if( this.mMultipartFilesMaker.isMultipart() ){
            this.mMultipartFilesMaker.interceptMultipartFiles();
            this.mFilesMapContainer         = this.mMultipartFilesMaker.getCurrentFilesMap();
            this.mCurrentMultipartRequest   = this.mMultipartFilesMaker.getCurrentMultipartRequest();
            this.mPOSTMapContainer          = this.mHttpEntityParser.siftPostFromParameterMap ( this.mCurrentMultipartRequest, false );
            this.mGlobalParameterContainer  = this.mHttpEntityParser.requestMapJsonify        ( this.mCurrentMultipartRequest,false );
        }
        else {
            this.mPOSTMapContainer         = this.mHttpEntityParser.siftPostFromParameterMap ( this.mConnectiom.request, false );
            this.mGlobalParameterContainer = this.mHttpEntityParser.requestMapJsonify        ( this.mConnectiom.request,false );
        }
        return this;
    }
}