package com.example.springjobboard.model.jobs;

import com.example.springjobboard.model.EntityWithId;
import com.example.springjobboard.model.users.Applicant;
import com.example.springjobboard.model.users.Employer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "response-to-vacancies")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ToString
public class ResponseToVacancy implements EntityWithId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    @NotNull
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    @NotNull
    private Employer employer;
}
