package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Client;
import com.swl.models.people.User;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.payload.request.BoardRequest;
import com.swl.repository.BoardRepository;
import com.swl.repository.ClientRepository;
import com.swl.repository.ProjectRepository;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("ClientServiceTest")
@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private UserService userService;;

    private ClientService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new ClientService(repository, userService);
    }


    @Test
    public void getClient_Sucessfully() {
        User user = BuilderUtil.buildUser();
        user.setId(1);
        Client client = BuilderUtil.buildClient();
        client.setUser(user);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(client));
        Mockito.when(repository.findClientByUserId(1)).thenReturn(Optional.of(client));

        var response = service.getClient();
        Assertions.assertEquals(response, client);
    }


    @Test
    public void getClient_Error() {
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getClient();
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }

}
