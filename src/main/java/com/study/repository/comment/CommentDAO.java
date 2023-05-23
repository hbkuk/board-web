package com.study.repository.comment;

import com.study.dto.CommentDTO;
import com.study.model.comment.Comment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private static final String FIND_ALL = "SELECT * FROM tb_comment WHERE board_idx = ?";
    private static final String SAVE = "INSERT INTO tb_comment (writer, password, content, regdate, board_idx) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM tb_comment WHERE board_idx = ?";

    public CommentDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<CommentDTO> findAllByBoardId(long boardIdx) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(FIND_ALL);
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            List<CommentDTO> comments = new ArrayList<>();
            while (resultSet.next()) {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setCommentIdx(resultSet.getLong("comment_idx"));
                commentDTO.setWriter(resultSet.getString("writer"));
                commentDTO.setPassword(resultSet.getString("password"));
                commentDTO.setContent((resultSet.getString("content")));
                commentDTO.setRegDate(resultSet.getTimestamp("regdate").toLocalDateTime());
                commentDTO.setBoardIdx(resultSet.getLong("board_idx"));
                comments.add(commentDTO);
            }
            return comments;
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public CommentDTO save(Comment comment) {
        Connection connection = null;
        PreparedStatement statement = null;

        CommentDTO commentDTO = new CommentDTO();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(SAVE);
            statement.setString(1, comment.getWriter().getWriter());
            statement.setString(2, comment.getPassword().getPassword());
            statement.setString(3, comment.getContent().getCommentContent());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(5, comment.getBoardIdx().getBoardIdx());
            statement.executeUpdate();

            commentDTO.setBoardIdx(comment.getBoardIdx().getBoardIdx());
            return commentDTO;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAllByBoardIdx(Long boardIdx) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            statement = connection.prepareStatement(DELETE);
            statement.setLong(1, boardIdx);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
