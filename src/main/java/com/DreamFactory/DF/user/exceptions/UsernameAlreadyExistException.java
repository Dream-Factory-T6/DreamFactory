package com.DreamFactory.DF.user.exceptions;

import com.DreamFactory.DF.exceptions.AppException;

public class UsernameAlreadyExistException extends AppException {
    public UsernameAlreadyExistException(String username) {
        super("This username: " + username + " already exist.");
    }
}
