<!--

Spring Boot 3.0과 jakarta 패키지 전환

-->


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/jakarta/img/cover.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

## 시작하며

<br />

Spring Boot 3.0으로 프로젝트를 올리거나 새 프로젝트를 만들다 보면 가장 먼저 눈에 들어오는 변화가 있습니다. 익숙하게 사용하던 `javax.persistence.Entity`, `javax.servlet.http.HttpServletRequest` 같은 패키지가 더 이상 자연스럽게 잡히지 않고, 대신 `jakarta.persistence.Entity`, `jakarta.servlet.http.HttpServletRequest`를 사용해야 합니다.

<br />

처음 보면 단순히 패키지 이름만 바뀐 것처럼 보입니다. 실제 API 사용법도 크게 다르지 않아서 IDE의 자동 변환만 믿고 넘어가기 쉽습니다.

<br />

그러나 `javax`에서 `jakarta`로 바뀐 것은 단순한 리팩터링이 아닙니다. Java 엔터프라이즈 생태계가 Oracle 중심에서 Eclipse Foundation 중심으로 옮겨가는 과정에서 생긴, 꽤 긴 역사와 상표권 문제가 담긴 변화입니다.

<br />

이번 글에서는 왜 Spring Boot 3.0부터 `jakarta`를 사용해야 하는지, 그리고 실무에서 마이그레이션할 때 어떤 부분을 조심해야 하는지 정리해봅니다.

<br />
<br />
<br />

# Spring Boot 3.0에서 바뀐 것

<br />

Spring Boot 2.x까지는 Java EE 또는 Jakarta EE 8 기반의 `javax.*` 패키지를 사용했습니다. 그러나 Spring Boot 3.0부터는 Jakarta EE 9 이상을 지원하면서 `jakarta.*` 패키지를 사용합니다.

<br />

대표적으로 다음과 같이 import가 바뀝니다.

<br />

```java
// Spring Boot 2.x
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

// Spring Boot 3.x
import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
```

<br />

정리하면 다음과 같습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">Spring Boot 버전</th>
      <th style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">기반</th>
      <th style="padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">네임스페이스</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">Spring Boot 2.x</td>
      <td style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">Java EE / Jakarta EE 8</td>
      <td style="padding:12px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 7px; border-radius:6px; font-size:14px;">javax.*</code></td>
    </tr>
    <tr>
      <td style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">Spring Boot 3.x</td>
      <td style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">Jakarta EE 9+</td>
      <td style="padding:12px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 7px; border-radius:6px; font-size:14px;">jakarta.*</code></td>
    </tr>
  </tbody>
</table>

<br />

Spring Boot 3.0은 Java 17 이상을 요구하며, Jakarta EE 기반 의존성도 함께 올라갑니다. 그래서 단순히 Spring Boot 버전만 바꾸는 작업이 아니라 JDK, Jakarta EE API, 서드파티 라이브러리 호환성을 함께 확인해야 합니다.

<br />
<br />
<br />

# Java EE의 시작

<br />

이야기는 Java EE에서 시작합니다. 1999년 Sun Microsystems는 엔터프라이즈 애플리케이션 개발을 위한 표준 플랫폼으로 J2EE(Java 2 Platform, Enterprise Edition)를 발표했습니다.

<br />

Servlet, JSP, EJB, JPA처럼 서버 개발에서 익숙한 여러 API가 이 흐름 안에서 만들어졌고, 이 API들은 `javax.*` 네임스페이스 아래에 자리 잡았습니다.

<br />

`javax`는 Java Extension의 의미를 가진 이름입니다. Java SE의 기본 API는 아니지만, Java 생태계에서 표준 확장 API로 제공되는 영역이라고 볼 수 있습니다.

<br />

이후 J2EE는 Java EE로 이름이 바뀌었고, Java EE 5, 6, 7, 8을 거치면서 오랫동안 엔터프라이즈 Java의 표준 역할을 했습니다.

<br />
<br />
<br />

# Oracle 인수 이후

<br />

2010년 Oracle이 Sun Microsystems를 인수하면서 Java의 상표권과 지식재산권도 Oracle로 넘어갑니다. Java EE도 예외는 아니었습니다.

<br />

