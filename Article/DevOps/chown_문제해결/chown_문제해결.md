# MySQL chown: changing ownership of 'var/lib/mysql': Operation not permitted 문제 해결


<br />
<br />

docker container는 외부와 격리되어 컨테이너 내부에 데이터를 관리합니다. 따라서 컨테이너가 파기되면 데이터는 사라집니다. 웹 서비스를 docker로 올릴 때 MySQL과 같은 데이터베이스를 docker에 올리는 경우 회원들의 정보가 사라지는 아찔한 경험을 할 수 있습니다. 따라서 이를 방지하기 위해 별도의 볼륨을 설정해서 데이터를 저장하여 docker-compose.yml에 경로를 저장합니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/4077e5f328fcdb6198328ae4ebfbd85737249393/Article/DevOps/chown_%EB%AC%B8%EC%A0%9C%ED%95%B4%EA%B2%B0/img/1.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>MySQL chown: changing ownership of 'var/lib/mysql': Operation not permitted 문제</center>

<br />

docker를 실행할 때 위의 오류를 만나게 됩니다. 이런 경우 MySQL 이미지가 실행되지 않으며 10초 간격으로 죽었다가 다시 실행했다가 반복 되는 것을 볼 수 있습니다.


<br />
<br />


# 해결 방법 1

<br />


`chown`을 붙입니다.

``` text
volumes:
      - chown 999:0 /private/etc/mysql:/var/lib/mysql:rw
```

<br />
<br />

# 해결 방법 2

<br />



`rw` 를 추가합니다. 

``` text
volumes:
      - /private/etc/mysql:/var/lib/mysql:rw
```

<br />
<br />


# 해결 방법 3

<br />


본 글을 쓰는 이유이기도 합니다. 위의 두 방법은 구글에 검색했을 때 나오는 방법이었지만 문제를 해결하지 못했습니다. 저는 `--user 1000`를 추가함으로 문제를 해결했습니다.

``` text
volumes:
      - /private/etc/mysql:/var/lib/mysql --user 1000
```


<br />
<br />
