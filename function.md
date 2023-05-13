### 인기검색어

1. nginx 통하여 인기검색어 로그 쌓기
   1. mac
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
   2. 인기검색어 BackEnd URL
2. 쌓인 데이터를 Logstash 에서 input 하여, output
3. kibana에서 데이터 잘나오나 QueryDSL 만들어보기
4. 백엔드에서 가져오기