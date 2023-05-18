package com.study.service;

import com.study.dto.BoardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

    @Test
    void create() {
        BoardService boardService = BoardService.getInstance();

        List<BoardDTO> boards = boardService.getBoardListDetails();
        assertThat(boards).hasSize(0);
    }

    @Test
    public void testMockito() {
        // Create a mock object
        List<String> mockedList = mock(List.class);

        // Define the behavior of the mock object
        when(mockedList.get(0)).thenReturn("Mockito");

        // Use the mock object
        String result = mockedList.get(0);

        // Verify the interactions
        verify(mockedList).get(0);

        // Assert the result
        assertEquals("Mockito", result);
    }
}

