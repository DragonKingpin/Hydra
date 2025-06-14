package com.walnut.odin.conduct;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.orchestration.Exertium;
import com.pinecone.hydra.task.kom.entity.TaskElement;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class TaskExertium extends Exertium {
    private Deque<TaskElement>  mDeque;

    private ExecuteCallBack     mExecuteCallBack;

    private int                 mRemainingNums;

    public TaskExertium( ExecuteCallBack callBack ) {
        this.mExecuteCallBack = callBack;
        this.mRemainingNums = 0;
        this.mDeque = new ArrayDeque<>();
    }

    @Override
    protected void doStart() {
        boolean flag = true;
        while( flag ) {
           while( !this.mDeque.isEmpty() ) {
               TaskElement node = this.mDeque.pop();
               Debug.trace( "执行节点" + node.getId() );
               this.mRemainingNums--;
           }

            if( this.mRemainingNums == 0 ) {
                List<TaskElement> taskElements = this.mExecuteCallBack.introduceTask();
                this.mRemainingNums = taskElements.size();
                this.mDeque.addAll( taskElements );
            }
            if( this.mRemainingNums == 0 ) {
                flag = false;
                this.mExecuteCallBack.nextTask();
            }
        }
    }
}
