# Spring Boot 3.0과 jakarta 패키지 전환

Spring Boot 3.0부터 Java EE에서 Jakarta EE 9+로 전환됨에 따라 패키지 네임스페이스가 변경되었습니다.
**새 파일 작성 시 반드시 `jakarta`를 사용해야 합니다.**

```java
// Spring Boot 2.x
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

// Spring Boot 3.x
import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpServletRequest;
```

---

## 왜 갑자기 패키지 이름이 바뀌었을까?

단순한 리팩터링이 아닙니다. 이 변경에는 약 20년에 걸친 Java 엔터프라이즈 생태계의 주도권 다툼과 상표권 분쟁이라는 복잡한 역사가 담겨 있습니다.

---

## 1. Java EE의 탄생과 Sun Microsystems 시절

1999년, Sun Microsystems는 엔터프라이즈 애플리케이션 개발을 위한 표준 플랫폼으로 **J2EE(Java 2 Platform, Enterprise Edition)** 를 발표했습니다. Servlet, JSP, EJB, JPA 같은 기업용 API들이 모두 이 플랫폼 아래에서 `javax.*` 네임스페이스로 정의되었습니다.

`javax`는 "Java Extension"의 약자로, 핵심 Java SE API를 확장하는 라이브러리라는 의미였습니다. 이후 J2EE는 Java EE 5, 6, 7로 버전업되면서 사실상 엔터프라이즈 Java의 표준으로 자리잡았습니다.

---

## 2. Oracle의 Java 인수 (2010)

2010년, Oracle이 Sun Microsystems를 약 74억 달러에 인수하면서 Java의 모든 상표권과 지식재산권이 Oracle로 넘어갔습니다. Java EE 역시 마찬가지였습니다.

Oracle은 한동안 Java EE를 유지했지만, 점차 관심이 줄어들었습니다. Java EE 8은 2017년에 겨우 출시되었는데, 전 버전인 Java EE 7이 2013년에 나온 것을 감안하면 4년이라는 긴 공백이 있었습니다. 그 사이 Spring 생태계가 빠르게 성장하며 사실상의 표준 자리를 차지해가는 동안, 공식 Java EE는 제자리에 머물렀습니다.

개발자 커뮤니티 사이에서 불만이 쌓이기 시작했고, Oracle은 결국 Java EE를 오픈소스 재단에 이관하기로 결정합니다.

---

## 3. Eclipse Foundation으로의 이관과 상표권 분쟁

2017년, Oracle은 Java EE를 **Eclipse Foundation**에 기증했습니다. 오픈소스 커뮤니티 주도로 더 빠르게 발전시키려는 의도였습니다.

그런데 여기서 결정적인 문제가 발생합니다.

Oracle은 Java EE 코드와 명세는 이관했지만, **"Java"라는 단어가 들어간 상표권은 이관하지 않았습니다.** Eclipse Foundation이 "Java EE"라는 이름을 계속 사용하거나 `javax.*` 네임스페이스에 새 패키지를 추가하려면 Oracle의 허가가 필요했습니다.

Eclipse Foundation은 Oracle과 협상을 시도했습니다. 기존 `javax.*` 패키지를 그대로 유지하되, 새 API는 `javax.*`로 추가할 수 있도록 해달라고 요청했습니다. 그러나 Oracle은 이를 거부했습니다. 결국 Eclipse Foundation이 선택할 수 있는 길은 하나뿐이었습니다.

> **브랜드를 완전히 바꾼다.**

Java EE는 **Jakarta EE**로 이름이 바뀌었고, `javax.*` 네임스페이스는 **`jakarta.*`** 로 전환되었습니다. Jakarta는 자바(Java) 섬의 수도이자, 역사적으로 Java 커뮤니티와 인연이 깊은 도시 이름이기도 합니다.

---

## 4. 전환의 단계적 진행

네임스페이스 변경이 한 번에 이루어진 것은 아닙니다. 이관 초기에는 하위 호환성을 위해 `javax.*`를 유지했습니다.

| 버전 | 내용 |
|------|------|
| **Jakarta EE 8** (2019) | Java EE 8과 동일한 내용, `javax.*` 유지. 사실상 브랜드만 변경 |
| **Jakarta EE 9** (2020) | `javax.*` → `jakarta.*` 로 전면 전환. API 내용 변경 없이 네임스페이스만 교체 |
| **Jakarta EE 10** (2022) | 네임스페이스 전환 완료 후 본격적인 기능 개선 시작 |

Spring Boot도 이 흐름을 따랐습니다.

| Spring Boot 버전 | 기반 | 네임스페이스 |
|-----------------|------|-------------|
| **2.x** | Java EE / Jakarta EE 8 | `javax.*` |
| **3.x** | Jakarta EE 9+ | `jakarta.*` |

Spring Boot 3.0은 2022년 11월에 출시되면서 Jakarta EE 9+를 기반으로 `jakarta.*`를 전면 채택했습니다. 동시에 Java 17을 최소 요구 버전으로 올렸습니다.

---

## 5. 실무에서 마주치는 영향

패키지 이름만 바뀌었을 뿐 API 사용법 자체는 동일합니다. 하지만 이 변경은 생각보다 넓은 범위에 영향을 줍니다.

**직접 영향을 받는 주요 패키지**

| 변경 전 | 변경 후 |
|---------|---------|
| `javax.persistence.*` | `jakarta.persistence.*` |
| `javax.servlet.*` | `jakarta.servlet.*` |
| `javax.validation.*` | `jakarta.validation.*` |
| `javax.transaction.*` | `jakarta.transaction.*` |
| `javax.annotation.*` | `jakarta.annotation.*` |

**주의할 점들**

- `javax.sql.*`, `javax.crypto.*` 같이 Java SE에 포함된 패키지들은 변경되지 않습니다. Oracle이 여전히 관리하는 영역이기 때문입니다.
- Spring Boot 2.x에서 3.x로 마이그레이션할 때 단순 find & replace로 `javax` → `jakarta`를 교체하면 Java SE 패키지까지 바뀌는 실수가 생길 수 있습니다.
- 서드파티 라이브러리가 아직 `javax.*`를 사용하고 있다면 Spring Boot 3.x와 호환되지 않을 수 있습니다. 의존성 버전 확인이 필요합니다.

> [!TIP]
> Spring Boot 팀이 공식 제공하는 [Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)와 `spring-boot-properties-migrator` 를 활용하면 마이그레이션 작업을 크게 줄일 수 있습니다.

---

## 정리

`javax` → `jakarta` 는 단순한 패키지 이름 변경이 아닙니다. Oracle이 Java 상표권을 내어주지 않으면서 생긴 불가피한 결과였고, Java 엔터프라이즈 생태계가 특정 기업의 통제에서 벗어나 커뮤니티 주도로 전환되는 과정의 산물입니다.

Spring Boot 3.x 프로젝트를 새로 시작하거나 기존 프로젝트를 마이그레이션할 때, 이 배경을 이해하고 있으면 왜 이런 변경이 생겼는지, 그리고 앞으로 Jakarta EE가 어떤 방향으로 발전해 나갈지를 더 잘 예측할 수 있습니다.
