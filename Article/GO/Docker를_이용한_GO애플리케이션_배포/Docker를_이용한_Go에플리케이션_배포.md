# Docker를 이용하여 클라우드에 Go 배포하기


<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Docker%EB%A5%BC_%EC%9D%B4%EC%9A%A9%ED%95%9C_GO%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98_%EB%B0%B0%ED%8F%AC/img/main.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

본 글에서 사용할 기술스택인 Docker, Docker-compose, Gin, MySQL, Naver Cloud Platform입니다. Docker와 GO(Gin-Gonic)를 이용해서 클라우드(NCloud-MiniServer)에 배포해보겠습니다. NCloud가 아니더라도 사용하는 클라우드 인스턴스를 사용하면 됩니다.


<br />
<br />

# 1. NCloud(Cloud 준비) 

<br />


Docker를 배포하기 위한 서버가 필요합니다. 본 글에서 Mini Server를 사용하겠습니다. NCloud Mini Server는 1년간 무료로 이용할 수 있습니다. AWS Free Tier 기간이 종료된 분들이라면 이용해 볼법합니다. NCloud는 처음 가입한 분들에게 3개월 10만원 크래딧을 제공합니다. [NCloud 할인 링크](https://www.ncloud.com/main/creditEvent) 


<br />
<br />


# 2. Go 설치

<br />


``` text
wget https://dl.google.com/go/go1.14.4.linux-amd64.tar.gz
tar -C /usr/local -xzf go1.14.4.linux-amd64.tar.gz
```

이 글을 20년 6월 현재 `apt-get install golang`으로 설치면 Go 1.10 버전이 설치됩니다. 이렇게 설치할 경우 본 글의 Docker 설정에 맞지 않습니다. [https://golang.org/dl/](https://golang.org/dl/) Golang 공식 홈페이지에는 go 1.14.4가 stable version입니다. 따라서 wget 명려어를 이용하여 설치하겠습니다.


<br />

``` text
export PATH=$PATH:/usr/local/go/bin
source ~/.profile
```

Go가 설치된 경로를 추가해줍니다. 


<br />


``` text
go version
>
go version go1.14.4 linux/amd64
```
설치한 Go 1.14.4 버전을 확인합니다.

<br />


``` text
go get -u github.com/gin-gonic/gin
```

Gin-Gonic이라는 Go 프레임워크를 사용할 것입니다. 위의 명령어를 입력하여 Gin-Gonic을 설치합니다. 

<br />


``` text
go get -u github.com/go-sql-driver/mysql
```

MySQL을 사용할 것이기에 Go와 MySQL을 연결해주는 드라이버를 설치합니다.


<br />
<br />

# 2. Docker 설치


<br />

``` text
sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common 
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - 
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable" 
sudo apt update 
apt-cache policy docker-ce
sudo apt install docker-ce
```

위의 명령어를 차례로 입력하여 Docker를 설치하도록 합니다. __Dockerfile, docker-compose.yml, go.mod, go.sum, main.go__ 의 코드를 [Code-Github](https://github.com/KoEonYack/go-lang-study/tree/master/docker-go-example) 에서 가져오도록 합니다. 본 Go 코드는 [mysql curd (with Gin)](https://dejavuqa.tistory.com/331) 에서 가져왔습니다.


<br />
<br />


# 3. MySQL 도커 이미지 가져오기

<br />

``` text
docker pull mysql:8.0.19
```
MySQL 8.0.19 버전의 도커 이미지를 리눅스 클라우드 머신에 가져옵니다.


<br />

``` text
docker run \
	--detach \
	--publish 3306:3306 \
	--env MYSQL_USER="root" \
	--env MYSQL_ROOT_PASSWORD=1234 \
	mysql:8.0.19;
```
- run: MySQL 도커 이미지를 실행합니다.
- --detach: 백그라운드에서 동작합니다. docker ps를 입력하면 현재 실행중인 도커 이미지 목록을 볼 수 있습니다. 
- --env: MySQL의 User ID를 root로, 초기 비밀번호를 1234로 설정합니다.
- mysql:8.0.19: 실행할 도커 이미지 이름이빈다.


<br />


``` text
mysql -h 0.0.0.0 -u root -p
```
위의 명령어를 입력하면 mysql 명령어를 찾을 수 없다고 나올 것입니다. 왜냐하면 MySQL을 리눅스 머신에 직접 설치한 것이 아니라 MySQL Docker Img를 가져온 것이기 때문입니다. 

<br />


``` text
docker ps
>

CONTAINER ID        IMAGE               COMMAND               
4a980a0d7dc9        mysql:8.0.19        "docker-entrypoint.s…"
```

docker ps를 통해서 MySQL의 CONTAINER ID를 확인하빈다.

<br />

``` text
docker exec -i -t 4a980a0d7dc9 bash
mysql -uroot -p
```
위의 명령어를 이용해서 docker의 bash에 접속합니다. 접속한 docker에서 MySql에 접속합니다. 위에서 --env의 인자로 주었던 root와 1234 비밀번호를 입력하면 MySQL에 접속할 수 있습니다.

<br />


``` text
create database user_db;
show databases;
use user_db;
show tables;
```
위의 명령어를 차례대로 입력합니다. 명령어의 의미는 위에서 차례대로 다음과 같습니다.
- user_db: user_db 데이터베이스를 생성합니다.
- show databases: MySQL에 저장된 데이터베이스 목록을 보여줍니다.
- use user_db: user_db 데이터베이스에 접근합니다.  
- show tables: user_db에 저장된 테이블을 보여줍니다. 현재 추가한 데이터가 없기에 아무것도 보이지 않을 것입니다. 

<br />


``` sql
CREATE TABLE IF NOT EXISTS person(
    id INT AUTO_INCREMENT,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=INNODB;
```

위와 같은 쿼리를 만듭니다. 위의 테이블에 클라이언트가 보낸 데이터가 저장될 것입니다.

<br />
<br />


# 4. Docker-compose 설치와 실행

<br />

``` text
apt install docker-compose
```

docker compose를 설치합니다.

<br />


``` text
docker-compose up -d
```
docker-compose.yml 파일이 있는 곳에서 위의 명령어를 입력합니다. -d 옵션을 이용해서 백그라운드로 실행합니다. 이 명령어로 Docker Compose에 정의되어있는 모든 서비스 컨테이너를 한 번에 실행할 수 있습니다. 
`docker-compose down`을 이용하면 docker-compose.yml에 정의된 서비스 컨테이너를 한 번에 종료합니다.

<br />
<br />


# 5. Docker 실행

<br />


``` text
docker build . -t go-dock
docker run -p 3001:3001 go-dock
```

docker를 빌드하고, 실행합니다. 3001번 포트로 실행합니다.

<br />
<br />

# 6. 실행 확인


<br />
<br />

## 6-1. ping


``` text
curl http://49.50.175.XX:3001/ping
```

NCloud 서버가 아닌 로컬 컴퓨터의 터미널에서 위와 같은 명령어를 입력합니다.
* __49.50.175.XX__ 는 NCloud의 Public IP 입니다.


<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Docker%EB%A5%BC_%EC%9D%B4%EC%9A%A9%ED%95%9C_GO%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98_%EB%B0%B0%ED%8F%AC/img/ping.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />


``` go
router.GET("/ping", func(c *gin.Context) {
    c.JSON(200, gin.H{
        "message": "pong",
    })
})
```

Github의 [main.go 코드](https://github.com/KoEonYack/go-lang-study/blob/master/docker-go-example/main.go)를 보면 /ping으로 GET Method에 대해서 JSON으로 "pong을 보냅니다." 


<br />
<br />

## 6-2. POST


``` text
curl http://49.50.175.XX:3001/person -X POST -d '{"first_name": "fn_3", "last_name": "ln"}' -H "Content-Type: application/json"
> {"message":" fn_3 ln successfully created"}
```
curl의 명령이 성공적이라면  {"message":" fn_3 ln successfully created"}가 터미널에 출력될 것입니다.


<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Docker%EB%A5%BC_%EC%9D%B4%EC%9A%A9%ED%95%9C_GO%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98_%EB%B0%B0%ED%8F%AC/img/post_res.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

Docker를 실행한 서버에서는 빨간색 박스와 같이 Post 메소드로 값을 200으로 받은 것을 볼 수 있습니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Docker%EB%A5%BC_%EC%9D%B4%EC%9A%A9%ED%95%9C_GO%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98_%EB%B0%B0%ED%8F%AC/img/post_mysql.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

MySQL 도커 이미지에 접속해서 확인해보면 정상적으로 값이 저장된 것을 확인할 수 있습니다. (id 1, 2는 이전에 미리 넣어놓은 값입니다.)


<br />
<br />


## 6-3. GET

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Docker%EB%A5%BC_%EC%9D%B4%EC%9A%A9%ED%95%9C_GO%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98_%EB%B0%B0%ED%8F%AC/img/get_1.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

`http://49.50.175.XX:3001/persons/` 를 브라우저에 입력하면 DB의 모든 값을 JSON으로 보여줍니다.



<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Docker%EB%A5%BC_%EC%9D%B4%EC%9A%A9%ED%95%9C_GO%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98_%EB%B0%B0%ED%8F%AC/img/get_2.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

`http://49.50.175.XX:3001/person/1` 를 브라우저에 입력하면 DB의 첫번째 값을 JSON으로 보여줍니다.


<br />
<br />
<br />



