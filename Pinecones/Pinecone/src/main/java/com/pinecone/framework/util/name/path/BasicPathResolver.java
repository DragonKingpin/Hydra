package com.pinecone.framework.util.name.path;

import java.util.ArrayList;
import java.util.List;

public class BasicPathResolver implements PathResolver {
    protected String mszSepRegex;
    protected String mszSeparator;

    public BasicPathResolver( String szSeparator, String szSepRegex ) {
        this.mszSeparator = szSeparator;
        this.mszSepRegex  = szSepRegex;
    }

    public BasicPathResolver() {
        this( "/", "/" );
    }


    @Override
    public List<String > resolvePath( String[] parts ) {
        ArrayList<String> resolvedParts = new ArrayList<>();
        for (String part : parts) {
            if ( part.equals(".") || part.isEmpty() ) {
                continue;
            }

            if ( part.equals("..") ) {
                if ( !resolvedParts.isEmpty() ) {
                    resolvedParts.remove( resolvedParts.size() - 1 );
                }
            }
            else {
                resolvedParts.add( part );
            }
        }
        return resolvedParts;
    }

    @Override
    public String resolvePath( String path ) {
        String[] parts = this.processPath( path ).split( this.mszSepRegex );
        return this.assemblePath( this.resolvePath( parts ) );
    }

    @Override
    public List<String > resolvePathParts( String path ) {
        return this.resolvePath( this.segmentPathParts( path ) );
    }

    @Override
    public String[] segmentPathParts( String path ) {
        return this.processPath( path ).split( this.mszSepRegex );
    }

    @Override
    public String assemblePath( List<String > parts ) {
        if ( parts == null || parts.size() == 0 ) {
            return "";
        }

        StringBuilder path = new StringBuilder();

        for ( int i = 0; i < parts.size(); ++i ) {
            if ( i > 0 ) {
                path.append( this.mszSeparator );
            }
            path.append( parts.get( i ) );
        }
        return path.toString();
    }

    protected String processPath( String path ) {
        return path;
    }
}
