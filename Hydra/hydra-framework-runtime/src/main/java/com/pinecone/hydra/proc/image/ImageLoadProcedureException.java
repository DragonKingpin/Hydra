package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.PineRuntimeException;

public class ImageLoadProcedureException extends PineRuntimeException {

    public ImageLoadProcedureException    () {
        super();
    }

    public ImageLoadProcedureException    ( String message ) {
        super(message);
    }

    public ImageLoadProcedureException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public ImageLoadProcedureException    ( Throwable cause ) {
        super(cause);
    }

}
