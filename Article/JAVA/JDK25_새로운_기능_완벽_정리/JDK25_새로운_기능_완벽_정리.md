# JDK 25 새로운 기능 완벽 정리

---


## 시작하며

![alt text](./img/30_year.png)

> Source: Linkedin [(Link)](https://www.linkedin.com/posts/vladmihalcea_30-years-ago-today-java-10-was-released-activity-7420470686995009536-VX26)

자바는 올해로 30주년을 맞이하였습니다. 한때 VM 기반의 성능 한계나 장황한 코드라는 비판을 받기도 했지만, 자바는 늘 끊임없이 변화하였습니다. 오늘날 가장 현대적이고 강력한 생태계를 갖춘 언어로 자리매김하고 있습니다.

JDK25는 장기 지원(LTS) 버전입니다. 단순한 버그 수정이나 소소한 개선이 아니라, 개발자 생산성과 성능을 크게 향상시키는 굵직한 변화들이 포함되어 있습니다. 일부 기능은 이전 버전에서 Preview로 먼저 선보였다가 이번에 정식 채택된 것들도 있습니다. 역동적인 자바의 변화 내용을 살펴보려고 합니다.

---


<br />

---

## 패턴 매칭에서 기본형(Primitive Type) 사용 — JEP 507

패턴 매칭이 더 똑똑해졌습니다.

기존에는 `switch`와 `instanceof`의 패턴 매칭에서 참조 타입만 사용할 수 있었습니다. JDK 25부터는 `int`, `long` 같은 기본형도 패턴 매칭에 직접 사용할 수 있습니다.

### 기존 방식 (JDK 25 이전)

```java
int status = x.getStatus();
String message;
if (status == 0) {
    message = "정상";
} else if (status == 1) {
    message = "경고";
} else if (status == 2) {
    message = "오류";
} else {
    message = "알 수 없는 상태: " + status;
}
```

### JDK 25 방식

```java
String message = switch (x.getStatus()) {
    case 0 -> "정상";
    case 1 -> "경고";
    case 2 -> "오류";
    case int i -> "알 수 없는 상태: " + i; // 매칭된 값을 변수로 바로 사용 가능
};
```

`case int i`처럼 기본형으로 매칭하면서 동시에 해당 값을 변수 `i`에 바인딩할 수 있습니다. 더 이상 별도의 변수 선언 없이 간결하게 처리할 수 있게 되었습니다.

---

<br />

---

## 2. 모듈 임포트

여러 패키지에서 클래스를 임포트할 때 import 문이 길게 늘어지는 문제를 경험해 보셨을 겁니다.

### 기존 방식

```java
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
```

### JDK 25 방식 — 모듈 단위 임포트

```java
import module java.util;
```

단 한 줄로 `java.util` 모듈 전체를 임포트할 수 있습니다. 이후 코드도 동일하게 동작합니다.

```java
String[] fruits = { "apple", "berry", "citrus" };

Map<String, String> map = Stream.of(fruits)
    .collect(Collectors.toMap(
        s -> s.toUpperCase().substring(0, 1),
        Function.identity()
    ));
```

### 주의: 모호한 참조(Ambiguous Reference)

서로 다른 모듈에 동일한 이름의 클래스가 있을 경우 컴파일 에러가 발생할 수 있습니다.

```java
import module java.base; // java.util.Date 포함
import module java.sql;  // java.sql.Date 포함

// ⚠️ Date가 어느 것인지 모호 → 명시적 임포트 필요
import java.util.Date; // 또는 java.sql.Date
```

이런 경우에는 기존 방식의 명시적 임포트로 직접 지정해 주어야 합니다.

---

<br />

---


## 간결한 소스 파일 & 인스턴스 메인 메서드 — JEP 512

Java를 처음 배울 때 당혹스러운것은 보일러플레이트 코드입니다.

### 기존의 코드

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

### JDK 25 방식 — 클래스 없이 바로 실행

```java
void main() {
    System.out.println("Hello, World!");
}
```

클래스 선언, `static`, `public`, `String[] args` — 아무것도 필요 없습니다. 스크립트처럼 바로 작성하고 실행할 수 있습니다.

### 새로운 IO 패키지로 더욱 간결하게

초보자를 위한 새로운 `IO` 유틸리티 클래스도 함께 도입되었습니다. `System.out.println`을 외울 필요가 없어집니다.

```java
void main() {
    IO.println("Hello, World!");
    IO.println("Java가 더 쉬워졌습니다!");
}
```

이 기능은 Java를 처음 배우는 입문자에게 특히 유용하며, 간단한 스크립트나 프로토타입 작성 시에도 불필요한 구조를 제거해 줍니다.


---

<br />

---

## 스코프 값 (Scoped Values) — JEP 506 (정식 채택)

JDK 25에서 **Preview에서 정식 기능으로 졸업**한 가장 주목할 만한 기능입니다. 스레드 간 **불변 컨텍스트 데이터**를 공유하는 방식을 근본적으로 개선합니다.

### 왜 필요한가? — ThreadLocal의 문제점

기존 Java에서 여러 계층에 걸쳐 데이터를 전달하려면 두 가지 방법이 있었습니다.

**방법 1: 파라미터로 계속 전달 (Parameter Drilling)**

```java
// A → B → C로 호출될 때, C만 context를 필요로 해도
// A, B 모두 파라미터로 받아서 넘겨야 함
public void handleRequest(FrameworkContext ctx) {
    processData(ctx);              // ctx 필요 없어도 전달
}

public void processData(FrameworkContext ctx) {
    readUserInfo(ctx);             // ctx 필요 없어도 전달
}

public void readUserInfo(FrameworkContext ctx) {
    String key = ctx.getKey();     // 여기서 실제로 사용
}
```

**방법 2: ThreadLocal 사용**

```java
static final ThreadLocal<FrameworkContext> CONTEXT = new ThreadLocal<>();

// 설정
CONTEXT.set(frameworkContext);

// 사용 (어느 메서드에서든)
FrameworkContext ctx = CONTEXT.get();
```

이런 방법은 다음의 문제를 갖습니다.

__문제 1. 가변(Mutable) — 언제든 덮어쓸 수 있다__

ThreadLocal은 set()을 통해 값을 언제든 변경할 수 있습니다. 여러 레이어를 거치는 과정에서 하위 메서드가 실수로 값을 덮어쓰더라도 컴파일 타임에 전혀 감지되지 않습니다.

```java
CONTEXT.set(new FrameworkContext("user-123"));

// ... 여러 레이어를 거치다가 하위 메서드에서 실수로 덮어씀
CONTEXT.set(new FrameworkContext("anonymous")); // ⚠️ 원본 컨텍스트 유실

// 이후 어디선가 꺼내면 엉뚱한 값이 나옴
FrameworkContext ctx = CONTEXT.get(); // "anonymous" — 원인 추적이 매우 어려움
```

__문제 2. 수동 remove() 누락 시 메모리 누수__

ThreadLocal의 값은 스레드가 살아있는 한 GC 대상이 되지 않습니다. 특히 스레드 풀(Thread Pool) 환경에서는 요청이 끝난 후에도 스레드가 재사용되기 때문에, remove()를 명시적으로 호출하지 않으면 이전 요청의 컨텍스트가 다음 요청에 그대로 남아있게 됩니다.

```java
// 서블릿 또는 필터에서 요청마다 컨텍스트를 설정하는 경우
CONTEXT.set(new FrameworkContext(request));

try {
    handleRequest();
} finally {
    CONTEXT.remove(); // ✅ 반드시 제거해야 함
                      // ⚠️ 이 줄이 누락되면 스레드 풀에서 꺼낸 다음 요청이
                      //    이전 사용자의 컨텍스트를 그대로 바라보게 됨 (데이터 오염)
}
```
누수가 누적되면 OutOfMemoryError로 이어질 수 있으며, 보안 관점에서도 다른 사용자의 인증 정보가 흘러들어가는 심각한 버그의 원인이 됩니다.


__문제 3. 가상 스레드(Virtual Thread)와의 근본적인 비효율__

Java 21부터 도입된 가상 스레드는 수십만 개를 동시에 생성하는 것을 전제로 설계되었습니다. 그런데 ThreadLocal은 스레드 하나당 독립적인 저장 공간을 할당하는 구조이기 때문에, 가상 스레드 수만큼 ThreadLocal 저장 공간도 함께 증가합니다.

```java
// 가상 스레드를 100,000개 생성하는 경우
for (int i = 0; i < 100_000; i++) {
    Thread.ofVirtual().start(() -> {
        CONTEXT.set(new FrameworkContext(...));
        // 각 가상 스레드마다 ThreadLocalMap 엔트리가 생성됨
        // → 메모리 사용량이 가상 스레드 수에 비례해 선형으로 증가
        handleRequest();
    });
}
```
가상 스레드의 핵심 장점인 "저렴한 비용으로 대량 생성"이 ThreadLocal의 1:1 저장 구조와 충돌하여, 대규모 동시성 환경에서 오히려 메모리 부담이 되는 역설이 발생합니다.


### 스코프 값 사용법

이러한 문제를 스코프 값으로 해결할  수 있습니다.

```java
// 1. ScopedValue 선언 (static final)
static final ScopedValue<FrameworkContext> CONTEXT = ScopedValue.newInstance();

// 2. 특정 스코프 내에서 값 바인딩
ScopedValue.where(CONTEXT, frameworkContext).run(() -> {
    handleRequest(); // 이 블록 안에서는 CONTEXT.get() 사용 가능
});
// 블록이 끝나면 자동으로 해제됨 — 메모리 누수 없음
```

```java
// 3. 어느 메서드에서든 get()으로 접근
public void readUserInfo() {
    FrameworkContext ctx = CONTEXT.get(); // 파라미터 없이 바로 접근
    String key = ctx.getKey();
}
```

### 예시 — 웹 프레임워크


전형적인 3-tier 웹 애플리케이션(Controller → Service → Repository)을 예시로 살펴보겠습니다. 로그인한 사용자 정보를 각 계층에서 조회해 보겠습니다.

__전체 구조 정의__
```java
public class Server {
    public static final ScopedValue<User> LOGGED_IN_USER = ScopedValue.newInstance();
}
```

ScopedValue는 반드시 static final로 선언합니다. 이 필드 자체는 값을 담는 게 아니라 키(key) 역할을 합니다.


__1단계 — 서블릿 진입점: 인증 후 스코프 바인딩__
```java
public class DispatcherServlet {

    private final UserController controller = new UserController();

    public void serve(HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = authenticateUser(request); // JWT 파싱, 세션 조회 등

        if (user.isPresent()) {
            // where().run() 블록이 시작되는 순간부터 LOGGED_IN_USER가 바인딩됨
            // 이 블록 안에서 호출되는 모든 메서드는 LOGGED_IN_USER.get()으로 접근 가능
            // 블록이 끝나면 바인딩이 자동 해제됨 — 명시적 cleanup 불필요
            ScopedValue.where(Server.LOGGED_IN_USER, user.get())
                       .run(() -> controller.processRequest(request, response));
        } else {
            response.setStatus(401); // 인증 실패 시 스코프 자체를 열지 않음
        }
    }
}
```


__2단계 — Controller: 유저 정보를 파라미터로 받지 않아도 됨__
```java
public class UserController {

    private final OrderService orderService = new OrderService();

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();

        // LOGGED_IN_USER를 파라미터로 받을 필요가 없음
        // 단지 요청을 적절한 서비스로 위임하기만 함
        if (path.endsWith("/orders")) {
            List<Order> orders = orderService.getOrdersForCurrentUser();
            writeJson(response, orders);
        }
    }
}
```

__3단계 — Service: 필요한 시점에 비로소 꺼내서 사용__
```java
public class OrderService {

    private final OrderRepository orderRepository = new OrderRepository();

    public List<Order> getOrdersForCurrentUser() {
        // 이 메서드가 serve()로부터 몇 단계나 떨어져 있든 상관없이
        // 동일한 스코프 안에 있다면 .get()으로 즉시 접근 가능
        User currentUser = Server.LOGGED_IN_USER.get();

        // 스코프 밖에서 .get()을 호출하면 NoSuchElementException 발생
        // → 바인딩 여부를 먼저 확인하고 싶다면 isBound()를 사용하여 확인
        if (!Server.LOGGED_IN_USER.isBound()) {
            throw new IllegalStateException("인증된 사용자 컨텍스트가 없습니다.");
        }

        return orderRepository.findByUserId(currentUser.getId());
    }
}
```


__4단계 — Repository: 동일하게 스코프에서 접근__

```java
public class OrderRepository {

    public List<Order> findByUserId(long userId) {
        // Repository 계층에서도 필요하다면 직접 꺼낼 수 있음
        User currentUser = Server.LOGGED_IN_USER.get();
        auditLog.record(currentUser.getId(), "ORDER_READ");
        return db.query("SELECT * FROM orders WHERE user_id = ?", userId);
    }
}
```

**호출 흐름 요약**
```
DispatcherServlet.serve()
  └─ ScopedValue.where(LOGGED_IN_USER, user).run(...)  ← 바인딩 시작
       └─ UserController.processRequest()               ← 파라미터에 User 없음
            └─ OrderService.getOrdersForCurrentUser()   ← .get()으로 꺼냄
                 └─ OrderRepository.findByUserId()      ← .get()으로 꺼냄
  └─ (블록 종료)                                        ← 자동 해제
```

Controller와 Service 어디에도 User를 파라미터로 넘기는 코드가 없지만, 필요한 계층에서는 언제든 꺼내 쓸 수 있습니다. 그리고 serve() 블록이 끝나는 순간 바인딩은 자동으로 해제되므로, ThreadLocal처럼 remove()를 깜빡할 위험이 구조적으로 존재하지 않습니다.



### 스콥프 값의 장점

__1. 불변성 (Immutable)__

한 번 where().run() 블록으로 바인딩된 값은 해당 스코프 내에서 절대 변경할 수 없습니다. ScopedValue에는 애초에 set() 메서드가 존재하지 않으며, 이는 API 설계 차원에서 변경 자체를 불가능하게 막아둔 것입니다.

```java
static final ScopedValue<User> LOGGED_IN_USER = ScopedValue.newInstance();

ScopedValue.where(LOGGED_IN_USER, user).run(() -> {
    // LOGGED_IN_USER.set(anotherUser); // ❌ set()과 같이 값을 수정할 수 있는 메서드가 존재하지 않음
    User u = LOGGED_IN_USER.get();      // ✅ 읽기만 가능
});
```

ThreadLocal에서 하위 메서드가 실수로 값을 덮어쓰는 문제가 구조적으로 발생할 수 없습니다. 스코프 안의 어떤 코드도 바인딩된 값을 바꿀 수 없으므로, 멀티스레드 환경에서도 별도의 동기화 없이 안전하게 읽을 수 있습니다.

단, 동일한 ScopedValue에 대해 **중첩 스코프(nested scope)**를 열면 그 블록 안에서만 일시적으로 다른 값을 바인딩할 수 있습니다. 이는 덮어쓰기가 아니라 새로운 스코프에서의 독립적인 바인딩이며, 내부 블록이 끝나면 원래 값이 그대로 복원됩니다.

```java
ScopedValue.where(LOGGED_IN_USER, userA).run(() -> {
    System.out.println(LOGGED_IN_USER.get()); // "userA"

    // 내부 스코프에서 다른 값으로 바인딩 (원본은 건드리지 않음)
    ScopedValue.where(LOGGED_IN_USER, userB).run(() -> {
        System.out.println(LOGGED_IN_USER.get()); // "userB"
    });

    System.out.println(LOGGED_IN_USER.get()); // 다시 "userA" — 원복됨
});
```

__2. 결정론적 생명주기 (Deterministic Lifecycle)__

ScopedValue의 바인딩은 where().run() 블록의 시작과 끝에 정확히 일치합니다. 블록이 정상 종료되든, 예외로 종료되든 상관없이 스코프가 끝나는 순간 바인딩은 자동으로 해제됩니다.


```java
ScopedValue.where(LOGGED_IN_USER, user).run(() -> {
    System.out.println(LOGGED_IN_USER.isBound()); // true

    // 예외가 발생해도 블록 종료 시 바인딩 자동 해제
    throw new RuntimeException("처리 중 오류 발생");
});

// 블록 바깥에서는 바인딩이 존재하지 않음
System.out.println(LOGGED_IN_USER.isBound()); // false
// LOGGED_IN_USER.get(); // NoSuchElementException 발생
```

ThreadLocal은 스레드가 살아있는 한 값이 유지되므로, 스레드 풀 환경에서 remove()를 빠뜨리면 이전 요청의 데이터가 다음 요청으로 흘러들어가는 문제가 생깁니다. ScopedValue는 이런 실수를 구조적으로 차단합니다. 개발자가 명시적으로 정리 코드를 작성할 필요가 없으며, 바인딩의 유효 범위가 코드 구조만으로 명확하게 드러납니다.


__3. 효율적인 조회 성능 (Efficient Lookup)__

ThreadLocal은 내부적으로 각 스레드가 ThreadLocalMap이라는 해시맵을 가지고 있으며, 값을 조회할 때마다 이 맵에서 키를 해시하여 탐색합니다. 저장된 ThreadLocal의 수가 많아질수록 해시 충돌이나 맵 탐색 비용이 커질 수 있습니다.
반면 ScopedValue는 호출 스택 자체를 활용하는 방식으로 구현되어 있습니다. 값을 조회할 때 현재 스코프에서 가장 가까운 바인딩을 찾아 올라가는 구조로, 해시 연산 없이 스택 탐색만으로 처리됩니다.

```java
// 콜 스택이 아무리 깊어도 조회 비용은 일정 수준으로 유지됨
ScopedValue.where(LOGGED_IN_USER, user).run(() -> {
    methodA(); // 내부에서 methodB() → methodC() → ... → methodZ() 호출
});

public void methodZ() {
    // 수십 단계 아래에 있어도 O(depth) 수준의 스택 탐색으로 조회
    User u = LOGGED_IN_USER.get();
}
```

또한 ScopedValue는 가상 스레드(Virtual Thread)와 함께 사용할 때 ThreadLocal보다 훨씬 효율적입니다. ThreadLocal은 스레드마다 독립적인 ThreadLocalMap을 할당하므로 가상 스레드가 수십만 개 생성될 경우 그만큼의 맵이 메모리에 올라갑니다. ScopedValue는 값을 스레드 로컬 저장소가 아닌 스코프 구조 안에 두기 때문에, 가상 스레드 수가 늘어나도 추가적인 저장 공간이 선형으로 증가하지 않습니다.

---

<br />

---


## 안정적인 값 (Stable Values) — JEP 502 (프리뷰)

> 아직 Preview 기능으로, 기본적으로 비활성화 상태입니다. `--enable-preview` 플래그로 활성화할 수 있습니다.

```
javac --enable-preview --release 25 Main.java
java --enable-preview Main
```

### 배경: final vs. 지연 초기화의 딜레마

자바에서 필드를 안전하게 다루는 방법은 크게 두 가지입니다. 그런데 두 방법 모두 실무에서 타협이 필요한 트레이드오프를 갖고 있습니다.

__방법 1: final 필드로 즉시 초기화__

```java
// ✅ 
// ❌ 단점: 
public class OrderService {
    private final Logger logger = Logger.create(...); // 사용 안 해도 무조건 생성
}
```

이렇게 final 필드를 사용하여 불변성 보장하며 JVM 최적화(상수 폴딩) 가능합니다. 그러나 생성자에서 반드시 즉시 초기화해야 합니다.

테스트 환경이나 특정 실행 경로에서 전혀 쓰이지 않더라도 OrderService 인스턴스가 생성되는 순간 모두 초기화됩니다. 애플리케이션 시작 시간 증가, 불필요한 리소스 점유하는 문제가 있습니다.



__방법 2: non-final 필드로 지연 초기화 (Lazy Initialization)__

```java

private Logger logger = null;

public Logger getLogger() {
    if (logger == null) {
        synchronized (this) {
            if (logger == null) {
                logger = Logger.create(...); // 이중 체크 락 패턴
            }
        }
    }
    return logger;
}
```

이를 통해서 필요할 때만 생성하는 지연 초기화를 할 수 있습니다. 

그러나 불변성이 없으며, 예시 코드는 동시성 문제가 있는 코드로 이중 체크 락(Double-Checked Locking) 패턴을 적용하기도 합니다. 불변성과 JVM 최적화를 위해서 즉시 초기화를 해야합니다.

### Stable Values로 문제 해결하기


JEP 502는 이 딜레마를 해결하기 위해 StableValue<T> API를 도입하였습니다. final의 불변성 보장과 지연 초기화를 동시에 제공하면서, JVM이 이를 상수처럼 최적화할 수 있도록 설계되었습니다.
```java
private final StableValue<Logger> logger = StableValue.of();

public Logger getLogger() {
    return logger.orElseSet(() -> Logger.create(...));    
}
```

StableValue.of()는 값이 아직 담기지 않은 빈 컨테이너를 생성합니다. 이 컨테이너 자체는 final로 선언되므로 다른 인스턴스로 교체할 수 없고, 내부에 담길 실제 값은 처음 getLogger()가 호출되는 시점까지 생성을 미룹니다.

orElseSet()은 컨테이너가 비어있는지 여부에 따라 두 가지로 동작합니다. 아직 값이 설정되지 않은 상태라면 인자로 전달한 supplier 람다(() -> Logger.create(...))를 실행하여 값을 초기화하고 반환합니다. 이미 값이 설정된 상태라면 supplier를 실행하지 않고 저장된 값을 즉시 반환합니다. __즉, Logger.create()는 애플리케이션 전체 생명주기 동안 단 한 번만 호출되는 것이 보장됩니다.__

동시성 측면에서도 별도의 처리가 필요하지 않습니다. 여러 스레드가 __동시에 최초 orElseSet()을 호출하더라도 JVM이 내부적으로 단 한 번의 초기화만 일어나도록 보장합니다.__ 이중 체크 락 패턴을 위하여 volatile 선언, synchronized 블록, null 체크가 모두 orElseSet() 한 줄로 대체됩니다.

JVM 최적화 관점에서도 StableValue는 final 필드와 동등한 수준의 최적화를 목표로 설계되었습니다. 한 번 값이 설정되면 이후 절대 변경되지 않는다는 것을 JVM이 인지할 수 있으므로, 상수 폴딩이나 값 인라이닝 같은 JIT 최적화를 적용할 수 있습니다. non-final 필드 기반의 지연 초기화에서는 불가능했던 부분입니다.

---

<br />

---

## 유연한 생성자 본문 (Flexible Constructor Bodies) - JEP 513

### 문제 상황

Java는 오랫동안 생성자 안에서 super() 또는 this() 호출이 반드시 첫 번째 문장이어야 한다는 규칙을 강제해왔습니다. 이 규칙은 부모 클래스가 자식 클래스보다 먼저 초기화되어야 한다는 원칙에서 비롯된 것으로, 컴파일러가 이를 엄격하게 검사합니다.

```java
class Base {
    Base(int value) {
        System.out.println("Base 초기화: " + value);
    }
}

class Sub extends Base {
    Sub(int x) {
        if (x < 0) throw new IllegalArgumentException("음수는 허용되지 않습니다."); // ❌ 컴파일 에러
        super(x); // super()가 첫 번째 줄이 아니면 컴파일 자체가 불가능
    }
}
```

이 제약은 단순해 보이지만 실제 코드를 작성할 때 꽤 많은 불편함을 야기합니다. super()에 넘길 인자를 미리 검증하거나 가공해야 하는 경우, 개발자는 억지스러운 우회 방법을 써야 했습니다.

### 우회 방법 1: 정적 헬퍼 메서드로 검증 로직 분리

```java
class PositiveNumber extends Number {
    PositiveNumber(int x) {
        super(validate(x)); // 검증 로직을 별도 static 메서드로 빼내야 함
    }

    // 생성자 안에 자연스럽게 쓸 수 있는 코드를
    // 굳이 static 메서드로 분리해야 하는 부자연스러움
    private static int validate(int x) {
        if (x < 0) throw new IllegalArgumentException("양수만 허용됩니다: " + x);
        return x;
    }
}
```

단순한 검증 로직 하나를 위해 클래스에 static 메서드가 추가되고, 코드의 흐름이 생성자와 헬퍼 메서드 사이에 분산됩니다.


### 우회 방법 2: 삼항 연산자로 인라인 처리
```java
class Connection extends BaseConnection {
    Connection(String host, int port) {
        // 검증과 변환 로직이 super() 인자 안에 모두 들어가야 함
        // 로직이 복잡해질수록 가독성이 급격히 나빠짐
        super(
            host != null ? host : "localhost",
            port > 0 && port < 65536 ? port : throwException("유효하지 않은 포트: " + port)
        );
    }

    private static int throwException(String msg) {
        throw new IllegalArgumentException(msg);
        // 예외를 던지기 위해 int를 반환하는 척하는 메서드까지 필요
    }
}
```

인자 검증이 복잡해질수록 super() 호출이 읽기 어려운 중첩 표현식으로 뒤엉키게 됩니다.

### 유연한 생성자 본문 방식
유연한 생성자 본문은 이 제약을 완화합니다. super() 또는 this() 호출 이전에도 코드를 작성할 수 있게 되며, 단 한 가지 조건만 지키면 됩니다. 생성 중인 객체(this)를 참조하지 않아야 합니다. 인자 검증, 로컬 변수 선언, 정적 메서드 호출 등은 모두 허용됩니다.

```java
class PositiveNumber extends Number {
    PositiveNumber(int x) {
        // ✅ super() 이전에 검증 로직 작성 가능
        if (x < 0) throw new IllegalArgumentException("양수만 허용됩니다: " + x);
        super(x);
    }
}
```
헬퍼 메서드 없이 생성자 안에서 자연스러운 흐름으로 검증 후 부모 생성자를 호출할 수 있습니다.

---

<br />

---

## 구조적 동시성 (Structured Concurrency, Preview 5회차) - JEP 505

__기존 멀티스레드 코드의 문제점__

하나의 요청을 처리하기 위해 여러 작업을 병렬로 실행해야 하는 상황은 매우 흔합니다. 예를 들어 사용자 프로필 페이지를 렌더링하려면 사용자 정보, 주문 내역, 추천 상품을 동시에 조회하는 것이 순차 실행보다 훨씬 빠릅니다. 기존에는 이런 병렬 처리를 ExecutorService와 Future로 구현했는데, 이 방식은 코드가 복잡해질수록 여러 문제가 드러납니다.

```java
// 기존 방식: ExecutorService + Future
ExecutorService executor = Executors.newFixedThreadPool(2);

Future<String> userFuture  = executor.submit(() -> fetchUser(id));
Future<Order>  orderFuture = executor.submit(() -> fetchOrder(id));

try {
    String user  = userFuture.get();   // 결과 대기
    Order  order = orderFuture.get();  // 결과 대기
    return new Response(user, order);
} catch (ExecutionException e) {
    // ⚠️ 문제 1: userFuture가 실패해도 orderFuture는 계속 실행 중
    //           불필요한 작업이 백그라운드에서 리소스를 낭비함
    userFuture.cancel(true);   // 수동으로 취소해야 함
    orderFuture.cancel(true);  // 하나씩 직접 챙겨야 함
    throw e;
} catch (InterruptedException e) {
    // ⚠️ 문제 2: 현재 스레드가 인터럽트되어도
    //           자식 작업들은 자동으로 중단되지 않음
    userFuture.cancel(true);
    orderFuture.cancel(true);
    Thread.currentThread().interrupt();
    throw e;
}
// ⚠️ 문제 3: executor를 직접 생성했다면 shutdown()도 직접 해야 함
// ⚠️ 문제 4: 스레드 덤프를 떠도 userFuture와 orderFuture가
//           어느 요청에서 파생된 작업인지 추적이 어려움
```

이 코드의 핵심 문제는 **작업의 생명주기가 코드 구조와 일치하지 않는다**는 점입니다. `userFuture`와 `orderFuture`는 논리적으로 하나의 요청을 처리하기 위해 함께 시작된 작업이지만, 런타임에서는 서로 아무런 관계가 없는 독립적인 스레드로 동작합니다. 하나가 실패하거나 취소되어도 나머지는 전혀 영향을 받지 않으므로, 개발자가 모든 예외 경로에서 수동으로 정리 코드를 작성해야 합니다.


### 구조적 동시성의 핵심 아이디어

구조적 동시성은 **함께 시작된 작업은 함께 끝나야 한다** 는 원칙을 API 수준에서 강제합니다. 마치 구조적 프로그래밍에서 `if`, `for`, `try` 블록이 명확한 진입점과 출구를 가지는 것처럼, 병렬 작업도 하나의 스코프 안에서 시작되고 그 스코프가 닫힐 때 모든 작업이 완료(혹은 취소)되도록 보장합니다.

```
// 구조적 동시성의 작업 계층 구조
메인 스레드 (요청 처리)
├── fork: fetchUser(id)   ─┐
└── fork: fetchOrder(id)  ─┤ scope.join() 시점에
                            └─ 모든 작업이 완료될 때까지 대기
                               스코프가 닫히면 미완료 작업 자동 취소
```

### StructuredTaskScope 사용법

```java
// try-with-resources로 스코프를 열고, 블록을 벗어나면 자동으로 닫힘
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

    // fork(): 새 가상 스레드에서 작업을 시작하고 Subtask 핸들을 반환
    // Future와 달리 Subtask는 이 scope에 종속됨
    Subtask<String> userTask  = scope.fork(() -> fetchUser(id));
    Subtask<Order>  orderTask = scope.fork(() -> fetchOrder(id));

    // join(): 포크된 모든 작업이 완료될 때까지 현재 스레드를 블로킹
    // throwIfFailed(): 하나라도 실패했다면 첫 번째 예외를 현재 스레드에서 던짐
    scope.join().throwIfFailed();

    // 여기까지 도달했다면 두 작업 모두 성공한 상태
    return new Response(userTask.get(), orderTask.get());

} // 스코프 종료: 아직 실행 중인 작업이 있다면 자동으로 인터럽트 후 정리
```

### 두 가지 종료 정책

StructuredTaskScope는 작업 실패 시 나머지 작업을 어떻게 처리할지에 대한 두 가지 내장 정책을 제공합니다.

__ShutdownOnFailure — 하나라도 실패하면 전체 취소__

여러 작업이 모두 성공해야만 의미 있는 결과를 만들 수 있을 때 사용합니다. 사용자 정보와 주문 내역을 함께 보여줘야 하는 페이지처럼, 하나라도 빠지면 응답 자체가 불완전한 경우에 적합합니다.

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> userTask    = scope.fork(() -> fetchUser(id));
    Subtask<Order>  orderTask   = scope.fork(() -> fetchOrder(id));
    Subtask<List<Product>> recsTask = scope.fork(() -> fetchRecommendations(id));

    scope.join().throwIfFailed();
    // fetchUser()가 예외를 던지는 순간:
    // 1. scope가 shutdown 신호를 보냄
    // 2. 아직 실행 중인 orderTask, recsTask에 인터럽트 전달
    // 3. join() 반환 후 throwIfFailed()가 원인 예외를 다시 던짐
    // 개발자가 cancel()을 한 번도 호출하지 않아도 자동으로 정리됨

    return new ProfilePage(userTask.get(), orderTask.get(), recsTask.get());
}
```

__ShutdownOnSuccess — 하나라도 성공하면 나머지 취소__

여러 소스 중 가장 빠르게 응답한 결과만 사용하면 될 때 적합합니다. 예를 들어 여러 지역 서버 중 가장 먼저 응답한 서버의 결과를 사용하는 경우입니다.
```java
try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
    scope.fork(() -> fetchFromSeoulServer(query));
    scope.fork(() -> fetchFromTokyoServer(query));
    scope.fork(() -> fetchFromSingaporeServer(query));

    scope.join();
    // 세 작업 중 하나라도 성공적으로 완료되는 순간:
    // 1. scope가 나머지 두 작업에 인터럽트 전달
    // 2. 가장 먼저 성공한 결과만 반환

    return scope.result(); // 가장 먼저 성공한 작업의 결과 반환
}
```

### 예외 처리 흐름 비교

구조적 동시성이 가져오는 가장 큰 실질적 이점은 예외 처리 코드가 얼마나 단순해지는지에 있습니다.

__기존 방식: 모든 예외 경로에서 수동 취소 필요__

```java
Future<String> f1 = executor.submit(task1);
Future<String> f2 = executor.submit(task2);
try {
    String r1 = f1.get();
    String r2 = f2.get();
    return process(r1, r2);
} catch (ExecutionException e) {
    f1.cancel(true); // 잊으면 리소스 낭비
    f2.cancel(true); // 잊으면 리소스 낭비
    throw e;
} catch (InterruptedException e) {
    f1.cancel(true);
    f2.cancel(true);
    Thread.currentThread().interrupt();
    throw e;
}
```

__구조적 동시성: 정리 코드 불필요__
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> s1 = scope.fork(task1);
    Subtask<String> s2 = scope.fork(task2);
    scope.join().throwIfFailed();
    return process(s1.get(), s2.get());
} // 예외 발생 여부와 무관하게 스코프 종료 시 자동 정리
```

