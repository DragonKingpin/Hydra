package com.pinecone.hydra.business.support.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public final class BusinessTags {
    public static final int MAX_TAGS = 16;
    public static final int MAX_LENGTH = 32;

    private BusinessTags() {}

    public static String normalize( String tag ) {
        if ( tag == null || tag.strip().isEmpty() ) {
            throw new IllegalArgumentException( "Tag must not be blank." );
        }
        String value = tag.strip();
        if ( value.length() > MAX_LENGTH || value.codePoints().anyMatch( cp -> cp > 0xffff || Character.isISOControl( cp )
                || ( cp >= 0xd800 && cp <= 0xdfff ) ) ) {
            throw new IllegalArgumentException( "Tag must contain at most 32 UTF-8 BMP characters without control characters." );
        }
        return value;
    }

    public static List<String> normalizeAll( Collection<String> tags ) {
        if ( tags == null ) {
            throw new IllegalArgumentException( "Tags are required; use an empty list to clear tags." );
        }
        LinkedHashSet<String> unique = new LinkedHashSet<>();
        for ( String tag : tags ) {
            unique.add( normalize( tag ) );
        }
        if ( unique.size() > MAX_TAGS ) {
            throw new IllegalArgumentException( "A business node may have at most 16 tags." );
        }
        return new ArrayList<>( unique );
    }

    public static String prefixPattern( String prefix ) {
        String value = prefix == null || prefix.strip().isEmpty() ? "" : normalize( prefix );
        return value.replace( "!", "!!" ).replace( "%", "!%" ).replace( "_", "!_" ) + "%";
    }
}
