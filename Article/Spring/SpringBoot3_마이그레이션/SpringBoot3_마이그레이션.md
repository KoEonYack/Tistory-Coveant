<!--

스프링 부트 2에서 스프링 부트 3로 업그레이드 가이드

-->

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/SpringBoot3_%EB%A7%88%EC%9D%B4%EA%B7%B8%EB%A0%88%EC%9D%B4%EC%85%98/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

# 0. 시작하며

<br />

22년 11월 스프링 부트 3가 정식 릴리즈 되었습니다. 18년 3월 1일 스프링 부트 2가 나온 이후 3년 9개월의 시간이 지난 오랜만의 메이저 업데이트 입니다. 기존의 프로젝트를 스프링 부트3로 업그레이드(마이그레이션)하면서 경험한것을 나누려고합니다.

<br />

메이저 버전 업그레이드가 일어나면 공식 문서에서 가이드를 제공합니다. (참고 [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)) 본 글은 여러 케이스를 추가여 조금 풀어서 작성하였습니다.

<br />
<br />
<br />

# 1. JDK 17 이상으로 업그레이드

<br />

스프링 부트2와 3의 가장 큰 변화는 스프링 부트3에서는 더이상 JDK 17 미만의 버전을 지원하지 않는다는 것입니다. 실제로 스프링 부트3 코드를 보면 래거시 자바 문법을 쳐내고 JDK 17 문법을 적용한 코드를 볼 수 있었습니다. 

<br />

프로젝트에서 JDK 17 이하의 버전을 사용하고 있다면 JDK 버전 업그레이드가 필요합니다.

```text
sourceCompatibility = '17'
```
__build.gradle 수정__

<br />

build.gradle에 스프링 부트의 최소 요구사항인 JDK 17로 컴파일시 사용할 자바 버전을 명시하였습니다. 

<br />

