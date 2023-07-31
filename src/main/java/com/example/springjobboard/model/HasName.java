package com.example.springjobboard.model;

public interface HasName {

    String getName();

    static String getRemasteredName(String name) {
        return name.replaceAll(" ", "-");
    }
}
