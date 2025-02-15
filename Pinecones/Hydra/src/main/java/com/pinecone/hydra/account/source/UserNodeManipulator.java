package com.pinecone.hydra.account.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.entity.GenericAccount;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.account.entity.Account;

import java.util.List;

public interface UserNodeManipulator extends GUIDNameManipulator {
    void insert(Account account);

    void remove(GUID userGuid);

    Account queryUser(GUID userGuid );

    List<GenericAccount> queryAllAccount();

    GenericAccount queryAccountByName(String userName);

    void update(Account account);

    GenericAccount queryAccountByUserGuid(GUID userGuid);

    List<GenericAccount> queryAccountsByGroup(GUID groupGuid);
}
