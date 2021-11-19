package com.swl.exceptions.business;

import com.swl.exceptions.BaseRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyException extends BaseRuntimeException {
    private static final String KEY = "empty";

    public EmptyException(String value) {
        super(Map.of("value", value));
    }

    public EmptyException(Class<?> typeClass) {
        super(Map.of("value", typeClass.getSimpleName()));
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
