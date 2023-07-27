package com.example.springjobboard.model.jobs;

import com.example.springjobboard.model.EntityWithId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "job-categories")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ToString
public class JobCategory implements EntityWithId<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotNull
    @Size(min = 3, max = 99, message = "Name should be in range from 3 to 99 characters")
    private String name;
}
