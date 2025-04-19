package com.pinecone.hydra.task.kom.marshaling;

import java.util.Collection;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.FolderElement;
import com.pinecone.hydra.task.kom.entity.GenericJobElement;
import com.pinecone.hydra.task.kom.entity.GenericNamespace;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;

public class TaskJSONDecoder implements TaskInstrumentDecoder {
    protected TaskInstrument instrument;

    public TaskJSONDecoder( TaskInstrument instrument ) {
        this.instrument = instrument;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public ElementNode    decode( String szName, Object o, GUID parentGuid ) {
        if ( o instanceof Map ) {
            return (ElementNode) this.instrument.get( this.decodeJSONObject( szName, (Map<String, Object>) o, parentGuid ).getGuid() );
        }

        throw new IllegalArgumentException( "Elements of `ServersInstrument` should all be object." );
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
                                    String.format( "<TaskInstrument> Existed child-destination [%s] should be namespace.", szName )
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
        JobElement app = null;

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
                        if( node instanceof JobElement ) {
                            app = (JobElement) node;
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



        JobElement neo ;
        if( app == null ) {
            neo = new GenericJobElement( jo, this.instrument );
            neo.setName( szName );
        }
        else {
            neo = app;
        }
        return new Object[] { app, neo };
    }

    protected Object[]    affirmSerExisted( String szName, GUID parentGuid, Map<String, Object > jo ) {
        TaskElement ser = null;

        if( parentGuid == null ) {
            ElementNode rootE = this.instrument.queryElement( szName );
            if( rootE != null ) {
                if( rootE.evinceTaskElement() == null ) {
                    throw new IllegalArgumentException(
                            String.format( "Existed child-destination [%s] should be `TaskElement`.", szName )
                    );
                }

                ser = rootE.evinceTaskElement();
            }
        }
        else {
            ElementNode parentNode = (ElementNode)this.instrument.get( parentGuid );
            Collection<ElementNode> destChildren;
            if( parentNode instanceof FolderElement ) {
                destChildren = ( (FolderElement) parentNode ).fetchChildren();
                for( ElementNode node : destChildren ) {
                    if( szName.equals( node.getName() ) ) {
                        if( node instanceof TaskElement ) {
                            ser = (TaskElement) node;
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



        TaskElement neo ;
        if( ser == null ) {
            neo = new GenericTaskElement( jo, this.instrument );
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
            if( szMetaType.equals( JobElement.class.getSimpleName() ) ) {
                pair = this.affirmAppExisted( szName, parentGuid, jo );
                bIsFolderElement = true;
            }
            else if( szMetaType.equals( TaskElement.class.getSimpleName() ) ) {
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
                Object services = jo.get( "services" );
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
