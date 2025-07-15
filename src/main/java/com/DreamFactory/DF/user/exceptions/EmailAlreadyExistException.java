package com.DreamFactory.DF.user.exceptions;

import com.DreamFactory.DF.exceptions.AppException;

public class EmailAlreadyExistException extends AppException {
    public EmailAlreadyExistException(String email) {
        super("The email: " + email + " already exist");
    }
}