---

### 관찰 가능성(Observability) 향상

구조적 동시성은 성능 이점 외에도 디버깅과 모니터링 측면에서 중요한 개선을 가져옵니다. `fork()`로 생성된 가상 스레드는 부모 스레드와 명확한 계층 관계를 형성하므로, 스레드 덤프에서 어떤 요청이 어떤 작업을 파생시켰는지 한눈에 파악할 수 있습니다.


__기존 ExecutorService 사용 시__
```
Thread[#42, request-handler, 5, main]        ← 부모 스레드
Thread[#57, pool-1-thread-1, 5, main]        ← 어떤 요청에서 온 작업인지 불명확
Thread[#58, pool-1-thread-2, 5, main]        ← 어떤 요청에서 온 작업인지 불명확
```


__구조적 동시성 사용 시 스레드 덤프 구조__
```
Thread[#42, request-handler, 5, main]        ← 요청을 처리하는 부모 스레드
└── Thread[#57, virtual, 5, main]            ← fetchUser() 작업
└── Thread[#58, virtual, 5, main]            ← fetchOrder() 작업
```

현재 JEP 505는 5번째 Preview 단계입니다. API가 안정화되고 있으며, 실제 프로덕션 코드에 도입하기 전에 향후 정식 채택 시 API 변경 가능성을 염두에 두어야 합니다.


