package com.swl.payload.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Builder
public class OrganizationRequest {

    @NotNull
    private String name;

    @Size(max = 14)
    private String cnpj;

    private AddessRequest address;

}
