package com.example.ai_mvc.task;

import java.time.LocalDate;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;

    // Default constructor
    public TaskDTO() {
    }

    // Constructor from Task entity
    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.dueDate = task.getDueDate();
        this.completed = task.isCompleted();
    }

    // Constructor from TaskBean (for service layer compatibility)
    public TaskDTO(TaskBean taskBean) {
        this.id = taskBean.getId();
        this.title = taskBean.getTitle();
        this.description = taskBean.getDescription();
        this.dueDate = taskBean.getDueDate();
        this.completed = taskBean.isCompleted();
    }

    // Getters and setters
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

    // Convert to TaskBean for service layer compatibility
    public TaskBean toTaskBean() {
        TaskBean bean = new TaskBean();
        bean.setId(this.id);
        bean.setTitle(this.title);
        bean.setDescription(this.description);
        bean.setDueDate(this.dueDate);
        bean.setCompleted(this.completed);
        return bean;
    }
}

