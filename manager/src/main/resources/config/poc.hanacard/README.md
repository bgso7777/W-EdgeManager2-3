# W-EdgeManager 환경설정
## 하나카드 POC 지원

---
### 1. 기본정보   
   1. H/W (infra)
      1. 인젠트 빌드 서버 
         1. public IP : 221.151.181.89   (서비스 입구)
         2. private IP : 192.168.100.161
      2. 이인규 데스크탑
         1. private IP : 192.168.150.45
   2. S/W (product)
      1. admin
         1. public : http://221.151.181.89:8180/websquare/websquare.html?w2xPath=/cm/main/login.xml
         2. private : http://192.168.150.45:8080/websquare/websquare.html?w2xPath=/cm/main/login.xml
      2. manager & websocket
         1. public : 221.151.181.89:8181
         2. private : 192.168.150.45:8081, 8082 (backup)
      3. svn
         1. public : svn://221.151.181.89:8690
         2. private : svn://192.168.150.45:3690 

### 2. 방화벽 해제 (ubuntu)
   1. sudo iptables -I INPUT 1 -p tcp --dport 8690 -j ACCEPT
   2. sudo iptables -I INPUT 1 -p tcp --dport 8180 -j ACCEPT 
   3. sudo iptables -I INPUT 1 -p tcp --dport 8181 -j ACCEPT

### 3. Port Forward
   1. 개요
      - SVN:// 프로토콜은 TCP 기반 동작이므로, 포트포워딩이 필요함. 
      - (nginx로도 처리가능한듯 보이지만.. 더 쉬운걸로!)
   2. 설치
      - sudo apt-get install socat 
   3. 실행
      - nohup socat TCP-LISTEN:8690,fork TCP:192.168.150.45:3690 < /dev/null &

### 4. NGINX
   1. 설치
      1. sudo apt install nginx
      2. 파일 생성 & nginx.conf include & link
         1. /etc/nginx/sites-available/main.conf
         2. ln -s /etc/nginx/sites-available/main.conf /etc/nginx/sites-enabled/main.conf
   2. 재기동
      1. service nginx restart 
   3. API & EventSource
      1. poc.hanacard\nginx\main.conf (참조)
         1. 8181 
            1. location /api/
   4. Websocket
      1. poc.hanacard\nginx\main.conf (참조)
         1. 8181 
            1. location /