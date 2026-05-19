package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.Credential;
import com.pinecone.hydra.account.source.CredentialManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface CredentialMapper extends CredentialManipulator {
    void insert(Credential credential);

    void remove(GUID credentialGuid);

    Credential queryCredential(GUID credentialGuid );
}
