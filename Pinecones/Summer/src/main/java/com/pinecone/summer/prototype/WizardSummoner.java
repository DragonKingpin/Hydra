package com.pinecone.summer.prototype;

import com.pinecone.framework.system.executum.ExecutableSummoner;

import javax.servlet.ServletException;
import java.io.IOException;

public interface WizardSummoner extends ExecutableSummoner {
    HostSystem getSystem();

    String queryNamespace( String szNickName );

    Wizard getLastSummoned();

    Wizard summonIfExist( String szNickName ) throws ServletException, IOException ;

    Wizard summonAndExecute( String szNickName ) throws ServletException, IOException ;
}
