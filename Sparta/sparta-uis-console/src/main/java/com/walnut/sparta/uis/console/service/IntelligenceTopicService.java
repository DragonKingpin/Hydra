package com.walnut.sparta.uis.console.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTopic;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import java.util.List;

public interface IntelligenceTopicService extends Pinenut {

    IntelligenceTopic createIntelligenceTopic(IntelligenceTopic intelligenceTopic, TableMeta tableMeta);

    IntelligenceTopic updateIntelligenceTopic(IntelligenceTopic intelligenceTopic, TableMeta tableMeta);

    void deleteIntelligenceTopic( GUID guid, TableMeta tableMeta );

    IntelligenceTopic getIntelligenceTopicByGuid(GUID guid, TableMeta tableMeta );

    List<IntelligenceTopic> getAllIntelligenceTopics(TableMeta tableMeta);
}