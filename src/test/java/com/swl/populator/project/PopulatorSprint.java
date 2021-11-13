package com.swl.populator.project;

import com.swl.models.project.Epic;
import com.swl.models.project.Sprint;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.SprintRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
@AllArgsConstructor
public class PopulatorSprint {


    @Autowired
    private SprintRepository sprintRepository;


    public Sprint create() {
        LocalDate initDate = getDate();

        return Sprint.builder()
                .name(getSprint())
                .description(FakerUtil.getInstance().faker.shakespeare().kingRichardIIIQuote())
                .initDate(initDate)
                .endDate(getEndDate(initDate))
                .build();
    }


    private String getSprint() {
        return FakerUtil.getInstance().fakeValuesService.bothify("Sprint - ????##");
    }


    private LocalDate getEndDate(LocalDate initDate) {
        LocalDate endDate = getDate();
        while (initDate.isAfter(endDate)) {
            endDate = getDate();
        }
        return endDate;
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


    public Sprint save(Epic epic) {
        Sprint sprint = create();
        sprint.setEpic(epic);

        return sprintRepository.save(sprint);
    }

}
