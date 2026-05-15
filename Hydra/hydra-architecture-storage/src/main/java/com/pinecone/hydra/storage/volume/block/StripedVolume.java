package com.pinecone.hydra.storage.volume.block;

import com.pinecone.hydra.storage.volume.core.VolumeExtent;

import java.util.List;

public interface StripedVolume extends BlockVolume {
    long getStripeUnit();

    void setStripeUnit( long stripeUnit );

    long getMemberLength();

    List<VolumeExtent> getMembers();

    void addMember( VolumeExtent extent );
}
