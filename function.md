### 인기검색어

1. nginx 통하여 인기검색어 로그 쌓기
   ```
   Q. nginx 위치 확인
   A. brew info nginx
   
   Q.ngnix 시작/종료/상태
   brew services start nginx
   brew services stop nginx
   brew services status nginx
   
   ps -ef|grep ngnix
   netstat -anv | grep "7776"
   
   kill -9 21781
   ```
   <br>

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
    
  <br>
2. 쌓인 데이터를 Logstash 에서 input 하여, output

      ##### - logstash (search-log.conf)
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

```

GET nginx-*/_search
{
  "query": {
    "range": {
      "@timestamp": {
        "gte": "now-30d",
        "lte": "now"
      }
    }
  }, 
  "size": 0,
  "aggs": {
    "params_searchWord": {
      "terms": {
        "field": "params.searchWord.keyword",
        "size": 10,
        "order": {
          "_count": "desc"
        }
      }
    }
  }
}

```