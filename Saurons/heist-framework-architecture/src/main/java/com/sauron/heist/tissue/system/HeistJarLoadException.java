package com.sauron.heist.tissue.system;

import com.sauron.heist.heistron.HeistException;

public class HeistJarLoadException extends HeistException {
    public HeistJarLoadException( String szMessage ) {
        super( szMessage );
    }

    public HeistJarLoadException( Throwable cause ) {
        super( cause );
    }

    public HeistJarLoadException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
