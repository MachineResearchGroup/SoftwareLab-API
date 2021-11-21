package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.Project;
import com.swl.payload.request.ColumnRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.BoardRepository;
import com.swl.repository.ColumnRepository;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ColumnService {

    @Autowired
    private final BoardRepository boardRepository;

    @Autowired
    private final ColumnRepository repository;


    private void verifyColumn(ColumnRequest columnRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Columns columns = new Columns();

        modelUtil.map(columnRequest, columns);
        ErrorResponse error = modelUtil.validate(columns);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    public Columns registerColumn(ColumnRequest columnRequest) {
        verifyColumn(columnRequest);
        Optional<Board> board = boardRepository.findById(columnRequest.getIdBoard());

        if (board.isPresent()) {
            Columns columns = Columns.builder()
                    .title(columnRequest.getTitle())
                    .board(board.get())
                    .build();

            columns = repository.save(columns);

            if (Objects.isNull(board.get().getColumns()))
                board.get().setColumns(new ArrayList<>());
            board.get().getColumns().add(columns);

            boardRepository.save(board.get());
            return columns;
        } else {
            throw new NotFoundException(Board.class);
        }
    }


    public Columns editColumn(Integer idColumn, ColumnRequest columnRequest) {
        verifyColumn(columnRequest);
        Columns columns = getColumn(idColumn);
        columns.setTitle(columnRequest.getTitle());
        return repository.save(columns);
    }


    public Columns getColumn(Integer idColumn) {
        Optional<Columns> columns = repository.findById(idColumn);
        if (columns.isPresent()) {
            return columns.get();
        } else {
            throw new NotFoundException(Board.class);
        }
    }


    public List<Columns> getAllColumnsByBoard(Integer idBoard) {
        Optional<Board> board = boardRepository.findById(idBoard);
        if (board.isPresent()) {
            return repository.findAllByBoardId(idBoard).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Board.class);
    }


    public void deleteColumn(Integer idColumn) {
        Columns columns = getColumn(idColumn);
        Optional<Board> board = boardRepository.findByColumnId(columns.getId());
        if (board.isPresent()) {
            board.get().getColumns().remove(board);
            boardRepository.save(board.get());
        }
        repository.deleteById(columns.getId());
    }


}
