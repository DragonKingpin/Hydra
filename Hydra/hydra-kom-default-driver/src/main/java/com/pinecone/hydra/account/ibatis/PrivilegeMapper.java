package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.GenericPrivilege;
import com.pinecone.hydra.account.source.PrivilegeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface PrivilegeMapper extends PrivilegeManipulator {
    @Insert("INSERT INTO `hydra_account_privilege` (`guid`, `name`, `privilege_code`, `create_time`, `update_time`, `type`, `parent_priv_guid`,`token`) VALUES (#{guid}, #{name}, #{privilegeCode}, #{createTime}, #{updateTime}, #{type}, #{parentPrivGuid},#{token})")
    void insert(GenericPrivilege privilege);

    @Insert("UPDATE `hydra_account_privilege` SET `guid` = #{guid}, `name` = #{name}, `privilege_code` = #{privilegeCode}, `create_time` = #{createTime}, `update_time` = #{updateTime}, `type` = #{type}, `parent_priv_guid` = #{parentPrivGuid} WHERE `guid` = #{guid}")
    void update(GenericPrivilege privilege);

    @Delete("DELETE FROM `hydra_account_privilege` WHERE `guid` = #{privilegeGuid}")
    void remove(GUID privilegeGuid);

    @Select("SELECT * FROM `hydra_account_privilege`")
    List<GenericPrivilege> queryAllPrivileges();

    @Select("SELECT id, guid, token, name, privilege_code AS 'privilegeCode', create_time AS 'createTime', update_time AS 'updateTime' ,type, parent_priv_guid AS 'parentPrivGuid' FROM `hydra_account_privilege` WHERE `guid` = #{guid}")
    GenericPrivilege queryPrivilege(GUID guid);

}
