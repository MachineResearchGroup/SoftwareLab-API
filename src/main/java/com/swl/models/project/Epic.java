package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString(exclude = "project")
@EqualsAndHashCode(exclude="project")
public class Epic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private LocalDate initDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @JsonIgnore
    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "epic", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private List<Sprint> sprints;

}
