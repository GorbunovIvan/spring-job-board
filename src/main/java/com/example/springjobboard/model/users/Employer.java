package com.example.springjobboard.model.users;

import com.example.springjobboard.model.HasId;
import com.example.springjobboard.model.jobs.Vacancy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employers")
@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@EqualsAndHashCode(of = { "name" })
@ToString
public class Employer implements HasId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NotNull
    @Size(min = 2, max = 99, message = "Name should be in range from 2 to 99 characters")
    private String name = "";

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "employer")
    @ToString.Exclude
    private Set<Vacancy> vacancies = new HashSet<>();

    public void addVacancy(Vacancy vacancy) {
        getVacancies().add(vacancy);
    }
}
