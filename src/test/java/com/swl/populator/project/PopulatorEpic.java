package com.swl.populator.project;

import com.swl.models.project.Epic;
import com.swl.models.project.Project;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.EpicRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorEpic {


    @Autowired
    private PopulatorSprint populatorSprint;

    @Autowired
    private EpicRepository epicRepository;


    public Epic create() {
        LocalDate initDate = getDate();

        return Epic.builder()
                .name(FakerUtil.getInstance().faker.lordOfTheRings().character())
                .description(FakerUtil.getInstance().faker.shakespeare().kingRichardIIIQuote())
                .initDate(initDate)
                .endDate(getEndDate(initDate))
                .build();
    }


    private LocalDate getEndDate(LocalDate initDate){
        LocalDate endDate = getDate();
        while(initDate.isAfter(endDate)){
            endDate = getDate();
        }
        return endDate;
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


    public Epic save(Project project, PopulatorConfig config) {
        Epic epic = create();
        epic.setProject(project);
        epic = epicRepository.save(epic);

        //Save Sprints
        epic.setSprints(new ArrayList<>());
        Epic finalEpic = epic;
        IntStream.range(0, config.getNumberSprintsByEpic()).forEach(e-> {
            finalEpic.getSprints().add(populatorSprint.save(finalEpic));
        });

        return epicRepository.save(epic);
    }

}
