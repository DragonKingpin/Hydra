package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.slime.map.QueryRange;
import com.pinecone.slime.source.rdb.RDBTargetTableMeta;
import org.apache.ibatis.session.ResultContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicQuerierMappedResultHandler<V > extends ArchDynamicQuerierResultHandler<V > {
    private Map<Object, V > mResults = new LinkedHashMap<>();
    private QueryRange      mRange;

    public DynamicQuerierMappedResultHandler( RDBTargetTableMeta meta, QueryRange range ) {
        super( meta );

        this.mRange = range;
    }

    @Override
    public void handleResult( ResultContext<? extends Map<Object, V > > context ) {
        Map<Object, V > resultObject = context.getResultObject();
        String szRangeKey = this.mRange.getRangeKey();
        Object keyVal = resultObject.get( szRangeKey );
        resultObject.remove( szRangeKey );

        this.mResults.put( keyVal, this.mConverter.convert( resultObject ) );
    }

    public Map<Object, V > getResults() {
        return this.mResults;
    }
}