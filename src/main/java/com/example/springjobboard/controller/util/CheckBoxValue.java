package com.example.springjobboard.controller.util;

import com.example.springjobboard.model.HasName;
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "object")
@ToString
public class CheckBoxValue<T extends HasName> {

    private T object;
    private boolean isChecked;

    public String getName() {
        return object.getName();
    }
}