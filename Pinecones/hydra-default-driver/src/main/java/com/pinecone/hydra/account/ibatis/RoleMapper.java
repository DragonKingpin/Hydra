package com.pinecone.hydra.account.ibatis;

import com.pinecone.hydra.account.entity.GenericRole;
import com.pinecone.hydra.account.entity.Role;
import com.pinecone.hydra.account.source.RoleManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

@IbatisDataAccessObject
public interface RoleMapper extends RoleManipulator {
    @Insert("INSERT INTO `hydra_account_role` (`id`, `name`, `privilege_guids`, `create_time`, `update_time`, `type`) VALUES (#{id}, #{name}, #{privilegeGuids}, #{createTime}, #{updateTime}, #{type})")
void insert(Role role);
    @Delete("DELETE FROM `hydra_account_role` WHERE `id` = #{id}")
    void remove(int id);

 @Insert("UPDATE `hydra_account_role` SET `create_time` = #{createTime}, `privilege_guids` = #{privilegeGuids}, `update_time` = #{updateTime}, `type`= #{type} WHERE `name` = #{name}")
    void updateRole(GenericRole role);

 @Select("SELECT * FROM `hydra_account_role` WHERE `name` = #{name}")
    GenericRole queryRolesByUserGuid(String userGuid);
}
