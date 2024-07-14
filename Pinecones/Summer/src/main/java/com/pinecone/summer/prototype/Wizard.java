package com.pinecone.summer.prototype;

import com.pinecone.framework.system.prototype.Ally;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;

/**
 *  Bean Nuts Pinecone PineconeJava Summer - Wizard
 *  ****************************************************************************************************************
 *  Summer: JSON Based Java Servlet [C/C++ Style]
 *  Matrix: Bean Nuts Pinecone C/CPP Runtime Framework Extension Fast CGI Servlet Summer (JSON Based MVC)
 *  Notice: Pinecone is base on JSON Prototype,
 *  Notice: All functions or methods are based on JSON. We highly recommend you using JSON as data format, it is
 *          easy to compatible with JS, PHP and other platforms.
 *  Notice: For sub modular extends this interface is necessary. Add any function if your json config haves.
 *  ****************************************************************************************************************
 */
public interface Wizard extends Ally, Citizen {
    @Override
    default String vocationName(){
        return this.getClass().getSimpleName();
    }

    String prototypeName();

    String getTitle();

    JSONObject getModularConfig();

    String getModularRole();

    int getModularRoleIndex();

    JSONArray getMyNaughtyGenies();

    String getWizardCommand();


    /***  Parent getter methods ***/
    Connectson getConnection();

    HostSystem getHostSystem();

    ConnectDispatcher getDispatcher();
}
