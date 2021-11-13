<!-- 

완벽정리! Junit5로 예외 테스트하는 방법

-->

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/junit5_exception_test/img/cover.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

# 환경 구성
```text
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

```text
testRuntimeClasspath - Runtime classpath of source set 'test'.
+--- org.springframework.boot:spring-boot-starter-web -> 2.5.6
\--- org.springframework.boot:spring-boot-starter-test -> 2.5.6
     +--- org.junit.jupiter:junit-jupiter:5.7.2
```

<center> gradle 의존성 </center>
<br />

spring-boot-starter-web를 선택했다면 자동으로 spring-boot-starter-test 의존성이 추가되어있습니다. spring-boot-starter-test에 junit5가 기본적으로 있으니 바로 Junit5를 사용할 수 있습니다.

<br />

```text
testImplementation 'org.junit.jupiter:junit-jupiter-api'
```

Junit5만 추가하고 싶다면 위의 의존성을 추가하면 됩니다.

<br />

```java
public class DoSomething {
    public static void func() {
        throw new RuntimeException("some exception message...");
    }
}
```

RuntimeException이 발생하는 func 메소드를 만들었습니다. 그러면 func 메서드가 예외를 잘 발생하는지 테스트 코드를 4가지 방법으로 만들어보겠습니다.

<br />
<br />
<br />

# 방법 1. assertThrows

```java
import static org.junit.jupiter.api.Assertions.assertThrows;

@Test
public void junit5에서_exception_테스트_1() {
    Assertions.assertThrows(RuntimeException.class, () -> {
        DoSomething.func();
    });
}
```

<br />

Assertions.assertThrows의 두 번째 인자인 DoSomething.func()를 실행하여 첫 번째 인자인 예외 타입과 같은지(혹은 캐스팅이 가능한 상속 관계의 예외인지) 검사합니다. 

<br />
<br />
<br />

# 방법 2. assertj의 assertThatThrownBy

```text
testImplementation 'org.assertj:assertj-core:3.21.0' 
```

assertj를 사용하기 위해서 그래들에 위와같이 추가하면 됩니다. (최신버전을 보려면? [assertj mvn repository](https://mvnrepository.com/artifact/org.assertj/assertj-core))

<br />

Junit5 예외처리를 이야기하면서 테스트 코드 가족성을 assertj를 소개하는것이 주제에 벗어나는것으로 보일 수 있습니다. 그러나 assertj는 테스트 코드 가독성을 높여주기위해 Junit5와 사용합니다. (참고. [AssertJ 공식 문서](http://joel-costigliola.github.io/assertj/assertj-core.html))

<br />

```java
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Test
public void junit5에서_exception_테스트_2() {
    assertThatThrownBy(() -> DoSomething.func())
            .isInstanceOf(RuntimeException.class);
}
```

<br />

assertThatThrownBy에 예외 테스트를 원하는 코드를 작성, isInstanceOf에 발생하는 예외 타입(혹은 부모 타입의 예외)를 작성하여 테스트합니다.

<br />
<br />
<br />


# 방법 3. 예외 메시지 테스트 - assertEquals (try ~ catch)
```java
import static org.junit.jupiter.api.Assertions.assertThrows;

