package com.pinecone.summer.prototype;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.summer.RouterType;

import javax.servlet.ServletException;

public interface HostSystem {
    JSONObject getGlobalConfig() ;

    JSONObject getSystemConfig() ;

    JSONObject getPublicWizardConfig();

    String getControlParameter() ;

    String getWizardParameter()  ;

    String getModelParameter()   ;

    void init() throws ServletException;

    String getSystemPath();

    String getRootClassPath();



    String getWizardSummonerConfig();

    String getWizardPackageName();

    String getModelClassSuffix();

    String getControlClassSuffix();


    ConnectDispatcher handleByDispatcher(RouterType routerType );

    default ConnectDispatcher handleByDispatcher() {
        return this.handleByDispatcher( RouterType.QueryString );
    }

    RouterDispatcher getPrimeRouterDispatcher();
}
