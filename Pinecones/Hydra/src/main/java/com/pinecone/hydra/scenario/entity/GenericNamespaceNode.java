package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericNamespaceNode implements NamespaceNode{
    private int enumId;
    private GUID guid;
    private String name;
    private GenericNamespaceNodeMeta namespaceNodeMeta;
    private GenericScenarioCommonData scenarioCommonData;

    public GenericNamespaceNode() {
    }

    public GenericNamespaceNode(int enumId, GUID guid, String name, GenericNamespaceNodeMeta namespaceNodeMeta, GenericScenarioCommonData scenarioCommonData) {
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
        this.namespaceNodeMeta = namespaceNodeMeta;
        this.scenarioCommonData = scenarioCommonData;
    }


    public int getEnumId() {
        return enumId;
    }


    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public GenericNamespaceNodeMeta getNamespaceNodeMeta() {
        return namespaceNodeMeta;
    }


    public void setNamespaceNodeMeta(GenericNamespaceNodeMeta namespaceNodeMeta) {
        this.namespaceNodeMeta = namespaceNodeMeta;
    }

    /**
     * 获取
     * @return scenarioCommonData
     */
    public GenericScenarioCommonData getScenarioCommonData() {
        return scenarioCommonData;
    }

    /**
     * 设置
     * @param scenarioCommonData
     */
    public void setScenarioCommonData(GenericScenarioCommonData scenarioCommonData) {
        this.scenarioCommonData = scenarioCommonData;
    }

    public String toString() {
        return "GenericNamespaceNode{enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", namespaceNodeMeta = " + namespaceNodeMeta + ", scenarioCommonData = " + scenarioCommonData + "}";
    }
}
