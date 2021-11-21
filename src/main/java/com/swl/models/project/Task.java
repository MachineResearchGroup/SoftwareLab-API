package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.people.Collaborator;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"column", "history"})
@EqualsAndHashCode(callSuper = true, exclude = {"column", "history"})
public class Task extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer estimate;

    private Integer duration;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Columns column;

    @JsonIgnore
    @ManyToOne
    private History history;

    @ManyToOne
    private Task superTask;

    @ManyToMany
    private List<Label> labels;

    @ManyToMany
    private List<Collaborator> collaborators;

}
