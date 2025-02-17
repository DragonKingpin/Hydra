package com.walnut.sparta.ucdn.console.umc.ufmc;

import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.ufmc.ExternalSessionValidator." )
public class UEFMSessionValidatorController {
    private Logger logger;

    public UEFMSessionValidatorController(){
        this.logger = LoggerFactory.getLogger( this.getClass() );
    }

    @AddressMapping("fileTransmitComplete")
    public void fileTransmitComplete(RequestHead head){
        log.info("分发完成 sessionId：" + head.getSessionId());
    }
}
