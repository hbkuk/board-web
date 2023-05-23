package com.study.service;

import com.study.dto.BoardDTO;
import com.study.dto.CategoryDTO;
import com.study.dto.CommentDTO;
import com.study.model.board.Board;
import com.study.model.comment.Comment;
import com.study.model.file.File;
import com.study.repository.board.BoardDAO;
import com.study.repository.category.CategoryDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.file.FileDAO;
import com.study.utils.FileUtils;
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
    private CategoryDAO categoryDAO;

    public BoardService(BoardDAO boardDAO, CommentDAO commentDAO, FileDAO fileDAO, CategoryDAO categoryDAO) {
        this.boardDAO = boardDAO;
        this.commentDAO = commentDAO;
        this.fileDAO = fileDAO;
        this.categoryDAO = categoryDAO;
    }

    /**
     * 모든 게시물에 대한 정보와 해당 게시물에 업로드된 파일의 존재여부를 생성해 리턴합니다
     */
    public List<BoardDTO> getBoardListDetails() {
        return boardDAO.findAllWithImageCheck();
    }

    /**
     * 게시물 번호를 인자로 받아 번호에 해당하는 게시물, 모든 댓글, 모든 파일 정보를 생성해 리턴합니다
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO getBoardWithDetails(long boardIdx) {
        isEmpty(boardDAO.increaseHitCount(boardIdx));

        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        boardDTO.setComments(commentDAO.findAllByBoardId(boardIdx));
        boardDTO.setFiles(fileDAO.findFilesByBoardId(boardIdx));

        return boardDTO;
    }

    /**
     * 게시물 번호를 인자로 받아 번호에 해당하는 게시물, 모든 파일 정보를 생성해 리턴합니다
     * @param boardIdx 게시물 번호
     * @return 게시물 번호에 해당하는 게시물이 있다면 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO getBoardWithImages(long boardIdx) {
        log.debug("getBoardWithImages() 메서드 호출시 BoardIdx: {}", boardIdx);

        BoardDTO boardDTO = boardDAO.findById(boardIdx);
        isEmpty(boardDTO);
        log.debug("getBoardWithImages() -> findById -> BoardIDX : {}", boardDTO.getBoardIdx());
        boardDTO.setFiles(fileDAO.findFilesByBoardId(boardIdx));

        return boardDTO;
    }

    /**
     * 게시물 정보와 업로드 할 파일을 인자로 받아 저장하고 게시물 번호를 리턴합니다
     * @param board 게시물 정보
     * @param files 업로드 할 파일
     * @return 게시물이 저장되었다면 게시물 번호만 담긴 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO saveBoardWithImages(Board board, List<File> files) {
        log.debug(" saveBoardWithImages() 메서드 호출 -> board : {} , files의 size : {} ",
                    board.toString(), files.size());
        BoardDTO boardDTO = boardDAO.save(board);

        if (files.size() != 0) {
            files.forEach(file -> fileDAO.save(file, boardDTO.getBoardIdx()));
        }

        return boardDTO;
    }

    /**
     * 수정할 게시물 정보와 추가 또는 삭제할 파일의 정보를 인자로 받아서 수정하고 게시물 번호를 리턴합니다<br>
     * 사용자가 삭제한 경우 {@code previouslyUploadedIndexes}에 파일의 번호가 포함되지 않고 인자로 전달됩니다
     * @param updateBoard 수정할 게시물 정보
     * @param newUploadFiles 추가로 업로드 할 파일 정보
     * @param previouslyUploadedIndexes 이전에 업로드 된 파일의 번호
     * @return 게시물 수정이되었다면 게시물 번호가 담긴 BoardDTO, 그렇지 않다면 null
     */
    public BoardDTO updateBoardWithImages(Board updateBoard, List<File> newUploadFiles, List<Long> previouslyUploadedIndexes) {
        log.debug(" updateBoardWithImages() 메서드 호출 -> updateBoard : {} , newUploadFiles size : {}, previouslyUploadedIndexes size : {}",
                updateBoard.toString(), newUploadFiles.size(), previouslyUploadedIndexes.size());

        BoardDTO updateReturnBoardDTO = boardDAO.update(updateBoard);
        isEmpty(updateReturnBoardDTO);

        // DB 확인
        List<Long> dbFileIndexes = fileDAO.findFileIndexesByBoardId(updateReturnBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = new ArrayList<>(dbFileIndexes);
        indexesToDelete.removeAll(previouslyUploadedIndexes);

        // 파일 삭제
        deleteFilesFromdatabaseAndDirectory(indexesToDelete);

        // 새 이미지 추가
        newUploadFiles.forEach(file -> fileDAO.save(file, updateReturnBoardDTO.getBoardIdx()));

        return updateReturnBoardDTO;
    }

    /**
     * 삭제할 파일의 번호를 인자로 받아 데이터베이스 및 디렉토리에서 해당 파일 정보를 삭제합니다
     * @param indexesToDelete 삭제할 파일의 번호 리스트
     */
    private void deleteFilesFromdatabaseAndDirectory(List<Long> indexesToDelete) {

        // 저장 디렉토리에서 파일 삭제
        List<String> fileNamesToDelete = indexesToDelete.stream()
                .map(fileIdx -> fileDAO.findSavedFileNameById(fileIdx))
                .collect(Collectors.toList());

        fileNamesToDelete.stream()
                .forEach(fileName -> FileUtils.deleteUploadedFile(fileName));

        // DB 파일 삭제
        indexesToDelete.stream()
                .forEach(fileIdx -> fileDAO.deleteByFileId(fileIdx));

    }

    /**
     * 삭제할 게시물의 정보를 인자로 받아 게시물 번호에 해당하는 게시물, 댓글, 파일 정보를 삭제합니다.
     * @param deleteBoardDTO 삭제할 게시물 정보
     */
    public void deleteBoardWithFilesAndComment(BoardDTO deleteBoardDTO) throws IllegalArgumentException {
        BoardDTO boardDTO = boardDAO.findById(deleteBoardDTO.getBoardIdx());

        if( !boardDTO.getPassword().equals(deleteBoardDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        boardDAO.deleteById(deleteBoardDTO.getBoardIdx(), deleteBoardDTO.getPassword());
        commentDAO.deleteAllByBoardIdx(deleteBoardDTO.getBoardIdx());

        List<Long> indexesToDelete = fileDAO.findFileIndexesByBoardId(boardDTO.getBoardIdx());
        deleteFilesFromdatabaseAndDirectory(indexesToDelete);
    }

    /**
     * 댓글 정보를 인자로 받아 게시물 번호
     * @param comment 댓글 정보
     * @return 댓글이 저장되었다면 게시물 번호만 담긴 CommentDTO, 그렇지 않다면 null
     */
    public CommentDTO saveComment(Comment comment) {
        BoardDTO boardDTO = boardDAO.findById(comment.getBoardIdx().getBoardIdx());
        isEmpty(boardDTO);
        log.debug("New Commnet / request! Comment  : {} ", comment.toString());

        return commentDAO.save(comment);
    }

    //TODO: javadocs
    /**
     *
     * @param fileIdx
     * @return
     */
    public String getSavedFileName(long fileIdx) {
        return fileDAO.findSavedFileNameById(fileIdx);
    }

    //TODO: javadocs
    /**
     *
     * @return
     */
    public List<CategoryDTO> getAllCategory() {
        return categoryDAO.findAll();
    }

    /**
     * 해당 객체가 null이라면 예외를 던집니다
     * @param boardDTO 게시물 정보
     */
    private void isEmpty(BoardDTO boardDTO) {
        if( boardDTO == null ) {
            throw new NoSuchElementException("해당 글을 찾을 수 없습니다.");
        }
    }
}

