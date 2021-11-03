package com.swl.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String name;

    @JsonIgnore
    @ManyToMany
    private List<Project> projects;

    @JsonIgnore
    @ManyToOne
    private Collaborator supervisor;
}
