package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.people.Collaborator;
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
@ToString(exclude = "column")
@EqualsAndHashCode(callSuper = true, exclude = "column")
public class History extends Item{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer weight;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Columns column;

    @OneToMany(mappedBy = "history")
    private List<Task> tasks;

    @ManyToMany
    private List<Label> labels;

    @ManyToMany
    private List<Collaborator> collaborators;

}
