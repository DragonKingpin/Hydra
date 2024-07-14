package com.pinecone.summer.prototype;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.summer.multiparts.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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