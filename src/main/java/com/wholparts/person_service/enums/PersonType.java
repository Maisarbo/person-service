package com.wholparts.person_service.enums;

public enum PersonType {

    CUSTOMER("C"),
    SUPPLIER("S"),
    EMPLOYEE("E"),
    CARRIER("T");

    private final String prefix;

    PersonType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }


}