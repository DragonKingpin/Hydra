package com.walnut.sparta.ucdn.console.umc;

import com.walnut.sparta.ucdn.console.domain.service.WebSocketService;
import com.walnut.sparta.ucdn.console.infrastructure.TransactionManage;
import com.walnut.sparta.ucdn.console.umc.ufm.SessionPhaser;
import com.walnut.sparta.ucdn.console.umc.ufm.UFMSessionPhaser;

public class UMCMasterWarehouse implements MasterWarehouse{
    private SessionPhaser               sessionPhaser;

    private TransactionManage           transactionManage;

    private WebSocketService            webSocketService;

    public UMCMasterWarehouse(TransactionManage transactionManage,WebSocketService webSocketService){
        this.sessionPhaser = new UFMSessionPhaser();
        this.transactionManage = transactionManage;
        this.webSocketService = webSocketService;


    }


    @Override
    public SessionPhaser getSessionPhaser() {
        return this.sessionPhaser;
    }

    @Override
    public TransactionManage getTransactionManage() {
        return this.transactionManage;
    }

    @Override
    public WebSocketService getWebSocketService() {
        return this.webSocketService;
    }
}
