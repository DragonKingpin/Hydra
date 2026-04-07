package com.walnut.archcraft.redstone.response;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.json.JSONEncoder;
import org.springframework.http.HttpStatus;


public class GenericResultResponse<T> implements RedResponseEntity<T> {
    private Boolean    success;
    private Integer    code = HttpStatus.OK.value();
    private String     message;
    private String     requestId;
    private String     errorCode;
    private T          data;

    public static <T> GenericResultResponse<T > success() {
        GenericResultResponse<T> result = new GenericResultResponse<>();
        result.code = HttpStatus.OK.value();
        result.success = true;
        return result;
    }

    public static <T> GenericResultResponse<T > successMsg( String msg  ) {
        GenericResultResponse<T> result = GenericResultResponse.success();
        result.message  = msg;
        return result;
    }

    public static <T> GenericResultResponse<T > success( T object ) {
        GenericResultResponse<T> result = GenericResultResponse.success();
        result.data = object;
        return result;
    }

    public static <T> GenericResultResponse<T > error( String msg ) {
        GenericResultResponse<T> result = new GenericResultResponse<>();
        result.success = false;
        result.message  = msg;
        result.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return result;
    }


    @Override
    public Boolean getSuccess() {
        return this.success;
    }

    @Override
    public void setSuccess( Boolean success ) {
        this.success = success;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public void setCode( Integer code ) {
        this.code = code;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public void setErrorCode( String errorCode ) {
        this.errorCode = errorCode;
    }

    @Override
    public void setRequestId( String requestId ) {
        this.requestId = requestId;
    }

    @Override
    public String getRequestId() {
        return this.requestId;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage( String msg ) {
        this.message = msg;
    }

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public void setData( T data ) {
        this.data = data;
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat(new KeyValue[]{
                new KeyValue<>( "success",   this.getSuccess() ),
                new KeyValue<>( "code",      this.code         ),
                new KeyValue<>( "message",   this.message      ),
                new KeyValue<>( "errorCode", this.errorCode    ),
                new KeyValue<>( "requestId", this.requestId    ),
                new KeyValue<>( "data",      this.data         )
        });
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }


}
