package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.framework.util.rdb.SQLStrings;
import com.pinecone.slime.map.QueryRange;
import com.pinecone.slime.source.GenericResultConverter;
import com.pinecone.slime.source.ResultConverter;
import com.pinecone.slime.source.rdb.RDBTargetTableMeta;
import com.pinecone.slime.source.rdb.RangedRDBQuerierDataManipulator;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GenericMybatisQuerierDataManipulator<K, V > extends RangedRDBQuerierDataManipulator<K, V > {

    @Override
    @Select( "SELECT COUNT(*) FROM ${meta.tableName} ${exSafeSQL}" )
    long counts( @Param("meta") RDBTargetTableMeta meta, @Param("exSafeSQL") String szExSafeSQL );

    @Override
    @Select( "SELECT COUNT(*) FROM ${meta.tableName} WHERE ${keyName} = ${key}" )
    long countsByColumn( @Param("meta") RDBTargetTableMeta meta, @Param("keyName") String szSpecificColumnKeyName, @Param("key") Object key );

    @Override
    @Select( "SELECT COUNT(*) FROM ${meta.tableName} WHERE ${range.rangeKey} >= ${range.min} AND ${range.rangeKey} <= ${range.max}" )
    long countsByRange( @Param("meta") RDBTargetTableMeta meta, @Param("range") QueryRange range );

    @SelectProvider( type = DynamicQuerierSqlBuilder.class, method = "selectList" )
    @ResultType( LinkedTreeMap.class )
    void selectList0( @Param("meta") RDBTargetTableMeta meta, @Param("handler") ResultHandler<Map<Object, V > > handler, @Param("exSafeSQL") String szExSafeSQL );

    @Override
    @SelectProvider( type = DynamicQuerierSqlBuilder.class, method = "selectListByRange" )
    List<V > selectListByRange( @Param("meta") RDBTargetTableMeta meta, @Param("range") QueryRange range );

    @SelectProvider( type = DynamicQuerierSqlBuilder.class, method = "selectMappedByRange" )
    @ResultType( LinkedTreeMap.class )
    void selectMappedByRange0( RDBTargetTableMeta meta, @Param("handler") ResultHandler<Map<Object, V > > handler, QueryRange range );

    @Override
    default Map selectMappedByRange( RDBTargetTableMeta meta, QueryRange range ) {
        DynamicQuerierMappedResultHandler<V> handler = new DynamicQuerierMappedResultHandler<>( meta, range );
        this.selectMappedByRange0( meta, handler, range );
        return handler.getResults();
    }

    @Override
    default List<V > selectList( RDBTargetTableMeta meta, String szExSafeSQL ) {
        DynamicQuerierEntityResultHandler<V> handler = new DynamicQuerierEntityResultHandler<>( meta );
        this.selectList0( meta, handler, szExSafeSQL );
        return handler.getResults();
    }

    @Override
    @Select( "${statement}" )
    List<Map > query ( @Param("meta") RDBTargetTableMeta meta, @Param("statement") String szStatementSQL );

    default List<V > queryVal ( RDBTargetTableMeta meta, String szStatementSQL ) {
        if( meta.getResultConverter() == null ) {
            meta.setResultConverter( new GenericResultConverter<>( meta.getValueType(), meta.getValueMetaKeys() ) );
        }
        ResultConverter<V > converter   = meta.getResultConverter();
        List<V > results = new ArrayList<>();
        List<Map > raw   = this.query( meta, szStatementSQL );
        for( Map map : raw ) {
            results.add( converter.convert( map ) );
        }

        return results;
    }

    @Override
    @Select( "SELECT MAX(${rangeKeyName}) FROM ${meta.tableName}" )
    Object getMaximumRangeVal( @Param("meta")RDBTargetTableMeta meta, @Param("rangeKeyName") String szRangeKeyName );

    @Override
    @Select( "SELECT MIN(${rangeKeyName}) FROM ${meta.tableName}" )
    Object getMinimumRangeVal( @Param("meta")RDBTargetTableMeta meta, @Param("rangeKeyName") String szRangeKeyName );

    @Override
    @SelectProvider( type = DynamicQuerierSqlBuilder.class, method = "selectListByColumn" )
    List<V > selectListByColumn( @Param("meta") RDBTargetTableMeta meta, @Param("columnKey") String szSpecificColumnKeyName, @Param("key") Object key );

    @Override
    default V selectByKey( RDBTargetTableMeta meta, Object key ) {
        DynamicQuerierEntityResultHandler<V> handler = new DynamicQuerierEntityResultHandler<>( meta );
        this.selectList0( meta, handler, String.format( " WHERE `%s` = %s", meta.getIndexKey(), SQLStrings.format( key )) );
        List<V > list = handler.getResults();
        if( list != null && !list.isEmpty() ) {
            return handler.getResults().get(0);
        }
        return null;
    }

    @InsertProvider( type = DynamicQuerierSqlBuilder.class, method = "insert" )
    void insert( @Param("meta") RDBTargetTableMeta meta, @Param("key") K key, @Param("entity") V entity );

    @Override
    @UpdateProvider( type = DynamicQuerierSqlBuilder.class, method = "updateByEntity" )
    void update( @Param("meta") RDBTargetTableMeta meta, @Param("key") K key, @Param("entity") V entity );

    @Override
    @DeleteProvider( type = DynamicQuerierSqlBuilder.class, method = "deleteByKey" )
    void deleteByKey( @Param("meta") RDBTargetTableMeta meta, @Param("key") Object key );

    @Override
    @Update( "TRUNCATE TABLE ${meta.tableName}" )
    void truncate( @Param("meta") RDBTargetTableMeta meta );

}