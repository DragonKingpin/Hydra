package Pinecone.Framework.Util.Summer.prototype;

import Pinecone.Framework.System.Prototype.ExecutableSummoner;

import javax.servlet.ServletException;
import java.io.IOException;

public interface JSONBasedSummoner extends ExecutableSummoner {
    HostSystem getParentMatrix();

    String spawnNamespace( String szNickName );

    Wizard getLastSummoned();

    void summonAndExecute( String szNickName ) throws ServletException, IOException ;
}
