package com.pinecone.summer.prototype;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Summer JSON Based Pagina(Page) Ion, for template stereotype.
 * Default as a controller. equal-> @Controller.
 */
public interface Pagesion extends Pageson, Wizard {
    void beforeDispatch() throws IOException, ServletException;

    void dispatch() throws IOException, ServletException ;

    void afterDispatch() throws IOException, ServletException;
}
