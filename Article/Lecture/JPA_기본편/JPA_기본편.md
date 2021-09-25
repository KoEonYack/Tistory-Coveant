<!--

JPA 기본편 스터디 강의자료

-->

<!-- <br />
<img src="./img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br /> -->
<br />

# 시작하며

<br />

지난 3개월간 총 404장, 12화로 제작한 (사내) 스프링 스터디를 마쳤습니다. (스터디 자료 보러가기! 👉 [11번가 스프링 & 오브젝트 스터디 강의자료](https://covenant.tistory.com/246))

<br />

이어서 8주간 JPA 스터디를 진행합니다. 지금까지 사용한 프레임워크 ORM인 Django ORM, GO ORM(go-queryset)에 비하면 JPA는 공부할것이 많고 실무 환경에서 제대로 사용하려면 더더욱 어렵습니다.

<br />

ORM을 공부하는데 두 달의 시간을 할애하는것이 가성비가 좋지 않아 보일 수 있습니다. 시간을 십분 활용하여 JPA, 디자인 패러다임, Best Practice Case Study, Testing 등.. 다양한 이야기를 담을 예정입니다.

<br />

공부를 진행하는 지식을 세상에 내놓는다는 것은 쉬운 결정은 아니지만 지난 3개월 사내 스터디로 진행한 결과물을 오픈했던것과 마찬가지로 스터디 자료를 공개합니다. 모쪼록 JPA를 용감하게 사용하고 싶은 개발자들에게 도움이 되었으면 합니다.

<br />
<br />
<br />

# Week1 - 하이버네이트, 캐시

<br />
<div align=center>
<iframe src="//www.slideshare.net/slideshow/embed_code/key/13Fqx86IaENp6a" width="595" height="385" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> <div style="margin-bottom:5px"> 
<center>
<strong> <a href="//www.slideshare.net/ssuser8f4c99/jpa-week1" title="JPA 스터디 Week1 - 하이버네이트, 캐시" target="_blank">JPA 스터디 Week1 - 하이버네이트, 캐시</a> </strong> from <strong><a href="https://www.slideshare.net/ssuser8f4c99" target="_blank">Covenant Ko</a></strong> 
</center>
</div>
</div>
<br />

__Week1 개요__

<br />

기존에 접한 ORM과 JPA를 비교하며 JPA의 장점을 이야기로 스터디를 시작합니다. 장점이 많은 JPA, 그러나 실무에서 사용하기에 주저되는 부분이 있습니다. 이를 극복하기 위해서 어떻게 공부해야할지 살펴봅니다. 

<br />

Hibernate, JPA, Spring Data JPA 관계, 인프런 강의에 다루지 않은 하이버네이트 라이프 싸이클에 관한 코드를 살펴보면서 JPA와 연관성을 갖고 살펴봅니다. 

<br />

1차 캐시, 2차 캐시를 살펴보고 MSA 환경에서 2차 캐시 동기화 문제를 해결 방법에 대해서 Case Study로 살펴보겠습니다.

<br />

- 일시: 21. 08. 14(토) 10:30 ~ 12:00
- 범위: 섹션 0. 강좌 소개 ~ 섹션 4. 엔티티 매핑 데이터베이스 스키마 자동 생성 까지

<br />
<br />
<br />

# Week2 - Object Relational Mapping

<br />
<div align=center>
<iframe src="//www.slideshare.net/slideshow/embed_code/key/5xMnEBUt83zWQI" width="595" height="385" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> 
<div style="margin-bottom:5px">
<center>
<strong> <a href="//www.slideshare.net/ssuser8f4c99/jpa-week2-object-relational-mapping" title="JPA 스터디 Week2 - Object Relational Mapping" target="_blank">JPA 스터디 Week2 - Object Relational Mapping</a> </strong> from <strong><a href="https://www.slideshare.net/ssuser8f4c99" target="_blank">Covenant Ko</a></strong> 
</center>
</div>
</div>
<br />

__Week2 개요__

<br />

ORM과 데이터페이스의 패러다임은 전혀 다릅니다. (마치 강아지와 고양이처럼요^^) 데이터베이스를 오브젝트에 매핑해야합니다. 앤터프라이즈 애플리케이션 아키텍처 패턴에서는 4가지를 소개합니다. 본 스터디에서는 Active Record, Data Mapper에 대해서 알아볼 것입니다. 

<br />

ActiveRecord Pattern은 도메인 안에서 데이터베이스에 객체를 컨트롤을 합니다. 즉 findByName 메서드가 엔티티 객체안에 있는 셈입니다.DataMapper Pattern은 Domain과 Mapper가 분리되도록 구현합니다. 

<br />

Case 스터디로 Layered Architecture의 단점을 짚어봅니다. 테이블 주도 개발, 변경의 전파, 테스트가 힘들어지는 비대해지는 코드의 사례를 살펴볼 것입니다.

<br />

- 일시: 21. 08. 21(토) 10:30 ~ 12:00
- 범위 
    - 섹션 4. 엔티티 매핑 필드와 컬럼 매핑
    - 섹션 5. 양방향 연관관계와 연관관계의 주인 1- 기본

<br />
<br />
<br />

# Week3 - Entity Mapping / Hexagonal Architecture

<br />
<div align=center>
<iframe src="//www.slideshare.net/slideshow/embed_code/key/j7Hq0olSjesOrS" width="595" height="385" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> <div style="margin-bottom:5px"> 
<center>
<strong> <a href="//www.slideshare.net/ssuser8f4c99/jpa-week3-entity-mapping-hexagonal-architecture-250068805" title="JPA Week3 Entity Mapping / Hexagonal Architecture" target="_blank">JPA Week3 Entity Mapping / Hexagonal Architecture</a> </strong> from <strong><a href="https://www.slideshare.net/ssuser8f4c99" target="_blank">Covenant Ko</a></strong> 
</center>
</div>
</div>
<br />

__Week3 개요__

<br />

지난 Case Study에서 레이어 아키텍처의 문제에 대해서 살펴보았습니다. 핵사고날 아키텍처를 살펴보며 레이어 아키텍처의 문제를 극복할 수 있는지, 테이블 주도 개발에서 벗어나 ORM을 잘 활용할 수 있을지 고민하는 시간을 갖어보겠습니다.

<br />

- 일시: 21. 08. 28(토) 10:30 ~ 12:00
- 범위 
    - 섹션 5. 양방향 연관관계와 연관관계의 주인 2 - 주의점, 정리
    - 섹션 6. 다양한 연관관계 매핑 전체

<br />
<br />
<br />

# Week4 - GREAT STEP 1. 테스트 코드를 향한 위대한 발걸음

<br />
<div align=center>
<iframe src="//www.slideshare.net/slideshow/embed_code/key/rAy97oNnMZRYAQ" width="595" height="385" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> <div style="margin-bottom:5px"> <strong> <a href="//www.slideshare.net/ssuser8f4c99/great-step-1" title="GREAT STEP 1. 테스트 코드를 향한 위대한 발걸음" target="_blank">GREAT STEP 1. 테스트 코드를 향한 위대한 발걸음</a> </strong> from <strong><a href="https://www.slideshare.net/ssuser8f4c99" target="_blank">Covenant Ko</a></strong> 
</div>
</div>

<br />

__Week4 개요__

<br />

아키텍처 변환을 하지 않는다면 테이블 주도 개발을 피할 수 없을까요? TDD, DDD 등등의 방법을 선택할 수 있습니다. 테스트 코드를 막상 작성하려니 두려움이 따릅니다. 가장 좋은 방법은 지금 공부하고 있는 부분, 즉 JPA 그리고 영속 계층에 대해서 어떻게 테스트 코드를 작성하는지를 시작으로 좋은 테스트 코드 작성법에 대해서 공부하는 것입니다. 본 슬라이드에서 테스트 코드의 전반적 내용 그리고 Mokito를 통해서 간단한 테스트코드를 작성해보겠습니다.

<br />

- 일시: 21. 09. 04(토) 10:30 ~ 12:30
- 범위: 섹션 7. 고급 매핑

<br />
<br />
<br />

# Week5 - GREAT STEP 2. TDD, MockMVC

<br />
<div align=center>
<iframe src="//www.slideshare.net/slideshow/embed_code/key/nBgxuxu0mpX3gC" width="595" height="385" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> <div style="margin-bottom:5px"> <strong> <a href="//www.slideshare.net/ssuser8f4c99/great-step-2-tdd-mockmvc" title="GREAT STEP 2. TDD &amp; MockMVC" target="_blank">GREAT STEP 2. TDD &amp; MockMVC</a> </strong> from <strong><a href="https://www.slideshare.net/ssuser8f4c99" target="_blank">Covenant Ko</a></strong> 
</div>
</div>

<br />

__Week5 개요__

<br />

테이블 주도 개발을 벗어나기 위해서 TDD, DDD 등등의 방법을 사용할 수 있습니다. 이중에서 TDD가 무엇인지 살펴보겠습니다. 컨트롤러 테스트를 하기 위해서 MockMVC를 사용합니다. 단위 테스트에 만족하고 MockMVC에 대한 관심이 덜 할 수 있습니다. MockMVC를 사용하면 Swagger보다 장점이 많은 Spring REST Docs 문서를 만들 수 있습니다. 케이스 스터디로 API 문서화 도구로 Wiki에서 Spring Rest Docs로 전환한 우아한형제들 사례를 살펴보겠습니다.

<br />

- 일시: 21. 09. 11(토) 10:30 ~ 12:30
- 범위: 섹션 7. 고급 매핑

<br />
<br />
<br />

# Week6 - VALUE TYPES / CQRS

<br />
<div align=center>
<iframe src="//www.slideshare.net/slideshow/embed_code/key/yrTUTnrWZqaJKz" width="595" height="385" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> <div style="margin-bottom:5px"> <strong> <a href="//www.slideshare.net/ssuser8f4c99/jpa-week4-value-types-cqrs-250291440" title="JPA Week4. VALUE TYPES / CQRS" target="_blank">JPA Week4. VALUE TYPES / CQRS</a> </strong> from <strong><a href="https://www.slideshare.net/ssuser8f4c99" target="_blank">Covenant Ko</a></strong> 
</div>
</div>

<br />

__Week6 개요__

<br />

하이버네이트 타입 중에서 하나인 값 타입과 실제 코드의 어디에서 활용할 수 있을지 확인해봅니다. JPQL은 Where의 서브쿼리를 지원하지 않기에 Join으로 풀어야합니다. 간단한 예제를 통해서 Join으로 변환 전략에대해서 살펴봅니다. 마지막으로 CQRS 패턴에 대해 살펴봅니다. CQRS의 장점과 우아한형제들의 활용 사례를 케이스 스터디로 살펴보겠습니다.

<br />

- 일시: 21. 09. 25(토) 10:30 ~ 11:30
- 범위: 섹션 10. 객체지향 쿼리 언어1 - 기본 문법

<br />
<br />
<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Lecture/JPA_%EA%B8%B0%EB%B3%B8%ED%8E%B8/img/home.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>
허전해서 넣어본 동네 사진입니다.. :)
</center>
<br />
<br />
