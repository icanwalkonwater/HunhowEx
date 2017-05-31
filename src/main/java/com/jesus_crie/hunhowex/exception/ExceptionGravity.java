package com.jesus_crie.hunhowex.exception;

public enum ExceptionGravity {
    FATAL("FATAL"),
    ERROR("Error"),
    WARNING("Warning");

    private String s;

    ExceptionGravity(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
