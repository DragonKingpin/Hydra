package com.walnut.odin.processor.metadata;

import com.walnut.odin.processor.event.TaskProcessorEstablishment;

public final class TaskProcessorRegisterMetadataSpec {

    public static final String KEY_ESTABLISHMENT = "odin.processor.establishment";
    public static final String KEY_NAME          = "odin.processor.name";
    public static final String KEY_ALIAS         = "odin.processor.alias";
    public static final String KEY_BIZ_PATH      = "odin.processor.bizPath";
    public static final String KEY_EXEC_CAPS     = "odin.processor.execCaps";
    public static final String KEY_RUNTIME       = "odin.processor.runtime";

    public static final String ESTABLISHMENT_INCORPORATED  = TaskProcessorEstablishment.Incorporated.name();
    public static final String ESTABLISHMENT_ANONYMOUS     = TaskProcessorEstablishment.Anonymous.name();

    private TaskProcessorRegisterMetadataSpec() {
    }
}
