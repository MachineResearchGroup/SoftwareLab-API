package com.swl.exceptions.business;

import com.swl.exceptions.BaseRuntimeException;
import com.swl.payload.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidFieldException extends BaseRuntimeException {
    private static final String KEY = "invalid.field";

    public InvalidFieldException(String value) {
        super(Map.of("value", value));
    }

    public InvalidFieldException(Class<?> typeClass) {
        super(Map.of("value", typeClass.getSimpleName()));
    }

    public InvalidFieldException(ErrorResponse errorResponse) {
        super(Map.of("value", errorResponse.getKey() + errorResponse.getError()));
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
