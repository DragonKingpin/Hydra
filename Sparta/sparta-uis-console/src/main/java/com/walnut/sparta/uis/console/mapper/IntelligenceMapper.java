package com.walnut.sparta.uis.console.mapper;

import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.entity.Intelligence;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;


@Mapper
public interface IntelligenceMapper  {

    @Insert("INSERT INTO ${tableMeta.tableName} (intelligence_guid, title, summary, source_url, collect_time, update_time, topic_name, topic_guid, ttl, status, content_hash) " +
            "VALUES (#{intelligence.intelligenceGuid}, #{intelligence.title}, #{intelligence.summary}, #{intelligence.sourceUrl}, #{intelligence.collectTime}, #{intelligence.updateTime}, #{intelligence.topicName}, #{intelligence.topicGuid}, #{intelligence.ttl}, #{intelligence.status}, #{intelligence.contentHash})")
    void insert(@Param("intelligence") Intelligence intelligence, @Param("tableMeta") TableMeta tableMeta);

    @Update("UPDATE ${tableMeta.tableName} SET title = #{intelligence.title}, summary = #{intelligence.summary}, source_url = #{intelligence.sourceUrl}, collect_time = #{intelligence.collectTime}, update_time = #{intelligence.updateTime}, topic_name = #{intelligence.topicName}, topic_guid = #{intelligence.topicGuid}, ttl = #{intelligence.ttl}, status = #{intelligence.status}, content_hash = #{intelligence.contentHash} WHERE intelligence_guid = #{intelligence.intelligenceGuid}")
    void update(@Param("intelligence") Intelligence intelligence, @Param("tableMeta") TableMeta tableMeta);

    @Delete("DELETE FROM ${tableMeta.tableName} WHERE intelligence_guid = #{guid}")
    void delete(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT id, intelligence_guid AS intelligenceGuid, title, summary, source_url AS sourceUrl, collect_time AS collectTime, update_time AS updateTime, topic_name, topic_guid AS topicGuid, ttl, status, content_hash AS contentHash FROM ${tableMeta.tableName} WHERE intelligence_guid = #{guid}")
    Intelligence selectByGuid(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT id, intelligence_guid AS intelligenceGuid, title, summary, source_url AS sourceUrl, collect_time AS collectTime, update_time AS updateTime, topic_name, topic_guid AS topicGuid, ttl, status, content_hash AS contentHash FROM ${tableMeta.tableName}")
    List<Intelligence> selectAll(@Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT COUNT(DISTINCT i.intelligence_guid) " +
            "FROM ${tableMeta.tableName} i " +
            "LEFT JOIN intelligence_tags it ON i.intelligence_guid = it.intelligence_guid " +
            "WHERE " +
            "(#{topicGuid} IS NULL OR i.topic_guid = #{topicGuid}) " +
            "AND (#{status} IS NULL OR i.status = #{status}) " +
            "AND (#{tagGuid} IS NULL OR it.tag_guid = #{tagGuid})")
    long countByCondition(@Param("topicGuid") GUID topicGuid,
                          @Param("status") String status,
                          @Param("tagGuid") GUID tagGuid,
                          @Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT id, intelligenceGuid, title, summary, sourceUrl, collectTime, updateTime, topic_name, topicGuid, ttl, status, contentHash FROM (" +
            "  SELECT i.id, i.intelligence_guid AS intelligenceGuid, i.title, i.summary, " +
            "         i.source_url AS sourceUrl, i.collect_time AS collectTime, " +
            "         i.update_time AS updateTime, i.topic_name, i.topic_guid AS topicGuid, " +
            "         i.ttl, i.status, i.content_hash AS contentHash " +
            "  FROM ${tableMeta.tableName} i " +
            "  LEFT JOIN intelligence_tags it ON i.intelligence_guid = it.intelligence_guid " +
            "  WHERE " +
            "    (#{topicGuid} IS NULL OR i.topic_guid = #{topicGuid}) " +
            "    AND (#{status} IS NULL OR #{status} = '' OR i.status = #{status}) " +
            "    AND (#{tagGuid} IS NULL OR it.tag_guid = #{tagGuid}) " +
            "  GROUP BY i.intelligence_guid " +
            ") AS grouped_data " +
            "ORDER BY updateTime DESC " +
            "LIMIT #{offset}, #{size}")
    List<Intelligence> selectByCondition(@Param("topicGuid") GUID topicGuid,
                                         @Param("status") String status,
                                         @Param("tagGuid") GUID tagGuid,
                                         @Param("offset") int offset,
                                         @Param("size") int size,
                                         @Param("tableMeta") TableMeta tableMeta);

    @Select("SELECT id, intelligence_guid AS intelligenceGuid, title, summary, source_url AS sourceUrl, collect_time AS collectTime, update_time AS updateTime, topic_name, topic_guid AS topicGuid, ttl, status, content_hash AS contentHash FROM ${tableMeta.tableName} WHERE topic_guid = #{topicGuid}")
    List<Intelligence> selectByTopicGuid(@Param("topicGuid") GUID topicGuid, @Param("tableMeta") TableMeta tableMeta);

    @Update("UPDATE ${tableMeta.tableName} SET topic_name = #{topicName}, update_time = #{updateTime} WHERE topic_guid = #{topicGuid}")
    void updateTopicNameByTopicGuid(@Param("topicGuid") GUID topicGuid, @Param("topicName") String topicName, @Param("updateTime") LocalDateTime updateTime, @Param("tableMeta") TableMeta tableMeta);
}