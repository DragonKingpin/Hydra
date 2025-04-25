package com.walnut.odin.mapper;


import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.category.entity.TaskCategory;
import com.walnut.odin.category.source.TaskCategoryManipulator;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@IbatisDataAccessObject
public interface TaskCategoryMapper extends TaskCategoryManipulator {
    @Insert( "INSERT INTO `hydra_task_task_category_nodes` ( `task_category_name`, `task_category_nickname`, `task_category_description`) VALUES (#{taskCategoryName},#{taskCategoryNickName},#{taskCategoryDescription})")
    void insert(TaskCategory taskCategory);
}
