package com.sauron.radium.heistron;

/**
 *  Bean Nuts Hazelnut Sauron Radium For Java, Stalker [潜伏者]
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Focus on batch crawler indexes sniffing.
 *  面向批量爬虫索引嗅探
 *  *****************************************************************************************
 *  Dragon King, the undefined
 */
public interface Stalker extends Crew {
    void toStalk();

    @Override
    default void isTimeToFeast(){
        this.toStalk();
    }
}
