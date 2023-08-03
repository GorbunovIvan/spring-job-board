package com.example.springjobboard.model.jobs;

import com.example.springjobboard.model.HasCollections;
import com.example.springjobboard.model.HasId;
import com.example.springjobboard.model.HasName;
import com.example.springjobboard.model.users.Employer;
import com.example.springjobboard.model.users.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "vacancies")
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = { "title", "employer" })
@ToString
public class Vacancy implements HasId<Long>, HasCollections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotNull
    @Size(min = 3, max = 200, message = "Title should be in range from 3 to 200 characters")
    private String title = "";

    @Column(name = "description")
    @NotNull
    @Size(min = 3, max = 200, message = "Description should be in range from 3 to 200 characters")
    private String description = "";

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @ElementCollection(targetClass = JobType.class)
    @CollectionTable(name = "vacancies_types", joinColumns = @JoinColumn(name = "vacancy_id"))
    @Enumerated(EnumType.STRING)
    private Set<JobType> types = new HashSet<>();

    @ElementCollection(targetClass = WorkMode.class)
    @CollectionTable(name = "vacancies_modes", joinColumns = @JoinColumn(name = "vacancy_id"))
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

    @OneToMany(mappedBy = "vacancy", cascade = { CascadeType.ALL })
    @ToString.Exclude
    private Set<ResponseToVacancy> responses = new HashSet<>();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VacancyStatus status;

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = VacancyStatus.OPENED;
        }
    }

    public void addType(JobType type) {
        getTypes().add(type);
    }

    public void addMode(WorkMode mode) {
        getModes().add(mode);
    }

    public void addSkill(Skill skill) {
        getSkills().add(skill);
    }

    public void addCategory(JobCategory category) {
        getCategories().add(category);
    }

    public void addResponse(ResponseToVacancy response) {
        getResponses().add(response);
    }

    @Override
    public Map<String, Collection<? extends HasName>> getCollections() {

        var collections = new HashMap<String, Collection<? extends HasName>>();

        collections.put("types", getTypes());
        collections.put("modes", getModes());
        collections.put("categories", getCategories());
        collections.put("skills", getSkills());

        return collections;
    }
}
