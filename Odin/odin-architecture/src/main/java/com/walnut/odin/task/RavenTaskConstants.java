package com.walnut.odin.task;

import com.pinecone.framework.util.datetime.DatePattern;

public final class RavenTaskConstants {

    public static final String InstanceTitleTimeFormat = "yyyy_MM_dd_HH_mm_ss";
    public static final String DefaultDateTimeFormat = DatePattern.NORM_DATETIME_PATTERN;

    public static final int  ScheduleScanThreadCount = 8;
    public static final long ScheduleScanIdWindow    = 1000L;

}
