<!-- 
SARGable 한 번 이해하면, 쿼리 튜닝이 쉬워진다
-->

<br />
<img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

## 시작하며 

<br />

```sql
WHERE DATE(created_at) = '2025-12-17'
```

<br />

위의 쿼리의 문제가 바로 보이시나요?

<br />

쿼리를 DBA에게 검수받으신다면 이런 피드백을 받을때가 있습니다.

<br />

- WHERE 절에서 컬럼에 함수를 씌우면 인덱스를 잘 못 탄다
- (user_id) 단일 인덱스 대신 (user_id, created_at) 같은 복합 인덱스가 더 맞다
- 날짜 조건은 DATE(created_at) 대신 범위 조건으로 바꿔라

```sql
created_at >= ‘2025-12-17’ AND created_at < ‘2025-12-18’
```

즉, 위와 같은 쿼리로 수정해야 합니다.

<br />

이 피드백의 공통 키워드가 SARGable(Search ARGument ABLE: 사저블)입니다. 이 글에서는 SARGable을 쉽게 설명해보겠고, MySQL에서 SARGable/Non-SARGable이 실행 계획과 성능에 어떤 영향을 주는지 예시로 정리합니다.

<br />
<br />
<br />

## 쉽게 풀어본 SARGable

<br />

초등학교에서 친구들이 명단이 키 순서대로 정렬되어 있다고 해봅시다. 이것이 인덱스입니다.

<br />

__SARGable한 쿼리__ 는 키가 140cm 이상 150cm 미만인 친구만 찾아줘 입니다.
명단이 키 순서대로 정렬돼 있으니까, 140cm부터 150cm까지 구간만 딱 펼쳐서 보면 됩니다. 즉, 범위를 잘라서 빨리 찾을 수 있습니다.,

<br />

__SARGable하지 않은 쿼리__ 는 (키를 10으로 나눈 몫이) 14인 친구만 찾아줘입니다.
명단은 키(140, 141, 142cm…)로 정렬돼 있는데, 키를 10으로 나눈 값(14, 14, 14…)”은 친구마다 계산해봐야 알 수 있습니다. 그래서 한 명씩 전부 계산해보게 되기에 수행 결과가 느려집니다.

<br />

SARGable은 __DB가 인덱스를 이용해서 처음부터 후보 범위를 좁게 잘라 들어갈 수 있는 WHERE 조건__ 을 말합니다.
Non-SARGable은 __DB가 인덱스로 범위를 잘라 들어가기 어렵고, 결국 많은 데이터를 읽어놓고(혹은 인덱스를 훑어놓고) 나중에 조건을 계산/필터링해야 하는 WHERE 조건__ 입니다.

<br />
<br />
<br />

## SARGable 쿼리

<br />

SARGable은 Search ARGument ABLE의 줄임말로, WHERE 조건이 인덱스를 활용해 빠르게 레코드를 찾거나, 읽어야 할 범위를 크게 줄일 수 있는 형태를 의미합니다. 핵심은 조건이 “컬럼 값을 그대로 비교하는 방식”으로 작성되어, 인덱스의 정렬 구조(B-Tree)를 그대로 활용할 수 있느냐입니다.

<br />

예를 들어 age 컬럼에 인덱스가 있을 때,

<br />

```sql
WHERE age = 25
```

<br />

컬럼 자체를 상수와 직접 비교하면, 데이터베이스는 인덱스에서 해당 값이 있는 위치로 바로 접근해 필요한 행만 효율적으로 조회할 수 있습니다.

<br />
<br />
<br />

## Non-SARGable 쿼리

<br />

Non-SARGable은 SARGable과 반대로, WHERE 조건이 인덱스를 충분히 활용하기 어려운 형태를 말합니다. 대표적인 패턴은 검색 대상 컬럼에 함수나 연산을 적용하는 경우입니다. 이 경우 DB는 인덱스의 정렬 구조를 그대로 사용해 범위를 자르기가 어려워지고, 결과적으로 더 많은 데이터를 읽은 뒤 조건을 계산해 필터링하는 방식으로 흐르기 쉽습니다.

<br />

```sql
WHERE YEAR(date_of_birth) = 1990
```

<br />
<br />
<br />

## 사례로 살펴보기

<br />

아래와 같이 테이블이 있습니다.

```sql
CREATE TABLE activity_logs (
  id BIGINT NOT NULL AUTO_INCREMENT,
  member_id VARCHAR(32) NOT NULL,
  action VARCHAR(50) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_member_id_created_at (member_id, created_at)
) ENGINE=InnoDB;
```

<br />

