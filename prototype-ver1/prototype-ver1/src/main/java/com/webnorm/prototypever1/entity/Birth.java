package com.webnorm.prototypever1.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Birth {
    private int year;
    private int month;
    private int day;

    public Birth(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
