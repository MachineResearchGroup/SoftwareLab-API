package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class RequirementRequest {

    @Size(max = 500)
    private String description;

    private String category;

    private String subcategory;

    private Integer idProject;
}
