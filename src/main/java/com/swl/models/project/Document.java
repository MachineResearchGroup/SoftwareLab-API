package com.swl.models.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.user.Collaborator;
import lombok.*;

import javax.persistence.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"project", "collaborator"})
@EqualsAndHashCode(exclude = {"project", "collaborator"})
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "url")})
public class Document {

    @Id
    private String url;

    @ManyToOne
    @JsonIgnore
    private Project project;

    @ManyToOne
    @JsonIgnore
    private Collaborator collaborator;

}
