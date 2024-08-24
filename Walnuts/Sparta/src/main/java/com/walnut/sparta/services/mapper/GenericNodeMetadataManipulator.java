package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GenericNodeMetadataManipulator extends NodeMetadataManipulator {
    @Insert("INSERT INTO `hydra_node_metadata` (`guid`, `scenario`, primary_impl_lang, extra_information, `level`, `description`) VALUES (#{guid},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void saveNodeMetadata(GenericNodeCommonData nodeMetadata);
    @Delete("DELETE FROM `hydra_node_metadata` WHERE `guid`=#{guid}")
    void deleteNodeMetadata(@Param("guid")GUID guid);
    @Select("SELECT `id`, `guid`, `scenario`, `primary_impl_lang` AS primaryImplLang, `extra_information` AS extraInformation, `level`, `description` FROM `hydra_node_metadata` WHERE `guid`=#{guid}")
    GenericNodeCommonData selectNodeMetadata(@Param("guid")GUID guid);
    void updateNodeMetadata(GenericNodeCommonData nodeMetadata);
}
