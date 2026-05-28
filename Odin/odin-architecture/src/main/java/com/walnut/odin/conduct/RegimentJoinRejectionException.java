package com.walnut.odin.conduct;

public class RegimentJoinRejectionException extends IllegalArgumentException {

    public RegimentJoinRejectionException() {
        super();
    }

    public RegimentJoinRejectionException(String message ) {
        super( message );
    }

    public RegimentJoinRejectionException(String message, Throwable cause ) {
        super( message, cause );
    }

    public RegimentJoinRejectionException(Throwable cause ) {
        super( cause );
    }

}
