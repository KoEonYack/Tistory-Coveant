# JWT란?

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br />

# 시작하며..


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/t_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br />

사용자가 ID, PW를 입력하면 서버로 전송이 됩니다. 서버에서는 ID, PW를 DB에 조회하고 회원이면 로그인처리합니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/t_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br />

그런데 문제가 있습니다. HTTP는 Stateless, 즉 사용자가 새로운 요청을 한다면 과거에 로그인한 사실을 기억하지 못합니다. 이 문제를 해결하기 위해서 예전에는 로그인 할 때 DB에 누가 로그인했는지 기록했습니다.  

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/t_3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br />

로그인을 하면 로그인한 사용자를 위한 토큰을 발급합니다. 이 토큰을 가지고 물건 구입, 구입 내역 조회와 같이 계정 정보가 필요한 요청을 할 때 이 토큰과 함께 요청합니다. 토큰에는 요청한 사람의 정보가 담겨있기에 서버는 DB를 조회하지 않고 누가 요청하는지 알 수 있습니다.

<br />
<br />


HTTP는 다음 특징을 가지고 있습니다. 

<br />

- Connenctionless: 클라이언트가 요청을 서버에게 보내면 서버는 이에 대한 적절한 응답을 한 후에 연결을 종료합니다.
- Stateless: Connenctionless의 특징으로 서버와 클라이언트와의 연결이 종료된다면 상태 정보는 저장하지 않고 사라집니다.

<br />

따라서 위의 특징으로 인해 로그인을 하고 상품 구매하기 버튼을 클릭하면 누가 구매하기 버튼을 클릭한지 알 수 없습니다. 따라서 세션, JWT 등 인증 방법이 필요합니다.


<br />
<br />
<br />


# 서버 기반 인증

<br />

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/c_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br />

- 서버 기반 인증 방식의 핵심은 서버측에서 유저 정보를 저장하는 것입니다.
- 대표적으로 세션을 사용하는 방법이 이에 해당합니다. 유저가 로그인을 하면 서버는 해당 유저의 세션을 만들고 서버의 메모리와 데이터 베이스에 저장합니다.
- 세션 데이터가 서버의 메모리에 저장됩니다. 사용자가 증가하여 서버를 확장하면 모든 서버에게 세션의 정보를 공유해야합니다. 따라서 별도의 중앙 세션 관리 서버를 만들게 됩니다.
- 문제
    - 사용자 수가 늘어날수록 세션으로 저장하는 정보가 증가하기에 메모리를 많이 사용하게 됩니다.
    - 쿠키는 단일 도메인 및 서브 도메인에서만 작동하기 때문에 여러 도메인에서 관리하는 것은 번거롭습니다.
<br />
<br />


# JWT 구성 요소

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/c_2.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br />

JWT를 복호화 하면 헤더, 내용, 서명으로 구분할 수 있습니다.

<br />
<br />

### Header(헤더), Payload(내용), Signature(서명)

<br />

- 구조: [Base64(HEADER)].[Base64(PAYLOAD)].[Base64(SIGNATURE)]
- xxxxxx.yyyyy.zzzzz
    - JWT는 .을 구분자로 3가지 문자열로 구성됩니다.
    - 전송을 위해서 Base64로 인코딩되며 SAML과 같은 XML 기반 표준과 비교했을 때 콤팩트합니다.
- Header와 Payload는 디코딩을 하면 평문으로 해독이 가능합니다. 따라서 인증정보, 비밀번호와 같은 민감한 정보를 넣으면 안됩니다. 그러나 Signature는 복호화할 수 없습니다.
- Header(헤더)
- 헤더는 JWT 토큰을 어떻게 해석해야 하는지를 알려주는 부분입니다.
- typ, alg 두 가지 정보로 구성됩니다.
- JWT 헤더를 디코딩했을 때 보여지는 예시입니다.

