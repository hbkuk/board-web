package com.study.repository.image;

import com.study.dto.ImageDTO;
import com.study.model.board.BoardIdx;
import com.study.model.image.ImageIdx;
import com.study.model.image.ImageName;
import com.study.model.image.ImageSize;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO implements ImageRepository {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public ImageDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public ImageDTO findById(Long id) {
        ImageDTO imageDTO = new ImageDTO();
        try {
            statement = connection.prepareStatement("SELECT * FROM image WHERE image_idx = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                imageDTO.setImageIdx(new ImageIdx(resultSet.getLong("image_idx")));
                imageDTO.setImageName(new ImageName(resultSet.getString("image_name")));
                imageDTO.setImageSize(new ImageSize(resultSet.getInt("image_size")));
                imageDTO.setBoardIdx(new BoardIdx(resultSet.getLong("board_idx")));
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
                // Handle exception
                e.printStackTrace();
            }
        }
        return imageDTO;
    }

    public boolean hasImageByBoardId(long boardId) {
        try {
            String sql = "SELECT * FROM image WHERE board_id = ? LIMIT 1";
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
    public List<ImageDTO> findImagesByBoardId(Long boardIdx) {
        try {
            statement = connection.prepareStatement("SELECT * FROM image WHERE board_idx = ?");
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            List<ImageDTO> images = new ArrayList<>();
            while (resultSet.next()) {
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageIdx(new ImageIdx(resultSet.getLong("image_idx")));
                imageDTO.setImageName(new ImageName(resultSet.getString("image_name")));
                imageDTO.setImageSize(new ImageSize(resultSet.getInt("image_size")));
                imageDTO.setBoardIdx(new BoardIdx(resultSet.getLong("board_idx")));
                images.add(imageDTO);
            }
            return images;
        } catch (SQLException e) {
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
        public ImageDTO save(ImageDTO imageDTO){
            try {
                statement = connection.prepareStatement("INSERT INTO image (image_name, image_size, board_idx) VALUES (?, ?, ?)");
                statement.setString(1, imageDTO.getImageName().toString());
                statement.setInt(2, imageDTO.getImageSize().getImageSize());
                statement.setLong(3, imageDTO.getBoardIdx().getBoardIdx());
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    imageDTO.setImageIdx(new ImageIdx(generatedKeys.getLong(1)));
                }
                return imageDTO;
            } catch (SQLException e) {
                e.printStackTrace();
                return imageDTO;
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

    public ImageDTO update(ImageDTO imageDTO) {
        try {
            statement = connection.prepareStatement("UPDATE image SET image_name = ?, image_size = ?, board_idx = ? WHERE image_idx = ?");

            statement.setString(1, imageDTO.getImageName().toString());
            statement.setInt(2, imageDTO.getImageSize().getImageSize());
            statement.setLong(3, imageDTO.getBoardIdx().getBoardIdx());
            statement.setLong(4, imageDTO.getImageIdx().getImageIdx());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                // Update failed, handle the failure
                return null;
            }
            return imageDTO;
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
        public void deleteByImageIdx(Long image_idx) {
            try {
                statement = connection.prepareStatement("DELETE FROM image WHERE image_idx = ?");
                statement.setLong(1, image_idx);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
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

    public void deleteAllByBoardIdx(Long board_idx) {
        try {
            statement = connection.prepareStatement("DELETE FROM image WHERE board_idx = ?");
            statement.setLong(1, board_idx);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    }