package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.Project;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.request.ColumnRequest;
import com.swl.repository.BoardRepository;
import com.swl.repository.ColumnRepository;
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
@DisplayName("ColumnServiceTest")
@ExtendWith(MockitoExtension.class)
public class ColumnServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ColumnRepository repository;

    private ColumnService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new ColumnService(boardRepository, repository);
    }


    @Test
    public void registerColumn_Sucessfully() {
        Columns column = BuilderUtil.buildColumn();

        ColumnRequest columnRequest = ColumnRequest.builder()
                .title(column.getTitle())
                .idBoard(1)
                .build();

        Mockito.when(boardRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Board.class)));
        Mockito.when(repository.save(column)).thenReturn(column);

        var response = service.registerColumn(columnRequest);
        column.setId(1);
        Assertions.assertEquals(response, column);
    }


    @Test
    public void registerColumn_Error() {
        Columns column = BuilderUtil.buildColumn();

        ColumnRequest columnRequest = ColumnRequest.builder()
                .title(column.getTitle())
                .idBoard(1)
                .build();

        Mockito.when(boardRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerColumn(columnRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void editColumnSucessfully() {
        Columns column = BuilderUtil.buildColumn();

        ColumnRequest columnRequest = ColumnRequest.builder()
                .title(column.getTitle())
                .idBoard(1)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(column));
        Mockito.when(repository.save(column)).thenReturn(column);

        var response = service.editColumn(1, columnRequest);
        column.setId(1);
        Assertions.assertEquals(response, column);
    }


    @Test
    public void getColumn_Sucessfully() {
        Columns column = BuilderUtil.buildColumn();
        column.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(column));

        var response = service.getColumn(1);

        Assertions.assertEquals(response, column);
    }


    @Test
    public void getColumn_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getColumn(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllColumnsByBoard_Sucessfully() {
        Columns column = BuilderUtil.buildColumn();
        column.setId(1);
        Board board = BuilderUtil.buildBoard();
        board.setColumns(new ArrayList<>(Collections.singletonList(column)));

        Mockito.when(boardRepository.findById(1)).thenReturn(Optional.of(board));
        Mockito.when(repository.findAllByBoardId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(column))));

        var response = service.getAllColumnsByBoard(1);
        Assertions.assertEquals(response.get(0), column);
    }


    @Test
    public void getAllColumnsByBoard_Error() {
        Mockito.when(boardRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllColumnsByBoard(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void deleteColumns_Sucessfully() {
        Columns column = BuilderUtil.buildColumn();
        column.setId(1);
        Board board = BuilderUtil.buildBoard();
        board.setId(1);
        board.setColumns(new ArrayList<>(Collections.singletonList(column)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(column));
        Mockito.when(boardRepository.findByColumnId(1)).thenReturn(Optional.of(board));

        service.deleteColumn(1);
    }

}
