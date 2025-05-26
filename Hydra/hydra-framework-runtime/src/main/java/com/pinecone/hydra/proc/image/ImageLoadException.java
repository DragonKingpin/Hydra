package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.prototype.Pinenut;

public class ImageLoadException extends Exception implements Pinenut {

    public ImageLoadException    () {
        super();
    }

    public ImageLoadException    ( String message ) {
        super(message);
    }

    public ImageLoadException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public ImageLoadException    ( Throwable cause ) {
        super(cause);
    }

}
