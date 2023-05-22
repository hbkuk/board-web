package com.study.repository.file;

import com.study.dto.FileDTO;
import com.study.model.file.File;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileDAO {

    public FileDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public FileDTO findById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        FileDTO fileDTO = new FileDTO();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
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
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Handle exception
                e.printStackTrace();
            }
        }
        return fileDTO;
    }

    public boolean hasImageByBoardId(long boardId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
            String sql = "SELECT * FROM file WHERE board_idx = ? LIMIT 1";
            statement = connection.prepareStatement(sql);
            statement.setLong(1, boardId);
            resultSet = statement.executeQuery();
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
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List<FileDTO> findImagesByBoardId(Long boardIdx) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
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
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

        public FileDTO save(File file, long boardIdx){
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            FileDTO fileDTO = null;
            log.debug("File Save -> Save File Name : {} ", file.getSaveFileName());

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
                preparedStatement = connection.prepareStatement("INSERT INTO file (save_name, original_name, size, board_idx) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, file.getSaveFileName());
                preparedStatement.setString(2, file.getOriginalName().getFileName());
                preparedStatement.setInt(3, file.getFileSize().getImageSize());
                preparedStatement.setLong(4, boardIdx);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected == 1) {
                    log.debug("File Save 성공");
                    fileDTO = new FileDTO();
                    fileDTO.setBoardIdx(boardIdx);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return fileDTO;
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
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
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
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteAllByBoardIdx(Long board_idx) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3316/ebsoft", "ebsoft", "123456");
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
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    }