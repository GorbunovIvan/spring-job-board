package com.example.springjobboard.model.jobs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobType {

    PERMANENT("Permanent"),
    TEMPORARY("Temporary"),
    CONTRACT("Contract"),
    FULL_TIME("Full-time"),
    PART_TIME("Part-time");

    private final String name;
}
