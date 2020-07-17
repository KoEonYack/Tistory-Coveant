# dial tcp 127.0.0.1:3306: connect: connection refused 에러 해결

<br />

# 오류 확인

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/dia_tcp_error/img/1-1.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

MySQL docker와 GO docker project 파일을 연결하려고 할 때 Go docker의 빌드는 성공하였으나 실행(docker run)하려고 하면 dial tcp 127.0.0.1:3306: connect: connection refused 오류가 발생하는 것을 확인할 수 있습니다. 

<br />
<br />


# 문제 해결

<br />

``` go
db, err = sql.Open("mysql", dbUser+":"+dbPassword+"@tcp(localhost:3306)/testDB")

if err != nil {
    panic(err.Error())
}
```

<br />


위의 코드와 같이 테스트 개발 환경에서 DB의 IP주소를 localhost로 로컬에 설치된 MySQL을 접속한다면 문제가 없을 것입니다. 그러나 Docker compose로 MySQL을 동작하면 localhost라는 이름으로 접속할 수 없기에 발생하는 문제입니다. 본 문제를 해결하기 위해서는 `localhost`를 docker.for.mac.localhostDocker로 변경하여 데몬이 이 이름을 호스트 IP에 자동으로 매핑하게 하면 됩니다. 


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/dia_tcp_error/img/2-2.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<center> 성공 </center>
<br />


이 문제는 Mac에서 발생하는 문제입니다. 공식 docker docs [I WANT TO CONNECT FROM A CONTAINER TO A SERVICE ON THE HOST](https://docs.docker.com/docker-for-mac/networking/#per-container-ip-addressing-is-not-possible)를 참고하세요.



<br />
<br />

