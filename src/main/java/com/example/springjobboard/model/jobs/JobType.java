package com.example.springjobboard.model.jobs;

import com.example.springjobboard.model.HasName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JobType implements HasName {

    PERMANENT("Permanent"),
    TEMPORARY("Temporary"),
    CONTRACT("Contract"),
    FULL_TIME("Full-time"),
    PART_TIME("Part-time");

    private final String name;

    public static JobType getByName(String name) {
        for (var value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
