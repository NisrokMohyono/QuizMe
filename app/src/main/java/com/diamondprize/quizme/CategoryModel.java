package com.diamondprize.quizme;

public class CategoryModel {
    private String categoryId, categoryName, categoryImage , categoryUrl;

    public CategoryModel(String categoryId, String categoryName, String categoryImage, String categoryUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryUrl = categoryUrl;
    }

    public CategoryModel() {}

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryImage = categoryUrl;
    }
}
