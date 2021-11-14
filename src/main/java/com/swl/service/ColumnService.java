package com.swl.service;

import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.Project;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.request.ColumnRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.BoardRepository;
import com.swl.repository.ColumnRepository;
import com.swl.repository.ProjectRepository;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ColumnService {

    @Autowired
    private final BoardRepository boardRepository;

    @Autowired
    private final ColumnRepository repository;


    public ResponseEntity<?> verifyColumn(ColumnRequest boardRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Board board = new Board();

        modelUtil.map(boardRequest, board);
        List<MessageResponse> messageResponses = modelUtil.validate(board);

        if (boardRepository.findById(boardRequest.getIdBoard()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Board.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, Columns.class));
    }


    public Columns registerColumn(ColumnRequest columnRequest) {
        Optional<Board> board = boardRepository.findById(columnRequest.getIdBoard());

        if (board.isPresent()) {
            Columns columns = Columns.builder()
                    .title(columnRequest.getTitle())
                    .build();

            return repository.save(columns);
        } else {
            return null;
        }
    }


    public Columns editColumn(Integer idColumn, ColumnRequest columnRequest) {
        Optional<Columns> columnAux = repository.findById(idColumn);

        if (columnAux.isPresent()) {
            columnAux.get().setTitle(columnRequest.getTitle());
            return repository.save(columnAux.get());
        }
        return null;
    }


    public Columns getColumn(Integer idColumn) {
        return repository.findById(idColumn).orElse(null);
    }


    public List<Columns> getAllColumnsByBoard(Integer idBoard) {
        return repository.findAllByBoardId(idBoard).orElse(null);
    }


    public boolean deleteColumn(Integer idColumn) {
        if (repository.existsById(idColumn)) {
            repository.deleteById(idColumn);
            return true;
        }
        return false;
    }


}
