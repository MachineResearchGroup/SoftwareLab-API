package com.swl.repository;

import com.swl.models.Address;
import com.swl.models.Organization;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private AddressRepository repository;


    @Test
    public void createAddess() {
        Address address = BuilderUtil.buildAddress();
        address.setOrganization(organizacaoRepository.save(BuilderUtil.buildOrganization()));

        repository.save(address);
        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteAddess() {
        Address address = BuilderUtil.buildAddress();
        address.setOrganization(organizacaoRepository.save(BuilderUtil.buildOrganization()));

        address = repository.save(address);
        repository.delete(address);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchAddess() {
        Address address = BuilderUtil.buildAddress();
        address.setOrganization(organizacaoRepository.save(BuilderUtil.buildOrganization()));

        address = repository.save(address);
        Optional<Address> enderecoFound = repository.findById(address.getId());

        Assertions.assertTrue(enderecoFound.isPresent());
    }


    @Test
    public void updateAddess() {
        Address address = BuilderUtil.buildAddress();
        Organization organization = organizacaoRepository.save(BuilderUtil.buildOrganization());
        address.setOrganization(organization);

        address = repository.save(address);

        Address addressUpdate = BuilderUtil.buildAddress();
        addressUpdate.setId(address.getId());
        addressUpdate.setOrganization(organization);
        addressUpdate.setStreet("Rua update");

        repository.save(addressUpdate);
        Optional<Address> responseFinal = repository.findById(address.getId());
        Assertions.assertEquals(responseFinal.get().getStreet(), "Rua update");
    }

}
