package org.lovesoa.callerservice.exception.responses;

import java.util.List;

public class ErrorResponse {
    private String errorCode;
    private String message;
    private List<String> details;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorResponse(String errorCode, String message, List<String> details) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }

    // геттеры и сеттеры
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}