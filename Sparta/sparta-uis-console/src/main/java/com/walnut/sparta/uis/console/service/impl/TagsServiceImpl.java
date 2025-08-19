package com.walnut.sparta.uis.console.service.impl;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.uis.console.domain.dto.TagsDto;
import com.walnut.sparta.uis.console.domain.entity.IntelligenceTag;
import com.walnut.sparta.uis.console.domain.entity.Tags;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import com.walnut.sparta.uis.console.mapper.IntelligenceTagsMapper;
import com.walnut.sparta.uis.console.mapper.TagsMapper;
import com.walnut.sparta.uis.console.service.ITagsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TagsServiceImpl implements ITagsService {

    @Resource
    private TagsMapper tagsMapper;

    @Resource
    private IntelligenceTagsMapper intelligenceTagsMapper;

    @Override
    public Tags createTag(TagsDto tagsDto, TableMeta tableMeta) {
        Tags existingTag = tagsMapper.selectByTagName(tagsDto.getTagName(), tableMeta);
        if (existingTag != null) {
            throw new RuntimeException("标签名称已存在: " + tagsDto.getTagName());
        }
        Tags tag = new Tags();
        BeanUtils.copyProperties(tagsDto, tag);
        tag.setTagGuid(GUIDs.newGuidAllocator().nextGUID());
        if (tag.getCreateTime() == null) {
            tag.setCreateTime(LocalDateTime.now());
        }

        tagsMapper.insert(tag, tableMeta);
        return tag;
    }

    @Override
    public Tags updateTag(Tags tag, TableMeta tableMeta) {
        tagsMapper.update(tag, tableMeta);
        return tag;
    }

    @Override
    @Transactional
    public void deleteTag( GUID guid, TableMeta tableMeta ) {

        intelligenceTagsMapper.deleteByTagGuid(guid, tableMeta);
        tagsMapper.delete(guid, tableMeta);
    }

    @Override
    public Tags getTagByGuid(GUID guid, TableMeta tableMeta ) {
        return tagsMapper.selectByGuid( guid, tableMeta );
    }

    @Override
    public List<Tags> getAllTags( TableMeta tableMeta ) {
        return tagsMapper.selectAll( tableMeta );
    }
}