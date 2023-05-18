package Pinecone.Framework.Util.Summer.prototype;

import Pinecone.Framework.Util.Summer.Connectiom;

import javax.servlet.ServletException;
import java.io.IOException;

public interface ControlDispatcher extends SequentialDispatcher {
    void invokeDispatchBus() throws ServletException, IOException ;

    void requestReceived() throws ServletException, IOException ;


    void afterConnectionAccepted( Connectiom connectiom) throws ServletException, IOException;

    /** Http Method Handler **/
    void handleGet( Connectiom connectiom ) throws ServletException, IOException;

    void handlePost( Connectiom connectiom ) throws ServletException, IOException;

    void handleHead( Connectiom connectiom ) throws ServletException, IOException;

    void handleOptions( Connectiom connectiom ) throws ServletException, IOException;

    void handlePut( Connectiom connectiom ) throws ServletException, IOException;

    void handlePatch( Connectiom connectiom ) throws ServletException, IOException;

    void handleDelete( Connectiom connectiom ) throws ServletException, IOException;

    void handleTrace( Connectiom connectiom ) throws ServletException, IOException;


    /** Tracer **/
    void traceSystemErrorMsg( String szTitle, String szErrorMsg ) throws IOException, ServletException;

    void traceSystemErrorMsg( int nErrorID, String szTitle, String szErrorMsg ) throws IOException, ServletException;

    void traceSystem404Error() throws IOException, ServletException;

    void traceSystem404Error( String szErrorMsg ) throws IOException, ServletException;

    void traceSystem500Error( String szErrorMsg ) throws IOException, ServletException;

}
