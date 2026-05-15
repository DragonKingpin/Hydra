package com.uofs;

import java.io.File;

final class UofsSmokePaths {
    static final long MB = 1024L * 1024L;
    static final long GB = 1024L * 1024L * 1024L;
    static final long ALLOCATION_UNIT = 64L * MB;

    static final String SOURCE_FILE = "E:\\MyFiles\\Picture\\Avatar\\Brickleberry1.png";
    static final String ROOT = "E:\\temp\\titan-uofs-manual";
    static final String TEMP_FOLDER = ROOT + "\\temp";
    static final String READBACK_ROOT = "E:\\titan-uofs-readback";

    private UofsSmokePaths() {
    }

    static void ensureLocalWorkspace() {
        new File( ROOT ).mkdirs();
        new File( TEMP_FOLDER ).mkdirs();
        new File( READBACK_ROOT ).mkdirs();
    }
}
