package com.sauron.radium.heistron;

public abstract class HeistEntity implements Heistum {
    protected String            indexPath;       // 索引路径
    protected String            spoilPath;       // 数据文件存储路径

    protected String            workingPath;     // 程序工作路径
    protected long              taskFrom         = 0;      // Range min
    protected long              taskTo           = 100000; // Range max
    protected long              fragBase;
    protected long              fragRange;

    protected int               maximumThread    = 5;

    protected boolean           fromDeathPoint;  // 从上一个死亡点复活
    protected Metier            metier;          // 职业 (掠夺者、潜伏者、洗钱者)

    protected HeistEntity() {
    }

    @Override
    public int getMaximumThread() {
        return this.maximumThread;
    }

}
