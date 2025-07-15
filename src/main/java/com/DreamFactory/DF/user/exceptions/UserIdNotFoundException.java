package com.DreamFactory.DF.user.exceptions;

import com.DreamFactory.DF.exceptions.AppException;

public class UserIdNotFoundException extends AppException {
    public UserIdNotFoundException(Long id) {
        super("This user id: " + id + " not found.");
    }
}
