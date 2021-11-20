package com.swl.service;

import com.swl.exceptions.business.EmptyException;
import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Organization;
import com.swl.models.management.Team;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Project;
import com.swl.payload.request.ProjectRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.*;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final TeamService teamService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final OrganizationRepository organizationRepository;


    private void verifyProject(ProjectRequest projectRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Project project = new Project();

        modelUtil.map(projectRequest, project);
        ErrorResponse error = modelUtil.validate(project);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    @Transactional
    public Project registerProject(ProjectRequest projectRequest) {
        verifyProject(projectRequest);

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

            project = saveProject(project);
            teamRepository.save(teamOptional.get());

            return project;
        } else {
            throw new NotFoundException(Team.class);
        }
    }

    @Transactional
    public Project saveProject(Project project){
        return repository.save(project);
    }


    @Transactional
    public Project editProject(Integer idProject, ProjectRequest project) {
        Project projectAux = getProject(idProject);
        projectAux.setName(project.getName());
        projectAux.setDescription(project.getDescription());
        projectAux.setRepository(project.getRepository());

        return saveProject(projectAux);
    }


    @Transactional
    public Project getProject(Integer idProject) {
        Optional<Project> project = repository.findById(idProject);
        if (project.isPresent()) {
            return project.get();
        } else {
            throw new NotFoundException(Project.class);
        }
    }


    @Transactional
    public List<Project> getAllProjectsByClient(Integer idClient) {
        if (clientRepository.existsById(idClient)) {
            return repository.findAllByClientId(idClient).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Client.class);
    }


    @Transactional
    public List<Project> getAllProjectsByCollaborator(Integer idCollaborator) {
        if (collaboratorRepository.existsById(idCollaborator)) {
            Optional<List<Team>> teams = teamRepository.findAllByCollaboratorId(idCollaborator);
            List<Project> projects = new ArrayList<>();

            teams.ifPresent(teamList -> teamList.forEach(t -> {
                if (teamRepository.existsById(t.getId()))
                    projects.addAll(getAllProjectsByTeam(t.getId()));
            }));

            return projects;
        }
        throw new NotFoundException(Collaborator.class);
    }


    @Transactional
    public List<Project> getAllProjectsByCollaboratorActual() {
        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            return getAllProjectsByCollaborator(((Collaborator) userService.getCurrentUser().get()).getId());
        }
        throw new NotFoundException(Collaborator.class);
    }


    @Transactional
    public List<Project> getAllProjectsByTeam(Integer idTeam) {
        Optional<Team> team = teamRepository.findById(idTeam);
        if (team.isPresent()) {
            List<Project> projects = team.get().getProjects();
            return !Objects.isNull(projects) ? projects : new ArrayList<>();
        } else {
            throw new NotFoundException(Team.class);
        }
    }


    @Transactional
    public List<Project> getAllProjectsByOrganization(Integer idOrganization) {
        Optional<Organization> organization = organizationRepository.findById(idOrganization);

        if (organization.isPresent()) {
            List<Team> teams = teamService.getAllTeamByOrganization(idOrganization);
            if (!Objects.isNull(teams) && !teams.isEmpty()) {
                List<Project> projects = new ArrayList<>();
                teams.stream().filter(t -> !t.getProjects().isEmpty()).forEach(t -> projects.addAll(t.getProjects()));
                return projects;
            }
            throw new EmptyException(Team.class);
        }
        throw new NotFoundException(Organization.class);
    }


    public void deleteProject(Integer idProject) {
        Project project = getProject(idProject);
        Optional<List<Team>> teams = teamRepository.findAllByProjectId(project.getId());
        teams.ifPresent(teamList -> teamList.forEach(t -> {
            t.getProjects().remove(project);
            teamRepository.save(t);
        }));
        repository.deleteById(project.getId());
    }


    public void addClientInProject(Integer idProjeto, String clientEmail) {
        Project project = getProject(idProjeto);
        Optional<Client> clientOptional = clientRepository.findClientByUserEmail(clientEmail);

        if (clientOptional.isPresent()) {
            if (Objects.isNull(project.getClients())) {
                project.setClients(new ArrayList<>());
            }
            AtomicBoolean clientExist = new AtomicBoolean(false);

            project.getClients().forEach(c -> {
                if (c.getId().equals(clientOptional.get().getId())) {
                    clientExist.set(true);
                }
            });

            if (!clientExist.get()) {
                project.getClients().add(clientOptional.get());
            }
            repository.save(project);
        }else {
            throw new NotFoundException(Client.class);
        }
    }

}
