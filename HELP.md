# 테이블 정의서

### Table definition

```
DROP TABLE IF EXISTS `tb_song`;
CREATE TABLE tb_song (
    id bigint NOT NULL AUTO_INCREMENT,
    melon_id bigint DEFAULT 0 COMMENT 'melon id',
    song_name VARCHAR(100) NOT NULL COMMENT '노래제목',
    artist VARCHAR(30) NOT NULL COMMENT '아티스트',
    rank int(4) COMMENT '순위',
    create_date DATE NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `melon_id` (`melon_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_song_detail`;
CREATE TABLE tb_song_detail (
    id bigint NOT NULL AUTO_INCREMENT,
    melon_id bigint DEFAULT 0 COMMENT 'melon id',
    album_name VARCHAR(100) NOT NULL COMMENT '앨범명',
    img_url VARCHAR(200) NOT NULL COMMENT '커버 이미지 URL',
    release_date VARCHAR(10) NOT NULL COMMENT '발매일',
    genre tinyint(4) NOT NULL COMMENT '장르',
    create_date DATE NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `melon_id` (`melon_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tb_song_lyrics`;
CREATE TABLE tb_song_lyrics (
    id bigint NOT NULL AUTO_INCREMENT,
    melon_id bigint DEFAULT 0 COMMENT 'melon id',
    song_name VARCHAR(100) NOT NULL COMMENT '노래제목',
    artist VARCHAR(30) NOT NULL COMMENT '아티스트',
    lyrics TEXT NOT NULL COMMENT '가사',
    create_date DATE NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `melon_id` (`melon_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```