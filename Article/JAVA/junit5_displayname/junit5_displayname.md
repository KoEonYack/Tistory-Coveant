<!-- 

Junit5 테스트 이름을 지정하는 4가지 방법 (DisplayName, DisplayNameGeneration, Named)

-->

<br />

# 시작하며 

Junit5에서는 다양한 방법으로 클래스와 테스트 메서드에 이름을 지정할 수 있습니다. 본 글에서는 DisplayName 뿐만 아니라 이 글을 작성하는 기준 Junit5 최신 버전인 5.8 버전에서 추가된 테스트 이름을 지정하는 방법을 살펴보겠습니다.

<br />
<br />
<br />

# 예제 코드

```java
public static String emailMasking(String email){
    int maxVisibleLength = 2;
    email = StringUtils.trim(email);
    String localPart = StringUtils.split(email, "@")[0];
    String emailDomain = StringUtils.split(email, "@")[1];
    return localPart.substring(0, localPart.length() - maxVisibleLength) + "**@" + emailDomain;
}
```

살짝 복잡해 보이는 정적 메서드 하나가 있습니다. 이메일 마스킹하는 메서드로 인자로 apple@naver.com이 넘어오면 로컬 파트(Local Part)인 apple을 app**로 마스킹하는 코드입니다. 

<br />

동작은 간단하나 쫌 복잡해 보이기에 잘 동작하는지 테스트 코드를 작성해봅시다.

<br />
<br />
<br />

# 방법 1. DisplayName

```java
@DisplayName("이메일 마스킹 테스트")
class EmailMaskingBasicTest {

    @Test
    @DisplayName("로컬 파트 5자리 마스킹 테스트")
    void localpart_5_length_masking() {
        String email = "apple@apple.com";

        String maskingEmail = MaskingUtils.emailMasking(email);

        Assertions.assertEquals("app**@apple.com", maskingEmail);
    }
}
```

<br />
<img src="./img/way1_default_res.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />
<center>
Junit5의 테스트 결과
</center>
<br />
<img src="./img/way1_res.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />
<center>
@DisplayName을 지정한 테스트 결과
</center>
<br />

Junit5에서는 기본적으로 클래스명, 메서드명 그대로 테스트 이름을 보여줍니다. camelCase 혹은 snake_case 테스트 결과가 한눈에 들어오지는 않습니다.

<br />

@DisplayName을 이용하면 테스트 클래스 혹은 테스트 메서드의 이름을 지정할 수 있습니다.

<br />
<br />
<br />

# 방법 2. DisplayNameGeneration

```java
@DisplayName("이메일 마스킹")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EmailMaskingDisplayNameSimpleGeneratorTest {

    @Test
    void 로컬_파트_5자리_마스킹_테스트() {
        String email = "apple@apple.com";

        String maskingEmail = MaskingUtils.emailMasking(email);

        Assertions.assertEquals("app**@apple.com", maskingEmail);
    }

    @Test
    void 공백을_포함한_로컬_파트_5자리_마스킹_테스트() {
        String email = " apple@apple.com";

        String maskingEmail = MaskingUtils.emailMasking(email);

        Assertions.assertEquals("app**@apple.com", maskingEmail);
    }
}
```

<br />
<img src="./img/way2_res.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />

@DisplayNameGeneration을 사용하여 테스트 메서드 이름을 변경할 수 있습니다. DisplayNameGenerator.ReplaceUnderscores.class은 테스트 메서드의 _를 공백으로 변경하여 테스트 메서드 이름으로 보여줍니다.

<br />
<br />
<br />

# 방법 3. DisplayNameGeneration 커스텀

```java
@DisplayNameGeneration(
    EmailMaskingDisplayNameCustomGeneratorTest.CustomDisplayNameGenerator.class
)
class EmailMaskingDisplayNameCustomGeneratorTest {

    @Test
    void localpart_5_length_masking() {
        String email = "apple@apple.com";

        String maskingEmail = MaskingUtils.emailMasking(email);

        Assertions.assertEquals("app**@apple.com", maskingEmail);
    }

    static class CustomDisplayNameGenerator extends DisplayNameGenerator.Standard {

        @Override
        public String generateDisplayNameForClass(Class<?> testClass) {
            return "이메일 마스킹 테스트";
        }

        @Override
        public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
            return super.generateDisplayNameForNestedClass(nestedClass);
        }

        @Override
        public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
            String name = testMethod.getName();
            return Arrays.stream(name.split("_")).collect(Collectors.joining(" "));
        }
    }
}
```

