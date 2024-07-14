package com.pinecone.slime.source.rdb;


import com.pinecone.slime.map.QueryRange;

import java.util.List;
import java.util.Map;

public interface RangedRDBQuerierDataManipulator<K, V > extends RDBQuerierDataManipulator<K, V > {
    long countsByRange                 ( RDBTargetTableMeta meta, QueryRange range );

    List selectListByRange             ( RDBTargetTableMeta meta, QueryRange range );

    Map selectMappedByRange            ( RDBTargetTableMeta meta, QueryRange range );

    Object getMaximumRangeVal          ( RDBTargetTableMeta meta, String szRangeKeyName );

    Object getMinimumRangeVal          ( RDBTargetTableMeta meta, String szRangeKeyName );
}
