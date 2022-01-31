
<!-- 

nginx | open() "/nginx/1.21.3/logs/nginx.pid" failed (2: No such file or directory) 해결
no_such_file_or_directory
-->

<br />
<br />
<br />

# 문제상황

<br />

```text
nginx: [error] open() "/usr/local/Cellar/nginx/1.21.3/logs/nginx.pid" failed (2: No such file or directory)
```

<br />

nginx를 실행했더니 __No such file or directory__ 에러가 나옵니다. 처음 nginx를 실행한다면 흔히 만날 수 있는 문제인데 의외로 인터넷에 명쾌한 해결법이 없어서 남깁니다.

<br />
<br />
<br />

# 해결

<br />

*STEP 1 ~ STEP 3 까지 했을때 잘 되었다면 Pre Step 설정은 하지 않아도 됩니다.

<br />

```text
$ vim /usr/local/etc/nginx/nginx.config
```

<br />

__Pre STEP 1__: nginx.config 설정으로 이동합니다.

<br />

```text
#pid   logs/nginx.pid;
```

__Pre STEP 2__: nginx.pid 경로를 변경하기 위해서 기존의 로그 경로를 제거합니다.

<br />

```text
$ pid /usr/local/Cellar/nginx/1.21.3/logs/nginx.pid;
```

__Pre STEP 3__: 절대경로로 변경해줍니다.

<br />

```text
$ mkdir /usr/local/Cellar/nginx/1.21.3/logs
```

__STEP 1__: 에러 로그에 나온대로 nginx의 log 디렉터리를 생성합니다.

<br />

```text
$ ./usr/local/Cellar/nginx/1.21.3/bin/nginx -t
```

__STEP 2__: bin 디렉터리로 이동하여 설정파일이 올바른지 테스트합니다.

<br />

```text
nginx: the configuration file /usr/local/etc/nginx/nginx.conf syntax is ok
nginx: configuration file /usr/local/etc/nginx/nginx.conf test is successful
```

__STEP 2 실행 결과__: 설정파일에 문제가 없다면 ok, successful 결과를 볼 수 있습니다.

<br />

```text
sudo brew services restart nginx
```

__STEP 3 방법 1__: brew로 nginx를 설치해서 brew로 재시작해줍니다.

<br />

```text
$ ps -afx | grep nginx
```
__STEP 3 검사__: 위의 명령어로 nginx가 정상 실행했는지 확인할 수 있습니다.

<br />

```text
(생략):00.01 nginx: master process nginx
(생략):0:00.00 nginx: worker process
```
__STEP 3 검사 결과__: master, worker 프로세스가 성공적으로 올라가있다면 성공적으로 실행한 것입니다.

<br />

```text
$ ./usr/local/Cellar/nginx/1.21.3/bin/nginx -s reload
```

__STEP 3 방법 2__: brew로 nginx 실행이 안된다면 nginx 실행파일을 직접 질행하는 방법이있습니다.

<br />
<br />
<br />
