package com.swl.populator.project;

import com.swl.models.project.Document;
import com.swl.models.project.Epic;
import com.swl.models.project.Event;
import com.swl.models.project.Project;
import com.swl.models.user.Client;
import com.swl.models.user.Collaborator;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.ClientRepository;
import com.swl.repository.EpicRepository;
import com.swl.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class PopulatorEvent {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EventRepository eventRepository;


    public Event create() {

        return Event.builder()
                .name(FakerUtil.getInstance().faker.lorem().word())
                .description(FakerUtil.getInstance().faker.shakespeare().kingRichardIIIQuote())
                .date(getDate())
                .description(FakerUtil.getInstance().faker.shakespeare().hamletQuote())
                .local(FakerUtil.getInstance().faker.internet().url())
                .build();
    }


    private LocalDateTime getDate(){
        try {
            return FakerUtil.getInstance().faker.date().between(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2019"),
                    new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2021")).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (ParseException ignored) {
        }
        return null;
    }


    public Event save(Project project, List<Collaborator> collaborator, List<Client> clients, Document document) {
        Event event = create();

        event.setProject(project);
        event.setCollaborators(collaborator);
        event.setClients(clients);
        event.setDocuments(new ArrayList<>());
        event.getDocuments().add(document);

        return eventRepository.save(event);
    }

}
