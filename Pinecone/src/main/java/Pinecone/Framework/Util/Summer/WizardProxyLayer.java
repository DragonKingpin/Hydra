package Pinecone.Framework.Util.Summer;

import Pinecone.Framework.Util.JSON.JSONObject;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartFile;
import Pinecone.Framework.Util.Summer.prototype.JSONBasedModel;
import Pinecone.Framework.Util.Summer.prototype.ModelEnchanter;
import Pinecone.Framework.Util.Summer.prototype.Wizard;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class WizardProxyLayer extends ArchWizard {
    protected JSONObject  mPageData                     =  null  ;
    protected boolean     mbGlobalEnchanter             =  false ;
    protected PrintWriter mConnectWriter                =  null;
    protected ServletOutputStream mConnectOut           =  null;

    public WizardProxyLayer ( ArchConnection session ) {
        super( session );
    }

    protected void init() {
        this.mPageData = new JSONObject();
        if( this instanceof JSONBasedModel){
            this.mbGlobalEnchanter = this.prospectIsDefaultEnchanter();
        }
        this.appendDefaultPageDate();
    }

    protected void appendDefaultPageDate(){
        this.mPageData.put( "PrototypeName", this.prototypeName() );
        this.mPageData.put( "szMainTitle", ((Wizard)this).getTitle() );
        this.mPageData.put( "szWizardRole", ((Wizard)this).getModularRole() );
    }

    public ArchHostSystem parent(){
        return this.mParentSystem;
    }

    public void forward ( WizardProxyLayer that ) {
        this.mPageData = that.mPageData;
    }


    public ArchControlDispatcher getSystemDispatcher() {
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
        return "?" + this.parent().getWizardParameter() + "=" + szPrototype;
    }

    public String spawnActionQuerySpell( String szActionFnName ){
        return this.spawnActionControlSpell( szActionFnName, null );
    }

    public String spawnControlQuerySpell( String szControlFnName ) {
        return this.spawnActionControlSpell( null, szControlFnName );
    }

    public String spawnActionControlSpell( String szActionFnName, String szControlFnName ) {
        String szQueryString = "?" + this.parent().getWizardParameter() + "=" + this.prototypeName();
        if( szActionFnName != null && !szActionFnName.isEmpty() ){
            szQueryString += "&" + this.parent().getModelParameter() + "=" + szActionFnName;
        }
        if( szControlFnName != null && !szControlFnName.isEmpty() ){
            szQueryString += "&" + this.parent().getControlParameter() + "=" + szControlFnName;
        }
        return szQueryString;
    }

    public Object summonNormalGenieByCallHisName(String szGenieName) throws NaughtyGenieInvokedException {
        throw new IllegalStateException("Notice: summonNormalGenieByCallHisName() is abstract.");
    }




    public boolean prospectIsDefaultEnchanter() {
        Annotation[] annotations = this.getClass().getAnnotations();
        for( Annotation annotation : annotations ){
            if( annotation instanceof ModelEnchanter ){
                return ((ModelEnchanter) annotation).value();
            }
        }
        return false;
    }

    public void setEnchanterRole( boolean bRole ){
        this.mbGlobalEnchanter = bRole;
    }

    public boolean isEnchanter() {
        return this.mbGlobalEnchanter;
    }

    public void render() throws ServletException, IOException {
        if( this instanceof JSONBasedModel && this.mbGlobalEnchanter ){
            this.writer().print( ((JSONBasedModel)this).toJSONString() );
        }
    }
}
