package com.swl.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "cnpj")
})
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @Size(max = 14)
    private String cnpj;

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address address;

    @JsonIgnore
    @OneToOne
    private Collaborator supervisor;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "cliente_organizacao",
            joinColumns = @JoinColumn(name = "organizacao_id"),
            inverseJoinColumns = @JoinColumn(name = "cliente_id"))
    private List<Client> client;

}
