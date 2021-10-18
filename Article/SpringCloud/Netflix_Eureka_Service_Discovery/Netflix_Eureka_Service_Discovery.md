<!-- 

넷플릭스 유레카를 이용한 서비스 디스커버리, 등록 구현(with 스프링부트, GO, 플라스크)

-->

# 0. 시작하며

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Netflix_Eureka_Service_Discovery/img/architecture.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

MSA구조에서 인스턴스 상태는 동적으로 변합니다. 이러한 변경 사항을 서비스 관리자가 실시간으로 변경하는 것은 어렵습니다. 

<br />

본 글에서 다룰 넷플릭스 유레카 서비스 디스커버리 서버(Spring Cloud Netflix Service Discovery Server)를 이용하면 더욱 수월한 MSA 세상을 만날 수 있습니다.

<br />

새로운 인스턴스는 시작할 때 유레카 서버에 IP, 호스트 주소, 포트 정보 등을 전달합니다. 유레카 서버는 등록된 인스턴스들을 주기적으로 상태를 체크하면서 해당 인스턴스를 관리합니다.

<br />
<br />
<br />

## 목차

<br />

본 글에서는 넷플릭스 유레카를 이용하여 스프링부트 디스커버리 서버를 만들어보고 스프링부트, Go, 플라스크를 이용하여 유레카 서버 클라이언트르 만들어 디스커버리 서버에 등록해보겠습니다.

<br />

1. 넷플릭스 유레카(Netflix Eureka) 디스커버리 서버 생성
2. 스프링 부트 유레카 서버 클라이언트 생성
3. Go 유레카 서버 클라이언트 생성
4. 파이썬(flask) 유레카 서버 클라이언트 생성 

<br />
<br />
<br />

## 예제 코드

본 글의 실습 코드는 Github에서 확인할 수 있습니다.

<br />

- [유레카 디스커버리 서버](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/eureka-service/server-eureka)
- [스프링부트 클라이언트](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/eureka-service/client-springboot)
- [Go 클라이언트](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/eureka-service/client-go)
- [플라스크 클라이언트](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/eureka-service/client-flask)


<br />
<br />
<br />

# 1. 넷플릭스 유레카(Netflix Eureka) 서비스 디스커버리 생성

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Netflix_Eureka_Service_Discovery/img/eureka_intelij.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center> 인텔리제이 의존성 선택
</center>
<br />


이 글을 작성하는 시점의 최신 버전인 스프링부트 2.5.5로 설정하였습니다. 의존성은  `Eureka Server`만 추가하면 됩니다.

<br />
<br />

## 어노테이션 추가

```java
@SpringBootApplication
@EnableEurekaServer //추가
public class EurekaserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaserverApplication.class, args);
    }
}
```

@SpringBootApplication이 있는 `main`에 `@EnableEurekaServer`를 추가합니다.

<br />
<br />

## yml 설정

```yml
server:
  port: 8761

spring:
  application:
    name: discoverservice

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

- __eureka.client.register-with-eureka:__ 본 서버는 유레카 디스커버리 서버이므로 자신을 클라언트로 등록하지 않는 설정입니다.
- __eureka.client.fetch-registry:__ 유레카 디스크커버리 서버에서 리스트 정보를 로컬에 caching 할지 여부입니다. 본 서바가 유레카 디스커버리 서버이므로 비활성화합니다.


<br />
<br />

## 실행

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Netflix_Eureka_Service_Discovery/img/init-ui.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

애플리케이션을 실행하고 브라우저에서 `http://localhost:8761/`에 접속하면 유래카 대시보드에 접속할 수 있습니다.

<br />
<br />
<br />

# 2. 스프링 부트 클라이언트 생성

<br />

## 어노테이션 추가

```java
@SpringBootApplication
@EnableDiscoveryClient // 추가
public class SpringbootclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootclientApplication.class, args);
    }

}
```

@SpringBootApplication이 있는 `main`에 `@EnableDiscoveryClient`를 추가합니다.


<br />
<br />

## yml 설정

```yml
server:
  port: 9002

spring:
  application:
    name: user-service

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
```
- __spring.application.name:__ 유레카 서버에 등록되는 서비스 명
- __eureka.client.register-with-eureka:__ 유레카 디스커버리 서버의 등록을 위해 true로 설정합니다.
- __eureka.client.fetch-registry:__ defaultZone의 유레카 서버에서 클라이언트 정보를 가져옵니다. false의 경우 정상 드록되지 않습니다.

<br />
<br />

## Service Discovery 등록 확인

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Netflix_Eureka_Service_Discovery/img/eureka_boot_register_ui.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

앞서 작업한 스프링부트 실행하고 `http://localhost:8761/`의 __Instances currently registered with Eureka__ 에서 등록된 것을 확인할 수 있습니다.

