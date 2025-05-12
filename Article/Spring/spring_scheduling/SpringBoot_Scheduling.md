<!--

스프링 부트에서 Schedule 사용하기

 -->

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_scheduling/img/cover.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="95%" >
<br />
<br />
<br />

# 시작하며

<br />

특정 시간에 주기적으로 해야 하는 일이 있습니다. 뉴스레터와 같은 이메일 발송, 주기적으로 데이터베이스 동기화, 이용시간이 적은 시간에 대량 로그 전송 등등의 일이 그 예시가 될 것입니다. 

스프링 부트에서 이런 작업을 `@Schedule` 어노테이션을 사용하면 쉽게 할 수 있습니다. 본 글에서 `@Schedule` 활용을 살펴보겠습니다.

<br />
<br />
<br />

# 의존성 추가

<br />

`@Schedule`은 org.springframework.scheduling 패키지에 있습니다. 다음과 같이 의존성을 추가하면 됩니다.

<br />

Gradle이라면 build.gradle에 다음과 같이 추가합니다.

```java
implementation 'org.springframework.boot:spring-boot-starter-web'
```

<br />

메이븐이라면 pom.xml에 다음과같이 추가하면 됩니다.

```xml
<dependency> 
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

<br />
<br />
<br />

# 스케줄링 활성화

<br />

스케줄링 기능을 활성화하기 위해서는 configuration 클래스 혹은 지금처럼 애플리케이션 매인 클래스에 `@EnableScheduling`를 추가합니다.

```java
@EnableScheduling // 추가!!
@SpringBootApplication
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }
}
```

이로써 스프링 부트에서 스케줄을 사용하기 위한 모든 준비를 마쳤습니다.

<br />
<br />
<br />

# 작업 메서드를 구현할 클래스

```java
@Slf4j     // 로그를 위해서
@Component // 빈 등록 
public class ScheduledTasks {

    private static final DateTimeFormatter formatter 
            = DateTimeFormatter.ofPattern("mm:ss:SSS");

}
```

주기적으로 실행할 작업을 구현하기 위한 클래스를 위와 같이 구성하였습니다. 스케줄링할 작업 메서드는 두 가지 특징을 갖습니다.

<br />

1. 메서드의 리턴값은 void여야 한다. (그러나 리턴값이 있더라도 동작합니다.)
2. 메서드의 인자는 없어야 한다. (인자가 있으면 컴파일시 BeanCreationException 발생)

<br />
<br />
<br />

# Fixed Rate

<br />

고정된 간격으로 실행하는 작업을 하나 만들어보겠습니다.

```java
@Scheduled(fixedRate = 3000)
public void fixedRate() {
    log.info("fixedRate: 현재시간 - {}", formatter.format(LocalDateTime.now()));
}
```

fixedRate의 값은 ms 단위입니다. 3000ms = 3초, 즉 매 3초 주기로 작업을 실행합니다.


```text
현재시간 - 00:03:000
현재시간 - 00:06:000
현재시간 - 00:09:000
```

<center> 3초 간격으로 실행 (로그 단위 분:초:밀리세컨드) </center>


<br />
<br />
<br />


# Fixed Delay

<br />

fixedRate와 마찬가지로 fixedDelay는 일정 간격으로 실행됩니다. 그러나 fixedDelay는 이전 작업 완료 이후 시간부터 측정됩니다.

```java
@Scheduled(fixedDelay = 1000)
public void fixedDelay() throws InterruptedException {
    log.info("시작시간 - {}", formatter.format(LocalDateTime.now()));
    TimeUnit.SECONDS.sleep(5);
    log.info("종료시간 - {}", formatter.format(LocalDateTime.now()));
}
```

TimeUnit을 사용하여 5초 테스크를 수행하는데 5초가 걸리게 하였습니다.


```text
시작시간 - 00:29:000
종료시간 - 00:34:000

시작시간 - 00:35:000
종료시간 - 00:40:000

시작시간 - 00:41:000
종료시간 - 00:46:000

시작시간 - 00:47:000
종료시간 - 00:52:000
```
<center> 작업 종료 후 1초뒤 실행 (로그 단위 분:초:밀리세컨드) </center>

<br />

fixedDelay는 작업 종료 후 1초 뒤 주기적으로 실행되는 것을 확인할 수 있습니다. __작업 수행 시작시점부터 delay 후에 재시작하는 fixedRate와 차이가 있습니다.__

<br />
<br />
<br />

# Initial Delay

<br />

첫 작업이 실행되기 전까지 대기할 시간을 지정할 수 있습니다. 

initialDelay는 fixedRate, fixedDelay 모두 사용할 수 있습니다. 

```java
@Scheduled(fixedRate = 3000, initialDelay = 5000)
public void fixedRateAndInitialDelay() {
    log.info("현재시간 - {}", formatter.format(LocalDateTime.now()));
}
```

```text
// 서버는 00:25:000 시작

