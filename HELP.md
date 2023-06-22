# INFO
- **BACKEND**
  - PORT: 7777
- **FRONTEND**
  - PORT: 7776

# 배포

```
// 1. 도커 이미지 빌드
docker build -t dream:0.0.1 .

// 2. Docker Container 실행
docker run -d -p 8080:8080 dream:0.0.1

// 3. 
```

```
로컬 PC에서 개발을 한 후 Github에 Push를 합니다.
Github Repository 특정 branch에 push가 되면 Github Action이 동작을 시작합니다.
Github Actions가 Github Container Registry에 소스를 받은 후 Docker 이미지로 빌드를 합니다.
빌드된 이미지를 EC2에 등록된 Runner가 복사합니다.
기존 이미지를 삭제하고 새로운 이미지로 실행을 합니다.

https://codegear.tistory.com/84 참조
```

# 테이블 정의서

### Table definition

```
CREATE TABLE `tb_song` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `melon_id` bigint(20) DEFAULT 0 COMMENT 'melon id',
  `song_name` varchar(200) NOT NULL COMMENT '노래제목',
  `artist` varchar(100) NOT NULL COMMENT '아티스트',
  `ranking` int(4) DEFAULT NULL COMMENT '순위',
  `create_date` date NOT NULL,
  PRIMARY KEY (`id`)
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

### Elasticsearch
```
관련 jdk Elasticsearch ssl 연결
keytool -importcert -keystore ${JAVA_HOME}/jre/lib/security/cacerts -storepass changeit -file xxx.cer
keytool -importcert -keystore /Library/Java/JavaVirtualMachines/jdk-11.0.14.jdk/Contents/Home/lib/security/cacerts -storepass changeit -file /Users/geumbit/sideproject/ELK/elasticsearch-8.2.2/config/certs/http_ca.crt -alias elastic
```

#### 1. Logstash

##### 1-1. song_info.conf
```
input {
  jdbc {
    jdbc_driver_library => "~/mysql-connector-j-8.0.31.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://"
    jdbc_user => ""
    jdbc_password => ""
    statement => "SELECT * FROM tb_song_lyrics"
    schedule => "10 0 * * *"
  }
}
 filter {
    ruby{
     code => "
          event.set('kr_time', event.timestamp.time.localtime.strftime('%Y%m%d%H%M'))
          "
   }
 }

output {
  elasticsearch {
    hosts => 
    index => "song_info-%{kr_time}"
    document_type => "_doc" # document type 명
    document_id => "%{[id]}"
    user => ""
    password => ""
    ssl => true
    ssl_certificate_verification => false
  }
}
```

##### 1-2. pipelines.yml

```
- pipeline.id: song
  path.config: "/Users/geumbit/sideproject/ELK/logstash-8.2.2/config/conf.d/song_info.conf"
```

#### 2. Index Template
```
{
  "template": {
    "settings": {
      "index": {
        "max_ngram_diff": "20",
        "routing": {
          "allocation": {
            "include": {
              "_tier_preference": "data_content"
            }
          }
        },
        "analysis": {
          "filter": {
            "synonym_artist_filter": {
              "type": "synonym",
              "synonyms_path": "analysis/synonyms_artist.txt",
              "updateable": "true"
            }
          },
          "char_filter": {
            "br_strip_filter": {
              "type": "mapping",
              "mappings": [
                "<br /> =>"
              ]
            },
            "nbsp_char_filter": {
              "type": "mapping",
              "mappings": [
                "\\u0020=>"
              ]
            }
          },
          "analyzer": {
            "ngram_white_char_analyzer_long": {
              "filter": [
                "lowercase",
                "trim"
              ],
              "char_filter": [
                "br_strip_filter",
                "nbsp_char_filter"
              ],
              "type": "custom",
              "tokenizer": "ngram_tokenizer_long"
            },
            "synonym_ngram_white_char_artist_analyzer": {
              "filter": [
                "lowercase",
                "synonym_artist_filter"
              ],
              "char_filter": [
                "nbsp_char_filter"
              ],
              "tokenizer": "ngram_tokenizer_short"
            },
            "ngram_white_char_analyzer_short": {
              "filter": [
                "lowercase",
                "trim"
              ],
              "char_filter": [
                "br_strip_filter",
                "nbsp_char_filter"
              ],
              "type": "custom",
              "tokenizer": "ngram_tokenizer_short"
            }
          },
          "tokenizer": {
            "ngram_tokenizer_short": {
              "token_chars": [
                "letter",
                "digit",
                "punctuation",
                "symbol"
              ],
              "min_gram": "1",
              "type": "ngram",
              "max_gram": "10"
            },
            "ngram_tokenizer_long": {
              "token_chars": [
                "letter",
                "digit",
                "punctuation",
                "symbol"
              ],
              "min_gram": "3",
              "type": "ngram",
              "max_gram": "20"
            }
          }
        },
        "number_of_shards": "5",
        "number_of_replicas": "1"
      }
    },
    "mappings": {
      "dynamic_templates": [],
      "properties": {
        "artist": {
          "type": "text",
          "analyzer": "ngram_white_char_analyzer_short",
          "search_analyzer": "synonym_ngram_white_char_artist_analyzer"
        },
        "create_date": {
          "type": "date"
        },
        "id": {
          "type": "long"
        },
        "lyrics": {
          "type": "text",
          "analyzer": "ngram_white_char_analyzer_long"
        },
        "melon_id": {
          "type": "long"
        },
        "song_name": {
          "type": "text",
          "analyzer": "ngram_white_char_analyzer_short"
        }
      }
    },
    "aliases": {}
  }
}
```
