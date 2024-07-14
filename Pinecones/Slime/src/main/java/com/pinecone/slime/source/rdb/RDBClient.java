package com.pinecone.slime.source.rdb;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.slime.source.DAOScanner;
import com.pinecone.slime.source.DataAccessObject;

import java.lang.annotation.Annotation;
import java.util.List;

public interface RDBClient extends Pinenut {
    String getInstanceName();

    void close();

    boolean isTerminated();

    DAOScanner getDataAccessObjectScanner();

    List<Class<? > > addDataAccessObjectScope( String szPacketName );

    List<Class<? > > addDataAccessObjectScope( String szPacketName, boolean bIgnoreOwnedChecked );

    default boolean hasOwnDataAccessObject( Class<?> clazz ) {
        Annotation[] annotations = clazz.getAnnotations();
        for( Annotation annotation : annotations ) {
            if( annotation instanceof DataAccessObject ) {
                String s = ((DataAccessObject) annotation).scope();
                if( s.isEmpty() || s.equals( this.getInstanceName() ) ){
                    return true;
                }
            }
        }
        return false;
    }
}
