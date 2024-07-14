package com.pinecone.framework.util.name;

import com.pinecone.framework.system.PineRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericNamespaceParser implements NamespaceParser {
    protected Class<? extends Namespace> namespaceClass;

    public GenericNamespaceParser() {
        this( UniNamespace.class );
    }

    public GenericNamespaceParser( Class<? extends Namespace> namespaceClass ) {
        this.namespaceClass = namespaceClass;
    }

    public void setNamespaceClass( Class<? extends Namespace> namespaceClass ) {
        this.namespaceClass = namespaceClass;
    }

    @Override
    public Namespace parse( String szNamespaceStr, Pattern pattern ) {
        if ( pattern == null ) {
            throw new IllegalArgumentException( "Pattern cannot be null" );
        }

        List<String > parts = new ArrayList<>();
        List<String > usedSeparators = new ArrayList<>();

        Matcher matcher = pattern.matcher( szNamespaceStr );
        int lastIndex = 0;
        while ( matcher.find() ) {
            String part = szNamespaceStr.substring( lastIndex, matcher.start() );
            parts.add(part);
            usedSeparators.add(matcher.group());
            lastIndex = matcher.end();
        }
        // Add the last one.
        parts.add( szNamespaceStr.substring( lastIndex ) );

        // Create the Namespace tree.
        Namespace current = null;
        String currentSeparator = null;
        for ( int i = 0; i < parts.size(); ++i ) {
            String part = parts.get(i);
            String nextSeparator = i < usedSeparators.size() ? usedSeparators.get(i) : Namespace.DEFAULT_SEPARATOR;
            current = this.newNamespaceInstance( part, current, currentSeparator );
            currentSeparator = nextSeparator;
        }

        return current;
    }

    protected Namespace newNamespaceInstance( String name, Namespace parent, String separator ) {
        try {
            return this.namespaceClass.getConstructor( String.class, Namespace.class, String.class ).newInstance( name, parent, separator );
        }
        catch ( Exception e ) {
            try {
                return this.namespaceClass.getConstructor( String.class, MultiNamespace.class, String.class ).newInstance( name, (MultiNamespace)parent, separator );
            }
            catch ( Exception e2 ) {
                throw new PineRuntimeException( "Failed to instantiate namespace class", e2 );
            }
        }
    }
}
