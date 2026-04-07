package com.pinecone.hydra.queue.ibatis;

import com.pinecone.framework.util.id.GUID;
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

import java.util.ArrayList;
import java.util.List;

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
            "(SELECT COUNT(*) + 1 FROM ${meta.QueueTable}",
            " WHERE priority = #{element.priority} AND ${field} = #{segmentName}),",
            "#{element.bias}, #{segmentName}",
            "</script>"
    })
    void pushBack(@Param("element") QueueElement queueElement,
                  @Param("field") String sharedSegmentField,
                  @Param("segmentName") String sharedSegmentName,
                  @Param("meta") QueueMeta meta);

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
    default List<QueueElement> batchPopFront(long currentPos, String sharedSegmentField, String sharedSegmentName, QueueMeta meta, long limit, long offset) {
        List<GenericQueueElement> queueElements = this.batchPopFront0(currentPos, sharedSegmentField, sharedSegmentName, meta, limit, offset);
        ArrayList<QueueElement> arrayList = new ArrayList<>(queueElements);
        long i = 0;
        for( QueueElement element : arrayList ) {
            Long indexPriority = this.getIndexPriority(currentPos + i, sharedSegmentField, sharedSegmentName, meta);
            element.setIndexPriority( indexPriority );
            i++;
        }
        return arrayList;
    }

    @Select({
            "<script>",
            "SELECT `id` AS mnEnumId, `object_guid` AS mObjectGuid, `priority`, `linked_priority` AS mnLinkedPriority, `bias`",
            "FROM ${meta.QueueTable}",
            "WHERE ${sharedSegmentField} = #{sharedSegmentName}",
            "ORDER BY id ASC",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<GenericQueueElement> batchPopFront0(
            @Param("currentPos") long currentPos,
            @Param("sharedSegmentField") String sharedSegmentField,
            @Param("sharedSegmentName") String sharedSegmentName,
            @Param("meta") QueueMeta meta,
            @Param("limit") long limit,
            @Param("offset") long offset
    );

    @Override
    default List<QueueElement> batchPopBack(String sharedSegmentField, String sharedSegmentName, QueueMeta meta, long limit, long offset) {
        List<GenericQueueElement> queueElements = this.batchPopBack0(sharedSegmentField, sharedSegmentName, meta, limit, offset);
        return new ArrayList<>(queueElements);
    }

    @Select({
            "<script>",
            "SELECT `id` AS mnEnumId, `object_guid` AS mObjectGuid, `priority`, `linked_priority` AS mnLinkedPriority, `bias`",
            "FROM ${meta.QueueTable}",
            "WHERE ${sharedSegmentField} = #{sharedSegmentName}",
            "ORDER BY priority DESC, linked_priority DESC",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<GenericQueueElement> batchPopBack0(
            @Param("sharedSegmentField") String sharedSegmentField,
            @Param("sharedSegmentName") String sharedSegmentName,
            @Param("meta") QueueMeta meta,
            @Param("limit") long limit,
            @Param("offset") long offset
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
    GenericQueueElement query( @Param("id") long enumId, @Param("field") String sharedSegmentField,
                        @Param("segmentName") String sharedSegmentName, @Param("meta") QueueMeta meta );

    @Override
    default List<QueueElement> fetchElementByPriority(long priority, String sharedSegmentField,
                                              String sharedSegmentName, QueueMeta meta,
                                              long limit, long offset) {
        return new ArrayList<>(this.fetchElementByPriority0( priority, sharedSegmentField, sharedSegmentName,meta,limit,offset ));
    }

    @Select({
            "<script>",
            "SELECT `id` AS enumId, `object_guid` AS objectGuid, `priority`, ",
            "`linked_priority` AS linkedPriority, `bias` ",
            "FROM ${meta.QueueTable} ",
            "WHERE priority = #{priority} ",
            "AND ${sharedSegmentField} = #{sharedSegmentName} ",
            "ORDER BY linked_priority DESC, id ASC ",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<GenericQueueElement> fetchElementByPriority0(
            @Param("priority") long priority,
            @Param("sharedSegmentField") String sharedSegmentField,
            @Param("sharedSegmentName") String sharedSegmentName,
            @Param("meta") QueueMeta meta,
            @Param("limit") long limit,
            @Param("offset") long offset
    );

    @Override
    default List<QueueElement> fetchElement(String sharedSegmentField, String sharedSegmentName,
                                    QueueMeta meta, long limit,long offset) {
        List<GenericQueueElement> elements = this.fetchElement0(sharedSegmentField, sharedSegmentName, meta, limit, offset);
        return new ArrayList<>(elements);
    }

    @Select({
            "<script>",
            "SELECT `id` AS enumId, `object_guid` AS objectGuid, `priority`, ",
            "`linked_priority` AS linkedPriority, `bias` ",
            "FROM ${meta.QueueTable} ",
            "WHERE ",
            "${sharedSegmentField} = #{sharedSegmentName} ",
            "ORDER BY linked_priority DESC, id ASC ",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<GenericQueueElement> fetchElement0(@Param("sharedSegmentField") String sharedSegmentField,
                                    @Param("sharedSegmentName") String sharedSegmentName,
                                    @Param("meta") QueueMeta meta,
                                    @Param("limit") long limit,
                                    @Param("offset") long offset);
    @Override
    @Select({
            "<script>",
            "SELECT  `object_guid` AS objectGuid ",
            "FROM ${meta.QueueTable} ",
            "WHERE ",
            "${sharedSegmentField} = #{sharedSegmentName} ",
            "ORDER BY linked_priority DESC, id ASC ",
            "LIMIT #{limit} OFFSET #{offset}",
            "</script>"
    })
    List<GUID> fetchElementGuid(@Param("sharedSegmentField") String sharedSegmentField,
                                @Param("sharedSegmentName") String sharedSegmentName,
                                @Param("meta") QueueMeta meta,
                                @Param("limit") long limit,
                                @Param("offset") long offset);

    @Override
    @Select({
            "<script>",
            "SELECT `id` AS enumId, `object_guid` AS objectGuid, `priority`," + "`linked_priority` AS linkedPriority, `bias` FROM ${meta.QueueTable}",
            "WHERE ${field} = #{segmentName}",
            "ORDER BY priority ASC, linked_priority ASC",
            "LIMIT 1 OFFSET #{index}",
            "</script>"
    })
    GenericQueueElement getByIndex(
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
