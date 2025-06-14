package com.pinecone.hydra.queue.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.iqueue.QueueExistManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface QueueExistMapper extends QueueExistManipulator {
    @Override
    @Insert("INSERT INTO `hydra_global_queue_exist` (`layer_guid`, `is_exist`) VALUES (#{layer_guid},1)")
    void insertQueueExist(GUID layerGuid );

    @Override
    @Insert("INSERT INTO `hydra_global_queue_exist` (`layer_guid`, `is_exist`) VALUES (#{layer_guid},0)")
    void insertQueueNotExist(GUID layerGuid );

    @Override
    @Update("UPDATE `hydra_global_queue_exist` SET `is_exist` = 1 WHERE `layer_guid` = #{layerGuid}")
    void setQueueExist(GUID layerGuid);

    @Override
    @Update("UPDATE `hydra_global_queue_exist` SET `is_exist` = 2 WHERE `layer_guid` = #{layerGuid}")
    void setQueueNotExist(GUID layerGuid);

    @Override
    default boolean isExist( GUID layerGuid ){
        Integer isExist = this.isExist0(layerGuid);
        if( isExist == null ) {
            this.insertQueueNotExist( layerGuid );
            return false;
        }
        return isExist == 1;
    }

    @Select("SELECT `is_exist` FROM `hydra_global_queue_exist` WHERE `layer_guid` = #{layerGuid}")
    Integer isExist0( GUID layerGuid );
}