```text
{ 
        "alg": "HS256",
        "typ": JWT
}
```
- alg: 알고리즘 장식을 지정, 서명 및 토큰 검증에 사용합니다.
- typ: 토큰의 타입을 지정합니다.

<br />
<br />
<br />

### Payload(내용)

<br />

- name / value 쌍으로 이루어져 있습니다.
- 페이로드에 있는 속성들을 클레임 셋(Claim Set)이라고 부릅니다.
- 페이로드는 등록된 클레임(Registered claims), 공개 클래임(Public claims), 비공개 클래임(Private claims)으로 구분됩니다.

<br />
<br />


- __Payload 종류 1: 등록된 클레임(Registered Claim Names)__
    - ss(issuer): 토큰 발급자
    - sub(subject): 토큰 제목
    - aud(audience): 토큰 대상자
    - exp(expiration): 토큰 만료 시간 - NumericDate 형식으로 작성되어야 합니다. ex) 1480849147370
    - nbf(not before): 토큰 활성 날짜 - 이 날이 지나기 전에 토큰은 활성화되지 않습니다.
    - iat(issued at): 토큰이 발급된 시간 - 토큰 발급 이후의 경과 시간을 알 수 있습니다
    - jti(JWT ID): JWT 토큰 식별자 - 중복 방지를 위해 사용하며, 일회용 토큰(Access Token) 등에 사용하면 유용


<br />
<br />


- __Payload 종류 2: 공개 클래임(Public Claim)__
    - 사용자 정의 클레임으로 공개용 정보를 위해 사용된다. 충돌 방지를 위해 URI 포맷을 이용해야 합니다.
```text
{
    "https://tandohak.co.kr/is_authenticated": true
}
```

<br />
<br />

- __Payload 종류 3: 비공개 클래임(Private Claim)__
    - 등록된 클레임도 아니며, 공개 클래임도 아닌 서버와 클라이언트 사이에 임의로 지정한 정보를 저장하기 위해 만들어진 사용자 지정 클레임입니다.
```text
{
    "username": "tandohak"
}
```
    - 비공개 클래임을 조심해서 사용하지 않으면 등록된 클래임과 공개 클래임과 다르게 충돌이 일어날 수 있기에 주의해야 합니다.

<br />
<br />

### Signature(서명)

<br />

- 헤더와 페이로드는 암호화 한 것이 아니라 단순히 JSON 문자열을 base64로 인코딩한 것입니다.
- 누구나 디코딩을 한다면 헤더와 페이로드의 내용을 볼 수 있습니다.
- 누군가 JWT를 탈취하여 수정한 후 서버로 보낼 수 있습니다. 이 경우에 대비해 다른 사람이 위변조 했는지 검증하기 위한 부분입니다.
- 서명은 헤더의 인코딩 값, 정보의 인코딩 값을 합친 후 비밀키로 해쉬를 하여 생성합니다.


<br />
<br />
<br />

# Access Token / Refresh Token

<br />

JWT를 이용한 인증을 살펴보기 전에 액세스 토큰와 리프래시 토큰에 대해서 알아보겠습니다. 

<br />
<br />

### 액세스 토큰(Access Token)

<br />

- 리소스에 직접 접근할 수 있도록 해주는 정보만을 갖고 있습니다.
- Refresh Token에 비해서 짧은 만료 기간을 갖습니다.
- 주로 세션에 담아서 관리합니다.

<br />
<br />

### 리프래시 토큰(Refresh Token)

<br />

- 새로운 Access Token을 발급받기 위한 정보를 갖습니다.
- 클라이언트가 Access Token이 없거나 만료되면 Refresh Token을 통해 Auth Server에 요청해서 새로운 Access Token을 발급 받을 수 있습니다.
- Access Token에 비해서 긴 만료 기간을 갖습니다.
- 외부에 노출되지 않도록 관리를 위해서 데이터베이스에 저장한다.

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/c_4.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

