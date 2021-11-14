package com.swl.populator.project;

import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.CSVToArrayUtil;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorHistory {

    @Autowired
    private PopulatorLabel populatorLabel;

    @Autowired
    private PopulatorTask populatorTask;

    @Autowired
    private HistoryRepository historyRepository;


    public History create() {
        List<List<String>> redactions = CSVToArrayUtil.csvToArrayList("src/test/resources/US-dataset.csv");

        History history = new History();
        history.setTitle(FakerUtil.getInstance().faker.lordOfTheRings().location());
        history.setWeight(FakerUtil.getInstance().faker.number().numberBetween(1, 10));
        history.setDescription(redactions.get(FakerUtil.getInstance().faker.number().numberBetween(1, 1613)).get(0));
        history.setEndDate(getDate());
        history.setPriority(FakerUtil.getInstance().faker.number().numberBetween(1, 10));
        return history;
    }


    private LocalDate getDate() {
        try {
            return FakerUtil.getInstance().faker.date().between(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2019"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2021")).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException ignored) {
        }
        return null;
    }


    public History save(Columns column, PopulatorConfig config) {
        History history = create();
        history.setColumn(column);

        history = historyRepository.save(history);

        // Save Taks
        history.setTasks(new ArrayList<>());
        History finalHistory = history;
        IntStream.range(0, config.getNumberTasksByHistory()).forEach(e -> {
            finalHistory.getTasks().add(populatorTask.saveFromHistory(column, finalHistory, config));
        });

        // Save Labels
        finalHistory.setLabels(new ArrayList<>());
        IntStream.range(0, config.getNumberLabels()).forEach(e -> {
            finalHistory.getLabels().add(populatorLabel.save());
        });

        return historyRepository.save(finalHistory);
    }

}
