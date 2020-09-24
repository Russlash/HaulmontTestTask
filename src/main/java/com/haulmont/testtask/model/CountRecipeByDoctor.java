package com.haulmont.testtask.model;

public class CountRecipeByDoctor {

    private String doctor;

    private Long count;

    public CountRecipeByDoctor(String doctor, Long count) {
        this.doctor = doctor;
        this.count = count;
    }

    public String getDoctor() {
        return doctor;
    }

    public Long getCount() {
        return count;
    }

}
