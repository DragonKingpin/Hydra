package com.pinecone.slime.jelly.source.ibatis;

import com.pinecone.slime.source.GenericResultConverter;
import com.pinecone.slime.source.rdb.RDBTargetTableMeta;
import com.pinecone.slime.source.ResultConverter;
import org.apache.ibatis.session.ResultHandler;

import java.util.Map;

public abstract class ArchDynamicQuerierResultHandler<V > implements ResultHandler<Map<Object, V > > {
    protected ResultConverter<V > mConverter ;
    protected RDBTargetTableMeta  mRDBTargetTableMeta;

    public ArchDynamicQuerierResultHandler( RDBTargetTableMeta meta ) {
        this.mRDBTargetTableMeta = meta;

        if( this.mRDBTargetTableMeta.getResultConverter() == null ) {
            this.mRDBTargetTableMeta.setResultConverter( new GenericResultConverter<>( this.mRDBTargetTableMeta.getValueType(), this.mRDBTargetTableMeta.getValueMetaKeys() ) );
        }
        this.mConverter          = this.mRDBTargetTableMeta.getResultConverter();
    }
}
