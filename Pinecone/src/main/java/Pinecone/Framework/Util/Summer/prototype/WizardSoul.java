package Pinecone.Framework.Util.Summer.prototype;

import Pinecone.Framework.Util.JSON.JSONObject;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartFile;
import Pinecone.Framework.Util.Summer.NaughtyGenieInvokedException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public interface WizardSoul {
    Connectson getConnection();

    HostSystem getHostSystem();

    ControlDispatcher getDispatcher();

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

    String prototypeName();



    /** Summoner **/
    void stop() throws RuntimeException;

    boolean prospectIsDefaultEnchanter();

    void setEnchanterRole( boolean bRole );

    boolean isEnchanter();
}
