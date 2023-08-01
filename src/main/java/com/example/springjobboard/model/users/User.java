package com.example.springjobboard.model.users;

import com.example.springjobboard.model.HasId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = { "email" })
@ToString
public class User implements HasId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    @NotNull(message = "Email should not be empty")
    @NotEmpty(message = "Email should not be empty")
//    @UniqueElements
    @Email(message = "Email has invalid form")
    private String email;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Employer employer;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Applicant applicant;

    @PrePersist
    private void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
