package Pinecone.Framework.Util.Summer.prototype;

import Pinecone.Framework.Util.JSON.JSONObject;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public interface Connectson extends Connection {
    JSONObject $_GPC();

    JSONObject $_GET();

    JSONObject $_POST();

    default HttpServletRequest $_REQUEST(){
        return this.getRequest();
    }

    HttpServletRequest $_REQUEST ( boolean bUsingMultipart );

    default HttpServletResponse $_RESPONSE() {
        return this.getResponse();
    }

    Map<String, MultipartFile> $_FILES();

    Map<String, Cookie> $_COOKIE();
}