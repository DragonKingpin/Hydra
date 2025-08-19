package com.walnut.sparta.uis.console.mapper;

import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTopic;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IntelligenceTopicMapper {

    @Insert("INSERT INTO ${tableMeta.tableName} (topic_guid, topic_name, topic_path, parent_guid, create_time, update_time) VALUES (#{intelligenceTopic.topicGuid}, #{intelligenceTopic.topicName}, #{intelligenceTopic.topicPath}, #{intelligenceTopic.parentGuid}, #{intelligenceTopic.createTime}, #{intelligenceTopic.updateTime})")
    void insert(@Param("intelligenceTopic") IntelligenceTopic intelligenceTopic, @Param("tableMeta") TableMeta tableMeta );

    @Update("UPDATE ${tableMeta.tableName} SET topic_name = #{intelligenceTopic.topicName}, topic_path = #{intelligenceTopic.topicPath}, parent_guid = #{intelligenceTopic.parentGuid}, update_time = #{intelligenceTopic.updateTime} WHERE topic_guid = #{intelligenceTopic.topicGuid}")
    void update(@Param("intelligenceTopic") IntelligenceTopic intelligenceTopic, @Param("tableMeta") TableMeta tableMeta );

    @Delete("DELETE FROM ${tableMeta.tableName} WHERE topic_guid = #{guid}")
    void delete(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta );

    @Select("SELECT id, topic_guid, topic_name, topic_path, parent_guid, create_time, update_time FROM ${tableMeta.tableName} WHERE topic_guid = #{guid}")
    IntelligenceTopic selectByGuid(@Param("guid") GUID guid, @Param("tableMeta") TableMeta tableMeta );

    @Select("SELECT id, topic_guid, topic_name, topic_path, parent_guid, create_time, update_time FROM ${tableMeta.tableName}")
    List<IntelligenceTopic> selectAll(@Param("tableMeta") TableMeta tableMeta );

    @Select("SELECT id, topic_guid, topic_name, topic_path, parent_guid, create_time, update_time FROM ${tableMeta.tableName} WHERE parent_guid = #{parentGuid}")
    List<IntelligenceTopic> selectByParentGuid(@Param("parentGuid") GUID parentGuid, @Param("tableMeta") TableMeta tableMeta );
}