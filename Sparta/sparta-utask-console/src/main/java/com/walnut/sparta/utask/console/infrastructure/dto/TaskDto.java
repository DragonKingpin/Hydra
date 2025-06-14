package com.walnut.sparta.utask.console.infrastructure.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

import java.util.HashMap;
import java.util.Map;

public class TaskDto implements Pinenut {
    protected String name;

    protected String taskType;

    protected String imagePath;

    protected String resourceType;

    protected String deploymentMethod;

    protected short priority;

    protected short actuallyPriority;

    protected boolean dryRun;

    protected KernelTaskScheduleCycle kernelScheduleCycle;

    protected KernelTaskScheduleType kernelScheduleType;

    protected boolean enable;

    public TaskDto() {
    }

    public TaskDto(String name, String taskType, String imagePath, String resourceType,
                   String deploymentMethod, short priority, short actuallyPriority,
                   boolean dryRun, KernelTaskScheduleCycle kernelScheduleCycle,
                   KernelTaskScheduleType kernelScheduleType, boolean enable) {
        this.name = name;
        this.taskType = taskType;
        this.imagePath = imagePath;
        this.resourceType = resourceType;
        this.deploymentMethod = deploymentMethod;
        this.priority = priority;
        this.actuallyPriority = actuallyPriority;
        this.dryRun = dryRun;
        this.kernelScheduleCycle = kernelScheduleCycle;
        this.kernelScheduleType = kernelScheduleType;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDeploymentMethod() {
        return deploymentMethod;
    }

    public void setDeploymentMethod(String deploymentMethod) {
        this.deploymentMethod = deploymentMethod;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public short getActuallyPriority() {
        return actuallyPriority;
    }

    public void setActuallyPriority(short actuallyPriority) {
        this.actuallyPriority = actuallyPriority;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public KernelTaskScheduleCycle getKernelScheduleCycle() {
        return kernelScheduleCycle;
    }

    public void setKernelScheduleCycle(KernelTaskScheduleCycle kernelScheduleCycle) {
        this.kernelScheduleCycle = kernelScheduleCycle;
    }

    public KernelTaskScheduleType getKernelScheduleType() {
        return kernelScheduleType;
    }

    public void setKernelScheduleType(KernelTaskScheduleType kernelScheduleType) {
        this.kernelScheduleType = kernelScheduleType;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public static TaskDto from(GenericTaskElement element) {
        TaskDto dto = new TaskDto();
        dto.setName(element.getName());
        dto.setTaskType(element.getType());
        dto.setImagePath(element.getImagePath());
        dto.setResourceType(element.getResourceType());
        dto.setDeploymentMethod(element.getDeploymentMethod());
        dto.setPriority(element.getPriority());
        dto.setActuallyPriority(element.getActuallyPriority());
        dto.setDryRun(element.isDryRun());
        dto.setKernelScheduleCycle(element.getScheduleCycle());
        dto.setKernelScheduleType(element.getScheduleType());
        dto.setEnable(element.isEnable());
        return dto;
    }

    public GenericTaskElement toEntity(TaskInstrument taskInstrument) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name); // 添加名称
        map.put("type", taskType);
        map.put("imagePath", imagePath);
        map.put("resourceType", resourceType);
        map.put("deploymentMethod", deploymentMethod);
        map.put("priority", priority);
        map.put("actuallyPriority", actuallyPriority);
        map.put("dryRun", dryRun);
        map.put("kernelScheduleCycle", kernelScheduleCycle);
        map.put("kernelScheduleType", kernelScheduleType);
        map.put("enable", enable);
        return new GenericTaskElement(map, taskInstrument);
    }

    public GenericTaskElement toEntity() {
        return toEntity(null);
    }
}