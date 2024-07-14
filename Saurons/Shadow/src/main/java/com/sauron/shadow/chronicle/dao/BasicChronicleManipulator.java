package com.sauron.shadow.chronicle.dao;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

@IbatisDataAccessObject( scope = "MySQLKingData0" )
public interface BasicChronicleManipulator {

    @Insert( "INSERT INTO ${tableName} ( `object_name`, `date_time`, `news_index` ) VALUES ( '${object_name}', '${date_time}', '${news_index}' )" )
    void insertOneNews(
            @Param( "tableName" ) String szTableName, @Param( "object_name" )
            String szObjectName, @Param( "date_time" ) String szDateTime, @Param( "news_index" ) String szNewsIndex
    );

}
