package com.pinecone.hydra.deployment.entity.resources;

import com.pinecone.framework.util.id.GUID;

public class GenericServer implements Server {
    private int enumId;
    private GUID guid;
    private String hostName;
    private String ipAddress;
    private String macAddress;
    private GUID dataCenterGuid;

    public GenericServer() {
    }

    public GenericServer(int enumId, GUID guid, String hostName, String ipAddress, String macAddress, GUID dataCenterGuid) {
        this.enumId = enumId;
        this.guid = guid;
        this.hostName = hostName;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.dataCenterGuid = dataCenterGuid;
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
     * @return hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * 设置
     * @param hostName
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * 获取
     * @return ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 设置
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * 获取
     * @return macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * 设置
     * @param macAddress
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * 获取
     * @return dataCenterGuid
     */
    public GUID getDataCenterGuid() {
        return dataCenterGuid;
    }

    /**
     * 设置
     * @param dataCenterGuid
     */
    public void setDataCenterGuid(GUID dataCenterGuid) {
        this.dataCenterGuid = dataCenterGuid;
    }

    public String toString() {
        return "GenericServer{enumId = " + enumId + ", guid = " + guid + ", hostName = " + hostName + ", ipAddress = " + ipAddress + ", macAddress = " + macAddress + ", dataCenterGuid = " + dataCenterGuid + "}";
    }
}