<br />
<img src="./img/way3_res.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />

DisplayNameGeneration.Standard를 상속하여 원하는 대로 테스트 이름을 변경할 수 있습니다. 방법 2에서 봤던 것과 다르게 클레스에 @DisplayName이 사라졌습니다. generateDisplayNameForClass에 지정한 클래스 이름이 표시됩니다.

<br />

generateDisplayNameForMethod는 테스트 메서드의 _을 공백으로 바꾸어줍니다.

<br />
<br />
<br />

# 방법 4. ParameterizedTest
```java
private static Stream<Arguments> emailMaskingParam() {
    return Stream.of(
            arguments("apple@apple.com", "app**@apple.com")
    );
}

@ParameterizedTest(name = "이메일주소 {0}은 {1}로 변환")
@MethodSource("emailMaskingParam")
void email_param_masking_test(String email, String expectMaskingEmail) {
    String maskingEmail = MaskingUtils.emailMasking(email);

    Assertions.assertEquals(expectMaskingEmail, maskingEmail);
}
```

<br />
<img src="./img/way4_res.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />

@ParameterizedTest에 표현 식을 지정할 수 있습니다. {0}, {1}은 파라미터값을 테스트 이름에 넣을 수 있습니다.

<br />

이외에도 다양한 표현식을 테스트 이름에 지정할 수 있습니다.

- {displayName}: 메서드 이름을 표시합니다.
- {index}: 테스트 실행 횟수를 표시합니다. (1-based)
- {arguments}: 파라미터를 콤마로 구분해서 보여줍니다.
- {argumentsWithNames}: 콤마로 구분된 파라미터 이름과 파리머터 값을 보여줍니다.
- {0}, {1} ...: 메서드에 인자로 넘어오는 각 파라미터를 표시합니다. 

<br />
<br />
<br />

# 방법 5. Junit 5.8에서 추가된 Named.named

```java
import static org.junit.jupiter.api.Named.named;

@DisplayName("이메일 마스킹 테스트")
class EmailMaskingParamTest {

    private static Stream<Arguments> emailMaskingParam() {
        return Stream.of(
                arguments(named("로컬 파트의 길이 5는", "apple@apple.com")
                        , named("로컬파트 끝 두자리 마스킹", "app**@apple.com")),
                arguments(named("공백으로 시작하는 로컬 파트의 길이 5는", " apple@apple.com")
                        , named("로컬파트 끝 두자리 마스킹", "app**@apple.com"))
        );
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("emailMaskingParam")
    @DisplayName("이메일 마스킹 테스트")
    void email_param_masking_test(String email, String expectMaskingEmail) {
        String maskingEmail = MaskingUtils.emailMasking(email);

        Assertions.assertEquals(expectMaskingEmail, maskingEmail);
    }
}
```

<br />
<img src="./img/way5_res.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />

Named에 대한 제안은 어떤 개발자의 [Github Junit5 issue](https://github.com/junit-team/junit5/issues/2301) 부터 시작합니다. 
Junit5에서 추가된 Named.named를 사용하면 파라미터에 이름을 지정할 수 있습니다. 

<br />

이게 왜 필요한가 생각이 들 수 있지만, 생각보다 개발을 하다 보면 테스트하는 파라미터에 상세한 코멘트를 추가하고 싶을 때가 있습니다. 특히 Mocking 값을 파라미터값으로 넘겨준다면 어떤 Mocking인지 상술하면 더욱더 명확한 테스트가 될 것입니다.

<br />
<br />
<br />

# 참고

<br />

본 글에서 다룬 코드는 제 저장소 [Github](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-junit5-displayname)에서 전체 코드를 볼 수 있습니다.

<br />

- [Junit5 docs](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests)
- [Github Junit5 issue](https://github.com/junit-team/junit5/issues/2301)

<br />
<br />
<br />
