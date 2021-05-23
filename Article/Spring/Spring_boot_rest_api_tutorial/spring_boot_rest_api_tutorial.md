<!-- 

🚀 [로켓 학습] 스프링부트 CRUD REST API (JPA, MySQL, Gradle)

-->

<br />
<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>
로켓처럼 빠르게 만나는 스프링부트!!
</center>
<br />
<br />

# 0. 시작하며

<br />

17년 처음 Django로 서버 사이드 개발을 접하였습니다. 당시 장고걸즈 튜토리얼을 따라하며 공부했습니다. DB도 모르던 시절이라 ORM도 생소하였고 템플릿 언어를 사용한 서버사이드 랜더링 모든게 낯설었습니다. 우선 튜토리얼을 따라서 게시판을 만들어보고 궁금했던 부분, 프로젝트시 막히던 부분을 찾아서 공부하는하는 방식이 도움이 되었습니다. Spring 공부 전략도 이와 마찬가지일 것입니다. Spring Boot 공부에 한세월, JPA 공부에 한세월 그리고 뭔가를 만들어보려고 하면 다시 새로운게 나오고 다시 공부하는데 한세월... 이러다가 흥미를 잃어버리거나 정년퇴직이 눈 앞에 다가올 것입니다. 

<br />

도서 예약 시스템 API 개발을 통하여 스프링 부트에서 기본적인 생성, 수정, 삭제, 조회에 대한 API를 만들것입니다. 중간에 있는 설명을 참고하여 간단한 API를 만들어보고 동작을 눈으로 확인하면서 하나씩 파악해 나가는 Top Down 공부법으로 Spring Boot를 정복해보세요.

<br />

