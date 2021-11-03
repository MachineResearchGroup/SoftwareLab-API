package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class TeamRequest {

    @NonNull
    private String name;

    @NonNull
    private Integer idOrganization;

    private String supervisorEmail;

}
