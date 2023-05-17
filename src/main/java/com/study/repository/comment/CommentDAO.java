package com.study.repository.comment;

import com.study.dto.CommentDTO;
import com.study.model.board.BoardIdx;
import com.study.model.board.Password;
import com.study.model.board.RegDate;
import com.study.model.comment.CommentContent;
import com.study.model.comment.CommentIdx;
import com.study.model.comment.CommentWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO implements CommentRepository {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public CommentDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommentDTO findById(Long boardIdx) {
        CommentDTO commentDTO = new CommentDTO();
        try {
            statement = connection.prepareStatement("SELECT * FROM comment WHERE comment_idx = ?");
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                commentDTO.setCommentIdx(new CommentIdx(resultSet.getLong("comment_idx")));
                commentDTO.setWriter(new CommentWriter(resultSet.getString("writer")));
                commentDTO.setPassword(new Password(resultSet.getString("password")));
                commentDTO.setContent(new CommentContent((resultSet.getString("content"))));
                commentDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                commentDTO.setBoardIdx(new BoardIdx(resultSet.getLong("board_idx")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return commentDTO;
    }

    @Override
    public List<CommentDTO> findAllByBoardId(long boardIdx) {
        try {
            statement = connection.prepareStatement("SELECT * FROM comment WHERE board_idx = ?");
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            List<CommentDTO> comments = new ArrayList<>();
            while (resultSet.next()) {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setCommentIdx(new CommentIdx(resultSet.getLong("comment_idx")));
                commentDTO.setWriter(new CommentWriter(resultSet.getString("writer")));
                commentDTO.setPassword(new Password(resultSet.getString("password")));
                commentDTO.setContent(new CommentContent((resultSet.getString("content"))));
                commentDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                commentDTO.setBoardIdx(new BoardIdx(resultSet.getLong("board_idx")));
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasImageByBoardId(long boardId) {
        try {
            String sql = "SELECT * FROM image WHERE board_id = ? limit 1";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, boardId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<CommentDTO> findAll() {
        try {
            statement = connection.prepareStatement("SELECT * FROM comment");
            resultSet = statement.executeQuery();
            List<CommentDTO> comments = new ArrayList<>();
            while (resultSet.next()) {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setCommentIdx(new CommentIdx(resultSet.getLong("comment_idx")));
                commentDTO.setWriter(new CommentWriter(resultSet.getString("writer")));
                commentDTO.setPassword(new Password(resultSet.getString("password")));
                commentDTO.setContent(new CommentContent((resultSet.getString("content"))));
                commentDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                commentDTO.setBoardIdx(new BoardIdx(resultSet.getLong("board_idx")));
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CommentDTO save(CommentDTO commentDTO) {
        try {
            statement = connection.prepareStatement("INSERT INTO comment (writer, password, content, board_idx) VALUES (?, ?, ?, ?)");
            statement.setString(1, commentDTO.getWriter().getWriter());
            statement.setString(2, commentDTO.getPassword().getPassword());
            statement.setString(3, commentDTO.getContent().getCommentContent());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                commentDTO.setCommentIdx(new CommentIdx(generatedKeys.getLong(1)));
            }
            return commentDTO;
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            return commentDTO;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                // Handle exception
                e.printStackTrace();
            }
        }
    }
    @Override
    public void deleteByCommentIdx(Long commentId) {
        try {
            statement = connection.prepareStatement("DELETE FROM comment WHERE comment_idx = ?");
            statement.setLong(1, commentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAllByBoardIdx(Long boardIdx) {
        try {
            statement = connection.prepareStatement("DELETE FROM comment WHERE board_idx = ?");
            statement.setLong(1, boardIdx);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
