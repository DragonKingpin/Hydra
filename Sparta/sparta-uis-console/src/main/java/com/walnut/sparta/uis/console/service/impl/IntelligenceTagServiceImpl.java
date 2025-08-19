package com.walnut.sparta.uis.console.service.impl;

import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTag;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import com.walnut.sparta.uis.console.mapper.IntelligenceTagsMapper;
import com.walnut.sparta.uis.console.service.IIntelligenceTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntelligenceTagServiceImpl implements IIntelligenceTagService {

    @Resource
    private  IntelligenceTagsMapper intelligenceTagsMapper;


    @Override
    public IntelligenceTag createIntelligenceTag(IntelligenceTag intelligenceTag, TableMeta tableMeta) {

        intelligenceTagsMapper.insert(intelligenceTag,tableMeta);
        return intelligenceTag;
    }

    @Override
    public IntelligenceTag updateIntelligenceTag(IntelligenceTag intelligenceTag, TableMeta tableMeta) {
        intelligenceTagsMapper.update(intelligenceTag, tableMeta);
        return intelligenceTag;
    }

    @Override
    @Transactional
    public void deleteIntelligenceTag(GUID guid, TableMeta tableMeta) {

        intelligenceTagsMapper.deleteByTagGuid(guid, tableMeta);

    }

    @Override
    public IntelligenceTag getIntelligenceTagById( GUID guid, TableMeta tableMeta ) {
        return intelligenceTagsMapper.selectByGuid(guid, tableMeta);
    }

    @Override
    public List<IntelligenceTag> getAllIntelligenceTags( TableMeta tableMeta ) {
        return intelligenceTagsMapper.selectAll(tableMeta);
    }

    @Override
    @Transactional
    public void removeIntelligenceTags(GUID intelligenceGuid, List<GUID> tagGuids, TableMeta tableMeta) {
        if (tagGuids == null || tagGuids.isEmpty()) {
            intelligenceTagsMapper.deleteByIntelligenceGuid(intelligenceGuid, tableMeta);
        } else {
            for (GUID tagGuid : tagGuids) {
                intelligenceTagsMapper.deleteByIntelligenceGuidAndTagGuid(intelligenceGuid, tagGuid, tableMeta);
            }
        }
    }

    @Override
    @Transactional
    public void batchAddIntelligenceTags(GUID intelligenceGuid, List<GUID> tagGuids, TableMeta tableMeta) {
        if (intelligenceGuid == null || tagGuids == null || tagGuids.isEmpty()) {
            return;
        }
        LocalDateTime createTime = LocalDateTime.now();
        intelligenceTagsMapper.batchInsert(intelligenceGuid, tagGuids, createTime, tableMeta);
    }
}