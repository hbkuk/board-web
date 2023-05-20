package com.study.service;

import com.study.dto.BoardDTO;
import com.study.dto.CommentDTO;
import com.study.dto.FileDTO;
import com.study.model.board.Board;
import com.study.model.comment.Comment;
import com.study.model.file.File;
import com.study.repository.board.BoardDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.file.fileDAO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class BoardService {
//    private static final Logger log = LoggerFactory.getLogger(BoardService.class);

    private static class LazyHolder {
        private static final BoardService INSTANCE = new BoardService();
    }
    private final BoardDAO boardDAO;
    private final CommentDAO commentDAO;
    private final fileDAO fileDAO;

    private BoardService() {
        boardDAO = new BoardDAO();
        commentDAO = new CommentDAO();
        fileDAO = new fileDAO();
    }

    public static BoardService getInstance() {
        return LazyHolder.INSTANCE;
    }

    public List<BoardDTO> getBoardListDetails() {
        return boardDAO.findAllWithImageCheck();
    }

    public BoardDTO getBoardByBoardIdx(long boardIdx) {
        BoardDTO boardDTO = boardDAO.increaseHitCount(boardIdx);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        return boardDTO;
    }

    public BoardDTO getBoardWithDetails(long boardIdx) {
        BoardDTO boardDTO = boardDAO.increaseHitCount(boardIdx);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        boardDTO = boardDAO.findById(boardIdx);
        boardDTO.setComments(commentDAO.findAllByBoardId(boardIdx));
        boardDTO.setFiles(fileDAO.findImagesByBoardId(boardIdx));

        return boardDTO;
    }

    public BoardDTO getBoardWithImages(long boardIdx) {
        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        boardDTO.setFiles(fileDAO.findImagesByBoardId(boardIdx));

        return boardDTO;
    }

    public BoardDTO saveBoardWithImages(Board board, List<File> files) {
        BoardDTO boardDTO = boardDAO.save(board);

        if (files.size() != 0) {
            files.forEach(image -> fileDAO.save(image, boardDTO.getBoardIdx()));
        }

        return boardDTO;
    }

    public CommentDTO saveComment(Comment comment) {
        log.debug("New Commnet / request! Comment  : {} ", comment.toString());


        CommentDTO commentDTO = commentDAO.save(comment);
        log.debug("save Commnet! / response Comment  : {} ", commentDTO.toString());
        return commentDAO.save(comment);
    }


    public BoardDTO updateBoardWithImages(Board board, List<File> newFiles, List<Integer> oldFiles) {
        BoardDTO boardDTO = boardDAO.update(board);

        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        // 새 이미지 추가
        newFiles.forEach(file -> fileDAO.save(file, boardDTO.getBoardIdx()));

        // DB 확인
        List<FileDTO> dbFiles =
                fileDAO.findImagesByBoardId(boardDTO.getBoardIdx());

        // oldFiles과 DB 매칭
        List<Integer> oldFilesIdx = oldFiles;


        return boardDTO;
    }

    public void deleteBoardWithImagesAndComment(BoardDTO boardDTO) {
        boardDAO.deleteById(boardDTO.getBoardIdx(), boardDTO.getPassword());
        commentDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
        fileDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
    }
}

