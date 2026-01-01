package com.walnut.archcraft.redstone.response;

import java.io.Serializable;

import com.pinecone.framework.system.prototype.Pinenut;

public interface RedResponse extends Pinenut, Serializable {

    Boolean getSuccess();

    void setSuccess( Boolean success );

    Integer getCode();

    void setCode( Integer code );

    String getErrorCode();

    void setErrorCode( String errorCode );

    String getMessage();

    void setMessage( String msg );


}
