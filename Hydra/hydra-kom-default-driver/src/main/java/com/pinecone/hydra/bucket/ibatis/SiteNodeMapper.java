package com.pinecone.hydra.bucket.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.entity.GenericSiteNode;
import com.pinecone.hydra.storage.bucket.entity.Site;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;
import com.pinecone.hydra.storage.bucket.source.SiteNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

@IbatisDataAccessObject
public interface SiteNodeMapper extends SiteNodeManipulator {

    @Insert("INSERT INTO `hydra_ucdn_site_node` (`node_name`, `node_guid`, `state`, `is_enabled`, `related_service`, `site_guid`) VALUES (#{nodeName},#{nodeGuid},#{state},#{isEnabled},#{relatedService},#{siteGuid})")
    void insert(SiteNode siteNode );

    @Delete("DELETE FROM `hydra_ucdn_site_node` WHERE `node_guid` = #{siteNodeGuid}")
    void remove( GUID siteNodeGuid );

    @Select("SELECT `node_name` AS nodeName, `node_guid` AS nodeGuid, `state`, `is_enabled` AS isEnabled, `related_service` AS relatedService, `id`, site_guid AS siteGuid FROM hydra_ucdn_site_node WHERE node_guid = #{siteNodeGuid}")
    GenericSiteNode querySiteNode(GUID siteNodeGuid );


    default List<SiteNode> querySiteNodeBySiteGuid( GUID siteGuid ){
        List<GenericSiteNode> genericSiteNodes = this.querySiteNodeBySiteGuid0(siteGuid);
        return new ArrayList<>(genericSiteNodes);
    }

    @Select("SELECT `node_name` AS nodeName, `node_guid` AS nodeGuid, `state`, `is_enabled` AS isEnabled, `related_service` AS relatedService, `id`, site_guid AS siteGuid FROM hydra_ucdn_site_node WHERE site_guid = #{siteGuid}")
    List<GenericSiteNode> querySiteNodeBySiteGuid0( GUID siteGuid );

    @Update("UPDATE `hydra_ucdn_site_node` SET `node_name` = #{nodeName}, `state` = #{state}, `is_enabled` = #{isEnabled} WHERE `node_guid` = #{nodeGuid}")
    void update( SiteNode siteNode );
}