웨일 브라우저는 URL에 whale://signin-internals/ 을 입력하면 여기서 액세스, 리프래시 토큰을 확인할 수 있습니다.

<br />
<br />
<br />

# JWT를 이용한 인증 과정

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/WebTech/jwt%EB%9E%80/img/c_3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" ><br />
<br />

1. 사용자가 ID, PW를 입력하여 서버에 로그인 인증을 요청합니다.
2. 서버는 사용자로 부터 받은 ID, PW를 확인한 후 secret key를 통해서 Access Token(JWT)과 Refresh Token을 발급합니다.
3. JWT가 요구되는 API를 요청할 때(나의 주문 내역 확인 등등..) 클라이언트가 Authorization header에 Access Token을 담아서 보냅니다. 
4. 서버는 JWT Signature를 체크하여 중간에 위변조 되었는지 확인합니다. 이상이 없으면 Payload로 부터 사용자의 정보를 확인해 요청에 맞는 정보를 응답합니다. 
5. 액세스 토큰의 시간이 만료되면 클라이언트는 리프래시 토큰을 이용해서 새로운 엑세스 토큰을 발급 받습니다.

<br />
<br />


# 토큰 기반 인증의 장단점

<br />

## 장점

<br />

### 장점1. 훌륭한 확장성

<br />

- 서버 기반 인증에서 보았듯이 세션을 이용한다면 스케일 아웃(서버 확장) 할 때마다 각 서버마다 세션 정보를 저장하게 됩니다. 사용자1이 [서버 1]에서 로그인 인증을 받았습니다. 그동안 사용자가 증가하여 [서버 2]로 스케일 아웃이 일어났습니다. 그러나 [서버2]는 사용자1이 로그인 했는지를 알지 못합니다.
- 서버가 늘어나도 토큰을 인증하는 방식만 알고 있다면 사용자 인증에 문제가 없습니다.
- JWT를 사용하지 않고 세션 전용 서버를 만들어 DB에 저장하는 방법도 있습니다. 그러나 사용자의 인증이 필요한 API의 모든 요청에 대해서 서버에 조회를 해서 DB의 값을 읽어야 하기에 DB 부하를 만들 수 있다. 이런 점을 고려했을 때 JWT는 훌륭한 확장성을 가지고 있습니다.

<br />
<br />
<br />

### 장점2. 확장성(Extensibility)

<br />

- 위에서 확장성과는 다릅니다.
- 토큰에 선택적인 권한만 부여하여 발급할 수 있다.
    - 예. 아마존에 구글 계정으로 로그인 할 수 있어도 아마존이 로그인한 구글 계정의 메일을 읽을 수 없습니다.
- OAuth의 경우 페이스북, 구글, 카카오와 같은 소셜 계정을 이용하여 다른 웹 서비스에서도 로그인 할 수 있습니다.

<br />
<br />
<br />

### 장점3. 보안성

<br />

- 클라이언트가 서버로 요청할 때 쿠키를 전달하지 않기 때문에 쿠키를 사용함으로써 발생하는 취약점이 사라집니다. 그러나 토큰을 사용했을 때 발생하는 취약점에 대해서 대비해야 합니다.

<br />
<br />
<br />

### 장점4. 여러 도메인

<br />

- 서버 기반 인증 시스템의 문제인 CORS 문제를 해결합니다. 쿠키는 발행한 서버에서만 유효합니다. www.server-1.covenant.com에서 발행한 토큰은 www.server-2.covenant.com에서 사용할 수 없습니다. 그러나 JWT는 어떤 도메인에서도 토큰만 유효하다면 처리가 가능합니다.
- 서버측에서 어플리케이션 응답 부분에 아래의 헤더를 포함시키면 됩니다.
```text
Access-Control-Allow-Origin: *
```

<br />
<br />
<br />

### 장점 5. 웹 앱 간의 상이한 쿠키 세션 처리

