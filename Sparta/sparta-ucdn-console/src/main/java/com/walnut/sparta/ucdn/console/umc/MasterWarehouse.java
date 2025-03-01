package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.sparta.ucdn.console.domain.service.WebSocketService;
import com.walnut.sparta.ucdn.console.infrastructure.TransactionManage;
import com.walnut.sparta.ucdn.console.umc.ufm.SessionPhaser;

public interface MasterWarehouse extends Pinenut {
    SessionPhaser               getSessionPhaser();

    TransactionManage           getTransactionManage();

    WebSocketService            getWebSocketService();
}
