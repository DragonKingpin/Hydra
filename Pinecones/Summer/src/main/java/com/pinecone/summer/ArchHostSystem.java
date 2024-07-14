package com.pinecone.summer;

import com.pinecone.framework.util.io.FileUtils;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.summer.http.CommonHttpEntityParser;
import com.pinecone.summer.http.HttpEntityParser;
import com.pinecone.summer.prototype.HostSystem;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArchHostSystem implements HostSystem {
    public static SystemServlet       G_SystemServlet;

    protected SystemSpawner           mSystemSpawner          = null;
    protected SystemServlet           mSystemServlet          = null;


    protected JSONObject              mGlobalConfig           = null;
    protected JSONObject              mSystemConfig           = null;
    protected JSONObject              mPublicWizardConfig     = null;
    protected ServletContext          mServletContext         = null   ;

    protected String                  mszModelParameter       = "action"  ;
    protected String                  mszControlParameter     = "control" ;
    protected String                  mszModelClassSuffix     = "Model"   ;
    protected String                  mszControlClassSuffix   = "Control" ;
    protected String                  mszWizardParameter      = "do"      ;
    protected String                  mszServerCharset        = "UTF-8"   ;

    protected String                  mszTemplatePath                  ;
    protected String                  mszRealTemplatePath              ;
    protected String                  mszConfigPath                    ;
    protected String                  mszSimpResourcesPath             ;
    protected String                  mszJavaWebInfoSuffix             ;
    protected String                  mszRootClassPath                 ;
    protected String                  mszSystemPath          = null    ;
    protected String                  mszResourcesPath       = null    ;

    protected HttpEntityParser        mHttpEntityParser = null ;
    protected ArchRouterDispatcher    mRouterDispatcher = null ;

    /** Class Function **/
    public String readFileContentAll( String szPath ) throws IOException {
        return FileUtils.readAll( szPath, Charset.forName( this.getServerCharset() ) );
    }

    private void parseConfig()  throws IOException {
        this.mGlobalConfig = new JSONMaptron( this.readFileContentAll( this.mszConfigPath )  );
    }

    private void construct() throws IOException {
        if( this.mServletContext != null ){
            this.mszServerCharset        = this.mServletContext.getInitParameter("encoding");
        }

        this.parseConfig();
        if( this.mGlobalConfig != null ){
            this.mSystemConfig           = this.mGlobalConfig.getJSONObject("SummerSystem");
            this.mszWizardParameter      = this.mSystemConfig.getString("WizardParameter");
            this.mszModelParameter       = this.mSystemConfig.getString("ModelParameter");
            this.mszControlParameter     = this.mSystemConfig.getString("ControlParameter");
            this.mPublicWizardConfig     = this.mSystemConfig.getJSONObject("PublicWizardConfig");
            this.mszTemplatePath         = this.mSystemConfig.getString("TemplatePath");
            this.mszSimpResourcesPath    = this.mSystemConfig.getString("ResourcesPath");
            this.mszJavaWebInfoSuffix    = this.mSystemConfig.getString("JavaWebInfoSuffix");
            this.mszRealTemplatePath     = this.mszJavaWebInfoSuffix + this.mszTemplatePath;
            this.mszModelClassSuffix     = this.mSystemConfig.getString("ModelClassSuffix");
            this.mszControlClassSuffix   = this.mSystemConfig.getString("ControlClassSuffix");
        }

        this.mSystemSpawner    = new SystemSpawner();

        if ( this.mSystemServlet != null ) {
            this.registerRootClassPath( this.mSystemServlet.getClassPath() );
        }

        this.mHttpEntityParser    = new CommonHttpEntityParser( this.getServerCharset() );
        this.mRouterDispatcher    = new ArchRouterDispatcher( this );
    }

    public ArchHostSystem( String szResourcesPath, String szConfigFileName ) throws IOException {
        this.mszResourcesPath = szResourcesPath;
        this.mszConfigPath    = szResourcesPath + szConfigFileName;
        this.construct();
    }

    public ArchHostSystem( SystemServlet servlet ) throws IOException {
        this.mSystemServlet  = servlet;
        this.mServletContext = this.mSystemServlet.getServletContext();
        this.mszConfigPath   = ArchHostSystem.getSystemConfigPath(
                this.mSystemServlet.getClassPath(), this.getServletContext().getInitParameter("IlluminationConfigLocation")
        );

        this.construct();
    }


    public HttpEntityParser getHttpEntityParser(){
        return this.mHttpEntityParser;
    }

    public JSONObject getGlobalConfig() {
        return this.mGlobalConfig;
    }

    public JSONObject getSystemConfig() {
        return this.mSystemConfig;
    }


    public JSONObject getPublicWizardConfig() {
        return this.mPublicWizardConfig;
    }

    public String getControlParameter() {
        return this.mszControlParameter;
    }

    public String getWizardParameter() {
        return this.mszWizardParameter;
    }

    public String getModelParameter() {
        return this.mszModelParameter;
    }

    public String getModelClassSuffix() { return this.mszModelClassSuffix; }

    public String getControlClassSuffix() { return this.mszControlClassSuffix; }

    public String getTemplatePath() {
        return this.mszTemplatePath;
    }

    public String getRealTemplatePath() {
        return this.mszRealTemplatePath;
    }

    public String getServerCharset() {
        return this.mszServerCharset;
    }

    public ServletContext getServletContext() {
        return this.mServletContext;
    }

    public String getDefaultErrorPagePath() {
        return this.mSystemConfig.getString("DefaultErrorPageTpl");
    }

    public JSONObject getHosts() {
        return this.mSystemConfig.getJSONObject("Hosts");
    }

    public String getResourcesPath() {
        if( this.mszResourcesPath == null ){
            this.mszResourcesPath = this.mSystemServlet.getClassPath() + this.mszSimpResourcesPath + "/";
        }
        return this.mszResourcesPath;
    }

    public void savageSetResourcesPath( String szUncheckedResourcesPath ){
        this.mszResourcesPath = szUncheckedResourcesPath;
    }


    /** Upload Function **/
    public JSONObject getUploadConfig() {
        return this.mSystemConfig.getJSONObject("UploadConfig");
    }

    public long getSingleFileSizeMax() {
        return this.getUploadConfig().getLong("SingleFileSizeMax");
    }

    public long getSumFileSizeMax() {
        return this.getUploadConfig().getLong("SumFileSizeMax");
    }

    public String getUploadEncode() {
        return this.getUploadConfig().getString("UploadEncode");
    }

    public String getUploadTempDir() {
        return this.getUploadConfig().getString("UploadTempDir");
    }




    /** System Class **/
    @Override
    public String getWizardSummonerConfig() { return this.getSystemConfig().getString("WizardSummoner"); }

    @Override
    public String getWizardPackageName(){
        return "Wizard";
    }

    public SystemSpawner getSystemSpawner() { return this.mSystemSpawner; }

    @Override
    public ArchConnectDispatcher handleByDispatcher(RouterType routerType ) {
        return new ArchConnectDispatcher( this, routerType );
    }

    @Override
    public ArchRouterDispatcher getPrimeRouterDispatcher() {
        return this.mRouterDispatcher;
    }


    public String getSystemPath() {
        if( this.mszRootClassPath != null && this.mszJavaWebInfoSuffix != null ){
            if( this.mszSystemPath == null ) {
                String[] szPathChip = this.mszRootClassPath.split( this.mszJavaWebInfoSuffix );
                if( szPathChip.length > 0 ){
                    this.mszSystemPath = szPathChip[0];
                }
            }
        }
        return this.mszSystemPath;
    }

    public String getRootClassPath() {
        return this.mszRootClassPath;
    }

    protected void registerRootClassPath( String szRootClassPath ) {
        this.mszRootClassPath = szRootClassPath;
    }

    public SystemServlet getSystemServlet(){
        return this.mSystemServlet;
    }

    public void init() throws ServletException {
        System.err.println( "----------------------------------------------" );
        System.err.println( "Bean Nuts Pinecone PineconeJava Summer Has Been Initiated" );
        System.err.println( "Time: " + ( new SimpleDateFormat("yyyy-MM-dd HH：mm：ss") ).format(new Date()) );
        System.err.println( "----------------------------------------------" );
    }


    protected static String getSystemConfigPath( String szClassPath , String szIlluminationConfigLocation ) {
        if( szIlluminationConfigLocation.startsWith("classpath:") ){
            szIlluminationConfigLocation = szIlluminationConfigLocation.replaceFirst( "classpath:", szClassPath );
        }
        return szIlluminationConfigLocation;
    }
}
