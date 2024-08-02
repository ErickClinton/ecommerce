package com.ecommerce.eccomerce.exceptions;

public class UserExistException extends RuntimeException {

    public UserExistException(){super("User already exist");}

    public UserExistException(String message){super(message);}
}
