package com.swl.util;

import com.swl.payload.response.ErrorResponse;
import org.modelmapper.ModelMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public final class ModelUtil {

    private static ModelUtil instance;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ModelMapper mapper = new ModelMapper();

    public static synchronized ModelUtil getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ModelUtil();
        }
        return instance;
    }


    public void map(Object source, Object destination) {
        mapper.map(source, destination);
    }


    public ErrorResponse validate(Object obj) {
        List<ErrorResponse> errors = validator.validate(obj).stream()
                .map(r -> new ErrorResponse(r.getPropertyPath().toString(), r.getMessage()))
                .collect(Collectors.toList());
        if(!errors.isEmpty()) return errors.get(0);
        else return null;
    }

}
