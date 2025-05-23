package com.pinecone.hydra.proc;

import java.util.Map;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ko.CascadeInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.ArchUniformInstitutionalizedInstrument;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public class UniformProcessManager implements ProcessManager {

    protected CascadeInstrument mParentInstrument;
    protected Map<GUID, EntityNode> mEntityNodeMap;

    public UniformProcessManager () {

    }

    @Override
    public CascadeInstrument parent() {
        return this.mParentInstrument;
    }

    @Override
    public void setParent( CascadeInstrument parent ) {

    }

    @Override
    public Namespace getTargetingName() {
        return null;
    }

    @Override
    public void setTargetingName(Namespace name) {

    }

    @Override
    public String getSuperiorPathScope() {
        return "";
    }

    @Override
    public void applySuperiorPathScope(String superiorPathScope) {

    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return null;
    }

    @Override
    public KernelObjectConfig getConfig() {
        return null;
    }
}
