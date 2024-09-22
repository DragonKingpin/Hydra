package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;

public final class PropertyTypes {
    public static String ELEMENT_STRING_TYPE_NAME      = "String";
    public static String ELEMENT_INT64_TYPE_NAME       = "int64";
    public static String ELEMENT_INT32_TYPE_NAME       = "int32";
    public static String ELEMENT_FLOAT32_TYPE_NAME     = "float32";
    public static String ELEMENT_FLOAT64_TYPE_NAME     = "float64";
    public static String ELEMENT_BOOLEAN_TYPE_NAME     = "bool";

    public static String ELEMENT_JSONOBJECT_TYPE_NAME  = "JSONObject";
    public static String ELEMENT_JSONARRAY_TYPE_NAME   = "JSONArray";

    public static String queryType( Object val ) {
        String type = PropertyTypes.ELEMENT_STRING_TYPE_NAME;
        if( val instanceof JSONObject ) {
            type = PropertyTypes.ELEMENT_JSONOBJECT_TYPE_NAME;
        }
        else if( val instanceof JSONArray ) {
            type = PropertyTypes.ELEMENT_JSONARRAY_TYPE_NAME;
        }
        else if( val instanceof Byte || val instanceof Short || val instanceof Integer ) {
            type = PropertyTypes.ELEMENT_INT32_TYPE_NAME;
        }
        else if( val instanceof Long ) {
            type = PropertyTypes.ELEMENT_INT64_TYPE_NAME;
        }
        else if( val instanceof Float ) {
            type = PropertyTypes.ELEMENT_FLOAT32_TYPE_NAME;
        }
        else if( val instanceof Double ) {
            type = PropertyTypes.ELEMENT_FLOAT64_TYPE_NAME;
        }
        else if( val instanceof Boolean ) {
            type = PropertyTypes.ELEMENT_BOOLEAN_TYPE_NAME;
        }

        return type;
    }
}
