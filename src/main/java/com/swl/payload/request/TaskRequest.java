package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class TaskRequest {

    @NonNull
    private String title;

    @Size(max = 500)
    private String description;

    private LocalDate endDate;

    private Integer priority;

    private Integer estimate;

    private Integer duration;

    private Integer idSuperTask;

    private Integer idColumn;
}
