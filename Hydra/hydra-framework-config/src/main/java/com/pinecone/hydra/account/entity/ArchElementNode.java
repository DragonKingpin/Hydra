package com.pinecone.hydra.account.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.account.AccountManager;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.ulf.util.guid.GUIDs;

import java.time.LocalDateTime;

public class ArchElementNode implements ElementNode {
    protected long              enumId;

    protected String            name;

    protected GUID              guid;

    protected LocalDateTime     createTime;

    protected LocalDateTime     updateTime;

    protected AccountManager    accountManager;

    protected GuidAllocator     guidAllocator = GUIDs.newGuidAllocator();

    public ArchElementNode(){
        this.guid = guidAllocator.nextGUID();
    }

    public ArchElementNode(AccountManager accountManager){
        this.guid = this.guidAllocator.nextGUID();
        this.accountManager = accountManager;
    }
    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
