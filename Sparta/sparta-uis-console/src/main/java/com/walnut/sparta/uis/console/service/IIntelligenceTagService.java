package com.walnut.sparta.uis.console.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.dto.IntelligenceTagDto;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTag;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;

import java.util.List;

public interface IIntelligenceTagService extends Pinenut {

    IntelligenceTag createIntelligenceTag(IntelligenceTag intelligenceTag, TableMeta tableMeta);

    IntelligenceTag updateIntelligenceTag(IntelligenceTag intelligenceTag, TableMeta tableMeta);

    void deleteIntelligenceTag(GUID guid, TableMeta tableMeta);

    IntelligenceTag getIntelligenceTagById(GUID guid, TableMeta tableMeta);

    List<IntelligenceTag> getAllIntelligenceTags(TableMeta tableMeta);


    void removeIntelligenceTags(GUID intelligenceGuid, List<GUID> tagGuids, TableMeta tableMeta);


    void batchAddIntelligenceTags(GUID intelligenceGuid, List<GUID> tagGuids, TableMeta tableMeta);
}