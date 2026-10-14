package com.pinecone.hydra.business.ibatis.tag;

import java.util.List;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.tag.NodeTag;
import com.pinecone.hydra.business.source.tag.NodeTagManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface NodeTagMapper extends NodeTagManipulator {
    List<NodeTag> fetchByNodeGuids( @Param("nodeGuids") List<GUID> nodeGuids );
    List<String> fetchCandidates( @Param("pattern") String pattern, @Param("limit") int limit );
    List<GUID> findNodeGuids( @Param("tag") String tag, @Param("offset") int offset, @Param("limit") int limit );
    long countNodes( @Param("tag") String tag );
    void insert( @Param("nodeGuid") GUID nodeGuid, @Param("tag") String tag );
    void removeTag( @Param("nodeGuid") GUID nodeGuid, @Param("tag") String tag );
    void removeByNodeGuid( @Param("nodeGuid") GUID nodeGuid );
}
