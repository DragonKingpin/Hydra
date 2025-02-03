package com.pinecone.hydra.account.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.GenericPrivilege;
import com.pinecone.hydra.account.entity.Privilege;

import java.util.List;

public interface PrivilegeManipulator extends Privilege {
    void insert( Privilege privilege);

    void remove( GUID privilegeGuid);

     Privilege queryPrivilege( GUID privilegeGuid);

    List<GenericPrivilege> queryAllPrivileges();
}
