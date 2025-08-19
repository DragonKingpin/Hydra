package com.walnut.sparta.uis.console.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.uis.console.domain.dto.TagsDto;
import com.walnut.sparta.uis.console.domain.entity.Tags;
import com.walnut.sparta.uis.console.domain.entity.TableMeta;
import java.util.List;

public interface ITagsService extends Pinenut {

    Tags createTag(TagsDto tag, TableMeta tableMeta );

    Tags updateTag( Tags tag, TableMeta tableMeta );

    void deleteTag( GUID guid, TableMeta tableMeta );

    Tags getTagByGuid(GUID guid, TableMeta tableMeta );

    List<Tags> getAllTags( TableMeta tableMeta );

}