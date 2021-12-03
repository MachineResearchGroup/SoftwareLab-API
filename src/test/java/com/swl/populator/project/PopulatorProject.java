package com.swl.populator.project;

import com.swl.models.management.Team;
import com.swl.models.project.Project;
import com.swl.models.people.Collaborator;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.user.PopulatorClient;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.EventRepository;
import com.swl.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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
    private PopulatorEvent populatorEvent;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private ProjectRepository projectRepository;


    private Project create() {
        return Project.builder()
                .name(FakerUtil.getInstance().faker.app().name())
                .description(FakerUtil.getInstance().faker.shakespeare().hamletQuote())
                .repository(FakerUtil.getInstance().faker.internet().url())
                .build();
    }


    public Project save(PopulatorConfig config, Team team) {
        Project project = create();
        project.setClients(new ArrayList<>());

        // Save Client
        project.getClients().add(populatorClient.save());
        project = projectRepository.save(project);

        // Save Epics
        project.setEpics(new ArrayList<>());
        Project finalProject = project;
        IntStream.range(0, config.getNumberEpicsByProject()).forEach(e -> {
            finalProject.getEpics().add(populatorEpic.save(finalProject, config));
        });

        // Save Boards
        Optional<List<Collaborator>> collaborators = collaboratorRepository.findAllCollaboratorByTeamId(team.getId());
        finalProject.setBoards(new ArrayList<>());
        List<Collaborator> finalCollaborators = collaborators.get();
        IntStream.range(0, config.getNumberBoardsByProject()).forEach(e -> {
            finalProject.getBoards().add(populatorBoard.save(finalProject, finalCollaborators, config));
        });

        // Save Labels
        finalProject.setLabels(new ArrayList<>());
        IntStream.range(0, config.getNumberLabels()).forEach(e -> {
            finalProject.getLabels().add(populatorLabel.save());
        });

        // Save Redactions
        finalProject.setRequirements(new ArrayList<>());
        finalProject.setRedactions(new ArrayList<>());
        IntStream.range(0, config.getNumberRedactionsByProjects()).forEach(e -> {
            finalProject.getRedactions().add(populatorRedaction.save(finalProject, finalProject.getClients().get(0), config));
        });

        // Save Documents
        finalProject.setDocuments(new ArrayList<>());
        IntStream.range(0, config.getNumberDocumentsByProject()).forEach(e -> {
            finalProject.getDocuments().add(populatorDocument.save(finalProject, finalCollaborators
                    .get(FakerUtil.getInstance().faker.number().numberBetween(0, finalCollaborators.size() - 1))));
        });

        // Save Events
//        finalProject.setEvents(new ArrayList<>());
//        IntStream.range(0, config.getNumberEventsByProject()).forEach(e -> {
//            finalProject.getEvents().add(populatorEvent.save(finalProject, collaborators.get(), finalProject.getClients(), finalProject.getDocuments().get(FakerUtil.getInstance().faker.number()
//                    .numberBetween(0, finalProject.getDocuments().size() - 1))));
//        });

        return projectRepository.save(project);
    }

}
