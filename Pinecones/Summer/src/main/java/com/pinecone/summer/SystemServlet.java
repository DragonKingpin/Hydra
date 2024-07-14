package com.pinecone.summer;

import com.pinecone.summer.prototype.Servletson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  Pinecone System Servlet
 *  System Primary Servlet for router(xxx:/host/), for map and dispatch all query-string based router.
 */
public class SystemServlet extends HttpServlet implements Servletson {
    private ArchHostSystem mSystem     = null             ;
    private String mszClassPath                           ;
    private String mszArchSystemClassName                 ;



    public String getClassPath() {
        return this.mszClassPath;
    }

    public String getServletMatrixConfig() {
        return this.mszArchSystemClassName;
    }

    public ArchHostSystem getHostSystem(){
        return this.mSystem;
    }




    @Override
    public void init() throws ServletException {
        this.mszClassPath = ArchHostSystem.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        this.mszArchSystemClassName = this.getServletContext().getInitParameter("HostSystem");

        this.mSystem = SystemSpawner.spawnSystem( this.mszArchSystemClassName, this );
        this.mSystem.init();
        ArchHostSystem.G_SystemServlet = this;
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        this.mSystem.handleByDispatcher().handleGet( new Connectiom( request, response, this ) );
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        this.mSystem.handleByDispatcher().handlePost( new Connectiom( request, response, this ) );
    }

}

