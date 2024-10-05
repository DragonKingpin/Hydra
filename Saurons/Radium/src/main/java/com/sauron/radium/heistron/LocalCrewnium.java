package com.sauron.radium.heistron;

import com.sauron.radium.heistron.scheduler.LocalPreemptiveSingleFrame64Consumer;
import com.sauron.radium.heistron.scheduler.PageFrame64ConsumerAdapter;
import com.sauron.radium.heistron.scheduler.TaskConsumer;
import com.sauron.radium.heistron.scheduler.TaskFrame64Producer;
import com.pinecone.framework.system.executum.ArchThreadum;

public abstract class LocalCrewnium extends ArchThreadum implements Crewnium {
    protected int                            mnCrewId;
    protected Heistum                        mParentHeist;
    protected TaskConsumer                   mTaskConsumer;
    protected PageFrame64ConsumerAdapter     mFrame64ConsumerAdapter = LocalCrewnium.this::consumeById;


    public LocalCrewnium ( Heist heist, int nCrewId )  {
        super( null, heist.getHeistium() );

        this.mnCrewId      = nCrewId;
        this.mParentHeist  = heist;

        Thread affinityThread = new Thread( this );
        this.setThreadAffinity( affinityThread );
        this.getAffiliateThread().setName( this.nomenclature() );
        this.setName( affinityThread.getName() );

//        this.mTaskConsumer = new LocalSingleTaskPageConsumer(
//                (TaskPageProducer) this.parentExecutum().getTaskProducer(), this.mFrame64ConsumerAdapter
//        );

        this.mTaskConsumer = new LocalPreemptiveSingleFrame64Consumer(
                (TaskFrame64Producer) this.parentExecutum().getTaskProducer(), this.mFrame64ConsumerAdapter
        );
    }

    protected abstract void consumeById( long index );

    @Override
    public Heistium parentExecutum() {
        return (Heistium) super.parentExecutum();
    }

    protected String nomenclature() {
        String szHeistName;
        if( this.mParentHeist instanceof CascadeHeist ) {
            szHeistName = ((CascadeHeist) this.mParentHeist).getInstanceFullName();
        }
        else {
            szHeistName = this.mParentHeist.heistName();
        }
        return String.format(
                "%s-%s", szHeistName, this.getAffiliateThread().getName()
        ).toLowerCase();
    }
}
