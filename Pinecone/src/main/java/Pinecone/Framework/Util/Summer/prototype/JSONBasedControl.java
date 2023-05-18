package Pinecone.Framework.Util.Summer.prototype;

import javax.servlet.ServletException;
import java.io.IOException;

public interface JSONBasedControl {
    void beforeDispatch() throws IOException, ServletException;

    void dispatch() throws IOException, ServletException ;

    void afterDispatch() throws IOException, ServletException;

    String getControlCommand();
}
