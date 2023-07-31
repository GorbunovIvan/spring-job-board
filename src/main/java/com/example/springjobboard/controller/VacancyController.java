package com.example.springjobboard.controller;

import com.example.springjobboard.model.jobs.ResponseToVacancy;
import com.example.springjobboard.model.jobs.Vacancy;
import com.example.springjobboard.repository.EmployerRepository;
import com.example.springjobboard.repository.VacancyRepository;
import com.example.springjobboard.utils.UsersUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyRepository vacancyRepository;
    private final EmployerRepository employerRepository;

    private final ControllersUtil controllersUtil;
    private final UsersUtil usersUtil;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("vacancies", vacancyRepository.findAll());
        return "vacancies/vacancies";
    }

    @GetMapping("/employer/{employerId}")
    public String getAllByEmployer(@PathVariable Long employerId, Model model) {
        var employer = employerRepository.findByIdEagerly(employerId, "vacancies");
        model.addAttribute("employer", employer);
        model.addAttribute("vacancies", employer.getVacancies());
        return "vacancies/vacancies";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {

        var vacancy = getVacancyByIdOrThrowException(id, true);
        var optionalProperties = controllersUtil.getAllOptionalPropertiesWithValuesForEntity(vacancy);

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("optionalProperties", optionalProperties);

        return "vacancies/vacancy";
    }

    @GetMapping("/new")
    public String initCreation(Model model) {

        var vacancy = new Vacancy();
        var optionalProperties = controllersUtil.getAllOptionalPropertiesForEntity(vacancy);

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("optionalProperties", optionalProperties);

        return "vacancies/createForm";
    }

    @PostMapping
    public String processCreation(Model model,
                                  @ModelAttribute @Valid Vacancy vacancy, BindingResult bindingResult) {

        var currentEmployer = usersUtil.getCurrentEmployer();

        if (currentEmployer == null) {
            throw new RuntimeException("You are not authorized as employer");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("vacancy", vacancy);
            model.addAttribute("optionalProperties", controllersUtil.getAllOptionalPropertiesWithValuesForEntity(vacancy));
            return "vacancies/createForm";
        }

        vacancy.setEmployer(currentEmployer);

        var vacancyPersisted = vacancyRepository.save(vacancy);

        return "redirect:/vacancies/" + vacancyPersisted.getId();
    }

    @GetMapping("/{id}/edit")
    public String initUpdate(@PathVariable Long id, Model model) {

        var vacancy = getVacancyByIdOrThrowException(id, true);
        var optionalProperties = controllersUtil.getAllOptionalPropertiesWithValuesForEntity(vacancy);

        model.addAttribute("vacancy", vacancy);
        model.addAttribute("optionalProperties", optionalProperties);

        return "vacancies/updateForm";
    }

    @PostMapping("/{id}")
    public String processUpdate(@PathVariable Long id, Model model,
                                @ModelAttribute @Valid Vacancy vacancy, BindingResult bindingResult) {

        var vacancyPersisted = getVacancyByIdOrThrowException(id, true);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", controllersUtil.bindingResultErrorsToMap(bindingResult));
            model.addAttribute("vacancy", vacancy);
            model.addAttribute("optionalProperties", controllersUtil.getAllOptionalPropertiesWithValuesForEntity(vacancy));
            return "vacancies/updateForm";
        }

        vacancyPersisted.setTitle(vacancy.getTitle());
        vacancyPersisted.setDescription(vacancy.getDescription());
        vacancyPersisted.setTypes(vacancy.getTypes());
        vacancyPersisted.setModes(vacancy.getModes());
        vacancyPersisted.setCategories(vacancy.getCategories());
        vacancyPersisted.setSkills(vacancy.getSkills());

        vacancyRepository.update(id, vacancyPersisted);

        return "redirect:/vacancies/" + id;
    }

    @PostMapping("/{id}/delete")
    public String processDelete(@PathVariable Long id) {
        var result = vacancyRepository.deleteById(id);
        if (!result) {
            throw new EntityNotFoundException(String.format("vacancy with id '%d' is not found", id));
        }
        return "redirect:/vacancies";
    }

    @GetMapping("/{id}/respond")
    public String respond(@PathVariable Long id) {

        var vacancy = getVacancyByIdOrThrowException(id, true);

        var currentApplicant = usersUtil.getCurrentApplicant();

        if (currentApplicant == null) {
            throw new RuntimeException("You are not authorized as applicant");
        }

        var response = new ResponseToVacancy();
        response.setVacancy(vacancy);
        response.setApplicant(currentApplicant);

        vacancy.addResponse(response);

        vacancyRepository.update(id, vacancy);

        return "redirect:/vacancies/" + id;
    }

    private Vacancy getVacancyByIdOrThrowException(Long id) {
        return getVacancyByIdOrThrowException(id, false);
    }

    private Vacancy getVacancyByIdOrThrowException(Long id, boolean eagerly) {
        Vacancy vacancy;
        if (eagerly) {
            vacancy = vacancyRepository.findByIdEagerly(id);
        } else {
            vacancy = vacancyRepository.findById(id);
        }
        if (vacancy == null) {
            throw new EntityNotFoundException(String.format("vacancy with id '%d' is not found", id));
        }
        return vacancy;
    }
}