package com.pinecone.framework.util.name;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public interface MultiNamespace extends Namespace, MultiScopeName {
    Collection<MultiNamespace > getParents();

    MultiNamespace              getFirstParent();

    String                      getFullName();

    String                      getFullNameByNS ( String szNS );

    void                        addParent       ( MultiNamespace parent );

    boolean                     hasOwnParent    ( MultiNamespace parent );

    boolean                     hasOwnParentNS  ( String szNS );

    MultiNamespace              getParentByNS   ( String szNS );

    void                        removeParent    ( MultiNamespace parent );

    int                         parentsSize();


    Comparator<Object > DefaultSetNameComparator = new Comparator<>() {
        @Override
        public int compare( Object o1, Object o2 ) {
            if ( o1 instanceof Namespace && o2 instanceof Namespace ) {
                return ( (Namespace) o1 ).getFullName().compareTo( ( (Namespace) o2 ).getFullName() );
            }
            else if ( o1 instanceof String && o2 instanceof String ) {
                return ((String) o1).compareTo( (String) o2 );
            }
            else if ( o1 instanceof Namespace && o2 instanceof String ) {
                return ( (Namespace) o1 ).getNodeName().compareTo( (String) o2 );
            }
            else if ( o1 instanceof String && o2 instanceof Namespace ) {
                return ( (String) o1 ).compareTo( ( (Namespace) o2 ).getNodeName() );
            }
            else {
                throw new IllegalArgumentException( "Objects are not of type Namespace or String" );
            }
        }
    };

    @Override
    default int compareTo( Namespace o ) {
        return this.getNodeName().compareTo( o.getNodeName() );
    }
}
