package com.pinecone.hydra.task.kom;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.TaskFamilyMeta;

public interface TaskFamilyNode extends TaskFamilyMeta {
    long getEnumId();

    void setEnumId(long id);

    void setName(String name);

    GUID getGuid();

    void setGuid(GUID guid);

    @Override
    default Identification getId() {
        return this.getGuid();
    }

    String getScenario();

    void setScenario( String scenario );

    String getMarshallingArchitecture();

    void setMarshallingArchitecture( String marshallingArchitecture );

    String getExtraInformation();

    void setExtraInformation( String extraInformation );

    String getLevel();

    void setLevel( String level );

    String getDescription();

    void setDescription( String description );

    TaskFamilyNode apply( Map<String, Object> joEntity ) ;
}