수록된 코드는 [javatodev.com](https://javatodev.com/spring-boot-mysql/)를 따랐지만 설명은 공감이 되지 않은 부분이 있기에 제가 새로 작성하였습니다. 

<br />

글에서 사용된 전체 코드는 [Github](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-boot-mysql)에서 보실 수 있습니다.

<br />
<br />
<br />

# 1. spring initializr로 프로젝트 시작!

<br />

spring initializer는 초기 설정된 스프링 부트 프로젝트를 원하는 의존성을 추가하여 생성할 수 있는 웹 사이트입니다. [start.spring.io](https://start.spring.io/) 에서 하단의 스크린샷처럼 입력한 후 `GENERATE`를 클릭하면 됩니다. 

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/init.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

본 프로젝트를 위해서 네 개의 스프링부트 의존성은 추가했습니다. 각각 역활은 다음과 같습니다. 

<br />

- **Spring Web:** HTTP클라이언트와 Spring의 원격 지원을 위한 웹 관련 부분을 제공합니다.
- **Spring Data JPA:** JPA기반 repository를 쉽게 만들 수 있도록 다양한 기능을 제공합니다.
- **MySQL Driver:** MySQL 데이터베이스에 접근하기 위한 드라이버이빈다.
- **Lombok:** 반복적인 개발을 줄일 수 있는 여러 기능을 제공하는 자바 라이브러리입니다. (Ref. [lombok](https://projectlombok.org/))

<br />

위의 이미지와 같이 설정 후  `GENERATE`를 클릭하면 압축파일이 다운로드됩니다. 압축을 푼 후 IDE로 열고 build.gradle 파일을 열어서 다음과 같이 의존성이 추가되었는지 확인합니다. 

<br />

```java
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'mysql:mysql-connector-java'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

<br />

# 2. 만들어볼 API 살펴보기

<br />

본 글에서는 CRUD API를 만들어볼 것입니다. CRUD란 Create, Read, Update, Delete의 앞 글자를 딴 약자입니다. 웹 애플케이션을 개발하기 위해서 기본적으로 생성, 수정, 삭제, 읽기와 같은 기본 기능이 필요합니다. 본 예제에서 CRUD API를 만들어보면서 스프링 부트로 웹 애플리케이션 개발에 필요한 뼈대 기능 구현을 살펴보겠습니다. API URI와 상세 기능은 다음과 같습니다. 

<br />

<div align=center>

|ㅤㅤㅤㅤㅤㅤㅤㅤ URI                   |ㅤHTTP 메서드ㅤ| 설명 |
|---------------------------------|:-----------:|:------:|
| ㅤ **__/api/library/book__**             | GET    | 도서 전체 조회 |
|ㅤ **__/api/library/book?isbn=1919__** ㅤ  | GET    | 도서 ISBN으로 도서 조회 |
|ㅤ **__/api/library/book/:id__**          | GET    | 도서ID로 도서 조회 |
| ㅤ **__/api/library/book__**             | POST   | 도서 등록 |
|ㅤ **__/api/library/book/:id__**       | DELETE | 도서 삭제 |
|ㅤ **__/api/library/book/lend__**      | POST   | 도서 대출 |
|ㅤ **__/api/library/member__**         | POST   | 회원 등록 |
|ㅤ **__/api/library/member/:id__**     | PATCH  | 회원 수정 |

</div>

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/flow.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

우리가 만들고자하는 스프링 부트 프로젝트의 아키텍처는 위와 같습니다. 

<br />
<br />

# 3. 데이터베이스 설정

<br />
<br />

## 3-1. 데이터베이스 생성

<br />

MySQL을 설치하여 주시고 터미널에 다음의 명령어를 입력하여 MySQL에 접속합니다. 

```text
$ mysql -uroot -p
```

root 계정으로 접속합니다.

<br />

```text
mysq> create database covenant;
Query OK, 1 row affected (0.02 sec)
```

covenant라는 데이터베이스를 생성해줍니다. 

<br />

```text
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| covenant           |
| db_example         |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
| test               |
+--------------------+
```

<br />

MySQL에 기본적으로 있는 데이터베이스에 추가적으로 covenant 데이터베이스가 생성된 것을 확인할 수 있습니다. 

<br />
<br />

## 3-2. 데이터베이스 접속 정보 추가

<br />

스프링 부트에서 MySQL에 접속하기 위해서 application.properties에 다음과 같이 데이터베이스 접속 정보를 입력합니다. 

```text
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/covenant
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```
<center>
/src/main/resource/application.properties
</center>

<br />

최근 프로젝트는 가독성을 위해서 yml 형식으로 관리합니다. 위와 동일한 형태의 yml 설정 정보는 다음과 같습니다. 

```yml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/covenant
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: create
```
<center>
/src/main/resource/application.yml
</center>

<br />

spring initializer 생성시 기본적으로 생성되는 파일일인 application.properties과 다르게 application.yml은 없습니다. application.properties와 같은 경로에 application.yml 파일을 생성하면 됩니다. 

<br />

설정 정보에 저장한 속성값의 의미는 다음과 같습니다. 

- **spring.datasource.url:** MySQL의 호스트 이름, 데이터베이스로 구성됩니다. 로컬에 접속하기에 127.0.0.1을, MySQL 설치시 기본 포트 번호인 3306, 그리고 생성한 데이터베이스인 covenant를 입력합니다.
- **spring.datasource.username:** MySQL username
- **spring.datasource.password:** MySQL password
- **spring.jpa.hibernate.ddl-auto:** 데이터베이스 스키마에 영향을 줍니다. 테이블을 생성할 것인지, 변경점만 수정할 것인지 등등을 설정할 수 있습니다. 

<br />
<br />

## 3-3. spring.jpa.hibernate.ddl-auto 란?

<br />

spring.jpa.hibernate.ddl-auto에 설정된 값에 따라 데이터베이스 테이블에 다음과 같이 동작합니다. 

<br />

- **create:** Spring Boot가 실행되면 테이블을 지우고 새로 만듭니다. 기존에 테이블에 저장된 데이터는 삭제됩니다.
- **create-drop:** Spring Boot가 종료되면 테이블을 삭제합니다.
- **update:** Entity 클래스와 DB 스키마를 비교하여 DB에 생성되지 않은 테이블, 칼럼을 추가하며 제거는 하지 않습니다.
- **validate:** Entity 클래스와 DB 스키마를 비교하여 다르면 예외를 발생시킵니다.
- **none:**  아무것도 실행하지 않습니다.

<br />
<br />

# 4. Model Class 생성

<br />
<br />

## 4-1. 저자 Model

```java
package com.covenant.springbootmysql.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
}
```
<center>
/model/Author.java
</center>
<br />

저자의 Model은 PK(Primary Key)와 firstName, lastName필드를 갖습니다. 

<br />
<br />

## 4-2. 도서 Model

```java
package com.covenant.springbootmysql.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String isbn;
}
```
<center>
/model/Book.java
</center>
<br />

도서 모델은 PK 도서명, ISBN번호 필드를 갖습니다. 

<br />
<br />

## 4-3. 회원 Model

```java
package com.covenant.springbootmysql.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;
}
```
<center>
/model/Member.java
</center>

<br />

회원 모델은 PK(Primary Key)와 firstName, lastName 그리고 회원 상태를 알려주는 Enum 필드를 갖습니다. 

<br />

```java
package com.covenant.springbootmysql.model;

public enum MemberStatus {
    ACTIVE, DEACTIVATED
}
```
<center>
/model/MemberStatus.java
</center>
<br />

Member 모델의 status 필드에 들어갈 MemberStatus enum 타입은  회원 활성회원, 비활성 회원으로 구분합니다. 

<br />
<br />

## 4-4. 대출 Model

```java
package com.covenant.springbootmysql.model;

import java.time.Instant;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lend")
public class Lend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant startOn;
    private Instant dueOn;
    @Enumerated(EnumType.ORDINAL)
    private LendStatus status;
}
```
<center>
/model/Lend.java
</center>
<br />

Lend 모델은 대출시작, 대출종료 그리고 대출상태 필드를 갖습니다. 

<br />

```java
package com.covenant.springbootmysql.model;

