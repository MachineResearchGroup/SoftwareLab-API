package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RedactionSheduleRequest {

    private String initDate;

    private String endDate;

    private Integer idProject;

    private Integer idCollaborator;
}
