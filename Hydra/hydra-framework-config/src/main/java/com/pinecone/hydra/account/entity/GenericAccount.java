package com.pinecone.hydra.account.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.account.AccountManager;

public class GenericAccount extends ArchElementNode implements Account {
    protected String            nickName;

    protected String            kernelCredential;

    protected GUID              credentialGuid;

    protected String            kernelGroupType;

    protected String            role;

    public GenericAccount(){
        super();
    }

    public GenericAccount(AccountManager accountManager){
        super(accountManager);
    }


    @Override
    public String getNickName() {
        return this.nickName;
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getKernelCredential() {
        return this.kernelCredential;
    }

    @Override
    public void setKernelCredential(String kernelCredential) {
        this.kernelCredential = kernelCredential;
    }

    @Override
    public GUID getCredentialGuid() {
        return this.credentialGuid;
    }

    @Override
    public void setCredentialGuid(GUID credentialGuid) {
        this.credentialGuid = credentialGuid;
    }

    @Override
    public String getKernelGroupType() {
        return this.kernelGroupType;
    }

    @Override
    public void setKernelGroupType(String kernelGroupType) {
        this.kernelGroupType = kernelGroupType;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
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
