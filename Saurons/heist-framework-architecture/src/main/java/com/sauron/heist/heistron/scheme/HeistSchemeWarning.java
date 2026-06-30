package com.sauron.heist.heistron.scheme;

import com.pinecone.framework.system.prototype.Pinenut;

public class HeistSchemeWarning implements Pinenut {
    private HeistSchemeRenderStage stage;
    private String code;
    private String message;

    public HeistSchemeWarning() {
    }

    public HeistSchemeWarning( HeistSchemeRenderStage stage, String code, String message ) {
        this.stage = stage;
        this.code = code;
        this.message = message;
    }

    public HeistSchemeRenderStage getStage() {
        return this.stage;
    }

    public HeistSchemeWarning setStage( HeistSchemeRenderStage stage ) {
        this.stage = stage;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public HeistSchemeWarning setCode( String code ) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public HeistSchemeWarning setMessage( String message ) {
        this.message = message;
        return this;
    }
}
