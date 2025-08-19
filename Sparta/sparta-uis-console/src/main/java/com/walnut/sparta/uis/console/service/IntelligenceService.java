package com.walnut.sparta.uis.console.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.dto.IntelligenceDto;
import com.walnut.sparta.uis.console.domain.entity.Intelligence;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import com.walnut.sparta.uis.console.domain.dto.IntelligenceDetailDTO;
import com.walnut.sparta.uis.console.domain.dto.IntelligenceQueryDTO;
import com.walnut.sparta.uis.console.domain.dto.PageResultDTO;
import java.util.List;

public interface IntelligenceService extends Pinenut {
    Intelligence createIntelligence(IntelligenceDto intelligence, TableMeta tableMeta , GUID tagGuid);

    Intelligence updateIntelligence( Intelligence intelligence, TableMeta tableMeta, List<GUID> tagGuids );

    void deleteIntelligence( GUID guid, TableMeta tableMeta );

    Intelligence getIntelligenceByGuid( GUID guid, TableMeta tableMeta );

    List<Intelligence> getAllIntelligences( TableMeta tableMeta );

    IntelligenceDetailDTO getIntelligenceDetail( GUID intelligenceGuid, TableMeta tableMeta );

    PageResultDTO<IntelligenceDetailDTO> pageQueryIntelligences( IntelligenceQueryDTO queryDTO, TableMeta tableMeta );


    List<Intelligence> getIntelligencesByTopicGuid(GUID topicGuid, TableMeta tableMeta);
}