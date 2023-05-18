package Pinecone.Framework.Util.JSON.Hometype;

import Pinecone.Framework.System.Functions.Executable;
import Pinecone.Framework.System.Hometype.HomeReactor;
import Pinecone.Framework.System.Hometype.StereotypicReactor;
import Pinecone.Framework.System.Prototype.Prototype;
import Pinecone.Framework.System.util.ReflectionUtils;
import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.Callable;

public class JavaifyReactor implements HomeReactor, StereotypicReactor {
    private String   mszFieldNS;

    private Class<?> mStereotype;

    private boolean  mbUsingHungary = false;

    private String getFieldName( String szKey ){
        if( !this.mszFieldNS.isEmpty() ){
            StringBuilder sb = new StringBuilder();
            sb.append( szKey );
            if( this.mbUsingHungary ){
                sb.setCharAt( 0, Character.toUpperCase( sb.charAt(0) ) );
            }
            szKey = this.mszFieldNS + sb.toString();
        }

        return szKey;
    }

    public String getFieldNamespace() {
        return this.mszFieldNS;
    }

    public void setFieldNamespace( String ns ) {
        this.mszFieldNS = ns;
    }

    public JavaifyReactor( String szFieldNS, Class stereotype ){
        this.mszFieldNS  = szFieldNS;
        this.mStereotype = stereotype;
    }

    public JavaifyReactor ( boolean bUsingHungary, Class stereotype ) {
        this.mbUsingHungary = bUsingHungary;
        if( this.mbUsingHungary ){
            this.mszFieldNS = "m";
        }
        this.mStereotype = stereotype;
    }

    public JavaifyReactor ( Class stereotype ) {
        this( "", stereotype );
    }



    private Object javaify ( Object data, Class<?> stereotype ) {
        if ( JavaifyReactor.trialHomogeneity( data ) ){
            return data;
        }
        else if( stereotype == Object.class ){
            return data;
        }
        else if( data instanceof Executable ){
            return this.javaify( (Executable) data );
        }
        else if ( data instanceof JSONArray ){
            return this.javaify( (JSONArray) data, stereotype );
        }
        else if ( data instanceof JSONObject ){
            return this.javaify( (JSONObject) data, stereotype );
        }
        return data;
    }

    private Collection javaifyDefaultCollection( JSONArray that, Class<?> stereotype ) {
        if( stereotype == null || List.class.isAssignableFrom( stereotype ) ){
            return new ArrayList();
        }
        else if( Set.class.isAssignableFrom( stereotype ) ){
            return new HashSet();
        }
        else if( Queue.class.isAssignableFrom( stereotype ) ){
            return new LinkedList();
        }
        else {
            return new ArrayList();
        }
    }

    private Object javaify ( JSONArray that, Class<?> stereotype ) {
        if( stereotype == JSONArray.class || stereotype == Object.class ){
            return that;
        }
        else if( stereotype == null || Collection.class.isAssignableFrom( stereotype )  ){
            Collection collections;
            if( stereotype == null || stereotype.isInterface() || Prototype.isAbstract( stereotype ) ){ // Motherfucker condition...
                collections = this.javaifyDefaultCollection( that, stereotype );
            }
            else {
                try{
                    collections = (Collection)stereotype.newInstance();
                }
                catch ( InstantiationException | IllegalAccessException e ) {
                    collections = this.javaifyDefaultCollection( that, stereotype );
                }
            }

            for( Object row : that ){
                collections.add( this.javaify( row, stereotype ) );
            }
            return collections;
        }
        else if( stereotype.isArray() ){
            Class innerType = stereotype.getComponentType();
            Object arr      = Array.newInstance( innerType, that.length() );
            if( innerType.isPrimitive() ){
                for( int i = 0; i < that.length(); ++ i ){
                    Array.set( arr, i, this.javaify( that.opt(i), innerType ) );
                }
                return arr;
            }
            else {
                Object[] objects = (Object[]) arr;
                for ( int i = 0; i < that.length(); i++ ) {
                    objects[ i ] = this.javaify( that.opt(i), innerType );
                }
                return objects;
            }
        }

        return null; // WHat fuck could be ??? asking jesus...
    }

