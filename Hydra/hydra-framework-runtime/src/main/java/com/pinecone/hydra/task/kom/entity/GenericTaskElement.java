package com.pinecone.hydra.task.kom.entity;

import java.util.Map;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.kom.ServiceInstrument;

public class GenericTaskElement extends ArchServoElement implements TaskElement {
    protected String                     serviceType;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject)JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericTaskElement() {
        super();
    }

    public GenericTaskElement(Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }

    public GenericTaskElement(Map<String, Object > joEntity, ServiceInstrument serviceInstrument) {
        super( joEntity, serviceInstrument);
        this.initSelf( joEntity );
    }

    public GenericTaskElement(ServiceInstrument serviceInstrument) {
        super(serviceInstrument);
    }

    @Override
    public String getServiceType() {
        return this.serviceType;
    }

    @Override
    public void setServiceType( String serviceType ) {
        this.serviceType = serviceType;
    }

}