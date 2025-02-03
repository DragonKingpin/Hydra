package com.pinecone.hydra.account.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.GenericRole;
import com.pinecone.hydra.account.entity.Role;

public interface RoleManipulator  extends Role {

 void insert(Role role);

void remove(GUID roleGuid);

Role queryRole(GUID roleGuid );

 void updateRole(GenericRole role);

    GenericRole queryRolesByUserGuid(String userGuid);
}