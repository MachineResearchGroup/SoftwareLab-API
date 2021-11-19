package com.swl.exceptions;

import com.swl.exceptions.contract.MessageException;
import com.swl.payload.response.ApiErrorResponse;
import com.swl.payload.response.ErrorResponse;
import com.swl.payload.response.MessageResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@ControllerAdvice
@AllArgsConstructor
public class ApplicationControllerAdvice {

    private static final String UNKNOWN_ERROR_KEY = "unknown.error";

    private static final Logger logger = LoggerFactory.getLogger(ApplicationControllerAdvice.class);
    private final MessageSource messageSource;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handlerMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        logger.error("Exception {}, Message: {}", exception.getClass().getName(), exception.getMessage());
        Set<ErrorResponse> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> buildError(error.getCode(), error.getDefaultMessage()))
                .collect(Collectors.toSet());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(baseErrorBuilder(HttpStatus.BAD_REQUEST, errors));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<?> handlerBaseException(Throwable exception) {
        logger.error("Exception {}", exception.getClass().getName());

        MessageException messageException = (MessageException) exception;
        ErrorResponse error = buildError(messageException.getExceptionKey(),
                bindExceptionKeywords(messageException.getMapDetails(), messageException.getExceptionKey()));

        Set<ErrorResponse> errors = Set.of(error);
        ApiErrorResponse apiErrorDto = baseErrorBuilder(getResponseStatus(exception), errors);

        return ResponseEntity
                .status(getResponseStatus(exception))
                .body(new MessageResponse("An error occurred during the request!", apiErrorDto));
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handlerMethodThrowable(Throwable exception) {
        logger.error("Exception {}, Message: {}", exception.getClass().getName(), exception.getMessage());
        Set<ErrorResponse> errors = Set.of(buildError(UNKNOWN_ERROR_KEY, exception.getMessage()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(baseErrorBuilder(HttpStatus.INTERNAL_SERVER_ERROR, errors));
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    protected ResponseEntity<?> handleConflictException(HttpClientErrorException exception, WebRequest request) {

        ErrorResponse error = buildError(exception.getMessage());
        ApiErrorResponse apiErrorResponse = baseErrorBuilder(getResponseStatus(exception), Set.of(error));

        return ResponseEntity
                .status(getResponseStatus(exception))
                .body(new MessageResponse("An error occurred during the request!", apiErrorResponse));
    }


    private ErrorResponse buildError(String key, String message) {
        return new ErrorResponse(key, message);
    }


    private ErrorResponse buildError(String message) {
        return new ErrorResponse(message);
    }


    private ApiErrorResponse baseErrorBuilder(HttpStatus httpStatus, Set<ErrorResponse> errorList) {
        return ApiErrorResponse.builder()
                .timestamp(LocalDate.now())
                .code(httpStatus.name())
                .status(httpStatus.value())
                .errors(errorList)
                .build();
    }


    private String bindExceptionKeywords(Map<String, Object> keywords, String exceptionKey) {
        String message = messageSource.getMessage(exceptionKey, null, LocaleContextHolder.getLocale());
        return Objects.nonNull(keywords) ? new StrSubstitutor(keywords).replace(message) : message;
    }


    private HttpStatus getResponseStatus(Throwable exception) {
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        if (Objects.isNull(exception.getClass().getAnnotation(ResponseStatus.class))) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return responseStatus.value();
    }
}
