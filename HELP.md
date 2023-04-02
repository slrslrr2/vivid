# 테이블 정의서

### Table definition

```
CREATE TABLE `tb_song` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `melon_id` bigint(20) DEFAULT 0 COMMENT 'melon id',
  `song_name` varchar(200) NOT NULL COMMENT '노래제목',
  `artist` varchar(100) NOT NULL COMMENT '아티스트',
  `rank` int(4) DEFAULT NULL COMMENT '순위',
  `create_date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `melon_id` (`melon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `tb_song_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `melon_id` bigint(20) DEFAULT 0 COMMENT 'melon id',
  `album_name` varchar(100) NOT NULL COMMENT '앨범명',
  `img_url` varchar(200) NOT NULL COMMENT '커버 이미지 URL',
  `release_date` varchar(10) NOT NULL COMMENT '발매일',
  `genre` tinyint(4) NOT NULL COMMENT '장르',
  `create_date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `melon_id` (`melon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `tb_song_lyrics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `melon_id` bigint(20) DEFAULT 0 COMMENT 'melon id',
  `song_name` varchar(200) NOT NULL COMMENT '노래제목',
  `artist` varchar(100) NOT NULL COMMENT '아티스트',
  `lyrics` text NOT NULL COMMENT '가사',
  `create_date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `melon_id` (`melon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```