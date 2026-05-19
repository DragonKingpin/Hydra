package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.GenericPrivilege;
import com.pinecone.hydra.account.entity.Privilege;
import com.pinecone.hydra.account.source.PrivilegeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import java.util.List;

@IbatisDataAccessObject
public interface PrivilegeMapper extends PrivilegeManipulator {
    void insert(Privilege privilege);

    void update(Privilege privilege);

    void remove(GUID privilegeGuid);

    List<GenericPrivilege> queryAllPrivileges();

    GenericPrivilege queryPrivilege(GUID guid);

}
