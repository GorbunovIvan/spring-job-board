package com.example.springjobboard.controller;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.repository.ApplicantRepository;
import com.example.springjobboard.utils.UsersUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/applicants")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantRepository applicantRepository;

    private final ControllersUtil controllersUtil;
    private final UsersUtil usersUtil;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("applicants", applicantRepository.findAll());
        return "applicants/applicants";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {

        var applicant = getApplicantByIdOrThrowException(id, true);
        var optionalProperties = controllersUtil.getAllOptionalPropertiesWithValuesForEntity(applicant);

        model.addAttribute("applicant", applicant);
        model.addAttribute("optionalProperties", optionalProperties);

        return "applicants/applicant";
    }

    @GetMapping("/new")
    public String initCreation(Model model) {

        var applicant = new Applicant();
        var optionalProperties = controllersUtil.getAllOptionalPropertiesForEntity(applicant);

        model.addAttribute("applicant", applicant);
        model.addAttribute("optionalProperties", optionalProperties);

        return "applicants/createForm";
    }

    @PostMapping
    public String processCreation(Model model, @ModelAttribute @Valid Applicant applicant,
                                  BindingResult bindingResult) {

        var currentUser = usersUtil.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("You are not authorized");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("applicant", applicant);
            model.addAttribute("optionalProperties", controllersUtil.getAllOptionalPropertiesWithValuesForEntity(applicant));
            return "applicants/createForm";
        }

        var applicantPersisted = applicantRepository.save(applicant);
        return "redirect:/applicants/" + applicantPersisted.getId();
    }

    @GetMapping("/{id}/edit")
    public String initUpdate(@PathVariable Long id, Model model) {

        var applicant = getApplicantByIdOrThrowException(id, true);
        var optionalProperties = controllersUtil.getAllOptionalPropertiesWithValuesForEntity(applicant);

        model.addAttribute("applicant", applicant);
        model.addAttribute("optionalProperties", optionalProperties);

        return "applicants/updateForm";
    }

    @PostMapping("/{id}")
    public String processUpdate(@PathVariable Long id, Model model,
                                @ModelAttribute @Valid Applicant applicant, BindingResult bindingResult) {

        var applicantPersisted = getApplicantByIdOrThrowException(id, true);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("applicant", applicant);
            model.addAttribute("optionalProperties", controllersUtil.getAllOptionalPropertiesWithValuesForEntity(applicant));
            return "applicants/updateForm";
        }

        applicantPersisted.setFirstName(applicant.getFirstName());
        applicantPersisted.setLastName(applicant.getLastName());
        applicantPersisted.setDescription(applicant.getDescription());
        applicantPersisted.setSkills(applicant.getSkills());
        applicantPersisted.setResponses(applicant.getResponses());

        applicantRepository.update(id, applicantPersisted);

        return "redirect:/applicants/" + id;
    }

    @DeleteMapping("/{id}")
    public String processDelete(@PathVariable Long id) {
        var result = applicantRepository.deleteById(id);
        if (!result) {
            throw new EntityNotFoundException(String.format("Applicant with id '%d' is not found", id));
        }
        return "redirect:/applicants";
    }

    private Applicant getApplicantByIdOrThrowException(Long id) {
        return getApplicantByIdOrThrowException(id, false);
    }

    private Applicant getApplicantByIdOrThrowException(Long id, boolean eagerly) {
        Applicant applicant;
        if (eagerly) {
            applicant = applicantRepository.findByIdEagerly(id);
        } else {
            applicant = applicantRepository.findById(id);
        }
        if (applicant == null) {
            throw new EntityNotFoundException(String.format("Applicant with id '%d' is not found", id));
        }
        return applicant;
    }
}
