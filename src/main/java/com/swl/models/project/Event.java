package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.user.Client;
import com.swl.models.user.Collaborator;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "project")
@EqualsAndHashCode(exclude = "project")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    private String name;

    @NotNull
    private LocalDateTime date;

    @Size(max = 500)
    private String description;

    private String local;

    @OneToMany
    private List<Document> documents;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Project project;

    @OneToMany
    @JsonIgnore
    private List<Collaborator> collaborators;

    @OneToMany
    @JsonIgnore
    private List<Client> clients;

}
