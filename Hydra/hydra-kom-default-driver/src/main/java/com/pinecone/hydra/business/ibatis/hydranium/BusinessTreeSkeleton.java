package com.pinecone.hydra.business.ibatis.hydranium;

import java.util.List;
import java.util.stream.Collectors;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.business.entity.GenericNodeTree;
import com.pinecone.hydra.business.source.TreeManipulator;
import com.pinecone.hydra.unit.imperium.GUIDImperialTrieNode;
import com.pinecone.hydra.unit.imperium.LinkedType;
import com.pinecone.hydra.unit.imperium.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

class BusinessTreeSkeleton implements TrieTreeManipulator, TireOwnerManipulator {

    protected TreeManipulator mTreeManipulator;

    public BusinessTreeSkeleton( TreeManipulator treeManipulator ) {
        this.mTreeManipulator = treeManipulator;
    }

    @Override
    public void insertRootNode( GUID guid, LinkedType linkedType ) {
        this.mTreeManipulator.insertOwned( guid, null );
    }

    @Override
    public void insert( GUID targetGuid, GUID parentGUID, LinkedType linkedType ) {
        this.mTreeManipulator.insertOwned( targetGuid, parentGUID );
    }

    @Override
    public void update( GUID targetGuid, GUID parentGUID, LinkedType linkedType ) {
        this.mTreeManipulator.updateParentGuid( targetGuid, parentGUID );
    }

    @Override
    public void updateParentGuid( GUID targetGuid, GUID parentGUID ) {
        this.mTreeManipulator.updateParentGuid( targetGuid, parentGUID );
    }

    @Override
    public void updateLinkedType( GUID targetGuid, LinkedType linkedType ) {
        this.assertOwned( linkedType );
    }

    @Override
    public void remove( GUID subordinateGuid, GUID ownerGuid ) {
        this.mTreeManipulator.remove( subordinateGuid );
    }

    @Override
    public void removeBySubordinate( GUID subordinateGuid ) {
        this.mTreeManipulator.remove( subordinateGuid );
    }

    @Override
    public void removeByOwner( GUID ownerGuid ) {
        this.mTreeManipulator.removeTreeNodeByParentGuid( ownerGuid );
    }

    @Override
    public GUID getOwner( GUID subordinateGuid ) {
        return this.mTreeManipulator.fetchParentGuid( subordinateGuid );
    }

    @Override
    public List<GUID > getSubordinates( GUID guid ) {
        return this.mTreeManipulator.fetchChildrenGuids( guid );
    }

    @Override
    public void setLinkedType( GUID sourceGuid, GUID targetGuid, LinkedType linkedType ) {
        this.assertOwned( linkedType );
    }

    @Override
    public LinkedType getLinkedType( GUID childGuid, GUID parentGuid ) {
        return LinkedType.Owned;
    }

    @Override
    public void insert( TireOwnerManipulator ownerManipulator, GUIDImperialTrieNode node ) {
        this.mTreeManipulator.insertOwned( node.getGuid(), null );
    }

    @Override
    public GUIDImperialTrieNode getNode( GUID guid ) {
        GenericNodeTree treeNode = this.mTreeManipulator.get( guid );
        if ( treeNode == null ) {
            return null;
        }

        GUIDImperialTrieNode trieNode = new GUIDImperialTrieNode();
        trieNode.setGuid( treeNode.getGuid() );
        trieNode.setParentGUID( this.fetchParentGuids( guid ) );
        return trieNode;
    }

    @Override
    public boolean contains( GUID key ) {
        return this.mTreeManipulator.get( key ) != null;
    }

    @Override
    public GUIDImperialTrieNode getTreeNodeOnly( GUID guid, GUID parentGuid ) {
        GenericNodeTree treeNode = this.mTreeManipulator.get( guid );
        if ( treeNode == null ) {
            return null;
        }
        if ( parentGuid == null && treeNode.getParentGuid() != null ) {
            return null;
        }
        if ( parentGuid != null && !parentGuid.equals( treeNode.getParentGuid() ) ) {
            return null;
        }

        GUIDImperialTrieNode trieNode = new GUIDImperialTrieNode();
        trieNode.setGuid( treeNode.getGuid() );
        trieNode.setParentGUID( this.fetchParentGuids( guid ) );
        return trieNode;
    }

    @Override
    public long countNode( GUID guid, GUID parentGuid ) {
        return this.mTreeManipulator.countNode( guid, parentGuid );
    }

