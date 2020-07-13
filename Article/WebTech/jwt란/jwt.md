# JWT란?

<br />
<br />


# 서버 기반 인증

<br />


[이미지1]

- 기존의 서버 기반 인증 방식은 서버측에서 유저 정보를 저장한다.
- 대표적으로 세션을 사용하는 방법이 이에 해당.
- 문제
    - 사용자 수가 늘어날수록 세션에 담아야할 정보가 증가하기에 메모리 과부화 문제가 발생한다.
    - 쿠키는 단일 도메인 및 서브 도메인에서만 작동하기 때문에 여러 도메인에서 관리하는 것은 번거롭다.

<br />
<br />


# JWT 구성 요소

<br />


[이미지2]

- Header(헤더), Payload(내용), Signature(서명)
    - [Base64(HEADER)].[Base64(PAYLOAD)].[Base64(SIGNATURE)]
        - xxxxxx.yyyyy.zzzzz
            - .을 구분자로 3가지 문자열로 구성
            - 전송을 위해서 Base64로 인코딩되며 SAML과 같은 XML 기반 표준과 비교했을 때 콤팩트하다.
    - Header(헤더)
        - typ, alg 두 가지 정보로 구성

        ```json 
        { 
        	 "alg": "HS256",
        	 "typ": JWT
         }
        ```

        - typ: 토큰의 타입을 지정
        - alg: 알고리즘 장식을 지정, 서명 및 토큰 검증에 사용
        - JWT 토큰을 어떻게 해석해야 하는지 명시한 부분
    - Payload(내용)
        - name / value 쌍으로 이루어져 있다.
            - 페이로드에 있는 속성들을 클레임 셋(Claim Set)이라고 부른다.
            - 등록된 클레임(Registered claims), 공개 클래임(Public claims), 비공개 클래임(Private claims) 으로 구분된다.
        - 등록된 클레임(Registered Claim Names)
            - ss(issuer): 토큰 발급자
            - sub(subject): 토큰 제목
            - aud(audience): 토큰 대상자
            - exp(expiration): 토큰 만료 시간, NumericDate 형식으로 되어 있어야 함 ex) 1480849147370
            - nbf(not before): 토큰 활성 날짜, 이 날이 지나기 전의 토큰은 활성화되지 않음
            - iat(issued at): 토큰이 발급된 시간, 토큰 발급 이후의 경과 시간을 알 수 있음
            - jti(JWT ID): JWT 토큰 식별자, 중복 방지를 위해 사용하며, 일회용 토큰(Access Token) 등에 사용하면 유용
        - 공개 클래임(Public Claim)
            - 사용자 정의 클레임으로 공개용 정보를 위해 사용된다. 충돌 방지를 위해 URI 포맷을 이용해야 한다.

            ```json
            {
                "https://tandohak.co.kr/is_authenticated": true
            }
            ```

        - 비공개 클래임(Private Claim)
            - 등록된 클레임도 아니며, 공개 클래임도 아닌 서버와 클라이언트 사이에 임의로 지정한 정보를 저장하기 위해 만들어진 사용자 지정 클레임이다.

            ```json
            {
                "username": "tandohak"
            }
            ```

            - RFC 7519 문서를 보면 조심해서 사용하지 않으면 공개 클래임, 비공개 클래임와 달리 충돌이 일어날수 있기에 주의해야한다고 한다.
    - Signature(서명)
        - 헤더와 페이로드는 암호화 한 것이 아니라 단순히 JSON 문자열을 base64로 인코딩한 것이다.
        - 누구나 디코딩하면 어떤 내용이 있는지 확인할 수 있다.
        - 다른 사람이 위변조 했는지 검증하기 위한 부분이다.
        - header의 인코딩 값, 정보의 인코딩 값을 합친 후 비밀키로 해쉬를 하여 생성

<br />
<br />


# JWT를 이용한 인증 과정

[이미지]()


