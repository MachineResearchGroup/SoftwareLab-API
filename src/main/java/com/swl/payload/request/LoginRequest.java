package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class LoginRequest {

	@NonNull
	private String email;

	@NonNull
	private String password;

}
