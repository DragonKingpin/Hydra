package com.pinecone.summer.prototype;

import javax.servlet.ServletException;
import java.io.IOException;

public interface GenieBottle extends Wizardum, SequentialDispatcher {
    void dispatch() throws IOException, ServletException;

    void defaultGenie() throws Exception ;

    void beforeGenieInvoke() throws Exception ;

    void afterGenieInvoked() throws Exception ;
}
