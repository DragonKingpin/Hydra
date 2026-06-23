package com.walnut.odin.formation.source;

public class KernelMasterManipulator implements MasterManipulator {

    protected GroupManipulator     mGroupManipulator;
    protected GroupTaskManipulator mGroupTaskManipulator;
    protected RunManipulator       mRunManipulator;
    protected PageManipulator      mPageManipulator;
    protected FrameManipulator     mFrameManipulator;

    public KernelMasterManipulator(
            GroupManipulator groupManipulator,
            GroupTaskManipulator groupTaskManipulator,
            RunManipulator runManipulator,
            PageManipulator pageManipulator,
            FrameManipulator frameManipulator
    ) {
        this.mGroupManipulator = groupManipulator;
        this.mGroupTaskManipulator = groupTaskManipulator;
        this.mRunManipulator = runManipulator;
        this.mPageManipulator = pageManipulator;
        this.mFrameManipulator = frameManipulator;
    }

    @Override
    public GroupManipulator groupManipulator() {
        return this.mGroupManipulator;
    }

    @Override
    public GroupTaskManipulator groupTaskManipulator() {
        return this.mGroupTaskManipulator;
    }

    @Override
    public RunManipulator runManipulator() {
        return this.mRunManipulator;
    }

    @Override
    public PageManipulator pageManipulator() {
        return this.mPageManipulator;
    }

    @Override
    public FrameManipulator frameManipulator() {
        return this.mFrameManipulator;
    }
}
