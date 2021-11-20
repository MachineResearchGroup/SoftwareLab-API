package com.swl.models.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Project;
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

    @Lob
    private byte[] icon;

    @JsonIgnore
    @ManyToMany
    private List<Project> projects;

    @ManyToOne
    private Collaborator supervisor;
}
