# GO 언어로 JWT 인증서버 만들기 


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

<br />
<br />


# 시작하며 

<br />


본 글은 [nexmo.com blog](https://www.nexmo.com/blog/2020/03/13/using-jwt-for-authentication-in-a-golang-application-dr) 글을 번역한 글입니다. 하지만 다음과 같은 차이점이 있습니다.

1. 원글 글쓴이 코드의 버그를 수정하였습니다. 
2. 원글에서 모든 구현이 하나의 main.go에 구현되었습니다. 역활에 맞추어 router, model, controller, request, response 파일로 분리하였습니다. 
3. 설명에 불필요한 수식어를 드러내며 글쓴이의 글을 윤문 하였으며, 많은 부분 초월 번역 하였습니다. 
4. 글쓴이가 사용한 Redis GUI 프로그램인 Redily는 지금 시점에서 사용할 수 없는 프로그램입니다. P3X Redis로 스크린샷을 교체하였습니다. 

JWT 기반 인증 서버를 구현하고 싶은 분들에게 가뭄에 단비 같은 글이 되기를 바랍니다.

<br />
<br />

# JWT?

<br />


✨ 제 블로그 [Covenant. JWT란]([https://covenant.tistory.com/201](https://covenant.tistory.com/201)글에서 JWT에 대하 자세하게 써 두었습니다. 본 글에서 빠른 호흡으로 JWT를 정리해 보겠습니다. 

JWT(JSON Web Token, JWT)는 JSON 객체로서 당사자간 정보를 안전하게 전송하기 위한 컴팩트한 방법입니다. JWT는 다음과 같은 이유로 인기가 있습니다.

1. JWT는 상태 비저장입니다. 즉, 불투명 토큰과 달리 데이터베이스(지속 계층)에 저장할 필요가 없습니다.
2. JWT의 서명은 한번 절대 해독되지 않으므로 토큰이 안전하게 보호되도록 합니다.
3. JWT는 일정 기간이 지나면 무효로 설정할 수 있습니다. 이것은 토큰이 탈취되었을 때 해커가 할 수 있는 피해를 최소화하는데 도움이 됩니다. 

<br />

본 튜토리얼에서 Go언어(Gin framework)를 이용한 간단한 RESTful API로 JWT의 생성, 사용, 무효화 기능을 구현해 보겠습니다.

<br />
<br />

# JWT는 어떻게 구성되었나요? 

<br />


JWT는 세 부분으로 구성됩니다.

- 헤더: 사용된 토큰 유형 및 서명 알고리즘. 토큰 유형은 "JWT"일 수 있고, 서명 알고리즘은 HMAC 또는 SHA256 등등.. 일 수 있습니다.
- 페이로드: 클레임이 포함된 토큰의 두 번째 부분. 애플리케이션별 데이터(예: 사용자 ID, 사용자 이름), 토큰 만료 시간(exp), 발급자(iss), 제목(sub) 등이 포함됩니다.
- 서명: 인코딩된 헤더, 인코딩된 페이로드 및 사용자가 제공한 비밀이 서명을 작성하는 데 사용됩니다.

<br />

헤더, 페이로드, 서명이 토큰에 어떻게 담기는지 토큰을 살펴봅시다.

```text
Token = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRoX3V1aWQiOiIxZGQ5MDEwYy00MzI4LTRmZjMtYjllNi05NDRkODQ4ZTkzNzUiLCJhdXRob3JpemVkIjp0cnVlLCJ1c2VyX2lkIjo3fQ.Qy8l-9GUFsXQm4jqgswAYTAX9F4cngrl28WJVYNDwtM
```

위의 토큰을 복사하여 [jwt.io]([https://jwt.io/](https://jwt.io/)) 의 Encoded에 붙여넣고, HEADER에 HS512를 입력하면 “Signature Verified” 메시지를 볼 수 있습니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/jwt.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

키를 이용하여 서명을 하면 JWT를 안전하게 유지할 수 있습니다. 심지어 JWT가 디코딩된 경우에도 서명이 암호화된 상태로 유지됩니다. JWT를 만들 때 키가 노출되지 않아야 합니다. 

<br />
<br />

# 토큰 타입

<br />


JWT는 특정 기간 후에 만료(무효)로 설정될 수 있습니다. 이 애플리케이션에서 만료 기간이 다른 두 개의 토큰을 생성할 것입니다.

- __액세스 토큰(Access Token):__ 인증이 필요한 요청에 액세스 토큰을 사용합니다. 일반적으로 요청 헤더에 추가됩니다. 액세스 토큰의 수명은 15분 정도로 짧을 것을 권장한다. 액세스 토큰에 짧은 시간 간격을 부여하면 토큰이 탈취되더라도 심각한 공격을 방지할 수 있습니다. 해커는 토큰이 무효화되기 전에 자신의 작업을 수행할 수 있는 시간이 15분 이하밖에 없기 때문입니다.
- __리프래시 토큰(Refresh Token):__ 리프래시 토큰은 일반적으로 7일 정도로 설정하며 액세스 토큰보다 유효기간이 더 깁니다. 이 토큰은 새로운 액세스를 생성하고 액세스 토큰을 다시 발급받는데 사용합니다. 액세스 토큰이 만료되는 경우 리프래시 토큰을 보내서 새로운 액세스 및 리프래시 토큰을 생성합니다.


<br />
<br />

# 어디에 JWT를 저장할까? 

<br />


JWT를 HttpOnly 쿠키에 저장하는 것을 적극 권장합니다. 이를 위해 백엔드에서 생성된 쿠키를 프런트엔드로 보내는 동안, 클라이언트측 스크립트를 통해 쿠키를 표시하지 않도록 브라우저에 지시하는 HttpOnly 플래그가 쿠키를 따라 전송합니다. 이렇게 하면 사이트 간 스크립팅(XSS) 공격을 방지할 수 있습니다.

JWT는 브라우저 로컬 스토리지 또는 세션 스토리지에도 저장할 수 있습니다. JWT를 이러한 방식으로 저장하면 위에서 언급한 XSS와 같은 여러 공격에 노출될 수 있으므로, 일반적으로 이런 방법은 `HttpOnly` 사용하는 것에 비해 안전성이 떨어집니다.


<br />
<br />

# 작지만, 위대한 시작

<br />

본 글에서 간단한 메모 RESTful API와 JWT를 개발할 것입니다. 

애플리케이션을 개발하기 위해서 앞으로 작업할 코드가 담길 `jwt-todo` 디렉토리를 만드세요. 그리고 의존성 관리를 위해서 go.mod를 초기화 하겠습니다.  

```text
go mod init jwt-todo
```

<br />

이제 프로젝트의 루트 디렉토리(`/jwt-todo`)에 `main.go`파일을 만들고 아래 내용을 작성해 봅시다.

```go
package main
 
func main() {}
```

본 프로젝트에서 gin을 이용해서 라우팅과 HTTP 요청을 처리할 것입니다. Gin 프레임워크를 이용하면 효율적이고 확장가능한 API를 쉽게 개발할 수 있습니다. 

<br />

터미널에 아래 명령어를 입력하여 gin을 설치할 수 있습니다. 

```text
go get github.com/gin-gonic
```

<br />

Gin설치를 마쳤다면 `main.go`파일에 아래 내용을 추가해 보세요.

```go
package main
 
Import (
    "github.com/gin-gonic/gin"
)
 
var (
  router = gin.Default()
)
 
func main() {
  router.POST("/login", Login)
  log.Fatal(router.Run(":8080"))
}
```

`/login` URI에서  사용자의 자격 증명(credentials)을 가져와서 데이터베이스와 조회한 후 유효한 경우 로그인합니다.(역자 주. 하단에 MySQL을 활용한 사용자 조회 기능을 추가한 코드를 첨부하였습니다.) 그러나 본 프로젝트의 API에서는 메모리에 담은 샘플 사용자만 사용할 것입니다. 메모리에 담을 샘플 사용자를  `main.go`에 다음과 같이 추가하여 생성할 수 있습니다.

```go
type User struct {
    ID       uint64   `json:"id"`
    Username string   `json:"username"`
    Password string   `json:"password"`
}

var user = User{
    ID:                1,
    Username: "username",
    Password: "password",
}
```

<br />
<br />

# 로그인 요청

<br />


사용자의 인증이 완료되면 로그인하여 JWT를 생성한다. 이러한 기능은 `login()` 함수를 통해 구현하였습니다.

```go
func Login(c *gin.Context) {
  var u User
  if err := c.ShouldBindJSON(&u); err != nil {
     c.JSON(http.StatusUnprocessableEntity, "Invalid json provided")
     return
  }
  //compare the user from the request, with the one we defined:
  if user.Username != u.Username || user.Password != u.Password {
     c.JSON(http.StatusUnauthorized, "Please provide valid login details")
     return
  }
  token, err := CreateToken(user.ID)
  if err != nil {
     c.JSON(http.StatusUnprocessableEntity, err.Error())
     return
  }
  c.JSON(http.StatusOK, token)
}
```

우리는 사용자의 요청을 받은 후 `User` 구조체에 언마샬링하였습니다. 그런 다음 입력 사용자를 메모리에 정의한 사용자와 비교하였습니다. 데이터베이스를 사용했으면 데이터베이스에 사용자의 정보가 있는지 조회하면 됩니다.

로그인 함수가 길어지지 않기 위해서 JWT를 생성하는 로직은 `Create`에 구현하였습니다. JWT생성할 때  userID는 클레임에 담깁니다. 

<br />

`CreateToken` 함수는 `dgrijalva/jwt-go` 패키지를 사용할 것이며, 다운로드는 아래의 명령어로 할 수 있습니다. 

```text
go get github.com/dgrijalva/jwt-go
```

`CreateToken`함수는 아래와 같이 정의합니다. 

```go
func CreateToken(userid uint64) (string, error) {
  var err error
  //Creating Access Token
  os.Setenv("ACCESS_SECRET", "jdnfksdmfksd")
  atClaims := jwt.MapClaims{}
  atClaims["authorized"] = true
  atClaims["user_id"] = userid
  atClaims["exp"] = time.Now().Add(time.Minute * 15).Unix()
  at := jwt.NewWithClaims(jwt.SigningMethodHS256, atClaims)
  token, err := at.SignedString([]byte(os.Getenv("ACCESS_SECRET")))
  if err != nil {
     return "", err
  }
  return token, nil
}
```

토큰을 15분 동안만 유효하도록 설정 하였으며 그 후에는 유효하지 않습니다. 토큰 생성시 환경변수 ACCESS_SECRET에 담긴 값을 이용하여 JWT에 서명하였습니다. ACCESS_SECRET은 코드에 노출되면 안됩니다. .env 혹은 .yml 파일에 저장하는 것이 좋습니다. 

이 작업을 다 마쳤다면 우리의 `main.go`함수는 다음과 같이 완성할 수 있습니다.

```go
package main
 
import (
  "github.com/dgrijalva/jwt-go"
  "github.com/gin-gonic/gin"
  "log"
  "net/http"
  "os"
  "time"
)
 
var (
  router = gin.Default()
)
 
func main() {
  router.POST("/login", Login)
  log.Fatal(router.Run(":8080"))
}
type User struct {
  ID       uint64    `json:"id"`
  Username string    `json:"username"`
  Password string    `json:"password"`
  Phone    string    `json:"phone"`
}
var user = User{
  ID:            1,
  Username: "username",
  Password: "password",
  Phone:    "49123454322", 
}
func Login(c *gin.Context) {
  var u User
  if err := c.ShouldBindJSON(&u); err != nil {
     c.JSON(http.StatusUnprocessableEntity, "Invalid json provided")
     return
  }
  //compare the user from the request, with the one we defined:
  if user.Username != u.Username || user.Password != u.Password {
     c.JSON(http.StatusUnauthorized, "Please provide valid login details")
     return
  }
  token, err := CreateToken(user.ID)
  if err != nil {
     c.JSON(http.StatusUnprocessableEntity, err.Error())
     return
  }
  c.JSON(http.StatusOK, token)
}
func CreateToken(userId uint64) (string, error) {
  var err error
  //Creating Access Token
  os.Setenv("ACCESS_SECRET", "jdnfksdmfksd")
  atClaims := jwt.MapClaims{}
  atClaims["authorized"] = true
  atClaims["user_id"] = userId
  atClaims["exp"] = time.Now().Add(time.Minute * 15).Unix()
  at := jwt.NewWithClaims(jwt.SigningMethodHS256, atClaims)
  token, err := at.SignedString([]byte(os.Getenv("ACCESS_SECRET")))
  if err != nil {
     return "", err
  }
  return token, nil
}
```

지금까지 작업한 애플리케이션을 실행해 봅시다. 

```text
go run main.go
```

Postman을 이용해서 로그인해 보겠습니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/postman_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

15분간 유효한 JWT 토큰을 얻을 수 있습니다. (개발 끝?)


<br />
<br />

# 앗, 헛점이!

<br />


앞서 작성한 웹애플리케이션은 전송받은 사용자 정보를 비교한 후 로그인하여 JWT를 생성할 수 있지만 다음과 같은 문제가 있습니다. 

1. JWT는 유효 기간이 지나야지 JWT가 무효화 됩니다. 사용자가 로그인한 후 즉시 로그아웃을 할 수 있지만 사용자의 JWT는 만료 시간에 도달할 때까지 유효합니다. (로그아웃이 소용이 없네요!)
2. JWT는 토큰이 만료될 때까지 해커에 의해 탈취되어 사용될 수 있습니다.
3. 토큰이 만료되면(15분) 사용자는 다시 로그인해야 합니다.

<br />

다음 두 가지 방법으로 위의 문제를 해결할 수 있습니다. 

1. 데이터베이스에 JWT 메타데이터 저장합니다. 사용자가 로그아웃을 요청한다면 이를 기록하여 JWT를 무효화할 수 있습니다.
2. 액세스 토큰(access token)이 만료된 경우 리프레시 토큰(refresh token)을 사용하여 새 액세스 토큰을 생성하여 액세스 토큰이 만료가 되더라도 사용자가 다시 로그인을 하지 않게 만들 수  있습니다.


<br />
<br />

# Redis에 JWT 메타 데이터를 담다.

<br />

위에서 제안한 해결책 중 하나인 Redis를 이용하여 문제를 해결할 것입니다. 우리가 생성하는 JWT는 만료 시간이 있으므로 Redis는 만료 시간이 지난 데이터를 자동으로 삭제하는 기능을 가지고 있습니다. Redis는 다량의 쓰기를 처리할 수 있고 수평으로 확장할 수 있습니다.

Redis는 key-value 스토리지이기에 키가 고유해야 합니다. 고유한 key를 만들기 위해서 uuid를 키로, 사용자 ID를 값으로 저장할 것입니다.

<br />

구현을 위해서 아래 명령어를 터미널에 입력하여 두 가지 패키지를 추가로 설치해 봅시다. 

```text
go get github.com/go-redis/redis/v7
go get github.com/twinj/uuid
```

`main.go` 파일의 import 부분에 아래와 같이 작성합니다. 

```go
import (
  …
  "github.com/go-redis/redis/v7"
  "github.com/twinj/uuid"
…
)
```

> 본 글에서 Redis 설치를 다루지 않습니다. 제가 설치해 봤는데 허벌나게 간단하니 설치 뚝딱하고 돌아오세요!

다음 코드를 통해서 redis를 초기화 합니다. 

```go
var  client *redis.Client

func init() {
  //Initializing redis
  dsn := os.Getenv("REDIS_DSN")
  if len(dsn) == 0 {
     dsn = "localhost:6379"
  }
  client = redis.NewClient(&redis.Options{
     Addr: dsn, //redis port
  })
  _, err := client.Ping().Result()
  if err != nil {
     panic(err)
  }
}
```

Redis 클라이언트는 init()함수에서 초기화 합니다. 이렇게 하면 main.go 파일을 실행할 때마다 redis가 연결됩니다.

이 시점부터 토큰을 만들 때 이전 구현에서 사용자 ID를 클레임으로 사용한 것처럼 토큰 클레임 중 하나로 사용될 UUID를 생성합니다.


<br />
<br />


# 메타데이터 정의

<br />

위에서 언급한 문제를 해결하기 위해서 두 개의 JWT를 생성해야 합니다.

1. 액세스 토큰(The Access Token)
2. 리프래시 토큰(The Refresh Token)

<br />

다음과 같은 토큰 정의, 만료 기간 및 UUID를 포함하는 구조체를 정의하겠습니다.

```go
type TokenDetails struct {
  AccessToken    string
  RefreshToken   string
  AccessUuid     string
  RefreshUuid    string
  AtExpires      int64
  RtExpires      int64
}
```

만료시간, UUID는 토큰 메타데이터를 Redis에 저장할 때 사용할 것입니다.

이제 `CreateToken` 을 다음과 같이 수정해 보겠습니다. 

```go
func CreateToken(userid uint64) (*TokenDetails, error) {
  td := &TokenDetails{}
  td.AtExpires = time.Now().Add(time.Minute * 15).Unix()
  td.AccessUuid = uuid.NewV4().String()

  td.RtExpires = time.Now().Add(time.Hour * 24 * 7).Unix()
  td.RefreshUuid = uuid.NewV4().String()

  var err error
  //Creating Access Token
  os.Setenv("ACCESS_SECRET", "jdnfksdmfksd") //this should be in an env file
  atClaims := jwt.MapClaims{}
  atClaims["authorized"] = true
  atClaims["access_uuid"] = td.AccessUuid
  atClaims["user_id"] = userid
  atClaims["exp"] = td.AtExpires
  at := jwt.NewWithClaims(jwt.SigningMethodHS256, atClaims)
  td.AccessToken, err = at.SignedString([]byte(os.Getenv("ACCESS_SECRET")))
  if err != nil {
     return nil, err
  }
  //Creating Refresh Token
  os.Setenv("REFRESH_SECRET", "mcmvmkmsdnfsdmfdsjf") //this should be in an env file
  rtClaims := jwt.MapClaims{}
  rtClaims["refresh_uuid"] = td.RefreshUuid
  rtClaims["user_id"] = userid
  rtClaims["exp"] = td.RtExpires
  rt := jwt.NewWithClaims(jwt.SigningMethodHS256, rtClaims)
  td.RefreshToken, err = rt.SignedString([]byte(os.Getenv("REFRESH_SECRET")))
  if err != nil {
     return nil, err
  }
  return td, nil
}
```

액세스 토큰이 15분 후에 만료되고, 리프래시 토큰은 7일 후에 만료하도록 정의했습니다. 또한 각 토큰에 대한 클레임으로 UUID를 추가한 것을 볼 수 있습니다.

UUID는 생성될 때마다 고유하므로 사용자는 둘 이상의 토큰을 생성할 수 있다. 이는 사용자가 다른 기기에 로그인할 때 UUID를 다시 생성해서 Redis에 저장합니다. 따라서 사용자가 하나의 기기에서 로그아웃 하면 로그아웃을 요청한 기기에서만 로그아웃이 됩니다. 허벌나게 멋있죠?


<br />
<br />

# JWT 매타데이터의 저장

<br />

JWT 매타데이터를 저장하는 함수를 구현해 보겠습니다. 

```go
func CreateAuth(userid uint64, td *TokenDetails) error {
    at := time.Unix(td.AtExpires, 0) //converting Unix to UTC
    rt := time.Unix(td.RtExpires, 0)
    now := time.Now()

    errAccess := client.Set(td.AccessUuid, strconv.Itoa(int(userid)), at.Sub(now)).Err()
    if errAccess != nil {
        return errAccess
    }
    errRefresh := client.Set(td.RefreshUuid, strconv.Itoa(int(userid)), rt.Sub(now)).Err()
    if errRefresh != nil {
        return errRefresh
    }
    return nil
}
```

리프래시 토큰 또는 액세스 토큰의 만료시간이 지나면 도달하면 JWT가 Redis에서 자동으로 삭제됩니다.

P3X Redis UI를 이용해서 JWT 메타데이터가 키-값 쌍에 어떻게 저장되는지 아래를 살펴보세요!

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/p3x.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

login 기능을 테스트하기 전에 `Login()`함수 안에 `CreateAuth()` 함수를 호출할 것입니다. 

```go
func Login(c *gin.Context) {
  var u User
  if err := c.ShouldBindJSON(&u); err != nil {
     c.JSON(http.StatusUnprocessableEntity, "Invalid json provided")
     return
  }
  //compare the user from the request, with the one we defined:
  if user.Username != u.Username || user.Password != u.Password {
     c.JSON(http.StatusUnauthorized, "Please provide valid login details")
     return
  }
  ts, err := CreateToken(user.ID)
 if err != nil {
 c.JSON(http.StatusUnprocessableEntity, err.Error())
   return
}
 saveErr := CreateAuth(user.ID, ts)
  if saveErr != nil {
     c.JSON(http.StatusUnprocessableEntity, saveErr.Error())
  }
  tokens := map[string]string{
     "access_token":  ts.AccessToken,
     "refresh_token": ts.RefreshToken,
  }
  c.JSON(http.StatusOK, tokens)
}
```

Postman으로 테스트하면 다음과 같은 결과를 볼 수 있습니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/postman_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

지금까지의 구현을 통해서 access_token, refresh_token를 생성했으며, 토큰의 메타데이터를 Redis에 저장하였습니다. 

<br />
<br />

# Todo 생성 

<br />

이제 우리는 JWT를 이용하여 인증이 필요한 요청을 진행할 수 있습니다. 

먼저 `Todo` 구조체를 정의해 보겠습니다.  

```go
type Todo struct {
  UserID uint64 `json:"user_id"`
  Title  string `json:"title"`
}
```

인증된 요청을 수행할 때 인증 헤더에 전달된 토큰의 유효성을 확인해야 합니다. 유효성 검사를 통해서 토큰이 위변조 되었는지 확인할 수 있습니다. 

요청 헤더(Request header)에서 토큰을 가져와야 합니다. 

```go
func ExtractToken(r *http.Request) string {
  bearToken := r.Header.Get("Authorization")
  //normally Authorization the_token_xxx
  strArr := strings.Split(bearToken, " ")
  if len(strArr) == 2 {
     return strArr[1]
  }
  return ""
}
```

다음과 같이 토큰을 검증할 것입니다. 

```go
func VerifyToken(r *http.Request) (*jwt.Token, error) {
  tokenString := ExtractToken(r)
  token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
     //Make sure that the token method conform to "SigningMethodHMAC"
     if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
        return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
     }
     return []byte(os.Getenv("ACCESS_SECRET")), nil
  })
  if err != nil {
     return nil, err
  }
  return token, nil
}
```

`VerifyToken`안에 `ExtractToken`을 호출합니다. `ExtractToken`은 토큰을 가져온 다음 signing method를 검증합니다. 

그런 다음 `TokenValid` 함수를 사용하여 토큰이 만료가 되었는지 검사합니다. 

```go
func TokenValid(r *http.Request) error {
  token, err := VerifyToken(r)
  if err != nil {
     return err
  }
  if _, ok := token.Claims.(jwt.Claims); !ok && !token.Valid {
     return err
  }
  return nil
}
```

Redis 저장소에서 조회할 토큰 메타데이터를 추출해 보겠습니다. 토큰을 추출하기 위해서 `TokenMetadata` 함수를 다음과 같이 작성합니다. 

```go
func ExtractTokenMetadata(r *http.Request) (*AccessDetails, error) {
  token, err := VerifyToken(r)
  if err != nil {
     return nil, err
  }
  claims, ok := token.Claims.(jwt.MapClaims)
  if ok && token.Valid {
     accessUuid, ok := claims["access_uuid"].(string)
     if !ok {
        return nil, err
     }
     userId, err := strconv.ParseUint(fmt.Sprintf("%.f", claims["user_id"]), 10, 64)
     if err != nil {
        return nil, err
     }
     return &AccessDetails{
        AccessUuid: accessUuid,
        UserId:   userId,
     }, nil
  }
  return nil, err
}
```

ExtractTokenMetadata 함수는 AccessDetails(구조체)를 반환합니다. 이 구조에는 Redis에서 조회해야 하는 메타데이터(`access_uuid`, `user_id`)가 포함되어 있습니다. 이 토큰에서 메타데이터를 가져올 수 없는 이유가 있으면 오류 메시지와 함께 요청이 중지됩니다.

`AccessDetails` 구조체는 다음과 같이 정의할 수 있습니다. 

```go
type AccessDetails struct {
    AccessUuid string
    UserId     uint64
}
```

토큰에 저장된 uuid를 Redis에서 찾는 로직을 다음과 같이 구현 할 수 있습니다.

```go
func FetchAuth(authD *AccessDetails) (uint64, error) {
  userid, err := client.Get(authD.AccessUuid).Result()
  if err != nil {
     return 0, err
  }
  userID, _ := strconv.ParseUint(userid, 10, 64)
  return userID, nil
}
```

`FetchAuth()`함수는 AccessDetails를 인자로 받습니다. Redis에 저장된 값이 없거나 토큰의 유효 기간이 만료된 경우 에러를 발생합니다. 

마침내 `CreateTodo`함수를 작성할 준비가 끝났습니다.

```go
func CreateTodo(c *gin.Context) {
  var td *Todo
  if err := c.ShouldBindJSON(&td); err != nil {
     c.JSON(http.StatusUnprocessableEntity, "invalid json")
     return
  }
  tokenAuth, err := ExtractTokenMetadata(c.Request)
  if err != nil {
     c.JSON(http.StatusUnauthorized, "unauthorized")
     return
  }
 userId, err = FetchAuth(tokenAuth)
  if err != nil {
     c.JSON(http.StatusUnauthorized, "unauthorized")
     return
  }
td.UserID = userId

//you can proceed to save the Todo to a database
//but we will just return it to the caller here:
  c.JSON(http.StatusCreated, td)
}
```

`ExtractTokenMetadata`에서 JWT 메타데이터를 추출합니다. 메타데이터가 redis 저장소에 여전히 있는지 확인합니다. 모든 것이 정상이면 Todo를 데이터베이스에 저장합니다.

`main()`에 `CreateToDo`를 추가합니다. 

```go
func main() {
  router.POST("/login", Login)
  router.POST("/todo", CreateTodo)

  log.Fatal(router.Run(":8080"))
}
```

`CreateToDo`를 테스트하기 위해서 발급받은 `access_token`을 복사하여 Authorization의 Bearer Token에 입력합니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/postman_3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

입력을 완료 했다면 Body탭으로 이동합니다. 그리고 다음의 JSON 값을 요청 바디에 보냅니다. 

```text
{
	"title": "test todo"
}
```

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/postman_4.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

새로운 메모를 생성하였습니다!


<br />
<br />

# 로그아웃 구현

<br />

지금까지 JWT가 인증된 요청을 처리하기 위해서 어떻게 해야 하는지 살펴보았습니다. 눈치가 빠른 분들은 사용자가 로그아웃하면 즉시 JWT 발급을 취소하지 않는다는 것을 알았을 것입니다. 로그아웃 기능은 Redis 저장소에서 JWT 메타데이터를 삭제하는 방식으로 구현할 수 있습니다.

유튜브 프리미엄 기간이 남은 사용자가 유튜브를 탈퇴했다고 합시다. 하지만 토큰이 만료되기 전까지 유튜브 프리미엄을 이용할 수 있을 것입니다. 지금 구현할 것은 로그아웃 뿐만 아니라 회원 탈퇴에도 사용할 수 있는 구현입니다.

이제 JWT 메타데이터를 redis에서 삭제할 수 있는 함수를 정의해 보겠습니다.

```go
func DeleteAuth(givenUuid string) (int64,error) {
  deleted, err := client.Del(givenUuid).Result()
  if err != nil {
     return 0, err
  }
  return deleted, nil
}
```

위의 함수는 매개 변수로 전달된 UUID에 해당하는 Redis의 레코드를 삭제합니다. `Logout`은 다음과 같이 구현할 수 있습니다. 

```go
func Logout(c *gin.Context) {
  au, err := ExtractTokenMetadata(c.Request)
  if err != nil {
     c.JSON(http.StatusUnauthorized, "unauthorized")
     return
  }
  deleted, delErr := DeleteAuth(au.AccessUuid)
  if delErr != nil || deleted == 0 { 
     c.JSON(http.StatusUnauthorized, "unauthorized")
     return
  }
  c.JSON(http.StatusOK, "Successfully logged out")
}
```

로그아웃 기능에서는 JWT 메타데이터를 먼저 추출합니다. 성공하면 해당 메타데이터를 삭제하여 JWT를 즉시 무효로 만듭니다.

테스트 하기 전에 logout 경로를 `main.go`에 업데이트합니다.

```go
func main() {
  router.POST("/login", Login)
  router.POST("/todo", CreateTodo)
  router.POST("/logout", Logout)

  log.Fatal(router.Run(":8080"))
}
```

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/postman_5.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

Postman에서 성공적으로 로그아웃 한 것을 확인할 수 있습니다.

<br />
<br />

# 인증 라우터를 안전하게

<br />

인증에 관련된 경로는 `/login`과 `/logout` 두 가지 입니다. 지금 구현으로는 인증이 있든 없든 누구나 이 경로에 접속할 수 있습니다. 이를 개선해 보겠습니다. 

`TokenAuthMiddleware()` 함수를 통해서 인증 경로를 안전하게 만들겠습니다. 

```go
func TokenAuthMiddleware() gin.HandlerFunc {
  return func(c *gin.Context) {
     err := TokenValid(c.Request)
     if err != nil {
        c.JSON(http.StatusUnauthorized, err.Error())
        c.Abort()
        return
     }
     c.Next()
  }
}
```

앞에서 구현한 `TokenValid()`를 사용해서 토큰이 유효한지, 기간이 지났는지 검사합니다. 

이제 `main.go`를 업데이트해 봅시다. 

```go
func main() {
  router.POST("/login", Login)
  router.POST("/todo", TokenAuthMiddleware(), CreateTodo)
  router.POST("/logout", TokenAuthMiddleware(), Logout)

  log.Fatal(router.Run(":8080"))
}
```


<br />
<br />

# 리프래시 토큰

<br />

액세스 토큰이 만료되고 사용자가 인증된 요청을 해야 하는 경우 어떻게 될까요? 권한이 없는 사용자로 인식하기에 다시 로그인해야합니다. 리프레시 토큰 개념을 사용하면 이런 불편한 상황을 막을 수 있습니다. 

액세스 토큰과 함께 생성된 리프래시 토큰은 새로운 액세스 토큰과 리프래시 토큰을 만드는 데 사용할 것입니다.

`Refresh()` 함수를 만들어봅시다. 

```go
func Refresh(c *gin.Context) {
  mapToken := map[string]string{}
  if err := c.ShouldBindJSON(&mapToken); err != nil {
     c.JSON(http.StatusUnprocessableEntity, err.Error())
     return
  }
  refreshToken := mapToken["refresh_token"]

  //verify the token
  os.Setenv("REFRESH_SECRET", "mcmvmkmsdnfsdmfdsjf") //this should be in an env file
  token, err := jwt.Parse(refreshToken, func(token *jwt.Token) (interface{}, error) {
     //Make sure that the token method conform to "SigningMethodHMAC"
     if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
        return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
     }
     return []byte(os.Getenv("REFRESH_SECRET")), nil
  })
  //if there is an error, the token must have expired
  if err != nil {
     c.JSON(http.StatusUnauthorized, "Refresh token expired")
     return
  }
  //is token valid?
  if _, ok := token.Claims.(jwt.Claims); !ok && !token.Valid {
     c.JSON(http.StatusUnauthorized, err)
     return
  }
  //Since token is valid, get the uuid:
  claims, ok := token.Claims.(jwt.MapClaims) //the token claims should conform to MapClaims
  if ok && token.Valid {
     refreshUuid, ok := claims["refresh_uuid"].(string) //convert the interface to string
     if !ok {
        c.JSON(http.StatusUnprocessableEntity, err)
        return
     }
     userId, err := strconv.ParseUint(fmt.Sprintf("%.f", claims["user_id"]), 10, 64)
     if err != nil {
        c.JSON(http.StatusUnprocessableEntity, "Error occurred")
        return
     }
     //Delete the previous Refresh Token
     deleted, delErr := DeleteAuth(refreshUuid)
     if delErr != nil || deleted == 0 { 
        c.JSON(http.StatusUnauthorized, "unauthorized")
        return
     }
    //Create new pairs of refresh and access tokens
     ts, createErr := CreateToken(userId)
     if  createErr != nil {
        c.JSON(http.StatusForbidden, createErr.Error())
        return
     }
    //save the tokens metadata to redis
 saveErr := CreateAuth(userId, ts)
 if saveErr != nil {
        c.JSON(http.StatusForbidden, saveErr.Error())
       return
 }
 tokens := map[string]string{
       "access_token":  ts.AccessToken,
       "refresh_token": ts.RefreshToken,
 }
     c.JSON(http.StatusCreated, tokens)
  } else {
     c.JSON(http.StatusUnauthorized, "refresh expired")
  }
}
```

이 함수에서 많은 일들이 일어나고 있고 흐름을 살펴보겠습니다. 

–  요청 바디에 있는 refresh_token을 읽어옵니다. 
–  [1] 토큰의 서명 방법을 검증합니다.
–  [2] 토큰의 여전히 유효기간이 지나지 않았는지 검사합니다. 
– refresh_uuid와 user_id를 추출합니다. 이 값은 refresh 토큰을 생성할 때 클레임으로 사용되는 메타데이터입니다.
– redis 저장소에서 메타데이터를 검색하고 refresh_uuid를 키로 사용하여 삭제합니다.
–  향후 요청에 사용될 새로운 액세스 및 리프레시 토큰 쌍을 생성합니다.
– 액세스 및 리프레시 토큰의 메타데이터를 redis에 저장합니다.
– 생성된 토큰은 응답합니다.
– [1] 혹은 [2]에서 토큰의 유효성 검사에 실패한다면(리프래시 토큰이 유효하지 않은 경우) 사용자가 새 토큰 쌍을 만들 수 없습니다. 다시 로그인하여 새로운 토큰을 발급 받아야 합니다. 

<br />

`main()` 함수에 라우터를 추가해 줍니다. 

```go
router.POST("/token/refresh", Refresh)
```

이렇게 구현한 것을 다음과 같이 테스트 해 보곘습니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/GO/go%EB%A1%9C_jwt%EA%B8%B0%EB%B0%98_%EC%9D%B8%EC%A6%9D%EC%84%9C%EB%B2%84/img/postman_6.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />


# 안녕!

<br />

원 저작자의 저장소 [github.com/victorsteven/jwt-best-practices](https://github.com/victorsteven/jwt-best-practices) 에 게시글에서 구현한 코드를 볼 수 있습니다. 

<br />

제가 직접 돌려보면서 발생한 오류를 수정하여 [Github Repo](https://github.com/golang-crew/Boilerplate-JWT-GO) 에 올렸습니다. 원 저작자의 코드와 다른 부분은 다음과 같습니다. 

- 기존 코드 오류 해결
- 하나의 main.go를 router, model, controller, request, response로 파일 분리
- MySQL을 통한 회원 정보 조회
- viper를 통한 환경변수 불러오기

<br />

읽어주셔서 감사합니다.

<br />
<br />

