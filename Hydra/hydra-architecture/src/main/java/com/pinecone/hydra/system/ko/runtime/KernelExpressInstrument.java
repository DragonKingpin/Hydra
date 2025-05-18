package com.pinecone.hydra.system.ko.runtime;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.handle.KHandle;
import com.pinecone.hydra.system.ko.handle.KOMMountPointHandle;
import com.pinecone.hydra.system.ko.handle.ObjectTreeAddressingSectionHandle;
import com.pinecone.hydra.system.ko.kom.ExpressInstrument;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.system.ko.kom.ProxiedKOMMountPointHandle;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class KernelExpressInstrument extends ArchDirectMappingTrieRuntimeKOMTree implements ExpressInstrument {

    public KernelExpressInstrument( String superiorPathScope, KernelObjectConfig kernelObjectConfig ) {
        super( superiorPathScope, kernelObjectConfig );
    }

    @Override
    public KOMInstrument mount( String mountPointPath, KOMInstrument that ) {
        String[] debris = mountPointPath.split( this.getConfig().getPathNameSepRegex() );
        if ( debris.length < 1 ) {
            throw new IllegalArgumentException( "Path given should not be empty." );
        }
        this.mount( mountPointPath, debris[ debris.length - 1 ], that );
        that.setParent( this );
        that.applySuperiorPathScope( mountPointPath );
        return that;
    }

    @Override
    public KOMInstrument mount( String mountPointPath, String treeNodeName, KOMInstrument that ) {
        KOMMountPointHandle handle = new ProxiedKOMMountPointHandle(
                treeNodeName, this.guidAllocator.nextGUID(), that
        );
        this.add( mountPointPath, handle );
        return that;
    }

    @Override
    public KHandle mount(String mountPointPath, ObjectTreeAddressingSectionHandle that) {
        this.add( mountPointPath, that );
        return that;
    }

    @Override
    public KOMInstrument getMountedInstrument( String mountPointPath ) {
        TreeNode tn = this.mNodeIndex.get( mountPointPath );
        if ( tn instanceof RuntimeTreeNode ) {
            tn = ((RuntimeTreeNode) tn).getTreeNode();
        }

        if ( tn instanceof KOMMountPointHandle ) {
            return ((KOMMountPointHandle) tn).revealWrapped();
        }
        else if ( tn instanceof KOMInstrument ) {
            return (KOMInstrument) tn;
        }
        return null;
    }
}
