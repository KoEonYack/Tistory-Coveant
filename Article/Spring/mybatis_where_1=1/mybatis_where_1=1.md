<!-- 

Mybatis WHERE 1=1을 trim으로 변환하면 해결될까?

-->

<br />
<br />
<br />

# 시작하며

<br />

회사 코드 중에 마이바티스 동적 쿼리로 WHERE 1=1 코드를 보았습니다. 

```sql
SELECT id, username, nickname
FROM member_test
WHERE
    1=1
    <if test="username != null">
        AND username = #{username}
    </if>
    <if test="nickname != null">
        AND nickname = #{nickname}
    </if>
```

<center> SELECT WHERE 1=1 예시 </center>

<br />

위의 예시 쿼리는 생각해볼 부분이 있습니다. username, nickname이 모두 null이 넘어오면 전체 조회가 발생합니다. __두 인자가 null이 넘어왔을 때 데이터 전체 조회가 된다는 사실을 의도하지 않거나 예상하지 못한다면 데이터에 따라서 애플리케이션 응답 지연 문제 등 발생할 수 있습니다.__

<br />

```sql
DELETE FROM member_test
WHERE
    1=1
    <if test="username != null">
        AND username = #{username}
    </if>
    <if test="nickname != null">
        AND nickname = #{nickname}
    </if>
```

<center> DELETE WHERE 1=1 예시 </center>

<br />

SELECT에서 전체조회가 발생하는 것은 그렇다 하더라도 DELETE(혹은 UPDATE) 쿼리에서 username, nickname 전부 null이 넘어온다면 전체 삭제가 발생합니다. 전체 쿼리가 수행되는 WHERE 1=1 혹은 이에 따르는 쿼리를 피하는 것이 좋습니다.

<br />

검색했을 때 WHERE 1=1은 지양하자. 그리고 WHERE 1=1은 trim을 사용하자는 글을 보았습니다. __물론 trim을 사용하면 WHERE 1=1을 피할 수 있지만, 여전히 null 값이 넘어오면 데이터 전체조회가 되기에 궁극적인 답은 되지 못합니다.__ 본 글에서 마이바티스 동적쿼리에서 WHERE 1=1을 사용하지 않고 어떻게 검증할까 고민해보겠습니다.

<br />

본 예제에서 사용한 플레이그라운드 코드는 [Github Tistory-Covenant-Code](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-mybatis-trim)에서 보실 수 있습니다.

<br />
<br />
<br />

# WHERE 1=1을 피하는 방법

<br />

## 방법1. where 사용

```sql
SELECT id, username, nickname
FROM member
<where>
    <if test="username != null">AND username = #{username} </if>
    <if test="nickname != null">AND nickname = #{nickname} </if>
</where>
```

<center> WHERE 1=1대신 <where> 사용 </center>

<br />

WHERE 테그를 사용하면 WHERE 1=1을 사용하지 않을 수 있습니다. 

<br />

다만, 검증 로직이 부재로 username, nickname 전부 null이 넘어오면 어떻게 될까요?

```sql
SELECT id, username, nickname
FROM member_test
```
<center> log4jdbc로 본 실행 쿼리 </center>

<br />

WHERE 1=1과 마찬가지로 전체 조회 쿼리가 나갑니다.

<br />
<br />
<br />

## 방법2. trim 사용 (1)

```sql
SELECT id, username, nickname
FROM member
<trim prefix="WHERE" prefixOverrides="AND">
    <if test="username != null">
        AND username = #{username}
    </if>
    <if test="nickname != null">
        AND nickname = #{nickname}
    </if>
</trim>
```

<center> WHERE 1=1대신 trim 사용 </center>

<br />

많은 블로그에서 WHERE 1=1을 피하고자 제안하는 방식입니다. trim을 사용하면 태그 내부에서 실행할 쿼리를 생성해줍니다. `prefix=WHERE`은 해당 블록 안의 쿼리에 WHERE 쿼리를 추가해줍니다. 

<br />

`prefixOverrides` <trim>태그 안에서 실행될 쿼리의 가장 앞 쿼리가 속성값에 설쟁해둔 문자와 동일할 경우 문자를 제거해줍니다. 즉. FROM member WHERE __AND__ username = #{username} 이런 요상한 쿼리가 생기지 않도록 WHERE 다음에 __AND__ 를 제거해줍니다.

<br />

그런데 username, nickname 전부 null이면 어떻게 될까요?

```sql
SELECT id, username, nickname
FROM member_test
```
<center> log4jdbc로 본 실행 쿼리 </center>

<br />

