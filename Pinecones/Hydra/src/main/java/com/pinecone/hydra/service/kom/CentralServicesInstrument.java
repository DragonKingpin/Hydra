package com.pinecone.hydra.service.kom;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.ElementNode;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.service.kom.entity.GenericMetaNodeInstanceFactory;
import com.pinecone.hydra.service.kom.entity.MetaNodeInstanceFactory;
import com.pinecone.hydra.service.kom.nodes.ApplicationNode;
import com.pinecone.hydra.service.kom.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.kom.nodes.GenericNamespace;
import com.pinecone.hydra.service.kom.nodes.Namespace;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.kom.operator.GenericServiceOperatorFactory;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.system.ko.kom.ArchReparseKOMTree;
import com.pinecone.hydra.system.ko.kom.StandardPathSelector;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;
import com.pinecone.hydra.unit.udtt.GenericDistributedTrieTree;
import com.pinecone.hydra.unit.udtt.source.TreeMasterManipulator;
import com.pinecone.ulf.util.id.GUIDs;

public class CentralServicesInstrument extends ArchReparseKOMTree implements ServicesInstrument {
    protected Hydrarum                  hydrarum;
    //GenericDistributedScopeTree
    private DistributedTrieTree         distributedTrieTree;

    MetaNodeInstanceFactory             metaNodeInstanceFactory;

    private ServiceMasterManipulator    serviceMasterManipulator;

    private ServiceNamespaceManipulator serviceNamespaceManipulator;

    private ApplicationNodeManipulator  applicationNodeManipulator;



    public CentralServicesInstrument(Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        super( hydrarum,masterManipulator, ServicesInstrument.KernelServiceConfig);
        Debug.trace(masterManipulator);
        this.hydrarum = hydrarum;
        this.serviceMasterManipulator    = (ServiceMasterManipulator) masterManipulator;
        this.serviceNamespaceManipulator = serviceMasterManipulator.getNamespaceManipulator();
        this.applicationNodeManipulator  = serviceMasterManipulator.getApplicationNodeManipulator();
        KOISkeletonMasterManipulator skeletonMasterManipulator = this.serviceMasterManipulator.getSkeletonMasterManipulator();
        TreeMasterManipulator        treeMasterManipulator     = (TreeMasterManipulator) skeletonMasterManipulator;
        this.distributedTrieTree         = new GenericDistributedTrieTree(treeMasterManipulator);
        this.metaNodeInstanceFactory     = new GenericMetaNodeInstanceFactory(this.serviceMasterManipulator,treeMasterManipulator);
        this.guidAllocator               = GUIDs.newGuidAllocator();
        this.operatorFactory             = new GenericServiceOperatorFactory(this,(ServiceMasterManipulator) masterManipulator);
        this.pathResolver                =  new KOPathResolver( this.kernelObjectConfig );
        this.pathSelector                  =  new StandardPathSelector(
                this.pathResolver, this.distributedTrieTree, this.serviceNamespaceManipulator, new GUIDNameManipulator[] { this.applicationNodeManipulator }
        );
    }

//    public CentralServicesTree( Hydrarum hydrarum ) {
//        this.hydrarum = hydrarum;
//    }

    protected ServiceTreeNode affirmTreeNodeByPath(String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy72();

        ServiceTreeNode node = this.queryElement(path);
        if ( node != null ){
            return node;
        }

        ServiceTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryElement( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    ApplicationNode applicationNode = (ApplicationNode) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    applicationNode.setName( parts[i] );
                    GUID guid = this.put( applicationNode );
                    this.affirmOwnedNode( parentGuid, guid );
                    return applicationNode;
                }
                else {
                    Namespace namespace = (Namespace) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    namespace.setName(parts[i]);
                    GUID guid = this.put(namespace);
                    if ( i != 0 ){
                        this.affirmOwnedNode( parentGuid, guid );
                        parentGuid = guid;
                    }
                    else {
                        parentGuid = guid;
                    }

                    ret = namespace;
                }
            }
            else {
                parentGuid = node.getGuid();
            }
        }

        return ret;
    }

    @Override
    public ApplicationNode affirmApplication(String path) {
        return ( ApplicationNode ) this.affirmTreeNodeByPath( path, GenericNamespace.class, GenericApplicationNode.class );
    }

    @Override
    public ServiceTreeNode queryElement(String path) {
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return  this.get( guid );
        }

        return null;
    }

    @Override
    public Namespace affirmNamespace(String path) {
        return ( Namespace ) this.affirmTreeNodeByPath( path, null, GenericNamespace.class );
    }

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
