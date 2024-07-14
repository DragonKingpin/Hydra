package com.pinecone.summer.prototype;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.summer.multiparts.MultipartFile;
import com.pinecone.summer.NaughtyGenieInvokedException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *  Pinecone For Java Wizardum [ Wizard Kernel Layer Prototype Interface ]
 *  Copyright © 2008 - 2028 Bean Nuts Foundation ( DR.Undefined ) All rights reserved. [Mr.A.R.B / WJH]
 *  *****************************************************************************************
 *  JSON Based: All dynamic map variables are based on JSON.
 *  PHP Style: QueryString, Form, Files, and etc. are overridden to $_GET, $_POST, and etc.
 *  *****************************************************************************************
 */
public interface Wizardum extends Wizard {
    JSONObject $_GPC();

    JSONObject $_GET();

    JSONObject $_POST();

    PrintWriter writer() ;

    ServletOutputStream out() ;

    HttpServletRequest $_REQUEST();

    HttpServletRequest getCurrentMultipartRequest();

    HttpServletResponse $_RESPONSE();

    Map<String, Cookie > $_COOKIE();

    Map<String, MultipartFile> $_FILES();

    void redirect( String szURL ) throws IOException;

    String spawnWizardQuerySpell( String szPrototype );

    String spawnActionQuerySpell( String szActionFunctionName ) ;

    String spawnControlQuerySpell( String szControlFunctionName ) ;

    String spawnActionControlSpell( String szActionFnName, String szControlFnName );

    Object summonNormalGenieByCallHisName( String szGenieName ) throws NaughtyGenieInvokedException;

    String getWizardCommand();

    String getModelCommand();

    String getControlCommand();

    void stop() throws RuntimeException;

}
