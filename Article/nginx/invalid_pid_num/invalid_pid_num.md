<!-- 

nginx 에러 | invalid PID number "" in "/usr/local/Cellar/nginx/1.21.3/logs/nginx.pid"
-->


# 문제 상황

```text
$ nginx -s reload

nginx: [error] invalid PID number "" in "/usr/local/Cellar/nginx/1.21.3/logs/nginx.pid"
```

<br />

nginx를 재시작했을떄 logs 디토리에 nginx.pid파일에 PID 값이 없어서 생기는 문제입니다. nginx.conf 파일 경로를 설정해줌으로 해결할 수 있습니다.

<br />
<br />
<br />

# 해결

```text
$ nginx -c /usr/local/etc/nginx/nginx.conf
```
__STEP 1__: -c 파일경로는 파일경로에 있는 nginx 설정파일 경로로 nginx 설정파일 경로로 재설정해줍니다.

<br />

*참고. 여기서 nginx 실행파일 경로는 ./usr/local/Cellar/nginx/1.21.3/bin/nginx이며 설치 환경마다 nginx 실팽하일 경로는 다릅니다.

<br />

```text
$ nginx -s reload
```

__STEP 2__: 새로운 설정파일 경로를 적용해서 nginx가 실행할 수 있도록 nginx를 재시작합니다.

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
<br />
<br />
