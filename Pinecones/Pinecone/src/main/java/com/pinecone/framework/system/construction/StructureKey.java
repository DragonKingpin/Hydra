package com.pinecone.framework.system.construction;

import java.util.Objects;

import com.pinecone.framework.system.prototype.Pinenut;

public class StructureKey implements Pinenut {
    protected Class<?> mType;
    protected String   mszName = "";
    protected String   mszLookup = "";
    protected String   mszMappedName = "";

    public StructureKey( Class<?> type ) {
        this.mType = type;
    }

    public StructureKey( Class<?> type, StructureDefinition definition ) {
        this( type );
        if ( definition != null ) {
            this.mszLookup = this.notNull( definition.getLookup() );
        }
    }

    public StructureKey( Class<?> type, Structure structure ) {
        this( type );
        if ( structure != null ) {
            this.mszName = this.notNull( structure.name() );
            this.mszLookup = this.notNull( structure.lookup() );
            this.mszMappedName = this.notNull( structure.mappedName() );
        }
    }

    public Class<?> getType() {
        return this.mType;
    }

    public String getName() {
        return this.mszName;
    }

    public String getLookup() {
        return this.mszLookup;
    }

    public String getMappedName() {
        return this.mszMappedName;
    }

    protected String notNull( String szValue ) {
        return szValue == null ? "" : szValue;
    }

    @Override
    public boolean equals( Object that ) {
        if ( this == that ) {
            return true;
        }
        if ( !( that instanceof StructureKey ) ) {
            return false;
        }
        StructureKey key = (StructureKey) that;
        return Objects.equals( this.mType, key.mType )
                && Objects.equals( this.mszName, key.mszName )
                && Objects.equals( this.mszLookup, key.mszLookup )
                && Objects.equals( this.mszMappedName, key.mszMappedName );
    }

    @Override
    public int hashCode() {
        return Objects.hash( this.mType, this.mszName, this.mszLookup, this.mszMappedName );
    }

    @Override
    public String toString() {
        return "StructureKey{"
                + "type=" + ( this.mType == null ? "null" : this.mType.getName() )
                + ", name='" + this.mszName + '\''
                + ", lookup='" + this.mszLookup + '\''
                + ", mappedName='" + this.mszMappedName + '\''
                + '}';
    }
}
