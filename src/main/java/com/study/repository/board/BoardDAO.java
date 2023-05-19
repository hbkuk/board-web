package com.study.repository.board;

import com.study.dto.BoardDTO;
import com.study.model.board.Board;
import com.study.model.board.Category;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public BoardDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public BoardDTO findById(Long id) {
        BoardDTO boardDTO = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM board WHERE board_idx = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boardDTO = new BoardDTO();
                boardDTO.setBoardIdx(id);
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(resultSet.getString("title"));
                boardDTO.setWriter(resultSet.getString("writer"));
                boardDTO.setContent(resultSet.getString("content"));
                boardDTO.setPassword((resultSet.getString("password")));
                boardDTO.setHit(resultSet.getInt("hit"));
                boardDTO.setRegDate(resultSet.getTimestamp("regdate").toLocalDateTime());
                if (resultSet.getTimestamp("moddate") != null) {
                    boardDTO.setModDate(resultSet.getTimestamp("moddate").toLocalDateTime());
                }
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
        return boardDTO;
    }

    public List<BoardDTO> findAll() {
        try {
            statement = connection.prepareStatement("SELECT * FROM board");
            resultSet = statement.executeQuery();
            List<BoardDTO> boards = new ArrayList<>();
            while (resultSet.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(resultSet.getString("title"));
                boardDTO.setWriter(resultSet.getString("writer"));
                boardDTO.setContent(resultSet.getString("content"));
                boardDTO.setPassword((resultSet.getString("password")));
                boardDTO.setHit(resultSet.getInt("hit"));
                boardDTO.setRegDate(resultSet.getTimestamp("regdate").toLocalDateTime());
                boardDTO.setModDate(resultSet.getTimestamp("moddate").toLocalDateTime());
                boards.add(boardDTO);
            }
            return boards;
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

    public List<BoardDTO> findAllWithImageCheck() {
        try {
            statement = connection.prepareStatement("SELECT b.*, (CASE WHEN EXISTS (SELECT 1 FROM file f WHERE f.board_idx = b.board_idx) THEN 1 ELSE 0 END) AS has_file FROM board b LEFT JOIN file f ON b.board_idx = f.board_idx group by b.board_idx");
            resultSet = statement.executeQuery();
            List<BoardDTO> boards = new ArrayList<>();
            while (resultSet.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setBoardIdx(resultSet.getLong("board_idx"));
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(resultSet.getString("title"));
                boardDTO.setWriter(resultSet.getString("writer"));
                boardDTO.setContent(resultSet.getString("content"));
                boardDTO.setPassword((resultSet.getString("password")));
                boardDTO.setHit(resultSet.getInt("hit"));
                boardDTO.setRegDate(resultSet.getTimestamp("regdate").toLocalDateTime());
                boardDTO.setModDate(resultSet.getTimestamp("moddate").toLocalDateTime());
                boolean hasImage = resultSet.getInt("has_file") == 1;
                boardDTO.setHasFile(hasImage);
                boards.add(boardDTO);
            }
            return boards;
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

    public BoardDTO save(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO board (category, title, writer, content, password, hit, regdate) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS );
            statement.setString(1, String.valueOf(board.getCategory()));
            statement.setString(2, board.getTitle().getTitle());
            statement.setString(3, board.getWriter().getWriter());
            statement.setString(4, board.getContent().getContent());
            statement.setString(5, board.getPassword().getPassword());
            statement.setInt(6, board.getHit().getHit());
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                boardDTO.setBoardIdx(generatedKeys.getLong(1));
            }
            return boardDTO;
        } catch (SQLException e) {
            e.printStackTrace();
            return boardDTO;
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

    public BoardDTO update(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        try {
            statement = connection.prepareStatement("UPDATE board SET title = ?, writer = ?, content = ?, moddate = ? WHERE board_idx = ? and password = ?");

            statement.setString(1, board.getTitle().getTitle());
            statement.setString(2, board.getWriter().getWriter());
            statement.setString(3, board.getContent().getContent());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(5, board.getBoardIdx().getBoardIdx());
            statement.setString(6, board.getPassword().getPassword());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }
            boardDTO.setBoardIdx(board.getBoardIdx().getBoardIdx());
            return boardDTO;
        } catch (SQLException e) {
            e.printStackTrace();
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

    public BoardDTO increaseHitCount(long boardIdx) {
        BoardDTO boardDTO = new BoardDTO();
        try {
            statement = connection.prepareStatement("UPDATE board SET hit = hit + 1 WHERE board_idx = ?");
            statement.setLong(1, boardIdx);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }
            boardDTO.setBoardIdx(boardIdx);
            return boardDTO;
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void deleteById(Long id, String password) {
        try {
            statement = connection.prepareStatement("DELETE FROM board WHERE board_idx = ? and password = ?");
            statement.setLong(1, id);
            statement.setString(2, password);

            statement.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
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
}
