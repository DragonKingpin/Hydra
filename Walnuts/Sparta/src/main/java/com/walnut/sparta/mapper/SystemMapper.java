package com.walnut.sparta.mapper;

import com.walnut.sparta.entity.node;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SystemMapper {
    @Insert("insert into hydranium.hydranium_node (uuid, name, parent_uuid, base_data_uuid, node_metadata_uuid) values (#{uuid},#{name},#{parent_uuid},#{base_data_uuid},#{node_metadata_uuid})")
    void save_node(node node);
    @Select("select id, uuid, name, parent_uuid, base_data_uuid, node_metadata_uuid from hydranium.hydranium_node where uuid=#{uuid}")
    node select_node_uuid(@Param("uuid") String uuid);
    @Delete("delete from hydranium.hydranium_node where uuid=#{uuid}")
    void delete_node(@Param("uuid") String uuid);
}