- 처음 사용자를 등록할 때 Access Token과 Refresh token이 모두 발급되어야 한다.
1. 사용자가 id, pw를 입력하여 로그인을 시도한다.
2. 서버는 요청을 확인하고 secret key를 통해서 Access Token을 발급한다.
3. JWT가 요구되는 API를 요청할 때 클라이언트가 Authorization header에 Access Token을 담아서 보낸다. 
4. 서버는 JWT Signature를 체크하고 Payload로 부터 사용자의 정보를 확인해 데이터를 리턴한다.


<br />
<br />


# Access Token / Refresh Token

<br />


- 사용처
    - API 요청을 허가하는데 AceessToken을 사용하며 만료가 된다면 Refresh Token을 사용한다.
- Access Token
    - 리소스에 직접 접근할 수 있도록 해주는 정보만을 갖고 있다.
        - HTTP 요청 대상을 "리소스"라고 부르는데, 그에 대한 본질을 이 이상으로 정의할 수 없습니다; 그것은 문서, 사진 또는 다른 어떤 것이든 될 수 있습니다.
        - [https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/Identifying_resources_on_the_Web](https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/Identifying_resources_on_the_Web)
    - 짧은 수명을 가지며, 만료 기간을 갖는다.
    - 주로 세션에 담아서 관리한다.
- Refresh Token
    - 새로운 Access Token을 발급받기 위한 정보를 갖는다.
    - 클라이언트가 Access Token이 없거나 만료되면 Refresh Token을 통해 Auth Server에 요청해서 발급 받을 수 있다.
    - Refresh Token은 만료기간이 있지만 Access Token에 비해서 길다.
    - 외부에 노출되지 않도록 관리를 위해서 데이터베이스에 저장한다.


<br />
<br />


# 토큰 기반 인증의 장단점

<br />


## 장점

<br />

[이미지]
[이미지]

- 확장성이 뛰어나다.
    - 세션을 이용한다면 스케일 아웃(서버 확장) 할 때마다 각 서버마다 세션 정보를 저장하게 된다. 로그인 시 서버 1에서 페이지를 이동하여 서버 2로 가면 서버는 인증이 안된다.
    - 서버가 늘어나도 토큰을 인증하는 방식만 알고 있다면 영향이 없다.
    - 세션을 각 서버에 저장하지 않고 세션 전용 서버를 만들어 DB에 저장해도 된다. 그러나 모든 요청시 해당 서버에 조회를 해서 DB의 값을 읽어야 하기에 DB 부하를 만들 수 있다.
- 보안성
    - 클라이언트가 서버로 요청할 때 쿠키를 전달하지 않기 때문에 쿠키를 사용함으로써 발생하는 취약점이 사라진다. 그러나 토큰을 사용했을 때 발생하는 취약점에 대해서 대비해야 한다.
- 확장성(Extensibility)
    - 토큰에 선택적인 권한만 부여하여 발급할 수 있다.
        - 예. 쿠팡에 연결된 네이버 계정은 네이버 메일 읽기 기능이 없다.
    - OAuth의 경우 페이스북, 구글, 카카오와 같은 소셜 계정을 이용하여 다른 웹 서비스에서도 로그인 할 수 있다.
- 여러 도메인
    - 서버 기반 인증 시스템의 문제인 CORS 문제를 해결한다. 어떤 도메인에서도 토큰만 유효하다면 처리가 가능하다.
    - 서버측에서 어플리케이션 응답 부분에 아래의 헤더를 포함시키면 된다.

    ```json
    Access-Control-Allow-Origin: *
    ```

- 웹 앱 간의 상이한 쿠키 세션 처리

[이미지]()

