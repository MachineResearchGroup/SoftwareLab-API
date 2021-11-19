package com.swl.exceptions.business;

import com.swl.exceptions.BaseRuntimeException;
import com.swl.payload.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class BlockedAdditionException extends BaseRuntimeException {
    private static final String KEY = "blocked.addition";

    public BlockedAdditionException(String value) {
        super(Map.of("value", value));
    }

    public BlockedAdditionException(Class<?> typeClass) {
        super(Map.of("value", typeClass.getSimpleName()));
    }

    public BlockedAdditionException(ErrorResponse errorResponse) {
        super(Map.of("value", errorResponse.getKey() + errorResponse.getError()));
    }

    @Override
    public String getExceptionKey() {
        return KEY;
    }
}
