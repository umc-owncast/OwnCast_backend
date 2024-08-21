package com.umc.owncast.common.exception;

import com.umc.owncast.common.response.ApiResponse;
import com.umc.owncast.common.response.ErrorReasonDTO;
import com.umc.owncast.common.response.status.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));
        log.error("Constraint violation exception occurred: ", e);
        return handleExceptionInternalConstraint(e, ErrorCode.valueOf(errorMessage), HttpHeaders.EMPTY, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("IllegalArgumentException - " + e.getMessage());
        ApiResponse<String> body = ApiResponse.ofFailure(ErrorCode._BAD_REQUEST, "잘못된 요청입니다.");
        return this.handleExceptionInternal(e, body, headers, status, request);
        /*
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
            errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
        });
        return handleExceptionInternalArgs(e, HttpHeaders.EMPTY, ErrorCode.valueOf("BAD_REQUEST"), request, errors);
        */
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Object[] args = new Object[]{ex.getPropertyName(), ex.getValue()};
        log.error("TypeMismatchException - Failed to convert '" + args[0] + "' with value: '" + args[1] + "'");
        ApiResponse<String> body = ApiResponse.ofFailure(ErrorCode._BAD_REQUEST, "요청 파라미터가 잘못되었습니다");
        return this.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e, WebRequest request) {
        log.error("Unhandled exception occurred: ", e);
        return handleExceptionInternalFalse(e, ErrorCode._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request, e.getMessage());
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException generalException, HttpServletRequest request) {
        log.error("General exception occurred: ", generalException);
        ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDTO reason, HttpHeaders headers, HttpServletRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null);
        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(e, body, headers, reason.getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorCode errorCommonStatus, HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorPoint);
        return super.handleExceptionInternal(e, body, headers, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorCode errorCommonStatus, WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorArgs);
        return super.handleExceptionInternal(e, body, headers, errorCommonStatus.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorCode errorCommonStatus, HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
        return super.handleExceptionInternal(e, body, headers, errorCommonStatus.getHttpStatus(), request);
    }
}
