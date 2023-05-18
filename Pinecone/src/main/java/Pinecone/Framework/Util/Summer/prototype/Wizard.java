package Pinecone.Framework.Util.Summer.prototype;

import Pinecone.Framework.System.Prototype.Ally;
import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;

/**
 *  Bean Nuts Pinecone PineconeJava Summer - Wizard
 *  ****************************************************************************************************************
 *  Summer: JSON Based Java Servlet [C/C++ Style]
 *  Matrix: Bean Nuts Pinecone C/CPP Runtime Framework Extension Fast CGI Servlet Summer (JSON Based MVC)
 *  Notice: Pinecone is base on JSON,
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

    JSONObject getPrivateTables();

    JSONObject getModularConfig();

    String getModularRole();

    int getModularRoleIndex();

    JSONArray getMyNaughtyGenies();

    String getWizardCommand();
}
