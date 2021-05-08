<br />

# REST란? REST API 디자인 가이드

<br />
<br />
<img src="./cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

## REST란?

- REST는 웹의 창시자(HTTP) 중의 한 사람인 Roy Fielding(로이 필딩)이 2000년 논문에서 소개합니다. 현재의 아키텍처가 웹의 본래 설계의 우수성을 많이 사용하지 못하고 있다고 판단했기 때문에, 웹의 장점을 최대한 활용할 수 있는 네트워크 기반의 아키텍처를 소개했는데 그것이 바로 Representational safe transfer (REST)입니다.
- 웹에 존재하는 모든 자원(이미지, 동영상, DB)에 고유한 URI를 부여해 활용 하는 것입니다.
- RESTful: REST 원리를 따르는 시스템으로 REST 특징을 지키면서 API를 제공하는 것입니다.

<br />
<br />

## 용어 정리

- 리소스(Resource): 데이터의 일부입니다. (예. user)
- 콜렉션(Collection): 리소스의 집합입니다. (예. users)
- URL(Identifies the location): 리소스 혹은 콜렉션을 식별할 수 있는 경로입니다. 
    - (예./company/google/employee/12)

<br />
<br />
<br />

## 1. URL에 케밥케이스(kebab-case)를 사용하자

orders의 목록을 반환하는 API를 만든다고 해봅시다.

<br />

**Bad:** 

```text
[GET] /systemOrders 
[GET] /system_orders
```

<br />

**Good:**

```text
[GET] /system-orders
```

<br />
<br />
<br />

## 2. Path Variable에는 카멜케이스(camelCase)를 사용하자

주문내역 조회 URL을 만든다고 해봅시다. 주문내역이라는 Orders 하위에 orderId가 있을 것입니다. Path Variable에 해당하는 orderId는 카멜케이스를 사용합시다.

<br />

**Bad:** 

```text
[GET] /orders/{order_id} 
[GET] /orders/{OrderId}
```

<br />

**Good:**

```text
[GET] /orders/{orderId}
```

<br />
<br />

## 3. Collection에는 단수가 아닌 복수를 사용하자

<br />

사용자들 전체를 조회하는 URL에 User의 집합이므로 복수를 사용합니다.

<br />

**Bad:** 

```text
[GET] /user
[GET] /User
```

<br />

**Good:**

```text
[GET] /users
```

<br />
<br />
<br />


## 4. Collection으로 시작해서 Identifier로 끝나자

**Bad:** 

```text
GET /shops/{shopId}/category/{categoryId}/price
```

리소스 대신에 price라는 속성을 가리키기 때문에 좋지 않습니다. 

<br />

**Good:**

```text
GET /shops/{shopId}
GET /category/{categoryId}
```

상점 목록이라는 콜랙션으로 시작해서 하위에 속한 하나의 shopID라는 Identifier로 끝났습니다. 


<br />
<br />
<br />


## 5. 동사보다 명사를 사용하자


**Bad:** 

```text
[POST] /updateuser/{userId} 
[GET] /getusers
```

행위를 URL에 붙인 경우 좋지 못합니다. 

<br />

**Good:**

```text
[PUT] /user/{userId}
```

HTTP Method를 이용해서 CRUD(생성, 읽기, 수정, 삭제)를 수행한다는 것을 알 수 있어야합니다. 

<br />
<br />
<br />

## 6. Non-Resource URL에는 동사를 사용하자

특정 작업을하고 응답으로 아무것도 하지 않는 경우 URL에 동사를 사용할 수 있습니다. 유저에게 알림을 보내는 URL을 다음과 같이 쓸 수 있습니다.

**Good:**

```text
POST /alerts/245743/resend
```

<br />
<br />
<br />

## 7. 가급적 소문자를 쓰자

RFC 3986(URI 문법 형식)은 URI 스키마와 호스트를 제외하고는 대소문자를 구별하도록 규정하고 있습니다. 대소문자를 썪어 쓰는 것은 가능하지만 URI를 기억하기 어려우며 실수할 가능성이 높아집니다. 가급적 URI는 소문자로 작성하는 것을 권장합니다.

<br />

**Bad:** 

```text
[GET] /users/1/PROFILE
```

<br />

**Good:**

```text
[GET] /users/1/profile
```

<br />
<br />
<br />

## 8. HTTP 메서드로 표현하자

HTTP 메서드를 통해서 CRUD 중 무엇을 수행하는지 알 수 있어야합니다. 

<br />

- __GET__: 리소스 정보를 조회합니다.
- __POST__: 새로운 리소스를 생성합니다.
- __PUT__: 존재하는 리소스를 수정합니다.
- __PATCH__: 제공된 리소스를 업데이트합니다. 제공된 필드만 업데이트하고 나머지 필드는 그대로둡니다.
- __DELETE__: 존재하는 리소스를 삭제합니다.

