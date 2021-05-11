package com.waracle.cakemgr.exceptions;

public class CakeMgrException extends RuntimeException {
    public CakeMgrException(String message) {
        super(message);
    }

    public CakeMgrException(String message, Throwable cause) {
        super(message, cause);
    }

    public CakeMgrException(Throwable cause) {
        super(cause);
    }
}
