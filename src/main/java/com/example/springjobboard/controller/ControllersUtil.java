package com.example.springjobboard.controller;

import com.example.springjobboard.model.HasCollections;
import com.example.springjobboard.model.HasName;
import com.example.springjobboard.model.jobs.JobType;
import com.example.springjobboard.model.jobs.WorkMode;
import com.example.springjobboard.repository.JobCategoryRepository;
import com.example.springjobboard.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ControllersUtil {

    private final SkillRepository skillRepository;
    private final JobCategoryRepository jobCategoryRepository;

    public Map<String, List<String>> bindingResultErrorsToMap(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
    }

    public Map<String, Collection<CheckBoxValue<? extends HasName>>> getAllOptionalPropertiesWithValuesForEntity(HasCollections entity) {

        var properties = getAllOptionalPropertiesForEntity(entity);

        for (var property : properties.entrySet()) {
            for (var collectionEntry : entity.getCollections().entrySet()) {
                if (property.getKey().equals(collectionEntry.getKey())) {
                    setCheckedValuesToPropertiesOfApplicant(collectionEntry.getValue(), property.getValue());
                }
            }
        }

        return properties;
    }

    public Map<String, Collection<CheckBoxValue<? extends HasName>>> getAllOptionalPropertiesForEntity(HasCollections entity) {

        var properties = new HashMap<String, Collection<CheckBoxValue<? extends HasName>>>();

        for (var collectionEntry : entity.getCollections().entrySet()) {

            var collectionName = collectionEntry.getKey();
            Collection<? extends HasName> collection = new ArrayList<>();

            if (collectionName.equals("skills")) {
                collection = skillRepository.findAll();
            } else if (collectionName.equals("types")) {
                collection = Arrays.asList(JobType.values());
            } else if (collectionName.equals("modes")) {
                collection = Arrays.asList(WorkMode.values());
            } else if (collectionName.equals("categories")) {
                collection = jobCategoryRepository.findAll();
            }

            properties.put(collectionName, collectionToCollectionOfCheckboxes(collection));
        }

        return properties;
    }

    private Collection<CheckBoxValue<? extends HasName>> collectionToCollectionOfCheckboxes(Collection<? extends HasName> collection) {
        return collection.stream()
                .map(el -> new CheckBoxValue<HasName>(el, false))
                .collect(Collectors.toList());
    }

    private void setCheckedValuesToPropertiesOfApplicant(Collection<?> collection,
                                                         Collection<CheckBoxValue<? extends HasName>> property) {
        if (!collection.isEmpty()) {
            for (var el : property) {
                for (var type : collection) {
                    if (type.equals(el.getObject())) {
                        el.setChecked(true);
                        break;
                    }
                }
            }
        }
    }
}