인텔리제이를 사용한다면 쉽게 프로젝트의 JDK 버전을 변경할 수 있습니다. 제 글 [인텔리제이 JDK 설치 및 JDK 버전 변경 방법](https://covenant.tistory.com/276)을 참고하여 JDK 버전을 17 이상으로 변경을 진행합니다.


<br />
<br />
<br />

# 2. 스프링 부트 2.7.X 업그레이드

<br />

스프링 부트 3로 넘어가기 전, 스프링부트 2 버전의 가장 높은 버전으로 업그레이드가 필요합니다. 바로 스프링 부트3로 업그레이드를 하려고하면 변경된 많은 내용으로 인해서 마이그레이션이 복잡해집니다. 순차적으로 버전을 넘어가는것이 좋습니다.

<br />

현재 [mvnrepository](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot)에서 확인해보면 2.7.7 버전이 스프링 부트 2의 가장 높은 버전입니다. 

<br />

```text
implementation 'org.springframework.boot:spring-boot:2.7.7'
```

gradle에 2.7.7 버전으로 변경해주고 테스트를 진행합니다.

<br />
<br />
<br />

# 3. 2.7.X 버전에서 Deprecation 제거

<br />

2.7.X 버전에서 Deprecated 기능은 변경하는 것이 좋습니다.

<br />

스프링 시큐리티를 예로 들어보겠습니다. 스프링 시큐리티 릴리즈 노트에서 밝힌것처럼 2.7.X 버전과 의존성이 있는 스프링 시큐리티 5.8 버전은 스프링 부트 3과 의존성이 있는 6.0으로 넘어가는것을 수월하기 위해서 도움을 줍니다. (참고. [Spring Security 5.8 and 6.0 are now GA](https://spring.io/blog/2022/11/21/spring-security-5-8-and-6-0-are-now-ga))

<br />

WebSecurityConfigurerAdapter는 스프링 시큐리티 5.7부터 Deprecated 되었으며, 스프링 부트 3(스프링 시큐리티 6.0)에서는 WebSecurityConfigurerAdapter가 제거되었습니다.

<br />

제 블로그 글 '[WebSecurityConfigurerAdapter Deprecated 대응법](https://covenant.tistory.com/277)' 을 참고하여 변경하면 됩니다.

<br />
<br />
<br />


# 4. 스프링 부트 3 업그레이드

<br />

이제 gradle에서 스프링 부트 3로 업그레이드를 진행합니다. 

```text
implementation 'org.springframework.boot:spring-boot:3.0.0'
```

위와같이 설정 후 gradle을 리로드를 하면 스프링 부트 3와 호환되는 버전에 맞는 의존성을 가져올 것입니다.


<br />
<br />
<br />

# 5. 컴파일 에러 해결

<br />

버전 업데이트하면서 다양한 컴파일 에러를 만날 수 있습니다. 프로젝트가 클 수록 다양하기에 결국 버전을 올리지 못하고 좌절하기도 합니다. 본 글에서 모두 다루 수는 없지만 흔히 발생할 수 있는 겨우 몇 가지 사례를 살펴보겠습니다.

<br />
<br />

## 5-1. javax 패키지를 jakarta로

<br />

스프링 부트3로 업그레이드 했을때 가장먼저 만나는 에러는 javax 패키지의 변경입니다. 이는 JavaEE에서 Jakarta EE로 전환하며 생기는 에러입니다. (참고. [SAMSUNG SDS Blog. Java EE에서 Jakarta EE로의 전환](https://www.samsungsds.com/kr/insights/java_jakarta.html)) javax로 시작하는 패키지 이름은 jakarta로 변경해야합니다. 

<br />

- javax.persistence.*   ➔ jakarta.persistence.*
- javax.validation.*    ➔ jakarta.validation.*
- javax.servlet.*       ➔ jakarta.servlet.*
- javax.annotation.*    ➔ jakarta.annotation.*
- javax.transaction.*   ➔ jakarta.transaction.*

<br />

javax.sql.*, javax.crypto.* 패키지는 jakarta로 변화가 없습니다. Java EE에서 제공하는 패키지가 아닌 JDK 에서 제공하는 패키지이기 때문입니다. 

<br />
<br />
<br />


## 5-2. Querydsl 설정 변경

<br />

javax.persistence.*가 jakarta.persistence.*로 변경되면서 Querydsl 관련 build.gradle 설정 변경이 필요합니다. 

<br />

```text
dependencies {
	// Querydsl 설정
	implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
	annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"s
    //= Querydsl 설정
}
```

<br />

해당 설정 관련해서 [Github. querydsl](https://github.com/querydsl/querydsl/issues/3233)을 보시면 조금 더 자세히 알 수 있습니다.

<br />
<br />
<br />

## 5-3. 스프링 시큐리티 변경

<br />

SecurityConfig에서 제거된 다음의 메서드를 변경해줍니다.

<br />

- authorizeRequests() ➔ authorizeHttpRequests()
- antMatchers() ➔ requestMatchers()     
- regexMatchers() ➔ RegexRequestMatchers()

<br />

참고하면 좋을 글을 링크로 남깁니다. [spring.io. Authorize HttpServletRequests with AuthorizationFilter](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html)

<br />
<br />
<br />

## 5-4. 스프링 부트 3를 지원하지 않는 라이브러리 변경

<br />

마이그레이션 과정에서 만난 대표적 스프링 부트3를 지원하지 않는 라아이브러리는 springfox swagger입니다. springfox swagger를 사용한 상태에서 컴파일 하면 다음과 같은 에러를 만납니다.

<br />

```text
java.lang.TypeNotPresentException: Type javax.servlet.http.HttpServletRequest not present
```

<br />

springfox swagger가 javax 패키지를 jakarta로 변경하지 않아서 생기는 문제입니다. springfox는 2020년 7월을 마지막으로 버전 업데이트가 이루어지지 않고 있으니 대안으로 springdoc-openapi-ui 라이브러리로 전환하면 됩니다. 

<br />

[springdoc. migrating-from-springfox](https://springdoc.org/#migrating-from-springfox) 를 참고하여 진행하면 됩니다. 


<br />
<br />
<br />

## 5-5. 기타 오류 수정

<br />

사용하는 라이브러리에 따라서 다양한 에러를 만날 수 있습니다. 이 경우 파워 구글링으로 해결해줍니다. Feign Client를 예시로 설명드리겠습니다.

<br />

```text 
'org.springframework.cloud.openfeign.FeignContext' that could not be found

Consider defining a bean of type 'org.springframework.cloud.openfeign.FeignContext' in your configuration.
```

Feign Client를 이용하는 경우 컴파일시 위와 같은 오류를 만날 수 있습니다. 

<br />

```java
@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class TestServerApplication {
```

<br />

[stackoverflow](https://stackoverflow.com/questions/74593433/consider-defining-a-bean-of-type-org-springframework-cloud-openfeign-feignconte) 글을 보면 버그로 보이고, 수정되었다고 합니다. 이렇게 최신 버전에 의한 의도치 않은 버그가 발생하기에 안정성이 중요한 프로젝트의 경우 보수적으로 버전 업그레이드를 진행합니다.

<br />
<br />
<br />

# 6. 스프링 부트 3 에서의 변경 점검

<br />

## 6-1. 스프링 부트 이미지 배너 제건

<br />

스프링 부트 이미지 배너는 제거되었습니다. banner.gif, banner.jpg, and banner.png을 사용하고 있다면 기본 banner.txt로 표시됩니다. 

<br />
<br />

## 6-2. 로그 포멧 변경

<br />

기존 스프링 부트 로그 형식이었던 __yyyy-MM-dd HH:mm:ss.SSS__ 에서 ISO-8601 표준인 __yyyy-MM-dd’T’HH:mm:ss.SSSXXX__ 로 날짜와 시간 사이에 구분 문자로 공백문자가 아닌 'T'를 사용합니다. 

<br />

로그 형식을 변경하고 싶다면 LOG_DATEFORMAT_PATTERN (참고. [docs.spring.io. LoggingSystemProperties](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/logging/LoggingSystemProperties.html)) application.yml에 logging.pattern.dateformat 속성을 사용하면 됩니다. 

<br />
<br />
<br />

# 7. 실행 및 테스트

<br />

컴파일 에러를 잡고 빌드에 성공하더라도 예상치 못한 곳에서 런타임 에러 혹은 의도와 다른 동작을 만날 수 있습니다. 현업에서는 API 쉐도잉 혹은, 카나리 배포를 하여 테스트를 진행합니다.

<br />

예를 들어서 

```java
@RestController
public class MemberController {

  @GetMapping("/member/register")
  public String registerPage {
    return "register";
  }
}
```

스프링 부트 2(스프링 5.X)에서는 /member/register, /member/register/ 처럼 URL의 마지막에 슬러시가 들어오더라도 같은 컨트롤러에서 받았습니다. 

<br />

그러나 스프링 부트 3(스프링 6.X) 에서는 /member/register, /member/register/ 경로를 구분합니다. 위의 컨트롤러에서 /member/register/로 접근하면 404 응답을 받습니다. 만약 프론트 페이지에서 위의 경로로 접근하고 있었다면 문제가 발생합니다. __컴파일 에러가 성공하더라도 이처럼 정상 동작하는지 모든 기능에 대한 점검이 필요합니다.__

<br />

```java
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
      configurer.setUseTrailingSlashMatch(true);
    }
}
```
위의 문제의 경우 위와 같이 설정한다면 URL의 마지막에 슬러시가 있는것과 없는것 구분하지 않고 처리합니다.


<br />
<br />
<br />


# 99. 참고

<br />

- [Github. Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [How To Migrate Spring Boot 2 To Spring Boot 3](https://javatechonline.com/how-to-migrate-spring-boot-2-to-spring-boot-3/)
- [Spring Security 5.8 and 6.0 are now GA](https://spring.io/blog/2022/11/21/spring-security-5-8-and-6-0-are-now-ga)

<br />
<br />
<br />