public enum LendStatus {
    AVAILABLE, BURROWED
}
```
<center>
/model/LendStatus.java
</center>
<br />

Lend 모델의 status 필드에 들어갈 LendStatus enum 타입은 대출가능, 대출중으로 구분합니다. 

<br />
<br />
<br />

# 5. Model Class간의 관계 정의

<br />
<br />

## 5-1. Author와 Book (One to Many)

<br />

저자와 도서의 관계는 1:N관계입니다. 한 저자가 여러 책을 쓸 수 있기 때문입니다. 물론 공저(여러 저자가 하나의 책을 저술)도 가능하지만 간단한 예제를 위해서 공저의 경우는 배제하겠습니다. 

<br />

```java
@ManyToOne
@JoinColumn(name = "author_id")
@JsonManagedReference
private Author author;
```
<center>
/model/Book.java (코드 추가)
</center>
<br />

```java
@JsonBackReference
@OneToMany(mappedBy = "author", 
           fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Book> books;
```
<center>
/model/Author.java (코드 추가)
</center>
<br />
<br />
<br />

## 5-2. Book와 Lend (One to Many)

<br />

같은 책이 풍부한 도서관이여서 하나의 책을 여러명이 대출할 수 있다는 가상의 상황을 가정하곘습니다. 책과 대출은 1:N 관계입니다. 

```java
@JsonBackReference
@OneToMany(mappedBy = "book", 
           fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Lend> lends;
```
<center>
/model/Book.java (코드 추가)
</center>
<br />

```java
@ManyToOne
@JoinColumn(name = "book_id")
@JsonManagedReference
private Book book;
```
<center>
/model/Lend.java (코드 추가)
</center>
<br />
<br />

## 5-3. Member와 Lend (One to Many)

<br />

한 명의 회원이 도서 여러권을 대출할 수 있습니다. 회원과 대출은 1:N관계입니다. 

```java
@JsonBackReference
@OneToMany(mappedBy = "member",
          fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Lend> lends;
```
<center>
/model/Member.java (코드 추가)
</center>
<br />

```java
@ManyToOne
@JoinColumn(name = "member_id")
@JsonManagedReference
private Member member;
```
<center>
/model/Lend.java (코드 추가)
</center>
<br />

위의 코드를 통해서 엔티티간의 연관관계 매핑을 마쳤습니다.  위에서 사용한 어노테이션과 파미터의 의미를 살펴보겠습니다. 

<br />

- **@OneToMany:** 일다다 관계 매핑입니다. mappedBy를 통해서 연관관계 주인 필드를 설정합니다.
- **@ManyToOne:** OneToMany의 반대 관계인 N:1 관계 매핑 어노테이션입니다.
- **@JoinColumn:** 외래키를 매핑할때 사용합니다. name에는 참조하는 테이블의 기본키 칼럼명이 들어갑니다.
- **cascade:** JPA에는 영속화란 개념이 있습니다. CascadeType.ALL이면 부모가 영속화가 되면 자식도 영속화가 됩니다. 정확한 표현은 아니지만 부모와 자식의 상태가 동시에 변하게 합니다.
- **fetch:** EAGER, LAZY로 나뉩니다.
    - EAGER(즉시로딩): 연관관계가 설정된 모든 테이블에 대해서 조인이 이뤄집니다.
    - LAZY(지연로딩): 기본적으로 연관관계 테이블을 조인하지 않고 조인이 필요한 경우에 Join을 합니다.

<br />
<br />
<br />

## 5-4. JPA Cascade Types

<br />

저자를 삭제하면 저자가 쓴 책을 삭제하게 할 수 있습니다. 이를 Cascade Type 설정으로 가능합니다. 모든 타입을 이해하려면 JPA의 persist, merge, detach 등의 개념을 알아야하기에 ALL과 REMOVE만 정리하고 넘어가겠습니다. 

<br />

- **ALL: 모든** Cascade(PERSIST, MERGE, REMOVE, REFRESH, DETACH)를 적용합니다.
- **REMOVE:** 삭제시 연관된 엔티티 같이 삭제

<br />
<br />
<br />

## 5-5. JPA 모델 클래스에서 Enum 활용

<br />

위에서 **@Enumerated(EnumType.STRING)**과 **@Enumerated(EnumType.ORDINAL)** 를 사용했는데 의미는 다음과 같습니다. 

<br />

- **EnumType.ORDINAL:** enum 순서 값을 DB에 저장
- **EnumType.STRING:** enum 이름을 DB에 저장

<br />

대부분의 상황에서 **STRING** 을 사용합니다.

```java
public enum LendStatus {
    AVAILABLE, BURROWED
}
```

<br />

여기서 예약 상태가 추가되거나 AVAILABLE 상태가 삭제되면 숫자 값을 추가한 **ORDINAL**의 경우 잘못된 값이 매핑됩니다. **STRING**인 경우 중복되는 정보가 DB에 저장되어야하므로 낭비가 생길 수 있습니다. 이를 해결하기 위해 Attribute Converter를 사용할 수 있습니다. (참고. [https://lng1982.tistory.com/279](https://lng1982.tistory.com/279))

<br />
<br />
<br />

### 5-6. JsonBackReference, JsonManagedReference 이란?

<br />

JPA로 연관관계 작업시 연관관계간에 서로를 무한으로 호출하는 현상이 생깁니다. 순환참조를 방어하기 위한 어노테이션입니다. 부모 클래스에 @JsonManagedReference, 자식 클래스에 @JsonBackReference 어노테이션을 추가하면됩니다. 

<br />
<br />
<br />

# 6. Repository를 구현해보자

<br />

JPARepository를 이용해서 SQL 작성 없이 기본적인 CRUD를 구현할 수 있습니다. 

```java
@NoRepositoryBean
public interface JpaRepository<T, ID> 
    extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
```
<center>
JpaRepository.java
</center>
<br />

JpaRepository 인터페이스를 보면 CRUD 뿐만 아니라 PagingSorting을 지원합니다.  JpaRepository에서 지원하는 기본적인 메소드는 다음과 같습니다. 

<br />

- **save():** 레코드 저장
- **findOne():** PK로 레코드 한 건 찾기
- **findAll():** 전체 레코드 불러오기
- **count():** 레코드 갯수
- **delete():** 레코드 삭제

<br />
<br />

## 6-1. AuthorRepository

```java
package com.covenant.springbootmysql.repository;

import com.covenant.springbootmysql.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
```
<center>
/repository/AuthorRepository.java
</center>
<br />

JpaRepository를 활용하기 위해서 JpaRepository에 엔티티와 ID를 지정해주어야 합니다. 나머지 Book, Lend 그리고 Member는 위 코드의 반복입니다. 

<br />
<br />

## 6-2. BookRepository

```java
package com.javatodev.api.repository;

import com.javatodev.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
}
```
<center>
/repository/BookRepository.java
</center>
<br />

JpaRepository에서 기본적으로 제공하는 기능 이외에 추가적으로 구현하고 싶은 부분이 있다면 findBy로 시작하는 메소드 이름으로 쿼리를 요청하는 메소드임을 지정하면 됩니다. 

<br />
<br />

## 6-3. LendRepository

```java
package com.javatodev.api.repository;

import com.javatodev.api.model.Book;
import com.javatodev.api.model.Lend;
import com.javatodev.api.model.LendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LendRepository extends JpaRepository<Lend, Long> {
    Optional<Lend> findByBookAndStatus(Book book, LendStatus status);
}
```
<center>
/repository/LendRepository.java
</center>
<br />

여러 필드를 검색하고 싶다면 And로 연결하면 됩니다. findByBookAndStatus는 Book과 Status  필드를 검색합니다. 지원하는 메소드 이름으로 키워드 지정하는 방식에 대한 추가 내용은 [docs.spring.io](https://docs.spring.io/spring-data/jpa/docs/1.10.1.RELEASE/reference/html/#jpa.sample-app.finders.strategies) 를 참고하면 됩니다.

<br />
<br />

## 6-4. MemberRepository

```java
package com.javatodev.api.repository;

import com.javatodev.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
```
<center>
/repository/MemberRepository.java
</center>

<br />
<br />
<br />

## 7. Service를 구현해보자

<br />

본 코드에서는 LibraryService에 만들고자 하는 도서대출 API의 비즈니스 로직을 추가할 것입니다. 다양한 서비스 로직이 있다면 Service 클래스를 추가하면 됩니다. 

<br />
<br />

## 7-1. LibraryService

```java
package com.covenant.springbootmysql.service;

import com.covenant.springbootmysql.repository.AuthorRepository;
import com.covenant.springbootmysql.repository.BookRepository;
import com.covenant.springbootmysql.repository.LendRepository;
import com.covenant.springbootmysql.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    private final LendRepository lendRepository;
    private final BookRepository bookRepository;

}
```
<center>
/service/LibraryService.java
</center>
<br />

@RequiredArgsConstructor는 lombok이 초기화 되지 않은 필드를 생성합니다. 이를 통해서 의존성 주입(Dependency Injection)을 할 수 있습니다. 

<br />
<br />

## 7-2 LibraryService 추가 코드

```java
public Book readBook(Long id) {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
        return book.get();
    }

    throw new EntityNotFoundException(
                "Cant find any book under given ID");
}

