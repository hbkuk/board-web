package com.study.model.image;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Image {
    private final long imageIdx;
    private final String imageName;
    private final long imageSize;

    public Image(Builder builder) {
        this.imageIdx = builder.imageIdx;
        this.imageName = builder.imageName;
        this.imageSize = builder.imageSize;
    }

    public static class Builder {
        private long imageIdx = 0;
        private String imageName;
        private long imageSize;

        public Builder(){};

        public Builder imageIdx(long imageIdx) {
            this.imageIdx = imageIdx;
            return this;
        }
        public Builder imageName(String imageName) {
            this.imageName = imageName;
            return this;
        }
        public Builder imageSize(long imageSize) {
            this.imageSize = imageSize;
            return this;
        }

        public Image build() {
            List<String> extensions = Arrays.asList("PNG", "JPEG", "BMP", "GIF", "JPG");

            Pattern pattern = Pattern.compile("\\.(\\w+)$");
            Matcher matcher = pattern.matcher(imageName);

            if (!matcher.find()) {
                throw new IllegalArgumentException("사진 파일이 아닙니다.");
            }

            String extension = matcher.group(1).toUpperCase();

            if (!extensions.contains(extension)) {
                throw new IllegalArgumentException("유효하지 않은 확장자입니다.");
            }

            if (imageSize >= 10_485_760) {
                throw new IllegalArgumentException("이미지의 크기가 10_485_760 byte 이상일 수 없습니다.");
            }

            return new Image(this);
        }
    }
}
