package com.swl.models.enums;

public enum MessageEnum {

    REGISTERED("%s registered successfully!"),
    EDITED("Successfully edited %s!"),
    FOUND("Found %s!"),
    NOT_FOUND("%s not found!"),
    DELETED("%s successfully deleted!"),
    ALREADY_USED("%s is already being used!"),
    ADDED("%s successfully added");

    String message;

    MessageEnum(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
