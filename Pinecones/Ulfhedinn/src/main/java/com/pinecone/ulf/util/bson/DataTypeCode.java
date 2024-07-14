package com.pinecone.ulf.util.bson;

public enum DataTypeCode {
    UNDEFINED                ( 0x01, "Undefined"            ),
    NULL                     ( 0x02, "Null"                 ),
    BYTE8                    ( 0x03, "Byte8"                ),
    INT16                    ( 0x04, "Int16"                ),
    INT32                    ( 0x05, "Int32"                ),
    INT64                    ( 0x06, "Int64"                ),
    FLOAT32                  ( 0x07, "Float32"              ),
    FLOAT64                  ( 0x08, "Float64"              ),
    BOOL                     ( 0x09, "Bool"                 ),
    BIG_INTEGER              ( 0x0A, "BigInteger"           ),
    BIG_DECIMAL              ( 0x0B, "BigDecimal"           ),
    STRING                   ( 0x0C, "String"               ),


    JSONOBJECT               ( 0xFA, "JSONObject"           ),
    JSONARRAY                ( 0xFB, "JSONArray"            ),

    JSONOBJECT_END           ( 0xEA, "JSONObject$End"       ),
    JSONARRAY_END            ( 0xEB, "JSONArray$End"        ),

    SERIALIZABLE_OBJ         ( 0xFC, "SerializableObj"      );

    private final int value;

    private final String name;

    DataTypeCode( int codeVal, String name ){
        this.value = codeVal;
        this.name  = name;
    }

    public String getName(){
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public byte getByteValue() {
        return (byte) this.value;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static DataTypeCode asCode( int codeVal ) {
        for ( DataTypeCode type : DataTypeCode.values() ) {
            if ( type.getValue() == codeVal ) {
                return type;
            }
        }
        throw new IllegalArgumentException( "Invalid DataTypeCode value: " + codeVal );
    }
}