Oracle은 한동안 Java EE를 유지했지만, Java EE의 발전 속도는 점점 느려졌습니다. Java EE 7은 2013년에 나왔고, Java EE 8은 2017년에야 출시되었습니다. 그 사이 Spring 생태계는 빠르게 성장했고, 많은 개발자에게는 Spring Boot가 사실상의 표준처럼 자리 잡았습니다.

<br />

결국 Oracle은 Java EE를 Eclipse Foundation으로 이관하기로 결정합니다. 커뮤니티 중심으로 Java EE를 더 빠르게 발전시키겠다는 방향이었습니다.

<br />

여기까지만 보면 좋은 오픈소스 전환 이야기처럼 보입니다. 문제는 이름과 패키지에서 발생합니다.

<br />
<br />
<br />

# Java라는 이름을 사용할 수 없었다

<br />

Oracle은 Java EE의 코드와 명세를 Eclipse Foundation으로 넘겼지만, "Java"라는 이름의 상표권까지 자유롭게 넘긴 것은 아니었습니다.

<br />

Eclipse Foundation 입장에서는 기존 Java EE라는 이름을 그대로 사용하기 어려웠고, `javax.*` 네임스페이스에 새 API를 계속 추가하는 것도 Oracle의 허가가 필요한 문제가 되었습니다.

<br />

그래서 Java EE는 Jakarta EE로 이름이 바뀌었습니다. 그리고 Jakarta EE 9에서 기존 `javax.*` 네임스페이스를 `jakarta.*`로 전환하는 큰 변경이 진행됩니다.

<br />

중요한 점은 이 변경이 기능 개선을 위한 변경이라기보다는, 생태계의 소유권과 상표권 문제를 정리하기 위한 변경에 가깝다는 점입니다. 그래서 처음 마주하는 개발자 입장에서는 "왜 굳이 패키지 이름을 바꿨지?"라는 생각이 들 수밖에 없습니다.

<br />
<br />
<br />

# Jakarta EE 전환 과정

<br />

Jakarta EE로 이름이 바뀌었다고 해서 바로 모든 패키지가 `jakarta`로 바뀐 것은 아닙니다. 전환은 단계적으로 진행되었습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:28%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">버전</th>
      <th style="padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">내용</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">Jakarta EE 8</td>
      <td style="padding:12px 10px; border-bottom:1px solid #eeeeee;">Java EE 8과 거의 동일한 내용으로 이관, <code style="background:#eef3ea; color:#222; padding:3px 7px; border-radius:6px; font-size:14px;">javax.*</code> 유지</td>
    </tr>
    <tr>
      <td style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">Jakarta EE 9</td>
      <td style="padding:12px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 7px; border-radius:6px; font-size:14px;">javax.*</code>에서 <code style="background:#eef3ea; color:#222; padding:3px 7px; border-radius:6px; font-size:14px;">jakarta.*</code>로 네임스페이스 전환</td>
    </tr>
    <tr>
      <td style="padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">Jakarta EE 10</td>
      <td style="padding:12px 10px; border-bottom:1px solid #eeeeee;">네임스페이스 전환 이후 기능 개선 진행</td>
    </tr>
  </tbody>
</table>

<br />

Jakarta EE 9의 핵심은 API의 동작을 크게 바꾸는 것이 아니라 패키지 네임스페이스를 옮기는 것이었습니다. 그래서 코드상으로는 import 변경이 가장 크게 보입니다.

<br />

하지만 패키지가 바뀌었다는 것은 소스 호환성과 바이너리 호환성이 깨질 수 있다는 의미이기도 합니다. 내가 작성한 코드뿐 아니라, 프로젝트가 사용하는 라이브러리도 Jakarta EE 9 이후의 `jakarta.*` 기반 버전을 지원해야 합니다.

<br />
<br />
<br />

# 실무에서 자주 만나는 변경

<br />

Spring Boot 3.x 프로젝트에서 직접 마주치는 대표적인 패키지는 다음과 같습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:50%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">변경 전</th>
      <th style="width:50%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">변경 후</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">javax.persistence.*</code></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">jakarta.persistence.*</code></td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">javax.servlet.*</code></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">jakarta.servlet.*</code></td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">javax.validation.*</code></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">jakarta.validation.*</code></td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">javax.transaction.*</code></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">jakarta.transaction.*</code></td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">javax.annotation.*</code></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">jakarta.annotation.*</code></td>
    </tr>
  </tbody>
