package com.sauron.radium.heistron;

import us.codecraft.webmagic.Request;

import java.io.File;
import java.io.IOException;

public abstract class MegaDOMIndexCrew extends HTTPCrew {
    public MegaDOMIndexCrew ( HTTPIndexHeist heist, int id ){
        super( heist, id );
    }

    @Override
    public HTTPIndexHeist parentHeist() {
        return (HTTPIndexHeist) this.heist;
    }

    public String queryHrefById ( long id ) {
        return this.parentHeist().queryHrefById( id );
    }

    public String queryFragNamespace( long id ) {
        id = (id == 0 ? 1 : id);
        long nBase = this.fragRange / this.fragBase;
        long nLow = id / this.fragRange;
        long nMod = id % this.fragRange;

        long nAbove = nLow;
        if ( nMod != 0 || id % 10 == 0 ) {
            ++nAbove;
        }

        nAbove *= nBase;
        nLow *= nBase;

        String szNS = nLow + "W";
        szNS += "_" + nAbove + "W";

        return szNS;
    }

    public String querySpoilStorageDir( long id ) {
        return this.parentHeist().spoilPath + this.queryFragNamespace( id ) + "/";
    }

    public String querySpoilStoragePath( long id ) {
        return this.querySpoilStorageDir( id ) + "page_" + id + ".html";
    }

    @Override
    protected void tryConsumeById( long id ) throws LootRecoveredException, LootAbortException, IllegalStateException, IOException {
        String szStorageDir  = this.querySpoilStorageDir ( id );
        String szStoragePath = this.querySpoilStoragePath( id );

        File storageDir = new File( szStorageDir );
        if ( !storageDir.isDirectory() ) {
            storageDir.mkdirs();
        }

        String szDummyHref = this.queryHrefById( id );
        String szHref;
        if ( szDummyHref.startsWith( "http" ) ) {
            szHref = szDummyHref;
        }
        else {
            szHref = this.heistURL + szDummyHref;
        }


        Request request = new Request( szHref );
        request.putExtra( "id", id );
        this.storeHrefCache( szStoragePath, request );
    }
}
