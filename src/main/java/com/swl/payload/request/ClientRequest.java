package com.swl.payload.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Builder
public class ClientRequest {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    private String corporateName;

    private List<String> segments;

}
