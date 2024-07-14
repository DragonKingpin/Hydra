package com.sauron.shadow.chronicle;

import com.pinecone.hydra.auto.ArchSuggestation;
import com.pinecone.hydra.auto.ContinueException;
import com.sauron.radium.heistron.orchestration.Instructations;

public class AffinitySuggestation extends ArchSuggestation {
    protected Clerk mClerk;

    public AffinitySuggestation( Clerk clerk ) {
        this.mClerk = clerk;
    }

    @Override
    public void execute() {
        try{
            Instructations.infoConformed( AffinitySuggestation.this.mClerk, "toRavage" );

            AffinitySuggestation.this.mClerk.isTimeToFeast();

            Instructations.infoCompleted( AffinitySuggestation.this.mClerk, "toRavage" );
        }
        catch ( Exception e ) {
            AffinitySuggestation.this.mClerk.tracer().warn(
                    String.format("[Fatality] (%s : %s) <Continue>", e.getClass().getSimpleName(), e.getMessage())
            );
            throw new ContinueException( e );
        }
    }
}
