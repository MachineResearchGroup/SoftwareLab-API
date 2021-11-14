package com.swl.populator.management;

import com.swl.models.management.Address;
import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.user.Collaborator;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.user.PopulatorCollaborator;
import com.swl.populator.util.CNPJGeneratorUtil;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.OrganizationRepository;
import com.swl.repository.OrganizationTeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Slf4j
@Component
@AllArgsConstructor
public class PopulatorOrganization {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationTeamRepository organizationTeamRepository;

    @Autowired
    private PopulatorCollaborator populatorCollaborator;

    @Autowired
    private PopulatorTeam populatorTeam;


    public Organization create() {
        return Organization.builder()
                .name(FakerUtil.getInstance().faker.company().name())
                .cnpj(getCnpj())
                .address(Address.builder()
                        .street(FakerUtil.getInstance().faker.address().streetName())
                        .number(FakerUtil.getInstance().faker.address().streetAddressNumber())
                        .city(FakerUtil.getInstance().faker.address().cityName())
                        .state(FakerUtil.getInstance().faker.address().state())
                        .complement(FakerUtil.getInstance().faker.address().streetAddressNumber())
                        .build())
                .build();
    }


    private String getCnpj() {
        String cnpj = CNPJGeneratorUtil.generate();
        while (organizationRepository.findOrganizationByCnpj(cnpj).isPresent()) {
            cnpj = CNPJGeneratorUtil.generate();
        }
        return cnpj;
    }


    public Organization save() {
        Organization organization = this.create();

        Address address = organization.getAddress();
        organization.setAddress(null);

        organization = organizationRepository.save(organization);

        address.setOrganization(organization);
        organization.setAddress(address);

        organization = organizationRepository.save(organization);

        return organization;
    }


    public void save(PopulatorConfig config) {
        IntStream.range(0, config.getNumberOrganizations()).forEach(f -> {
            log.info("[" + (f + 1) + " | " + config.getNumberOrganizations() + "]");
            Organization organization = this.save();

            Integer idOrg = organization.getId();

            Collaborator supervisor = populatorCollaborator.saveSupervisor(idOrg);

            organization.setSupervisor(supervisor);
            organization = organizationRepository.save(organization);

            organizationTeamRepository.save(OrganizationTeam.builder()
                    .organization(organization)
                    .collaborator(supervisor)
                    .build());

            Organization finalOrganization = organization;
            IntStream.range(0, config.getNumberTeams())
                    .forEach(i -> populatorTeam.save(finalOrganization, config));
        });
    }

}
