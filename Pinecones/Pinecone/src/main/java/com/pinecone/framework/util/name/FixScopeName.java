package com.pinecone.framework.util.name;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.name.ArchName;
import com.pinecone.framework.util.name.MultiScopeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FixScopeName extends ArchName implements MultiScopeName {
    protected List<String >   mDomains;
    protected List<String >   mSuffixes;

    public FixScopeName( String szName, String szDomain ) {
        this( szName );

        this.mDomains.add  ( szDomain );
    }

    public FixScopeName( String szName, String szDomain, String szSuffix ) {
        this( szName );

        this.mDomains.add  ( szDomain );
        this.mSuffixes.add ( szSuffix );
    }

    public FixScopeName( String szName, @Nullable String[] domains, @Nullable String[] suffixes ) {
        this( szName );
        if ( domains != null ) {
            this.mDomains.addAll( Arrays.asList( domains ) );
        }
        if ( suffixes != null ) {
            this.mSuffixes.addAll( Arrays.asList( suffixes ) );
        }
    }

    public FixScopeName( String szName, List<String > domains, List<String > suffixes ) {
        super( szName );

        this.mDomains  = domains;
        this.mSuffixes = suffixes;
    }

    public FixScopeName( String szName ) {
        super( szName );

        this.mDomains   = new ArrayList<>();
        this.mSuffixes  = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public String getFullName() {
        if ( !this.mDomains.isEmpty() && !this.mSuffixes.isEmpty() ) {
            return this.mDomains.get( 0 ) + this.mszName + this.mSuffixes.get( 0 );
        }
        else if ( !this.mDomains.isEmpty() ) {
            return this.mDomains.get( 0 ) + this.mszName;
        }
        else if ( !this.mSuffixes.isEmpty() ) {
            return this.mszName + this.mSuffixes.get( 0 );
        }
        else {
            return this.mszName;
        }
    }

    @Override
    public String getDomain(){
        if ( !this.mDomains.isEmpty() ) {
            return this.mDomains.get( 0 );
        }
        else {
            return "";
        }
    }

    @Override
    public String toString() {
        return this.getFullName();
    }

    @Override
    public List<String> getFullNames() {
        List<String> fullNames = new ArrayList<>();
        if ( this.mDomains.isEmpty() && this.mSuffixes.isEmpty() ) {
            fullNames.add( this.mszName );
        }
        else if ( this.mDomains.isEmpty() ) {
            for ( String suffix : this.mSuffixes ) {
                fullNames.add( this.mszName + suffix );
            }
        }
        else if ( this.mSuffixes.isEmpty() ) {
            for ( String domain : this.mDomains ) {
                fullNames.add( domain + this.mszName );
            }
        }
        else {
            for ( String domain : this.mDomains ) {
                for ( String suffix : this.mSuffixes ) {
                    fullNames.add( domain + this.mszName + suffix );
                }
            }
        }
        return fullNames;
    }

    public void addDomain( String domain ) {
        this.mDomains.add( domain );
    }

    public void addSuffix( String suffix ) {
        this.mSuffixes.add( suffix );
    }

    public List<String> getDomains() {
        return this.mDomains;
    }

    public List<String> getSuffixes() {
        return this.mSuffixes;
    }
}
