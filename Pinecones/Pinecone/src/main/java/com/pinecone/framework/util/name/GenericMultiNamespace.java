package com.pinecone.framework.util.name;

import com.pinecone.framework.unit.LinkedTreeSet;

import java.util.*;

public class GenericMultiNamespace extends ArchNamespaceNode implements MultiNamespace {
    protected Set<MultiNamespace > mParents;

    public GenericMultiNamespace( String name ) {
        this( name, new LinkedTreeSet<>( MultiNamespace.DefaultSetNameComparator ), Namespace.DEFAULT_SEPARATOR );
    }

    public GenericMultiNamespace( String name, Set<MultiNamespace > parents ) {
        this( name, parents, Namespace.DEFAULT_SEPARATOR );
    }

    public GenericMultiNamespace( String name, Set<MultiNamespace > parents, String separator ) {
        super( name, separator );
        this.mParents = parents != null ? parents : new LinkedTreeSet<>( MultiNamespace.DefaultSetNameComparator );
    }

    public GenericMultiNamespace( String name, MultiNamespace parent ) {
        this( name, parent, Namespace.DEFAULT_SEPARATOR );
    }

    public GenericMultiNamespace( String name, MultiNamespace parent, String separator ) {
        super( name, separator );

        LinkedTreeSet<MultiNamespace > set = new LinkedTreeSet<>( MultiNamespace.DefaultSetNameComparator );
        if( parent != null ) {
            set.add( parent );
        }

        this.mParents = set;
    }

    public GenericMultiNamespace( String name, String separator ) {
        this( name, new LinkedTreeSet<>( MultiNamespace.DefaultSetNameComparator ), separator );
    }


    @Override
    public Collection<MultiNamespace > getParents() {
        return this.mParents;
    }

    @Override
    public Namespace parent() {
        return this.getFirstParent();
    }

    @Override
    public void setParent( Namespace parent ) {
        this.mParents.clear();
        this.addParent( (MultiNamespace)parent );
    }

    @Override
    public MultiNamespace getFirstParent() {
        return this.mParents.isEmpty() ? null : this.mParents.iterator().next();
    }

    @Override
    public String getFullName() {
        Namespace firstParent = this.getFirstParent();
        if ( firstParent == null ) {
            return this.mszName;
        }
        return firstParent.getFullName() + this.getSeparator() + this.mszName;
    }

    @Override
    public List<String > getFullNames() {
        List<String > fullNames = new ArrayList<>();
        if( this.mParents.isEmpty() ) {
            fullNames.add( this.getNodeName() );
        }
        else {
            for ( MultiNamespace parent : this.mParents ) {
                this.addFullNames( parent, fullNames );
            }
        }
        return fullNames;
    }

    protected void addFullNames( MultiNamespace namespace, List<String > fullNames ) {
        List<String > parentFullNames = namespace.getFullNames();
        for ( String parentFullName : parentFullNames ) {
            String fullName = parentFullName + this.getSeparator() + this.getNodeName();
            fullNames.add( fullName );
        }
    }

    @Override
    public String getFullNameByNS( String szNS ) {
        for ( Namespace parent : this.mParents ) {
            if ( parent.getNodeName().equals( szNS ) ) {
                return parent.getFullName() + this.getSeparator() + this.mszName;
            }
        }
        return null;
    }

    @Override
    public void addParent( MultiNamespace parent ) {
        this.mParents.add( parent );
    }

    @Override
    public boolean hasOwnParent( MultiNamespace parent ) {
        return this.mParents.contains( parent );
    }

    @Override
    public boolean hasOwnParentNS( String szNS ) {
        return this.mParents.contains( szNS );
    }

    @Override
    public MultiNamespace getParentByNS( String szNS ) {
        for ( MultiNamespace parent : this.mParents ) {
            if ( parent.getNodeName().equals(szNS) ) {
                return parent;
            }
        }
        return null;
    }

    @Override
    public void removeParent( MultiNamespace parent ) {
        this.mParents.remove( parent );
    }

    @Override
    public int parentsSize() {
        return this.mParents.size();
    }

    @Override
    public String getSimpleName() {
        return getNodeName();
    }


    @Override
    public boolean equals( Object obj ) {
        if( obj instanceof MultiNamespace ) {
            return this.getFullNames().equals( ((MultiNamespace) obj).getFullNames() );
        }
        return false;
    }
}