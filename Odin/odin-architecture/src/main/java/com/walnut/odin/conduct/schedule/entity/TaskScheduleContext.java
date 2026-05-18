package com.walnut.odin.conduct.schedule.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.entity.TaskElement;

public class TaskScheduleContext implements Pinenut {

    protected TaskElement element;
    protected LocalDateTime targetTime;
    protected LocalDateTime nextScheduleTime;
    protected LocalDateTime thisScheduleTime;

    public TaskScheduleContext( TaskElement element, LocalDateTime targetTime ) {
        this.element = element;
        this.targetTime = targetTime;
    }

    public TaskElement getElement() {
        return this.element;
    }

    public void setElement( TaskElement element ) {
        this.element = element;
    }

    public LocalDateTime getTargetTime() {
        return this.targetTime;
    }

    public void setTargetTime( LocalDateTime targetTime ) {
        this.targetTime = targetTime;
    }

    public LocalDateTime getNextScheduleTime() {
        return this.nextScheduleTime;
    }

    public void setNextScheduleTime( LocalDateTime nextScheduleTime ) {
        this.nextScheduleTime = nextScheduleTime;
    }

    public LocalDateTime getThisScheduleTime() {
        return this.thisScheduleTime;
    }

    public void setThisScheduleTime( LocalDateTime thisScheduleTime ) {
        this.thisScheduleTime = thisScheduleTime;
    }
}
