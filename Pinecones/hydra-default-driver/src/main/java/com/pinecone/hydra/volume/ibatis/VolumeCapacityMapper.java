package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.VolumeCapacity;
import com.pinecone.hydra.storage.volume.source.VolumeCapacityManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@IbatisDataAccessObject
public interface VolumeCapacityMapper extends VolumeCapacityManipulator {
    @Update("UPDATE `hydra_uofs_volumes` SET `definition_capacity` = #{definitionCapacity}, `used_size` = #{usedSize}, `quota_capacity` = #{quotaCapacity} WHERE `guid` = #{volumeGuid}")
    void insert( VolumeCapacity volumeCapacity );
    void remove( GUID guid );
    @Select("SELECT `guid` AS volumeGuid, `definition_capacity` AS definitionCapacity, `used_size` AS userSize, `quota_capacity` AS quotaCapacity FROM `hydra_uofs_volumes` WHERE `guid` = #{guid}")
    VolumeCapacity getVolumeCapacity(GUID guid);
}
