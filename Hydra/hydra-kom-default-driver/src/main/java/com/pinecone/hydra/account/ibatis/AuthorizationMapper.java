package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Authorization;
import com.pinecone.hydra.account.entity.GenericAuthorization;
import com.pinecone.hydra.account.source.AuthorizationManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface AuthorizationMapper extends AuthorizationManipulator {
    @Insert("INSERT INTO `hydra_account_authorization` (`guid`, `user_name`, `user_guid`, `credential_guid`, `privilege_token`, `privilege_guid`, `create_time`, `update_time`) VALUES (#{guid},#{userName},#{userGuid},#{credentialGuid},#{privilrgrToken},#{privilegeGuid},#{createTime},#{updateTime})")
    void insert(Authorization authorization);

    @Delete("DELETE FROM `hydra_account_authorization` WHERE `guid` = #{authorizationGuid}")
    void remove(GUID authorizationGuid);

    @Insert("UPDATE `hydra_account_authorization` SET `privilege_token` = #{privilegeToken}, `privilege_guid` = #{privilegeGuid}, `update_time` = #{updateTime} WHERE guid = #{authorizationGuid}")
    void update(GUID authorizationGuid);

    @Select("SELECT `id`, `guid`, `user_name`, `user_guid`, `credential_guid`, `privilege_token`, `privilege_guid`, `create_time`, `update_time` FROM `hydra_account_authorization` WHERE guid = #{authorizationGuid}")
    Authorization queryCredential(GUID authorizationGuid );

    @Select("SELECT `id`, `guid`, `user_name` AS userName, `user_guid` AS userGuid, `credential_guid` AS credentialGuid , `privilege_token` AS privilegeToken, `privilege_guid` AS privilegeGuid, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_account_authorization` WHERE user_guid = #{userGuid}")
    List<GenericAuthorization> queryAuthorizationByUserGuid(GUID userGuid);

    @Delete("DELETE FROM `hydra_account_authorization` WHERE user_guid = #{userGuid}")
    void removeAuthorizationByUserGuid(GUID userGuid);

    @Select("SELECT `id`, `guid`, `user_name` AS userName, `user_guid` AS userGuid, `credential_guid` AS credentialGuid , `privilege_token` AS privilegeToken, `privilege_guid` AS privilegeGuid, `create_time` AS createTime, `update_time` AS updateTime FROM `hydra_account_authorization`")
    List<GenericAuthorization> queryAllAuthorization();
}
