package com.finepointmobile.myapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by danielmalone on 10/27/17.
 */

@Entity
public class Task{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "short_text")
    String shortText;

    @ColumnInfo(name = "long_text")
    String longText;

    @ColumnInfo(name = "check_text")
    String checkText;

    @ColumnInfo(name = "task_id")
    String taskId;

    public Task(String shortText, String longText, String checkText, String taskId) {
        this.shortText = shortText;
        this.longText = longText;
        this.checkText = checkText;
        this.taskId = taskId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public String getCheckText() {
        return checkText;
    }

    public void setCheckText(String checkText) {
        this.checkText = checkText;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
