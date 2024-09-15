package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.entity.GenericTaskNodeMeta;
import com.pinecone.hydra.task.entity.TaskNodeMeta;
import com.pinecone.hydra.task.source.TaskNodeMetaManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface TaskNodeMetaMapper extends TaskNodeMetaManipulator {
    @Insert("INSERT INTO `hydra_task_task_node_meta` (`guid`) VALUES (#{guid})")
    void insert(TaskNodeMeta taskNodeMeta);
    @Delete("DELETE FROM `hydra_task_task_node_meta` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `id`, `guid` FROM `hydra_task_task_node_meta` WHERE `guid`=#{guid}")
    GenericTaskNodeMeta getTaskNodeMeta(GUID guid);

    void update(TaskNodeMeta taskNodeMeta);
}
