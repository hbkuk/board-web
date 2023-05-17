package com.study.model.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ImageNameTest {

    @Test
    void create() {
        ImageName imageName = new ImageName("test.png");

        assertThat(imageName).isEqualTo(new ImageName("test.png"));
    }

    @DisplayName("유효하지 않은 확장자일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"test.exe", "test.com", "test.bat", "test.ti", "test.abc"})
    void invalid_image_name_extension(String name) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new ImageName(name);
                })
                .withMessageMatching("유효하지 않은 확장자입니다.");
    }
}
