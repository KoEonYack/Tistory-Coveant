<!-- 

프로메테우스, 그라파나를 이용한 스프링부트 모니터링

-->

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>
Cover Photo by <a href="https://unsplash.com/@tobiastu?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Tobias Tullius</a> on <a href="https://unsplash.com/s/photos/monitoring?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
</center>

<br />
<br />
<br />

# 0. 소개

<br />

스프링부트 웹 애플리케이션을 배포하여 경우 잘 동작하는지 모니터링을 통하여 애플리케이션의 상태를 확인해야합니다. 모니터링을 통하여 문제가 발생하기전에 미리 대응을 하거나 문제를 분석하는데 도움을 받을 수 있습니다. 본 글에서 Spring Actuator, Micrometer, Prometheus, Grafana로 모니터링할 수 있는 환경을 구성해보겠습니다. 

<br />

- **Spring Actuator:** 스프링부트의 서브 프로젝트로 스프링 부트 애플리케이션이 제공하는 여러가지 정보를 쉽게 모니터링 할 수 있게 도와주는 라이브러리입니다.
- **Micrometer:** Spring 5부터 Spring의 메트릭은 Micrometer에서 처리됩니다.
- **Prometheus:** 오픈소스 시스템 모니터링 및 경고 툴킷입니다.
- **Grafana:** 메트릭을 시각화해주는 오픈소스 분석 플랫폼입니다.

<br />

사용한 스프링 부트 코드는 [Github](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-boot-monitoring) 에서 볼 수 있습니다. 

<br />