---

<br />

---

## 키 파생 함수 API (Key Derivation Function API, 정식 채택) - JEP 510
키 파생 함수(KDF)는 하나의 비밀 키와 추가 데이터를 입력받아 새로운 암호화 키를 생성하는 암호학적 알고리즘입니다. 예를 들어 사용자 비밀번호 하나로 암호화 키, 서명 키, 인증 키를 각각 별도로 파생시키는 데 사용됩니다. JDK 25 이전까지는 표준 Java API가 없어 각 보안 라이브러리마다 제각각의 방식으로 구현했지만, JEP 510으로 javax.crypto 패키지에 표준 KDF API가 정식 채택되었습니다.


__HKDF 알고리즘으로 키 파생__
 
```java
KDF hkdf = KDF.getInstance("HKDF-SHA256");

AlgorithmParameterSpec params = HKDFParameterSpec
    .ofExtract()
    .addIKM(secretKey)       // 원본 비밀 키 (Input Key Material)
    .addSalt(salt)           // 솔트 값
    .thenExpand(info, 32);   // 파생할 키의 길이 (bytes)

SecretKey derivedKey = hkdf.deriveKey("AES", params);
```

---

<br />

---

## PEM 인코딩 (PEM Encodings of Cryptographic Objects, Preview) - JEP 470
PEM(Privacy-Enhanced Mail)은 암호화 키, 인증서, CSR 등을 텍스트로 표현하는 사실상의 표준 형식입니다. -----BEGIN PRIVATE KEY-----로 시작하는 그 형식입니다. 기존 Java는 PEM을 직접 다루는 표준 API가 없어 Base64 디코딩 후 수동으로 파싱하거나 BouncyCastle 같은 서드파티 라이브러리에 의존해야 했습니다. JEP 470은 이를 표준 API로 제공합니다. 현재는 Preview 단계입니다.

