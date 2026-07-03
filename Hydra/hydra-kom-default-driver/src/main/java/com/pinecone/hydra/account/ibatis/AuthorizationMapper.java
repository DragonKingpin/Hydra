package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Authorization;
import com.pinecone.hydra.account.entity.GenericAuthorization;
import com.pinecone.hydra.account.source.AuthorizationManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import java.util.List;

@IbatisDataAccessObject
public interface AuthorizationMapper extends AuthorizationManipulator {
    void insert(Authorization authorization);

    void remove(GUID authorizationGuid);

    void update(GUID authorizationGuid);

    Authorization queryCredential(GUID authorizationGuid );

    List<GenericAuthorization> queryAuthorizationByUserGuid(GUID userGuid);

    void removeAuthorizationByUserGuid(GUID userGuid);

    List<GenericAuthorization> queryAllAuthorization();
}
