package com.example.springjobboard.model.jobs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VacancyStatus {

    OPENED("Opened"),
    HIDDEN("Hidden"),
    CLOSED("Closed");

    private final String name;
}
