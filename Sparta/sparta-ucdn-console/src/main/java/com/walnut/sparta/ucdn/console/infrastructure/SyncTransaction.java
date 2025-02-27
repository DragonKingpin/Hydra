package com.walnut.sparta.ucdn.console.infrastructure;

import java.util.concurrent.atomic.AtomicInteger;

public interface SyncTransaction {
    int getNodeNum();

    void setNodeNum( int nodeNum );

    int checkRemainingNum();

    int decreaseRemainingNum();
}
