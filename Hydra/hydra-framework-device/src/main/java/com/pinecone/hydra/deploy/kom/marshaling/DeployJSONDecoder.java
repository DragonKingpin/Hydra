package com.pinecone.hydra.deploy.kom.marshaling;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.deploy.kom.entity.FolderElement;
import com.pinecone.hydra.deploy.kom.entity.GenericClusterElement;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.deploy.kom.entity.GenericDeployElement;
import com.pinecone.hydra.deploy.kom.entity.ClusterElement;
import com.pinecone.hydra.deploy.kom.entity.Namespace;

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

    protected Object[]    affirmAppExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        ClusterElement app = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceJobElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `JobElement`.", szName )
                    );
                }

                app = rootE.evinceJobElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            if( parentNode instanceof Namespace ) {
                Collection<ElementNode> destChildren = parentNode.evinceNamespace().fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof ClusterElement) {
                            app = (ClusterElement) node;
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
        if( app == null ) {
            neo = new GenericClusterElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = app;
        }
        return new Object[] { app, neo };
    }

    protected Object[]    affirmSerExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        DeployElement ser = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceDeployElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `TaskElement`.", szName )
                    );
                }

                ser = rootE.evinceDeployElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            Collection<ElementNode> destChildren;
            if( parentNode instanceof FolderElement ) {
                destChildren = ( (FolderElement) parentNode ).fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof DeployElement) {
                            ser = (DeployElement) node;
                            break;
                        }
                        else {
                            throw new IllegalArgumentException(
                                    String.format( "Existed child-destination [%s] should be `TaskElement`.", szName )
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



        DeployElement neo ;
        if( ser == null ) {
            neo = new GenericDeployElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = ser;
        }
        return new Object[] { ser, neo };
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
                pair = this.affirmAppExisted( szName, parentGuid, jo );
                bIsFolderElement = true;
            }
            else if( szMetaType.equals( DeployElement.class.getSimpleName() ) ) {
                pair = this.affirmSerExisted( szName, parentGuid, jo );
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
                Object services = jo.get( "tasks" );
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
