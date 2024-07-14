package com.sauron.radium.heistron;

/**
 *  Bean Nuts Hazelnut Sauron Radium For Java, Embezzler [洗钱者]
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Focus on batch crawler data processing
 *  面向批处理爬虫数据处理
 *  *****************************************************************************************
 *  Dragon King, the undefined
 */
public interface Embezzler extends Crew {
    void toEmbezzle();

    @Override
    default void isTimeToFeast(){
        this.toEmbezzle();
    }
}
