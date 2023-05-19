package com.study.model.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class fileNameTest {

    @Test
    void create() {
        fileName imageName = new fileName("test.png");

        assertThat(imageName).isEqualTo(new fileName("test.png"));
    }

    @DisplayName("유효하지 않은 확장자일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"test.exe", "test.com", "test.bat", "test.ti", "test.abc"})
    void invalid_image_name_extension(String name) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new fileName(name);
                })
                .withMessageMatching("유효하지 않은 확장자입니다.");
    }
}
