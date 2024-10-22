package com.pinecone.hydra.service.kom;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.GenericMetaNodeInstanceFactory;
import com.pinecone.hydra.service.kom.entity.MetaNodeInstanceFactory;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.kom.operator.GenericServiceOperatorFactory;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GUIDs;

public class CentralServicesInstrument extends ArchKOMTree implements ServicesInstrument {
    protected Hydrarum                  hydrarum;
    //GenericDistributedScopeTree
    private DistributedTrieTree         distributedTrieTree;

    MetaNodeInstanceFactory             metaNodeInstanceFactory;

    private ServiceMasterManipulator    serviceMasterManipulator;



    public CentralServicesInstrument(Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        super( hydrarum,masterManipulator, ServicesInstrument.KernelServiceConfig);
        Debug.trace(masterManipulator);
        this.hydrarum = hydrarum;
        this.serviceMasterManipulator    = (ServiceMasterManipulator) masterManipulator;
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.serviceMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.distributedTrieTree         = new GenericDistributedTrieTree(treeMasterManipulator);
        this.metaNodeInstanceFactory     = new GenericMetaNodeInstanceFactory(this.serviceMasterManipulator,treeMasterManipulator);
        this.guidAllocator               = GUIDs.newGuidAllocator();
        this.operatorFactory             = new GenericServiceOperatorFactory(this,(ServiceMasterManipulator) masterManipulator);
        this.pathResolver                =  new KOPathResolver( this.kernelObjectConfig );
    }

//    public CentralServicesTree( Hydrarum hydrarum ) {
//        this.hydrarum = hydrarum;
//    }

    public CentralServicesInstrument(KOIMappingDriver driver ) {
        this(
                driver.getSystem(),
                driver.getMasterManipulator()
        );
    }


    /**
     * Affirm path exist in cache, if required.
     * 确保路径存在于缓存，如果有明确实现必要的话。
     * 对于GenericDistributedScopeTree::getPath, 默认会自动写入缓存，因此这里可以通过getPath保证路径缓存一定存在。
     * @param guid, target guid.
     * @return Path
     */
    protected void affirmPathExist( GUID guid ) {
        this.distributedTrieTree.getCachePath( guid );
    }

    @Override
    public ServiceTreeNode get(GUID guid ){
        return (ServiceTreeNode) super.get( guid );
    }


    @Override
    public void remove( GUID guid ) {
        super.remove( guid );
    }

    @Override
    public Object queryEntityHandleByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }
}
