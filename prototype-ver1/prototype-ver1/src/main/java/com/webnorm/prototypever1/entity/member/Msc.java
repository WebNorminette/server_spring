package com.webnorm.prototypever1.entity.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor
public class Msc {
    private boolean email;
    private boolean sms;
}
