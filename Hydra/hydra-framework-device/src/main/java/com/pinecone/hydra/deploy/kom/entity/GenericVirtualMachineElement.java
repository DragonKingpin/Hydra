package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.deploy.DeployExtraMeta;
import com.pinecone.hydra.deploy.kom.DeployInstrument;

import java.util.Map;

public class GenericVirtualMachineElement extends ArchServerElement implements VirtualMachineElement {

    protected GUID affiliateHostGuid;

    protected String status;

    protected DeployExtraMeta vmExtraMeta;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericVirtualMachineElement( Map<String, Object> joEntity ) {
        super(joEntity);
    }

    public GenericVirtualMachineElement( DeployInstrument deployInstrument ) {
        super(deployInstrument);
    }

    public GenericVirtualMachineElement() {
        super();
    }

    public GenericVirtualMachineElement( Map<String, Object > joEntity, DeployInstrument deployInstrument ) {
        super( joEntity, deployInstrument);
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
    public DeployExtraMeta getVmExtraMeta() {
        return this.vmExtraMeta;
    }

    @Override
    public DeployExtraMeta getExtraMeta() {
        return this.extraMeta;
    }

    @Override
    public void setExtraMeta(DeployExtraMeta extraMeta) {
            this.extraMeta = extraMeta;
    }

    @Override
    public DeployElement getAffiliateDeployment() {
        return null;
    }
}
