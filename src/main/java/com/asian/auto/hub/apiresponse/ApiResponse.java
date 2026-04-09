package com.asian.auto.hub.apiresponse;

import com.asian.auto.hub.dto.PagedResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final int status;
    private final String message;
    private final T data;
    private final String error;
    private final LocalDateTime timestamp;

    private ApiResponse(boolean success, int status, String message, T data, String error) {
        this.success   = success;
        this.status    = status;
        this.message   = message;
        this.data      = data;
        this.error     = error;
        this.timestamp = LocalDateTime.now();
    }


    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, 200, "Success", data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, 200, message, data, null);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(true, 201, message, data, null);
    }

    public static <T> ApiResponse<T> deleted(String message) {
        return new ApiResponse<>(true, 200, message, null, null);
    }

    public static <T> ApiResponse<T> notFound(String error) {
        return new ApiResponse<>(false, 404, "Not Found", null, error);
    }

    public static <T> ApiResponse<T> badRequest(String error) {
        return new ApiResponse<>(false, 400, "Bad Request", null, error);
    }

    public static <T> ApiResponse<T> internalError(String error) {
        return new ApiResponse<>(false, 500, "Internal Server Error", null, error);
    }

    public static <T> ApiResponse<T> conflict(String error) {
        return new ApiResponse<>(false, 409, "Conflict", null, error);
    }
    
    public static <T> ApiResponse<PagedResponse<T>> paged(Page<T> page) {
      PagedResponse<T> pagedResponse = new PagedResponse<>(
          page.getContent(),
          page.getNumber(),
          page.getTotalPages(),
          page.getTotalElements(),
          page.getSize(),
          page.isFirst(),
          page.isLast()
      );
      return new ApiResponse<>(true, 200, "Success", pagedResponse, null);
  }
}