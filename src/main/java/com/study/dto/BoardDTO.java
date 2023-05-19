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
    private boolean hasFile;
    private List<CommentDTO> comments;
    private List<FileDTO> files;

    @Override
    public String toString() {
        return "BoardDTO{" +
                "boardIdx=" + boardIdx +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                ", password='" + password + '\'' +
                ", hit=" + hit +
                ", regDate=" + regDate +
                ", modDate=" + modDate +
                ", hasFile=" + hasFile +
                ", comments=" + comments +
                ", files=" + files +
                '}';
    }
}
