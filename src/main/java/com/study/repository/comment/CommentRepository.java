package com.study.repository.comment;

import com.study.dto.CommentDTO;

import java.util.List;

public interface CommentRepository {
    CommentDTO findById(Long id);

    List<CommentDTO> findAllByBoardId(long boardId);

    boolean hasImageByBoardId(long boardId);

    List<CommentDTO> findAll();

    CommentDTO save(CommentDTO commentDTO);

    void deleteByCommentIdx(Long id);

    void deleteAllByBoardIdx(Long boardIdx);
}
