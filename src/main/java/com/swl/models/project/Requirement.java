package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.user.Client;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "projects")
@EqualsAndHashCode(exclude = "projects")
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 2000)
    private String description;

    private String category;

    private String subcategory;

    @NotNull
    @JsonIgnore
    @ManyToMany
    private List<Project> projects;

}
