package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.lang.DynamicFactory;

public interface InstanceDispenser extends Pinenut {
    StructureDefinition update ( Class<?> type, StructureDefinition definition ) ;

    InstanceDispenser register( Class<?> type, StructureDefinition definition, @Nullable InstancePool<? > pool ) ;

    InstanceDispenser register( Class<?> type, StructureDefinition definition ) ;

    InstanceDispenser register( Class<?> type, Structure structure, @Nullable InstancePool<? > pool ) ;

    InstanceDispenser register( Class<?> type, Structure structure ) ;

    InstanceDispenser register( Class<?> type, @Nullable InstancePool<? > pool ) ;

    InstanceDispenser register( Class<?> type ) ;

    InstanceDispenser register( StructureDefinition definition ) ;

    InstanceDispenser registerByImplicitFirstFound( Class<?> type, @Nullable Structure structure, @Nullable InstancePool<? > pool ) ;

    InstanceDispenser registerByImplicitFirstFound( Class<?> type, @Nullable Structure structure ) ;

    InstanceDispenser registerByImplicitFirstFound( Class<?> type ) ;

    boolean  hasRegistered( Class<? > type );

    <T > T allotInstance( Class<T> type, @Nullable Structure instanceStructure ) ;

    <T > T allotInstance( Class<T> type ) ;

    void free( Class<?> type, Object instance ) ;

    void free( Object instance );

    StructureDefinition getStructureDefinition( Class<?> type );

    InstancePool<? > getInstancePool( Class<?> type );

    DynamicFactory getCentralFactory();
}
