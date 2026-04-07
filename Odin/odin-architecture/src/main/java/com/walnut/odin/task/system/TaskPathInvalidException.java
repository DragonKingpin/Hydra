package com.walnut.odin.task.system;

import com.walnut.odin.system.RavenRuntimeException;

public class TaskPathInvalidException extends RavenRuntimeException {

    public TaskPathInvalidException() {
        super();
    }

    public TaskPathInvalidException( String path ) {
        super( "Path `" + path + "` is invalided." );
    }

    public TaskPathInvalidException( String message, Throwable cause ) {
        super(message, cause);
    }

    public TaskPathInvalidException( Throwable cause ) {
        super(cause);
    }

    protected TaskPathInvalidException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
