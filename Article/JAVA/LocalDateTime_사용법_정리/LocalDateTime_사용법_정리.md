<!-- 

헷갈리는 LocalDateTime 정리

-->

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/LocalDateTime_%EC%82%AC%EC%9A%A9%EB%B2%95_%EC%A0%95%EB%A6%AC/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

# 시작하며

<br />

Go 언어로 개발하다가 스프링으로 넘어와서 의외로 헷갈렸던 부분이 시간을 다루는 것이었습니다. Go에서는 time, Month 패키지명 부터 시작해서 함수 이름이 상당히 명확한데 LocalDateTime 이라는 이름부터 이상했으며 또 Java 8 이후로는 Date는 사용 안 한다고 하니 레거시는 Date를 사용하고.. 더욱 헷갈렸습니다. 본 글에서는 LocalDateTime을 정리해보겠습니다.


<br />
<br />
<br />

# 1. LocalDateTime now() - 현재 시스템 시간

<br />

- __static LocalDateTime now()__ - 현재 로컬 컴퓨터의 날짜와 시간을 반환

```java
LocalDateTime.now();
```
```text
2021-11-08T11:44:30.327959
```

<br />

- __static LocalDateTime now(Clock clock)__ - Clock에 맞는 현재 날짜와 시간을 반환

Clock 활용법은 [javatpoint](https://www.javatpoint.com/java-clock)를 참고하세요.

```java
LocalDateTime.now(Clock.systemDefaultZone());
```
```text
2021-11-08T11:58:20.551705
```

<br />
<br />

- __static LocalDateTime now(ZoneId zone)__ - ZoneId에 해당하는 날짜 시간을 반환 
    - ZoneId를 넘기면 Zone에 해당하는 현재 시간을 반환합니다. 
    - ex. 태평양 표준시: America/Los_Angeles, 일본 표준시: JST, 동부 표준시: America/Los_Angeles

```java
LocalDateTime.now(ZoneId.of("Asia/Seoul"));
```
```text
2021-11-08T11:58:20.551705
```

<br />
<br />
<br />


# 2. LocalDateTime 타입 생성

<br />

- __static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute)__
- __static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second)__
- __static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond)__

<br />

초, 밀리세컨드는 생략할 수 있습니다. 또한 month는 int 뿐만 아니라 enum형(java.time.Month)을 사용해도 됩니다.

```java
LocalDateTime.of(2021, 1, 1, 0, 0, 0);
```
```text
2021-01-01T00:00
```

<br />
<br />
<br />

# 3. format - LocalDateTime의 출력형식을 변환

<br />

- __DateTimeFormatter.ofPattern()__ 을 사용하면 출력형식을 변환할 수 있습니다.
    - yyyy: 년도
    - MM: 달
    - dd: 일
    - ss: 초
    - SSS: 밀리세컨드
- __ofPattern__ 인자로 "yyyy-MM-dd HH:mm:ss:SSS", "yyyy-MM-dd HH:mm:ss" 등 다양하게 활용할 수 있습니다.

<br />

```java
// localDateTime: 2021년 1월 1일 0시 0분 0초
LocalDateTime localDateTime 
    = LocalDateTime.of(2021, 1, 1, 0, 0, 0);

String localDateTimeFormat1 
    = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

String localDateTimeFormat2 
    = localDateTime.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")
        );

System.out.println("localDateTime = " + localDateTime);
System.out.println("localDateTimeFormat1 = " + localDateTimeFormat1);
System.out.println("localDateTimeFormat2 = " + localDateTimeFormat2);
```
```text
localDateTime = 2021-01-01T00:00
localDateTimeFormat1 = 20210101000000000
localDateTimeFormat2 = 2021-01-01 00:00:00:000
```

<br />
<br />

