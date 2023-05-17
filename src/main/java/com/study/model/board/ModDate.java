package com.study.model.board;

import java.time.LocalDateTime;

public class ModDate {

    private LocalDateTime localDatetime = LocalDateTime.now();

    public ModDate(LocalDateTime localDateTime) {
        this.localDatetime = localDateTime;
    }
}