public List<Book> readBooks() {
    return bookRepository.findAll();
}

public Book readBook(String isbn) {
    Optional<Book> book = bookRepository.findByIsbn(isbn);
    if (book.isPresent()) {
        return book.get();
    }

    throw new EntityNotFoundException(
                "Cant find any book under given ISBN");
}

public Book createBook(BookCreationRequest book) {
    Optional<Author> author = authorRepository.findById(book.getAuthorId());
    if (!author.isPresent()) {
        throw new EntityNotFoundException(
                    "Author Not Found");
    }

    Book bookToCreate = new Book();
    BeanUtils.copyProperties(book, bookToCreate);
    bookToCreate.setAuthor(author.get());
    return bookRepository.save(bookToCreate);
}

public void deleteBook(Long id) {
    bookRepository.deleteById(id);
}

public Member createMember(MemberCreationRequest request) {
    Member member = new Member();
    BeanUtils.copyProperties(request, member);
    return memberRepository.save(member);
}

public Member updateMember (Long id, MemberCreationRequest request) {
    Optional<Member> optionalMember = memberRepository.findById(id);
    if (!optionalMember.isPresent()) {
        throw new EntityNotFoundException(
                    "Member not present in the database");
    }

    Member member = optionalMember.get();
    member.setLastName(request.getLastName());
    member.setFirstName(request.getFirstName());
    return memberRepository.save(member);
}

