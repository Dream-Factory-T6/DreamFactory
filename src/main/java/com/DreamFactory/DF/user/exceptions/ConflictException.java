package com.DreamFactory.DF.user.exceptions;

import com.DreamFactory.DF.exceptions.AppException;

public class ConflictException extends AppException {
  public ConflictException() {
    super("A conflict has arisen, username or email already exists");
  }
}
