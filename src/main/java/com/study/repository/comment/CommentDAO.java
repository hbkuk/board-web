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

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public CommentDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public CommentDTO findById(Long boardIdx) {
        CommentDTO commentDTO = new CommentDTO();
        try {
            statement = connection.prepareStatement("SELECT * FROM comment WHERE comment_idx = ?");
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                commentDTO.setCommentIdx(resultSet.getLong("comment_idx"));
                commentDTO.setWriter(resultSet.getString("writer"));
                commentDTO.setPassword(resultSet.getString("password"));
                commentDTO.setContent((resultSet.getString("content")));
                commentDTO.setRegDate(resultSet.getTimestamp("regdate").toLocalDateTime());
                commentDTO.setBoardIdx(resultSet.getLong("board_idx"));
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

    public List<CommentDTO> findAllByBoardId(long boardIdx) {
        try {
            statement = connection.prepareStatement("SELECT * FROM comment WHERE board_idx = ?");
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

    public List<CommentDTO> findAll() {
        try {
            statement = connection.prepareStatement("SELECT * FROM comment");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public CommentDTO save(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        try {
            statement = connection.prepareStatement("INSERT INTO comment (writer, password, content, regdate, board_idx) VALUES (?, ?, ?, ?, ?)");
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
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
