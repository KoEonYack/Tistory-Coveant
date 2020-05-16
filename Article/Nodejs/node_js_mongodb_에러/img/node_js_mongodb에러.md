# nodejs Could not connect to any servers in your MongoDB Atlas cluster. 에러 해결법


<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Nodejs/node_js_mongodb_%EC%97%90%EB%9F%AC/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

# 오류 메세지

<br />

Nodejs로 mongo db를 연결하다가 처음 만나는 에러를 보았습니다.

<br />

``` text
 message:
   'Could not connect to any servers in your MongoDB Atlas cluster. Make sure your current IP address is on your Atlas cluster\'s IP whitelist: https://docs.atlas.mongodb.com/security-whitelist/.',
```

<br />
<br />


# 해결 방법

<br />

오류메시지대로 white list 작업을 해야합니다.

<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Nodejs/node_js_mongodb_%EC%97%90%EB%9F%AC/img/1.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

`ADD IP ADDRESS` 를 클릭합니다. 

<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Nodejs/node_js_mongodb_%EC%97%90%EB%9F%AC/img/2.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

- `ADD CURRENT IP ADDRESS` 클릭, Whitelist Entry에 입력되는 IP 주소를 확인하고 `Confirm`을 누릅니다. 
- Pending에서 기다리면 Active로 바뀝니다.
- 같은 오류가 반복된다면 실행중인 nodejs를 다시 실행하면 됩니다. 
- 경우에 따라서 VPN을 사용한다던가, 방화벽 정책으로 인해서 해결이 되지 않을 수 있습니다.

<br />
<br />

