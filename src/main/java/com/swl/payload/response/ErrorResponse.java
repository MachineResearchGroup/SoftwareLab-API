package com.swl.payload.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class ErrorResponse {

    private LocalDate timestamp;

    private String status;

    private List<String> errors;

}