    private Object javaify ( JSONObject that, Class<?> stereotype ) {
        if( stereotype == JSONObject.class || stereotype == Object.class ){
            return that;
        }
        else if( stereotype == null || Map.class.isAssignableFrom( stereotype )  ){
            Map map;
            if( stereotype == null || stereotype.isInterface() || Prototype.isAbstract( stereotype ) ){ // Motherfucker condition...
                map = new LinkedHashMap();
            }
            else {
                try{
                    map = (Map)stereotype.newInstance();
                }
                catch ( InstantiationException | IllegalAccessException e ) {
                    map = new LinkedHashMap();
                }
            }

            for( Object row : that.entrySet() ){
                Map.Entry kv = (Map.Entry) row;
                map.put( kv.getKey(), this.javaify( kv.getValue(), stereotype ) );
            }
            return map;
        }
        else  {
            Object obj;
            try{
                Constructor constructor = stereotype.getConstructor();
                ReflectionUtils.makeAccessible( constructor );
                obj = constructor.newInstance();
            }
            catch ( NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e ) {
                return null;
            }

            return this.javaify( that, obj );
        }
    }

    public  Object javaify ( JSONObject that, Object stereotype ) {
        for( Object row : that.entrySet() ){
            Map.Entry kv = (Map.Entry) row;
            Field field;
            try{
                field = stereotype.getClass().getDeclaredField( this.getFieldName( kv.getKey().toString() ) );
            }
            catch ( NoSuchFieldException e ){
                try {
                    field = stereotype.getClass().getDeclaredField(kv.getKey().toString());
                }
                catch ( NoSuchFieldException e1 ){
                    field = null;
                }
            }
            if( field != null ){
                ReflectionUtils.makeAccessible( field );
                try {
                    try {
                        Object j = this.javaify( kv.getValue() , field.getType() );
                        field.set( stereotype, j );
                    }
                    catch ( IllegalArgumentException e ){
                        e.printStackTrace();
                    }
                }
                catch ( IllegalAccessException e ){
                    throw new IllegalStateException(e); // This should never be happened.
                }
            }
        }

/*            Field[] fields = stereotype.getClass().getDeclaredFields();
            for ( Field field : fields ) {
                ReflectionUtils.makeAccessible( field );
                try {
                    Object val = that.opt( this.getFieldName( field ) );
                    if( val == null ){
                        val = that.opt( field.getName() );
                    }
                    try {
                        Object j = this.javaify( val , field.getType() );
                        field.set( stereotype, j );
                    }
                    catch ( IllegalArgumentException e ){
                        e.printStackTrace();
                    }
                }
                catch ( IllegalAccessException e ){
                    throw new IllegalStateException(e); // This should never be happened.
                }
            }*/

        return stereotype;
    }



    public Object javaify   ( JSONArray that ){
        return this.javaify( that, this.mStereotype );
    }

    public Object javaify   ( JSONObject that ){
        return this.javaify( that, this.mStereotype );
    }

    public Object javaify   ( Executable data ) {
        Method fn;
        try {
            fn = data.getClass().getMethod( "reveal" );
            try {
                return fn.invoke( data );
            }
            catch ( Exception e ){
                throw new IllegalArgumentException( "Executable `reveal` function should never be modified.", e ); // What fuck was that, did you modified it ?
            }
        }
        catch ( NoSuchMethodException e ){
            return data;
        }
    }

    public Number javaify   ( Number data ){
        return data;
    }

    public Boolean javaify  ( Boolean data ){
        return data;
    }

    public String javaify   ( String data ){
        return data;
    }

    public JSONType javaify ( JSONType data ){
        return data;
    }

    public Runnable javaify ( Runnable data ){
        return data;
    }

    public Callable javaify ( Callable data ){
        return data;
    }

    public Method   javaify ( Method data ){
        return data;
    }

    public Object   javaify ( Object that ){
        return this.javaify( that, this.mStereotype );
    }



    @Override
    public Object react ( Object that ) throws Exception {
        return this.javaify( that );
    }

    @Override
    public boolean  isHomogeneity( Object that ){
        return JavaifyReactor.trialHomogeneity( that );
    }

    @Override
    public Class<?> getStereotype() {
        return this.mStereotype;
    }

    @Override
    public void setStereotype( Class<?> stereotype ) {
        this.mStereotype = stereotype;
    }

    public static boolean trialHomogeneity(Object that ) {
        return  that instanceof JSONType   ||
                that instanceof Number     || that instanceof Boolean   || that instanceof String || that == JSONObject.NULL ||
                that instanceof Callable   || that instanceof Runnable  || that instanceof Method;
    }


}
