package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.entity.GenericTaskNode;
import com.pinecone.hydra.task.entity.TaskNode;
import com.pinecone.hydra.task.source.TaskNodeManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface TaskNodeMapper extends TaskNodeManipulator {
    @Insert("INSERT INTO `hydra_task_task_node` (`guid`, `name`) VALUES (#{guid},#{name})")
    void insert(TaskNode taskNode);
    @Delete("DELETE FROM `hydra_task_task_node` WHERE `guid`=#{guid}")
    void remove(GUID guid);
    @Select("SELECT `id`, `guid`, `name` FROM `hydra_task_task_node` WHERE `guid`=#{guid}")
    GenericTaskNode getTaskNode(GUID guid);

    void update(TaskNode taskNode);
    @Select("SELECT `guid` FROM `hydra_task_task_node` WHERE `name`=#{name}")
    List<GUID> getNodeByName(String name);
}
