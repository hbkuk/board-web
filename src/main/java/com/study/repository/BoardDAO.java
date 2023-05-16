package com.study.repository;

import com.study.dto.BoardDTO;
import com.study.model.board.BoardId;
import com.study.model.board.Category;
import com.study.model.board.Hit;
import com.study.model.board.Password;
import com.study.model.board.RegDate;
import com.study.model.board.Title;
import com.study.model.board.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardDAO {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public BoardDAO() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
    }

    public Optional<BoardDTO> findById(Long id) throws SQLException {
        try {
            statement = connection.prepareStatement("SELECT * FROM board WHERE board_id = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(new Title(resultSet.getString("title")));
                boardDTO.setWriter(new Writer(resultSet.getString("writer")));
                boardDTO.setPassword(new Password((resultSet.getString("password"))));
                boardDTO.setHit(new Hit(resultSet.getInt("hit")));
                boardDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                return Optional.of(boardDTO);
            } else {
                return Optional.empty();
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    public List<BoardDTO> findAll() throws SQLException {
        try {
            statement = connection.prepareStatement("SELECT * FROM board");
            resultSet = statement.executeQuery();
            List<BoardDTO> boards = new ArrayList<>();
            while (resultSet.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setBoardId(new BoardId(resultSet.getLong("board_id")));
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(new Title(resultSet.getString("title")));
                boardDTO.setWriter(new Writer(resultSet.getString("writer")));
                boardDTO.setPassword(new Password((resultSet.getString("password"))));
                boardDTO.setHit(new Hit(resultSet.getInt("hit")));
                boardDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                boards.add(boardDTO);
            }
            return boards;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    public BoardDTO save(BoardDTO boardDTO) throws SQLException {
        try {
            statement = connection.prepareStatement("INSERT INTO board (category_id, title, writer, password, hit, regdate) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, String.valueOf(boardDTO.getCategory()));
            statement.setString(2, boardDTO.getTitle().toString());
            statement.setString(3, boardDTO.getWriter().toString());
            statement.setString(4, boardDTO.getPassword().toString());
            statement.setInt(5, boardDTO.getHit().getHit());
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                boardDTO.setBoardId(new BoardId(generatedKeys.getLong(1)));
            }
            return boardDTO;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void deleteById(Long id) throws SQLException {
        try {
            statement = connection.prepareStatement("DELETE FROM board WHERE board_id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

}

