package com.pinecone.hydra.account.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Authorization;
import com.pinecone.hydra.account.entity.GenericAuthorization;

import java.util.List;

public interface AuthorizationManipulator extends Pinenut {
    void insert(Authorization authorization);

    void remove(GUID authorizationGuid);

    void update(GUID authorizationGuid);

    Authorization queryCredential(GUID authorizationGuid );

    List<GenericAuthorization> queryAuthorizationByUserGuid(GUID userGuid);

    void removeAuthorizationByUserGuid(GUID userGuid);

    List<GenericAuthorization> queryAllAuthorization();

}
