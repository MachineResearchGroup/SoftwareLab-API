package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.enums.MessageEnum;
import com.swl.models.management.Organization;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.BoardRepository;
import com.swl.repository.ProjectRepository;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BoardService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final BoardRepository repository;


    public void verifyBoard(BoardRequest boardRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Board board = new Board();

        if (projectRepository.findById(boardRequest.getIdProject()).isEmpty()) {
            throw new NotFoundException(Project.class);
        }

        modelUtil.map(boardRequest, board);
        ErrorResponse error = modelUtil.validate(board);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    public Board registerBoard(BoardRequest boardRequest) {
        Optional<Project> project = projectRepository.findById(boardRequest.getIdProject());

        if (project.isPresent()) {
            Board board = Board.builder()
                    .title(boardRequest.getTitle())
                    .project(project.get())
                    .build();
            board = repository.save(board);

            if (Objects.isNull(project.get().getBoards()))
                project.get().setBoards(new ArrayList<>());
            project.get().getBoards().add(board);

            projectRepository.save(project.get());
            return board;
        } else {
            throw new NotFoundException(Project.class);
        }
    }


    public Board editBoard(Integer idBoard, BoardRequest boardRequest) {
        Optional<Board> boardAux = repository.findById(idBoard);

        if (boardAux.isPresent()) {
            boardAux.get().setTitle(boardRequest.getTitle());
            return repository.save(boardAux.get());
        }
        throw new NotFoundException(Board.class);
    }


    public Board getBoard(Integer idBoard) {
        Optional<Board> board = repository.findById(idBoard);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new NotFoundException(Board.class);
        }
    }


    public List<Board> getAllBoardByProject(Integer idProject) {
        Optional<Project> project = projectRepository.findById(idProject);
        if (project.isPresent()) {
            return repository.findAllByProjectId(idProject).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Project.class);
    }


    public void deleteBoard(Integer idBoard) {
        Board board = getBoard(idBoard);
        repository.deleteById(board.getId());
    }

}
