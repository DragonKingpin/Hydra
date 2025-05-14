package com.pinecone.hydra.queue.ibatis;

import com.pinecone.hydra.unit.iqueue.DPStratumQueueManipulator;
import com.pinecone.hydra.unit.iqueue.QueueMeta;
import com.pinecone.hydra.unit.iqueue.entity.GenericStratumQueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.iqueue.entity.QueueStratumElement;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface AtlasStratumQueueMapper extends DPStratumQueueManipulator {
    @Override
    @Insert({
            "<script>",
            "INSERT INTO ${meta.QueueTable}",
            "(object_guid, priority, linked_priority, bias, ${field}, stratum)",
            "SELECT ",
            "#{element.objectGuid}, #{element.priority},",
            "(SELECT COUNT(*) + 1 FROM ${meta.QueueTable}",
            " WHERE priority = #{element.priority} AND ${field} = #{segmentName}),",
            "#{element.bias}, #{segmentName}, #{element.stratum}",
            "WHERE NOT EXISTS (",
            "  SELECT 1 FROM ${meta.QueueTable}",
            "  WHERE object_guid = #{element.objectGuid}",
            "  AND ${field} = #{segmentName}",
            ")",
            "</script>"
    })
    void pushBack(@Param("element") QueueStratumElement queueElement,
                  @Param("field") String sharedSegmentField,
                  @Param("segmentName") String sharedSegmentName,
                  @Param("meta") QueueMeta meta);

    @Override
    @Select({
            "<script>",
            "SELECT `id` AS mnEnumId, `object_guid` AS mObjectGuid, `priority`, `linked_priority` AS mnLinkedPriority, `bias`, `stratum`",
            "FROM ${meta.QueueTable}",
            "WHERE ${field} = #{segmentName}",
            "ORDER BY id ASC",
            "LIMIT 1",
            "</script>"
    })
    GenericStratumQueueElement popFront( @Param("field") String sharedSegmentField, @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta queueMeta );

    @Override
    @Delete({
            "<script>",
            "DELETE FROM ${meta.QueueTable}",
            "WHERE ${field} = #{segmentName}",
            "ORDER BY id ASC",
            "LIMIT 1",
            "</script>"
    })
    void removeFront(@Param("field") String sharedSegmentField, @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta queueMeta);

    @Override
    @Select({
            "<script>",
            "SELECT COUNT(`id`)",
            "FROM ${meta.QueueTable}",
            "WHERE ${field} = #{segmentName}",
            "</script>"
    })
    long isEmpty(@Param("field") String sharedSegmentField, @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta queueMeta);
}
