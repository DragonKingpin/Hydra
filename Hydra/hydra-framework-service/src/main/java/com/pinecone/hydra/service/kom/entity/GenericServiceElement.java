package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.service.kom.ServicesInstrument;

import java.util.Map;

public class GenericServiceElement extends ArchServoElement implements ServiceElement {
    protected String                     serviceType;

    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject)JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericServiceElement() {
        super();
    }

    public GenericServiceElement( Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }

    public GenericServiceElement( Map<String, Object > joEntity, ServicesInstrument servicesInstrument ) {
        super( joEntity, servicesInstrument );
        this.initSelf( joEntity );
    }

    public GenericServiceElement( ServicesInstrument servicesInstrument ) {
        super( servicesInstrument );
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