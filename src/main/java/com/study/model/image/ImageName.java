package com.study.model.image;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageName {
    private static final String IMAGE_NAME_EXTENSION__REGEX = "\\.(\\w+)$";
    private static final Pattern EXTENSION_PATTERN_COMPILE = Pattern.compile(IMAGE_NAME_EXTENSION__REGEX);
    private String imageName;

    public ImageName(String imageName) {

        if (isInvalidImageName(imageName)) {
            throw new IllegalArgumentException("유효하지 않은 확장자입니다.");
        }

        this.imageName = imageName;
    }

    private boolean isInvalidImageName(String imageName) {
        Matcher matcher = getMatcher(imageName);
        if (!matcher.find()) {
            return true;
        }
        if (!ImageNameExtension.contains(matcher.group(1).toUpperCase())) {
            return true;
        }
        return false;
    }

    private Matcher getMatcher(String imageName) {
        return EXTENSION_PATTERN_COMPILE.matcher(imageName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ImageName imageName1 = (ImageName) o;
        return Objects.equals(imageName, imageName1.imageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageName);
    }
}
