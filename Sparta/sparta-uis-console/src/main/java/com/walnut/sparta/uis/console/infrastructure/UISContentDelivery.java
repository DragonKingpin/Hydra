package com.walnut.sparta.uis.console.infrastructure;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.tritium.Tritium;

public class UISContentDelivery extends Tritium implements UISContentDeliveryService{
    protected SpartaUISService spartaUISService;

    public UISContentDelivery(String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public UISContentDelivery(String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    public SpartaUISService getSpartaUISService(){
        return this.spartaUISService;
    }

    @Override
    public void vitalize () throws Exception {
        this.spartaUISService = new SpartaUISService("SpartaUISService", this );
        this.spartaUISService.execute();


        this.getTaskManager().add(this.spartaUISService);
        this.getTaskManager().syncWaitingTerminated();
    }
}
