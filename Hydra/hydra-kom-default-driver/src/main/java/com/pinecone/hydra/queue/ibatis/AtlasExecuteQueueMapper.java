package com.pinecone.hydra.queue.ibatis;

import com.pinecone.hydra.unit.iqueue.DPQueueManipulator;
import com.pinecone.hydra.unit.iqueue.QueueMeta;
import com.pinecone.hydra.unit.iqueue.entity.GenericQueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
@IbatisDataAccessObject
public interface AtlasExecuteQueueMapper extends DPQueueManipulator {

    @Override
    @Insert({
            "<script>",
            "INSERT INTO ${meta.QueueTable}",
            "(object_guid, priority, linked_priority, bias, ${field})",
            "SELECT ",
            "#{element.objectGuid}, #{element.priority},",
            "COUNT(id) + 1, #{element.bias}, #{segmentName}",
            "FROM (SELECT `id` AS mnEnumId, `object_guid` AS mObjectGuid, `priority`, `linked_priority` AS mnLinkedPriority, `bias` FROM ${meta.QueueTable}) AS tmp",
            "WHERE priority = #{element.priority} AND ${field} = #{segmentName}",
            "</script>"
    })
    void pushBack(@Param("element") QueueElement queueElement, @Param("field") String sharedSegmentField,
             @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta meta);

    @Override
    @Insert({
            "<script>",
            "INSERT INTO ${meta.QueueTable}",
            "(object_guid, priority, linked_priority, bias, ${field})",
            "SELECT ",
            "#{element.objectGuid}, #{element.priority},",
            "1, #{element.bias}, #{segmentName}",
            "FROM (SELECT `id` AS mnEnumId, `object_guid` AS mObjectGuid, `priority`, `linked_priority` AS mnLinkedPriority, `bias` FROM ${meta.QueueTable}) AS tmp",
            "WHERE priority = #{element.priority} AND ${field} = #{segmentName}",
            "</script>"
    })
    void pushFront(@Param("element") QueueElement queueElement, @Param("field") String sharedSegmentField,
                   @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta meta);

    @Override
    @Update({
            "<script>",
            "UPDATE ${meta.QueueTable}",
            "SET linked_priority = linked_priority + 1",
            "WHERE priority = #{element.priority}",
            "AND ${field} = #{segmentName}",
            "AND object_guid != #{element.objectGuid}",
            "</script>"
    })
    void incrementLinkedPriorities(@Param("element") QueueElement queueElement, @Param("field") String sharedSegmentField,
                                   @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta meta);

    @Override
    @Select({
            "<script>",
            "SELECT `id` AS mnEnumId, `object_guid` AS mObjectGuid, `priority`, `linked_priority` AS mnLinkedPriority, `bias`",
            "FROM ${meta.QueueTable}",
            "WHERE id = #{currentPos} AND ${field} = #{segmentName}",
            "</script>"
    })
    GenericQueueElement popFront (
            @Param("currentPos") long currentPos, @Param("field") String sharedSegmentField,
            @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta meta
    );

    @Override
    @Select({
            "<script>",
            "SELECT `id` AS mnEnumId, `object_guid` AS mObjectGuid, `priority`, `linked_priority` AS mnLinkedPriority, `bias`",
            "FROM ${meta.QueueTable}",
            "WHERE ${field} = #{segmentName}",
            "ORDER BY priority DESC, linked_priority DESC",
            "LIMIT 1",
            "</script>"
    })
    QueueElement popBack(
            @Param("field") String sharedSegmentField,
            @Param("segmentName") String sharedSegmentName,
            @Param("meta") QueueMeta meta
    );

    @Override
    @Select({
            "<script>",
            "SELECT COUNT(id) FROM ${meta.QueueTable}",
            "WHERE ${field} = #{segmentName}",
            "</script>"
    })
    long queryQueueSize( @Param("field") String sharedSegmentField, @Param("segmentName") String sharedSegmentName,
               @Param("meta") QueueMeta meta );

    @Delete("DELETE FROM ${meta.QueueTable} WHERE id = #{currentPos} AND ${field} = ${segmentName}")
    QueueElement remove( @Param("currentPos") long currentPos, @Param("field") String sharedSegmentField,
                         @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta meta );

    @Override
    @Select("SELECT `id` AS enumId, `object_guid` AS objectGuid, `priority`, " +
            "`linked_priority` AS linkedPriority, `bias` " +
            "FROM ${meta.QueueTable} WHERE id = #{id} AND ${field} = ${segmentName}")
    QueueElement query( @Param("id") long enumId, @Param("field") String sharedSegmentField,
                        @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta meta );

    @Override
    @Select({
            "<script>",
            "SELECT `id` AS enumId, `object_guid` AS objectGuid, `priority`," + "`linked_priority` AS linkedPriority, `bias` FROM ${meta.QueueTable}",
            "WHERE ${field} = #{segmentName}",
            "ORDER BY priority ASC, linked_priority ASC",
            "LIMIT 1 OFFSET #{index}",
            "</script>"
    })
    QueueElement getByIndex(
            @Param("index") long index,
            @Param("field") String sharedSegmentField,
            @Param("segmentName") String sharedSegmentName,
            @Param("meta") QueueMeta meta
    );

    @Override
    @Select({
            "<script>",
            "SELECT id FROM ${meta.queueTable}",
            "WHERE ${sharedSegmentField} = #{sharedSegmentName}",
            "AND id > #{currentPos}",
            "ORDER BY priority ASC, linked_priority ASC",
            "LIMIT 1",
            "</script>"
    })
    Long nextPos(@Param("currentPos") long currentPos,
                 @Param("sharedSegmentField") String sharedSegmentField,
                 @Param("sharedSegmentName") String sharedSegmentName,
                 @Param("meta") QueueMeta meta);

    @Override
    @Select({
            "<script>",
            "SELECT COUNT(id) AS rank_value",
            "FROM ${meta.QueueTable}",
            "WHERE ${sharedSegmentField} = #{sharedSegmentName}",
            "AND (priority &lt; (SELECT priority FROM ${meta.QueueTable} WHERE id = #{currentPos})",
            "     OR (priority = (SELECT priority FROM ${meta.QueueTable} WHERE id = #{currentPos})",
            "         AND linked_priority &lt; (SELECT linked_priority FROM ${meta.QueueTable} WHERE id = #{currentPos})))",
            "</script>"
    })
    Long getIndexPriority(@Param("currentPos") long currentPos,
                          @Param("sharedSegmentField") String sharedSegmentField,
                          @Param("sharedSegmentName") String sharedSegmentName,
                          @Param("meta") QueueMeta meta);
}
