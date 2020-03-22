package com.cafjo.carjo.api.error;

import org.springframework.http.HttpStatus;

/**
 * @author Khalid Elshafie <abolkog@gmail.com>
 * @Created 18/09/2018 10:37 PM.
 */
public class ThreadException extends ApiBaseException {

    public ThreadException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
