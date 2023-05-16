package com.study.model.board;

import java.time.LocalDateTime;

public class RegDate {

    private LocalDateTime dateTime = LocalDateTime.now();

    public RegDate(LocalDateTime dateTime) {
        if( dateTime.isBefore(LocalDateTime.now().minusMinutes(1)) ) {
            throw new IllegalArgumentException("등록 시간이 초과되었습니다.");
        }
        this.dateTime = dateTime;
    }
}
