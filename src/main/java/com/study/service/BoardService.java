package com.study.service;

import com.study.dto.BoardDTO;
import com.study.dto.CommentDTO;
import com.study.model.board.Board;
import com.study.model.comment.Comment;
import com.study.model.file.File;
import com.study.repository.board.BoardDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.file.FileDAO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class BoardService {

    private BoardDAO boardDAO;
    private CommentDAO commentDAO;
    private FileDAO fileDAO;

    public BoardService(BoardDAO boardDAO, CommentDAO commentDAO, FileDAO fileDAO) {
        this.boardDAO = boardDAO;
        this.commentDAO = commentDAO;
        this.fileDAO = fileDAO;
    }

    public List<BoardDTO> getBoardListDetails() {
        return boardDAO.findAllWithImageCheck();
    }

    public BoardDTO getBoardWithDetails(long boardIdx) {
        if( boardDAO.increaseHitCount(boardIdx) == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        boardDTO.setComments(commentDAO.findAllByBoardId(boardIdx));
        boardDTO.setFiles(fileDAO.findFilesByBoardId(boardIdx));

        return boardDTO;
    }

    public BoardDTO getBoardWithImages(long boardIdx) {
        log.debug("getBoardWithImages() 메서드 호출시 BoardIdx: {}", boardIdx);

        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("getBoardWithImages() -> findById -> BoardIDX : {}", boardDTO.getBoardIdx());
        boardDTO.setFiles(fileDAO.findFilesByBoardId(boardIdx));

        return boardDTO;
    }

    public BoardDTO saveBoardWithImages(Board board, List<File> files) {
        log.debug(" saveBoardWithImages() 메서드 호출 -> board : {} , files의 size : {} ",
                    board.toString(), files.size());
        BoardDTO boardDTO = boardDAO.save(board);

        if (files.size() != 0) {
            files.forEach(file -> fileDAO.save(file, boardDTO.getBoardIdx()));
        }

        return boardDTO;
    }

    public CommentDTO saveComment(Comment comment) {
        BoardDTO boardDTO = boardDAO.findById(comment.getBoardIdx().getBoardIdx());
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("New Commnet / request! Comment  : {} ", comment.toString());
        return commentDAO.save(comment);
    }


    public BoardDTO updateBoardWithImages(Board updateBoard, List<File> newUploadFiles, List<Long> previouslyUploadedIndexes) {
        log.debug(" updateBoardWithImages() 메서드 호출 -> updateBoard : {} , newUploadFiles size : {}, previouslyUploadedIndexes size : {}",
                updateBoard.toString(), newUploadFiles.size(), previouslyUploadedIndexes.size());

        BoardDTO updateReturnBoardDTO = boardDAO.update(updateBoard);
        if( updateReturnBoardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }

        // DB 확인
        List<Long> dbFileIndexes = fileDAO.findFileIndexesByBoardId(updateReturnBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = new ArrayList<>(dbFileIndexes);
        indexesToDelete.removeAll(previouslyUploadedIndexes);
        
        // 파일 삭제
        indexesToDelete.stream().forEach(fileIdx -> fileDAO.deleteByFileId(fileIdx));

        // 저장 디렉토리에서 파일 삭제
        indexesToDelete.stream()
                .map(fileIdx -> fileDAO.findSavedFileNameById(fileIdx))
                .forEach(fileName -> new java.io.File( "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\download", fileName ).delete());

        // 새 이미지 추가
        newUploadFiles.forEach(file -> fileDAO.save(file, updateReturnBoardDTO.getBoardIdx()));

        return updateReturnBoardDTO;
    }

    public void deleteBoardWithImagesAndComment(BoardDTO boardDTO) {
        boardDAO.deleteById(boardDTO.getBoardIdx(), boardDTO.getPassword());
        commentDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
        fileDAO.deleteAllByBoardId(boardDTO.getBoardIdx());
    }
}

