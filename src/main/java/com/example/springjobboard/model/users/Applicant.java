package com.example.springjobboard.model.users;

import com.example.springjobboard.model.HasCollections;
import com.example.springjobboard.model.HasId;
import com.example.springjobboard.model.HasName;
import com.example.springjobboard.model.jobs.ResponseToVacancy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "applicants")
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = { "user" })
@ToString
public class Applicant implements HasId<Long>, HasCollections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 99, message = "First name should be in range from 2 to 99 characters")
    private String firstName = "";

    @Column(name = "last_name")
    @NotNull
    @Size(min = 2, max = 99, message = "Last name should be in range from 2 to 99 characters")
    private String lastName = "";

    @Column(name = "description")
    @NotNull
    @Size(min = 10, max = 999, message = "Description should be in range from 10 to 999 characters")
    private String description = "";

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "skills_applicants",
            joinColumns = @JoinColumn(name = "applicant_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "applicant")
    @ToString.Exclude
    private Set<ResponseToVacancy> responses = new HashSet<>();

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public void addSkill(Skill skill) {
        getSkills().add(skill);
    }

    @Override
    public Map<String, Collection<? extends HasName>> getCollections() {
        var collections = new HashMap<String, Collection<? extends HasName>>();
        collections.put("skills", getSkills());
        return collections;
    }
}
