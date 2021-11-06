package com.swl.models.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "organization")
@EqualsAndHashCode(exclude="organization")
public class Address {

    @Id
    @Column(name = "organization_id")
    private Integer id;

    @MapsId
    @JsonIgnore
    @JoinColumn(name = "organization_id")
    @OneToOne
    private Organization organization;

    private String street;

    private String number;

    private String complement;

    private String city;

    private String state;
}
