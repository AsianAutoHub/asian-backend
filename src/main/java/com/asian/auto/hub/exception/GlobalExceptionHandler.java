package com.asian.auto.hub.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.asian.auto.hub.apiresponse.ApiResponse;
import com.asian.auto.hub.dto.ApiError;
import com.asian.auto.hub.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	  private  final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	    @ExceptionHandler({
	            UsernameNotFoundException.class,
	            BadCredentialsException.class,
	            CredentialsExpiredException.class,
	            DisabledException.class

	    })
	    public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request) {
	        logger.info("Exception  : {}", e.getClass().getName());
	        var apiError=ApiError.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage(), request.getRequestURI());
	        return ResponseEntity.badRequest().body(apiError);

	    }

//	    //resource not found exception handler :: method
//	    @ExceptionHandler(ResourceNotFoundException.class)
//	    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
//	        ErrorResponse internalServerError = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, 404);
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
//	    }


	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
	        ErrorResponse internalServerError = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, 400);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internalServerError);
	    }

	
	
	@ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotFound(InvalidDataException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(ApiResponse.notFound(ex.getMessage()));
  }
	
	
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound(ex.getMessage()));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.conflict(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(errorMsg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.internalError(ex.getMessage()));
    }
}
