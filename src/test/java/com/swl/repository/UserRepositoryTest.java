package com.swl.repository;

import com.swl.models.User;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;


    @Test
    public void createUser() {
        User user = BuilderUtil.buildUser();

        repository.save(user);
        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteUser() {
        User user = BuilderUtil.buildUser();

        user = repository.save(user);
        repository.delete(user);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchUser() {
        User user = BuilderUtil.buildUser();

        user = repository.save(user);
        Optional<User> clienteFound = repository.findById(user.getId());

        Assertions.assertTrue(clienteFound.isPresent());
    }


    @Test
    public void updateUser() {
        User user = BuilderUtil.buildUser();

        user = repository.save(user);

        User userUpdate = BuilderUtil.buildUser();
        userUpdate.setId(user.getId());
        userUpdate.setUsername("Username Update");

        repository.save(userUpdate);
        Optional<User> responseFinal = repository.findById(user.getId());
        Assertions.assertEquals(responseFinal.get().getUsername(), "Username Update");
    }


    @Test
    public void findByUsername() {
        User user = BuilderUtil.buildUser();
        user = repository.save(user);

        Optional<User> responseFinal = repository.findByUsername(user.getUsername());
        Assertions.assertTrue(responseFinal.isPresent());
    }


    @Test
    public void findByEmail() {
        User user = BuilderUtil.buildUser();
        user = repository.save(user);

        Optional<User> responseFinal = repository.findByEmail(user.getEmail());
        Assertions.assertTrue(responseFinal.isPresent());
    }


    @Test
    public void existsByUsername() {
        User user = BuilderUtil.buildUser();
        user = repository.save(user);

        Assertions.assertTrue(repository.existsByUsername(user.getUsername()));
    }


    @Test
    public void existsByEmail() {
        User user = BuilderUtil.buildUser();
        user = repository.save(user);

        Assertions.assertTrue(repository.existsByEmail(user.getEmail()));
    }
}
