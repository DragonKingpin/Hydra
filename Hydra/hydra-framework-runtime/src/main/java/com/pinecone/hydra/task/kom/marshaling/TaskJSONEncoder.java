package com.pinecone.hydra.task.kom.marshaling;


import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;

public class TaskJSONEncoder implements TaskInstrumentEncoder {
    protected TaskInstrument instrument;

    public TaskJSONEncoder(TaskInstrument instrument ) {
        this.instrument = instrument;
    }

    @Override
    public Object encode( ElementNode node ) {
        return node.toJSONObject();
    }

}