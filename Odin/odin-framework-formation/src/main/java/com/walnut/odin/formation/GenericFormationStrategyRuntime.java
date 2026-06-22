package com.walnut.odin.formation;

public class GenericFormationStrategyRuntime implements FormationStrategyRuntime {
    protected long    mnPageSize;
    protected long    mnFrameSize;
    protected long    mnWindowSize;
    protected long    mnInflightLimit;
    protected long    mnInflightCount;
    protected long    mnProductsSum;
    protected long    mnProducedCount;
    protected long    mnConsumedCount;
    protected boolean mbFinished;

    @Override
    public long pageSize() {
        return this.mnPageSize;
    }

    public void setPageSize( long pageSize ) {
        this.mnPageSize = pageSize;
    }

    @Override
    public long frameSize() {
        return this.mnFrameSize;
    }

    public void setFrameSize( long frameSize ) {
        this.mnFrameSize = frameSize;
    }

    @Override
    public long windowSize() {
        return this.mnWindowSize;
    }

    public void setWindowSize( long windowSize ) {
        this.mnWindowSize = windowSize;
    }

    @Override
    public long inflightLimit() {
        return this.mnInflightLimit;
    }

    public void setInflightLimit( long inflightLimit ) {
        this.mnInflightLimit = inflightLimit;
    }

    @Override
    public long inflightCount() {
        return this.mnInflightCount;
    }

    public void setInflightCount( long inflightCount ) {
        this.mnInflightCount = inflightCount;
    }

    @Override
    public long productsSum() {
        return this.mnProductsSum;
    }

    public void setProductsSum( long productsSum ) {
        this.mnProductsSum = productsSum;
    }

    @Override
    public long producedCount() {
        return this.mnProducedCount;
    }

    public void increaseProducedCount() {
        ++this.mnProducedCount;
    }

    @Override
    public long consumedCount() {
        return this.mnConsumedCount;
    }

    public void increaseConsumedCount() {
        ++this.mnConsumedCount;
    }

    @Override
    public boolean isFinished() {
        return this.mbFinished;
    }

    public void setFinished( boolean finished ) {
        this.mbFinished = finished;
    }
}
