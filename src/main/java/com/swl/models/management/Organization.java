package com.swl.models.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.people.Collaborator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "cnpj")})
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 18)
    @CNPJ(message = "Invalid cpnj format")
    private String cnpj;

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address address;

    @OneToOne
    private Collaborator supervisor;

}
