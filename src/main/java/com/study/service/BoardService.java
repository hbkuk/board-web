package com.study.service;

import com.study.dto.BoardDTO;
import com.study.dto.FileDTO;
import com.study.model.board.Board;
import com.study.model.file.file;
import com.study.repository.board.BoardDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.file.fileDAO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BoardService {
    private static class LazyHolder {
        private static final BoardService INSTANCE = new BoardService();
    }
    private final BoardDAO boardDAO;
    private final CommentDAO commentDAO;
    private final fileDAO imageDAO;

    private BoardService() {
        boardDAO = new BoardDAO();
        commentDAO = new CommentDAO();
        imageDAO = new fileDAO();
    }

    public static BoardService getInstance() {
        return LazyHolder.INSTANCE;
    }

    public List<BoardDTO> getBoardListDetails() {
        return boardDAO.findAllWithImageCheck();
    }

    public BoardDTO getBoardWithDetails(long boardIdx) {
        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        boardDTO.setComments(commentDAO.findAllByBoardId(boardIdx));
        boardDTO.setFiles(imageDAO.findImagesByBoardId(boardIdx));

        return boardDTO;
    }

    public BoardDTO saveBoardWithImages(Board board, List<file> images) {
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

        boardDTO.setFiles(imageDAO.findImagesByBoardId(boardId));

        return boardDTO;
    }

    public void updateBoardWithImages(Board board, List<file> images) {
        BoardDTO boardDTO = boardDAO.update(board);

        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        // 새 이미지 찾기
        List<file> newImages = images.stream()
                .filter(image -> image.getImageIdx().getImageIdx() == 0)
                .collect(Collectors.toList());

        // 새 이미지 추가
        newImages.forEach(image -> imageDAO.save(image, boardDTO.getBoardIdx()));

        // 기존 이미지 찾기
        List<FileDTO> oldImages =
                imageDAO.findImagesByBoardId(boardDTO.getBoardIdx());

        // 기존 이미지 제거
        oldImages.stream()
                .filter(imageDTO -> !newImages.contains(imageDTO))
                .forEach(imageDTO -> imageDAO.deleteByImageIdx(imageDTO.getFileIdx()));
    }

    public void deleteBoardWithImagesAndComment(BoardDTO boardDTO) {
        boardDAO.deleteById(boardDTO.getBoardIdx(), boardDTO.getPassword());
        commentDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
        imageDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
    }
}

