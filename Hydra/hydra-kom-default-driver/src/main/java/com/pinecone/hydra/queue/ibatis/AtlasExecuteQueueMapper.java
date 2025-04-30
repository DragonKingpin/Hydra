package com.pinecone.hydra.queue.ibatis;

import com.pinecone.hydra.unit.pqueue.DPQueueManipulator;
import com.pinecone.hydra.unit.pqueue.QueueMeta;
import com.pinecone.hydra.unit.pqueue.entity.QueueElement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AtlasExecuteQueueMapper extends DPQueueManipulator {

    @Insert({
            "<script>",
            "INSERT INTO ${queueMeta.mszQueueTableName}",
            "(object_guid, priority, linked_priority, bias, ${field})",
            "VALUES (",
            "#{element.objectGuid}, #{element.priority},",
            "(SELECT COUNT(id) + 1 FROM ${queueMeta.mszQueueTableName} WHERE priority = #{element.priority} AND ${field} = #{segmentName}),",
            "#{element.bias},",
            "#{segmentName})",
            "</script>"
    })
    void add(@Param("element") QueueElement queueElement, @Param("field") String sharedSegmentField,
             @Param("segmentName") String sharedSegmentName, @Param("queueMeta") QueueMeta queueMeta);

    @Select("SELECT `id` AS enumId, `object_guid` AS objectGuid, `priority`, `linked_priority` AS linkedPriority, `bias` FROM ${queueMeta.mszQueueTableName} " +
            "WHERE id = #{currentPos} AND ${field} = ${segmentName}")
    QueueElement peek( @Param("currentPos") long currentPos, @Param("field") String sharedSegmentField,
                       @Param("segmentName") String sharedSegmentName, @Param("queueMeta") QueueMeta queueMeta );

    @Select("SELECT COUNT(id) FROM ${queueMeta.mszQueueTableName} WHERE ${field} = ${segmentName}")
    long size( @Param("field") String sharedSegmentField, @Param("segmentName") String sharedSegmentName,
               @Param("queueMeta") QueueMeta queueMeta );

    @Delete("DELETE FROM ${queueMeta.mszQueueTableName} WHERE id = #{currentPos} AND ${field} = ${segmentName}")
    QueueElement remove( @Param("currentPos") long currentPos, @Param("field") String sharedSegmentField,
                         @Param("segmentName") String sharedSegmentName, @Param("queueMeta") QueueMeta queueMeta );

    @Select("SELECT `id` AS enumId, `object_guid` AS objectGuid, `priority`, " +
            "`linked_priority` AS linkedPriority, `bias` " +
            "FROM ${queueMeta.mszQueueTableName} WHERE id = #{id} AND ${field} = ${segmentName}")
    QueueElement query( @Param("id") long enumId, @Param("field") String sharedSegmentField,
                        @Param("segmentName") String sharedSegmentName, @Param("queueMeta") QueueMeta queueMeta );

    @Select({
            "<script>",
            "SELECT id FROM ${queueMeta.mszQueueTableName}",
            "WHERE ${sharedSegmentField} = #{sharedSegmentName}",
            "AND id > #{currentPos}",
            "ORDER BY priority ASC, linked_priority ASC",
            "LIMIT 1",
            "</script>"
    })
    long nextPos(@Param("currentPos") long currentPos,
                 @Param("sharedSegmentField") String sharedSegmentField,
                 @Param("sharedSegmentName") String sharedSegmentName,
                 @Param("queueMeta") QueueMeta queueMeta);
}
