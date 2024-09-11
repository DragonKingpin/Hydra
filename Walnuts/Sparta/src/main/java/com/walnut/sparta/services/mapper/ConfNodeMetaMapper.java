package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.ConfNodeMeta;
import com.pinecone.hydra.config.distribute.entity.GenericConfNodeMeta;
import com.pinecone.hydra.config.distribute.source.ConfNodeMetaManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface ConfNodeMetaMapper extends ConfNodeMetaManipulator {
    @Insert("INSERT INTO `hydra_conf_conf_node_meta` (`guid`) VALUES (#{guid})")
    void insert(ConfNodeMeta confNodeMeta);
    @Delete("DELETE FROM `hydra_conf_conf_node_meta` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `id`, `guid` FROM `hydra_conf_conf_node_meta` WHERE `guid`=#{guid}")
    GenericConfNodeMeta getConfNodeMeta(GUID guid);

    void update(ConfNodeMeta confNodeMeta);
}
