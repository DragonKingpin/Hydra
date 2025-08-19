package com.walnut.sparta.uis.console.mapper;

import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.entity.Tags;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TagsMapper {
    @Insert("INSERT INTO ${tableMeta.tableName} (tag_name, tag_category, description, create_time, tag_guid) VALUES (#{tag.tagName}, #{tag.tagCategory}, #{tag.description}, #{tag.createTime}, #{tag.tagGuid})")
    void insert(@Param("tag") Tags tag, @Param("tableMeta") TableMeta tableMeta );

    @Update("UPDATE ${tableMeta.tableName} SET tag_name = #{tag.tagName}, tag_category = #{tag.tagCategory}, description = #{tag.description}, update_time = #{tag.updateTime} WHERE tag_guid = #{tag.tagGuid}")
    void update(@Param("tag") Tags tag, @Param("tableMeta") TableMeta tableMeta );

    @Delete("DELETE FROM ${tableMeta.tableName} WHERE tag_guid = #{guid}")
    void delete(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta );

    @Select("SELECT id, tag_name AS tagName, tag_category AS tagCategory, description, create_time AS createTime, tag_guid AS tagGuid FROM ${tableMeta.tableName} WHERE tag_guid = #{guid}")
    Tags selectByGuid(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta );

    @Select("SELECT id, tag_name AS tagName, tag_category AS tagCategory, description, create_time AS createTime, tag_guid AS tagGuid FROM ${tableMeta.tableName}")
    List<Tags> selectAll(@Param("tableMeta") TableMeta tableMeta );

    @Select("SELECT id, tag_name AS tagName, tag_category AS tagCategory, description, create_time AS createTime, tag_guid AS tagGuid FROM ${tableMeta.tableName} WHERE tag_name = #{tagName}")
    Tags selectByTagName(@Param("tagName") String tagName, @Param("tableMeta") TableMeta tableMeta );
}