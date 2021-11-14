package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class BoardRequest {

    @NonNull
    private String title;

    @NonNull
    private Integer idProject;

}
