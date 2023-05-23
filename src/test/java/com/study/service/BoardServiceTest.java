package com.study.service;

import com.study.dto.BoardDTO;
import com.study.model.board.*;
import com.study.model.file.File;
import com.study.model.file.FileOriginalName;
import com.study.model.file.FileSize;
import com.study.repository.board.BoardDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.file.FileDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("BoardService는")
@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardDAO boardDAO;
    @Mock
    private CommentDAO commentDAO;
    @Mock
    private FileDAO fileDAO;

    private BoardService boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardDAO, commentDAO, fileDAO);
    }

    @Test
    @DisplayName("게시글을 클릭했을때 메서드는 순서대로 호출된다.")
    void board_click_triggers_method_calls() {
        // given
        Long boardIdx = 1L;
        BoardDTO boardDTO = new BoardDTO();
        when(boardDAO.increaseHitCount(boardIdx)).thenReturn(boardDTO);
        when(boardDAO.findById(boardIdx)).thenReturn(boardDTO);

        // when
        boardService.getBoardWithDetails(boardIdx);

        // then
        InOrder inOrder = inOrder(boardDAO, commentDAO, fileDAO);
        inOrder.verify(boardDAO).increaseHitCount(boardIdx);
        inOrder.verify(boardDAO).findById(boardIdx);
        inOrder.verify(commentDAO).findAllByBoardId(boardIdx);
        inOrder.verify(fileDAO).findFilesByBoardId(boardIdx);
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    @DisplayName("게시물을 클릭했을때 조회수가 증가되지 못했다면 예외가 발생한다")
    void views_not_increased_exception() {

        when(boardDAO.increaseHitCount(1)).thenReturn(null);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> {boardService.getBoardWithDetails(1);})
                .withMessageMatching("해당 글을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("게시물 작성시 업로드된 파일이 없다면 글만 저장되어야 한다.")
    void save_only_post_when_no_uploaded_file() {
        // given
        long expectedBoardIdx = 1L;

        Board board = new Board.Builder()
                .category(Category.JAVA)
                .title(new Title("Title 1"))
                .writer(new BoardWriter("테스터"))
                .content(new BoardContent("Content 1"))
                .password(new Password("rkskekfkakqkt!1"))
                .build();

        File file = new File.Builder()
                .originalName(new FileOriginalName("test.png"))
                .saveFileName(("test.png"))
                .fileSize(new FileSize(127904))
                .boardIdx(new BoardIdx(expectedBoardIdx))
                .build();

        List<File> files = new ArrayList<>();

        BoardDTO mockedBoardDTO = new BoardDTO();
        when(boardDAO.save(board)).thenReturn(mockedBoardDTO);

        // when
        boardService.saveBoardWithImages(board, files);

        // then
        verify(boardDAO, times(1)).save(board);
        verify(fileDAO, never()).save(file, expectedBoardIdx);
    }

    @Test
    @DisplayName("게시글 작성시 업로드된 파일이 있으면 글과 파일이 저장되어야 한다.")
    void save_post_and_file_when_exist_uploaded_file() {
        // given
        long expectedBoardIdx = 1L;

        Board board = new Board.Builder()
                .category(Category.JAVA)
                .title(new Title("Title 1"))
                .writer(new BoardWriter("테스터"))
                .content(new BoardContent("Content 1"))
                .password(new Password("rkskekfkakqkt!1"))
                .build();

        File file = new File.Builder()
                .originalName(new FileOriginalName("test.png"))
                .saveFileName(("test.png"))
                .fileSize(new FileSize(127904))
                .build();

        List<File> files = new ArrayList<>();
        files.add(file);

        BoardDTO mockedBoardDTO = new BoardDTO();
        mockedBoardDTO.setBoardIdx(expectedBoardIdx);

        when(boardDAO.save(board)).thenReturn(mockedBoardDTO);

        // when
        boardService.saveBoardWithImages(board, files);

        // then
        verify(boardDAO, times(1)).save(board);
        verify(fileDAO, times(1)).save(file, expectedBoardIdx);
    }

    @Test
    @DisplayName("게시글 수정을 눌렀을때 메서드가 다음과 같은 순서대로 호출된다.")
    void modify_post_click_triggers_method_calls() {
        // given
        Long boardIdx = 1L;
        BoardDTO boardDTO = new BoardDTO();
        when(boardDAO.findById(boardIdx)).thenReturn(boardDTO);

        // when
        boardService.getBoardWithImages(boardIdx);

        // then
        InOrder inOrder = inOrder(boardDAO, fileDAO);
        inOrder.verify(boardDAO).findById(boardIdx);
        inOrder.verify(fileDAO).findFilesByBoardId(boardIdx);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("게시글 수정을 눌렀을때 글을 찾지 못했다면 예외가 발생한다.")
    void modify_post_not_found_exception() {

        when(boardDAO.findById(1L)).thenReturn(null);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> {boardService.getBoardWithImages(1L);})
                .withMessageMatching("해당 글을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("기존 업로드 된 파일이 없을때, 게시글의 내용만 수정할 경우 게시글만 수정된다.")
    void before_notting_uploaded_files_modify_post_when_only_text_update() {
        // given
        Long boardIdx = 1L;

        Board updateBoard = new Board.Builder()
                .boardIdx(new BoardIdx(boardIdx))
                .category(Category.JAVA)
                .title(new Title("Title 1"))
                .writer(new BoardWriter("테스터"))
                .content(new BoardContent("Content 1"))
                .password(new Password("rkskekfkakqkt!1"))
                .build();

        BoardDTO updateReturnBoardDTO = new BoardDTO();
        updateReturnBoardDTO.setBoardIdx(boardIdx);

        List<File> newUploadFiles = new ArrayList<>();

        List<Long> previouslyUploadedIndexes = new ArrayList<>();

        List<Long> getDbFileIndexes = new ArrayList<>();

        when(boardDAO.update(updateBoard)).thenReturn(updateReturnBoardDTO);
        when(fileDAO.findFileIndexesByBoardId(boardIdx)).thenReturn(getDbFileIndexes);

        // when
        boardService.updateBoardWithImages(updateBoard, newUploadFiles, previouslyUploadedIndexes);

        // then
        verify(boardDAO, times(1)).update(updateBoard);
    }

    @Test
    @DisplayName("기존 3개의 파일이 업로드 된 상태에서, 게시글 수정시 내용만 수정할 경우 게시글만 수정하고 기존 파일은 유지된다.")
    void before_uploaded_files_modify_post_when_only_text_update() {
        // given
        Long boardIdx = 1L;

        Board updateBoard = new Board.Builder()
                .boardIdx(new BoardIdx(boardIdx))
                .category(Category.JAVA)
                .title(new Title("Title 1"))
                .writer(new BoardWriter("테스터"))
                .content(new BoardContent("Content 1"))
                .password(new Password("rkskekfkakqkt!1"))
                .build();

        BoardDTO updateReturnBoardDTO = new BoardDTO();
        updateReturnBoardDTO.setBoardIdx(boardIdx);

        List<File> newUploadFiles = new ArrayList<>();

        List<Long> previouslyUploadedIndexes = Arrays.asList(10L, 11L, 12L);

        List<Long> dbFileIndexes = Arrays.asList(10L, 11L, 12L);

        when(boardDAO.update(updateBoard)).thenReturn(updateReturnBoardDTO);
        when(fileDAO.findFileIndexesByBoardId(boardIdx)).thenReturn(dbFileIndexes);

        // when
        boardService.updateBoardWithImages(updateBoard, newUploadFiles, previouslyUploadedIndexes);

        // then
        verify(boardDAO, times(1)).update(updateBoard);

        dbFileIndexes.stream()
                        .forEach(fileIdx ->
                            verify(fileDAO, never()).deleteByFileId(fileIdx));
    }

    @Test
    @DisplayName("기존 3개의 파일이 업로드 된 상태에서, 3개의 파일을 삭제 시 게시글은 수정하고 DB에 저장된 파일정보는 삭제된다.")
    void before_uploaded_files_modify_post_when_delete_all_uploaded_files() {
        // given
        Long boardIdx = 1L;

        Board updateBoard = new Board.Builder()
                .boardIdx(new BoardIdx(boardIdx))
                .category(Category.JAVA)
                .title(new Title("Title 1"))
                .writer(new BoardWriter("테스터"))
                .content(new BoardContent("Content 1"))
                .password(new Password("rkskekfkakqkt!1"))
                .build();

        BoardDTO updateReturnBoardDTO = new BoardDTO();
        updateReturnBoardDTO.setBoardIdx(boardIdx);

        List<File> newUploadFiles = new ArrayList<>();

        List<Long> previouslyUploadedIndexes = new ArrayList<>();

        List<Long> dbFileIndexes = Arrays.asList(10L, 11L, 12L);

        List<Long> fileIndexesToDelete = new ArrayList<>(dbFileIndexes);
        fileIndexesToDelete.removeAll(previouslyUploadedIndexes);

        when(boardDAO.update(updateBoard)).thenReturn(updateReturnBoardDTO);
        when(fileDAO.findFileIndexesByBoardId(boardIdx)).thenReturn(dbFileIndexes);
        when(fileDAO.findSavedFileNameById(10L)).thenReturn("image1.jpg");
        when(fileDAO.findSavedFileNameById(11L)).thenReturn("image2.jpg");
        when(fileDAO.findSavedFileNameById(12L)).thenReturn("image3.jpg");

        // when
        boardService.updateBoardWithImages(updateBoard, newUploadFiles, previouslyUploadedIndexes);

        // then
        verify(boardDAO, times(1)).update(updateBoard);

        fileIndexesToDelete.stream()
                        .forEach(fileIdx -> verify(fileDAO, times(1)).deleteByFileId(fileIdx));
    }

    @Test
    @DisplayName("기존 2개의 파일이 업로드 된 상태에서, 1개의 파일을 추가로 업로드할 경우, 게시글은 수정하고 추가로 업로드한다.")
    void before_uploaded_files_modify_post_when_new_file() {
        // given
        Long boardIdx = 1L;

        Board updateBoard = new Board.Builder()
                .boardIdx(new BoardIdx(boardIdx))
                .category(Category.JAVA)
                .title(new Title("Title 1"))
                .writer(new BoardWriter("테스터"))
                .content(new BoardContent("Content 1"))
                .password(new Password("rkskekfkakqkt!1"))
                .build();

        BoardDTO updateReturnBoardDTO = new BoardDTO();
        updateReturnBoardDTO.setBoardIdx(boardIdx);

        File newFile = new File.Builder()
                .originalName(new FileOriginalName("test1.png"))
                .saveFileName(("test1.png"))
                .fileSize(new FileSize(127904))
                .build();

        List<File> newUploadFiles = Arrays.asList(newFile);

        List<Long> previouslyUploadedIndexes = Arrays.asList(10L, 11L);

        List<Long> dbFileIndexes = Arrays.asList(10L, 11L);

        when(boardDAO.update(updateBoard)).thenReturn(updateReturnBoardDTO);
        when(fileDAO.findFileIndexesByBoardId(boardIdx)).thenReturn(dbFileIndexes);

        // when
        boardService.updateBoardWithImages(updateBoard, newUploadFiles, previouslyUploadedIndexes);

        // then
        verify(boardDAO, times(1)).update(updateBoard);
        verify(fileDAO, times(1)).save(newUploadFiles.get(0), boardIdx);
        dbFileIndexes.stream()
                .forEach(fileIdx ->
                        verify(fileDAO, never()).deleteByFileId(fileIdx));
    }
}


