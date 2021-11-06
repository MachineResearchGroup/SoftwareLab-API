package com.swl.service;

import com.swl.models.user.Client;
import com.swl.models.project.Project;
import com.swl.models.management.Team;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.ProjectRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.ClientRepository;
import com.swl.repository.ProjectRepository;
import com.swl.repository.TeamRepository;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private final ProjectRepository repository;

    @Autowired
    private final TeamRepository teamRepository;

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final TeamService teamService;


    public ResponseEntity<?> verifyProject(ProjectRequest projectRequest){
        ModelUtil modelUtil = ModelUtil.getInstance();
        Project project = new Project();

        modelUtil.map(projectRequest, project);
        List<MessageResponse> messageResponses = modelUtil.validate(project);

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, Project.class));
    }


    public Project registerProject(ProjectRequest projectRequest) {

        Optional<Team> teamOptional = teamRepository.findById(projectRequest.getIdTeam());
        if (teamOptional.isPresent()) {
            Project project = Project.builder()
                    .name(projectRequest.getName())
                    .description(projectRequest.getDescription())
                    .repository(projectRequest.getRepository())
                    .build();

            if (Objects.isNull(teamOptional.get().getProjects()))
                teamOptional.get().setProjects(new ArrayList<>());
            teamOptional.get().getProjects().add(project);

            project = repository.save(project);
            teamRepository.save(teamOptional.get());

            return project;
        } else {
            return null;
        }
    }


    public Project editProject(Integer idProject, ProjectRequest project) {
        Optional<Project> projectAux = repository.findById(idProject);

        if (projectAux.isPresent()) {
            projectAux.get().setName(project.getName());
            projectAux.get().setDescription(project.getDescription());
            projectAux.get().setRepository(project.getRepository());

            return repository.save(projectAux.get());
        }

        return null;
    }


    public Project getProject(Integer idProject) {
        return repository.findById(idProject).orElse(null);
    }


    public boolean deleteProject(Integer idProjeto) {
        if (repository.existsById(idProjeto)) {
            repository.deleteById(idProjeto);
            return true;
        }
        return false;
    }


    public boolean addClientInProject(Integer idProjeto, String clientEmail) {
        Optional<Project> projectOptional = repository.findById(idProjeto);
        Optional<Client> clientOptional = clientRepository.findClientByUserEmail(clientEmail);

        if (projectOptional.isPresent() && clientOptional.isPresent()) {
            if (Objects.isNull(projectOptional.get().getClients())) {
                projectOptional.get().setClients(new ArrayList<>());
            }
            AtomicBoolean clientExist = new AtomicBoolean(false);

            projectOptional.get().getClients().forEach(c -> {
                if (c.getId().equals(clientOptional.get().getId())) {
                    clientExist.set(true);
                }
            });

            if (!clientExist.get()) {
                projectOptional.get().getClients().add(clientOptional.get());
            }

            repository.save(projectOptional.get());

            return true;
        }
        return false;
    }


    public List<Project> getAllProjectsByTeam(Integer idTeam){
        return teamRepository.findById(idTeam)
                .map(Team::getProjects)
                .orElse(null);
    }


    public List<Project> getAllProjectsByOrganization(Integer idOrganization){
        List<Team> teams = teamService.getAllTeamByOrganization(idOrganization);
        if(!Objects.isNull(teams) && !teams.isEmpty()) {
            List<Project> projects = new ArrayList<>();
            teams.stream().filter(t -> !t.getProjects().isEmpty()).forEach(t -> projects.addAll(t.getProjects()));

            return projects;
        }
        return null;
    }

}