__PEM 문자열로부터 개인 키 읽기__
```java
String pemString = """
    -----BEGIN PRIVATE KEY-----
    MIIEvQIBADANBgkqhkiG9w0BAQEFAASC...
    -----END PRIVATE KEY-----
    """;

PEMDecoder decoder = PEMDecoder.of();
PrivateKey privateKey = (PrivateKey) decoder.decode(pemString);

// 반대로 키를 PEM 형식으로 인코딩
PEMEncoder encoder = PEMEncoder.of();
String encoded = encoder.encodeToString(privateKey);
```

---

<br />

---

## 컴팩트 객체 헤더 (Compact Object Headers, 정식 채택) - JEP 519
JVM에서 모든 Java 객체는 실제 데이터 외에 동기화 정보, 타입 정보, GC 메타데이터 등을 담는 객체 헤더를 가집니다. 기존 64비트 JVM에서 이 헤더 크기는 96~128비트였는데, JEP 519는 이를 64비트로 절반 가까이 축소합니다. 헤더 크기 자체는 작아 보이지만, Integer, String, Entry 같은 작은 객체가 수백만 개 존재하는 일반적인 Java 애플리케이션에서는 전체 힙 사용량이 눈에 띄게 줄어들고 CPU 캐시 효율도 함께 개선됩니다. JDK 24에서 실험적으로 도입된 후 JDK 25에서 별도의 JVM 플래그 없이 기본 동작하는 정식 기능으로 승격되었습니다.

