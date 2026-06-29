package com.pinecone.hydra.system.imperium;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ObjectFacilityFacet extends Pinenut {

    default String facilityName() {
        return this.facilityClass().name();
    }

    // Major
    FacilityClass facilityClass();

    // Major and hosting minor classes.
    default FacilityClass[] ownedClass() {
        return new  FacilityClass[]{ this.facilityClass() };
    }
}
