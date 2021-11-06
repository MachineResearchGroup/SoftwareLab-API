package com.swl.service;

import com.swl.models.user.Collaborator;
import com.swl.models.management.Team;
import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.TeamRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.TeamRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.repository.OrganizacaoRepository;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private final OrganizacaoRepository organizacaoRepository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final OrganizationTeamRepository organizationTeamRepository;


    public ResponseEntity<?> verifyTeam(TeamRequest teamRequest){
        ModelUtil modelUtil = ModelUtil.getInstance();
        Team team = new Team();

        modelUtil.map(teamRequest, team);
        List<MessageResponse> messageResponses = modelUtil.validate(team);

        if (!Objects.isNull(teamRequest.getSupervisorEmail()) &&
                collaboratorRepository.findCollaboratorByUserEmail(teamRequest.getSupervisorEmail()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Collaborator.class));
        }

        if(organizacaoRepository.findById(teamRequest.getIdOrganization()).isEmpty()){
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, Team.class));
    }


    public Team registerTeam(TeamRequest teamRequest) {
        Optional<Organization> org = organizacaoRepository.findById(teamRequest.getIdOrganization());

        if (org.isPresent()) {
            Team team;
            if (!Objects.isNull(teamRequest.getSupervisorEmail())) {
                Optional<Collaborator> colaborador = collaboratorRepository.findCollaboratorByUserEmail(teamRequest.getSupervisorEmail());

                if (colaborador.isPresent()) {
                    team = Team.builder()
                            .name(teamRequest.getName())
                            .supervisor(colaborador.get())
                            .build();
                } else{
                    return null;
                }
                team = repository.save(team);

                OrganizationTeam organizationTeam = OrganizationTeam.builder()
                        .organization(org.get())
                        .team(team)
                        .collaborator(colaborador.get())
                        .build();

                organizationTeamRepository.save(organizationTeam);
            }else {
                team = Team.builder()
                        .name(teamRequest.getName())
                        .build();

                team = repository.save(team);
            }

            OrganizationTeam organizationTeam = OrganizationTeam.builder()
                    .organization(org.get())
                    .team(team)
                    .build();

            organizationTeamRepository.save(organizationTeam);
            return team;

        } else {
            return null;
        }
    }


    public Team editTeam(Integer idTeam, TeamRequest teamRequest) {
        Optional<Team> teamAux = repository.findById(idTeam);

        if (teamAux.isPresent()) {
            teamAux.get().setName(teamRequest.getName());
            if(!Objects.isNull(teamRequest.getSupervisorEmail())) {
                Optional<Collaborator> colaborador = collaboratorRepository
                        .findCollaboratorByUserEmail(teamRequest.getSupervisorEmail());
                colaborador.ifPresent(value -> teamAux.get().setSupervisor(value));
            }

            return repository.save(teamAux.get());
        }

        return null;
    }


    public Team getTeam(Integer idTeam) {
        return repository.findById(idTeam).orElse(null);
    }


    public boolean deleteTeam(Integer idTeam) {
        if (repository.existsById(idTeam)) {
            Optional<List<OrganizationTeam>> optional = organizationTeamRepository.findAllByTeamId(idTeam);
            optional.ifPresent(organizationTeamRepository::deleteAll);

            repository.deleteById(idTeam);
            return true;
        }
        return false;
    }


    public List<Collaborator> addCollaborator(Integer idTeam, List<String> emails) {
        Optional<Team> teamOptional = repository.findById(idTeam);

        if (teamOptional.isPresent()) {
            Optional<Organization> orgOptional = organizacaoRepository.findOrganizacaoByEquipeId(teamOptional.get().getId());

            if (orgOptional.isPresent()) {
                List<Optional<Collaborator>> userList = emails.stream()
                        .map(collaboratorRepository::findCollaboratorByUserEmail)
                        .collect(Collectors.toList());


                userList.stream().filter(Optional::isPresent).forEach(c -> {
                    if(organizationTeamRepository.findByTeamIdAndCollaboratorId(idTeam, c.get().getId()).isEmpty()) {
                        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                                .organization(orgOptional.get())
                                .team(teamOptional.get())
                                .collaborator(c.get())
                                .build();
                        organizationTeamRepository.save(organizationTeam);
                    }
                });

                Optional<List<Collaborator>> colaboradorList = collaboratorRepository.findAllCollaboratorByTeamId(teamOptional.get().getId());

                return colaboradorList.orElseGet(ArrayList::new);
            }
        }
        return null;
    }


    public List<Collaborator> getCollaborators(Integer idTeam) {
        Optional<List<Collaborator>> collaboratorOptional = collaboratorRepository.findAllCollaboratorByTeamId(idTeam);
        return collaboratorOptional.orElseGet(ArrayList::new);
    }


    public List<Collaborator> deleteCollaborator(Integer idTeam, List<String> emails) {
        Optional<Team> teamOptional = repository.findById(idTeam);

        if (teamOptional.isPresent()) {
            Optional<List<Collaborator>> collaboratorList = collaboratorRepository.findAllCollaboratorByTeamId(idTeam);

            if (collaboratorList.isPresent()) {
                List<Collaborator> removeCollaborators = emails.stream().map(e -> collaboratorRepository
                        .findCollaboratorByUserEmail(e).orElseGet(null))
                        .collect(Collectors.toList());

                removeCollaborators.forEach(c -> {
                    Optional<OrganizationTeam> organizacaoEquipeOptional = organizationTeamRepository
                            .findByTeamIdAndCollaboratorId(idTeam, c.getId());
                    organizationTeamRepository.delete(organizacaoEquipeOptional.get());
                });

                return collaboratorRepository.findAllCollaboratorByTeamId(idTeam).orElseGet(ArrayList::new);
            }
        }

        return null;
    }


    public List<Team> getAllTeamByOrganization(Integer idOrganization){
        Optional<Organization> organization = organizacaoRepository.findById(idOrganization);

        return organization.map(value -> repository.findAllByOrganizationId(idOrganization)
                .orElseGet(ArrayList::new))
                .orElse(null);
    }


}
