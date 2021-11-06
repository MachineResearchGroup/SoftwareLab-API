package com.swl.repository;

import com.swl.models.Client;
import com.swl.models.User;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void createClient() {
        Client client = BuilderUtil.buildClient();
        client.setUser(userRepository.save(client.getUser()));

        repository.save(client);
        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteClient() {
        Client client = BuilderUtil.buildClient();
        client.setUser(userRepository.save(client.getUser()));

        client = repository.save(client);
        repository.delete(client);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchClient() {
        Client client = BuilderUtil.buildClient();
        client.setUser(userRepository.save(client.getUser()));

        client = repository.save(client);
        Optional<Client> clienteFound = repository.findById(client.getId());

        Assertions.assertTrue(clienteFound.isPresent());
    }


    @Test
    public void updateClient() {
        Client client = BuilderUtil.buildClient();
        User user = userRepository.save(client.getUser());

        client.setUser(user);
        client = repository.save(client);

        Client clientUpdate = BuilderUtil.buildClient();
        clientUpdate.setId(client.getId());
        clientUpdate.setUser(user);
        clientUpdate.setCorporateName("Cliente Update");

        repository.save(clientUpdate);
        Optional<Client> responseFinal = repository.findById(client.getId());
        Assertions.assertEquals(responseFinal.get().getCorporateName(), "Cliente Update");
    }


    @Test
    public void findClientByUserId() {
        Client client = BuilderUtil.buildClient();
        client.setUser(userRepository.save(client.getUser()));

        repository.save(client);

        Optional<Client> clienteResponse = repository.findClientByUserId(client.getUser().getId());
        Assertions.assertEquals(client.getCorporateName(), clienteResponse.get().getCorporateName());
    }
}
