package com.pinecone.hydra.device.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.device.DeviceExtraMeta;
import com.pinecone.hydra.device.kom.DeviceInstrument;

import java.util.Map;

public class GenericVirtualMachineElement extends ArchServerElement implements VirtualMachineElement {

    protected GUID affiliateHostGuid;

    protected String imageName;

    protected String osName;

    protected String cpuArch;

    protected DeviceExtraMeta vmExtraMeta;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericVirtualMachineElement( Map<String, Object> joEntity ) {
        super(joEntity);
    }

    public GenericVirtualMachineElement( DeviceInstrument deviceInstrument ) {
        super(deviceInstrument);
    }

    public GenericVirtualMachineElement() {
        super();
    }

    public GenericVirtualMachineElement( Map<String, Object > joEntity, DeviceInstrument deviceInstrument ) {
        super( joEntity, deviceInstrument);
        this.initSelf( joEntity );
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String getLocalDomain() {
        return this.localDomain;
    }

    @Override
    public void setLocalDomain(String localDomain) {
        this.localDomain = localDomain;
    }

    @Override
    public String getWideDomain() {
        return this.wideDomain;
    }

    @Override
    public void setWideDomain(String wideDomain) {
            this.wideDomain =wideDomain ;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public GUID getAffiliateHostGuid() {
        return this.affiliateHostGuid;
    }

    @Override
    public void setAffiliateHostGuid(GUID guid) {
        this.affiliateHostGuid = guid;
    }

    @Override
    public String getImageName() {
        return this.imageName;
    }

    @Override
    public void setImageName( String imageName ) {
        this.imageName = imageName;
    }

    @Override
    public String getOsName() {
        return this.osName;
    }

    @Override
    public void setOsName( String osName ) {
        this.osName = osName;
    }

    @Override
    public String getCpuArch() {
        return this.cpuArch;
    }

    @Override
    public void setCpuArch( String cpuArch ) {
        this.cpuArch = cpuArch;
    }

    @Override
    public DeviceExtraMeta getVmExtraMeta() {
        return this.vmExtraMeta;
    }

    @Override
    public DeviceExtraMeta getExtraMeta() {
        return this.extraMeta;
    }

    @Override
    public void setExtraMeta(DeviceExtraMeta extraMeta) {
            this.extraMeta = extraMeta;
    }

    @Override
    public DeviceElement getAffiliateDevicement() {
        return null;
    }


}
