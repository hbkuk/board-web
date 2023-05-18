package com.study.service;

import com.study.dto.BoardDTO;
import com.study.dto.ImageDTO;
import com.study.model.board.Board;
import com.study.model.image.Image;
import com.study.repository.board.BoardDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.image.ImageDAO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BoardService {
    private static class LazyHolder {
        private static final BoardService INSTANCE = new BoardService();
    }
    private final BoardDAO boardDAO;
    private final CommentDAO commentDAO;
    private final ImageDAO imageDAO;

    private BoardService() {
        boardDAO = new BoardDAO();
        commentDAO = new CommentDAO();
        imageDAO = new ImageDAO();
    }

    public static BoardService getInstance() {
        return LazyHolder.INSTANCE;
    }

    public List<BoardDTO> getBoardListDetails() {
        return boardDAO.findAllWithImageCheck();
    }

    public BoardDTO getBoardWithDetails(long boardId) {
        BoardDTO boardDTO = boardDAO.findById(boardId);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        boardDTO.setComments(commentDAO.findAllByBoardId(boardId));
        boardDTO.setImages(imageDAO.findImagesByBoardId(boardId));

        return boardDTO;
    }

    public BoardDTO saveBoardWithImages(Board board, List<Image> images) {
        BoardDTO boardDTO = boardDAO.save(board);

        if (images.size() != 0) {
            images.forEach(image -> imageDAO.save(image, boardDTO.getBoardIdx()));
        }

        return boardDTO;
    }


    public BoardDTO getBoard(long boardId) {
        BoardDTO boardDTO = boardDAO.findById(boardId);

        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        boardDTO.setImages(imageDAO.findImagesByBoardId(boardId));

        return boardDTO;
    }

    public void updateBoardWithImages(Board board, List<Image> images) {
        BoardDTO boardDTO = boardDAO.update(board);

        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        // 새 이미지 찾기
        List<Image> newImages = images.stream()
                .filter(image -> image.getImageIdx().getImageIdx() == 0)
                .collect(Collectors.toList());

        // 새 이미지 추가
        newImages.forEach(image -> imageDAO.save(image, boardDTO.getBoardIdx()));

        // 기존 이미지 찾기
        List<ImageDTO> oldImages =
                imageDAO.findImagesByBoardId(boardDTO.getBoardIdx());

        // 기존 이미지 제거
        oldImages.stream()
                .filter(imageDTO -> !newImages.contains(imageDTO))
                .forEach(imageDTO -> imageDAO.deleteByImageIdx(imageDTO.getImageIdx()));
    }

    public void deleteBoardWithImagesAndComment(BoardDTO boardDTO) {
        boardDAO.deleteById(boardDTO.getBoardIdx(), boardDTO.getPassword());
        commentDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
        imageDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
    }
}

