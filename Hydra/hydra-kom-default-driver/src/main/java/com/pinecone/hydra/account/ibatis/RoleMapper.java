package com.pinecone.hydra.account.ibatis;

import com.pinecone.hydra.account.entity.GenericRole;
import com.pinecone.hydra.account.entity.Role;
import com.pinecone.hydra.account.source.RoleManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import java.util.List;

@IbatisDataAccessObject
public interface RoleMapper extends RoleManipulator {
    void insert(Role role);

    void updateRole(GenericRole role);

    GenericRole queryRolesByUserGuid(String userGuid);

    List<GenericRole> queryAllRoles();


    void removeRoleById(int id);
}
