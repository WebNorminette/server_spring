package com.webnorm.prototypever1.exception.exceptions;


public class MemberEmailDuplicateException extends IllegalStateException {
    public MemberEmailDuplicateException() {
        super();
    }

    public MemberEmailDuplicateException(String msg) {
        super(msg);
    }

    public MemberEmailDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberEmailDuplicateException(Throwable cause) {
        super(cause);
    }

}