---

<br />

---

## 세대별 Shenandoah GC (Generational Shenandoah, 정식 채택) - JEP 521
Shenandoah는 GC로 인한 Stop-The-World 일시 정지를 최소화하는 저지연 GC입니다. JEP 521은 여기에 세대별 수집(Generational Collection) 전략을 추가합니다. 대부분의 객체는 생성 직후 짧게 사용되고 버려진다는 약한 세대 가설(Weak Generational Hypothesis)에 기반해, 오래 살아남은 객체와 새로 생성된 객체를 분리하여 수집합니다. 새로 생성된 객체만 담긴 Young Generation을 자주 빠르게 수집하고, Old Generation은 덜 자주 수집함으로써 전체 GC 처리량과 지연 시간을 개선합니다. JDK 24의 실험적 도입을 거쳐 JDK 25에서 정식 채택되었습니다.

```bash
java -XX:+UseShenandoahGC -XX:ShenandoahGCMode=generational -jar app.jar
```

---

<br />

---

## AOT 클래스 로딩 & 링킹 캐시 (Ahead-of-Time Class Loading & Linking) - JEP 514
JVM은 애플리케이션을 시작할 때마다 클래스 파일을 디스크에서 읽고, 파싱하고, 링킹하는 과정을 반복합니다. JEP 514는 이 과정의 결과를 AOT 캐시 파일에 저장해두고, 다음 실행 시 재사용함으로써 시작 시간을 단축합니다. 특히 마이크로서비스나 서버리스 환경처럼 인스턴스를 자주 재시작하는 환경에서 효과가 큽니다.
bash# 1회차 실행: 종료 시 캐시 파일 생성
java -XX:AOTCacheOutput=app.aot -cp app.jar com.example.App

