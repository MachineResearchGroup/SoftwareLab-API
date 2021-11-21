package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class RedactionRequest {

    @Size(max = 500)
    private String description;

    private Integer idProject;

    private Integer idClient;
}
