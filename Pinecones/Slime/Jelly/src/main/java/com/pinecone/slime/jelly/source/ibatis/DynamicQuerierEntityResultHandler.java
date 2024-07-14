package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.slime.source.rdb.RDBTargetTableMeta;
import org.apache.ibatis.session.ResultContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicQuerierEntityResultHandler<V > extends ArchDynamicQuerierResultHandler<V > {
    private List<V > mResults = new ArrayList<>();

    public DynamicQuerierEntityResultHandler( RDBTargetTableMeta meta ) {
        super( meta );
    }

    @Override
    public void handleResult( ResultContext<? extends Map<Object, V > > context ) {
        Map<Object, V > resultObject = context.getResultObject();
        this.mResults.add( this.mConverter.convert( resultObject ) );
    }

    public List<V > getResults() {
        return this.mResults;
    }
}