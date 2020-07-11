# Gin-gonic Hot Reloading 방법


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/gin-gonic_hot_reloading/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center> 칼퇴를 위해서 Gin이 빨리 리로딩되야한다... </center>

<br />


# Gin-gonic의 문제...

<br />


Gin은 Go의 웹 프레임워크 중 하나입니다. Go 프레임워크인 Beego와 다르게 Auto Reloading을 지원하지 않습니다. 파이썬 프레임워크인 Django의 경우 setting.py에서 DEBUG = True 값으로 설정되어 있으면 코드 변경이 발견되면 자동 재시작을 해주며 Node.js의 경우 nodemon이라는 강력한 패키지가 있습니다. Gin에서 자동 재시작을 하고 싶기에.. Stack Overflow, 여러 키워드로 구글링을 하여도 Docker에 올려서 Reloading을 하거나 지원하지 않은 오픈소스를 이용하라는 도움이 안되는 말 밖에 없었습니다.

<br />
<br />


# 오픈소스를 써볼까?


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/gin-gonic_hot_reloading/img/1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

[https://github.com/codegangsta/gin](https://github.com/codegangsta/gin) 

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/gin-gonic_hot_reloading/img/2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

[https://github.com/johannesboyne/gomon](https://github.com/johannesboyne/gomon) 

<br />


위의 두 오픈소스 모두 동작하지 않습니다. go run을 매번 입력하기 싫었기에 아이디어를 얻기 위해서 오픈소스를 열어본 결과 결국 go run을 어떻게 하냐의 문제였습니다.

<br />
<br />


# nodemon 활용


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/gin-gonic_hot_reloading/img/3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

Nodemon Github의 README.md를 보면 `.js` 파일 뿐만 아니라 다른 프로그램의 모니터링을 하는데 사용할 수 있으며 예시로 파이썬을 들고 있습니다. Gin-gonic의 hot reload를 위해서 nodemon을 준비해 봅시다.

<br />

``` text
brew update 
brew install nodemon
npm install -g nodemon
```

nodemon을 준비하는 MAC의 명령어입니다. 
hot loading을 감지하고 싶은 main.go가 있는 GO 프로젝트의 루트 디렉토리에 nodemon.json 파일을 만들고 아래와 같이 입력합니다.

<br />

``` json
{
    "watch": ["*"],
    "ext": "go graphql",
    "exec": "go run main.go && killall -9 | go run main.go "
}
```
TCP:1111에서 1111은 현재 실행하는 Go의 포트 번호를 입력하면 됩니다. 


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/gin-gonic_hot_reloading/img/4.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>nodemon을 이용한 hot reloading 동작</center>

<br />
<br />


# :bind address already used(포트 충돌) 문제 해결

<br />


잘 사용하다보면 포트 충돌이 일어나게 됩니다. 이는 Go 서버를 완전히 종료한 다음에 재시작이 일어나야 하는데 그러지 않은 상태에서 nodemon이 go 프로젝트를 다시 실해하기 때문에 발생하는 문제입니다. 이를 해결하기 위해서 아래와 같이 명령어를 수정합니다. 

``` text
go run main.go && lsof -n -i4TCP:1111 | xargs killall -9 && go run main.go
```

lsof 명령어로 1111 포트를 사용하는 PID를 추출합니다. 그다음 xargs 명령어로 추출한 PID를 killall 명령어로 넘깁니다. 그러면 포트 1111을 사용한 Go 프로세스가 죽게 되고 그 다음 go run으로 go 프로젝트가 실행됩니다.   

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/gin-gonic_hot_reloading/img/5.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center> 포트가 충돌이 일어나면 Go 프로세스를 죽이고 다시 시작합니다. </center>


<br />
<br />

# 응용

<br />

nodejs 개발할때 처럼 nodemon.json을 똑같이 사용하면 됩니다. env 환경변수를 nodemon.json에 그대로 사용할 수 있습니다. 아래는 nodemon.json에 데이터베이스 환경변수 값을 작성한 예시입니다.

<br />


``` text
{
    "watch": ["*"],
    "ext": "go graphql",
    "env": {
      "NODE_ENV": "development",
      "PRODENV": "dev",
      "SECRET_KEY": "temporalkey",
      "DB_HOST": "127.0.0.1",
      "DB_PORT": "3306",
      "DB_NAME": "test_db",
      "DB_USER": "root",
      "DB_PASSWORD": "1234",
      "INTERVAL": "5"
    },
    "exec": "go run main.go && lsof -n -i4TCP:1111 | xargs killall -9 && go run main.go "
  }
```

<br />
<br />

# 만능은 아닙니다.

<br />

<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/gin-gonic_hot_reloading/img/6.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<center> 빌드에 실패하면 nodemon이 종료됩니다. </center>
<br />

위의 이미지처럼 Go프로젝트 빌드에 실패하면 nodemon이 대기 상태가 아닌 프로세스가 죽습니다. 모델과 컨트롤러 작업을 하다보면 이런 상황이 자주 발생하는데 빌드에 실패하지 않게 작업한 후 nodemon으로 테스트를 하며 개발해야할 것입니다. 


<br />
<br />
<br />
