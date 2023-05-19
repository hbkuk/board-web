package com.study.repository.file;

import com.study.dto.FileDTO;
import com.study.model.file.file;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class fileDAO {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public fileDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebrainsoft_study", "ebsoft", "ebsoft");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public FileDTO findById(Long id) {
        FileDTO fileDTO = new FileDTO();
        try {
            statement = connection.prepareStatement("SELECT * FROM file WHERE file_idx = ?");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fileDTO.setFileIdx(resultSet.getLong("file_idx"));
                fileDTO.setSaveFileName(resultSet.getString("save_name"));
                fileDTO.setOriginalFileName(resultSet.getString("original_name"));
                fileDTO.setFileSize(resultSet.getInt("size"));
                fileDTO.setBoardIdx(resultSet.getLong("board_idx"));
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
        return fileDTO;
    }

    public boolean hasImageByBoardId(long boardId) {
        try {
            String sql = "SELECT * FROM file WHERE board_idx = ? LIMIT 1";
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


    public List<FileDTO> findImagesByBoardId(Long boardIdx) {
        try {
            statement = connection.prepareStatement("SELECT * FROM file WHERE board_idx = ?");
            statement.setLong(1, boardIdx);
            resultSet = statement.executeQuery();
            List<FileDTO> files = new ArrayList<>();
            while (resultSet.next()) {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setFileIdx(resultSet.getLong("file_idx"));
                fileDTO.setSaveFileName(resultSet.getString("save_name"));
                fileDTO.setOriginalFileName(resultSet.getString("original_name"));
                fileDTO.setFileSize(resultSet.getInt("size"));
                fileDTO.setBoardIdx(resultSet.getLong("board_idx"));
                files.add(fileDTO);
            }
            return files;
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

        public FileDTO save(file file, long boardIdx){
            FileDTO fileDTO = new FileDTO();
            try {
                statement = connection.prepareStatement("INSERT INTO file (save_name, original_name, size, board_idx) VALUES (?, ?, ?, ?)");
                statement.setString(1, file.getOriginalName().getFileName());
                statement.setInt(2, file.getFileSize().getImageSize());
                statement.setLong(3, boardIdx);
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    fileDTO.setFileIdx(generatedKeys.getLong(1));
                }
                return fileDTO;
            } catch (SQLException e) {
                e.printStackTrace();
                return fileDTO;
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
            statement = connection.prepareStatement("DELETE FROM file WHERE file_idx = ?");
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
            statement = connection.prepareStatement("DELETE FROM file WHERE board_idx = ?");
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