__이후 실행: 캐시를 활용해 빠른 시작__
```
java -XX:AOTCache=app.aot -cp app.jar com.example.App
```

---

<br />

---

## AOT 메서드 프로파일링 (Ahead-of-Time Method Profiling) - JEP 515
JIT 컴파일러는 런타임에 메서드 호출 빈도와 실행 패턴을 수집한 뒤, 충분한 프로파일이 쌓이면 비로소 최적화된 네이티브 코드를 생성합니다. 이 때문에 애플리케이션 시작 직후에는 최적화가 적용되지 않은 인터프리터 모드로 실행되는 워밍업(Warm-up) 구간이 존재합니다. JEP 515는 이전 실행에서 수집된 메서드 프로파일을 AOT 캐시에 함께 저장해, 다음 실행 시 JIT가 워밍업 없이 처음부터 최적화된 코드를 생성할 수 있도록 합니다. JEP 514가 클래스 로딩 시간을 줄인다면, JEP 515는 런타임 실행 성능의 초기 수준을 끌어올리는 역할을 합니다.

---

<br />

---


## JFR (JDK Flight Recorder) 관련
JDK Flight Recorder(JFR)는 JVM 내부의 이벤트를 낮은 오버헤드로 수집하는 프로파일링 및 진단 도구입니다. JDK 25에서는 세 가지 JEP를 통해 JFR의 정확도, 안정성, 기능을 각각 개선했습니다.

