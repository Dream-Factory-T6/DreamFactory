package com.DreamFactory.DF.review.exceptions;

import com.DreamFactory.DF.exceptions.AppException;

public class ReviewNotFoundByIdException extends AppException {
    public ReviewNotFoundByIdException (Long id) {
        super("Review with id " + id + " not found!");
    }
}
