package com.study.repository.board;

import com.study.dto.BoardDTO;
import com.study.model.board.BoardContent;
import com.study.model.board.BoardIdx;
import com.study.model.board.BoardWriter;
import com.study.model.board.Category;
import com.study.model.board.Hit;
import com.study.model.board.ModDate;
import com.study.model.board.Password;
import com.study.model.board.RegDate;
import com.study.model.board.Title;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO implements BoardRepository {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public BoardDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BoardDTO findById(Long id) {
        BoardDTO boardDTO = new BoardDTO();
        try {
            statement = connection.prepareStatement("SELECT * FROM board WHERE board_idx = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(new Title(resultSet.getString("title")));
                boardDTO.setWriter(new BoardWriter(resultSet.getString("writer")));
                boardDTO.setContent(new BoardContent(resultSet.getString("content")));
                boardDTO.setPassword(new Password((resultSet.getString("password"))));
                boardDTO.setHit(new Hit(resultSet.getInt("hit")));
                boardDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                boardDTO.setModDate(new ModDate(resultSet.getTimestamp("moddate").toLocalDateTime()));
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

    @Override
    public List<BoardDTO> findAll() {
        try {
            statement = connection.prepareStatement("SELECT * FROM board");
            resultSet = statement.executeQuery();
            List<BoardDTO> boards = new ArrayList<>();
            while (resultSet.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(new Title(resultSet.getString("title")));
                boardDTO.setWriter(new BoardWriter(resultSet.getString("writer")));
                boardDTO.setContent(new BoardContent(resultSet.getString("content")));
                boardDTO.setPassword(new Password((resultSet.getString("password"))));
                boardDTO.setHit(new Hit(resultSet.getInt("hit")));
                boardDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                boardDTO.setModDate(new ModDate(resultSet.getTimestamp("moddate").toLocalDateTime()));
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
            statement = connection.prepareStatement("SELECT b.*, (CASE WHEN EXISTS (SELECT 1 FROM image i WHERE i.board_idx = b.board_idx) THEN 1 ELSE 0 END) AS has_image FROM board b LEFT JOIN image i ON b.board_idx = i.board_idx WHERE b.board_idx = ?;");
            resultSet = statement.executeQuery();
            List<BoardDTO> boards = new ArrayList<>();
            while (resultSet.next()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setCategory(Category.valueOf(resultSet.getString("category")));
                boardDTO.setTitle(new Title(resultSet.getString("title")));
                boardDTO.setWriter(new BoardWriter(resultSet.getString("writer")));
                boardDTO.setContent(new BoardContent(resultSet.getString("content")));
                boardDTO.setPassword(new Password((resultSet.getString("password"))));
                boardDTO.setHit(new Hit(resultSet.getInt("hit")));
                boardDTO.setRegDate(new RegDate(resultSet.getTimestamp("regdate").toLocalDateTime()));
                boardDTO.setModDate(new ModDate(resultSet.getTimestamp("moddate").toLocalDateTime()));
                boolean hasImage = resultSet.getInt("has_image") == 1;
                boardDTO.setHasImage(hasImage);
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

    @Override
    public BoardDTO save(BoardDTO boardDTO) {
        try {
            statement = connection.prepareStatement("INSERT INTO board (category, title, writer, content, password, hit, regdate) VALUES (?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, String.valueOf(boardDTO.getCategory()));
            statement.setString(2, boardDTO.getTitle().getTitle());
            statement.setString(3, boardDTO.getWriter().getWriter());
            statement.setString(4, boardDTO.getContent().getContent());
            statement.setString(5, boardDTO.getPassword().getPassword());
            statement.setInt(6, boardDTO.getHit().getHit());
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                boardDTO.setBoardIdx(new BoardIdx(generatedKeys.getLong(1)));
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

    public BoardDTO update(BoardDTO boardDTO) {
        try {
            statement = connection.prepareStatement("UPDATE board SET title = ?, writer = ?, content = ?, moddate = ? WHERE board_idx = ? and password = ?");

            statement.setString(1, boardDTO.getTitle().getTitle());
            statement.setString(2, boardDTO.getWriter().getWriter());
            statement.setString(3, boardDTO.getContent().getContent());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(5, boardDTO.getBoardIdx().getBoardIdx());
            statement.setString(6, boardDTO.getPassword().getPassword());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }
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

    @Override
    public void deleteById(Long id, String password) {
        try {
            statement = connection.prepareStatement("DELETE FROM board WHERE board_idx = ? and password = ?");
            statement.setLong(1, id);
            statement.setString(1, password);

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
