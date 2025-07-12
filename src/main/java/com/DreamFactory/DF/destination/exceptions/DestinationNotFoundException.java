package com.DreamFactory.DF.destination.exceptions;

public class DestinationNotFoundException extends RuntimeException {
    public DestinationNotFoundException(String message){
        super(message);
    }

    public DestinationNotFoundException(Long id) {
        super("Destination with id " + id + " not found");
    }
}
