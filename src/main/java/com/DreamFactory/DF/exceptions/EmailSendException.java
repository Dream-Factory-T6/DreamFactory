package com.DreamFactory.DF.exceptions;

public class EmailSendException extends AppException {
    public EmailSendException(String message) {
        super(message);
    }
    public EmailSendException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}