package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "epic")
@EqualsAndHashCode(exclude="epic")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    private String name;

    @JsonIgnore
    @ManyToOne
    private Epic epic;

    @Size(max = 500)
    private String description;

    @NotNull
    private LocalDate initDate;

    @NotNull
    private LocalDate endDate;

}
