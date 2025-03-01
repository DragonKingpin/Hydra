package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@AddressMapping( "com.walnut.sailor.stream.fm.SessionValidator." )
public class SFMSessionValidatorController implements Pinenut {
    protected Logger logger;

    public SFMSessionValidatorController(){
        this.logger = LoggerFactory.getLogger( this.getClass() );
    }

    @AddressMapping( "fileTransmitComplete" )
    public void fileTransmitComplete( RequestHead head ){
        this.logger.info( "FileTransmitComplete sessionId：" + head.getSessionId() );
    }
}
