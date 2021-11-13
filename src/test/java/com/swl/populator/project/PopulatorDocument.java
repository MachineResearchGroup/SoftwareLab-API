package com.swl.populator.project;

import com.swl.models.project.Document;
import com.swl.models.project.Label;
import com.swl.models.project.Project;
import com.swl.models.user.Collaborator;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.DocumentRepository;
import com.swl.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PopulatorDocument {


    @Autowired
    private DocumentRepository documentRepository;


    public Document create() {
        return Document.builder()
                .url(getUrl())
                .build();
    }


    private String getUrl(){
        boolean flag = false;
        String url = FakerUtil.getInstance().faker.company().url();

        while (documentRepository.findByUrl(url).isPresent()){
            url = flag ? FakerUtil.getInstance().faker.company().url() : FakerUtil.getInstance().faker.internet().url();
            flag = !flag;
        }

        return url;
    }


    public Document save(Project project, Collaborator collaborator) {
        Document doc = create();
        doc.setProject(project);
        doc.setCollaborator(collaborator);
        return documentRepository.save(doc);
    }

}
