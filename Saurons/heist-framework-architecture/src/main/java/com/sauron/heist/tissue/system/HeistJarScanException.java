package com.sauron.heist.tissue.system;

import com.sauron.heist.heistron.HeistException;

public class HeistJarScanException extends HeistException {
    public HeistJarScanException( String szMessage ) {
        super( szMessage );
    }

    public HeistJarScanException( Throwable cause ) {
        super( cause );
    }

    public HeistJarScanException( String szMessage, Throwable cause ) {
        super( szMessage, cause );
    }
}
