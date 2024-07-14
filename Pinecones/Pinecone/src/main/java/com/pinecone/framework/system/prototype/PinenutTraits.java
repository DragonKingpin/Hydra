package com.pinecone.framework.system.prototype;

import com.pinecone.framework.system.functions.Executor;
import com.pinecone.framework.system.functions.Function;
import com.pinecone.framework.util.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public final class PinenutTraits {
    public static final String OBJ_STRINGIFY_DEFAULT                 = "[object %s]"; //I think javascript's format is marvelous.

    public static final String FUN_TO_JSON_STRING_NAME               = "toJSONString";

    public static String invokeToJSONString    ( Object that ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method fnToJSONString = that.getClass().getMethod( PinenutTraits.FUN_TO_JSON_STRING_NAME );
        ReflectionUtils.makeAccessible( fnToJSONString );
        return (String) fnToJSONString.invoke( that );
    }

    public static String invokeToJSONString    ( Object that, int nIndentFactor, int nIndentBlankNum ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method fnToJSONString = that.getClass().getMethod( PinenutTraits.FUN_TO_JSON_STRING_NAME, int.class, int.class );
        ReflectionUtils.makeAccessible( fnToJSONString );
        return (String) fnToJSONString.invoke( that, nIndentFactor, nIndentBlankNum );
    }

    public static String invokeToJSONString    ( Object that, String szDefaultResult )  {
        try{
            return PinenutTraits.invokeToJSONString( that );
        }
        catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException e ){
            if( szDefaultResult == null ){
                return that.toString();
            }
            return szDefaultResult;
        }
    }

    public static String invokeToString        ( Object that, Object dyDefaultResult ) {
        try{
            return PinenutTraits.invokeCaseToString( that, dyDefaultResult );
        }
        catch ( IllegalArgumentException e ) {
            return String.format(
                    PinenutTraits.OBJ_STRINGIFY_DEFAULT,
                    that.getClass().getName() + "(0x" + Integer.toHexString( that.hashCode() ) + ")"
            );
        }
    }

    public static String invokeCaseToString    ( Object that, Object dyDefaultResult ) throws IllegalArgumentException {
        if( that == null ){
            return "null";
        }
        else if( that instanceof Function ){
            return "[object Function]";
        }
        else if( that instanceof Executor ){
            return "[object Executor]";
        }
        else if( that instanceof Runnable ){
            return "[object Runnable]";
        }
        else if( that instanceof Callable ){
            return "[object Callable]";
        }
        else if( that.getClass().isEnum() ){
            return that.toString();
        }
        else if( Prototype.isMethodDeclared( that, "toString" ) ){
            return that.toString();
        }
        else if( that.getClass() == Object.class ){ //Hei hei hei~ :)
            return "[object Object]";
        }
        else if( dyDefaultResult instanceof Boolean && (boolean)dyDefaultResult ) {
            return that.toString();
        }
        else if( dyDefaultResult instanceof String ){
            return (String) dyDefaultResult;
        }

        throw new IllegalArgumentException();
    }

}
