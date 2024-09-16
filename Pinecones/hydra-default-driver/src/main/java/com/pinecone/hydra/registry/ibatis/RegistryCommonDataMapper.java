package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericNodeCommonData;
import com.pinecone.hydra.registry.entity.NodeCommonData;
import com.pinecone.hydra.registry.source.RegistryCommonDataManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface RegistryCommonDataMapper extends RegistryCommonDataManipulator {
    @Insert("INSERT INTO `hydra_conf_node_common_data` (`guid`) VALUES (#{guid})")
    void insert(NodeCommonData nodeCommonData);

    @Delete("DELETE FROM `hydra_conf_node_common_data` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid` FROM `hydra_conf_node_common_data` WHERE `guid`=#{guid}")
    GenericNodeCommonData getNodeCommonData(GUID guid);

    void update(NodeCommonData nodeCommonData);
}
