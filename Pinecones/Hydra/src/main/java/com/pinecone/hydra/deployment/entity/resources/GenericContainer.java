package com.pinecone.hydra.deployment.entity.resources;

import com.pinecone.framework.util.id.GUID;

public class GenericContainer implements Container{
    private int enumId;
    private GUID guid;
    private String imageName;
    private String name;
    private GUID serverGuid;

    public GenericContainer() {
    }

    public GenericContainer(int enumId, GUID guid, String imageName, String name, GUID serverGuid) {
        this.enumId = enumId;
        this.guid = guid;
        this.imageName = imageName;
        this.name = name;
        this.serverGuid = serverGuid;
    }

    /**
     * 获取
     * @return enumId
     */
    public int getEnumId() {
        return enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }

    /**
     * 获取
     * @return guid
     */
    public GUID getGuid() {
        return guid;
    }

    /**
     * 设置
     * @param guid
     */
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    /**
     * 获取
     * @return imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * 设置
     * @param imageName
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return serverGuid
     */
    public GUID getServerGuid() {
        return serverGuid;
    }

    /**
     * 设置
     * @param serverGuid
     */
    public void setServerGuid(GUID serverGuid) {
        this.serverGuid = serverGuid;
    }

    public String toString() {
        return "GenericContainer{enumId = " + enumId + ", guid = " + guid + ", imageName = " + imageName + ", name = " + name + ", serverGuid = " + serverGuid + "}";
    }
}