<br />
<br />
<br />

# 3. Go 유레카 서버 클라이언트 생성

<br />

## Go 유레카 클라이언트 설치

go 라이브러리 검색 사이트 중 하나인 [libs.garden](https://libs.garden/go/search?q=eureka)에서 eureka로 라이브러리 검색하면 활발하게 유지보수가 되는 eureka client는 없습니다. 그나마 최신(마지막 커밋이 15개월 전)인 [xuanbo/eureka-client](https://github.com/xuanbo/eureka-client)를 이용해보겠습니다.

<br />

```text
$ go mod init
```
go는 사전에 설치되어있어야하며 위의 명령어를 입력하여 go 프로젝트 초기 설정을 합니다.

<br />

```text
$ go get -u github.com/xuanbo/eureka-client
```

위의 명령어를 이용하여 `xuanbo/eureka-client` 라이브러리를 다운로드받습니다. `go.sum` 파일이 생성되었다면 성공적으로 다운로드 받은 것입니다.

<br />
<br />

## Go 코드

```go
package main

import (
	"encoding/json"
	"fmt"
	"net/http"

	eureka "github.com/xuanbo/eureka-client"
)

func main() {
	// eureka 클라이언트 설정 정보
	client := eureka.NewClient(&eureka.Config{
		DefaultZone:           "http://127.0.0.1:8761/eureka/",
		App:                   "go-service",
		Port:                  10000,
		RenewalIntervalInSecs: 10,
		DurationInSecs:        30,
		Metadata: map[string]interface{}{
			"VERSION":              "0.1.0",
			"NODE_GROUP_ID":        0,
			"PRODUCT_CODE":         "DEFAULT",
			"PRODUCT_VERSION_CODE": "DEFAULT",
			"PRODUCT_ENV_CODE":     "DEFAULT",
			"SERVICE_VERSION_CODE": "DEFAULT",
		},
	})

	client.Start()

    http.HandleFunc("/services", 
    func(writer http.ResponseWriter, request *http.Request) {
		apps := client.Applications

		b, _ := json.Marshal(apps)
		_, _ = writer.Write(b)
	})

	// HTTP 서버 시작
	if err := http.ListenAndServe(":10000", nil); err != nil {
		fmt.Println(err)
	}
}
```

유레카에 등록하기위한 GO 서비스 서버의 코드는 위와같습니다.
- __DefaultZone:__ 현재 서비스를 등록할 유레카 서버 URL 정보
- __App:__  유레카 서버 등록시 앱 이름(현재 GO 서비스)
- __Port:__  GO 서비스 개방 포트

<br />
<br />

## Service Discovery 등록 확인

```text
$ go run go-client.go
```

<br />
<!-- <div align="center"> -->
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Netflix_Eureka_Service_Discovery/img/go_log.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<!-- </div> -->
<br />
<center>
Go서비스를 유레카 서버에 등록 성공시 로그
</center>
<br />
<br />

__register application instance successful__ 로그가 나오면 등록에 성공하였습니다. 주기적으로 heartbeat, refresh로그가 찍힙니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Netflix_Eureka_Service_Discovery/img/go_register_ui.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

`http://localhost:8761/`의 __Instances currently registered with Eureka__ 에서 Go 서비스가 등록된 것을 확인할 수 있습니다.

<br />
<br />
<br />

# 4. 파이썬(flask) 유레카 서버 클라이언트 생성

<br />

## 플라스크, 유레카 클라이언트 설치

```text
$ pip3 install flask eureka_client
```
pip3 명령어를 통해서 flask와 eureka_client를 설치합니다.

<br />
<br />

## Flask 코드

```python
from flask import Flask, request
import pandas as pd
import py_eureka_client.eureka_client as eureka_client

rest_port = 8050
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="flask-service",
                   instance_port=rest_port)

app = Flask(__name__)

@app.route("/intro", methods=['GET'])
def hello():
    return "Flask Server"

if __name__ == "__main__":
    app.run(host='0.0.0.0', port = rest_port)
```

유레카에 등록하기위한 GO 서비스 서버의 코드는 위와같습니다.
- __eureka_server:__ 현재 서비스를 등록할 유레카 서버 URL 정보
- __app_name:__  유레카 서버 등록시 앱 이름(현재 플라스크 서비스)
- __rest_port:__  플라스크 서비스 개방 포트

<br />
<br />

## Service Discovery 등록 확인

```text
$ python3 flask_app.py
```

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Netflix_Eureka_Service_Discovery/img/flask_register_ui.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>
<a href="http://localhost:8761/">http://localhost:8761/</a> 에서 서비스 등록 확인
</center>
<br />

flask-service 이름으로 8050 포트 서비스가 등록되었음을 확인할 수 있습니다.

<br />
<br />
<br />

