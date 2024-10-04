package com.pinecone.hydra.registry;

import java.util.List;
import java.util.Stack;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;

public class ReparseLinkSelector extends StandardPathSelector implements ReparsePointSelector {
    public ReparseLinkSelector( StandardPathSelector pathSelector ) {
        super( pathSelector.pathResolver, pathSelector.distributedTrieTree, pathSelector.dirManipulator, pathSelector.fileManipulators );
    }

    public ReparseLinkSelector( PathResolver pathResolver, DistributedTrieTree trieTree, GUIDNameManipulator dirMan, GUIDNameManipulator[] fileMans ) {
        super( pathResolver, trieTree, dirMan, fileMans );
    }

    @Override
    public Object search( String[] parts ) {
        List<String> resolvedParts = this.pathResolver.resolvePath(parts);
        return this.dfsSearch( resolvedParts );
    }

    @Override
    public ReparseLinkNode searchLinkNode( String[] parts ) {
        Object result = this.search( parts );
        if( result instanceof ReparseLinkNode ) {
            return (ReparseLinkNode) result;
        }
        return null;
    }

    @Override
    protected Object beforeDFSTermination( String currentPart, GUID guid ) {
        ReparseLinkNode reparseLinkNode = this.distributedTrieTree.getReparseLinkNodeByNodeGuid( currentPart, guid );
        if ( reparseLinkNode != null ) {
            return reparseLinkNode;
        }
        return guid;
    }
}
