package com.swl.populator.user;

import com.swl.models.enums.FunctionEnum;
import com.swl.models.people.Collaborator;
import com.swl.populator.util.RegisterGeneratorUtil;
import com.swl.repository.CollaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PopulatorCollaborator {

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private PopulatorUser populatorUser;


    public Collaborator create(Integer idOrg, boolean isSupervisor, boolean isPo) {
        Optional<List<Collaborator>> collaborator = collaboratorRepository.findLastCollaboratorByRegister(idOrg);

        return Collaborator.builder()
                .register(collaborator.get().size() == 0 ? "202120004" :
                        RegisterGeneratorUtil.gerarNumero(collaborator.get().get(0).getRegister()))
                .function(isSupervisor ? FunctionEnum.ROLE_PMO.name() : isPo ? FunctionEnum.ROLE_PO.name() : FunctionEnum.ROLE_DEV.name())
                .build();
    }

    public Collaborator save(Collaborator collaborator, Collaborator supervisor) {
        collaborator.setSupervisor(supervisor);
        collaborator.setUser(populatorUser.save());
        return collaboratorRepository.save(collaborator);
    }


    public Collaborator saveSupervisor(Integer idOrg) {
        return save(this.create(idOrg, true, false), null);
    }


    public Collaborator savePMO(Integer idOrg, Collaborator supervisor) {
        return save(this.create(idOrg, true, false), supervisor);
    }


    public Collaborator savePO(Integer idOrg, Collaborator supervisor) {
        return save(this.create(idOrg, false, true), supervisor);
    }


    public Collaborator saveDEV(Integer idOrg, Collaborator supervisor) {
        return save(this.create(idOrg, false, false), supervisor);
    }
}
