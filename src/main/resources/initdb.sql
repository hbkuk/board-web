drop table if exists tb_comment;
drop table if exists tb_file;
drop table if exists tb_board;
drop table if exists tb_category;

CREATE TABLE `tb_board` (
                            `board_idx` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `category_idx`	INT	NOT NULL,
                            `title`	varchar(200) NOT NULL,
                            `writer`	varchar(10)	NOT NULL,
                            `content`	varchar(2000)	NOT NULL,
                            `password`	varchar(16)	NOT NULL,
                            `hit`	INT	NOT NULL,
                            `regdate`	datetime	NOT NULL,
                            `moddate`	datetime	NULL
);

CREATE TABLE `tb_comment` (
                              `comment_idx`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              `writer`	varchar(10)	NOT NULL,
                              `password`	varchar(16)	NOT NULL,
                              `content`	varchar(2000)	NOT NULL,
                              `regdate`	datetime	NOT NULL,
                              `board_idx`	BIGINT	NOT NULL
);

CREATE TABLE `tb_file` (
                           `file_idx`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `saved_name`	varchar(255)	NOT NULL,
                           `original_name`	varchar(255)	NOT NULL,
                           `size`	INT	NOT NULL,
                           `board_idx`	BIGINT	NOT NULL
);

CREATE TABLE `tb_category` (
                               `category_idx`	INT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               `category`	varchar(100)	NOT NULL
);


ALTER TABLE `tb_board` ADD CONSTRAINT `FK_tb_category_TO_tb_board_1` FOREIGN KEY (
                                                                                  `category_idx`
    )
    REFERENCES `tb_category` (
                              `category_idx`
        );

ALTER TABLE `tb_comment` ADD CONSTRAINT `FK_tb_board_TO_tb_comment_1` FOREIGN KEY (
                                                                                   `board_idx`
    )
    REFERENCES `tb_board` (
                           `board_idx`
        );

ALTER TABLE `tb_file` ADD CONSTRAINT `FK_tb_board_TO_tb_file_1` FOREIGN KEY (
                                                                             `board_idx`
    )
    REFERENCES `tb_board` (
                           `board_idx`
        );

INSERT INTO tb_category (category) VALUES
    ('HTML'),
    ('CSS'),
    ('JAVASCRIPT'),
    ('SPRING'),
    ('JPA');

INSERT INTO tb_board (board_idx, category_idx, title, writer, content, password, hit, regdate, moddate) VALUES
    (1, 1, 'Title 1', '테스터1', 'Content 1', 'password1!', 10, '2023-05-18 10:00:00', null),
    (2, 2, 'Title 2', '테스터2', 'Content 2', 'password2!', 5, '2023-05-18 11:00:00', null),
    (3, 3, 'Title 3', '테스터3', 'Content 3', 'password3!', 8, '2023-05-18 12:00:00', null);


