package com.walnut.sparta.uis.console.service.impl;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.KernelObjectConstants;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.uis.console.mapper.IntelligenceMapper;
import org.springframework.transaction.annotation.Transactional;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTopic;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import com.walnut.sparta.uis.console.mapper.IntelligenceTopicMapper;
import com.walnut.sparta.uis.console.service.IntelligenceTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntelligenceTopicServiceImpl implements IntelligenceTopicService {

    @Resource
    private IntelligenceTopicMapper intelligenceTopicMapper;

    @Resource
    private IntelligenceMapper intelligenceMapper;


    @Override
    public IntelligenceTopic createIntelligenceTopic( IntelligenceTopic intelligenceTopic, TableMeta tableMeta ) {

        GUID parentGuid = intelligenceTopic.getParentGuid();
        if (parentGuid != null) {
            IntelligenceTopic parentTopic = this.getIntelligenceTopicByGuid(parentGuid, tableMeta);
            if (parentTopic == null) {
                throw new IllegalArgumentException("Parent topic not found with GUID: " + parentGuid);
            }

            String parentPath = parentTopic.getTopicPath();
            String topicPath = parentPath + KernelObjectConstants.PathNameSepRegex + intelligenceTopic.getTopicName();
            intelligenceTopic.setTopicPath(topicPath);
        } else {

            intelligenceTopic.setTopicPath(intelligenceTopic.getTopicName());
        }

        GUID topicGuid = GUIDs.newGuidAllocator().nextGUID();
        intelligenceTopic.setTopicGuid(topicGuid);

        LocalDateTime now = LocalDateTime.now();
        intelligenceTopic.setCreateTime(now);
        intelligenceTopic.setUpdateTime(now);

        this.intelligenceTopicMapper.insert(intelligenceTopic, tableMeta);
        return intelligenceTopic;
    }

    @Override
    @Transactional
    public IntelligenceTopic updateIntelligenceTopic( IntelligenceTopic intelligenceTopic, TableMeta tableMeta ) {

        this.intelligenceTopicMapper.update( intelligenceTopic, tableMeta );
        
        String newTopicName = intelligenceTopic.getTopicName();

        GUID topicGuid = intelligenceTopic.getTopicGuid();

        LocalDateTime updateTime = LocalDateTime.now();

        this.intelligenceMapper.updateTopicNameByTopicGuid(topicGuid, newTopicName, updateTime, tableMeta);
        
        return intelligenceTopic;
    }

    @Override
    @Transactional
    public void deleteIntelligenceTopic( GUID guid, TableMeta tableMeta ) {
        long count = this.intelligenceMapper.countByCondition(guid, null, null, tableMeta);
        if (count > 0) {
            throw new IllegalArgumentException("Cannot delete topic with associated intelligence: " + guid);
        }
        deleteChildTopics(guid, tableMeta);

        intelligenceTopicMapper.delete(guid, tableMeta);
    }

    private void deleteChildTopics(GUID parentGuid, TableMeta tableMeta) {
        List<IntelligenceTopic> childTopics = intelligenceTopicMapper.selectByParentGuid(parentGuid, tableMeta);
        for (IntelligenceTopic childTopic : childTopics) {

            deleteChildTopics(childTopic.getTopicGuid(), tableMeta);

            intelligenceTopicMapper.delete(childTopic.getTopicGuid(), tableMeta);
        }
    }

    @Override
    public IntelligenceTopic getIntelligenceTopicByGuid(GUID guid, TableMeta tableMeta ) {
        return intelligenceTopicMapper.selectByGuid( guid, tableMeta );
    }

    @Override
    public List<IntelligenceTopic> getAllIntelligenceTopics( TableMeta tableMeta ) {
        return intelligenceTopicMapper.selectAll( tableMeta );
    }
}