package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeMetadata;
import com.pinecone.hydra.service.tree.source.NodeMetadataManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GenericNodeMetadataManipinate extends NodeMetadataManipulator {
    @Insert("INSERT INTO `hydra_node_metadata` (`guid`, `scenario`, primary_impl_lang, extra_information, `level`, `description`) VALUES (#{UUID},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    @Delete("DELETE FROM `hydra_node_metadata` WHERE `guid`=#{UUID}")
    void deleteNodeMetadata(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, `guid` AS UUID, `scenario`, `primary_impl_lang` AS primaryImplLang, `extra_information` AS extraInformation, `level`, `description` FROM `hydra_node_metadata` WHERE `guid`=#{UUID}")
    GenericNodeMetadata selectNodeMetadata(@Param("UUID")GUID UUID);
    void updateNodeMetadata(GenericNodeMetadata nodeMetadata);
}
