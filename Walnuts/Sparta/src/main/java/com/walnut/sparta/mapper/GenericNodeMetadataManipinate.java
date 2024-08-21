package com.walnut.sparta.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.unit.udsn.NodeMetadataManipinate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GenericNodeMetadataManipinate extends NodeMetadataManipinate {
    @Insert("INSERT INTO `hydra_node_metadata` (UUID, `scenario`, primary_impl_lang, extra_information, `level`, `description`) VALUES (#{UUID},#{scenario},#{primaryImplLang},#{extraInformation},#{level},#{description})")
    void saveNodeMetadata(GenericNodeMetadata nodeMetadata);
    @Delete("DELETE FROM `hydra_node_metadata` WHERE UUID=#{UUID}")
    void deleteNodeMetadata(@Param("UUID")GUID UUID);
    @Select("SELECT `id`, UUID, `scenario`, `primary_impl_lang` AS primaryImplLang, `extra_information` AS extraInformation, `level`, `description` FROM `hydra_node_metadata` WHERE UUID=#{UUID}")
    GenericNodeMetadata selectNodeMetadata(@Param("UUID")GUID UUID);
    void updateNodeMetadata(GenericNodeMetadata nodeMetadata);
}