---

### JFR CPU 시간 프로파일링 (Experimental) - JEP 509
기존 JFR은 경과 시간(wall-clock time) 기반으로 샘플링했기 때문에, I/O 대기나 슬립 중인 스레드도 함께 측정되어 실제 CPU를 점유한 시간과 차이가 있었습니다. JEP 509는 Linux에서 실제 CPU 실행 시간만을 기준으로 샘플링하는 기능을 추가합니다. CPU를 많이 사용하는 메서드를 더 정확하게 찾아낼 수 있습니다. 현재 실험적(Experimental) 단계입니다.

---

### JFR 협력적 샘플링 (Cooperative Sampling) - JEP 518
기존 JFR의 스택 샘플링은 스레드가 어떤 상태에 있든 비동기적으로 강제 수행되었습니다. 이로 인해 간헐적으로 JVM이 불안정한 상태에서 스택을 읽으려다 오류가 발생하는 경우가 있었습니다. JEP 518은 스레드가 안전한 지점(safepoint)에 도달했을 때만 샘플링이 이루어지도록 협력적으로 설계를 바꿔 안정성을 높입니다.

---

### JFR 메서드 타이밍 & 트레이싱 - JEP 520
기존 JFR은 GC, 클래스 로딩, I/O 같은 JVM 수준의 이벤트는 잘 수집했지만, 애플리케이션 내 특정 메서드의 실행 시간을 측정하려면 별도의 APM 도구나 수동 계측이 필요했습니다. JEP 520은 바이트코드 계측(bytecode instrumentation)을 통해 JFR 자체에서 특정 메서드의 호출 횟수, 실행 시간, 인자 값 등을 추적할 수 있는 기능을 제공합니다.

