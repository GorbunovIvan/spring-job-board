package com.example.springjobboard.model.jobs;

import com.example.springjobboard.model.HasId;
import com.example.springjobboard.model.users.Applicant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "response-to-vacancies")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = { "vacancy", "applicant" })
@ToString
public class ResponseToVacancy implements HasId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    @NotNull
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @NotNull
    private Applicant applicant;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
