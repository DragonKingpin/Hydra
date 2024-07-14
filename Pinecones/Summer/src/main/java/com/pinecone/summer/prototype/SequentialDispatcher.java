package com.pinecone.summer.prototype;

import javax.servlet.ServletException;
import java.io.IOException;

public interface SequentialDispatcher {
    void dispatch() throws IOException, ServletException;

    void stop() throws RuntimeException;

}