---

<br />

---

## Vector API (10번째 인큐베이터) - JEP 508
Vector API는 SIMD(Single Instruction Multiple Data) 하드웨어 명령어를 Java 코드에서 직접 활용할 수 있게 해주는 API입니다. SIMD는 하나의 명령어로 여러 데이터를 동시에 처리하는 CPU 기능으로, 이미지 처리, 머신러닝 추론, 과학 계산처럼 대량의 수치 연산이 필요한 영역에서 일반 스칼라 연산 대비 수 배의 처리량을 낼 수 있습니다.


```java
for (int i = 0; i < a.length; i++) {
    c[i] = a[i] * b[i];
}

// Vector API: 여러 요소를 한 번에 처리 (SIMD 명령어로 컴파일됨)
static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;

for (int i = 0; i < a.length; i += SPECIES.length()) {
    FloatVector va = FloatVector.fromArray(SPECIES, a, i);
    FloatVector vb = FloatVector.fromArray(SPECIES, b, i);
    va.mul(vb).intoArray(c, i);
}
```

JDK 16부터 인큐베이터 단계를 유지하고 있으며, JDK 25가 10번째입니다. 정식 채택이 늦어지는 이유는 Project Valhalla에서 개발 중인 값 타입(Value Types)이 완성되어야 Vector API의 내부 구현을 그에 맞게 최적화한 뒤 Preview로 승격할 수 있기 때문입니다. API 자체는 안정적이나 정식 채택 전까지는 프로덕션 의존을 피하는 것이 권장됩니다.

---

<br />

---

# 참고
- [openjdk.org](https://openjdk.org/projects/jdk/25/)
- [JDK 25 — What’s New?](https://medium.com/@redouanekarzazi/jdk-25-whats-new-00542bfdf9cc)
- [[Java] Java 25 LTS 버전에 대해 알아보자](https://velog.io/@hsh111366/Java-Java-25-LTS-%EB%B2%84%EC%A0%84%EC%97%90-%EB%8C%80%ED%95%B4-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90)
- [광고 엔진 개발: JDK 25(LTS)](https://wiki.navercorp.com/spaces/SAPLATDEV3/pages/4141097756/JDK+25+LTS)

---

