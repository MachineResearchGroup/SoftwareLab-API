package com.swl.exceptions.business;

import com.swl.exceptions.BaseRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseRuntimeException {
    private static final String KEY = "not.found";

    public NotFoundException(String value) {
        super(Map.of("value", value));
    }

    public NotFoundException(Class<?> typeClass) {
        super(Map.of("value", typeClass.getSimpleName()));
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
