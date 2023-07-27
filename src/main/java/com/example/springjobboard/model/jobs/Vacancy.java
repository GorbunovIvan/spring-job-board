package com.example.springjobboard.model.jobs;

import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vacancies")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = { "title",  })
@ToString
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotNull
    @Size(min = 3, max = 200, message = "Title should be in range from 3 to 200 characters")
    private String title;

    @Column(name = "description")
    @NotNull
    @Size(min = 3, max = 200, message = "Description should be in range from 3 to 200 characters")
    private String description;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    @NotNull
    private Employer employer;

    @ElementCollection(targetClass = JobType.class)
    @CollectionTable(name = "job_types", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Enumerated(EnumType.STRING)
    private Set<JobType> types = new HashSet<>();

    @ElementCollection(targetClass = WorkMode.class)
    @CollectionTable(name = "work_modes", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Enumerated(EnumType.STRING)
    private Set<WorkMode> modes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "skills_vacancies",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "vacancies_categories",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "category")
    )
    @ToString.Exclude
    private Set<JobCategory> categories = new HashSet<>();

    @OneToMany(mappedBy = "vacancy")
    private Set<ResponseToVacancy> responses = new HashSet<>();

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
