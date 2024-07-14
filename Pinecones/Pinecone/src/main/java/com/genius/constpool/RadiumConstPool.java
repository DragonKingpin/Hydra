package com.genius.constpool;

/**
 * @author Genius
 * @date 2023/05/09 02:34
 **/
public class RadiumConstPool {

    public static final int CPU_PROCESSORS = Runtime.getRuntime().availableProcessors();


    //YAML
    public static final String CONFIG_SYSTEM_PREFIX = "com.pinecone.radium.system.";

    public static final String CONFIG_SYSTEM_CONFIG_PREFIX = CONFIG_SYSTEM_PREFIX+"config.";

    public static final String CONFIG_SYSTEM_CONFIG_TRACER_PREFIX = CONFIG_SYSTEM_CONFIG_PREFIX+"tracer.";
    public static final String CONFIG_COMPONENTS_PREFIX = CONFIG_SYSTEM_PREFIX+"components.";

}
