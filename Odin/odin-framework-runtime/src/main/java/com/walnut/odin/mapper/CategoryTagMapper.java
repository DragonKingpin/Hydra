package com.walnut.odin.mapper;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.category.entity.CategoryTag;
import com.walnut.odin.category.source.CategoryTagManipulator;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@IbatisDataAccessObject
public interface CategoryTagMapper extends CategoryTagManipulator {
    @Insert( "INSERT INTO `hydra_task_category_tag_nodes` ( `task_guid`, `kernel_category_name`, `task_category_name`) VALUES (#{taskGuid},#{kernelCategoryName},#{taskCategoryName})")
    void insert(CategoryTag categoryTag);
}