    @Override
    public void insertNode( GUID guid, GUIDImperialTrieNode distributedTreeNode ) {
        this.mTreeManipulator.insertOwned( guid, null );
    }

    @Override
    public void updateNode( GUID guid, GUIDImperialTrieNode distributedTreeNode ) {
    }

    @Override
    public void purge( GUID guid ) {
        this.mTreeManipulator.remove( guid );
    }

    @Override
    public void removeTreeNode( GUID guid ) {
        this.mTreeManipulator.remove( guid );
    }

    @Override
    public void removeTreeNodeByParentGuid( GUID parentGuid ) {
        this.mTreeManipulator.removeTreeNodeByParentGuid( parentGuid );
    }

    @Override
    public void removeTreeNodeYoke( GUID guid, GUID parentGuid ) {
        this.mTreeManipulator.remove( guid );
    }

    @Override
    public void removeTreeNodeWithLinkedType( GUID guid, LinkedType linkedType ) {
        this.mTreeManipulator.remove( guid );
    }

    @Override
    public void removeNodeMeta( GUID guid ) {
    }

    @Override
    public List<GUIDImperialTrieNode > getChildren( GUID guid ) {
        return this.mTreeManipulator.fetchChildrenGuids( guid ).stream().map( this::getNode ).collect( Collectors.toList() );
    }

    @Override
    public List<GUID > fetchChildrenGuids( GUID parentGuid ) {
        return this.mTreeManipulator.fetchChildrenGuids( parentGuid );
    }

    @Override
    public List<GUID > fetchParentGuids( GUID guid ) {
        GUID parentGuid = this.mTreeManipulator.fetchParentGuid( guid );
        if ( parentGuid == null ) {
            return List.of();
        }

        return List.of( parentGuid );
    }

    @Override
    public void removeInheritance( GUID childNode, GUID parentGUID ) {
        this.mTreeManipulator.remove( childNode );
    }

    @Override
    public void addChild( GUID childGuid, GUID parentGuid ) {
        this.mTreeManipulator.insertOwned( childGuid, parentGuid );
    }

    @Override
    public void updateType( UOI type, GUID guid ) {
    }

    @Override
    public List<GUID > fetchRoot() {
        return this.mTreeManipulator.fetchRootGuids();
    }

    @Override
    public boolean isRoot( GUID guid ) {
        GenericNodeTree treeNode = this.mTreeManipulator.get( guid );
        return treeNode != null && treeNode.getParentGuid() == null;
    }

    @Override
    public long queryLinkedCount( GUID guid, LinkedType linkedType ) {
        return linkedType == LinkedType.Owned && this.contains( guid ) ? 1 : 0;
    }

    @Override
    public long queryAllLinkedCount( GUID guid ) {
        return this.contains( guid ) ? 1 : 0;
    }

    @Override
    public void newLinkTag( GUID originalGuid, GUID dirGuid, String tagName, GUID tagGuid, LinkedType linkedType ) {
        throw new UnsupportedOperationException( "Business tree only supports owned lineage." );
    }

    @Override
    public void updateLinkTagName( GUID tagGuid, String tagName ) {
        throw new UnsupportedOperationException( "Business tree only supports owned lineage." );
    }

    @Override
    public GUID getOriginalGuid( String tagName, GUID parentDirGuid ) {
        return null;
    }

    @Override
    public GUID getOriginalGuidByNodeGuid( String tagName, GUID nodeGUID ) {
        return null;
    }

    @Override
    public ReparseLinkNode getReparseLinkNode( String tagName, GUID parentDirGuid ) {
        return null;
    }

    @Override
    public ReparseLinkNode getReparseLinkNodeByNodeGuid( String tagName, GUID nodeGUID ) {
        return null;
    }

    @Override
    public List<GUID > fetchOriginalGuid( String tagName ) {
        return List.of();
    }

    @Override
    public List<GUID > fetchOriginalGuidRoot( String tagName ) {
        return List.of();
    }

    @Override
    public boolean isTagGuid( GUID guid ) {
        return false;
    }

    @Override
    public GUID getOriginalGuidByTagGuid( GUID tagGuid ) {
        return null;
    }

    @Override
    public void removeReparseLink( GUID guid ) {
        throw new UnsupportedOperationException( "Business tree only supports owned lineage." );
    }

    protected void assertOwned( LinkedType linkedType ) {
        if ( linkedType != LinkedType.Owned ) {
            throw new UnsupportedOperationException( "Business tree only supports owned lineage." );
        }
    }
}
