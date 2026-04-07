package com.acorn.redqueen.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;

public abstract class ArchRedApplication implements RedApplication {
    protected ApplicationElement mApplicationElement;

    @Override
    public ApplicationElement getApplicationElement() {
        return this.mApplicationElement;
    }

    @Override
    public long getEnumId() {
        return this.mApplicationElement.getEnumId();
    }

    @Override
    public GUID getGuid() {
        return this.mApplicationElement.getGuid();
    }

    @Override
    public Identification getId() {
        return this.mApplicationElement.getId();
    }

    @Override
    public String getName() {
        return this.mApplicationElement.getName();
    }

    @Override
    public String getScenario() {
        return this.mApplicationElement.getScenario();
    }

    @Override
    public String getPrimaryImplLang() {
        return this.mApplicationElement.getPrimaryImplLang();
    }

    @Override
    public String getExtraInformation() {
        return this.mApplicationElement.getExtraInformation();
    }

    @Override
    public String getLevel() {
        return this.mApplicationElement.getLevel();
    }

    @Override
    public String getDescription() {
        return this.mApplicationElement.getDescription();
    }
}
