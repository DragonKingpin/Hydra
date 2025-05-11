package com.pinecone.hydra.unit.imperium;

import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;

public abstract class ArchRegimentObjectModel extends ArchUniformInstitutionalizedInstrument implements KOMInstrument {
    protected ImperialTree          imperialTree;

    protected TreeMasterManipulator treeMasterManipulator;

    protected KernelObjectConfig    kernelObjectConfig;

    public ArchRegimentObjectModel( TreeMasterManipulator masterManipulator, KernelObjectConfig kernelObjectConfig, String superiorPathScope ) {
        super( superiorPathScope );
        this.treeMasterManipulator = masterManipulator;  // [1st]
        this.kernelObjectConfig    = kernelObjectConfig; // [2st]
        this.imperialTree          = new RegimentedImperialTree( this );
    }

    public ArchRegimentObjectModel( KOIMasterManipulator masterManipulator, KernelObjectConfig kernelObjectConfig, String superiorPathScope ) {
        this( (TreeMasterManipulator) masterManipulator.getSkeletonMasterManipulator(), kernelObjectConfig, superiorPathScope );
    }

    public ImperialTree getMasterTrieTree() {
        return this.imperialTree;
    }

    TreeMasterManipulator getTreeMasterManipulator() {
        return this.treeMasterManipulator;
    }

    @Override
    public KernelObjectConfig getConfig() {
        return this.kernelObjectConfig;
    }

}
