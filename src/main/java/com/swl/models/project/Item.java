package com.swl.models.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Item {

    @NotNull
    private String title;

    @Size(max = 500)
    private String description;

    private LocalDate endDate;

    private Integer priority;
}
