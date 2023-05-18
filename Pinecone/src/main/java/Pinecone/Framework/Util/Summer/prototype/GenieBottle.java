package Pinecone.Framework.Util.Summer.prototype;

import javax.servlet.ServletException;
import java.io.IOException;

public interface GenieBottle extends WizardSoul, SequentialDispatcher {
    void dispatch() throws IOException, ServletException;

    void defaultGenie() throws Exception ;

    void beforeGenieInvoke() throws Exception ;

    void afterGenieInvoked() throws Exception ;
}
