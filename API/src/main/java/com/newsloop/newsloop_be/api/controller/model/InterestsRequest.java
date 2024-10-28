package com.newsloop.newsloop_be.api.controller.model;

import java.util.List;
import java.util.Map;

public class InterestsRequest {
    private String email;
    private String name;
    private Map<String, CategoryDetails> categories;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, CategoryDetails> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, CategoryDetails> categories) {
        this.categories = categories;
    }

    public static class CategoryDetails {
        private List<String> subcategories;
        private String country;

        // Getters and setters
        public List<String> getSubcategories() {
            return subcategories;
        }

        public void setSubcategories(List<String> subcategories) {
            this.subcategories = subcategories;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
