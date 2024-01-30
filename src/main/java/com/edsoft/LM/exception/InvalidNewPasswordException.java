package com.edsoft.LM.exception;

public class InvalidNewPasswordException extends RuntimeException{
    public InvalidNewPasswordException() {
        super("The new password does not meet the required criteria.");
    }
}
