package com.pinecone.framework.util.json;

public final class JSONMarshalMode {

    private JSONMarshalMode() {}

    public static final long MODE_ANNOTATED_FIELD = 1L;
    public static final long MODE_ANY_FIELD       = 1L << 1;
    public static final long MODE_PUBLIC_FIELD    = 1L << 2;
    public static final long MODE_BEAN_GETTER     = 1L << 3;

    public static final long MODE_DEFAULT = MODE_ANNOTATED_FIELD;
    public static final long MODE_COMMON  = MODE_ANNOTATED_FIELD | MODE_PUBLIC_FIELD | MODE_BEAN_GETTER;
    public static final long MODE_ALL     = MODE_ANNOTATED_FIELD | MODE_PUBLIC_FIELD | MODE_BEAN_GETTER | MODE_ANY_FIELD;

}
