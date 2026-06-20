package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.LinkedType;
import com.pinecone.hydra.unit.imperium.entity.HardlinkEntry;
import com.pinecone.hydra.unit.imperium.entity.TreeReparseLinkNode;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@IbatisDataAccessObject
public interface DeviceTreeMapper extends TrieTreeManipulator {
    void insertRootNode( @Param("guid")  GUID guid, @Param("linkedType") LinkedType linkedType );

    @Override
    default void insert ( TireOwnerManipulator ownerManipulator, GUIDImperialTrieNode node ){
        this.insertTreeNode( node.getGuid(), node.getType(), node.getAttributesGUID(), node.getNodeMetadataGUID() );
        ownerManipulator.insertRootNode( node.getGuid() );
    }

    void insertTreeNode( @Param("guid") GUID guid, @Param("type") UOI type, @Param("baseDataGuid") GUID baseDataGuid, @Param("nodeMetaGuid") GUID nodeMetaGuid );

    GUIDImperialTrieNode getNodeExtendsFromMeta( @Param("guid") GUID guid );

    @Override
    default GUIDImperialTrieNode getNode( GUID guid ) {
        GUIDImperialTrieNode node = this.getNodeExtendsFromMeta( guid );
        if ( node == null ) {
            return null;
        }

        List<GUID > parent = this.fetchParentGuids( guid );
        node.setParentGUID( parent );
        return node;
    }

    boolean contains( @Param("guid") GUID key );

    GUIDImperialTrieNode getTreeNodeOnly(@Param("guid") GUID guid, @Param("parentGuid") GUID parentGuid );

    long countNode( @Param("guid") GUID guid, @Param("parentGuid") GUID parentGuid );



    @Override
    default void purge( GUID guid ) {
        this.removeNodeMeta( guid );
        this.removeTreeNode( guid );
        this.removeOwnedTreeNode( guid );
    }

    void removeNodeMeta( @Param("guid") GUID guid );

    void removeTreeNode( @Param("guid") GUID guid );

    void removeTreeNodeByParentGuid( @Param("parent_guid") GUID parentGuid );

    void removeTreeNodeYoke( @Param("guid") GUID guid, @Param("parent_guid") GUID parentGuid );

    void removeTreeNodeWithLinkedType( @Param("guid") GUID guid, @Param("linkedType") LinkedType linkedType );




    void removeInheritance( @Param("childGuid") GUID childGuid, @Param("parentGuid") GUID parentGuid );

    List<GUIDImperialTrieNode> getChildren(@Param("guid") GUID guid );

    List<GUID > fetchChildrenGuids( @Param("parentGuid") GUID parentGuid );

    List<GUID > fetchParentGuids( @Param("guid") GUID guid );

    void updateType( @Param("type") UOI type, @Param("guid") GUID guid );

    List<GUID > fetchRoot();

    @Override
    boolean isRoot( @Param("guid") GUID guid );




    @Override
    long queryLinkedCount( @Param("guid") GUID guid, @Param("linkedType") LinkedType linkedType );

    @Override
    long queryAllLinkedCount( @Param("guid") GUID guid );


    @Override
    void newLinkTag(
            @Param("originalGuid") GUID originalGuid, @Param("dirGuid") GUID dirGuid,
            @Param("tagName") String tagName, @Param("tagGuid") GUID tagGuid, @Param("linkedType") LinkedType linkedType
    );

    @Override
    void updateLinkTagName( @Param("tagGuid") GUID tagGuid, @Param("tagName") String tagName );

    @Override
    GUID getOriginalGuid( @Param("tagName") String tagName, @Param("dirGuid") GUID dirGuid );

    @Override
    GUID getOriginalGuidByNodeGuid( @Param("tagName") String tagName, @Param("nodeGuid") GUID nodeGUID );

    @Override
    TreeReparseLinkNode getReparseLinkNode( @Param("tagName") String tagName, @Param("parentDirGuid") GUID parentDirGuid );

    @Override
    TreeReparseLinkNode getReparseLinkNodeByNodeGuid( @Param("tagName") String tagName, @Param("nodeGuid") GUID nodeGUID );

    @Override
    List<GUID > fetchOriginalGuid( @Param("tagName") String tagName );

    @Override
    List<GUID > fetchOriginalGuidRoot( @Param("tagName") String tagName );

    @Override
    boolean isTagGuid(@Param("guid") GUID guid);

    @Override
    void removeReparseLink( @Param("guid") GUID guid );

    @Override
    GUID getOriginalGuidByTagGuid(@Param("tagGuid") GUID tagGuid);

    @Override
    List<HardlinkEntry> listHardlinks(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Override
    long countHardlinks( @Param("keyword") String keyword );
}