<br />

- 스마트폰이 없던 시절 클라이언트는 웹 브라우저가 유일했습니다. 그러나 이제 스마트폰, 태블릿과 같이 다양한 모바일 기기로 접근하는 경우를 생각해야 합니다. 브라우저의 쿠키 처리 방법과 모바일 기기의 쿠키 처리 방법은 다릅니다. 따라서 JWT를 이용하는 것이 다양한 디바이스 지원에서 유리합니다.

<br />
<br />
<br />

## 단점

<br />

### 단점 1. 데이터 증가에 따른 네트워크 부하 증가

<br />

- 모든 요청에 대해서 토큰이 전송되므로 토큰에 담기는 정보가 증가할 수록 네트워크 부하가 증가합니다. 그래서 보통 약자가 많이 사용됩니다.

<br />
<br />
<br />

### 단점 2. self-contained

<br />

- 토큰 자체에 정보를 담고 있습니다. JWT가 만료시간 전에 탈취당하면 서버에서 할 수 있는 것이 없습니다.

<br />
<br />
<br />

### 단점 3. Payload 인코딩

<br />

- 페이로드 자체는 암호화 된 것이 아니라 Base64로 인코딩 된 것입니다. 중간에 Payload를 탈취하여 디코딩하면 데이터를 볼 수 있으므로 JWE로 암호화 하거나 Payload에 중요한 데이터를 넣지 말아야 합니다.

<br />
<br />
<br />

### 단점 4. Stateless
- JWT는 상태를 저장하지 않기 때문에 한번 만들어지면 제어가 불가능합니다. 토큰을 임의로 삭제하는 것이 불가능하므로 만료 시간을 꼭 넣어 주어야 합니다.

<br />
<br />
<br />


# JWT와 보안

<br />

공격자에 의해서 토큰이 하이재킹 당할 수 있습니다. JWT를 보호하기 위한 HTTP 헤더 플래그를 설정해야 합니다.

<br />

- Secure 쿠키
    - HTTPS 프로토콜을 통한 암호 요청만을 통해서 전송할 수 있습니다.
- HttpOnly
    - 본 플래그를 설정하면 자바 스크립트의 Document.cookie API를 통한 접근이 불가능합니다. 오직 서버로만 전송됩니다.
- SameSite
    - 교차 도메인 전송이 차단됩니다.
    - CSRF에 대한 보안을 제공합니다.
    - 상대적으로 등장한지 얼마 되지 않은 신규 플래그 입니다.
- Prefix
    - 브라우저에게 특정 속성이 필요하다고 이야기하는 역할입니다.
    - 예시로 Secure-, Host- 가 있습니다.

<br />
<br />
<br />

# Go JWT 구현

<br />

저의 블로그 글 [GO 언어로 JWT 인증서버 만들기
](https://covenant.tistory.com/203)에서 GO를 이용하여 JWT 구현을 볼 수 있습니다. 

<br />
<br />
<br />


### 참고

<br />

- [JWT를 구현하면서 마주치게 되는 고민들](https://blog.hwarf.com/2009/06/howto-set-sshd-idle-timeout.html)
- [JWT (토큰 기반 인증 방식)의 이해]https://kimyhcj.tistory.com/350)
- [Security On JSON Web Token](https://code-machina.github.io/2019/09/01/Security-On-JSON-Web-Token.html)
- [JWT(JSON Web Token) 란 무엇인가?](https://velog.io/@ypo09/JWTJSON-Web-Token-%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80)
- [JSON Web Token (JWT)](https://tools.ietf.org/html/rfc7519)
- [Implementing JWT based authentication in Golang](https://www.sohamkamani.com/golang/2019-01-01-jwt-authentication/)
- [액세스 토큰 재발급 cafe24](https://developers.cafe24.com/app/front/develop/oauth/retoken)

<br />
<br />
<br />
