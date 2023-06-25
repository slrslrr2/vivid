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
```

```
로컬 PC에서 개발을 한 후 Github에 Push를 합니다.
Github Repository 특정 branch에 push가 되면 Github Action이 동작을 시작합니다.
Github Actions가 Github Container Registry에 소스를 받은 후 Docker 이미지로 빌드를 합니다.
빌드된 이미지를 EC2에 등록된 Runner가 복사합니다.
기존 이미지를 삭제하고 새로운 이미지로 실행을 합니다.

https://codegear.tistory.com/84 참조
```

---

# Kafka install 

docker-compose.yml
```
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka:2.12-2.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```
<br>

Kafka 정상설치 테스트
```
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic testTopic

카프카 생성
geumbit@gimgeumbich-ui-MacBookPro kafka % docker exec -it kafka kafka-console-producer.sh --topic testTopic --broker-list 0.0.0.0:9092

컨슈머 생성
docker exec -it kafka kafka-console-consumer.sh --topic testTopic --bootstrap-server localhost:9092
```

---

# 이벤트 쿠폰 발급 프로세스

동시성 이슈 관련 정리
1. 선착순 50명에게 스트리밍 무료 서비스 쿠폰을 발급해준다.
2. 순간적으로 DB에 몰리는 트래픽부분을 생각하여
  - Kafka를 이용해 쿠폰관련 Topic을 생성하여 (Producer, Consumer) DB를 요청하여 분산한다.
3. 1인당 1명으로 제한한다.
  - 쿠폰발급여부를 확인하는데 DB에까지 접근할 경우, Consumer에서 발급되어질 쿠폰 프로세스와 정확성이 떨어질 수 있다.
    => redis sadd(set) 사용하여 쿠폰 발급 userId를 따로 저장한다.
4. raceCondition으로 인한 동시성 문제를 생각한다.
  - 해당 메소드에 한꺼번에 트래픽이 발생함으로써,
    commit 되지 않은 직전 시점에 race condition을 생각
    => redis를 이용하여 [쿠폰발급 수량] 정확성에 초점을 두고
    redisTemplate.opsForValue().increment("coupon_count"); 을 사용한다.

5. Executor를 만들어서 동시접근 사용자로 인한 정합성 테스트 코드 확인

```
teminal에서 Consumer로 이벤트 메시지 확인

docker exec -it kafka [kafka-console-consumer.sh](http://kafka-console-consumer.sh/) --topic coupon_create --bootstrap-server localhost:9092 --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
```

---

# 통합검색 구현 Elasticsearch Setting

관련 jdk Elasticsearch ssl 연결
```
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


# nginx 통하여 인기검색어 로그 쌓기

   ```
   Q. nginx 위치 확인
   brew info nginx
   
   Q.ngnix 시작/종료/상태
   brew services start nginx
   brew services stop nginx
   brew services status nginx
   
   ps -ef|grep ngnix
   netstat -anv | grep "7776"
   
   kill -9 21781
   ```

   ##### - nginx.conf

   ```
   http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;

    log_format main '$remote_addr - $remote_user [$time_local] "$request" $status $body_bytes_sent "$http_referer" "$http_user_agent"';

    access_log /opt/homebrew/var/log/nginx/access.log;
    error_log /opt/homebrew/var/log/nginx/error.log;

    server {
        listen 80;
        server_name vivid.co; 

        location /api/search/song/info {
             access_log /opt/homebrew/var/log/nginx/search.log main;

              proxy_set_header X-Real-IP            $remote_addr;
              proxy_set_header X-Forwarded-Host     $host;
              proxy_set_header X-Forwarded-Server   $host;
              proxy_set_header X-Forwarded-For      $proxy_add_x_forwarded_for;
              proxy_set_header Host                 $http_host;
              proxy_set_header X-SSL-Server         true;
              proxy_pass   <http://localhost:7776/api/search/song/info>;
        }

        location / {
              proxy_set_header X-Real-IP            $remote_addr;
              proxy_set_header X-Forwarded-Host     $host;
              proxy_set_header X-Forwarded-Server   $host;
              proxy_set_header X-Forwarded-For      $proxy_add_x_forwarded_for;
              proxy_set_header Host                 $http_host;
              proxy_set_header X-SSL-Server         true;
              proxy_pass   <http://localhost:7776>;
        }
    }
}
```

<br>
2. 쌓인 데이터를 Logstash 에서 input 하여, output logstash (search-log.conf)

```
input {
  file {
    path => ["/opt/homebrew/var/log/nginx/search.log"]
#    sincedb_path => "/dev/null"
#    start_position => "beginning"

    sincedb_path => "/Users/geumbit/sideproject/ELK/logstash-8.2.2/sincedb/sincedb.txt"
    start_position => "end"
#    codec => line #
  }
}

filter {
  grok {
    patterns_dir => "/usr/share/logstash/patterns"
    match => { "message" => "%{NGINX_ACCESS}" }
    add_field => [ "received_at", "%{@timestamp}" ]
  }

  grok {
    match => { "request" => ["%{URIPATH:uri_path}"] }
  }

  grok {
    match => { "request" => ["%{URIPARAM:uri_param}"] }
  }

  kv { #키-값 형식 데이터 구문 분석 추출
    source => "request"
    field_split => "&?" # & 혹은 ? 로 구분
    value_split => "="
    target => "params"
  }

  urldecode {
    charset => "UTF-8"
    field => "params"
  }
}

output {
  elasticsearch {
    hosts => ["https://localhost:9200"]
    index => "nginx-search.log-%{+YYYY.MM}"
    document_type => "_doc" # document type 명
    document_id => "%{@timestamp}"
    user => "elastic"
    password => "********"
    ssl => true
    ssl_certificate_verification => false
    #cacert => "PATH\certs\http_ca.crt"
  }
}
```

<br>
3. kibana에서 데이터 잘나오나 QueryDSL 만들어보기