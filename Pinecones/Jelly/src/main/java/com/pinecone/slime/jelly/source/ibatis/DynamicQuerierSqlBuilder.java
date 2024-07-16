package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.slime.map.QueryRange;
import com.pinecone.slime.source.rdb.RDBTargetTableMeta;
import com.pinecone.slime.source.ResultConverter;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class DynamicQuerierSqlBuilder {

    public static SQL assembleSelectSQL( RDBTargetTableMeta meta, Set<String > keys ) {
        return new SQL() {{
            if ( keys.isEmpty() ) {
                SELECT("*");
            }
            else {
                SELECT( String.join( ", ", keys ) );
            }
            FROM( meta.getTableName() );
        }};
    }

    public static String selectList  ( Map<String, Object> params ) {
        RDBTargetTableMeta meta = (RDBTargetTableMeta) params.get( "meta" );
        Set<String > keys       = meta.getValueMetaKeys();

        SQL sql = DynamicQuerierSqlBuilder.assembleSelectSQL( meta, keys );
        return sql.toString() + "${exSafeSQL}";
    }

    public static String selectObjectByRange( Map<String, Object> params, boolean bWithRangeKey ) {
        RDBTargetTableMeta meta = (RDBTargetTableMeta) params.get("meta");
        QueryRange        range = (QueryRange) params.get("range");
        Set<String >       keys = meta.getValueMetaKeys();

        if( range != null && bWithRangeKey ) {
            keys = new TreeSet<>( keys );
            keys.add( range.getRangeKey() );
        }

        SQL sql = DynamicQuerierSqlBuilder.assembleSelectSQL( meta, keys );

        if ( range != null ) {
            sql.WHERE( range.getRangeKey() + " >= #{range.min}" );
            sql.WHERE( range.getRangeKey() + " <= #{range.max}" );
        }
        return sql.toString();
    }

    public static String selectListByRange( Map<String, Object> params ) {
        return DynamicQuerierSqlBuilder.selectObjectByRange( params, false );
    }

    public static String selectMappedByRange( Map<String, Object> params ) {
        return DynamicQuerierSqlBuilder.selectObjectByRange( params, true );
    }

    public static String selectListByColumn  ( Map<String, Object> params ) {
        RDBTargetTableMeta meta = (RDBTargetTableMeta) params.get( "meta" );
        Set<String > keys       = meta.getValueMetaKeys();

        SQL sql = new SQL() {{
            if ( keys.isEmpty() ) {
                SELECT("*");
            }
            else {
                SELECT( String.join( ", ", keys ) );
            }
            FROM( meta.getTableName() );

            WHERE( "#{columnKey} = #{key}" );
        }};
        return sql.toString();
    }

    public static String insert      ( Map<String, Object> params ) {
        RDBTargetTableMeta meta = (RDBTargetTableMeta) params.get("meta");
        Object              key = params.get("key");
        Object           entity = params.get("entity");
        Set<String >       keys = meta.getValueMetaKeys();

        SQL sql = new SQL() {{
            INSERT_INTO( meta.getTableName() );
            if ( key != null ) {
                String szIdxKey = meta.getIndexKey();
                VALUES( szIdxKey, "#{key}" );
                keys.remove( szIdxKey );
            }

            if( ResultConverter.isPrimitiveOrSpecialType( entity.getClass() ) ) {
                keys.forEach( k -> VALUES( k, "#{entity}" ));
            }
            else {
                keys.forEach( k -> VALUES( k, "#{entity." + k + "}" ));
            }
        }};

        return sql.toString();
    }

    public static String updateByEntity ( Map<String, Object> params ) {
        RDBTargetTableMeta meta = (RDBTargetTableMeta) params.get("meta");
        Object entity = params.get("entity");
        Set<String> keys = meta.getValueMetaKeys();
        Object key = params.get("key");

        SQL sql = new SQL() {{
            UPDATE(meta.getTableName());

            if( ResultConverter.isPrimitiveOrSpecialType( entity.getClass() ) ) {
                keys.forEach(key -> SET(key + " = #{entity}"));
                if( key != null ) {
                    WHERE(key + " = #{entity}");
                }
                else {
                    WHERE(meta.getIndexKey() + " = #{entity}");
                }
            }
            else {
                keys.forEach(key -> SET(key + " = #{entity." + key + "}"));
                WHERE(meta.getIndexKey() + " = #{entity." + meta.getIndexKey() + "}");
            }
        }};

        return sql.toString();
    }

    public static String deleteByKey ( Map<String, Object> params ) {
        RDBTargetTableMeta meta = (RDBTargetTableMeta) params.get("meta");
        Object key = params.get("key");

        SQL sql = new SQL() {{
            DELETE_FROM(meta.getTableName());
            WHERE(meta.getIndexKey() + " = #{key}");
        }};

        return sql.toString();
    }

}