public Author createAuthor (AuthorCreationRequest request) {
    Author author = new Author();
    BeanUtils.copyProperties(request, author);
    return authorRepository.save(author);
}

public List<String> lendABook (List<BookLendRequest> list) {
    List<String> booksApprovedToBurrow = new ArrayList<>();
    list.forEach(bookLendRequest -> {
        Optional<Book> bookForId = 
                bookRepository.findById(bookLendRequest.getBookId());
        if (!bookForId.isPresent()) {
            throw new EntityNotFoundException(
                        "Cant find any book under given ID");
        }

        Optional<Member> memberForId = 
                         memberRepository.findById(bookLendRequest.getMemberId());
        if (!memberForId.isPresent()) {
            throw new EntityNotFoundException(
                        "Member not present in the database");
        }

        Member member = memberForId.get();
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new RuntimeException(
                        "User is not active to proceed a lending.");
        }

        Optional<Lend> burrowedBook = 
        lendRepository.findByBookAndStatus(
                       bookForId.get(), LendStatus.BURROWED);

        if (!burrowedBook.isPresent()) {
            booksApprovedToBurrow.add(bookForId.get().getName());
            Lend lend = new Lend();
            lend.setMember(memberForId.get());
            lend.setBook(bookForId.get());
            lend.setStatus(LendStatus.BURROWED);
            lend.setStartOn(Instant.now());
            lend.setDueOn(Instant.now().plus(30, ChronoUnit.DAYS));
            lendRepository.save(lend);
        }
    });

    return booksApprovedToBurrow;
}
```
<center>
/service/LibraryService.java 코드 추가 
</center>

<br />

- **readBookById(String id)**:  id를 기준으로 데이터베이스의 도서를 조회합니다.
- **readBooks()**: 데이터베이스에 저장된 모든 도서를 읽습니다.
- **createBook(BookCreationRequest book):** BookCreationRequest로 도서를 생성합니다.
- **deleteBook(String id):** id를 기준으로 도서를 삭제합니다.
- **createMember(MemberCreationRequest request):** MemberCreationRequest로 회원을 생성합니다.
- **updateMember (String id, MemberCreationRequest request):** id에 해당하는 회원을 Member Creation Request로 변경합니다.
- **createAuthor (AuthorCreationRequest request):** AuthorCreationRequest로 저자를 생성합니다.
- **lendABook (BookLendRequest request):** BookLendRequest로 도서를 대출합니다.

<br />

Service에서 인자로 사용하는 **Request** 에 대한 class를 만들어봅시다.

<br />
<br />
<br />

## 7-3. AuthorCreationRequest

<br />

Request 클래스는 lombok에서 지원하는 @Data 어노테이션을 사용했습니다. @Data는 Getter, Setter, RequiredArgsConstructor, ToString, EqualsAndHashCode 어노테이션을 포함한 어노테이션입니다. 다음과 같이 Request 클래스를 생성해줍니다. 

<br />

```java
package com.covenant.springbootmysql.model.request;

