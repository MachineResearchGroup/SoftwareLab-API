package com.swl.config.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swl.models.Client;
import com.swl.models.Collaborator;
import com.swl.models.User;
import com.swl.models.enums.FunctionEnum;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Builder
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(Integer id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Object usuario) {

        List<GrantedAuthority> authorities = new ArrayList<>(Collections.singletonList(
                new SimpleGrantedAuthority(FunctionEnum.ROLE_USER.name())));

        if (usuario instanceof Collaborator) {
            authorities.add(new SimpleGrantedAuthority(((Collaborator) usuario).getFunction()));
            return new UserDetailsImpl(
                    ((Collaborator) usuario).getId(),
                    ((Collaborator) usuario).getUser().getUsername(),
                    ((Collaborator) usuario).getUser().getEmail(),
                    ((Collaborator) usuario).getUser().getPassword(),
                    authorities);
        } else if (usuario instanceof Client) {
            authorities.add(new SimpleGrantedAuthority(FunctionEnum.ROLE_CLIENT.name()));

            return new UserDetailsImpl(
                    ((Client) usuario).getId(),
                    ((Client) usuario).getUser().getUsername(),
                    ((Client) usuario).getUser().getEmail(),
                    ((Client) usuario).getUser().getPassword(),
                    authorities);
        } else if (usuario instanceof User){
            return new UserDetailsImpl(
                    ((User) usuario).getId(),
                    ((User) usuario).getUsername(),
                    ((User) usuario).getEmail(),
                    ((User) usuario).getPassword(),
                    authorities);
        }
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id.longValue();
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
