package com.mealtiger.backend.rest.error_handling;

import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import com.mealtiger.backend.rest.error_handling.exceptions.InvalidRequestFormatException;
import com.mealtiger.backend.rest.error_handling.exceptions.RatingOwnRecipeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles bad requests.
     * @param e the exception thrown by the api.
     * @param headers the headers to be written to the response.
     * @param status the selected response status.
     * @param request the current request.
     * @return ResponseEntity to be sent to the client.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(e, handleBadRequest(new InvalidRequestFormatException(
                        e.getBindingResult().getAllErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .reduce((a, b) ->  a + " " +  b).orElse(null)),
                request).getBody(), headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles not acceptable accept headers.
     * @param e the exception thrown by the api.
     * @param headers the headers to be written to the response.
     * @param status the selected response status.
     * @param request the current request.
     * @return ResponseEntity to be sent to the client.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String uri = getRequestURI(request);

        if(e.getMessage() == null) {
            Map<String, String> errorMessage = createErrorMessage(
                    406,
                    "The requested image type is not served!",
                    uri
            );
            return handleExceptionInternal(e, errorMessage, headers, HttpStatus.NOT_ACCEPTABLE, request);
        }

        Map<String, String> errorMessage = createErrorMessage(
                406,
                e.getMessage(),
                uri
        );
        return handleExceptionInternal(e, errorMessage, headers, HttpStatus.NOT_ACCEPTABLE, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    // CUSTOM EXCEPTIONS

    /**
     * Handles exception when resource is not found.
     * @param e the exception thrown by the api.
     * @param request the current request.
     * @return ResponseEntity to be sent to the client.
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException e, WebRequest request) {
        String uri = getRequestURI(request);

        HttpHeaders headers = HttpHeaders.EMPTY;

        Map<String, String> errorMessage = e.getMessage() == null ?
                createErrorMessage(
                        404,
                        "Resource not found!",
                        uri
                ) :
                createErrorMessage(
                        404,
                        e.getMessage(),
                        uri
                );

        return handleExceptionInternal(e, errorMessage, headers, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles bad requests.
     * @param e the exception thrown by the api.
     * @param request the current request.
     * @return ResponseEntity to be sent to the client.
     */
    @ExceptionHandler(value = { InvalidRequestFormatException.class } )
    protected ResponseEntity<Object> handleBadRequest(RuntimeException e, WebRequest request) {
        String uri = getRequestURI(request);

        HttpHeaders headers = HttpHeaders.EMPTY;

        Map<String, String> errorMessage = e.getMessage() == null ?
                createErrorMessage(
                        400,
                        "Invalid request!",
                        uri
                ) :
                createErrorMessage(
                        400,
                        e.getMessage(),
                        uri
                );

        return handleExceptionInternal(e, errorMessage, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { RatingOwnRecipeException.class })
    protected ResponseEntity<Object> handleRatingOwnRecipe(RuntimeException e, WebRequest request) {
        String uri = getRequestURI(request);

        HttpHeaders headers = HttpHeaders.EMPTY;

        Map<String, String> errorMessage = e.getMessage() == null ?
                createErrorMessage(
                        403,
                        "You may not rate your own recipe!",
                        uri
                ) :
                createErrorMessage(
                        403,
                        e.getMessage(),
                        uri
                );

        return handleExceptionInternal(e, errorMessage, headers, HttpStatus.FORBIDDEN, request);
    }

    // HELPER METHOD

    /**
     * Creates a uniform error message.
     * @param status the selected response status.
     * @param error the error message.
     * @param path the path where the error occurred.
     * @return the error message as a map.
     */
    private Map<String, String> createErrorMessage(int status, String error, String path) {
        Map<String, String> errorMessage = new HashMap<>();

        errorMessage.put("timestamp", Timestamp.from(Instant.now()).toString());
        errorMessage.put("status", String.valueOf(status));
        errorMessage.put("error", error);
        errorMessage.put("path", path);

        return Collections.unmodifiableMap(errorMessage);
    }

    /**
     * Assembles the request uri from WebRequest.
     * @return URI (with query string) of the requested resource.
     */
    private String getRequestURI(WebRequest request) {
        String uri = ((ServletWebRequest)request).getRequest().getRequestURI();
        String queryString = ((ServletWebRequest)request).getRequest().getQueryString();

        if (queryString != null) {
            return uri.concat("?").concat(queryString);
        } else {
            return uri;
        }
    }

}
