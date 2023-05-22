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
import java.util.stream.Collectors;

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
        log.debug("getBoardWithImages() 메서드 호출시 BoardIdx: {}", boardIdx);

        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("getBoardWithImages() -> findById -> BoardIDX : {}", boardDTO.getBoardIdx());
        boardDTO.setFiles(fileDAO.findImagesByBoardId(boardIdx));

        return boardDTO;
    }

    public BoardDTO saveBoardWithImages(Board board, List<File> files) {
        log.debug(" saveBoardWithImages() 메서드 호출 -> board : {} , files의 size",
                    board.toString(), files.size());
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


    public BoardDTO updateBoardWithImages(Board board, List<File> newFiles, List<Long> oldFiles) {
        log.debug(" updateBoardWithImages() 메서드 호출 -> board : {} , newFiles의 size : {}, oldFiles의 size : {}",
                board.toString(), newFiles.size(), oldFiles.size());

        BoardDTO boardDTO = boardDAO.update(board);
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
        log.debug("Board Modify 성공 -> boardIdx : {}", boardDTO.getBoardIdx());

        // DB 확인
        List<Long> dbFiles = fileDAO.findImagesByBoardId(boardDTO.getBoardIdx())
                        .stream().map(fileDTO -> fileDTO.getFileIdx())
                        .collect(Collectors.toList());
        log.debug("기존 글에 대한 dbFiles find 성공 -> dbFiles의 개수 : {} ", dbFiles.size());

        List<Long> removeFiles = new ArrayList<>(dbFiles);
        removeFiles.removeAll(oldFiles);

        log.debug("retainAll() 메서드 호출 -> removeFiles의 개수 : {} ", removeFiles);

        for( Long index : removeFiles) {
            log.debug("삭제된 파일의 인덱스 : {} ", index);
        }

        List<String> removeFileNames = removeFiles.stream()
                .map(fileIdx -> fileDAO.findById(fileIdx).getSaveFileName())
                .collect(Collectors.toList());

        removeFiles.stream().forEach(fileIdx -> fileDAO.deleteByImageIdx(fileIdx));

        // download 폴더에서 해당 파일 삭제
        for (String fileName : removeFileNames) {
            java.io.File file = new java.io.File( "C:\\git\\ebrain\\eb-study-templates-1week\\src\\main\\webapp\\download", fileName );
            file.delete();
        }

        // 새 이미지 추가
        newFiles.forEach(file -> fileDAO.save(file, boardDTO.getBoardIdx()));

        log.debug("NewFile Save 성공");

        return boardDTO;
    }

    public void deleteBoardWithImagesAndComment(BoardDTO boardDTO) {
        boardDAO.deleteById(boardDTO.getBoardIdx(), boardDTO.getPassword());
        commentDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
        fileDAO.deleteAllByBoardIdx(boardDTO.getBoardIdx());
    }
}

