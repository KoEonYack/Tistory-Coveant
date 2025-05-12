<!-- 

WebSecurityConfigurerAdapter Deprecated 대응법

-->

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringSecurity/WebSecurityConfigurerAdapter_deprecated/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

# WebSecurityConfigurerAdapter란?

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/SpringSecurity/WebSecurityConfigurerAdapter_deprecated/img/deprecaed.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

스프링 시큐리티를 사용하면 기본적인 시큐리티 설정을 하기 위해서 WebSecurityConfigurerAdapter라는 추상 클래스를 상속하고, configure 메서드를 오버라이드하여 설정하였습니다. 그러나 스프링 시큐리티 5.7.0-M2 부터 WebSecurityConfigurerAdapter는 deprecated 되었습니다. 

<br />

[스프링 공식 블로그](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter) 2022년 2월 21일 글에서 WebSecurityConfigurerAdapter를 사용하는 것을 권장하지 않는다고 컴포넌트 기반 설정으로 변경할것을 권항합니다. 

<br />

스프링 부트 2.7.0 이상의 버전을 사용하면 스프링 시큐리티 5.7.0 혹은 이상의 버전과 의존성이 있습니다. 그렇다면 WebSecurityConfigurerAdapter가 deprecated 된 것을 확인할 수 있습니다. 현재 스프링 부트 3와 의존관계인 스프링 시큐리티6에서는 WebSecurityConfigurerAdapter 클래스가 제거되었습니다. 스프링 부트 혹은 스프링 시큐리티 버전을 높이기 위해서라면 WebSecurityConfigurerAdapter deprecated 된 설정을 제거해야 합니다.

<br />
<br />
<br />

# Case Study

<br />

케이스별로 어떻게 변경해야 하는지 알아보겠습니다.

<br />

## 1. configure

__WebSecurityConfigurerAdapter 코드__
```java
@Configuration 
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   @Override 
   protected void configure(HttpSecurity http) throws Exception { 
     http.cors().and().csrf().disable()
         .and().authorizeRequests() 
         .antMatchers("/home").permitAll()
         .antMatchers("/mypage").authenticated()
         .anyRequest().authenticated()
     ; 
   }
}
```

<br />


__WebSecurityConfigurerAdapter 제거 코드__
```java
@Configuration 
@EnableWebSecurity
public class WebSecurityConfig {

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        http.cors().and().csrf().disable()
         .and().authorizeRequests() 
            .antMatchers("/home").permitAll()
            .antMatchers("/mypage").authenticated()
            .anyRequest().authenticated()
      return http.build();
   }
}
```

<br />


WebSecurityConfigurerAdapter를 제거한 코드에서는 @Bean 에노테이션을 이용하여 빈으로 등록해주어야 합니다. HttpSecurity 자체를 변경할 필요는 없습니다. 

<br />

다만 주의할 점은 메서드 이름이 configure에서 filterChain으로 변경해야 합니다. 처음에 변경하지 않았는데 스프링 시큐리티 설정이 적용되지 않아서 한참 삽질한 경험이 있습니다. 

<br />
<br />
<br />

## 2. AuthenticationManager

__WebSecurityConfigurerAdapter 코드__
```java
@Configuration 
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowire
    UserDetailsService userDetailsService;

    @Override
    public void configure(
        AuthenticationManagerBuilder authenticationManagerBuilder) 
                                                        throws Exception {

        authenticationManagerBuilder.userDetailsService(userDetailsService)
                                    .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

<br />

__WebSecurityConfigurerAdapter 제거 코드__
```java
@Configuration 
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
```

<br />

스프링 시큐리티의 인증을 담당하는 AuthenticationManager는 이전 설정 방법으로 authenticationManagerBuilder를 이용해서 userDetailsService와 passwordEncoder를 설정해주어야 했습니다. 

<br />

그러나 변경된 설정에서는 AuthenticationManager 빈 생성 시 스프링의 내부 동작으로 인해 위에서 작성한 UserDetailsService와 PasswordEncoder가 자동으로 설정됩니다. 

<br />
<br />
<br />


## 3. web.ignoring

__WebSecurityConfigurerAdapter 코드__
```java
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/ignore1", "/ignore2");
    }
}
```

<br />

__WebSecurityConfigurerAdapter 제거 코드__
```java
@Configuration  
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }
}
```


WebSecurity를 커스텀 설정하기 위해서 WebSecurityCustomizer라는 콜백 인터페이스를 사용합니다. /ignore1, /ignore2 요청에 대해서 스프링 시큐리티 적용하지 않는 설정입니다. webSecurityCustomizer 빈을 등록하면 됩니다.

<br />
<br />
<br />

## 4. In-Memory Authentication

<br />


__WebSecurityConfigurerAdapter 코드__
```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) 
                                                throws Exception {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("ADMIN")
            .build();
        auth.inMemoryAuthentication()
            .withUser(user);
    }
}
```

<br />

__WebSecurityConfigurerAdapter 제거 코드__
```java
@Configuration
public class SecurityConfiguration {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
```

개발 단계에서 간단하게 사용할 때 인 메모리에 사용자 정보를 저장합니다. userDetailsService를 빈 이름으로 하며 InMemoryUserDetailsManager 반환 값으로 갖는 빈을 등록하여 설정합니다.


<br />
<br />
<br />


## 참고. 

<br />

- [Spring Blog. Spring Security without the WebSecurityConfigurerAdapter](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)
- [Medium. Spring Security — H2 Database — Without WebSecurityConfigurerAdapter](https://medium.com/@gurkanucar/spring-security-h2-database-without-websecurityconfigureradapter-fc4a83b6f60d)

<!-- 
- https://youtu.be/s4X4SJv2RrU
- https://javatechonline.com/spring-security-without-websecurityconfigureradapter/
- https://devlog-wjdrbs96.tistory.com/434
- https://honeywater97.tistory.com/264
- http://honeymon.io/tech/2019/06/17/spring-boot-2-start.html
-->

<br />
<br />
<br />
