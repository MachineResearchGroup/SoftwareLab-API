package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RedactionSheduleRequest {

    private LocalDateTime initDate;

    private LocalDateTime endDate;

    private Integer idProject;

    private Integer idCollaborator;
}
