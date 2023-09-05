package com.webnorm.prototypever1.util;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailMessage {
    private String to;
    private String subject;
    private String message;
    private String name;

    @Builder
    public EmailMessage(String to, String subject, String message, String name) {
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.name = name;
    }
}
