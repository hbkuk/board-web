CREATE TABLE board (
                       board_idx BIGINT NOT NULL auto_increment,
                       category VARCHAR(100) NOT NULL,
                       title VARCHAR(200) NOT NULL,
                       writer VARCHAR(10) NOT NULL,
                       content VARCHAR(2000) NOT NULL,
                       password VARCHAR(16) NOT NULL,
                       hit INT NOT NULL,
                       regdate DATETIME NOT NULL,
                       moddate DATETIME NULL,

                       PRIMARY KEY (board_idx)
);

CREATE TABLE comment (
                         comment_idx BIGINT NOT NULL auto_increment,
                         writer VARCHAR(10) NOT NULL,
                         password VARCHAR(16) NOT NULL,
                         content VARCHAR(2000) NOT NULL,
                         regdate DATETIME NOT NULL,
                         board_idx BIGINT NOT NULL,

                         PRIMARY KEY (comment_idx),
                         FOREIGN KEY (board_idx) REFERENCES board (board_idx)
);

CREATE TABLE file (
                      file_idx BIGINT NOT NULL auto_increment,
                      save_name VARCHAR(255) NOT NULL,
                      original_name VARCHAR(255) NOT NULL,
                      size int NOT NULL,
                      board_idx BIGINT NOT NULL,

                      PRIMARY KEY (file_idx),
                      FOREIGN KEY (board_idx) REFERENCES board (board_idx)
);



INSERT INTO board (board_idx, category, title, writer, content, password, hit, regdate, moddate)
VALUES
    (1, 'JAVA', 'Title 1', '테스터1', 'Content 1', 'password1!', 10, '2023-05-18 10:00:00', '2023-05-18 12:00:00'),
    (2, 'JAVASCRIPT', 'Title 2', '테스터2', 'Content 2', 'password2!', 5, '2023-05-18 11:00:00', '2023-05-18 13:00:00'),
    (3, 'SPRING', 'Title 3', '테스터3', 'Content 3', 'password3!', 8, '2023-05-18 12:00:00', '2023-05-18 14:00:00');


INSERT INTO comment (comment_idx, writer, password, content, regdate, board_idx)
VALUES
    (1, '테스터4', 'password1!', 'Comment 1', '2023-05-18 10:30:00', 1),
    (2, '테스터5', 'password1!', 'Comment 2', '2023-05-18 11:30:00', 1),
    (3, '테스터6', 'password1!', 'Comment 3', '2023-05-18 12:30:00', 2);

INSERT INTO file (file_idx, save_name, original_name, size, board_idx)
VALUES
    (1, 'image1.jpg', 'image1.jpg', 1024, 1),
    (2, 'image2.jpg', 'image2.jpg', 2048, 1),
    (3, 'image3.jpg', 'image3.jpg', 3072, 2);
