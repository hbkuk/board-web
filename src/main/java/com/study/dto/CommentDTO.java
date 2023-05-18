package com.study.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDTO {
    private Long commentIdx;
    private String writer;
    private String password;
    private String content;
    private LocalDateTime regDate;
    private long boardIdx;
}