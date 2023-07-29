package com.example.springjobboard.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UtilForControllers {

    public Map<String, List<String>> bindingResultErrorsToMap(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
    }

}
