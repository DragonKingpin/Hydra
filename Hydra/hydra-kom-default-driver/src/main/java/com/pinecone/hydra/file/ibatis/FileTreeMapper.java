package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.storage.bucket.BucketNodeManipulator;
import com.pinecone.hydra.storage.file.entity.HardlinkEntry;
import com.pinecone.hydra.storage.file.source.FileChildManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.LinkedType;
import com.pinecone.hydra.unit.imperium.entity.TreeReparseLinkNode;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FileTreeMapper extends TrieTreeManipulator, BucketNodeManipulator, FileChildManipulator {
    void insertRootNode(@Param("guid")  GUID guid, @Param("linkedType") LinkedType linkedType );

    @Override
    default void insert ( TireOwnerManipulator ownerManipulator, GUIDImperialTrieNode node ){
        this.insertTreeNode( node.getGuid(), node.getType(), node.getAttributesGUID(), node.getNodeMetadataGUID() );
        ownerManipulator.insertRootNode( node.getGuid() );
    }

    void insertTreeNode( @Param("guid") GUID guid, @Param("type") UOI type, @Param("baseDataGuid") GUID baseDataGuid, @Param("nodeMetaGuid") GUID nodeMetaGuid );

    GUIDImperialTrieNode getNodeExtendsFromMeta(GUID guid );

    boolean contains( GUID key );


    @Override
    default GUIDImperialTrieNode getNode(GUID guid ) {
        GUIDImperialTrieNode node = this.getNodeExtendsFromMeta( guid );
        List<GUID > parent = this.fetchParentGuids( guid );
        node.setParentGUID( parent );
        return node;
    }

    GUIDImperialTrieNode getTreeNodeOnly(@Param("guid") GUID guid, @Param("parentGuid") GUID parentGuid );

    long countNode( GUID guid, GUID parentGuid );



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

    List<GUIDImperialTrieNode> getChildren(GUID guid );

    List<GUID > fetchChildrenGuids( @Param("parentGuid") GUID parentGuid );

    List<GUID > fetchParentGuids( GUID guid );

    void updateType( UOI type , GUID guid );

    @Override
    void updateBucketGuid( @Param( "guid" ) GUID guid, @Param( "bucketGuid" ) GUID bucketGuid );

    List<GUID > fetchRoot();

    @Override
    boolean isRoot( GUID guid );




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
    TreeReparseLinkNode getReparseLinkNode(@Param("tagName") String tagName, @Param("parentDirGuid") GUID parentDirGuid );

    @Override
    TreeReparseLinkNode getReparseLinkNodeByNodeGuid( @Param("tagName") String tagName, @Param("nodeGuid") GUID nodeGUID );

    @Override
    List<GUID > fetchOriginalGuid( String tagName );

    @Override
    List<GUID > fetchOriginalGuidRoot( String tagName );

    @Override
    boolean isTagGuid(GUID guid);

    @Override
    void removeReparseLink( GUID guid );

    @Override
    GUID getOriginalGuidByTagGuid(GUID tagGuid);

    List<HardlinkEntry> listHardlinks(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countHardlinks( @Param("keyword") String keyword );
}