각 단계는 [Link](https://dev.to/mydeveloperplanet/how-to-monitor-a-spring-boot-app-f4m) 를 따랐으며 프로메테우스, 그라파나 설치 방법 및 설명이 필요한 부분에 대해서 보충설명하였습니다. 

<br />
<br />
<br />

# 1. 스프링 부트 애플리케이션

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/init.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

[start.spring.io](https://start.spring.io/) 에서 위와같이 Spring Boot Actuator, Prometheus, Spring Web 의존성을 추가한 후 GENERATE 버튼을 클릭합니다. 

<br />

```java
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'org.springframework.boot:spring-boot-starter-web'
runtimeOnly 'io.micrometer:micrometer-registry-prometheus'
```

<br />

생성한 스프링부트 프로젝트를 확인해보면 위와같이 의존성이 추가되어있습니다. 

<br />

다음과 같이 단순 문자열을 반환하는 GET API를 만들어봅니다. 

```java
@RestController
public class MetricsController {

    @GetMapping("/endPoint1")
    public String endPoint1() {
        return "Metrics for endPoint1";
    }

    @GetMapping("/endPoint2")
    public String endPoint2() {
        return "Metrics for endPoint2";
    }
}
```

<br />

스프링 부트를 실행하여 정상적으로 API가 동작하는지 확인합니다. 테스트를 위해서 Postman을 사용해도 되지만 간단한 GET API이기에 curl명령어를 사용하겠습니다. 

```text
$ curl http://localhost:8080/endPoint1
Metrics for endPoint1
```

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/init_actuator.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />
<center>
http://localhost:8080/actuator
</center>
<br />

http://localhost:8080/actuator 를 통해서 Actuator가 현재 제공하는 엔드포인트를 확인할 수 있습니다. 

<br />

다음과 같이 설정을 추가하여 Spring Actuator가 추가적으로 프로메테우스에 더 많은 정보를 제공할 수 있습니다. 

```text
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```
<center>
/resources/application.properrties
</center>
<br />

yaml 형식은 다음과 같습니다.

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, info, metrics, prometheus
```
<center>
/resources/application.yml
</center>
<br />

- __/actuator__ : Actuator가 제공하는 URL을 볼 수 있습니다.
- __/actuator/metrics__ : 메트릭 목록을 보여줍니다.
- __/actuator/prometheus__ :  Micrometer를 통해 수집된 metric들을 볼 수 있습니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/actuator.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" />
<br />
<center>
http://localhost:8080/actuator
</center>
<br />

각 엔트포인트별 설명은 [docs.spring.io](https://docs.spring.io/spring-boot/docs/1.5.x/reference/html/production-ready-endpoints.html) 혹은 [Spring Boot Actuator](https://www.baeldung.com/spring-boot-actuators)에서 확인하실 수 있습니다.


<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/actuator_prometheus.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />
<center>
http://localhost:8080/actuator/prometheus
</center>

<br />
<br />
<br />

# 2. 프로메테우스(Prometheus)

<br />

프로메테우스를 설치하는 방법은 여러가지가 있습니다. 

<br />

1. 서버에 직접 설치
2. binary로 설치
3. docker를 설치
4. kubernetes cluster에 설치

<br />

간단하게 로컬에서 테스트하기에는 도커를 이용한 설치가 좋습니다. 본 글에서 도커를 이용하여 설치하겠습니다. [prom/prometheus](https://hub.docker.com/r/prom/prometheus) 도커 이미지를 이용하겠습니다. 

<br />

```text
$ docker pull prom/prometheus
```

도커이미지를 받아옵니다.

<br />

```yaml
global:
  scrape_interval:     10s

scrape_configs:
  - job_name: 'spring--app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
```

prometheus.yml 이름름으로 다음과 같이 작성하고 저장합니다. host.docker.internal를 사용하여 도커 인스턴스 내부에서 호스트의 포트에 접속하겠습니다. 로컬이 아닌 환경이라면 이에 맞게 입력하며 됩니다. 

<br />

```text
docker run \
    -p 9090:9090 \
    -v {%상위경로%}/prometheus.yml:/etc/prometheus/prometheus.yml \
    prom/prometheus
```

{%상위 경로%}에는 위에서 입력한 prometheus.yml의 상위 경로를 작성해줍니다. 실행에 성공하면 [http://localhost:9090](http://localhost:9090/) 에 접근할 수 있습니다. 

<br />

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/pro_target.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

[http://localhost:9090/targets](http://localhost:9090/targets) 혹은 Status > Targets을 선택합니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/prometheus_graph.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

 `http_server_requests_seconds_max` 메트릭을 입력하여 Graph로 시각화하여 볼 수 있습니다.

<br />
<br />
<br />

# 3. 그라파나(Grafana)

프로메테우스가 메트릭에 대한 시각화를 제공하지만 그라파나를 이용하여 더욱 강력한 메트릭 시각화를 할 수 있습니다. 

간단한 실습을 위해 grafana/grafana([https://hub.docker.com/r/grafana/grafana/](https://hub.docker.com/r/grafana/grafana/)) 도커 이미지를 이용하여 실행해보겠습니다. 

```text
$ docker pull grafana/grafana
```

```text
$ docker run --name grafana -d -p 3000:3000 grafana/grafana
```

docker run을 성공적으로 실행하면 [http://localhost:3000](http://localhost:3000/) 를 브라우저에 입력하여 그라파나에 접속할 수 있습니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_login.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" />
<br />

1. 초기 계정은 admin / admin 입니다. 처음 로그인하면 비밀번호를 변경하는 창이 나옵니다. 

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_dashboard.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

2. 데이터소스 추가를 위해 그라파나 첫 페이지에서 `DATA SOURCE`를 클릭합니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_add.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

3. `Prometheus`에 수집되는 메트릭을 시각화 할것이기에 최상단에 있는 `Prometheus`를 선택합니다.


<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_add_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

4. Data Source이름을 지정해주고 URL에 [http://host.docker.internal:9090/](http://host.docker.internal:9090/)를 입력합니다. Data Source 설정에 성공할 경우 성공을 알리는 녹색 상태창이 나옵니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_add_dashboard.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

5. `Import`를 클릭하여 대시보드를 생성하겠습니다. 스프링부트 메트릭을 보여주는 유명한 대시보드로 [JVM dashboard](https://grafana.com/grafana/dashboards/4701)가 있습니다. 다양한 대시보드는 [Grafana Dashboards](https://grafana.com/grafana/dashboards)에서 확인할 수 있습니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_import.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

6. [https://grafana.com/grafana/dashboards/4701](https://grafana.com/grafana/dashboards/4701)를 입력하고 `Load`를 클릭합니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_import_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

7. 원하는 대시보드 이름을 입력하고 방금전에 생성한 Prometheus 데이터 소스를 선택한 후 `Import`를 선택합니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_dashboard_complete.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

8. 멋진 그라파나 대시보드를 만들어보았습니다. 스크린샷보다 더 많은 메트릭이 있으므로 스크롤하여 확인해보세요. 기본 범위는 24시간으로 설정되어있습니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_add_panel.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

9. 대시보드에 커스텀 패널을 추가할 수 있습니다. 상단의 `Add panel`을 클릭하세요.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/spring_boot_monitoring/img/grafana_add_panal_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" />
<br />

10. `Metrics` 필드에 `http_server_requests_seconds_max`를 입력해보세요. 
http://localhost:8080/actuator/prometheus 에 있는 Metrics 중에서 모니터링을 위한 Metrics를 선택하여 패널을 추가할 수 있습니다.

<br />
<br />
<br />

# 4. References

<br />

- Building a RESTful Web Service with Spring Boot Actuator [https://spring.io/guides/gs/actuator-service/](https://spring.io/guides/gs/actuator-service/)
- Prometheus 모니터링: [teamsmiley.github.io](https://teamsmiley.github.io/2020/01/17/prometheus/)
- SpringBoot Application의 monitoring 시스템 구축하기: [jongmin92.github.io](https://jongmin92.github.io/2019/12/04/Spring/prometheus/)

<br />
<br />
<br />