- 기존의 클라이언트는 웹 브라우저가 유일했다. 그러나 이제 모바일로 접근하는 경우도 생각해야한다. 웹 /앱의 쿠키 처리 방법이 다르다.
- JWT는 웹표준 RFC7591에 등록되어 있다라고 블로그들이 이야기를 한다. 그러나 정확하게는 인터넷 표준이 아닌 성숙도가 떨어지거나 안정적이고 검토된 규격을 선정한 PROPOSED STANDARD라고  RFC7591 문서의 상단에 쓰여져 있다.
    - RFC는 IETF가 관리하고 있는 규약문서이며 많은 표준이 RFC 형태로 존재하지만 모든 RFC가 표준인 것은 아니다.
    - [RFC 7519 JSON Web Token (JWT)][https://tools.ietf.org/html/rfc7519](https://tools.ietf.org/html/rfc7519)
    - [RFC Not All RFCs are Standards] [https://tools.ietf.org/html/rfc1796](https://tools.ietf.org/html/rfc1796)


<br />
<br />


## 단점

<br />


- 데이터 증가에 따른 네트워크 부하 증가
    - 모든 요청에 대해서 토큰이 전송되므로 토큰에 담기는 정보가 증가할 수록 네트워크 부하가 증가한다.
- self-contained
    - 토큰 자체에 정보를 담고 있다.
- Payload 인코딩
    - 페이로드 자체는 암호화 된 것이 아니라 Base64로 인코딩 된 것이다. 중간에 Payload를 탈취하여 디코딩하면 데이터를 볼 수 있으므로 JWE로 암호화 하거나 Payload에 중요한 데이터를 넣지 말아야 한다.
- Stateless
    - JWT는 상태를 저장하지 않기 때문에 한번 만들어지면 제어가 불가능하다. 토큰을 임의로 삭제하는 것이 불가능하므로 만료 시간을 꼭 넣어 주어야 한다.


<br />
<br />


# JWT와 보안

<br />

- 공격자에 의해서 토큰이 하이재킹 당할 수 있다. JWT를 보호하기 위한 HTTP 헤더 플래그를 설정해야한다.
    - Secure 쿠키
        - HTTPS 프로토콜을 통한 암호 요청만을 통해서 전송할 수 있다.
    - HttpOnly
        - 자바 스크립트의 Document.cookie API를 통한 접근이 불가능하다. 오직 서버로만 전송된다.
    - SameSite
        - 교차 도메인 전송이 차단된다.
        - CSRF에 대한 보안을 제공한다.
        - 상대적으로 신규 플래그이며 주요 브라우저에서 지원되는 사양이다.
    - Prefix
        - 브라우저에게 특정 속성이 필요하다고 이야기하는 역할
        - 예시로 Secure-, Host- 가 있다.


<br />
<br />


# JWT 구현

<br />


- Ref. Implementing JWT based authentication in Golang
    - [https://www.sohamkamani.com/golang/2019-01-01-jwt-authentication/](https://www.sohamkamani.com/golang/2019-01-01-jwt-authentication/)
- Code
```

```


<br />
<br />


### Reference

<br />


- [Tech Interview JWT (JSON Web Token)| https://github.com/gyoogle/tech-interview-for-developer/blob/master/Web/JWT(JSON Web Token).md](https://github.com/gyoogle/tech-interview-for-developer/blob/master/Web/JWT(JSON%20Web%20Token).md)
- [JWT를 구현하면서 마주치게 되는 고민들| https://blog.hwarf.com/2009/06/howto-set-sshd-idle-timeout.html](https://blog.hwarf.com/2009/06/howto-set-sshd-idle-timeout.html)
- [JWT (토큰 기반 인증 방식)의 이해| https://kimyhcj.tistory.com/350](https://kimyhcj.tistory.com/350)
- [https://code-machina.github.io/2019/09/01/Security-On-JSON-Web-Token.html](https://code-machina.github.io/2019/09/01/Security-On-JSON-Web-Token.html)
- [JWT(JSON Web Token)란 무엇인가?| https://velog.io/@ypo09/JWTJSON-Web-Token-란-무엇인가](https://velog.io/@ypo09/JWTJSON-Web-Token-%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80)
- [hJSON Web Token (JWT)| ttps://tools.ietf.org/html/rfc7519](https://tools.ietf.org/html/rfc7519)
- [Implementing JWT based authentication in Golang| https://www.sohamkamani.com/golang/2019-01-01-jwt-authentication/](https://www.sohamkamani.com/golang/2019-01-01-jwt-authentication/)