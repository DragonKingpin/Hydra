package Pinecone.Framework.Util.JSON;

public abstract class JSON {

    public static Object parse ( String szJsonString ) {
        return ( new JSONCursorParser( szJsonString ) ).nextValue();
    }

    public static String stringify ( Object that ) {
        return JSONSerializer.stringify( that );
    }



}
