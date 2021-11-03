package com.swl.service;

import com.swl.repository.ClientRepository;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@DisplayName("ProjetoServiceTest")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private ClientRepository clientRepository;

    private UserService service;


    @BeforeEach
    public void initUseCase() {
        service = new UserService(userRepository, collaboratorRepository, clientRepository);
    }


    @Test
    public void getCurrentUser_UserBasic() {
//        Usuario usuario = BuilderUtil.buildUsuario();
//        usuario.setId(1);
//
//        List<GrantedAuthority> authorities = new ArrayList<>(Collections.singletonList(
//                new SimpleGrantedAuthority(CargoEnum.ROLE_USUARIO.name())));
//
//        UserDetailsImpl userDetails = UserDetailsImpl.builder()
//                .id(1)
//                .username(usuario.getNomeUsuario())
//                .email(usuario.getEmail())
//                .password(usuario.getSenha())
//                .authorities(authorities)
//                .build();
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//
//        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//                .thenReturn(userDetails);
//
//        Mockito.when(userRepository.findByNomeUsuario(usuario.getNomeUsuario()))
//                .thenReturn(Optional.of(usuario));
//
//        Mockito.when(colaboradorRepository.findColaboradorByUsuarioId(1)).thenReturn(Optional.empty());
//        Mockito.when(clienteRepository.findClienteByUsuarioId(1)).thenReturn(Optional.empty());
//
//        var response = service.getCurrentUser();
//        Assertions.assertEquals(response, Optional.of(usuario));
    }

}
