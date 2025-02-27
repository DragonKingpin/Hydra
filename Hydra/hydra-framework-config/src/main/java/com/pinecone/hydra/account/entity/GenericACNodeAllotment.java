package com.pinecone.hydra.account.entity;

import com.pinecone.hydra.account.AccountManager;
import com.pinecone.hydra.account.source.UserMasterManipulator;

public class GenericACNodeAllotment implements ACNodeAllotment{
    protected AccountManager        accountManager;

    protected UserMasterManipulator userMasterManipulator;

    public GenericACNodeAllotment(AccountManager accountManager){
        this.accountManager = accountManager;
        this.userMasterManipulator = (UserMasterManipulator) accountManager.getMasterTrieTree();
    }


    @Override
    public Domain newDomain() {
        return new GenericDomain( accountManager, userMasterManipulator.getDomainNodeManipulator() );
    }
}