현재시간 - 00:30:000
현재시간 - 00:33:000
현재시간 - 00:36:000
```
<center> 첫 작업은 5초뒤 실행 (로그 단위 분:초:밀리세컨드) </center>

<br />

00:25:000에 서버가 시작되었다면 5초 후 첫 작업이 시작하여 3초 간격으로 실행됩니다.

<br />
<br />
<br />

# 크론 표현식

<br />

그러나 현실은 ms로 지정하는 fixedRate, fixedDelay보다 더 구체적인 조건을 주어야 합니다. 매일 아침 8시 뉴스레터를 보내기, 매일 새벽 03시 00분 로그전송 등등일 것입니다. 이럴 때는 크론 표현식이 유용합니다. 

<br />
<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_scheduling/img/chron.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="95%" >
<br />
<center>
크론식 의미를 실시간으로 확인 하고싶다면? <a href="crontab.guru/">crontab.guru</a>
</center>
<br />

다만 <a href="crontab.guru/">crontab.guru</a>를 볼 때 조심해야 할 점은 맨 앞글자인 초 단위가 없는 5글자이기에 실제 스프링부트에서 크론식에 작성할때는 앞에 초 글자까지 포함해야합니다. 5글자만 넣으면 BeanCreationException이 발생합니다.

<br />

```text
*           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
초(0-59)    분(0-59)　　시간(0-23)　일(1-31)　　월(1-12)　　요일(0-7)

```
- 총 6글자 (초/분/시간/일/월/요일)
- 요일의 경우 숫자 뿐만 아니라 MON 문자 표현 가능합니다.

<br />

```java
@Scheduled(cron = "0 0 7 * * ?")
public void cronExpression() {
    log.info("현재시간 - {}", formatter.format(LocalDateTime.now()));
}
```
`0 0 7 * * ?` 크론식의 의미대로 매일 아침 7시 로그가 찍힙니다.


<br />
<br />
<br />

# 쓰레드 풀 설정

<br />

기본적으로 `@EnableScheduling` 어노테이션을 사용 시 작업을 실행할 스케줄링을 위해서 스레드가 하나만 있는 스레드 풀을 만듭니다. @Scheduled 작업은 대기열에 쌓이게 되며 단일 스레드에 의해서만 실행됩니다. 

<br />

여러 작업을 제시간에 맞추어서 실행하기 위해서는 스레드를 늘릴 필요가 있을 것입니다. SchedulingConfigurer 인터페이스를 구현하여 설정을 변경할 수 있습니다.


```java
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    @Value("${thread.pool.size}")
    private int POOL_SIZE;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(POOL_SIZE);
        scheduler.setThreadNamePrefix("현재 쓰레드-");
        scheduler.initialize();

        taskRegistrar.setTaskScheduler(scheduler);
    }
}
```

`setThreadNamePrefix`를 이용하여 로그로 남길시 현재 실행하는 쓰레드 명 Prefix를 붙일 수 있습니다. 

<br />

thread.pool.size는 application.yml 파일을 통하여 3개로 늘려줍니다. 

```yml
thread:
  pool:
    size: 3
```

<br />

```text
[    현재 쓰레드-2] com.covenant.schedule.ScheduledTasks     : 현재시간 - 50:12:760
[    현재 쓰레드-3] com.covenant.schedule.ScheduledTasks     : 현재시간 - 50:13:763
[    현재 쓰레드-1] com.covenant.schedule.ScheduledTasks     : 현재시간 - 50:13:763
[    현재 쓰레드-1] com.covenant.schedule.ScheduledTasks     : 현재시간 - 50:13:766
```

setThreadNamePrefix 지정한 이름으로 현재 쓰레드라는 쓰레드 명이 추가되었으며 쓰레드 3개로 작업을 수행하는 것을 확인할 수 있습니다.

<br />
<br />
<br />

# 마치며

<br />

본 글에서 사용한 플레이그라운드 코드는 [Github. Tistory-Covenant-Code](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-schedule)에서 확인할 수 있습니다.

<br />

본 글 작성을 위해서 참고한 글 링크를 남깁니다.

- [attacomsian.com](https://attacomsian.com/blog/task-scheduling-spring-boot)
- [callicoder.com](https://www.callicoder.com/spring-boot-task-scheduling-with-scheduled-annotation/)

<br />
<br />
<br />
