package com.swl.populator.project;

import com.swl.models.project.Label;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PopulatorLabel {


    @Autowired
    private LabelRepository labelRepository;


    public Label create() {
        return Label.builder()
                .name(FakerUtil.getInstance().faker.funnyName().name())
                .description(FakerUtil.getInstance().faker.shakespeare().asYouLikeItQuote())
                .color(FakerUtil.getInstance().faker.color().name())
                .build();
    }


    public Label save() {
        return labelRepository.save(create());
    }

}
