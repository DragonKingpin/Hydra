package com.walnut.sparta.uis.console.mapper;

import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTag;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface IntelligenceTagsMapper {
    @Insert("INSERT INTO ${tableMeta.tableName} (intelligence_guid, tag_guid, create_time) " +
            "VALUES (#{intelligenceTag.intelligenceGuid}, #{intelligenceTag.tagGuid}, #{intelligenceTag.createTime})")
    void insert(@Param("intelligenceTag") IntelligenceTag intelligenceTag, @Param("tableMeta") TableMeta tableMeta);

    @Update("UPDATE ${tableMeta.tableName} SET intelligence_guid = #{intelligenceTag.intelligenceGuid}, tag_guid = #{intelligenceTag.tagGuid}, update_time = #{intelligenceTag.updateTime} WHERE guid = #{intelligenceTag.guid}")
    void update(@Param("intelligenceTag") IntelligenceTag intelligenceTag, @Param("tableMeta") TableMeta tableMeta);

    @Delete("DELETE FROM ${tableMeta.tableName} WHERE guid = #{guid}")
    void delete(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta);

    @Delete("DELETE FROM ${tableMeta.tableName} WHERE intelligence_guid = #{intelligenceGuid}")
    void deleteByIntelligenceGuid(@Param("intelligenceGuid") GUID intelligenceGuid, @Param("tableMeta") TableMeta tableMeta);

    @Delete("DELETE FROM ${tableMeta.tableName} WHERE tag_guid = #{tagGuid}")
    void deleteByTagGuid(@Param("tagGuid") GUID tagGuid, @Param("tableMeta") TableMeta tableMeta);

    @Delete("DELETE FROM ${tableMeta.tableName} WHERE intelligence_guid = #{intelligenceGuid} AND tag_guid = #{tagGuid}")
    void deleteByIntelligenceGuidAndTagGuid(@Param("intelligenceGuid") GUID intelligenceGuid, 
                                           @Param("tagGuid") GUID tagGuid, 
                                           @Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT id,intelligence_guid AS intelligenceGuid, tag_guid AS tagGuid, create_time AS createTime FROM ${tableMeta.tableName} WHERE guid = #{guid}")
    IntelligenceTag selectByGuid(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT id,intelligence_guid AS intelligenceGuid, tag_guid AS tagGuid, create_time AS createTime FROM ${tableMeta.tableName} WHERE intelligence_guid = #{intelligenceGuid}")
    List<IntelligenceTag> selectByIntelligenceGuid(@Param("intelligenceGuid") GUID intelligenceGuid, @Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT id,intelligence_guid AS intelligenceGuid, tag_guid AS tagGuid, create_time AS createTime FROM ${tableMeta.tableName}")
    List<IntelligenceTag> selectAll(@Param("tableMeta") TableMeta tableMeta);

    @Insert({
        "<script>",
        "INSERT IGNORE INTO ${tableMeta.tableName} (intelligence_guid, tag_guid, create_time) VALUES ",
        "<foreach collection='tagGuids' item='tagGuid' separator=','>",
        "(#{intelligenceGuid}, #{tagGuid}, #{createTime})",
        "</foreach>",
        "</script>"
    })
    void batchInsert(@Param("intelligenceGuid") GUID intelligenceGuid,
                    @Param("tagGuids") List<GUID> tagGuids,
                    @Param("createTime") LocalDateTime createTime,
                    @Param("tableMeta") TableMeta tableMeta);
}