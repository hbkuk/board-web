package com.study.model.image;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ImageNameExtension {
    PNG, JPEG, BMP, GIF, JPG;

    public static boolean contains(String extension) {
        List<String> extensions = Arrays.asList(values()).stream().map(Enum::name).collect(Collectors.toList());
        return extensions.contains(extension);
    }
}
