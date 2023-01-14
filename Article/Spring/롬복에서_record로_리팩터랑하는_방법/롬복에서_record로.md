<!-- 

롬복에서 record로 리팩터랑하는 방법

-->

<br />
<img src="./img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

## 시작하며

<br />

반복적인 코드를 줄이기 위해서 롬복을 사용하여 Getter, Setter 등 사용하였을 것입니다. 그러나 JDK 14부터 등장한 record를 사용하면 롬복을 사용하지 않고 조금 더 코드를 간결하게 만들 수 있습니다.

<br />

본 글은 기존에 롬복을 사용한 클래스를 롬복을 제거하고 단계별로 리팩터링하여 record로 전환해보겠습니다.

<br />
<br />
<br />


## 현재 상황
```java
@Data
@AllArgsConstructor
public class Member {
   private String username;
   private String nickname;
}
```

<br />

Data 에노테이션을 사용한 코드가 비즈니스 로직에 퍼져있다고 가정해보겠습니다. 이 Member 클래스를 record로 리팩터링을 단계별로 진행해보겠습니다.

<br />
<br />
<br />


## STEP 1. 테스트 코드 생성
```java
@Test
void memberTest() {
    Member member = new Member("john", "covenant");
    member.setUsername("apple");

    assertEquals("apple", member.getUsername());
    assertEquals(member, new Member("apple", "covenant"));
    assertEquals("covenant", member.getNickname());
}
```

<br />

리팩터링을 잘하는 방법으로 리팩터링을 진행하기 전 리팩터링 대상의 코드에 테스트코드를 잘 작성하는 것입니다. 그리고 단계별로 리팩터링 과정을 거칠 때마다 테스트를 실행하며 리팩터링 전과 후가 동일한 기능을 수행하는지 파악해야 합니다.

<br />

실습을 위해서 테스트코드를 간단하게 작성해봤습니다.

<br />
<br />
<br />


## STEP 2. 롬복 제거

<br />
<img src="./img/delombok.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

롬복 제거에 인텔리제이의 도움을 받을 것입니다. 오른쪽 마우스 -> Refactor -> Delombok -> All lombok annotations를 클릭하면 롬복이 제거되고, POJO 형식의 getter, setter, equals, canEqual, hashCode, toString 메서드가 생성됩니다. 

<br />

```java
public class Member {
    private String username;
    private String nickname;

    public Member(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // 후략 ...
}
```

<br />

롬복 제거하기 전과 후 문제가 없는지 앞서 작성한 테스트를 돌려서 테스트가 통과하는지 확인합니다.

<br />
<br />
<br />


## STEP 3. Getter 메서드 이름 변경

<br />

record를 사용하면 필드에 접근하기 위해서 Getter 메서드를 사용할 필요 없이 필드명으로 필드에 접근할 수 있습니다. 이러한 record 특징을 이용해서 Getter 메서드를 필드 이름으로 변경할 것입니다.

<br />

인텔리제이에서는 Shift + F6를 이용해서 메서드 이름을 변경할 수 있습니다. 

<br />

```java
public class Member {
    private String username;
    private String nickname;

    public Member(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public String username() { // 변경!
        return this.username;
    }

    public String nickname() { // 변경!
        return this.nickname;
    }

    // ...
}

@Test
void createMemberTest() {
    Member member = new Member("john", "covenant");
    member.setUsername("apple");


    assertEquals("apple", member.username()); // 변경 확인!
    assertEquals(member, new Member("apple", "covenant"));
    assertEquals("covenant", member.nickname()); // 변경 확인!
}
```

<br />

리팩터링이 되었기에 다시 테스트코드를 실행하여 잘못 변경된 리팩터링이 잘 되었는지 확인합니다.

<br />
<br />
<br />


## STEP 4. 사용하지 않는 Setter 제거

<br />
<img src="./img/remove_setter.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

다음 스텝에서 진행할 Setter 리팩터링은 조금 신경을 써야 합니다. 그렇기에 프로젝트에서 사용하지 않은 Setter는 인텔리제이의 도움을 받아서 제거해보겠습니다.

<br />

코드상에서 어둡게 회색 음영 처리된 부분은 프로젝트 상에서 사용되지 않은 코드입니다. 음영 처리된 메서드 이름에 커서를 놓고 Option + Enter(Alt + Enter)를 클릭하여 Safe Delete를 선택하여 Setter 메서드를 제거합니다.

<br />
<br />
<br />

## STEP 5. Setter를 Withers로 변경

<br />

사용하는 Setter를 변경하려고합니다. record는 불변(immutable) 데이터 객체이므로 setter를 사용하는 곳에서 새롭게 객체를 만들어주어야 합니다.

<br />

지금까지는 인텔리제이의 도움으로 수월하게 래팩터링을 진행하였습니다. 이 단계부터는 인텔리제이의 도움을 받을 수 없습니다.

<br />

__1) 메서드 이름 변경:__ setUsername 이름을 변경해줍니다. 저는 여기서 withUsername으로 변경하였습니다. 변경의 의미를 명확하게 담고 싶다면 changeUsername으로 변경해도 됩니다.

```java
public void withUsername(String username) {
    this.username = username;
}
```

<br />
<br />

__2) 생성자로 객체 생성:__ 불변 객체가 되어야하므로 데이터 변경이 일어나면 새로운 객체를 만들어줄 것입니다. 생성자를 통하여 객체를 만들어 줍니다.

```java
public Member withUsername(String username) {
    return new Member(username, nickname);
}
```

<br />

__3) 테스트 코드 수정:__ 값 변경이 일어나면 객체를 다시 대입(assign)해야 합니다. 새롭게 생성된 객체를 다시 대입합니다.

```java
Member member = new Member("john", "covenant");
member = member.withUsername("apple");
```

<br />

테스트코드를 실행해서 리팩터링이 잘 진행되었는지 확인합니다.

<br />
<br />
<br />


## STEP 6. final로 필드 변경

<br />
<img src="./img/final.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

이제 불변 필드로 변경할 수 있습니다. Option + Enter(Alt + Enter)를 누르면 final 필드를 만들 수 있다고 제안해줍니다. final로 변경해줍니다.

<br />

```java
public class Member {
    private final String username;
    private final String nickname;

    // ...
```

<br />
<br />
<br />

## STEP 7. record로 변경

<br />

이제 클래스를 record로 변경할 수 있는 모든 조건을 만들었습니다. 클래스 이름에 밑줄로 record로 변경할 수 있다고 추천을 해줄 것입니다.

<br />
<img src="./img/record.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

Option + Enter(Alt + Enter)를 누르면 record로 변경할 것을 제안해줍니다. record로 변경해줍니다.

<br />

```java
public record Member(String username, String nickname) {
    // ...
}
```

<br />
<br />
<br />

## STEP 8. 필요 없는 코드 제거 

<br />

record를 사용하기에 사용하지 않는 equals, canEqual, hashCode, toString을 제거합니다. 

<br />

롬복을 제거하기 전 코드와 record 코드를 비교함으로 본 글을 마칩니다. <><.


<br />
<br />

```java
// Before
@Data
@AllArgsConstructor
public class Member {
   private String username;
   private String nickname;
}

// After
public record Member(String username, String nickname) {
    public Member withUsername(String username) {
        return new Member(username, nickname);
    }
}
```

<!-- 
## 참고
-->

<br />
<br />
<br />