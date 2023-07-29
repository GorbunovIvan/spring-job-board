package com.example.springjobboard.controller;

import com.example.springjobboard.model.jobs.Vacancy;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.repository.EmployerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerRepository employerRepository;
    private final UtilForControllers utilForControllers;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("employers", employerRepository.findAll());
        return "employers/employers";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("employer", employerRepository.findByIdEagerly(id, "vacancies"));
        return "employers/employer";
    }

    @GetMapping("/new")
    public String initCreation(Model model) {
        model.addAttribute("employer", new Employer());
        return "employers/createForm";
    }

    @PostMapping
    public String processCreation(Model model, @ModelAttribute @Valid Employer employer,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", utilForControllers.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("employer", employer);
            return "employers/createForm";
        }
        var employerPersisted = employerRepository.save(employer);
        return "redirect:/employers/" + employerPersisted.getId();
    }

    @GetMapping("/{id}/edit")
    public String initUpdate(@PathVariable Long id, Model model) {
        var employer = getEmployerByIdOrThrowException(id, true);
        model.addAttribute("employer", employer);
        return "employers/updateForm";
    }

    @PostMapping("/{id}")
    public String processUpdate(@PathVariable Long id, Model model,
                                @ModelAttribute @Valid Employer employer, BindingResult bindingResult) {

        var employerPersisted = getEmployerByIdOrThrowException(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", utilForControllers.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("employer", employer);
            return "employers/updateForm";
        }

        employerPersisted.setName(employer.getName());

        employerRepository.update(id, employerPersisted);

        return "redirect:/employers/" + id;
    }

    @PostMapping("/{id}/add-vacancy")
    public String processAddVacancy(@PathVariable Long id, @RequestParam("vacancy") Vacancy vacancy) {
        var employer = getEmployerByIdOrThrowException(id, true);
        employer.addVacancy(vacancy);
        employerRepository.update(id, employer);
        return "redirect:/employers/" + id + "/edit";
    }

    @DeleteMapping("/{id}")
    public String processUpdate(@PathVariable Long id) {
        var result = employerRepository.deleteById(id);
        if (!result) {
            throw new EntityNotFoundException(String.format("employer with id '%d' is not found", id));
        }
        return "redirect:/employers/" + id;
    }

    private Employer getEmployerByIdOrThrowException(Long id) {
        return getEmployerByIdOrThrowException(id, false);
    }

    private Employer getEmployerByIdOrThrowException(Long id, boolean eagerly) {
        Employer employer;
        if (eagerly) {
            employer = employerRepository.findByIdEagerly(id);
        } else {
            employer = employerRepository.findById(id);
        }
        if (employer == null) {
            throw new EntityNotFoundException(String.format("employer with id '%d' is not found", id));
        }
        return employer;
    }
}
