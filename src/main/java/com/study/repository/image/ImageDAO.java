package com.study.repository.image;

import com.study.dto.ImageDTO;
import com.study.model.image.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public ImageDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ImageDTO findById(Long id) {
        ImageDTO imageDTO = new ImageDTO();
        try {
            statement = connection.prepareStatement("SELECT * FROM image WHERE image_idx = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                imageDTO.setImageIdx(resultSet.getLong("image_idx"));
                imageDTO.setImageName(resultSet.getString("name"));
                imageDTO.setImageSize(resultSet.getInt("size"));
                imageDTO.setBoardIdx(resultSet.getLong("board_idx"));
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


    public List<ImageDTO> findImagesByBoardId(Long boardIdx) {
        try {
            statement = connection.prepareStatement("SELECT * FROM image WHERE board_idx = ?");
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            List<ImageDTO> images = new ArrayList<>();
            while (resultSet.next()) {
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageIdx(resultSet.getLong("image_idx"));
                imageDTO.setImageName(resultSet.getString("name"));
                imageDTO.setImageSize(resultSet.getInt("size"));
                imageDTO.setBoardIdx(resultSet.getLong("board_idx"));
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

        public ImageDTO save(Image image, long boardIdx){
            ImageDTO imageDTO = new ImageDTO();
            try {
                statement = connection.prepareStatement("INSERT INTO image (image_name, image_size, board_idx) VALUES (?, ?, ?)");
                statement.setString(1, image.getImageName().getImageName());
                statement.setInt(2, image.getImageSize().getImageSize());
                statement.setLong(3, boardIdx);
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    imageDTO.setImageIdx(generatedKeys.getLong(1));
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

// 사용안함
/*    public ImageDTO update(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        try {
            statement = connection.prepareStatement("UPDATE image SET image_name = ?, image_size = ?, board_idx = ? WHERE image_idx = ?");

            statement.setString(1, image.getImageName().toString());
            statement.setInt(2, image.getImageSize().getImageSize());
            statement.setLong(3, image.getBoardIdx().getBoardIdx());
            statement.setLong(4, image.getImageIdx().getImageIdx());

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
    }*/

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