package com.project.Kafka;

public class CategoryEvent {

    private int categoryId;
    private String categoryName;
    private String action;

    public CategoryEvent() {}

    public CategoryEvent(int categoryId, String categoryName, String action) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.action = action;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getAction() {
        return action;
    }
}
