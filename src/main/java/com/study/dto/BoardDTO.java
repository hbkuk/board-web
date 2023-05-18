package com.study.dto;

import com.study.model.board.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardDTO {
    private long boardIdx;
    private Category category;
    private String title;
    private String writer;
    private String content;
    private String password;
    private int hit;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private boolean hasImage;
    private List<CommentDTO> comments;
    private List<ImageDTO> images;
}
