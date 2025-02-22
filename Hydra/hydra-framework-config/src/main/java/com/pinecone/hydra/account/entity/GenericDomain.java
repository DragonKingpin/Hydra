package com.pinecone.hydra.account.entity;

import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.account.AccountManager;
import com.pinecone.hydra.account.source.DomainNodeManipulator;

public class GenericDomain extends ArchFolderElementNode  implements Domain{
    protected String                domainName;

    protected DomainNodeManipulator domainNodeManipulator;

    public GenericDomain(){
        super();
    }

    public GenericDomain(AccountManager accountManager, DomainNodeManipulator domainNodeManipulator){
        super(accountManager);

        this.accountManager = accountManager;
        this.domainNodeManipulator = domainNodeManipulator;
    }

    @Override
    public String getDomainName() {
        return this.domainName;
    }

    @Override
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }


    @Override
    public void save() {
        this.accountManager.put( this );
    }

    @Override
    public void delete() {
        this.accountManager.remove( this.guid );
    }

    @Override
    public void setDomainNodeManipulator(DomainNodeManipulator domainNodeManipulator) {
        this.domainNodeManipulator = domainNodeManipulator;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
