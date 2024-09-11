package com.pinecone.hydra.deployment.entity.infrastructure;

import com.pinecone.framework.util.id.GUID;

public class GenericDataCenter implements DataCenter{
    private int enumId;

    private GUID guid;

    private String name;

    private String location;

    private String address;


    public GenericDataCenter() {
    }

    public GenericDataCenter(int enumId, GUID guid, String name, String location, String address) {
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
        this.location = location;
        this.address = address;
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
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return "GenericDataCenter{enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", location = " + location + ", address = " + address + "}";
    }
}
