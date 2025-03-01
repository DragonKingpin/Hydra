package com.walnut.sailor.stream.fm;

import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.ufmc.ExternalSessionValidator." )
public class UEFMSessionValidatorController {
    protected Logger logger;

    public UEFMSessionValidatorController(){
        this.logger = LoggerFactory.getLogger( this.getClass() );
    }

    @AddressMapping( "fileTransmitComplete" )
    public void fileTransmitComplete( RequestHead head ){
        this.logger.info( "分发完成 sessionId：" + head.getSessionId() );
    }
}
