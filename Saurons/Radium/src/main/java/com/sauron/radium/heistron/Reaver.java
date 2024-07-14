package com.sauron.radium.heistron;

/**
 *  Bean Nuts Hazelnut Sauron Radium For Java, Reaver [掠夺者]
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Focus on batch crawler downloading and retrieving.
 *  面向批处理化爬虫数据取回
 *  *****************************************************************************************
 *  Dragon King, the undefined
 */
public interface Reaver extends Crew {
    default void toRavage() {
        this.startBatchTask();
    }

    @Override
    default void isTimeToFeast(){
        this.toRavage();
    }
}