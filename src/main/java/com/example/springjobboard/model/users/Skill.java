package com.example.springjobboard.model.users;

import com.example.springjobboard.model.EntityWithId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "skills")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "name")
@ToString
public class Skill implements EntityWithId<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    @NotNull
    @Size(min = 2, max = 99, message = "Name should be in range from 2 to 99 characters")
//    @UniqueElements
    private String name;

    public Skill(String name) {
        this.name = name;
    }
}
