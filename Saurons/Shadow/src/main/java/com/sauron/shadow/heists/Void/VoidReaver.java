package com.sauron.shadow.heists.Void;

import com.sauron.radium.heistron.*;

import java.io.IOException;

public class VoidReaver extends MegaDOMIndexCrew implements Reaver {
    public VoidReaver( HTTPIndexHeist heist, int id ){
        super( heist, id );
    }

    @Override
    protected void tryConsumeById( long id ) throws LootRecoveredException, LootAbortException, IllegalStateException, IOException {
        //Page retryPage = this.queryHTTPPageSafe(new Request("https://www.artstation.com/sitemap.xml"));

        //Debug.trace( retryPage.getRawText() );
        //this.terminate();
    }

    @Override
    public void toRavage() {
        this.startBatchTask();
    }
}
