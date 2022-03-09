/*
 * package com.utils;
 * 
 * import org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.ControllerAdvice; import
 * org.springframework.web.bind.annotation.ExceptionHandler; import
 * org.springframework.web.context.request.WebRequest; import
 * org.springframework.web.servlet.mvc.method.annotation.
 * ResponseEntityExceptionHandler;
 * 
 * @ControllerAdvice public class GlobalErrorHandler extends
 * ResponseEntityExceptionHandler {
 * 
 * @ExceptionHandler(Exception.class) public ResponseEntity<Object>
 * handleAnyException(Exception ex, WebRequest request) { String bodyOfResponse
 * = "Failure,Something went wrong!"; return
 * ResponseEntity.internalServerError().body(bodyOfResponse+ex.getMessage()); }
 * }
 */