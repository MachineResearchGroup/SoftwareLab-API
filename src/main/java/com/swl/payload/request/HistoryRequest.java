package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class HistoryRequest {

    @NonNull
    private String title;

    @Size(max = 500)
    private String description;

    private String endDate;

    private Integer priority;

    private Integer weight;

    private Integer idColumn;

    private List<Integer> idCollaborators;
}
