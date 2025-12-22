<!-- 
스프링부트4 신규기능! api 버저닝
-->


<br />
<img src="./img/cover.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />


## 시작하며

<br />

API는 시간이 지나며 진화하고, 그 과정에서 하위 호환성을 끝까지 유지하는 것은 이상적이지만 현실적으로 어렵습니다. 앱의 경우 업데이트를 하지 않은 구버전 앱을 때문에 쉽게 API를 변경하지 못하고 새로운 버전의 API를 만들때가 있습니다.

이를 위해서 API 버저닝을 도입합니다. 흔한 방식들을 비교하면 아래와 같습니다.

- URI(Path) 버저닝: /v1/users — 직관적이지만 URL이 지저분해지고 버전이 리소스 URI에 고정됨
- Header 버저닝: X-API-Version: 1.1 — URL은 깨끗하지만 클라이언트/테스트에서 헤더 세팅이 필요
- Media Type / Param 버저닝: Accept: application/json;v=1.1 - 컨텐츠 협상과 잘 맞지만 복잡해지기 쉬움

<br />

프레임워크 차원에서 API 버저닝을 지원하지 않았는데 스프링부트4 에서는 API Versioning을 지원합니다. 

<br />
<br />
<br />

## 스프링부트4 예시 코드

<br />

__application.yml__ 에 다음같이 설정을 해주면 됩니다.

<br />

```yml
spring:
  mvc:
    apiversion:
      use:
        header: X-API-Version # 버전을 어디서 읽을지 지정(현재 헤더로 설정)
      required: false # 버전이 없으면 에러 대신 default에 설정한 버전으로 처리
      default: 1.0    # 버전 미지정 요청의 기본 버전
      detect-supported: true # 컨트롤러에 선언된 버전을 스캔해 지원 버전 후보를 추정
```
	
<br /> 
<br /> 
<br />

위와같이 application.yml을 설정했다면 같은 URL로 다른 버전으로 라우팅이 가능합니다.

<br />

[GET] /orders/{id} API에서 다음과 같이 응답값 변경이 필요하여 API 버저닝 되었다고 가정해봅시다

<br />

- v1.0: customerFirstName, customerLastName을 응답합니다.
- v1.1+: customerName(full name)로 구조 변경

(* 물론 실무였다면 1.0 버전 API에 full name필드를 추가로 만들었을 것입니다^^..)

<br />

```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    // v1.0 스펙
    @GetMapping(path = "/{id}", version = "1.0")
    public OrderV1Response getOrderV1(@PathVariable long id) {
        return new OrderV1Response(id, "길동", "홍");
    }

    // v1.1 이상 버전 스펙
    @GetMapping(path = "/{id}", version = "1.1+")
    public OrderV2Response getOrderV11Plus(@PathVariable long id) {
        return new OrderV2Response(id, "홍길동");
    }

    public record OrderV1Response(long id, String customerFirstName, String customerLastName) {}
    public record OrderV2Response(long id, String customerName) {}
}
```

<br />
<br />


curl을 사용해서 버저닝이 잘 적용되었는지 테스트해봅시다.

```sh
curl -i \
  -H "X-API-Version: 1.0" \
  http://localhost:8080/orders/1

```

X-API-Version으 1.0으로 getOrderV1()를 호출합니다.

<br />
<br />

```sh
curl -i \
  -H "X-API-Version: 1.1" \
  http://localhost:8080/orders/1
```

X-API-Version으 1.1로 변경하면 getOrderV11Plus()을 호출합니다.

<br />
<br />

X-API-Version으로 1.2를 줄 수 있습니다. 이는 1.1+에 포함되는 값입니다. getOrderV11Plus()를 실행합니다.

```sh
curl -i \
  -H "X-API-Version: 1.2" \
  http://localhost:8080/orders/1
```

<br />
<br />

X-API-Version 버전 헤더 없이 호출할 수 있습니다.

```sh
curl -i http://localhost:8080/orders/1
```

<br />

```yml
spring:
  mvc:
    apiversion:
      // ..
      default: 1.0
      required: false
```

앞서 application.yml에 required: false, default 1.0으로 설정하였습니다. 그렇기에  X-API-Version으 1.0으로 getOrderV1()를 호출한 것과 동일한 결과가 나옵니다.

<br />

(* -i 옵션은 HTTP 상태코드/응답 헤더까지 같이 보여줘서, 라우팅/버전 적용을 확인할 때 유용합니다.)

<br />
<br />
<br />

## 클라이언트도 “버전”을 1급으로: ApiVersionInserter + .apiVersion(...)

<br />

서버만 버저닝해도 끝이 아니라, 클라이언트(다른 서비스 호출, SDK, 테스트)에서도 버전 전송을 표준화해야 운영이 편해집니다. 스프링은 이를 위해 ApiVersionInserter와 각 클라이언트의 .apiVersion(...)를 제공합니다.  ￼

<br />

__RestClient__ 는 아래와 같이 작성할 수 있습닌다.
```java
RestClient client = RestClient.builder()
        .baseUrl("http://localhost:8080")
        .apiVersionInserter(ApiVersionInserter.useHeader("X-API-Version"))
        .build();

OrderV2Response res = client.get()
        .uri("/orders/{id}", 1)
        .apiVersion("1.1")
        .retrieve()
        .body(OrderV2Response.class);
```


<br />
<br />
<br />

### HTTP Interface Client에서 version 속성 지원

<br />

인터페이스 레벨에서도 버전을 선언적으로 고정할 수 있습니다.

<br />

```java
@HttpExchange("/orders")
public interface OrderClient {

    @GetExchange(url="/{id}", version="1.1")
    OrderV2Response getOrder(@PathVariable long id);
}
```


<br />
<br />
<br />