</table>

<br />

JPA 엔티티, Servlet API, Bean Validation, Transaction 관련 코드를 사용하고 있다면 대부분 이 변경을 만나게 됩니다.

<br />

예를 들어 Spring Boot 2.x에서 작성한 엔티티는 다음과 같은 import를 가지고 있을 수 있습니다.

<br />

```java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;
}
```

<br />

Spring Boot 3.x에서는 다음처럼 바뀝니다.

<br />

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;
}
```

<br />

코드 모양만 보면 큰 차이가 없어 보입니다. 그러나 의존성 중 하나라도 오래된 `javax.*` 기반 라이브러리를 물고 있으면 컴파일 에러나 런타임 에러로 이어질 수 있습니다.

<br />
<br />
<br />

# 조심해야 할 점

<br />

## 1. 모든 javax가 jakarta로 바뀌는 것은 아니다

<br />

가장 흔한 실수는 프로젝트 전체에서 `javax` 문자열을 무조건 `jakarta`로 바꾸는 것입니다.

<br />

하지만 모든 `javax` 패키지가 Jakarta EE로 이동한 것은 아닙니다. 예를 들어 `javax.sql.*`, `javax.crypto.*`는 Java SE 영역에 남아 있습니다.

<br />

따라서 단순 find & replace로 전체 치환하면 정상적인 Java SE 패키지까지 깨뜨릴 수 있습니다. IDE의 마이그레이션 기능이나 OpenRewrite 같은 도구를 사용하더라도 변경 결과는 반드시 리뷰해야 합니다.

<br />
<br />
<br />

## 2. 라이브러리 호환성을 같이 확인해야 한다

<br />

내 코드의 import만 `jakarta`로 바꿨다고 마이그레이션이 끝나는 것은 아닙니다.

<br />

Spring Boot 3.x에서는 Jakarta EE 9 이상을 기준으로 동작하기 때문에, 오래된 라이브러리가 내부적으로 `javax.*` API를 사용하고 있다면 문제가 생길 수 있습니다.

<br />

특히 인증, Servlet Filter, JPA 확장, Validation, Swagger/OpenAPI, 파일 업로드, 메일, XML 바인딩 관련 라이브러리는 버전 호환성을 확인하는 것이 좋습니다.

<br />
<br />
<br />

## 3. Spring Boot 3.0 마이그레이션은 Java 17 전환과 함께 온다

<br />

Spring Boot 3.0은 Java 17 이상을 요구합니다. 따라서 `javax`에서 `jakarta`로 바꾸는 문제와 별개로, JDK 버전 전환에 따른 영향도 같이 확인해야 합니다.

<br />

빌드 도구, CI 환경, Docker 이미지, 로컬 개발 환경, 운영 환경의 JDK 버전이 모두 맞아야 합니다. 패키지 import는 고쳤는데 CI에서 Java 11로 빌드하고 있다면 당연히 다른 문제가 발생합니다.

<br />
<br />
<br />

# 마이그레이션할 때의 순서

<br />

개인적으로는 다음 순서로 확인하는 것이 안전하다고 생각합니다.

<br />

1. 현재 Spring Boot 2.x의 최신 패치 버전까지 먼저 올립니다.
2. Java 17에서 빌드와 테스트가 가능한지 확인합니다.
3. Spring Boot 3.x로 올리면서 `javax.*`에서 `jakarta.*` 변경을 진행합니다.
4. 오래된 서드파티 라이브러리와 starter 의존성을 확인합니다.
5. 컴파일 에러를 해결한 뒤 통합 테스트와 주요 API smoke test를 실행합니다.

<br />

Spring Boot 공식 마이그레이션 가이드에서도 Spring Boot 2.7 최신 버전으로 먼저 올린 뒤 3.x로 이동하는 흐름을 권장합니다. 한 번에 모든 것을 바꾸면 문제가 생겼을 때 원인을 찾기 어렵습니다.

<br />
<br />
<br />

# 참고 자료

<br />

- [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Jakarta EE 9 Release](https://jakarta.ee/release/9/)
- [Javax to Jakarta Namespace Ecosystem Progress](https://jakarta.ee/blogs/javax-jakartaee-namespace-ecosystem-progress/)

<br />
<br />
<br />
