package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.people.Client;
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
@ToString(exclude = {"project", "client"})
@EqualsAndHashCode(exclude = {"project", "client"})
public class Redaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 2000)
    private String description;

    @NotNull
    private LocalDateTime date;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Project project;

    @NotNull
    @ManyToOne
    @JsonIgnore
    private Client client;

    @ManyToMany
    @JsonIgnore
    private List<Requirement> requirements;

}
