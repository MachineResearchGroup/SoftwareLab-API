package com.swl.payload.request;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ProjectRequest {

	@NonNull
	private String name;

	@NonNull
	private Integer idTeam;

	private String description;

	private String repository;

}
