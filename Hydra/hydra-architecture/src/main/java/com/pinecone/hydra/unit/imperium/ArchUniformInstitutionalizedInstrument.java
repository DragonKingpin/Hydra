package com.pinecone.hydra.unit.imperium;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;

/**
 *  Pinecone Ursus For Java Uniform Institutionalized Instrument
 *  Author: Harald.E (Dragon King)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Uniform Institutionalized Instrument
 *  统一编制化元信息中级模型
 *  *****************************************************************************************
 */
public abstract class ArchUniformInstitutionalizedInstrument implements KOMInstrument {

    protected String                superiorPathScope;

    public ArchUniformInstitutionalizedInstrument( String superiorPathScope ) {
        this.superiorPathScope = superiorPathScope;
    }


    @Override
    public String querySystemKernelObjectPath( GUID objectGuid ) {
        String thisScopePath = this.getPath( objectGuid );
        if ( thisScopePath == null ) {
            return null;
        }

        return this.getSuperiorPathScope() + this.getConfig().getPathNameSeparator() + thisScopePath;
    }

    @Override
    public String getSuperiorPathScope() {
        return this.superiorPathScope;
    }

    @Override
    public void applySuperiorPathScope( String superiorPathScope ) {
        this.superiorPathScope = superiorPathScope;
    }

}
