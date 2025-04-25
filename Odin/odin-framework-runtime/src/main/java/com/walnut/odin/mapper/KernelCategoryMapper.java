package com.walnut.odin.mapper;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.category.entity.GenericKernelCategory;
import com.walnut.odin.category.entity.KernelCategory;
import com.walnut.odin.category.source.KernelCategoryManipulator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface KernelCategoryMapper extends KernelCategoryManipulator {
    @Insert( "INSERT INTO `hydra_task_kernel_category_nodes` ( `kernel_category_name`, `kernel_category_nickname`, `kernel_category_description`) VALUES (#{kernelCategoryName},#{kernelCategoryNickName},#{kernelCategoryDescription})")
    void insert(KernelCategory kernelCategory);

    @Select( "SELECT `kernel_category_name` AS kernelCategoryName, `kernel_category_nickname` AS kernelCategoryNickName, `kernel_category_description` AS kernelCategoryDescription FROM `hydra_task_kernel_category_nodes` WHERE `kernel_category_name` = #{kernelCategoryName}")
    GenericKernelCategory queryKernelCategory(String kernelCategoryName);

    @Delete( "DELETE FROM `hydra_task_kernel_category_nodes` WHERE `kernel_category_name` = #{kernelCategoryName}")
    void remove(String kernelCategoryName);

    @Insert( "UPDATE `hydra_task_kernel_category_nodes` SET `kernel_category_nickname` = #{kernelCategoryNickName}, `kernel_category_description` = #{kernelCategoryDescription} WHERE `kernel_category_name` = #{kernelCategoryName}")
    void update(KernelCategory kernelCategory);
}
