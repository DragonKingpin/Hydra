package com.walnut.redstone.ether.red;

public final class RedSchemes {
    public static final String Red = "red";
    public static final String Uofs = "uofs";
    public static final String S3 = "s3";

    public static final String Http = "http";
    public static final String Https = "https";

    private RedSchemes() {
    }

    public static boolean isSupported( String szScheme ) {
        return Red.equals( szScheme )
                || Uofs.equals( szScheme )
                || S3.equals( szScheme );
    }
}
