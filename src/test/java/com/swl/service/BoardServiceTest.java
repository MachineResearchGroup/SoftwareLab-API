package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Team;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.request.ProjectRequest;
import com.swl.repository.BoardRepository;
import com.swl.repository.ProjectRepository;
import com.swl.util.BuilderUtil;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("BoardServiceTest")
@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private BoardRepository repository;

    private BoardService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new BoardService(projectRepository, repository);
    }


    @Test
    public void registerBoard_Sucessfully() {
        Board board = BuilderUtil.buildBoard();

        BoardRequest boardRequest = BoardRequest.builder()
                .title(board.getTitle())
                .idProject(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Project.class)));
        Mockito.when(repository.save(board)).thenReturn(board);

        var response = service.registerBoard(boardRequest);
        board.setId(1);
        Assertions.assertEquals(response, board);
    }


    @Test
    public void registerBoard_Error() {
        Board board = BuilderUtil.buildBoard();

        BoardRequest boardRequest = BoardRequest.builder()
                .title(board.getTitle())
                .idProject(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerBoard(boardRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void editBoard_Sucessfully() {
        Board board = BuilderUtil.buildBoard();

        BoardRequest boardRequest = BoardRequest.builder()
                .title(board.getTitle())
                .idProject(1)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(board));
        Mockito.when(repository.save(board)).thenReturn(board);

        var response = service.editBoard(1, boardRequest);
        board.setId(1);
        Assertions.assertEquals(response, board);
    }


    @Test
    public void getBoard_Sucessfully() {
        Board board = BuilderUtil.buildBoard();
        board.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(board));
        var response = service.getBoard(1);

        Assertions.assertEquals(response, board);
    }


    @Test
    public void getBoard_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getBoard(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllBoardByProject_Sucessfully() {
        Board board = BuilderUtil.buildBoard();
        Project project = BuilderUtil.buildProject();
        project.setBoards(new ArrayList<>(Collections.singletonList(board)));

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(repository.findAllByProjectId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(board))));

        var response = service.getAllBoardByProject(1);
        Assertions.assertEquals(response.get(0), board);
    }


    @Test
    public void getAllBoardByProject_Error() {
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllBoardByProject(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void deleteBoard_Sucessfully() {
        Board board = BuilderUtil.buildBoard();
        board.setId(1);
        Project project = BuilderUtil.buildProject();
        project.setBoards(new ArrayList<>(Collections.singletonList(board)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(board));
        Mockito.when(projectRepository.findByBoardId(1)).thenReturn(Optional.of(project));

        service.deleteBoard(1);
    }

}
