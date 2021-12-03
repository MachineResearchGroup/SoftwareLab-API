package com.swl.populator.project;

import com.swl.models.people.Collaborator;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorBoard {

    @Autowired
    private PopulatorLabel populatorLabel;

    @Autowired
    private PopulatorColumn populatorColumn;

    @Autowired
    private BoardRepository boardRepository;

    private List<String> boardNames;

    public Board create() {
        boardNames = new ArrayList<>(Arrays.asList("development", "upstreaming", "backloag", "tests"));

        return Board.builder()
                .title(boardNames.get(FakerUtil.getInstance().faker.number().numberBetween(0, boardNames.size() - 1)))
                .build();
    }


    public Board save(Project project, List<Collaborator> collaborators, PopulatorConfig config) {
        Board board = create();
        board.setProject(project);

        board = boardRepository.save(board);

        // Save Columns
        board.setColumns(new ArrayList<>());
        Board finalBoard = board;
        IntStream.range(0, config.getNumberColumnsByBoard()).forEach(e -> {
            finalBoard.getColumns().add(populatorColumn.save(finalBoard, collaborators, config));
        });

        // Save Labels
        finalBoard.setLabels(new ArrayList<>());
        IntStream.range(0, config.getNumberLabels()).forEach(e -> {
            finalBoard.getLabels().add(populatorLabel.save());
        });

        return boardRepository.save(finalBoard);
    }

}
