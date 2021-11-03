package com.swl.payload.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;


@Data
@Builder
public class RegisterRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private List<String> phones;

}
