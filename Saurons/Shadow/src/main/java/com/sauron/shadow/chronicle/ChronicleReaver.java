package com.sauron.shadow.chronicle;

import com.sauron.radium.heistron.HTTPCrew;
import com.sauron.radium.heistron.HTTPHeist;
import com.sauron.radium.heistron.LootRecoveredException;
import com.sauron.radium.heistron.Reaver;
import com.sauron.radium.heistron.LootAbortException;

import java.io.IOException;

public class ChronicleReaver extends HTTPCrew implements Reaver {
    public ChronicleReaver( HTTPHeist heist, int id ){
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
