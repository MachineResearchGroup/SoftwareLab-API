package com.swl.populator.project;

import com.swl.models.people.Collaborator;
import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.models.project.Task;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.CSVToArrayUtil;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.HistoryRepository;
import com.swl.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorTask {

    @Autowired
    private PopulatorLabel populatorLabel;

    @Autowired
    private TaskRepository taskRepository;


    public Task create() {
        Task task = new Task();
        task.setTitle(FakerUtil.getInstance().faker.lordOfTheRings().location());
        task.setDuration(FakerUtil.getInstance().faker.number().numberBetween(1, 1000));
        task.setEstimate(FakerUtil.getInstance().faker.number().numberBetween(1, 1000));
        task.setDescription(FakerUtil.getInstance().faker.shakespeare().romeoAndJulietQuote());
        task.setEndDate(getDate());
        task.setPriority(FakerUtil.getInstance().faker.number().numberBetween(1, 10));
        return task;
    }


    private LocalDate getDate(){
        try {
            return FakerUtil.getInstance().faker.date().between(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2019"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2021")).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException ignored) {
        }
        return null;
    }


    public Task saveFromHistory(Columns columns, Collaborator collaborator, History history, PopulatorConfig config) {
        Task task = create();
        task.setHistory(history);
        task.setColumn(columns);
        task.setCollaborators(new ArrayList(Collections.singletonList(collaborator)));
        task = taskRepository.save(task);

        // Save Labels
        task.setLabels(new ArrayList<>());
        Task finalTask = task;
        IntStream.range(0, config.getNumberLabels()).forEach(e -> {
            finalTask.getLabels().add(populatorLabel.save());
        });

        return taskRepository.save(finalTask);
    }


    public Task save(Columns columns, Collaborator collaborator, PopulatorConfig config) {
        Task task = create();
        task.setColumn(columns);
        task.setCollaborators(new ArrayList(Collections.singletonList(collaborator)));
        task = taskRepository.save(task);

        // Save Labels
        task.setLabels(new ArrayList<>());
        Task finalTask = task;
        IntStream.range(0, config.getNumberLabels()).forEach(e -> {
            finalTask.getLabels().add(populatorLabel.save());
        });

        return taskRepository.save(finalTask);
    }
}
