package com.pinecone.summer;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.summer.multiparts.MultipartFile;
import com.pinecone.summer.prototype.Wizardum;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public abstract class ArchWizardum extends ArchWizard implements Wizardum {
    protected PrintWriter mConnectWriter                =  null;
    protected ServletOutputStream mConnectOut           =  null;

    public ArchWizardum( ArchConnection session ) {
        super( session );
    }

    public ArchHostSystem system(){
        return this.mParentSystem;
    }

    public ArchConnectDispatcher getSystemDispatcher() {
        return this.mDispatcher;
    }

    public JSONObject $_GPC(){
        return this.getConnection().$_GPC();
    }

    public JSONObject $_GET(){
        return this.getConnection().$_GET();
    }

    public JSONObject $_POST(){
        return this.getConnection().$_POST();
    }

    @Override
    public PrintWriter writer() {
        try{
            if( this.mConnectWriter == null ) {
                this.mConnectWriter = this.getConnection().writer();
            }
            return this.mConnectWriter;
        }
        catch ( IOException e ) {
            throw new IllegalStateException( "Illegal invoke writer.", e );
        }
    }

    @Override
    public ServletOutputStream out() {
        try{
            if( this.mConnectOut == null ) {
                this.mConnectOut = this.getConnection().out();
            }
            return this.mConnectOut;
        }
        catch ( IOException e ) {
            throw new IllegalStateException( "Illegal invoke out.", e );
        }
    }

    public HttpServletRequest $_REQUEST(){
        return this.getConnection().$_REQUEST();
    }

    public HttpServletRequest getCurrentMultipartRequest() {
        return this.getConnection().getMultipartRequest();
    }

    public HttpServletResponse $_RESPONSE(){
        return this.getConnection().$_RESPONSE();
    }

    @Override
    public Map<String, Cookie> $_COOKIE() {
        return this.getConnection().$_COOKIE();
    }

    @Override
    public Map<String, MultipartFile> $_FILES() {
        throw new IllegalStateException("Notice: $_FILES() is abstract.");
    }




    public void beforeDispatch() throws IOException, ServletException {}

    public void afterDispatch() throws IOException, ServletException {}

    public void stop() throws RuntimeException {
        this.mDispatcher.stop();
    }


    public String getWizardCommand() {
        return this.mDispatcher.getWizardCommand();
    }

    public String getModelCommand() {
        return this.mDispatcher.getModelCommand();
    }

    public String getControlCommand() {
        return this.mDispatcher.getControlCommand();
    }

    public void redirect( String szURL ) throws IOException {
        this.$_RESPONSE().sendRedirect( szURL );
    }



    public String spawnWizardQuerySpell( String szPrototype ){
        return "?" + this.system().getWizardParameter() + "=" + szPrototype;
    }

    public String spawnActionQuerySpell( String szActionFnName ){
        return this.spawnActionControlSpell( szActionFnName, null );
    }

    public String spawnControlQuerySpell( String szControlFnName ) {
        return this.spawnActionControlSpell( null, szControlFnName );
    }

    public String spawnActionControlSpell( String szActionFnName, String szControlFnName ) {
        String szQueryString = "?" + this.system().getWizardParameter() + "=" + this.prototypeName();
        if( szActionFnName != null && !szActionFnName.isEmpty() ){
            szQueryString += "&" + this.system().getModelParameter() + "=" + szActionFnName;
        }
        if( szControlFnName != null && !szControlFnName.isEmpty() ){
            szQueryString += "&" + this.system().getControlParameter() + "=" + szControlFnName;
        }
        return szQueryString;
    }

    public Object summonNormalGenieByCallHisName(String szGenieName) throws NaughtyGenieInvokedException {
        throw new IllegalStateException("Notice: summonNormalGenieByCallHisName() is abstract.");
    }
}
