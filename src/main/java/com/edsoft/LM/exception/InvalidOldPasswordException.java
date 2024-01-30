package com.edsoft.LM.exception;

public class InvalidOldPasswordException extends RuntimeException{
    public InvalidOldPasswordException() {
        super("The old password did not match the entered password.");
    }
}
