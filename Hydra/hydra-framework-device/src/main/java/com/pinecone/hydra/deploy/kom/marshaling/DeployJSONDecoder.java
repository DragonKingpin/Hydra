package com.pinecone.hydra.deploy.kom.marshaling;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ContainerElement;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.deploy.kom.entity.FolderElement;
import com.pinecone.hydra.deploy.kom.entity.GenericClusterElement;
import com.pinecone.hydra.deploy.kom.entity.GenericContainerElement;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.deploy.kom.entity.ClusterElement;
import com.pinecone.hydra.deploy.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;

public class DeployJSONDecoder implements DeployInstrumentDecoder {
    protected DeployInstrument instrument;

    public DeployJSONDecoder(DeployInstrument instrument ) {
        this.instrument = instrument;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public ElementNode    decode( String szName, Object o, GUID parentGuid ) {
        if ( o instanceof Map ) {
            return (ElementNode) this.instrument.get( this.decodeJSONObject( szName, (Map<String, Object>) o, parentGuid ).getGuid() );
        }

        throw new IllegalArgumentException( "Elements of `DeployInstrument` should all be object." );
    }

    protected Namespace   newNamespace( String szName, Map<String, Object > jo ) {
        Namespace ns = new GenericNamespace( jo, this.instrument );
        ns.setName( szName );

        return ns;
    }

    protected Object[]    affirmNSExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        Namespace ns = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceNamespace() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be namespace.", szName )
                    );
                }

                ns = rootE.evinceNamespace();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            if( parentNode instanceof Namespace ) {
                Collection<ElementNode> destChildren = parentNode.evinceNamespace().fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof Namespace ) {
                            ns = (Namespace) node;
                            break;
                        }
                        else {
                            throw new IllegalArgumentException(
                                    String.format( "<DeployInstrument> Existed child-destination [%s] should be namespace.", szName )
                            );
                        }
                    }
                }
            }
        }


        GUID currentGuid;
        if( ns == null ) {
            ns = this.newNamespace( szName, jo );
            currentGuid  = this.instrument.put( ns );
            this.instrument.affirmOwnedNode( parentGuid, currentGuid );
        }
        else {
            currentGuid = ns.getGuid();
        }
        return new Object[] { ns, currentGuid };
    }

    protected Object[]    affirmClusterExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        ClusterElement cluster = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceClusterElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `JobElement`.", szName )
                    );
                }

                cluster = rootE.evinceClusterElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            if( parentNode instanceof Namespace ) {
                Collection<ElementNode> destChildren = parentNode.evinceNamespace().fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof ClusterElement) {
                            cluster = (ClusterElement) node;
                            break;
                        }
                        else {
                            throw new IllegalArgumentException(
                                    String.format( "Existed child-destination [%s] should be `JobElement`.", szName )
                            );
                        }
                    }
                }
            }
        }



        ClusterElement neo ;
        if( cluster == null ) {
            neo = new GenericClusterElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = cluster;
        }
        return new Object[] { cluster, neo };
    }

    protected Object[]    affirmPhyExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        PhysicalHostElement dep = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evincePhysicalHostElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `TaskElement`.", szName )
                    );
                }

                dep = rootE.evincePhysicalHostElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            Collection<ElementNode> destChildren;
            if( parentNode instanceof FolderElement ) {
                destChildren = ( (FolderElement) parentNode ).fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof PhysicalHostElement ) {
                            dep = (PhysicalHostElement) node;
                            break;
                        }
                        else {
                            throw new IllegalArgumentException(
                                    String.format( "Existed child-destination [%s] should be `PhysicalHostElement`.", szName )
                            );
                        }
                    }
                }
            }
            else {
                throw new IllegalStateException(
                        String.format( "Parent of `TaskElement` [%s] should be `FolderElement`.", szName )
                );
            }
        }



        PhysicalHostElement neo ;
        if( dep == null ) {
            neo = new GenericPhysicalHostElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = dep;
        }
        return new Object[] { dep, neo };
    }

    protected Object[]    affirmVMExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        VirtualMachineElement dep = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceVirtualMachineElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `TaskElement`.", szName )
                    );
                }

                dep = rootE.evinceVirtualMachineElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            Collection<ElementNode> destChildren;
            if( parentNode instanceof FolderElement ) {
                destChildren = ( (FolderElement) parentNode ).fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof VirtualMachineElement ) {
                            dep = (VirtualMachineElement) node;
                            break;
                        }
                        else {
                            throw new IllegalArgumentException(
                                    String.format( "Existed child-destination [%s] should be `VirtualMachineElement`.", szName )
                            );
                        }
                    }
                }
            }
            else {
                throw new IllegalStateException(
                        String.format( "Parent of `TaskElement` [%s] should be `FolderElement`.", szName )
                );
            }
        }



        VirtualMachineElement neo ;
        if( dep == null ) {
            neo = new GenericVirtualMachineElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = dep;
        }
        return new Object[] { dep, neo };
    }

    protected Object[]    affirmQuickExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        QuickElement dep = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceQuickElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `TaskElement`.", szName )
                    );
                }

                dep = rootE.evinceQuickElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            Collection<ElementNode> destChildren;
            if( parentNode instanceof FolderElement ) {
                destChildren = ( (FolderElement) parentNode ).fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof QuickElement ) {
                            dep = (QuickElement) node;
                            break;
                        }
                        else {
                            throw new IllegalArgumentException(
                                    String.format( "Existed child-destination [%s] should be `QuickElement`.", szName )
                            );
                        }
                    }
                }
            }
            else {
                throw new IllegalStateException(
                        String.format( "Parent of `TaskElement` [%s] should be `FolderElement`.", szName )
                );
            }
        }



        QuickElement neo ;
        if( dep == null ) {
            neo = new GenericQuickElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = dep;
        }
        return new Object[] { dep, neo };
    }

    protected Object[]    affirmContainerExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        ContainerElement dep = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceContainerElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `TaskElement`.", szName )
                    );
                }

                dep = rootE.evinceContainerElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            Collection<ElementNode> destChildren;
            if( parentNode instanceof FolderElement ) {
                destChildren = ( (FolderElement) parentNode ).fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof ContainerElement ) {
                            dep = (ContainerElement) node;
                            break;
                        }
                        else {
                            throw new IllegalArgumentException(
                                    String.format( "Existed child-destination [%s] should be `ContainerElement`.", szName )
                            );
                        }
                    }
                }
            }
            else {
                throw new IllegalStateException(
                        String.format( "Parent of `TaskElement` [%s] should be `FolderElement`.", szName )
                );
            }
        }



        ContainerElement neo ;
        if( dep == null ) {
            neo = new GenericContainerElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = dep;
        }
        return new Object[] { dep, neo };
    }


    protected Object[]    decodeExternalElements( String szMetaType, String szName, GUID parentGuid, Map<String, Object > jo ) throws IllegalArgumentException {
        throw new IllegalArgumentException( "Unknown metaType '" + szMetaType + "'." );
    }

    protected void        decodeChildren ( Map jo, GUID currentGuid ) {
        for ( Object o : jo.entrySet() ) {
            Map.Entry kv = (Map.Entry) o;
            Object   val = kv.getValue();
            if( val instanceof Map ) {
                this.decode( kv.getKey().toString(), val, currentGuid );
            }
        }
    }

    protected ElementNode decodeJSONObject( String szName, Map<String, Object > jo, GUID parentGuid ) {
        String szMetaType = (String) jo.get( "metaType" );
        boolean isNamespace = szMetaType == null || szMetaType.equals( Namespace.class.getSimpleName() );
        ElementNode elementNode;
        GUID currentGuid;

        if ( isNamespace ) {
            Object[] pair = this.affirmNSExisted( szName, parentGuid, jo );
            Namespace     ns = (Namespace) pair[ 0 ];
            currentGuid      = (GUID)      pair[ 1 ];

            this.decodeChildren( jo, currentGuid );

            elementNode = ns;
        }
        else {
            Object[] pair;
            boolean bIsFolderElement = false;
            if( szMetaType.equals( ClusterElement.class.getSimpleName() ) ) {
                pair = this.affirmClusterExisted( szName, parentGuid, jo );
                bIsFolderElement = true;
            }
            else if( szMetaType.equals( PhysicalHostElement.class.getSimpleName() ) ) {
                pair = this.affirmPhyExisted( szName, parentGuid, jo );
            }
            else if( szMetaType.equals( VirtualMachineElement.class.getSimpleName() ) ) {
                pair = this.affirmVMExisted( szName, parentGuid, jo );
            }
            else if( szMetaType.equals( QuickElement.class.getSimpleName() ) ) {
                pair = this.affirmQuickExisted( szName, parentGuid, jo );
            }
            else if( szMetaType.equals( ContainerElement.class.getSimpleName() ) ) {
                pair = this.affirmContainerExisted( szName, parentGuid, jo );
            }
            else {
                try{
                    pair = this.decodeExternalElements( szMetaType, szName, parentGuid, jo );
                }
                catch ( RuntimeException e ) {
                    throw new IllegalArgumentException( e );
                }
            }

            ElementNode          arc = (ElementNode) pair[ 0 ];
            ElementNode          neo = (ElementNode) pair[ 1 ];

            if( arc == null ) {
                currentGuid = this.instrument.put( neo );
                this.instrument.affirmOwnedNode( parentGuid, currentGuid );
            }
            else {
                currentGuid = arc.getGuid();
                this.instrument.update( neo );
            }

            if( bIsFolderElement ) {
                Object services = jo.get( "deployments" );
                if( services instanceof Map ) {
                    Map joSer = (Map) services;
                    this.decodeChildren( joSer, currentGuid );
                }
            }

            elementNode = neo;
        }

        return elementNode;
    }

}
