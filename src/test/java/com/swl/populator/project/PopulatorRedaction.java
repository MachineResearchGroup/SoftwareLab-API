package com.swl.populator.project;

import com.swl.models.project.Project;
import com.swl.models.project.Redaction;
import com.swl.models.project.Requirement;
import com.swl.models.user.Client;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.CSVToArrayUtil;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.RedactionRepository;
import com.swl.repository.RequirementRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorRedaction {

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private RedactionRepository redactionRepository;


    public Redaction create() {
        List<List<String>> redactions = CSVToArrayUtil.csvToArrayList("src/test/resources/US-dataset.csv");

        return Redaction.builder()
                .description(redactions.get(FakerUtil.getInstance().faker.number().numberBetween(1, 1613)).get(0))
                .date(getDate())
                .build();
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


    public Redaction save(Project project, Client client, PopulatorConfig config) {
        Redaction redaction = create();
        redaction.setProject(project);
        redaction.setClient(client);

        //Save Requirements
        IntStream.range(0, config.getMaxNumberRequirementsByRedaction()).forEach(f -> {
            int posRequirement = FakerUtil.getInstance().faker.number().numberBetween(1, 2409);
            Optional<Requirement> requirement = requirementRepository.findById(posRequirement);
            redaction.setRequirements(new ArrayList<>(Collections.singletonList(requirement.get())));

            project.getRequirements().add(requirement.get());
        });

        return redactionRepository.save(redaction);
    }

}
