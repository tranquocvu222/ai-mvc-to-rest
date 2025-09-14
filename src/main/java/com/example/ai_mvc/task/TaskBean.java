package com.example.ai_mvc.task;

import com.example.ai_mvc.common.ListControl;

import java.time.LocalDate;
import java.util.List;

public class TaskBean extends ListControl {
    private static final long serialVersionUID = 1L;
    private int selectedLink = 0;

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
    private List<Task> taskList;

    public int getSelectedLink() {
        return this.selectedLink;
    }

    public void setSelectedLink(int selectedLink) {
        this.selectedLink = selectedLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}