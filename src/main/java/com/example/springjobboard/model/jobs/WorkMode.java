package com.example.springjobboard.model.jobs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkMode {

    IN_OFFICE("In office"),
    REMOTE("Remote");

    private final String name;
}
