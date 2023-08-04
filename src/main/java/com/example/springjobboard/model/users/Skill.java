package com.example.springjobboard.model.users;

import com.example.springjobboard.model.HasId;
import com.example.springjobboard.model.HasName;
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
public class Skill implements HasId<Integer>, HasName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    @NotNull
    @Size(min = 2, max = 99, message = "Name should be in range from 2 to 99 characters")
    private String name;

    public Skill(String name) {
        this.name = HasName.getRemasteredName(name);
    }

    @PrePersist
    private void init() {
        setName(HasName.getRemasteredName(name));
    }
}
