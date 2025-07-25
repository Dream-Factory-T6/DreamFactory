package com.DreamFactory.DF.destination.exceptions;

import com.DreamFactory.DF.exceptions.AppException;

public class UnauthorizedAccessException extends AppException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    public UnauthorizedAccessException(Long destinationId) {
        super("You are not authorized to access destination with id " + destinationId);
    }
}
