package com.pinecone.hydra.service.kom.entity;


import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.hydra.service.kom.GenericNamespaceRules;
import com.pinecone.hydra.service.kom.ArchServiceFamilyNode;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.ulf.util.id.GuidAllocator;

public class GenericNamespace implements Namespace {
    protected long enumId;

    protected GUID guid;

    protected String name;

    protected GUID rulesGUID;

    protected GUIDDistributedTrieNode     distributedTreeNode;

    protected GenericNamespaceRules       classificationRules;

    protected ArchServiceFamilyNode nodeAttributes;


    protected ServicesInstrument          servicesInstrument;

    protected ServiceNamespaceManipulator namespaceManipulator;


    public GenericNamespace() {
    }

    public GenericNamespace( ServicesInstrument servicesInstrument ) {
        this.servicesInstrument = servicesInstrument;
        GuidAllocator guidAllocator = this.servicesInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
    }

    public GenericNamespace( ServicesInstrument servicesInstrument, ServiceNamespaceManipulator namespaceManipulator ) {
        this(servicesInstrument);
        this.namespaceManipulator = namespaceManipulator;
    }

    @Override
    public GUIDDistributedTrieNode getDistributedTreeNode() {
        return distributedTreeNode;
    }

    @Override
    public void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }

    @Override
    public GenericNamespaceRules getClassificationRules() {
        return classificationRules;
    }

    @Override
    public void setClassificationRules(GenericNamespaceRules classificationRules) {
        this.classificationRules = classificationRules;
    }

    @Override
    public ArchServiceFamilyNode getAttributes() {
        return nodeAttributes;
    }

    @Override
    public void setNodeCommonData(ArchServiceFamilyNode nodeAttributes) {
        this.nodeAttributes = nodeAttributes;
    }

    @Override
    public long getEnumId() {
        return enumId;
    }

    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    @Override
    public GUID getGuid() {
        return guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public GUID getRulesGUID() {
        return rulesGUID;
    }

    @Override
    public void setRulesGUID(GUID rulesGUID) {
        this.rulesGUID = rulesGUID;
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "guid"        , this.getGuid()            ),
                new KeyValue<>( "name"        , this.getName()            )
        } );
    }

    @Override
    public String toString() {
        return this.name;
    }
}