이 예시는 idx_member_id_created_at (member_id, created_at) 복합 인덱스가 존재한다는 전제에서, DATE(created_at)처럼 컬럼을 가공했을 때 created_at 범위 스캔이 깨질 수 있는 상황을 보여줍니다.

<br />

__Non-SARGable 예시: DATE(created_at) =__

```sql
DELETE FROM activity_logs
WHERE DATE(created_at) = :targetDate
  AND member_id = :memberId;
```

<br />

이 쿼리는 사용자 로그가 많으면, 사용자 로그를 많이 읽고 나서 DATE(created_at)를 하나씩 계산해 필터링하는 형태가 됩니다.

<br />

__SARGable 예시: 날짜를 범위 조건으로 바꾸기__

```sql
DELETE FROM activity_logs
WHERE member_id = :memberId
  AND created_at >= :startAt
  AND created_at <  :endAt;
```
<br />

예를 들어 2025-12-17에 해당하는 member_id가 admin인 사용자의 created_at 값을 삭제하고 싶다면 아래와 같이 쿼리를 작성합니다.

```sql
DELETE FROM activity_logs
WHERE member_id = 'admin'
  AND created_at >= '2025-12-17 00:00:00'
  AND created_at <  '2025-12-18 00:00:00';
```

<br />

이 형태는 인덱스 정렬 구조를 그대로 활용해 범위를 좁힐 수 있어 SARGable합니다.

<br />
<br />
<br />

## SARGable을 고려한 실무 적용 가이드

<br />

### 1. WHERE 절에서 인덱스 컬럼에 함수/계산을 적용하지 말 것

<br />

인덱스는 컬럼의 원본 값으로 정렬되어 있습니다. 그런데 WHERE에서 컬럼을 DATE(), YEAR(), LOWER()처럼 가공하면, DB가 인덱스 정렬을 그대로 이용해 __여기부터 여기까지 범위를 자르기 어려워집니다.__(즉, DATE(created_at)처럼 컬럼을 가공하면 인덱스의 정렬을 그대로 활용한 range scan(start/stop key)이 깨질 수 있습니다.)

<br />

위에서 관련 쿼리를 봤지만 다른 예시 하나 더 보겠습니다.

<br />

```sql
SELECT * FROM users
WHERE YEAR(birth_at) = 2025;
```

<br />

위와같이 YEAR를 사용하는 것이 아니라 아래와 같이 범위 >=, <를 이용합니다.

<br />

```sql
SELECT * FROM users
WHERE birth_at >= '2025-01-01'
  AND birth_at <  '2026-01-01';
```

<br />
<br />
<br />

### 2. LIKE는 앞부분 고정만 인덱스에 친화적입니다.

<br />

__SARGable__

```sql
WHERE username LIKE 'kim%'
```

앞 부분을 고정한 LIKE라면 SARGable합니다.

<br />

__Non-SARGable__

```sql
WHERE username LIKE '%kim%'
```

<br />

위 LIKE 쿼리는 포함검색이라 B-Tree 인덱스로는 시작 지점을 잡기 어렵습니다. 이런 요구가 핵심 기능이면 Fulltext, 검색엔진 혹은 별도 설계를 고민하는 게 더 현실적입니다.

<br />
<br />
<br />


### 3. 필요시 computed column(계산 컬럼) 또는 function-based index를 고려할 것

<br />

대소문자 무시 검색, 날짜(일 단위) 컬럼로 검색처럼 가공된 값을 탐색해야할 떄가 있습니다. 이때 쿼리를 바꾸기 어렵다면, 가공 결과를 인덱싱 가능한 형태로 만들어 주는 방식을 고려해 볼 법합니다.

<br />

__케이스 1: LOWER(email)로 대소문자 무시 검색이 필요__

<br />

__Non-SARGable 쿼리__

```sql
SELECT * FROM users
WHERE LOWER(email) = LOWER(:email);
```

<br />
<br />

__대안 (1): 생성 컬럼(generated column) + 인덱스__

```sql
ALTER TABLE users
  ADD COLUMN email_lc VARCHAR(255)
    GENERATED ALWAYS AS (LOWER(email)) STORED,
  ADD INDEX idx_email_lc (email_lc);
```

그리고 쿼리는 이렇게 작성하면 됩니다.

```sql
SELECT * FROM users
WHERE email_lc = LOWER(:email);
```

<br />
<br />
<br />

__대안 (2): 함수 기반 인덱스(표현식 인덱스)__

<br />

MySQL 8에서는 표현식에 인덱스를 거는 방식도 사용할 수 있습니다.

```sql
CREATE INDEX idx_email_lc ON users ((LOWER(email)));
```

<br />
<br />
<br />


<!-- 
참고
https://www.linkedin.com/pulse/sargable-vs-non-query-md-mehedi-hasan/

-->