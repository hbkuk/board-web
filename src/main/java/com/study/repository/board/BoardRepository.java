package com.study.repository.board;

import com.study.dto.BoardDTO;

import java.util.List;

public interface BoardRepository {
    BoardDTO findById(Long id);

    List<BoardDTO> findAll();

    List<BoardDTO> findAllWithImageCheck();

    BoardDTO save(BoardDTO boardDTO);

    BoardDTO update(BoardDTO boardDTO);

    void deleteById(Long id, String password);
}
