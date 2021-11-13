package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board")
@EqualsAndHashCode(exclude = "board")
public class Columns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String title;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Board board;

    @JsonIgnore
    @OneToMany(mappedBy = "column")
    private List<History> histories;

    @JsonIgnore
    @OneToMany(mappedBy = "column")
    private List<Task> tasks;

    @ManyToMany
    private List<Label> labels;

}
