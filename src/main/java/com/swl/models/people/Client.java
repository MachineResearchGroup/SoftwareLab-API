package com.swl.models.people;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JsonIgnore
    private User user;

    @NotNull
    private String corporateName;

    @ElementCollection
    @JoinTable(name = "client_segment")
    private List<String> segments;

    public Client(String corporateName, List<String> segments) {
        this.corporateName = corporateName;
        this.segments = segments;
    }

    public Client(String corporateName) {
        this.corporateName = corporateName;
    }

}
