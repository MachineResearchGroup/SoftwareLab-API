package com.swl.populator.project;

import com.swl.models.project.Requirement;
import com.swl.populator.util.CSVToArrayUtil;
import com.swl.repository.RequirementRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PopulatorRequirement {


    @Autowired
    private RequirementRepository requirementRepository;


    private List<Requirement> createAll() {
        List<List<String>> requirements = CSVToArrayUtil.csvToArrayList("src/test/resources/Rainbow_Dataset.csv");
        requirements.remove(0);

        return requirements.stream().map(r -> Requirement.builder()
                .description(r.get(1))
                .category(r.get(2))
                .build()).collect(Collectors.toList());
    }


    public void saveAll() {
        List<Requirement> requirements = createAll();
        requirementRepository.saveAll(requirements);
    }

}
