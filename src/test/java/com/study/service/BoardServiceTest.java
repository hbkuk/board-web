package com.study.service;

import com.study.repository.board.BoardDAO;
import com.study.repository.comment.CommentDAO;
import com.study.repository.file.FileDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

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

    @Test()
    void create() {
        when(boardDAO.increaseHitCount(1)).thenReturn(null);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> {boardService.getBoardWithDetails(1);});
    }
}


