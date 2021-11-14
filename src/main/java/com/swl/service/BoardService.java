package com.swl.service;

import com.swl.models.enums.MessageEnum;
import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.management.Team;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.models.user.Collaborator;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.request.TeamRequest;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BoardService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final BoardRepository repository;


    public ResponseEntity<?> verifyBoard(BoardRequest boardRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Board board = new Board();

        modelUtil.map(boardRequest, board);
        List<MessageResponse> messageResponses = modelUtil.validate(board);

        if (projectRepository.findById(boardRequest.getIdProject()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, Board.class));
    }


    public Board registerBoard(BoardRequest boardRequest) {
        Optional<Project> project = projectRepository.findById(boardRequest.getIdProject());

        if (project.isPresent()) {
            Board board = Board.builder()
                    .title(boardRequest.getTitle())
                    .build();

            return repository.save(board);
        } else {
            return null;
        }
    }


    public Board editBoard(Integer idBoard, BoardRequest boardRequest) {
        Optional<Board> boardAux = repository.findById(idBoard);

        if (boardAux.isPresent()) {
            boardAux.get().setTitle(boardRequest.getTitle());
            return repository.save(boardAux.get());
        }
        return null;
    }


    public Board getBoard(Integer idBoard) {
        return repository.findById(idBoard).orElse(null);
    }


    public List<Board> getAllBoardByProject(Integer idProject) {
        return repository.findAllByProjectId(idProject).orElse(null);
    }


    public boolean deleteBoard(Integer idBoard) {
        if (repository.existsById(idBoard)) {
            repository.deleteById(idBoard);
            return true;
        }
        return false;
    }


}
