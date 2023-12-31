package com.example.springjobboard.controller;

import com.example.springjobboard.controller.util.ControllersUtil;
import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.ApplicantRepository;
import com.example.springjobboard.util.UsersUtil;
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
        model.addAttribute("isCurrentApplicant", applicant.equals(getCurrentApplicant()));
        model.addAttribute("optionalProperties", optionalProperties);

        return "applicants/applicant";
    }

    @GetMapping("/new")
    public String initCreation(Model model) {

        var currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        var currentApplicant = currentUser.getApplicant();
        if (currentApplicant != null) {
            return "redirect:/applicants/" + currentApplicant.getId();
        }

        var applicant = new Applicant();
        var optionalProperties = controllersUtil.getAllOptionalPropertiesForEntity(applicant);

        model.addAttribute("applicant", applicant);
        model.addAttribute("optionalProperties", optionalProperties);

        return "applicants/createForm";
    }

    @PostMapping
    public String processCreation(Model model, @ModelAttribute @Valid Applicant applicant,
                                  BindingResult bindingResult) {

        var currentUser = getCurrentUser();

        if (currentUser.getApplicant() != null) {
            return "redirect:/applicants/" + currentUser.getApplicant().getId();
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("applicant", applicant);
            model.addAttribute("optionalProperties", controllersUtil.getAllOptionalPropertiesWithValuesForEntity(applicant));
            return "applicants/createForm";
        }

        applicant.setUser(currentUser);

        var applicantPersisted = applicantRepository.save(applicant);

        return "redirect:/applicants/" + applicantPersisted.getId();
    }

    @GetMapping("/{id}/edit")
    public String initUpdate(@PathVariable Long id, Model model) {

        var currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        var applicant = getApplicantByIdOrThrowException(id, true);
        var optionalProperties = controllersUtil.getAllOptionalPropertiesWithValuesForEntity(applicant);

        var currentApplicant = currentUser.getApplicant();
        if (!currentApplicant.getId().equals(id)) {
            throw new RuntimeException("You have no permissions to edit other's pages");
        }

        model.addAttribute("applicant", applicant);
        model.addAttribute("optionalProperties", optionalProperties);

        return "applicants/updateForm";
    }

    @PostMapping("/{id}")
    public String processUpdate(@PathVariable Long id, Model model,
                                @ModelAttribute @Valid Applicant applicant, BindingResult bindingResult) {

        var applicantPersisted = getApplicantByIdOrThrowException(id, true);

        var currentApplicant = getCurrentApplicant();
        if (!currentApplicant.getId().equals(id)) {
            throw new RuntimeException("You have no permissions to edit other's pages");
        }

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

    @PostMapping("/{id}/delete")
    public String processDelete(@PathVariable Long id) {

        var currentApplicant = getCurrentApplicant();
        if (!currentApplicant.getId().equals(id)) {
            throw new RuntimeException("You have no permissions to delete other's pages");
        }

        var result = applicantRepository.deleteById(id);
        if (!result) {
            throw new EntityNotFoundException(String.format("Applicant with id '%d' is not found", id));
        }
        return "redirect:/users/my-page";
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

    @ModelAttribute("currentUser")
    private User getCurrentUser() {
        return usersUtil.getCurrentUser();
    }

    @ModelAttribute("currentApplicant")
    private Applicant getCurrentApplicant() {
        return usersUtil.getCurrentApplicant();
    }
}
