package com.study.repository.image;

import com.study.dto.ImageDTO;

import java.util.List;

public interface ImageRepository {
    ImageDTO findById(Long id);

    boolean hasImageByBoardId(long boardId);

    List<ImageDTO> findImagesByBoardId(Long boardIdx);

    ImageDTO save(ImageDTO imageDTO);

    ImageDTO update(ImageDTO imageDTO);

    void deleteByImageIdx(Long image_idx);

    void deleteAllByBoardIdx(Long board_idx);
}