ISO 8601(참고 [위키백과](https://ko.wikipedia.org/wiki/ISO_8601)) 날짜 시간 표준 포멧의 경우 DateTimeFormatter에 등록된 포멧이 있습니다. 

<br />

```java
// localDateTime: 2021년 1월 1일 0시 0분 0초
LocalDateTime localDateTime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
String localDateTimeFormat
    = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);

System.out.println("localDateTime = " + localDateTime);
System.out.println("localDateTimeFormat = " + localDateTimeFormat);
```
```text
localDateTime = 2021-01-01T00:00
localDateTimeFormat = 2021-01-01
```

<br />
<br />
<br />

# 4. LocalDateTime에서 년도, 월, 주, 일 조회

<br />

- __int getYear()__ - 년도 반환
- __Month getMonth()__ - 달 반환
- __int getDayOfMonth()__ - 일 반환
- __DayOfWeek getDayOfWeek()__ - 요일 반환
- __int getDayOfYear()__ - 1년 중 며칠인지 반환

```java
LocalDateTime now = LocalDateTime.now();
System.out.println("getYear() = " + now.getYear());
System.out.println("getMonth() = " + now.getMonth());
System.out.println("getDayOfMonth() = " + now.getDayOfMonth());
System.out.println("getDayOfWeek() = " + now.getDayOfWeek());
System.out.println("getDayOfYear() = " + now.getDayOfYear());
```
```text
getYear() = 2021
getMonth() = NOVEMBER
getDayOfMonth() = 8
getDayOfWeek() = MONDAY
getDayOfYear() = 312
```

<br />
<br />
<br />

# 3. LocalDateTime에서 시간, 분, 초 조회

<br />

- __int getHour()__ - 시간 반환
- __int getMinute()__ - 분 반환
- __int getSecond()__ - 초 반환
- __int getNano()__ - 나노세컨드 반화

<br />

```java
LocalDateTime now = LocalDateTime.now();
System.out.println("getHour() = " + now.getHour());
System.out.println("getMinute() = " + now.getMinute());
System.out.println("getSecond() = " + now.getSecond());
System.out.println("getNano() = " + now.getNano());
```
```text
getHour() = 12
getMinute() = 7
getSecond() = 21
getNano() = 180343000
```

<br />
<br />
<br />

# 4. LocalDateTime 연산

<br />

## Plus

<br />

- __LocalDateTime plusYears(long years)__ - 인자만큼 년도 추가
- __LocalDateTime plusMonths(long months)__ - 인자만큼 달 추가
- __LocalDateTime plusWeeks(long weeks)__ - 인자만큼 주 추가
- __LocalDateTime plusDays(long days)__ - 인자만큼 일 추가
- __LocalDateTime plusHours(long hours)__ - 인자만큼 시간 추가
- __LocalDateTime plusMinutes(long minutes)__ - 인자만큼 분 추가
- __LocalDateTime plusSeconds(long seconds)__ - 인자만큼 초 추가
- __LocalDateTime plusNanos(long nanos)__ - 인자만큼 나노세컨드 추가

```java
// now: 2021년 9월 9일 10시 0분 0초
LocalDateTime now = LocalDateTime.of(2021, 9, 9, 10, 0, 0);
System.out.println("plusYears(3) = " + now.plusYears(3));
System.out.println("plusMonths(3) = " + now.plusMonths(3));
System.out.println("plusDays(3) = " + now.plusDays(3));
System.out.println("plusHours(3) = " + now.plusHours(3));
System.out.println("plusMinutes(3) = " + now.plusMinutes(3));
System.out.println("plusSeconds(3) = " + now.plusSeconds(3));
```
```text
plusYears(3) = 2024-09-09T10:00
plusMonths(3) = 2021-12-09T10:00
plusDays(3) = 2021-09-12T10:00
plusHours(3) = 2021-09-09T13:00
plusMinutes(3) = 2021-09-09T10:03
plusSeconds(3) = 2021-09-09T10:00:03
```

<br />
<br />

## Minus

<br />

- __LocalDateTime minusYears(long years)__ - 인자만큼 년도 감소
- __LocalDateTime minusMonths(long months)__ - 인자만큼 달 감소
- __LocalDateTime minusDays(long days)__ - 인자만큼 일 감소
- __LocalDateTime minusWeeks(long weeks)__ - 인자만큼 주 감소
- __LocalDateTime minusHours(long hours)__ - 인자만큼 시간 감소
- __LocalDateTime minusMinutes(long minutes)__ - 인자만큼 분 감소
- __LocalDateTime minusNanos(long nanos)__ - 인자만큼 나노세컨드 감소
- __LocalDateTime minusSeconds(long seconds)__ - 인자만큼초 감소

<br />

```java
// now: 2021년 9월 9일 10시 0분 0초
LocalDateTime now = LocalDateTime.of(2021, 9, 9, 10, 0, 0);
System.out.println("minusYears(3) = " + now.minusYears(3));
System.out.println("minusMonths(3) = " + now.minusMonths(3));
System.out.println("minusDays(3) = " + now.minusDays(3));
System.out.println("minusHours(3) = " + now.minusHours(3));
System.out.println("minusMinutes(3) = " + now.minusMinutes(3));
System.out.println("minusSeconds(3) = " + now.minusSeconds(3));
```
```text
minusYears(3) = 2018-09-09T10:00
minusMonths(3) = 2021-06-09T10:00
minusDays(3) = 2021-09-06T10:00
minusHours(3) = 2021-09-09T07:00
minusMinutes(3) = 2021-09-09T09:57
minusSeconds(3) = 2021-09-09T09:59:57
```

<br />
<br />
<br />

# 5. LocalDateTime 비교

<br />

- __isAfter()__ - 인자보다 미래 시간이라면 true 반환
- __isBefore()__ - 인자보다 과거 시간이면 true 반환
- __isEqual()__ - 인자와 같은 시간이면 true 반환
- __compareTo()__ 
    - __compareTo() > 0__ : 인자보다 미래 시간
    - __compareTo() < 0__ : 인자보다 과거 시간
    - __compareTo() ==__ : 인자와 같은 시간

<br />

```java
// localDateTime1: 2021년 1월 1일 0시 0분 0초
LocalDateTime localDateTime1 = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
// localDateTime2: 2021년 1월 1일 0시 0분 0초
LocalDateTime localDateTime2 = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
// localDateTime3: 2023년 1월 1일 0시 0분 0초
LocalDateTime localDateTime3 = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

if (localDateTime1.isBefore(localDateTime3)) {
    System.out.println("localDateTime1 is before localDateTime3");
}

if (localDateTime3.isAfter(localDateTime1)) {
    System.out.println("localDateTime3 is after localDateTime1");
}

if (localDateTime1.isEqual(localDateTime2)) {
    System.out.println("localDateTime1 is equal to localDateTime2");
}

if (localDateTime1.compareTo(localDateTime2) == 0) {
    System.out.println("localDateTime1 is equal to localDateTime2");
}
```
```text
localDateTime1 is before localDateTime3
localDateTime3 is after localDateTime1
localDateTime1 is equal to localDateTime2
localDateTime1 is equal to localDateTime2
```


<br />
<br />
<br />


# 6. LocalDateTime을 LocalDate로 변환

<br />

- __LocalDate toLocalDate()__ - 시간 단위(시간, 분, 초, 나노세컨드)가 없는 LocalDate로 반환합니다.

```java
LocalDateTime localDateTime = LocalDateTime.now();
System.out.println(localDateTime);
System.out.println(localDateTime.toLocalDate());
```
```text
2021-11-08T12:42:11.769062
2021-11-08
```

<br />
<br />
<br />

# 마치며 

<br />

본 글에서 사용한 플레이그라운드 코드는 [Github. Tistory-Covenant-Code](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/java-localdatetime)에서 확인할 수 있습니다.

<br />

글 작성을 위해서 참고한 글 링크를 남깁니다.

- [codechacha.com](https://codechacha.com/ko/java-compare-date-and-time/)
- [docs.oracle.com](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html)
- [javaguides.net](https://www.javaguides.net/2018/08/java-8-localdatetime-class-api-guide.html)

<br />
<br />
<br />

