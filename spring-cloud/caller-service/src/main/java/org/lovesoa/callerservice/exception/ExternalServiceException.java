package org.lovesoa.callerservice.exception;

import java.util.List;

public class ExternalServiceException extends RuntimeException {
    private final int status;
    private final List<String> details;

    public ExternalServiceException(int status, String message, List<String> details) {
        super(message);
        this.status = status;
        this.details = details;
    }

    public ExternalServiceException(int status, String message) {
        this(status, message, List.of(message));
    }

    public int getStatus() {
        return status;
    }

    public List<String> getDetails() {
        return details;
    }
}
