package com.study.model;

public enum Category {
    JAVA, JAVASCRIPT, SPRING, SPRING_BOOT;

    public static boolean isInvalidCategory(Category category) {
        return category == null || category.equals("");
    }
}
