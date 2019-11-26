package com.hflw.vasp.enums;

public enum StatusEnum {

    FALSE(0), TRUE(-1);

    private int status;

    StatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
