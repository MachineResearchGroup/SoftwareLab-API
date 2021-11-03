package com.swl.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;

    @NotNull
    private String register;

    @NotNull
    private String function;

    @JsonIgnore
    @ManyToOne
    private Collaborator supervisor;


    public Collaborator(String register, String function) {
        this.register = register;
        this.function = function;
    }

}
