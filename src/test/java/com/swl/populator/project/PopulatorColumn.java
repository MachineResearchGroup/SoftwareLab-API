package com.swl.populator.project;

import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.ColumnRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorColumn {

    @Autowired
    private PopulatorLabel populatorLabel;

    @Autowired
    private PopulatorHistory populatorHistory;

    @Autowired
    private PopulatorTask populatorTask;

    @Autowired
    private ColumnRepository columnRepository;


    public Columns create() {
        return Columns.builder()
                .title(FakerUtil.getInstance().faker.lordOfTheRings().location())
                .build();
    }


    public Columns save(Board board, PopulatorConfig config) {
        Columns columns = create();
        columns.setBoard(board);

        columns = columnRepository.save(columns);

        // Save Histories
        Columns finalColumns = columns;
        finalColumns.setHistories(new ArrayList<>());
        IntStream.range(0, config.getNumberHistoriesByColumn()).forEach(e -> {
            finalColumns.getHistories().add(populatorHistory.save(finalColumns, config));
        });

        // Save Taks
        finalColumns.setTasks(new ArrayList<>());
        IntStream.range(0, config.getNumberTasksByColumn()).forEach(e -> {
            finalColumns.getTasks().add(populatorTask.save(finalColumns, config));
        });

        // Save Labels
        finalColumns.setLabels(new ArrayList<>());
        IntStream.range(0, config.getNumberLabels()).forEach(e -> {
            finalColumns.getLabels().add(populatorLabel.save());
        });

        return columnRepository.save(finalColumns);
    }

}
