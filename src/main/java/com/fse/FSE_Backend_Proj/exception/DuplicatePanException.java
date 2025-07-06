package com.fse.FSE_Backend_Proj.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)  // Sends 409 Conflict to frontend
public class DuplicatePanException extends RuntimeException {

    public DuplicatePanException(String message) {
        super(message);
    }
}
