package com.swl.payload.request;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;


@Data
@Builder
public class OrganizationRequest {

    @NonNull
    private String name;

    @Size(max = 14)
    private String cnpj;

    private AddessRequest address;

}
