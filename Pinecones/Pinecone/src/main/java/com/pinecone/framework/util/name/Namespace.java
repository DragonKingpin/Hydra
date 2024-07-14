package com.pinecone.framework.util.name;

public interface Namespace extends Name, Comparable<Namespace > {
    String      DEFAULT_SEPARATOR = ".";

    Namespace   parent();

    void setParent( Namespace parent );

    default Namespace   root(){
        Namespace p = this;
        Namespace c = p;
        while ( p != null ) {
            c = p;
            p = p.parent();
        }

        return c;
    }

    default String      rootName() {
        return this.root().getNodeName();
    }

    String      getSeparator();

    String      getNodeName();

    String      getSimpleName();

    String      getFullName();

    void        setSeparator  ( String separator );

    @Override
    default String      getName() {
        return this.getNodeName();
    }

    @Override
    default String      getDomain() {
        Namespace p = this.parent();
        if( p != null ) {
            return p.getFullName();
        }
        return "";
    }

    @Override
    default int compareTo( Namespace o ) {
        return this.getFullName().compareTo( o.getFullName() );
    }
}