WHERE 1=1과 마찬가지로 전체 조회 쿼리가 나갑니다. WHERE 1=1은 사용하지 않을 수 있지만, 여전히 인자값 검증을 하지 않는다면 데이터 전체 조회가 발생할 가능성을 제거하지 못하였습니다.

<br />
<br />
<br />

## 방법3. trim 사용 (2)

```sql
SELECT id, username, nickname
FROM member_test
WHERE
<trim prefixOverrides="AND">
    <if test="username != null">
        AND username = #{username}
    </if>
    <if test="nickname != null">
        AND nickname = #{nickname}
    </if>
</trim>
```

방법 2과 유일한 차이는 `<trim prefix="WHERE"`을 제거하였습니다. 방법 1과 동일하게 동작할 것 같지만 username, nickname 전부 null로 넘어오면 큰 차이가 생깁니다.

<br />

```sql
SELECT id, username, nickname
FROM member_test WHERE
```
<center> log4jdbc로 본 실행 쿼리 </center>

<br />

WHERE 다음에 어떤 조건이 걸리지 않기 때문에 __BadSqlGrammarException__ 이 발생합니다. 검증 로직이 부족하여 username, nickname 전부 null로 넘어온다면 Exception이 발생하기에 데이터 전체 조회가 일어나는 것을 막을 수 있습니다.

<br />
<br />
<br />

# 문제는 어디서 검증할까?

<br />

trim을 쓰더라도 문제는 쿼리의 복잡성이 증가한다는 것입니다. 마이바티스 운영 동적쿼리를 작성하면 5중 if 등장은 예사가 아닐 것입니다. 그렇다면 동적 쿼리상태에서 데이터 전체 영향을 주지 않도록 null로 넘어오는 값을 잘 처리해야 하는데 이것을 마이바티스에서 하는 게 맞을까 고민할 시기가 오는 것입니다.

<br />
<br />
<br />

## MVC에서의 데이터 검증

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/mybatis_where_1%3D1/img/layer.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

Spring MVC 패턴에서 회원가입 요청이 넘어왔다고 가정하고 각 레이어별 데이터 검증을 살펴보겠습니다. 각 레이어별 검증은 다양하기에 하나의 예시라고 보면 됩니다.

<br />

- ❶ 검증: 회원가입시 입력 폼에 입력한 값 중에서 필수 필드가 누락되었는지(예. 아이디가 최소 4글자 이상인지 비밀번호를 누락해서 보냈는지) 검증
- ❷ 검증: 영속성 레이어 조회를 통해 중복된 닉네임인지, 블락된 아이디로 가입 시도하는지 등등 검증
- ❸ 검증: 영속성 레이어에 보낼 수 없는 값을 보내는지 검증

<br />

MVC 검증관점에서 보았을 때 null 인자가 넘어가는 것은 __❸ 검증__ 에 추가할 수 있습니다. 그렇게 하면 if가 줄어드는 것은 덤이고 null 인자 처리를 쿼리에서 고민을 덜 수 있습니다. 

<br />

(사족: 값 응답시 null은 피해야합니다. 참고. [자바에서 null을 안전히 다루는 방법](https://youtu.be/vX3yY_36Sk4))

<br />
<br />
<br />

## 쿼리에 조건을 넣어야할때

<br />

그럼에도 마이바티스로 배치형, 운영형 동적 쿼리 생성시 잘못된 값을 검증하기 위해 속칭 if를 덕지덕지 붙여야 할 때가 있다고 생각합니다. 100줄이 넘어가는 운영 형 쿼리는 같은 역할을 하면 복사하다가 다른 프로젝트에서 사용하는 경우가 있습니다.

<br />

기나긴 비즈니스 로직에서 검증 로직을 완벽하게 옮기지 않는 경우 발생하는 사이드이팩트를 막기 위해 동적쿼리 조건을 빡빡하게 하여 잘못된 값이 넘어왔을 때를 방지해야 한다고 생각합니다.

<br />
<br />
<br />

# 마치며

<br />

WHERE 1=1 쿼리를 사용하실 전체 데이터를 대상으로 동작할 수 있다는 위험성과 어떻게 해결할지를 보았습니다. 어떻게, 어디까지 검증할지는 개발자 여러분의 몫입니다.

<br />

trim에 대해 정확하게 이해하고 사용하고 싶다면 강아지 코딩공부님의 [mybatis trim 태그를 prefixoverrides를 이용해서 잘 써먹어 봅시다.](https://codingdog.tistory.com/563) 글을 추천합니다.

<br />
<br />
<br />
<br />
<br />
