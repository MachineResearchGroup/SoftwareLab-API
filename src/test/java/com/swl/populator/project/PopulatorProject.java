package com.swl.populator.project;

import com.swl.models.management.Team;
import com.swl.models.project.Project;
import com.swl.models.user.Collaborator;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.user.PopulatorClient;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorProject {


    @Autowired
    private PopulatorClient populatorClient;

    @Autowired
    private PopulatorEpic populatorEpic;

    @Autowired
    private PopulatorLabel populatorLabel;

    @Autowired
    private PopulatorRedaction populatorRedaction;

    @Autowired
    private PopulatorBoard populatorBoard;

    @Autowired
    private PopulatorDocument populatorDocument;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private ProjectRepository projectRepository;


    private Project create() {
        return Project.builder()
                .name(FakerUtil.getInstance().faker.dragonBall().character())
                .description(FakerUtil.getInstance().faker.shakespeare().hamletQuote())
                .repository(FakerUtil.getInstance().faker.internet().url())
                .build();
    }


    public Project save(PopulatorConfig config, Team team) {
        Project project = create();
        project.setClients(new ArrayList<>());

        int numberEpics = FakerUtil.getInstance().faker.number().numberBetween(2, config.getMaxNumberEpicsByProject());
        int numberLabels = FakerUtil.getInstance().faker.number().numberBetween(2, config.getMaxNumberLabels());
        int numberRedactions = FakerUtil.getInstance().faker.number().numberBetween(2, config.getMaxNumberRedactionsByProject());
        int numberBoards = FakerUtil.getInstance().faker.number().numberBetween(1, config.getMaxNumberRedactionsByProject());
        int numberDocs = FakerUtil.getInstance().faker.number().numberBetween(1, config.getMaxNumberDocumentsByProject());

        // Save Client
        project.getClients().add(populatorClient.save());
        project = projectRepository.save(project);

        // Save Epics
        project.setEpics(new ArrayList<>());
        Project finalProject = project;
        IntStream.range(0, numberEpics).forEach(e -> {
            finalProject.getEpics().add(populatorEpic.save(finalProject, config));
        });

        // Save Boards
        finalProject.setBoards(new ArrayList<>());
        IntStream.range(0, numberBoards).forEach(e -> {
            finalProject.getBoards().add(populatorBoard.save(finalProject, config));
        });

        // Save Labels
        finalProject.setLabels(new ArrayList<>());
        IntStream.range(0, numberLabels).forEach(e -> {
            finalProject.getLabels().add(populatorLabel.save());
        });

        // Save Redactions
        finalProject.setRequirements(new ArrayList<>());
        finalProject.setRedactions(new ArrayList<>());
        IntStream.range(0, numberRedactions).forEach(e -> {
            finalProject.getRedactions().add(populatorRedaction.save(finalProject, finalProject.getClients().get(0), config));
        });

        // Save Documents
        Optional<List<Collaborator>> collaborators = collaboratorRepository.findAllCollaboratorByTeamId(team.getId());
        finalProject.setDocuments(new ArrayList<>());
        IntStream.range(0, numberDocs).forEach(e -> {
            finalProject.getDocuments().add(populatorDocument.save(finalProject, collaborators.get()
                    .get(FakerUtil.getInstance().faker.number().numberBetween(0, collaborators.get().size() - 1))));
        });

        //TODO: Save Events

        return projectRepository.save(project);
    }

}
