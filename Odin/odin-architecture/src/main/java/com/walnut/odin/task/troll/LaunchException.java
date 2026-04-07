package com.walnut.odin.task.troll;

import com.pinecone.framework.system.prototype.Pinenut;

public class LaunchException extends Exception implements Pinenut {

    public LaunchException() {
        super();
    }

    public LaunchException(String message ) {
        super(message);
    }

    public LaunchException(String message, Throwable cause ) {
        super(message, cause);
    }

    public LaunchException(Throwable cause ) {
        super(cause);
    }

}