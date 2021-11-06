package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.user.Client;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "clients")
@EqualsAndHashCode(exclude="clients")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Size(max = 50)
    private String name;

    @Size(max = 500)
    private String description;

    private String repository;

    @JsonIgnore
    @ManyToMany
    private List<Client> clients;

    @JsonIgnore
    @OneToMany(mappedBy="project")
    private List<Epic> epics;

    @JsonIgnore
    @OneToMany(mappedBy="project")
    private List<Board> boards;

    @ManyToMany
    private List<Label> labels;

}
