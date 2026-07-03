package com.walnut.odin.processor.metadata;

import com.walnut.odin.processor.event.TaskProcessorEstablishment;

public final class TaskProcessorRegisterMetadataSpec {

    public static final String KEY_ESTABLISHMENT = "odin.processor.establishment";
    public static final String KEY_NAME          = "odin.processor.name";
    public static final String KEY_ALIAS         = "odin.processor.alias";
    public static final String KEY_BIZ_PATH      = "odin.processor.bizPath";
    public static final String KEY_EXEC_CAPS     = "odin.processor.execCaps";
    public static final String KEY_RUNTIME       = "odin.processor.runtime";

    // Incorporated processor authoritative metadata projected by Odin server.
    public static final String KEY_GUID                              = "odin.processor.guid";
    public static final String KEY_CLUSTER_NAME                      = "odin.processor.clusterName";
    public static final String KEY_CLUSTER_PATH                      = "odin.processor.clusterPath";
    public static final String KEY_LOCAL                             = "odin.processor.local";
    public static final String KEY_EXCLUSIVE                         = "odin.processor.exclusive";
    public static final String KEY_ENABLE                            = "odin.processor.enable";
    public static final String KEY_PRIORITY                          = "odin.processor.priority";
    public static final String KEY_QUEUE_NAME                        = "odin.processor.queue.name";
    public static final String KEY_QUEUE_MAX_CAPACITY                = "odin.processor.queue.maxCapacity";
    public static final String KEY_QUEUE_MIN_CAPACITY                = "odin.processor.queue.minCapacity";
    public static final String KEY_QUEUE_RUNTIME_INSTANCE_CAPACITY   = "odin.processor.queue.runtimeInstanceCapacity";
    public static final String KEY_EXTRA_METADATA                    = "odin.processor.extraMetadata";
    public static final String KEY_DYNAMIC_METADATA                  = "odin.processor.dynamicMetadata";

    public static final String ESTABLISHMENT_INCORPORATED  = TaskProcessorEstablishment.Incorporated.name();
    public static final String ESTABLISHMENT_ANONYMOUS     = TaskProcessorEstablishment.Anonymous.name();

    private TaskProcessorRegisterMetadataSpec() {
    }
}
