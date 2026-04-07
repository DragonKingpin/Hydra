package com.pinecone.hydra.system.ko.runtime;

import java.util.Collection;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.unit.trie.DirectoryNode;
import com.pinecone.framework.unit.trie.TrieNode;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public abstract class ArchDirectMappingTrieRuntimeKOMTree extends ArchRuntimeKOMTree implements DirectMappingTrieRuntimeInstrument {
    public ArchDirectMappingTrieRuntimeKOMTree( @Nullable Processum superiorProcess, String superiorPathScope, KernelObjectConfig kernelObjectConfig, @Nullable GuidAllocator guidAllocator ) {
        super( superiorProcess, superiorPathScope, kernelObjectConfig, guidAllocator );
    }

    @Override
    public boolean hasOwnProperty( Object elm ) {
        return this.mNodeIndex.hasOwnProperty( elm );
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.queryGUIDByPath( key.toString() ) != null;
    }

    @Override
    public TrieNode<TreeNode> getOwnProperty( String path ) {
        return this.mNodeIndex.queryNode( path );
    }

    @Override
    public DirectoryNode<TreeNode > fetchOwnChildren( String path ) {
        TrieNode<TreeNode> self = this.getOwnProperty( path );
        if ( self == null ) {
            return null;
        }

        return self.evinceDirectory();
    }

    @Override
    public Collection<String > fetchOwnMappingPath() {
        return this.mNodeIndex.keySet();
    }

}
