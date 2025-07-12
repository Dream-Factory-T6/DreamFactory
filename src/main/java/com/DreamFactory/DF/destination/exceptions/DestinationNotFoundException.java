package com.DreamFactory.DF.destination.exceptions;

import com.DreamFactory.DF.exceptions.AppException;

public class DestinationNotFoundException extends AppException {
    public DestinationNotFoundException(String message) {
        super(message);
    }

    public DestinationNotFoundException(Long id) {
        super("Destination with id " + id + " not found");
    }
}
