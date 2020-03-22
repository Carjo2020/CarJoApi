package com.cafjo.carjo.api.error;

import org.springframework.http.HttpStatus;

/**
 * @author Khalid Elshafie <abolkog@gmail.com>
 * @Created 18/09/2018 10:37 PM.
 */
public class BadRequestException extends ApiBaseException {

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
