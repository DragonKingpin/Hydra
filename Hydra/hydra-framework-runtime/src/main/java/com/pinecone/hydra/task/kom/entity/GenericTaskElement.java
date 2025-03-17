package com.pinecone.hydra.task.kom.entity;

import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.kom.TasksInstrument;

import java.util.Map;

public class GenericTaskElement extends ArchServoElement implements TaskElement{
    protected String                     taskType;
    private void initSelf( Map<String, Object > joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
        if ( this.szElementaryConfig != null ) {
            this.elementaryConfig = (JSONObject) JSON.parse( this.szElementaryConfig );
        }
    }

    public GenericTaskElement() {
        super();
    }

    public GenericTaskElement( Map<String, Object > joEntity ) {
        super( joEntity );
        this.initSelf( joEntity );
    }

    public GenericTaskElement( Map<String, Object > joEntity, TasksInstrument tasksInstrument ) {
        super( joEntity, tasksInstrument );
        this.initSelf( joEntity );
    }

    public GenericTaskElement( TasksInstrument tasksInstrument ) {
        super( tasksInstrument );
    }

    @Override
    public String getServiceType() {
        return this.taskType;
    }

    @Override
    public void setServiceType( String serviceType ) {
        this.taskType = serviceType;
    }
}