import lombok.Data;

@Data
public class AuthorCreationRequest {
    private String firstName;
    private String lastName;
}
```
<center>
/model/request/AuthorCreationRequest.java
</center>
<br />
<br />

## 7-4. BookCreationRequest

```java
package com.covenant.springbootmysql.model.request;

import lombok.Data;

@Data
public class BookCreationRequest {
    private String name;
    private String isbn;
    private Long authorId;
}
```
<center>
/model/request/BookCreationRequest.java
</center>
<br />
<br />

## 7-5. BookLendRequest

```java
package com.covenant.springbootmysql.model.request;

import java.util.List;

import lombok.Data;

@Data
public class BookLendRequest {
    private List<Long> bookIds;
    private Long memberId;
}
```
<center>
/model/request/BookLendRequest.java
</center>
<br />
<br />

## 7-6. MemberCreationRequest

```java
package com.covenant.springbootmysql.model.request;

import lombok.Data;

@Data
public class MemberCreationRequest {
    private String firstName;
    private String lastName;
}
```
<center>
/model/request/MemberCreationRequest.java
</center>
<br />
<br />
<br />

## 8. Controller를 만들어보자

```java
package com.covenant.springbootmysql.controller;

import com.javatodev.api.service.LibraryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;    
}
```
<center>
/controller/LibraryController.java
</center>
<br />

RequestMapping은 URL을 컨트롤러의 메소드와 매핑할 때 사용하는 스프링프레임워크에서 제공하는 어노테이션 중 하나입니다. /api/library 값을 주어서 현재 컨트롤러에 메도스와 매핑되는 URL의 공통도상위 경로를 /api/library로 지정합니다. 

<br />
<br />

```java
@GetMapping("/book")
public ResponseEntity readBooks(@RequestParam(required = false) String isbn) {
    if (isbn == null) {
        return ResponseEntity.ok(libraryService.readBooks());
    }
    return ResponseEntity.ok(libraryService.readBook(isbn));
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

@GetMapping 어노테이션은 GET으로 요청된 URL을 처리합니다. 앞서 URL을 /api/library 로 지정했으므로 GET /api/library/book에 매핑되는 메소드입니다. 

<br />

컨트롤러에서는 직접 Repository를 호출하지 않고 Service를 거쳐서 호출합니다. 처리 결과에 따라 응답값을 반환합니다. 

<br />

*주의! 실제 서비스를 개발할때는 Repository에서 반환하는 ResponseEntity를 응답값으로 반환하면 안됩니다. RepositoryEntity 스팩이 변경되면 API의 응답값이 변경되기 때문입니다. 조회한 객체를 응답값으로 매핑하는 로직이 필요하지만 해당 예제에서는 생략하겠습니다. 

<br />

```java
@GetMapping("/book/{bookId}")
public ResponseEntity<Book> readBook (@PathVariable Long bookId) {
    return ResponseEntity.ok(libraryService.readBook(bookId));
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

REST API의 설계 규칙 중 집합(Collection)/{집합 번호} 가 있습니다. (참고: [REST란? REST API 디자인 가이드](https://covenant.tistory.com/241))

```java
@PostMapping("/book")
public ResponseEntity<Book> createBook (@RequestBody BookCreationRequest request) {
    return ResponseEntity.ok(libraryService.createBook(request));
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

**POST** 메소드는 도서 생성과 같이 리소스 생성에 사용하는 메소드입니다. application/json의 request body를 받도록 스프링부트에서 기본적으로 설정되어있습니다. 

<br />

```java
@DeleteMapping("/book/{bookId}")
public ResponseEntity<Void> deleteBook (@PathVariable Long bookId) {
    libraryService.deleteBook(bookId);
    return ResponseEntity.ok().build();
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

**DELTE** 메소드는 도서 삭제와 같이 리소스 삭제 메소드입니다. 해당 메소드는 도서 ID에 해당하는 도서를 삭제합니다.  **** 

<br />

```java
@PostMapping("/member")
public ResponseEntity<Member> createMember (@RequestBody MemberCreationRequest request) {
    return ResponseEntity.ok(libraryService.createMember(request));
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

회원 생성 API입니다. 

<br />

```java
@PatchMapping("/member/{memberId}")
public ResponseEntity<Member> updateMember (@RequestBody MemberCreationRequest request, @PathVariable Long memberId) {
    return ResponseEntity.ok(libraryService.updateMember(memberId, request));
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

**PATCH** 메소드는 이미 존재하는 리소스의 부분 정보를 수정합니다. 

<br />

```java
@PostMapping("/book/lend")
public ResponseEntity<List<String>> lendABook(@RequestBody BookLendRequest bookLendRequests) {
    return ResponseEntity.ok(libraryService.lendABook(bookLendRequests));
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

도서 대출 API입니다. 

<br />

```java
@PostMapping("/author")
public ResponseEntity<Author> createAuthor (@RequestBody AuthorCreationRequest request) {
    return ResponseEntity.ok(libraryService.createAuthor(request));
}
```
<center>
/controller/LibraryController.java 추가 코드
</center>
<br />

저자 추가 API입니다. 

<br />
<br />
<br />

# 9. API 테스트

<br />

Postman을 이용해서 지금까지 개발한 API를 테스트해보겠습니다. 

<br />
<br />

## 9-1. 저자 추가

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/postman_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

POST, PUT, PATCH 메소드인 경우 Request Body에 JSON을 선택해서 body에 요청값을 담아야합니다.

<br />
<br />

## 9-2. 도서 추가

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/postman_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

## 9-3. 회원 추가

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/postman_3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

## 9-4. 도서 조회

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/postman_4.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

## 9-5. ISBN번호로 도서조회

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/postman_5.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

## 9-6. ID로 도서 조회

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/postman_6.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

## 9-7. 도서 대출

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/Spring_boot_rest_api_tutorial/img/postman_7.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />
<br />

### 10. References

<br />

- javatodev: [javatodev.com](javatodev.com/spring-boot-mysql)
- 부스트코스 Spring: [www.boostcourse.org](https://www.boostcourse.org/web316/lecture/20655?isDesc=false)
- Spring에서 JPA / Hibernate 초기화 전략: [pravusid.kr](https://pravusid.kr/java/2018/10/10/spring-database-initialization.html)
- Spring Data JPA: [spring.io](https://spring.io/projects/spring-data-jpa)
- Building a RESTful Web Service: [spring.io](https://spring.io/guides/gs/rest-service/)
- @RequestMapping 어노테이션에 대하여: [sarc.io](https://sarc.io/index.php/development/1139-requestmapping)

<br />
<br />
<br />
