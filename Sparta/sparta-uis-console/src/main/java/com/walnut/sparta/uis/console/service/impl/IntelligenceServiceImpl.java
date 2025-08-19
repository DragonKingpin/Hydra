package com.walnut.sparta.uis.console.service.impl;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.uis.console.domain.dto.IntelligenceDto;
import com.walnut.sparta.uis.console.domain.entity.ArchIntelligence;
import com.walnut.sparta.uis.console.domain.entity.ArchIntelligenceTag;
import com.walnut.sparta.uis.console.domain.entity.Intelligence;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTag;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import com.walnut.sparta.uis.console.mapper.IntelligenceMapper;
import com.walnut.sparta.uis.console.domain.dto.IntelligenceDetailDTO;
import com.walnut.sparta.uis.console.domain.dto.IntelligenceQueryDTO;
import com.walnut.sparta.uis.console.domain.dto.PageResultDTO;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTag;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTopic;
import com.walnut.sparta.uis.console.domain.entity.Tags;
import java.util.ArrayList;
import java.lang.Math;
import com.walnut.sparta.uis.console.mapper.IntelligenceTagsMapper;
import com.walnut.sparta.uis.console.service.IntelligenceService;
import com.walnut.sparta.uis.console.service.IntelligenceTopicService;
import com.walnut.sparta.uis.console.service.ITagsService;
import org.springframework.beans.BeanUtils;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IntelligenceServiceImpl implements IntelligenceService {

    @Resource
    private IntelligenceMapper intelligenceMapper;

    @Resource
    private IntelligenceTagsMapper intelligenceTagsMapper;

    @Resource
    private IntelligenceTopicService intelligenceTopicService;

    @Resource
    private ITagsService tagsService;


    @Transactional
    @Override
    public Intelligence createIntelligence(IntelligenceDto intelligenceDto, TableMeta tableMeta , GUID tagGuid) {
        Intelligence intelligence = new ArchIntelligence();
        BeanUtils.copyProperties(intelligenceDto, intelligence);
        intelligence.setIntelligenceGuid(GUIDs.newGuidAllocator().nextGUID());
        intelligenceMapper.insert( intelligence, tableMeta );
        IntelligenceTag intelligenceTags = new ArchIntelligenceTag();
        intelligenceTags.setIntelligenceGuid(intelligence.getIntelligenceGuid());
        intelligenceTags.setTagGuid(tagGuid);
        intelligenceTagsMapper.insert(intelligenceTags, tableMeta);
        return intelligence;
    }

    @Transactional
    @Override
    public Intelligence updateIntelligence(Intelligence intelligence, TableMeta tableMeta, List<GUID> tagGuids) {
        // 更新情报基本信息
        intelligenceMapper.update(intelligence, tableMeta);
        // 如果提供了标签列表，则更新标签关联
        if (tagGuids != null && !tagGuids.isEmpty()) {
            // 先删除旧的标签关联
            intelligenceTagsMapper.deleteByIntelligenceGuid(intelligence.getIntelligenceGuid(), tableMeta);
            // 添加新的标签关联
            for (GUID tagGuid : tagGuids) {
                IntelligenceTag intelligenceTag = new ArchIntelligenceTag();
                intelligenceTag.setIntelligenceGuid(intelligence.getIntelligenceGuid());
                intelligenceTag.setTagGuid(tagGuid);
                intelligenceTag.setCreateTime(LocalDateTime.now());
                intelligenceTagsMapper.insert(intelligenceTag, tableMeta);
            }
        }
        
        return intelligence;
    }

    @Transactional
    @Override
    public void deleteIntelligence( GUID guid, TableMeta tableMeta ) {

        intelligenceTagsMapper.deleteByIntelligenceGuid(guid, tableMeta);

        intelligenceMapper.delete( guid, tableMeta );
    }

    @Override
    public Intelligence getIntelligenceByGuid(GUID guid, TableMeta tableMeta ) {
        return intelligenceMapper.selectByGuid( guid, tableMeta );
    }

    @Override
    public List<Intelligence> getIntelligencesByTopicGuid(GUID topicGuid, TableMeta tableMeta) {
        return intelligenceMapper.selectByTopicGuid(topicGuid, tableMeta);
    }

    @Override
    public List<Intelligence> getAllIntelligences( TableMeta tableMeta ) {
        return intelligenceMapper.selectAll( tableMeta );
    }

    @Override
    public IntelligenceDetailDTO getIntelligenceDetail( GUID intelligenceGuid, TableMeta tableMeta ) {
        // 获取情报基本信息
        Intelligence intelligence = intelligenceMapper.selectByGuid(intelligenceGuid, tableMeta);
        if (intelligence == null) {
            return null;
        }

        // 获取主题详情
        IntelligenceTopic topic = null;
        GUID topicGuid = intelligence.getTopicGuid();
        if (topicGuid != null) {
            topic = intelligenceTopicService.getIntelligenceTopicByGuid(topicGuid, tableMeta);
        }

        // 创建DTO并设置基本信息和主题信息
        IntelligenceDetailDTO detailDTO = new IntelligenceDetailDTO(intelligence, topic);

        // 获取关联的标签列表
        List<IntelligenceTag> intelligenceTags = intelligenceTagsMapper.selectByIntelligenceGuid(intelligenceGuid, tableMeta);
        List<Tags> tagsList = new ArrayList<>();
        if (intelligenceTags != null && !intelligenceTags.isEmpty()) {
            for (IntelligenceTag intelligenceTag : intelligenceTags) {
                GUID tagGuid = intelligenceTag.getTagGuid();
                Tags tag = tagsService.getTagByGuid(tagGuid,tableMeta);
                if (tag != null) {
                    tagsList.add(tag);
                }
            }
        }
        detailDTO.setTagsList(tagsList);

        return detailDTO;
    }

    @Override
    public PageResultDTO<IntelligenceDetailDTO> pageQueryIntelligences( IntelligenceQueryDTO queryDTO, TableMeta tableMeta ) {

        int page = queryDTO.getPage();
        int size = queryDTO.getSize();
        GUID topicGuid = queryDTO.getTopicGuid();
        String status = queryDTO.getStatus();
        GUID tagGuid = queryDTO.getTagGuid();

        int offset = (page - 1) * size;

        long total = intelligenceMapper.countByCondition(topicGuid, status, tagGuid, tableMeta);

        int pages = (int) Math.ceil((double) total / size);

        List<Intelligence> intelligences = intelligenceMapper.selectByCondition(topicGuid, status, tagGuid, offset, size, tableMeta);

        List<IntelligenceDetailDTO> detailDTOs = new ArrayList<>();
        for (Intelligence intelligence : intelligences) {
            detailDTOs.add(getIntelligenceDetail(intelligence.getIntelligenceGuid(), tableMeta));
        }

        return new PageResultDTO<>(total, pages, page, size, detailDTOs);
    }
}
