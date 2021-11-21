package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.models.people.User;
import com.swl.repository.CollaboratorRepository;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("ClientServiceTest")
@ExtendWith(MockitoExtension.class)
public class CollaboratorServiceTest {

    @Mock
    private CollaboratorRepository repository;

    @Mock
    private UserService userService;

    private CollaboratorService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new CollaboratorService(repository, userService);
    }


    @Test
    public void getCollaborator_Sucessfully() {
        User user = BuilderUtil.buildUser();
        user.setId(1);
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setUser(user);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findCollaboratorByUserId(1)).thenReturn(Optional.of(collaborator));

        var response = service.getCollaborator();
        Assertions.assertEquals(response, collaborator);
    }


    @Test
    public void getCollaborator_Error() {
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getCollaborator();
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }

}
