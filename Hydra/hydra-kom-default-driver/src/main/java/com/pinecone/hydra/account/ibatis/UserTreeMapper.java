package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.LinkedType;
import com.pinecone.hydra.unit.imperium.entity.HardlinkEntry;
import com.pinecone.hydra.unit.imperium.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@IbatisDataAccessObject
public interface UserTreeMapper extends TrieTreeManipulator {
    void insertRootNode(@Param("guid") GUID guid);

    @Override
    default void insert (TireOwnerManipulator ownerManipulator, GUIDImperialTrieNode node ){
        this.insertTreeNode( node.getGuid(), node.getType(), node.getAttributesGUID(), node.getNodeMetadataGUID() );
        ownerManipulator.insertRootNode( node.getGuid() );
    }

    void insertTreeNode(@Param("guid") GUID guid, @Param("type") UOI type, @Param("baseDataGuid") GUID baseDataGuid, @Param("nodeMetaGuid") GUID nodeMetaGuid );

    GUIDImperialTrieNode getNodeExtendsFromMeta(@Param("guid") GUID guid );

    boolean contains( @Param("key") GUID key );

    @Override
    default GUIDImperialTrieNode getNode(GUID guid ) {
        GUIDImperialTrieNode node = this.getNodeExtendsFromMeta( guid );
        if( node == null ){
            return node;
        }
        List<GUID > parent = this.fetchParentGuids( guid );
        node.setParentGUID( parent );
        return node;
    }

    GUIDImperialTrieNode getTreeNodeOnly(@Param("guid") GUID guid, @Param("parentGuid") GUID parentGuid );

    long countNode( @Param("guid") GUID guid, @Param("parentGuid") GUID parentGuid );


    @Override
    default void purge( GUID guid ) {
        this.removeNodeMeta( guid );
        this.removeTreeNode( guid );
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

    void updateType( @Param("type") UOI type , @Param("guid") GUID guid );

    List<GUID > fetchRoot();

    @Override
    boolean isRoot( @Param("guid") GUID guid );

    void addChild( @Param("childGuid") GUID childGuid, @Param("parentGuid") GUID parentGuid );

    long queryLinkedCount( @Param("guid") GUID guid, @Param("linkedType") LinkedType linkedType );

    long queryAllLinkedCount( @Param("guid") GUID guid );

    void newLinkTag( @Param("originalGuid") GUID originalGuid, @Param("dirGuid") GUID dirGuid, @Param("tagName") String tagName, @Param("tagGuid") GUID tagGuid, @Param("linkedType") LinkedType linkedType );

    void updateLinkTagName( @Param("tagGuid") GUID tagGuid, @Param("tagName") String tagName );

    GUID getOriginalGuid( @Param("tagName") String tagName, @Param("parentDirGuid") GUID parentDirGuid );

    GUID getOriginalGuidByNodeGuid( @Param("tagName") String tagName, @Param("nodeGuid") GUID nodeGUID );

    ReparseLinkNode getReparseLinkNode( @Param("tagName") String tagName, @Param("parentDirGuid") GUID parentDirGuid );

    ReparseLinkNode getReparseLinkNodeByNodeGuid( @Param("tagName") String tagName, @Param("nodeGuid") GUID nodeGUID );

    List<GUID > fetchOriginalGuid( @Param("tagName") String tagName );

    List<GUID > fetchOriginalGuidRoot( @Param("tagName") String tagName );

    boolean isTagGuid( @Param("guid") GUID guid );

    GUID getOriginalGuidByTagGuid( @Param("tagGuid") GUID tagGuid );

    void removeReparseLink( @Param("guid") GUID guid );

    List<HardlinkEntry> listHardlinks(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countHardlinks( @Param("keyword") String keyword );
}
