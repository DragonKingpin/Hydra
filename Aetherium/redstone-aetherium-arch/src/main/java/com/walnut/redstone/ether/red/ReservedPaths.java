package com.walnut.redstone.ether.red;

public final class ReservedPaths {
    public static final String System = "/" + ReservedLabels.System;
    public static final String Red = "/" + ReservedLabels.Red;
    public static final String Api = "/" + ReservedLabels.Api;

    public static final String RedStatus = Red + "/status";
    public static final String RedConfig = Red + "/config";
    public static final String RedLocate = Red + "/locate";
    public static final String RedProxy = Red + "/proxy";

    public static final String ApiStatus = Api + "/status";
    public static final String ApiProbe = Api + "/probe";

    private ReservedPaths() {
    }

    public static boolean isReservedPath( String szPath ) {
        String szValue = szPath == null ? "" : szPath;
        return szValue.equals( Red )
                || szValue.startsWith( Red + "/" )
                || szValue.equals( Api )
                || szValue.startsWith( Api + "/" );
    }
}
