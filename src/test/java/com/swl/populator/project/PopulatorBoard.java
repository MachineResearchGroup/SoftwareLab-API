package com.swl.populator.project;

import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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


    public Board create() {
        return Board.builder()
                .title(FakerUtil.getInstance().faker.lordOfTheRings().location())
                .build();
    }


    public Board save(Project project, PopulatorConfig config) {
        Board board = create();
        board.setProject(project);

        board = boardRepository.save(board);

        int numberLabels = FakerUtil.getInstance().faker.number().numberBetween(2, config.getMaxNumberLabels());

        // Save Columns
        board.setColumns(new ArrayList<>());
        Board finalBoard = board;
        IntStream.range(0, numberLabels).forEach(e -> {
            finalBoard.getColumns().add(populatorColumn.save(finalBoard, config));
        });

        // Save Labels
        finalBoard.setLabels(new ArrayList<>());
        IntStream.range(0, numberLabels).forEach(e -> {
            finalBoard.getLabels().add(populatorLabel.save());
        });

        return boardRepository.save(finalBoard);
    }

}