@Test
public void junit5에서_exception_테스트_3() {
    try {
        DoSomething.func();
    } catch (RuntimeException e) {
        Assertions.assertEquals("some exception message...", e.getMessage());
    }
}
```

<br />

try ~ catch 문을 사용하여 예외 메시지를 테스트할 수 있습니다. 예외 메시지는 변하기 쉬운 값이기에 테스트하면 깨지기 쉬운 테스트코드가 발생하므로 테스트를 안하는 것이 좋습니다. 다만 코드형 응답 메시지의 경우 테스트할 수 있습니다.

<br />

그러나 위의 코드는 전통적인 try ~ catch 문의 관념상 테스트코드가 무엇을 테스트하는지 명확하게 보이지 않을 수 있습니다. 그렇기에 방법 4를 제안합니다.

<br />
<br />
<br />

# 방법 4. 예외 메시지 테스트 - assertThrows 반환값 사용
```java
@Test
public void junit5에서_exception_테스트_4() {
    Throwable exception = assertThrows(RuntimeException.class, () -> {
        DoSomething.func();
    });
    assertEquals("some exception message...", exception.getMessage());
}
```
<br />

방법 1에서 봤던 assertThrows는 발생하는 예외를 모든 예외 클래스의 선조 클래스인 Throwable 타입으로 반환합니다. Throwable으로 반환된 메서드에서 발생한 예외 객체의 메시지를 통하여 예외 메시지 테스트할 수 있습니다.

<br />
<br />
<br />

# 좀 더 깊은 이야기

<br />

## assertThrows 예외 타입 체크 비밀

<br />

위에서 assertThrows는 인자로 넘어온 예외 타입 혹은 부모 타입의 예외를 상속한 예외인지 검사한다고 하였는데 어떤 의미인지 살펴보겠습니다.


```java
public class SomeException extends RuntimeException {

    public SomeException(String message) {
        super(message);
    }
}
```
RuntimeException을 상속받은 SomeException을 만들었습니다.

<br />

```java
@Test
public void junit5에서_exception_테스트_1_2() {
    Assertions.assertThrows(RuntimeException.class, () -> {
        DoSomething.func2();
    });
}
```

그리고 assertThrows를 사용하여 RuntimeException 타입인지 테스트를 합니다. 그러면 이 테스트는 통과할까요? 위에서 설명한 대로 부모 예외를 상속한 경우이기에 정답은 테스트를 통과합니다. 테스트를 통과하는 이유는 Assertions.assertThrows 내부 구현에 비밀이 있습니다.

<br />

```java
private static <T extends Throwable> T assertThrows(
    Class<T> expectedType, Executable executable, Object messageOrSupplier) {

    try {
        executable.execute();
    }
    catch (Throwable actualException) {
        if (expectedType.isInstance(actualException)) {
            return (T) actualException;
        }

        // 후략
```
<center> org.junit.jupiter.api.AssertThrow.java </center>

<br />

executable인자로 넘어온 DoSomething.func2()를 실행합니다. SomeException이 발생하면 isInstance를 검사하게 되며 이 조건문은 참이되기에 return (T) actualException; 이 실행됩니다. 발생한 예외와 기대하는 예외 타입 검사를 isInstance로 검사하기에 기대하는 예외 타입을 상속받은 예외라면 테스트를 통과할 수 있습니다.

<br />
<br />
<br />

## 예외 테스트를 꼭 해야하는가?

```java
public class TestCsv extends TestDb {

    private void testOptions() {
        // 생략 

        assertThrows(ErrorCode.FEATURE_NOT_SUPPORTED_1, csv)
                            .setOptions("escape=a error=b");
    }
}
```

<center>
h2.test.db.TestCsv.java <br />
(h2는 assertThrows는 Junit5가 아닌 자체 AssertThrows를 사용합니다.)
</center>
<br />

위 코드는 스프링으로 웹 서비스를 만들면 필수인 h2의 CSV 테스트 코드입니다. 사실 실패 테스트를 하지 않고 유닛테스트를 구성할 경우 통합 테스트에서(속칭 서버 올리고 UI 클릭하며 진행하는 테스트) 시간을 많이 소요됩니다. 

<br />

만일 회원가입이라고 한다면

<br />

- 메일을 보낼 수 없는 인증 메일을 입력한 경우
- 이메일 인증하지 않고 재가입 시도하는 경우
- 유효기간이 만료된 인증 토큰을 사용하는 경우
- 내부에서의 잘못된 암호화 키값으로 사용자의 비밀번호를 암호화하는 경우
- 등등..

<br />

이 모든 테스트를 통합 테스트에서 테스트하려면 정말 힘들것입니다. 예외에 대한 단위 테스트는 애플리케이션에 대해 빠르게 견고하게 만들어 줄 것입니다.

<br />
<br />
<br />

# 마치며

<br />

본 글에서 사용한 플레이그라운드 코드는 [Github. Tistory-Covenant-Code](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-junit5-exception)에서 확인할 수 있습니다.

<br />
<br />
<br />

