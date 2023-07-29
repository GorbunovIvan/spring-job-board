package com.example.springjobboard.controller;

import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.Skill;
import com.example.springjobboard.repository.ApplicantRepository;
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
    private final UtilForControllers utilForControllers;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("applicants", applicantRepository.findAll());
        return "applicants/applicants";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("applicant", applicantRepository.findByIdEagerly(id, "skills"));
        return "applicants/applicant";
    }

    @GetMapping("/new")
    public String initCreation(Model model) {
        model.addAttribute("applicant", new Applicant());
        return "applicants/createForm";
    }

    @PostMapping
    public String processCreation(Model model, @ModelAttribute @Valid Applicant applicant,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", utilForControllers.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("applicant", applicant);
            return "applicants/createForm";
        }
        var applicantPersisted = applicantRepository.save(applicant);
        return "redirect:/applicants/" + applicantPersisted.getId();
    }

    @GetMapping("/{id}/edit")
    public String initUpdate(@PathVariable Long id, Model model) {
        var applicant = getApplicantByIdOrThrowException(id, true);
        model.addAttribute("applicant", applicant);
        return "applicants/updateForm";
    }

    @PostMapping("/{id}")
    public String processUpdate(@PathVariable Long id, Model model,
                                @ModelAttribute @Valid Applicant applicant, BindingResult bindingResult) {

        var applicantPersisted = getApplicantByIdOrThrowException(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", utilForControllers.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("applicant", applicant);
            return "applicants/updateForm";
        }

        applicantPersisted.setFirstName(applicant.getFirstName());
        applicantPersisted.setLastName(applicant.getLastName());
        applicantPersisted.setDescription(applicant.getDescription());

        applicantRepository.update(id, applicantPersisted);

        return "redirect:/applicants/" + id;
    }

    @PostMapping("/{id}/add-skill")
    public String processAddSkill(@PathVariable Long id, @RequestParam("skill") Skill skill) {
        var applicant = getApplicantByIdOrThrowException(id, true);
        applicant.addSkill(skill);
        applicantRepository.update(id, applicant);
        return "redirect:/applicants/" + id + "/edit";
    }

    @DeleteMapping("/{id}")
    public String processUpdate(@PathVariable Long id) {
        var result = applicantRepository.deleteById(id);
        if (!result) {
            throw new EntityNotFoundException(String.format("Applicant with id '%d' is not found", id));
        }
        return "redirect:/applicants/" + id;
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
