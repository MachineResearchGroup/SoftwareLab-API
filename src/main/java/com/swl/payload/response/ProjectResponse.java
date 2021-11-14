package com.swl.payload.response;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ProjectResponse {

	@NonNull
	private String name;

	@NonNull
	private Integer idTeam;

	private String description;

	private String repository;

}
