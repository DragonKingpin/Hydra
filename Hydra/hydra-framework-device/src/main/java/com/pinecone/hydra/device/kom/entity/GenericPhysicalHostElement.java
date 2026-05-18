package com.pinecone.hydra.device.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.device.kom.DeviceInstrument;

import java.util.Map;

public class GenericPhysicalHostElement extends ArchServerElement implements PhysicalHostElement {

    protected String hardwareSpecs;

    protected String osName;

    protected String cpuArch;

    protected Integer cpuCores;

    protected Long memoryMb;

    protected Long storageGb;

    protected Integer gpuCount;

    protected String gpuModel;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericPhysicalHostElement(Map<String, Object> joEntity) {
        super(joEntity);
    }

    public GenericPhysicalHostElement(DeviceInstrument deviceInstrument) {
        super(deviceInstrument);
    }

    public GenericPhysicalHostElement() {
        super();
    }

    public GenericPhysicalHostElement( Map<String, Object > joEntity, DeviceInstrument deviceInstrument ) {
        super( joEntity, deviceInstrument);
        this.initSelf( joEntity );
    }


    @Override
    public void setHardwareSpecs(String hardwareSpecs) {
        this.hardwareSpecs = hardwareSpecs;
    }

    @Override
    public String getHardwareSpecs() {
        return this.hardwareSpecs;
    }

    @Override
    public void setOsName( String osName ) {
        this.osName = osName;
    }

    @Override
    public String getOsName() {
        return this.osName;
    }

    @Override
    public void setCpuArch( String cpuArch ) {
        this.cpuArch = cpuArch;
    }

    @Override
    public String getCpuArch() {
        return this.cpuArch;
    }

    @Override
    public void setCpuCores( Integer cpuCores ) {
        this.cpuCores = cpuCores;
    }

    @Override
    public Integer getCpuCores() {
        return this.cpuCores;
    }

    @Override
    public void setMemoryMb( Long memoryMb ) {
        this.memoryMb = memoryMb;
    }

    @Override
    public Long getMemoryMb() {
        return this.memoryMb;
    }

    @Override
    public void setStorageGb( Long storageGb ) {
        this.storageGb = storageGb;
    }

    @Override
    public Long getStorageGb() {
        return this.storageGb;
    }

    @Override
    public void setGpuCount( Integer gpuCount ) {
        this.gpuCount = gpuCount;
    }

    @Override
    public Integer getGpuCount() {
        return this.gpuCount;
    }

    @Override
    public void setGpuModel( String gpuModel ) {
        this.gpuModel = gpuModel;
    }

    @Override
    public String getGpuModel() {
        return this.gpuModel;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return this.status;
    }
    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


}
