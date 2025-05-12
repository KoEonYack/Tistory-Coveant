# org.apache.coyote.http11.HeadersTooLargeException 에러 해결

<br />
<!-- <br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/90c453cf79054b0a0d096f2687d15a4cae8e1ec2/Article/WebTech/apache_error/img/cover.png?raw=true?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br /> -->
<br />
<br />

## 문제 상황

<br />
<p style="text-align: center;">
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/90c453cf79054b0a0d096f2687d15a4cae8e1ec2/Article/WebTech/apache_error/img/error.png?raw=true" align="center" width="" >
</p>
<br />


```text
org.apache.coyote.http11.HeadersTooLargeException:
	at org.apache.coyote.http11.Http11OutputBuffer.checkLengthBeforeWrite(Http11OutputBuffer.java:551)
		at org.apache.coyote.Response.action(Response.java:208)
	at org.apache.coyote.http11.Http11OutputBuffer.write(Http11OutputBuffer.java:476)
		at org.apache.coyote.Response.sendHeaders(Response.java:419)
	at org.apache.coyote.http11.Http11OutputBuffer.write(Http11OutputBuffer.java:462)
		at org.apache.catalina.connector.OutputBuffer.doFlush(OutputBuffer.java:310)
```

<br />
<br />

## 해결법

<br />

`apache-tomcat-8.5.64/conf/server.xml` 파일을 엽니다.


```xml
<Connector port="8080" protocol="HTTP/1.1"
            connectionTimeout="20000"
            maxHttpHeaderSize="90000"    <!-- 추가 -->
            redirectPort="8443" />
```

기본 8192byte로 설정되어 있는 것을 `maxHttpHeaderSize="90000"` 으로 늘려주면 됩니다. (충분히 늘려주시면 됩니다.)

<br />
<br />
<br />