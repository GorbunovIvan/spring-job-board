package com.example.springjobboard.model.jobs;

import com.example.springjobboard.model.HasName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkMode implements HasName {

    IN_OFFICE("In-office"),
    REMOTE("Remote");

    private final String name;

    public static WorkMode getByName(String name) {
        for (var value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
