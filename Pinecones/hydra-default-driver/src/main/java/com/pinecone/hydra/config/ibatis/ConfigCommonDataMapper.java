package com.pinecone.hydra.config.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.GenericNodeCommonData;
import com.pinecone.hydra.config.distribute.entity.NodeCommonData;
import com.pinecone.hydra.config.distribute.source.NodeCommonDataManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ConfigCommonDataMapper extends NodeCommonDataManipulator {
    @Insert("INSERT INTO `hydra_conf_node_common_data` (`guid`) VALUES (#{guid})")
    void insert(NodeCommonData nodeCommonData);

    @Delete("DELETE FROM `hydra_conf_node_common_data` WHERE `guid`=#{guid}")
    void remove(GUID guid);

    @Select("SELECT `id`, `guid` FROM `hydra_conf_node_common_data` WHERE `guid`=#{guid}")
    GenericNodeCommonData getNodeCommonData(GUID guid);

    void update(NodeCommonData nodeCommonData);
}
