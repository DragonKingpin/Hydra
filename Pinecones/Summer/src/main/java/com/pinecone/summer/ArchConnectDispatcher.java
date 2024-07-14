package com.pinecone.summer;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.JSONException;
import com.pinecone.summer.http.HttpEntityParser;
import com.pinecone.summer.prototype.ConnectDispatcher;
import com.pinecone.summer.prototype.Pagesion;
import com.pinecone.summer.prototype.Wizard;
import com.pinecone.Pinecone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 *  Pinecone For Java SystemDispatcher [ Bean Nuts Pinecone PineconeJava Summer SystemDispatcher ]
 *  Copyright © 2008 - 2024 Bean Nuts Foundation ( DR.Undefined ) All rights reserved. [Mr.A.R.B / WJH]
 *  Tip:
 *  *****************************************************************************************
 *  For Pinecone Net Family, we adopted PHP-Style as function paradigm because for Pinecone CPP
 *  we has been referenced PHP-Style for long history and in this case there is almost no inappropriate
 *  scenarios has found so for each version of Pinecone in other language we still inherited Pinecone CPP.
 *  For following case we defined:
 *   => $_GET      : Parsed query string key-values json object.
 *   => $_POST     : Whichever form or multipart for key-values json object.
 *   => $_GPC      : Using Java parameter map but json object format.
 *   => $_FILES    : Like PHP $_FILES just files map.
 *   => $_REQUEST  : Currently session global http request.
 *   => $_RESPONSE : Currently session global http response.
 *  *****************************************************************************************
 */
public class ArchConnectDispatcher implements ConnectDispatcher {
    protected ArchHostSystem      mArchHostSystem;
    protected RouterType          mRouterType       = RouterType.QueryString;
    protected String              mszURI            = "";
    protected String[]            mURIParts         = new String[0];
    protected String              mszDomainHref     = "";
    protected ArchWizardSummoner  mWizardSummoner   = null;
    protected HttpEntityParser    mHttpEntityParser = null ;

    protected String mszWizardCommand   = null;
    protected String mszModelCommand    = null;
    protected String mszControlCommand  = null;

    protected ArchConnection mConnection  = null;

    public ArchConnectDispatcher( ArchHostSystem system, RouterType routerType ){
        this.mArchHostSystem   = system;
        this.mHttpEntityParser = this.mArchHostSystem.mHttpEntityParser;
        this.mRouterType       = routerType;
    }

    public ArchHostSystem getHostSystem(){
        return this.mArchHostSystem;
    }

    public HttpEntityParser getHttpEntityParser(){
        return this.mHttpEntityParser;
    }

    public ArchWizardSummoner getWizardSummoner() {
        return this.mWizardSummoner;
    }

    public ArchConnection getConnection(){
        return this.mConnection;
    }



    public String getWizardCommand() {
        return this.mszWizardCommand;
    }

    public String getModelCommand() {
        return this.mszModelCommand;
    }

    public String getControlCommand() {
        return this.mszControlCommand;
    }



    @Override
    public void afterConnectionAccepted( Connectiom connectiom ) throws ServletException, IOException {
        connectiom.response.setCharacterEncoding( this.mArchHostSystem.getServerCharset() );
    }

    /** Http Method Handler **/
    @Override
    public void handleGet( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
        this.mConnection = new GetConnection( this, connectiom );
        this.invokeDispatchBus();
    }

    @Override
    public void handlePost( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
        this.mConnection = new PostConnection( this, connectiom );
        this.invokeDispatchBus();
    }

    @Override
    public void handleHead( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
    }

    @Override
    public void handleOptions( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
    }

    @Override
    public void handlePut( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
    }

    @Override
    public void handlePatch( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
    }

    @Override
    public void handleDelete( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
    }

    @Override
    public void handleTrace( Connectiom connectiom ) throws ServletException, IOException {
        this.afterConnectionAccepted(connectiom);
    }


    @Override
    public void stop() throws RuntimeException {
        throw new TerminateSessionException("This session or sequence has been terminated.");
    }

    public void jspRenderPage( String szDispatcherPath ) throws IOException, ServletException {
        this.mConnection.getRequest().getRequestDispatcher( szDispatcherPath ).forward(this.mConnection.getRequest(), this.mConnection.getResponse());
    }

    public void jspTPLRenderPage( String szTemplatePath ) throws IOException, ServletException {
        this.mConnection.getRequest().getRequestDispatcher( this.mArchHostSystem.getRealTemplatePath() + szTemplatePath ).forward(this.mConnection.getRequest(), this.mConnection.getResponse());
    }




    @Override
    public void traceSystemErrorMsg( String szTitle, String szErrorMsg ) throws IOException, ServletException {
        this.mConnection.getRequest().setAttribute("pineVersion", Pinecone.VERSION);
        this.mConnection.getRequest().setAttribute("pineReleaseDate", Pinecone.RELEASE_DATE);
        this.mConnection.getRequest().setAttribute("javaVersion", System.getProperty("java.version"));
        this.mConnection.getRequest().setAttribute("pageTitle", szTitle);
        this.mConnection.getRequest().setAttribute("errorMsg", szErrorMsg);

        this.jspRenderPage( this.mArchHostSystem.getDefaultErrorPagePath() );
    }

    @Override
    public void traceSystemErrorMsg( int nErrorID, String szTitle, String szErrorMsg ) throws IOException, ServletException {
        this.mConnection.getResponse().setStatus(nErrorID);
        this.traceSystemErrorMsg( szTitle,szErrorMsg );
    }

