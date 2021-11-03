package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Data
@Builder
public class AddessRequest {

	private String street;

	private String number;

	private String complement;

	private String city;

	private String state;

}
