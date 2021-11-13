package com.swl.populator.user;

import com.swl.models.user.Client;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class PopulatorClient {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PopulatorUser populatorUser;

    public Client create() {
        return Client.builder()
                .corporateName(FakerUtil.getInstance().faker.company().name())
                .segments(new ArrayList<>(Collections.singletonList(FakerUtil.getInstance().faker.company().profession())))
                .build();
    }


    public Client save() {
        Client client = create();
        client.setUser(populatorUser.save());
        return clientRepository.save(client);
    }
}
