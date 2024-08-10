package com.walnut.sparta.mapper;

import com.walnut.sparta.entity.Node;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;


@Mapper
public interface SystemMapper {
    @Insert("INSERT INTO  `hydranium_node` (`uuid`, `name`, `parent_uuid`, `base_data_uuid`, `node_metadata_uuid`) VALUES (#{UUID},#{name},#{parentUUID},#{baseDataUUID},#{nodeMetadataUUID})")
    void saveNode(Node node);
    @Select("SELECT `id`, `uuid`, `name`, `parent_uuid`, `base_data_uuid`, `node_metadata_uuid` FROM `hydranium_node` where uuid=#{UUID}")
    Node selectNodeUUID( @Param("uuid") String uuid);
    @Delete("DELETE FROM 'hydranium_node' WHERE uuid=#{UUID}")
    void deleteNode(@Param("uuid") String uuid);
    @Select("SELECT `id`, `uuid`, `name`, `parent_uuid`, `base_data_uuid`, `node_metadata_uuid` FROM `hydranium_node`")
    List<Node> selectAllNode();
}
