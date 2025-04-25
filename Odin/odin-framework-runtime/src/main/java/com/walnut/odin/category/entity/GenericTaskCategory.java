package com.walnut.odin.category.entity;

public class GenericTaskCategory implements TaskCategory {
    protected String taskCategoryName;
    protected String taskCategoryNickName;
    protected String taskCategoryDescription;
    @Override
    public void setTaskCategoryName(String taskCategoryName) {
        this.taskCategoryName = taskCategoryName;
    }

    @Override
    public String getTaskCategoryName() {
        return this.taskCategoryName;
    }

    @Override
    public void setTaskCategoryNickName(String taskCategoryNick) {
         this.taskCategoryNickName = taskCategoryNick;
    }

    @Override
    public String getTaskCategoryNickName() {
        return this.taskCategoryNickName;
    }

    @Override
    public void setTaskCategoryDescription(String taskCategoryDescription) {
          this.taskCategoryDescription = taskCategoryDescription;
    }

    @Override
    public String getTaskCategoryDescription() {
        return this.taskCategoryDescription;
    }
}
