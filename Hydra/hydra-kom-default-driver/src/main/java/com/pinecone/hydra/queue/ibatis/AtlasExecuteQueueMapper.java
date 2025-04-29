package com.pinecone.hydra.queue.ibatis;

import com.pinecone.hydra.unit.pqueue.DPQueueManipulator;
import com.pinecone.hydra.unit.pqueue.entity.QueueElement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface AtlasExecuteQueueMapper extends DPQueueManipulator {
    @Insert("INSERT INTO `hydra_atlas_execute_queue` (`object_guid`, `priority`, `bias`) VALUES (#{objectGuid},#{priority},#{bias})")
    void add(QueueElement queueElement);

    default QueueElement poll() {
        QueueElement peek = this.peek();
        if( peek == null ) {
            return peek;
        }
        this.remove( peek );
        return peek;
    }

    @Select("SELECT `id` AS enmuId, `object_guid`, `priority`, `bias` FROM `hydra_atlas_execute_queue` ORDER BY `id` LIMIT 1")
    QueueElement peek();

    @Select("SELECT COUNT(id) FROM hydra_atlas_execute_queue")
    long size();

    @Delete("DELETE FROM hydra_atlas_execute_queue WHERE id = #{enumId} AND object_guid = #{objectId}")
    QueueElement remove( QueueElement queueElement );

    void update( QueueElement queueElement );

}
