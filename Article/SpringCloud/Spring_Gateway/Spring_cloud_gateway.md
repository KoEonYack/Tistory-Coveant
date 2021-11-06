<!--

Spring Cloud Gateway를 이용하여 게이트웨이 구성 및 유레카 서버 연동

-->

# 0. 시작하며

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/service-discovery.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

이전 글([넷플릭스 유레카를 이용한 서비스 디스커버리, 등록 구현(with 스프링부트, GO, 플라스크)](https://covenant.tistory.com/251))에서 넷플릭스 유레카를 이용하여 서비스 디스커버리를 만들었고 스프링부트, Go, 플라스크 서비스를 등록하였습니다. 

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/gateway-msa.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

본 글에서는 스프링 클라우드 게이트웨이(Spring Cloud Gateway)를 이용하여 클라이언트는 스프링 게이트웨이를 통해서 URI을 호출하면 스프링 게이트웨이가 요청을 처리할 수 있는 벡엔드 애플리케이션에게 요청을 전달하고, 응답을 전달받아 클라이언트에 응답하도록 하겠습니다.

<br />
<br />
<br />

## 목차

<br />

1. 왜 게이트웨이를 사용해야할까?
2. 게이트웨이의 클라이언트 요청 처리 시나리오
3. 스프링 클라우드 게이트웨이 구현
4. 게이트웨이 테스트

<br />
<br />
<br />

# 1. 왜 게이트웨이를 사용해야할까?

<br />

## 문제상황

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/client-direct-call.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

게이트웨이를 사용하지 않으면 다양한 문제를 만나게됩니다. 간단한 사례를 보겠습니다. 만약 클라이언트에서 마이크로서비스를 직접 호출한다고 가정해봅시다.

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/client-direct-call-problem.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

주문량이 많아져서 많아진 주문량을 처리하기 위해서 인스턴스를 4개로 늘렸습니다. 클라이언트에서 각각 주문에 대한 호스트네임(혹은 IP)를 알아야하며 호출해야합니다. 즉 부하분산(로드벨런서)가 어려워집니다. 주문량이 줄어들어 주문 인스턴스를 하나로 줄일경우 역시 클라이언트에서 주문 인스턴스가 하나로 줄었다는것을 알아야합니다. 

<br />

마이페이지와 같이 주문내역, 회원정보, 포인트, 쿠폰이 한 번에 보일경우 하나의 페이지를 보여주기 위해서 수 많은 인스턴스를 호출해야합니다. 이럴 경우 단일 페이지 로드 시간이 늘어납니다.

<br />

Microsoft Ignite 문서인 [API 게이트웨이 패턴과 클라이언트-마이크로 서비스 간 직접 통신](https://docs.microsoft.com/ko-kr/dotnet/architecture/microservices/architect-microservice-container-applications/direct-client-to-microservice-communication-versus-the-api-gateway-pattern)에서는 게이트웨이를 사용하지 않을경우 (1)결함, (2) 너무 많은 왕복, (3) 보안 문제, (4) 교차 편집 문제로 설명합니다. 

<br />
<br />
<br />

## 게이트웨이 특징

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/api-gateway.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" >
<br />
<center> 
<a href="https://www.slideshare.net/ifkakao/msa-api-gateway"> 카카오 광고 플랫폼 MSA 적용 사례 및 API Gateway와 인증 구현에 대한 소개 </a>
</center>
<br />

게이트웨이는 클라이언트 요청의 앞단에 위치합니다. 그렇기에 클라이언트 요청에 대헤서 유효한 IP 인지, 인증된 사용자인지 처리할 수 있습니다.

로드벨런서 기능으로 클라이언트의 요청에대해서 부하분산을 할 수 있습니다.

모든 요청은 우선 게이트웨이를 통하게되므로 로그 처리에 용이합니다.

<br />

- 인증 및 권한 부여
- 응답 캐싱
- 속도 제한
- 부하 분산
- IP 허용 목록에 추가

<br />
<br />
<br />

# 2. 게이트웨이의 클라이언트 요청 처리 시나리오

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/gateway-msa-example.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

로그인을 한다는 가상의 시나리오에대해서 어떻게 동작하는지 살펴보겠습니다.

<br />

- __(1):__ 각 인스턴스를 실행하여 유레카 서버에 등록합니다. 이 방법은 [넷플릭스 유레카를 이용한 서비스 디스커버리, 등록 구현](https://covenant.tistory.com/251)에서 살펴보았습니다.
- __(2):__ 클라이언트에서 로그인 요청이 API 게이트로 옵니다.
- __(3,4):__ API 게이트웨이에서 받은 로그인 요청을 어떤 인스턴스에서 처리할 수 있을지 서비스 디스커버리에서 찾습니다. (뒤에서 다시 살펴보겠지만 매번 질의하지 않고 인스턴스 정보를 캐싱합니다.)
- __(5):__ API 게이트웨이는 로그인 처리 요청을 Auth Service에 합니다. 
- __(6):__ Auth Service의 응답 결과를 클라이언트에 전달합니다.

<br />

이제 코드로 구현해보겠습니다.

<br />
<br />
<br />

## 예제 코드

<br />

본 글에 사용한 코드는 Github에서 확인할 수 있습니다.

<br />

- [1. 스프링 클라우드 게이트웨이](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-cloud-api-gateway-msa/api-gateway)
- [2. 유레카 디스커버리 서버](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-cloud-api-gateway-msa/eureka-service-discovery)
- [3. 스프링부트 서비스](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-cloud-api-gateway-msa/client-springboot)
- [4. Go 서비스](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-cloud-api-gateway-msa/client-go)
- [5. 플라스크 서비스](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-cloud-api-gateway-msa/client-flask)

<br />
<br />
<br />

# 3. 스프링 클라우드 게이트웨이 구현

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/dependency.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>
인텔리제이 dependency 선택
</center>
<br />

스프링 게이트웨이를 구현하기 위해서 다음 의존성을 선택합니다.

<br />

- Spring Cloud Discovery: Eureka Discovery Client
- Spring Cloud Routing: Gateway

<br />

스크린샷에는 롬복이 있지만 본 예제에서는 사용하지 않을 예정입니다. (* 앞서 게이트웨이에서 로깅 처리를 한다고도 하였습니다. 롬복을 추가하여 게이트웨이 필터에 로그를 추가할 수 있습니다.)

<br />
<br />

이렇게 생성한 게이트웨이 기본 프로젝트에서 __yml__ 설정만 추가하면 됩니다.

```yml
server:
  port: 9000

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defalutZone: http://localhost:8761/eureka

spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      routes:
        - id: spring-service
          uri: lb://SPRING-SERVICE
          predicates:
            - Path=/spring-service/**
        - id: flask-service
          uri: lb://FLASK-SERVICE
          predicates:
            - Path=/flask-service/**
        - id: go-service
          uri: lb://GO-SERVICE
          predicates:
            - Path=/go-service/**
```

__eureka__ 설정은 지난 글([넷플릭스 유레카를 이용한 서비스 디스커버리, 등록 구현](https://covenant.tistory.com/251))에서 보았습니다. 해당 글에 있는 설명을 다시 가져왔습니다.

<br />

- __spring.application.name:__ 유레카 서버에 등록되는 서비스 명
- __eureka.client.register-with-eureka:__ 유레카 디스커버리 서버의 등록을 위해 true로 설정합니다.
- __eureka.client.fetch-registry:__ defaultZone의 유레카 서버에서 클라이언트 정보를 가져옵니다. false의 경우 정상 드록되지 않습니다.

<br />

스프링 게이트웨이는 세 가지(Route, Predicate, Filter) 설정을 해야합니다.

<br />

- __라우트(Route):__ 응답을 보낼 목적지 URI, 필터 항목을 식별하기 위한 ID로 구성됩니다.
- __조건자(Predicate):__ 요청을 처리하기전 HTTP 요청이 정의된 조건에 부합하는지 검사합니다.
- __필터(Filter):__ 게이트웨이에서 받은 요청과 응답을 수정할 수 있습니다.

<br />

```yml
cloud:
    gateway:
        routes:
        - id: spring-service
            uri: lb://SPRING-SERVICE
            predicates:
            - Path=/spring-service/**
```

위의 yml 설정을 일부 분리하여 어떻게 동작하는지 살펴보겠습니다.

<br />

1. localhost:9000/spring-service/ 요청이 게이트웨이로 들어옴
2. 요청받은 URI의 조건을 Predicate에서 탐색. cloud.gateway.routes.id.predicate의 `/spring-service/` 확인
3. 받은 요청을 유레카 디스커버리 서버에 등록된 `SPRING-SERVICE`이름의 서버에 요청을 전송
4. `SPRING-SERVICE`이름의 서버가 복수개가 등록되었는경우 `lb` 즉 로드벨런서로 라운드 로빈 방식으로 동작

<br />
<br />
<br />

# 4. 게이트웨이 테스트

<br />

## 서비스 실행

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/register-instance.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

지난 글([넷플릭스 유레카를 이용한 서비스 디스커버리, 등록 구현](https://covenant.tistory.com/251))에서 했었던 스프링, GO, 플라스크 인스턴스를 유레카에 등록합니다. 이미 유레카 등록 설정을 마쳤기에 디스커버리 서버를 실행하고 각각 인스턴스를 실행합니다. 

<br />

마지막으로 앞서 구현한 게이트웨이를 실행하여 유레카에 등록합니다.

<br />
<br />

## 유레카에 서비스 등록 확인

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/eureka.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

성공적으로 서비스가 등록되었으면 http://localhost:8761/에 접속시 4개의 서비스가 유레카에 등록되었음을 확인할 수 있습니다.

<br />
<br />
<br />

## 게이트웨이를 통한 요청 테스트

포스트맨에서 스프링 서비스에 요청을해보겠습니다. 스프링 서비스 서버는 127.0.0.1:8030이지만 스프링서버를 직접호출하는것이 아닌 게이트웨이 주소를 호출할 것입니다. __[GET] localhost:9000/spring-service__ 를 호출해봅시다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/postman.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

게이트웨이 주소를 호출했지만 성공적으로 스프링 서비스를 호출한 것을 확인할 수 있습니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/gateway-discover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

- (1): 스프링부트, GO, 플라스크 서비스 실행시 유레카에 등록합니다.
- (2): 클라이언트에서 __http://apigateway/spring-service__ 요청을 게이트웨이가 받습니다. 
- (3): 게이트웨이는 받은 요청 정보를 어떤 서버에서 처리할 수 있는지 유라카에 질의를 합니다.
- (4): 유레카 서버에서 받은 정보를 가지고 현재 클라이언트 요청을 처리할 수 있는 Sprig Boot 서버에 요청을 보냅니다.

<br />

그러나 (3)처럼 매번 유레카 서버에 클라이언트로 부터 넘오는 요청을 처리할 수 있는 서비스를 찾기 위해서 질의하지 않습니다.

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringCloud/Spring_Gateway/img/actuator.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>
캐싱된 routes 정보 확인
</center>
<br />

유레카 서버에 서비스 ID에 해당하는 서버를 찾기 위해서(service id - service endpoint) 매번 질의하지 않고 로컬에 캐싱합니다.

<br />

게이트웨이에서 /actuator/routes에 접속하면 유레카서버에서 캐싱한 서비스 정보를 확인할 수 있습니다.

<br />
<br />
<br />
<br />


<!--
- https://wonit.tistory.com/497
- https://startupcloudplatform.github.io/cloud4dev-docs/msaspringcloud.html#api-gateway
- https://kouzie.github.io/spring/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-eureka-%EA%B8%B0%EB%B3%B8%EC%84%A4%EC%A0%95/#eureka-%EC%84%9C%EB%B2%84-%EC%84%A4%EC%A0%95
-->
