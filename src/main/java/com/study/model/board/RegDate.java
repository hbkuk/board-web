package com.study.model.board;

import java.time.LocalDateTime;

public class RegDate {

    private LocalDateTime dateTime = LocalDateTime.now();

    public RegDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
