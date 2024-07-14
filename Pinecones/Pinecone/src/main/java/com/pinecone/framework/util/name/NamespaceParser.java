package com.pinecone.framework.util.name;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.List;
import java.util.regex.Pattern;

public interface NamespaceParser extends Pinenut {
    default Namespace parse( String szNamespaceStr, List<String> separators ) {
        if ( separators == null || separators.isEmpty() ) {
            throw new IllegalArgumentException( "Separators list cannot be null or empty" );
        }

        StringBuilder regexBuilder = new StringBuilder();
        for ( String sep : separators ) {
            regexBuilder.append( Pattern.quote(sep) ).append( "|" );
        }
        String regex = regexBuilder.substring( 0, regexBuilder.length() - 1 );
        return this.parse( szNamespaceStr, Pattern.compile( regex ) );
    }

    default Namespace parse( String szNamespaceStr, String szSeparatorsRegex ) {
        if ( szSeparatorsRegex == null || szSeparatorsRegex.isEmpty() ) {
            throw new IllegalArgumentException( "Regex string cannot be null or empty" );
        }
        return this.parse( szNamespaceStr, Pattern.compile( szSeparatorsRegex ) );
    }

    Namespace parse( String namespaceStr, Pattern separatorsPattern );
}
