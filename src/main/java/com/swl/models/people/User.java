package com.swl.models.people;

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
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

    @Lob
    private byte[] profileImage;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "user_phone")
    private List<String> phones;


    public User(String name, String username, String email, String password, List<String> phones) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
