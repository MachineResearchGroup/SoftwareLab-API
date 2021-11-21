package com.swl.service;

import com.swl.exceptions.business.EmptyException;
import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.management.Team;
import com.swl.models.people.Collaborator;
import com.swl.models.people.User;
import com.swl.payload.request.CollaboratorRequest;
import com.swl.payload.request.TeamRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.OrganizationRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.repository.TeamRepository;
import com.swl.util.ModelUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamService {

    @Autowired
    private final TeamRepository repository;

    @Autowired
    private final OrganizationRepository organizationRepository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final OrganizationTeamRepository organizationTeamRepository;

    @Autowired
    private final AuthService authService;

    @Autowired
    private final UserService userService;


    private void verifyTeam(TeamRequest teamRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Team team = new Team();

        modelUtil.map(teamRequest, team);
        ErrorResponse error = modelUtil.validate(team);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    @Transactional
    public Team registerTeam(TeamRequest teamRequest) {
        verifyTeam(teamRequest);

        Optional<Organization> org = organizationRepository.findById(teamRequest.getIdOrganization());

        if (org.isPresent()) {
            Team team;
            if (!Objects.isNull(teamRequest.getSupervisorEmail())) {
                Optional<Collaborator> collaborator = collaboratorRepository.findCollaboratorByUserEmail(teamRequest.getSupervisorEmail());

                if (collaborator.isPresent()) {
                    team = Team.builder()
                            .name(teamRequest.getName())
                            .supervisor(collaborator.get())
                            .build();
                } else {
                    throw new NotFoundException(Collaborator.class);
                }
                team = saveTeam(team);

                OrganizationTeam organizationTeam = OrganizationTeam.builder()
                        .organization(org.get())
                        .team(team)
                        .collaborator(collaborator.get())
                        .build();

                organizationTeamRepository.save(organizationTeam);
            } else {
                team = Team.builder()
                        .name(teamRequest.getName())
                        .build();

                team = saveTeam(team);
            }

            OrganizationTeam organizationTeam = OrganizationTeam.builder()
                    .organization(org.get())
                    .team(team)
                    .build();

            organizationTeamRepository.save(organizationTeam);
            return team;

        } else {
            throw new NotFoundException(Organization.class);
        }
    }


    @Transactional
    public Team saveTeam(Team team) {
        return repository.save(team);
    }


    @Transactional
    public Team editTeam(Integer idTeam, TeamRequest teamRequest) {
        verifyTeam(teamRequest);
        Optional<Team> teamAux = repository.findById(idTeam);

        if (teamAux.isPresent()) {
            teamAux.get().setName(teamRequest.getName());
            if (!Objects.isNull(teamRequest.getSupervisorEmail())) {
                Optional<Collaborator> colaborador = collaboratorRepository
                        .findCollaboratorByUserEmail(teamRequest.getSupervisorEmail());
                colaborador.ifPresent(value -> teamAux.get().setSupervisor(value));
            }

            return repository.save(teamAux.get());
        }

        throw new NotFoundException(Team.class);
    }


    @Transactional
    public Team getTeam(Integer idTeam) {
        Optional<Team> team = repository.findById(idTeam);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new NotFoundException(Team.class);
        }
    }


    public List<Collaborator> getCollaborators(Integer idTeam) {
        Team team = getTeam(idTeam);
        Optional<List<Collaborator>> collaboratorOptional = collaboratorRepository.findAllCollaboratorByTeamId(team.getId());
        return collaboratorOptional.orElseGet(ArrayList::new);
    }


    @Transactional
    public List<Team> getAllTeamByOrganization(Integer idOrganization) {
        Optional<Organization> organization = organizationRepository.findById(idOrganization);

        if (organization.isPresent()) {
            Optional<List<Team>> teams = repository.findAllByOrganizationId(idOrganization);
            return teams.orElseGet(ArrayList::new);
        } else {
            throw new NotFoundException(Organization.class);
        }
    }


    @Transactional
    public List<Team> getTeamsByCollaborator() {
        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            Optional<List<Team>> teams = repository.findAllByCollaboratorId(((Collaborator) userService.getCurrentUser().get()).getId());
            return teams.orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Collaborator.class);
    }


    public void deleteTeam(Integer idTeam) {
        Team team = getTeam(idTeam);
        Optional<List<OrganizationTeam>> optional = organizationTeamRepository.findAllByTeamId(team.getId());
        optional.ifPresent(organizationTeamRepository::deleteAll);
        repository.deleteById(idTeam);
    }


    @Transactional
    public List<Collaborator> addCollaborator(Integer idTeam, List<CollaboratorRequest> collaboratorRequests) {

        Team team = getTeam(idTeam);

        Optional<Organization> orgOptional = organizationRepository.findOrganizationByTeamId(team.getId());

        if (orgOptional.isPresent()) {
            List<Collaborator> userList = collaboratorRequests.stream()
                    .map(e -> {
                        Optional<Collaborator> collaborator = collaboratorRepository.findCollaboratorByUserEmail(e.getEmail());

                        if (collaborator.isPresent()) {
                            return collaborator.get();
                        } else {
                            authService.registerCollaborator(e);
                        }

                        return collaboratorRepository.findCollaboratorByUserEmail(e.getEmail()).get();
                    }).collect(Collectors.toList());


            userList.forEach(c -> {
                if (organizationTeamRepository.findByTeamIdAndCollaboratorId(idTeam, c.getId()).isEmpty()) {
                    OrganizationTeam organizationTeam = OrganizationTeam.builder()
                            .organization(orgOptional.get())
                            .team(team)
                            .collaborator(c)
                            .build();
                    organizationTeamRepository.save(organizationTeam);
                }
            });

            Optional<List<Collaborator>> colaboradorList = collaboratorRepository.findAllCollaboratorByTeamId(team.getId());

            return colaboradorList.orElseGet(ArrayList::new);
        }

        throw new NotFoundException(Organization.class);
    }


    public List<Collaborator> deleteCollaborators(Integer idTeam, List<String> emails) {
        Team team = getTeam(idTeam);

        Optional<List<Collaborator>> collaboratorList = collaboratorRepository.findAllCollaboratorByTeamId(team.getId());

        if (collaboratorList.isPresent()) {
            List<Collaborator> removeCollaborators = emails.stream().map(e -> {
                Optional<Collaborator> collaborator = collaboratorRepository.findCollaboratorByUserEmail(e);
                return collaborator.orElse(null);
            }).filter(Objects::nonNull).collect(Collectors.toList());

            removeCollaborators.forEach(c -> {
                Optional<OrganizationTeam> organizacaoEquipeOptional = organizationTeamRepository
                        .findByTeamIdAndCollaboratorId(idTeam, c.getId());
                organizacaoEquipeOptional.ifPresent(organizationTeamRepository::delete);
            });

            return collaboratorRepository.findAllCollaboratorByTeamId(idTeam).orElseGet(ArrayList::new);
        } else {
            throw new EmptyException(Team.class);
        }
    }

}
