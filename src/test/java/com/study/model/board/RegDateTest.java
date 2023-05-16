package com.study.model.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("글 등록일자는")
public class RegDateTest {

    @Test
    void create() {
        RegDate regDate = new RegDate(LocalDateTime.now());
    }
    
    @DisplayName("현재시간에서 1분을 뺀 시간보다 작다면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {61, 62, 100, 200, 300})
    void invalid_dateTime_shorter_than(int seconds) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new RegDate(LocalDateTime.now().minusSeconds(seconds));
                })
                .withMessageMatching("등록 시간이 초과되었습니다.");

    }
}