    @Override
    public void traceSystem404Error() throws IOException, ServletException {
        this.traceSystem404Error("<h2>You are trying to access an undefined file !</h2>" );
    }

    @Override
    public void traceSystem404Error( String szErrorMsg ) throws IOException, ServletException {
        this.traceSystemErrorMsg( 404,"SERVER 404 ERROR",szErrorMsg );
    }

    @Override
    public void traceSystem500Error( String szErrorMsg ) throws IOException, ServletException {
        this.traceSystemErrorMsg( 500,"SERVER 500 ERROR",szErrorMsg );
    }

    public void echoIndexPage() throws IOException, ServletException {
        this.traceSystemErrorMsg( "WELCOME TO PINECONE JAVA" ,"<h1>Everything should be fine.</h1>" );
    }




    protected void beforeDispath() throws ServletException, IOException {
    }

    protected void afterDispatch() throws ServletException, IOException {

    }

    @Override
    public void invokeDispatchBus() throws ServletException, IOException {
        this.requestReceived();
        this.dispatch();
    }

    @Override
    public void dispatch() throws IOException, ServletException {
        this.beforeDispath();
        this.profileURL();
        this.toSummon();
        this.afterDispatch();
    }

    @Override
    public void requestReceived() throws ServletException, IOException {
        try {
            this.mszWizardCommand = this.mConnection.$_GET().getString(this.mArchHostSystem.getWizardParameter());
        } catch (JSONException e){ this.mszWizardCommand = ""; }
        try {
            this.mszModelCommand = this.mConnection.$_GET().getString(this.mArchHostSystem.getModelParameter());
        } catch (JSONException e){ this.mszModelCommand = ""; }
        try {
            this.mszControlCommand = this.mConnection.$_GET().getString(this.mArchHostSystem.getControlParameter());
        }
        catch (JSONException e){ this.mszControlCommand = ""; }

        this.mWizardSummoner   = SystemSpawner.spawnWizardSummoner( this.mArchHostSystem.getWizardSummonerConfig(), this.mConnection );
    }

    protected void profileURL() throws ServletException {
        HttpServletRequest request = this.mConnection.getRequest();
        StringBuffer  sbRequestURL = request.getRequestURL();
        String        szRequestURI = request.getRequestURI();

        if( szRequestURI.equals( "/" ) ){
            if( sbRequestURL.charAt( sbRequestURL.length() - 1 ) == '/' ){
                this.mszDomainHref = sbRequestURL.deleteCharAt( sbRequestURL.length() - 1 ).toString();
            }
            else {
                this.mszDomainHref = sbRequestURL.toString();
            }
        }
        else {
            String            szRequestURL = sbRequestURL.toString();
            String[] debris = szRequestURL.split( szRequestURI );
            if( debris.length >= 1 ) {
                this.mszDomainHref = debris[0];
            }
            else {
                throw new ServletException( "Illegal URL given '" + szRequestURL + "'." );
            }
        }

        this.mszURI    = szRequestURI;
        this.mURIParts = StringUtils.trimEmptyElement( this.mszURI.split( "/" ) );
        //Debug.trace( this.mURIParts, this.mszURI );
    }

    protected void summonByQueryString() throws ServletException, IOException {
        switch ( this.mszWizardCommand ){
            case "":{
                this.echoIndexPage();
                break;
            }
            default:{
                this.mWizardSummoner.summonAndExecute( this.mszWizardCommand );
                break;
            }
        }
    }

    protected void summonByRouterPath() throws ServletException, IOException {
        Object routum = this.mArchHostSystem.getPrimeRouterDispatcher().queryRoutum( this.mszURI );
        if( routum != null ) {
            ArchRouterDispatcher.RouterClass routerClass = null;
            ArchRouterDispatcher.RouterMethod routerMethod = null;
            if( routum instanceof ArchRouterDispatcher.RouterClass ) {
                routerClass = (ArchRouterDispatcher.RouterClass) routum;
            }
            else if( routum instanceof ArchRouterDispatcher.RouterMethod ) {
                routerMethod = (ArchRouterDispatcher.RouterMethod) routum;
                routerClass  = routerMethod.parent;
            }

            if( routerClass != null ) {

                this.mszWizardCommand = routerClass.antetype.getSuperclass().getSimpleName();
                Wizard wizard = this.mWizardSummoner.summonIfExist( this.mszWizardCommand );
                if( routerMethod != null ) {
                    Pagesion pagesion = (Pagesion) wizard;
                    pagesion.setRenderum( routerMethod.antetype );
                    try{
                        routerMethod.antetype.invoke( pagesion );
                    }
                    catch ( IllegalAccessException | InvocationTargetException e ){
                        e.printStackTrace();
                    }

                    pagesion.render();
                }
            }

        }

        String szClass = "";
        if( this.mURIParts.length > 0 ) {
            szClass = this.mURIParts[0];
        }

        switch ( szClass ){
            case "":{
                this.echoIndexPage();
                break;
            }
            default:{
                this.mszWizardCommand = szClass;
                this.mWizardSummoner.summonAndExecute( szClass );
                break;
            }
        }
    }

    protected void toSummon() throws ServletException, IOException {
        switch ( this.mRouterType ) {
            case QueryString:{
                this.summonByQueryString();
                break;
            }
            case PathRouter:{
                this.summonByRouterPath();
                break;
            }
            default:{
                break;
            }
        }
    }


}
