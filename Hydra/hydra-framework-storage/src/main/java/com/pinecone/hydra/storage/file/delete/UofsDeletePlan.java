package com.pinecone.hydra.storage.file.delete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class UofsDeletePlan implements Pinenut {
    protected final GUID rootGuid;
    protected final List<UofsDeletePlanItem> items = new ArrayList<>();

    public UofsDeletePlan( GUID rootGuid ) {
        this.rootGuid = rootGuid;
    }

    public GUID getRootGuid() {
        return this.rootGuid;
    }

    public void add( UofsDeletePlanItem item ) {
        if ( item != null ) {
            this.items.add( item );
        }
    }

    public List<UofsDeletePlanItem> getItemsByTargetType( UofsDeleteTargetType targetType ) {
        List<UofsDeletePlanItem> ret = new ArrayList<>();
        for ( UofsDeletePlanItem item : this.items ) {
            if ( item.getTargetType() == targetType ) {
                ret.add( item );
            }
        }
        return ret;
    }

    public List<UofsDeletePlanItem> getItems() {
        return Collections.unmodifiableList( this.items );
    }
}
