package com.study.model.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("이미지의 ")
public class ImageTest {

    @Test
    void create_image() {
        Image image = new Image.Builder()
                .imageName("test.jpg")
                .imageSize(127904)
                .build();
    }

    @Nested
    @DisplayName("이름은 ")
    class name {

        @DisplayName("유효한(png, jpeg, bmp 등) 확장자여야만 한다.")
        @ParameterizedTest
        @ValueSource(strings = {"test.png", "test.jpeg", "test.bmp", "test.gif", "test.jpg"})
        void valid_image_name_extension(String nameExtension) {
            Image image = new Image.Builder()
                    .imageName(nameExtension)
                    .imageSize(127904)
                    .build();
        }

        @DisplayName("유효하지 않은 확장자일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"test.exe", "test.com", "test.bat", "test.ti", "test.abc"})
        void invalid_image_name_extension(String nameExtension) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        new Image.Builder()
                                .imageName(nameExtension)
                                .imageSize(127904)
                                .build();
                    })
                    .withMessageMatching("유효하지 않은 확장자입니다.");
        }
    }

    @Nested
    @DisplayName("크기는")
    class size {

        @DisplayName("10_485_760 byte 미만이여만 한다.")
        @ParameterizedTest
        @ValueSource(ints = {1_048_576, 1_048_577, 10_485_758, 10_485_759})
        void valid_image_size(long imageSize) {
            Image image = new Image.Builder()
                    .imageName("test.png")
                    .imageSize(imageSize)
                    .build();
        }

        @DisplayName("10_485_760 byte 이상일 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {10_485_760, 10_485_761, 10_485_762, 12_000_000})
        void invalid_image_size(long imageSize) {

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> {
                        Image image = new Image.Builder()
                                .imageName("test.png")
                                .imageSize(imageSize)
                                .build();
                    })
                    .withMessageMatching("이미지의 크기가 10_485_760 byte 이상일 수 없습니다.");

        }

    }
}
