package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Factory;
import com.pinecone.framework.util.name.Name;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DynamicFactory extends Factory {
    DynamicFactory DefaultFactory = new GenericDynamicFactory();

    @Override
    ClassLoader           getClassLoader();

    ClassScope            getClassScope();

    Object loadInstance ( String szClassFullName, Class<?>[] stereotypes, Object[] args ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    Object newInstance  ( Class<? > that, Class<?>[] stereotypes, Object[] args ) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    // No exception, but with null.
    default Object optNewInstance  ( Class<? > that, Class<?>[] stereotypes, Object[] args ) {
        try{
            return this.newInstance( that, stereotypes, args );
        }
        catch ( InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            return null;
        }
    }

    // No exception, but with null.
    default Object optLoadInstance ( String szClassFullName, Class<?>[] stereotypes, Object[] args ) {
        try{
            return this.loadInstance( szClassFullName, stereotypes, args );
        }
        catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            return null;
        }
    }

    // No exception, but with null.
    default Object optLoadInstance ( Name classFullName, Class<?>[] stereotypes, Object[] args ) {
        return this.optLoadInstance( classFullName.getFullName(), stereotypes, args );
    }


    // No exception, but with null.
    default Object optNewInstance  ( Class<? > that, Object[] args ) {
        return this.optNewInstance( that, null, args );
    }

    // No exception, but with null.
    default Object optLoadInstance ( String szClassFullName, Object[] args ) {
        return this.optLoadInstance( szClassFullName, null, args );
    }

    // No exception, but with null.
    default Object optLoadInstance ( Name classFullName, Object[] args ) {
        return this.optLoadInstance( classFullName.getFullName(), args );
    }

    // No exception, but with null.
    default Object optLoadInstanceFromScope ( String szClassSimpleName, Class<?>[] stereotypes, Object[] args ) {
        ClassScope scope  = this.getClassScope();
        List<String > nss = scope.getAllNameScopes();

        for( String ns : nss ) {
            if( !ns.endsWith( "." ) ) {
                ns = ns + ".";
            }

            Object neo = this.optLoadInstance( ns + szClassSimpleName, stereotypes, args );
            if( neo != null ){
                return neo;
            }
        }

        return null;
    }

    default Object optLoadInstanceFromScope ( String szClassSimpleName, Object[] args ) {
        return this.optLoadInstance( szClassSimpleName, null, args );
    }

}
