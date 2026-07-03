package com.walnut.odin.atlas.deletion;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskPurgeException extends RuntimeException implements Pinenut {

    public TaskPurgeException( String message ) {
        super( message );
    }

    public TaskPurgeException( Throwable cause ) {
        super( cause );
    }
}
