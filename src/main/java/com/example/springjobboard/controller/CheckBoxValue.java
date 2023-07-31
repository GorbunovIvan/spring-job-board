package com.example.springjobboard.controller;

import com.example.springjobboard.model.HasName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter @Setter
@ToString
class CheckBoxValue<T extends HasName> {

    private T object;
    private boolean isChecked;

    public String getName() {
        return object.getName();
    }
}