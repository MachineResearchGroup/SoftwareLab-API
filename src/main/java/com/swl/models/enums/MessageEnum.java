package com.swl.models.enums;

public enum MessageEnum {

    REGISTERED("%s registered successfully!"),
    NOT_REGISTERED("%s was not registered successfully!"),
    EDITED("Successfully edited %s!"),
    UNEDITED("%s has not been edited!"),
    FOUND("Found %s!"),
    NOT_FOUND("%s not found!"),
    DELETED("%s successfully deleted!"),
    NOT_DELETED("%s was not deleted!"),
    ALREADY_USED("%s is already being used!"),
    ADDED("%s successfully added"),
    NOT_ADDED("%s have not been added"),
    INVALID("Invalid field: %s"),
    VALID("Valid fields: %s"),
    ALREADY_EXISTS("%s already exists");

    String message;

    MessageEnum(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
