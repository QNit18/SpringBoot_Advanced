package com.qnit18.springtutorial.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1000, "Uncategorized error"),
    INVALID_KEY(1001, "Uncategorized error"),
    USER_EXISTED(1002, "User existed"),
    USER_NOT_EXISTED(1003, "User not existed"),
    USERNAME_INVALID(1004, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1005, "Password must be at least 8 characters"),
    UNAUTHENTICATED(1006, "Unauthenticate!"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
