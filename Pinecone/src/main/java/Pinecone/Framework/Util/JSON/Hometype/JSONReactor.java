package Pinecone.Framework.Util.JSON.Hometype;

import Pinecone.Framework.System.Functions.Executable;
import Pinecone.Framework.System.Functions.Executor;
import Pinecone.Framework.System.Functions.Function;
import Pinecone.Framework.System.Hometype.HomeReactor;
import Pinecone.Framework.System.util.ReflectionUtils;
import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;
import Pinecone.Framework.Util.JSON.Prototype.JSONString;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class JSONReactor implements HomeReactor {
    public static Number jsonify( Number data ){
        return data;
    }

    public static Boolean jsonify( Boolean data ){
        return data;
    }

    public static String jsonify( String data ){
        return data;
    }

    public static Executable jsonify( Executable data ){
        return data;
    }

    public static JSONObject jsonify( JSONObject data ){
        return data;
    }

    public static JSONArray jsonify( JSONArray data ){
        return data;
    }

    public static JSONType jsonify( JSONType data ){
        return data;
    }

    public static Object  jsonify( Object data ) throws IllegalArgumentException {
        return JSONReactor.jsonify( data, true );
    }

    public static Object  jsonify( Object data, boolean bRecursive ) throws IllegalArgumentException  {
        if ( data != null ) {
            if ( JSONReactor.trialHomogeneity( data ) ) {
                return data;
            }
            else if ( data instanceof Map) {
                return new JSONObject((Map)data);
            }
            else if ( data instanceof List) {
                return new JSONArray((List) data);
            }
            else if ( data instanceof Collection ) {
                return new JSONArray((Collection)data);
            }
            else if ( data instanceof JSONString ) {
                return ( (JSONString) data ).toJSONString();
            }
            else {
                return JSONReactor.javaObjectJsonify( data, bRecursive );
            }
        }

        return JSONObject.NULL;
    }

    public static Object  javaObjectJsonify( Object data, boolean bRecursive ) throws IllegalArgumentException {
        if( data != null ){
            if( data.getClass().isArray() ){
                JSONArray jsonArray = new JSONArray();
                if( data.getClass().getComponentType().isPrimitive() ){
                    for ( int i = 0; i < Array.getLength(data); i++ ) {
                        jsonArray.put( JSONReactor.jsonify( Array.get( data, i ) ) );
                    }
                }
                else {
                    for ( Object row : (Object[]) data ) {
                        jsonArray.put( JSONReactor.jsonify( row ) );
                    }
                }
                return jsonArray;
            }
            else if( data.getClass().isEnum() ) {
                return data.toString();
            }
            else if( data instanceof Method ){
                throw new IllegalArgumentException( "Method cannot survive without its mother." );
            }
            else if( data instanceof Runnable ){
                return new Executor() {
                    Runnable proto = (Runnable) data;

                    public Runnable reveal(){
                        return this.proto;
                    }

                    @Override
                    public void execute() throws Exception {
                        this.proto.run();
                    }
                };
            }
            else if( data instanceof Callable ){
                return new Function() {
                    Callable proto = (Callable) data;

                    public Callable reveal(){
                        return this.proto;
                    }

                    @Override
                    public Object invoke( Object... obj ) throws Exception {
                        return this.proto.call();
                    }
                };
            }
            else {
                JSONObject jsonObject = new JSONObject();
                Field[] fields = data.getClass().getDeclaredFields();
                for ( Field field : fields ) {
                    ReflectionUtils.makeAccessible( field );
                    Object value;
                    try{
                        value = field.get( data );
                    }
                    catch ( IllegalAccessException e ){
                        value = null;
                    }
                    jsonObject.embed( field.getName(), JSONReactor.jsonify(value, bRecursive) );
                }
                return jsonObject;
            }

        }
        return JSONObject.NULL;
    }

    public static boolean trialHomogeneity( Object that ) {
        return  that instanceof JSONType   ||
                that instanceof Number     || that instanceof Boolean   || that instanceof String || that == JSONObject.NULL ||
                that instanceof JSONObject || that instanceof JSONArray ||
                that instanceof Executable;
    }

    @Override
    public boolean isHomogeneity( Object that ) {
        return JSONReactor.trialHomogeneity( that );
    }

    @Override
    public Object react( Object that ) throws Exception {
        return JSONReactor.jsonify( that );
    }

}
