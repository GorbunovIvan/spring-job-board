package com.example.springjobboard.controller;

import com.example.springjobboard.controller.util.ControllersUtil;
import com.example.springjobboard.model.jobs.Vacancy;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.EmployerRepository;
import com.example.springjobboard.util.UsersUtil;
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

    private final UsersUtil usersUtil;
    private final ControllersUtil controllersUtil;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("isCurrentApplicant", employerRepository.findAll());
        return "employers/employers";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {

        var employer = getEmployerByIdOrThrowException(id, true);

        model.addAttribute("employer", employer);
        model.addAttribute("isCurrentEmployer", employer.equals(getCurrentEmployer()));

        return "employers/employer";
    }

    @GetMapping("/new")
    public String initCreation(Model model) {

        var currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        var currentEmployer = currentUser.getEmployer();
        if (currentEmployer != null) {
            return "redirect:/employers/" + currentEmployer.getId();
        }

        model.addAttribute("employer", new Employer());
        return "employers/createForm";
    }

    @PostMapping
    public String processCreation(Model model, @ModelAttribute @Valid Employer employer,
                                  BindingResult bindingResult) {

        var currentUser = getCurrentUser();

        if (currentUser.getEmployer() != null) {
            return "redirect:/employers/" + currentUser.getEmployer().getId();
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("employer", employer);
            return "employers/createForm";
        }

        employer.setUser(currentUser);

        var employerPersisted = employerRepository.save(employer);

        return "redirect:/employers/" + employerPersisted.getId();
    }

    @GetMapping("/{id}/edit")
    public String initUpdate(@PathVariable Long id, Model model) {

        var currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        var employer = getEmployerByIdOrThrowException(id, true);

        var currentEmployer = currentUser.getEmployer();
        if (!currentEmployer.getId().equals(id)) {
            throw new RuntimeException("You have no permissions to edit other's pages");
        }

        model.addAttribute("employer", employer);

        return "employers/updateForm";
    }

    @PostMapping("/{id}")
    public String processUpdate(@PathVariable Long id, Model model,
                                @ModelAttribute @Valid Employer employer, BindingResult bindingResult) {

        var employerPersisted = getEmployerByIdOrThrowException(id);

        var currentEmployer = getCurrentEmployer();
        if (!currentEmployer.getId().equals(id)) {
            throw new RuntimeException("You have no permissions to edit other's pages");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
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

        var currentEmployer = getCurrentEmployer();
        if (!currentEmployer.getId().equals(id)) {
            throw new RuntimeException("You have no permissions to edit other's pages");
        }

        employer.addVacancy(vacancy);
        employerRepository.update(id, employer);
        return "redirect:/employers/" + id + "/edit";
    }

    @PostMapping("/{id}/delete")
    public String processDelete(@PathVariable Long id) {

        var currentEmployer = getCurrentEmployer();
        if (!currentEmployer.getId().equals(id)) {
            throw new RuntimeException("You have no permissions to delete other's pages");
        }

        var result = employerRepository.deleteById(id);
        if (!result) {
            throw new EntityNotFoundException(String.format("employer with id '%d' is not found", id));
        }
        return "redirect:/employers";
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

    @ModelAttribute("currentUser")
    private User getCurrentUser() {
        return usersUtil.getCurrentUser();
    }

    @ModelAttribute("currentEmployer")
    private Employer getCurrentEmployer() {
        return usersUtil.getCurrentEmployer();
    }
}