update라는 동작이 들어가서 안됩니다. PUT 메서드를 이용하여 리소스를 수정한다는 정보를 줄 수 있습니다.

<br />

**Bad:** 

```text
[GET] /posts/update/1
[GET] /users?method=update&id=covenant
```

<br />

**Good:**

```text
[PUT] /posts/1
```

예시.

- __GET /shops/2/products__: shop 2에서 판매하는 모든 상품을 조회합니다.
- __GET /shops/2/products/31__: shop 2에서 판매하는 31번 상품을 조회합니다.
- __DELETE /shops/2/products/31__: shop 2에서 판매하는 31번 상품을 삭제합니다.
- __PUT /shops/2/products/31__: shop 2에서 판매하는 31번 상품의 정보를 수정합니다.
- __POST /shops__ : 새로운 shop을 생성합니다.

<br />
<br />
<br />

## 9. 적절한 상태값을 응답하자

잘 설계된 REST API는 URI만 잘 설게된 것이 아니라 리소스에 대한 응답을 잘 전달해야합니다. 정확한 응답의 상태코드는 많은 정보를 전달할 수 있습니다. 

응답 상태 코드는 RFC 2616에 정의되어 있습니다. 상태코드는 크게 다섯개의 그룹으로 나눕니다. (참고 RFC 2616: [https://tools.ietf.org/html/rfc2616#section-10](https://tools.ietf.org/html/rfc2616#section-10))

<br />

- Informational 1XX: 정보를 제공하는 응답
- Successful 2XX : 성공적인 응답 
    - 200 OK: 요청이 성공적으로 수행
- Redirection 3XX 
    - 메시지 클라이언트의 요청을 완료하기 위해서 추가적인 행동이 필요한 경우 
- Client Error 4XX 
    - 클라이언트가 서버에게 잘못된 요청을 하는 경우
    - 401 Unauthorized: 인증이 필요한 페이지를 요청한 경우
    - 403 Forbiden: 허용되지 않은 메소드가 있을 때
    - 406 Not Acceptable: 허용 불가능
- Server Error 5XX 
    - 서버에서 오류가 발생하여 정상적으로 요청을 처리할 수 없는 경우 
    - 500 Internal Server Error: 웹 서버가 처리할 수 없음
    - 503 Service Unavailable:  서비스 제공불가, 서버 과부하, 서버 폭주

<br />

또한 에러 내용에 대한 상세 내용을 http body에 정의해서 상세한 에러 원인을 전달하는 것이 디버깅에 좋습니다.

<br />

다음은 Kakao Open API에서 카카오 로그인 응답 예시입니다. 상태메시지를 통하여 400 Status Code를 응답한 이유를 담고있습니다. (참고. [developers.kakao](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code))

```text
HTTP/1.1 400 Bad Request
{
    "msg":"user property not found ([gender, age] for appId={APP_ID})",
    "code":-201
}
```

<br />
<br />
<br />


## 10. JSON property에는 카멜케이스(camelCase)를 사용하자

**Bad:** 

```text
{
	user_id: "1"   
	user_name: "Covenant" 
}
```
<br />

**Good:**

```text
{
	 userId: "1"
   userName: "Covenant"
}
```

<br />
<br />
<br />


## 11. URI에 파일 확장자를 포함하지 않는다.

URL에 파일확장자를 포함하지 않는 대신에 클라이언트가 허용할 수 있는 파일 형식 정보가 있는 Accept header를 사용합니다.

**Bad:** 

```text
[GET] /users/1/profile.png
```
<br />

**Good:**

```text
[GET] covenant.tistory.com/users/1/profile-img
Accept: image/jpg
```

<br />
<br />


## 12. API 버전을 위해서 서수를 사용하자

하위호한성을 위하여 API에 버전을 넣어야합니다. 새로운 기능이 들어간 API를 제공할때 이전 API와 구분하기 위해서 서수를 사용하여 URL의 상단의 경로에 넣습니다. (참고: [developers.kakao](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api))

**Good:**

```text
https://kapi.kakao.com/v2/user/me
```

<br />
<br />
<br />

## 13. list 리소스의 경우 갯수도 함께 응답하자

리스트 형식의 객체를 응답할때 응답하는 리스트의 갯수를 응답하는것이 좋습니다. 페이지네이션 등 API 이용자가 다양하게 사용할 수 있습니다. 

**Bad:** 

```text
{
  users: [ 
     ...
  ]
}
```

<br />

**Good:**

```text
{
  users: [ 
     ...
  ],
  total: 34
}
```

<br />
<br />
<br />


## 14. 리스트 응답의 경우 파라미터로 limit과 offset 정보를 받자

다수의 정보를 조회하는 경우 limit, offset의 정보를 파라미터로 받아서 응답하세요. API를 응답받기 전까지 몇개의 리스트가 내려올지 알지 못합니다. 하나의 API 호출로 1만개(640kb)를 응답한 적도 있습니다. 이용자가 많다면 서비스 응답시간이 오래 걸리거나 언제 죽더라도 이상하지 않을 것입니다. 

limit, offset 값을 이용하여 프론트에서 페이지네이션을 구현할 수 있습니다. 

<br />

**Good:**

```text
GET /shops?offset=5&limit=5
```

<br />
<br />
<br />


## 15. 쿼리 파라미터를 위한 매개변수를 가져오자

응답하는 데이터 양을 고려해야합니다. 다음의 예처럼  fields 파라미터를 이용하여 API에서 응답하는 데이터 값을  정할 수 있습니다. 

**Good:**

```text
GET /shops?fields=id,name,address,contact
```

<br />

id, name, address 그리고 contact 정보만 받습니다. 이를 통해서 응답값 크기를 줄일 수 있습니다. 하나의 API로 모바일과 웹 브라우저에서 호출할 때 일반적으로 모바일 환경은 웹 브라우저보다 화면에 보여주는 데이터 양이 적습니다. 웹 브라우저처럼 사용하지 않은 모든 데이터를 내려받는다면 불필요한 데이터를 다운로드하게 할 것입니다.


<br />
<br />
<br />


## 16. 인증토큰을  URL에 노출하지 말자

최근 JWT를 이용하여 토큰 인증 방식으로 구현이 늘어나고 있습니다. 인증토큰이 URL에 노출되도록 구현하면 안됩니다. 

**Bad:** 

```text
GET /shops/123?token=some_kind_of_authenticaiton_token
```

<br />

**Good:**

```text
Authorization: Bearer xxxxxx, Extra yyyyy
```

요청 Header에 넣는 것이 좋습니다. 또한 Token은 짧은 유효기간을 부여하여 탈취에 대비해야합니다. 


<br />
<br />
<br />


## 17. URI의 마지막 문자로 슬래시(/)를 포함하지 말자.

url의 마지막에 슬래시(Trailing slash)가 붙어있다면 이는 리소스가 디렉토리라는 의미입니다.

**Bad:** 

```text
[GET] /users/1/profile/
```

<br />

**Good:**

```text
[GET] /users/1/profile
```

<br />
<br />
<br />


## 18. 리소스 이름에는 테이블명을 사용하지마라

리소스 이름을 통해서 아키텍처적인 정보가 노출되는 것은 좋지 못합니다.

**Bad:** 

```text
product_order
```

<br />

**Good:**

```text
product-orders
```

<br />
<br />
<br />


## 19. API 문서화 도구를 사용하자

<br />

다음과 같은 API 문서화 도구가 있습니다. 

- [API Blueprint](https://apiblueprint.org/)
- [Swagger](https://swagger.io/)

<br />

문서화 도구를 통해서 정확한 API정보를 API를 이용하는 사용자, 프론트엔드 개발자들에게 제공할 수 있습니다. 


<br />
<br />
<br />

## 20. 모니터링

RESTfull HTTP서비스를 위해서 /health, /version, /metrics API 엔드포인트를 만들어야합니다.

<br />

- /health: 200OK 상태값을 반환합니다. (해당 API가 정상 연결되었지 체크할 수 있습니다.)
- /version: API버전 숫자를 응답합니다.
- /metrics: 평균 응답 시간과 같은 다양한 metrics을 응답합니다.

<br />

이외에도 /debug 혹은 /status와 같은 API 엔드포인트를 사용할 수 있습니다. 

<br />
<br />
<br />


### 참고.

[22 Best Practices to Take Your API Design Skills to the Next Level](https://betterprogramming.pub/22-best-practices-to-take-your-api-design-skills-to-the-next-level-65569b200b9) 글에서 쓰여진 Parameter는 Path Parameter(Path Variable), Query Parameter 해석의 여지가 모호하기에 Path Variable라고 수정하였습니다. 해당 글의 항목 일부를 드러내거나 통합하였으며 설명이 부족한 부분이 많아 대부분의 항목에 대한 설명을 추가하였습니다.

<br />

- [22 Best Practices to Take Your API Design Skills to the Next Level](https://betterprogramming.pub/22-best-practices-to-take-your-api-design-skills-to-the-next-level-65569b200b9)
- [REST API Tutorial REST Resource Naming Guide](https://restfulapi.net/resource-naming/)
- [RESTful API Designing guidelines — The best practices](https://wayhome25.github.io/etc/2017/11/26/restful-api-designing-guidelines/)
- [REST API 디자인 가이드](https://bcho.tistory.com/914)

<br />
<br />
<br />


