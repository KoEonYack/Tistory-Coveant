# Go 언어로 네트워크 프로그래밍하기



<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Golang%EC%9C%BC%EB%A1%9C_%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%ED%95%98%EA%B8%B0/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

최흥배(컴투스)님께서 NDC에서 18년 7월 발표하신 [Go 언어로 네트워크 프로그래밍하기](https://youtu.be/5NsUPbdKOF8) 내용을 정리한 것입니다. 

<br />
<br />


# 결론부터 이야기한다면?

<br />


- GO 언어로 고성능 네트워크 프로그래밍을 쉽게 할 수 있다.
- GO는 C언어가 진화한 것으로 C++과 다르다.
    - 그래서 OOP 언어를 주로 하신 분이 GO를 처음 다루면 설계할 때 무넺가 된다.
- 고루틴은 stateless에서 아주 좋지만 statefull 에서는 한계가 있다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Golang%EC%9C%BC%EB%A1%9C_%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%ED%95%98%EA%B8%B0/img/game_server.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>Source. 18 NDC 발표자료</center>
<br />

- 대규모 온라인 게임할 때 분산 서버를 만들 때 Front Server(클라이언트와 서버간의 패킷 전달)은 stateless에 가깝고, 컨텐츠 로직이 없으므로 Go언어를 사용하면 좋다.


<br />
<br />


# GO가 무엇인가?

<br />


- 구글이 만든 언어
- 2009년에 나와서 10년 넘었다.
- Docker, Kubernetes, GRPC에서 사용하고 있다. (신뢰 가득?)
- [Python 을 사용하는 대기업들 지금은 어떻게 됐을까?](https://coolspeed.wordpress.com/2016/10/23/big_firms_that_used_to_use_python_nowadays/)
    - 구글, 드랍박스, 우버 등등 실리콘벨리의 많은 회사들이 GO 언어로 넘어왔다.
- [로켓펀치 GO](https://www.rocketpunch.com/tag/golang-og092d)
- 데브시스터즈 게임서버로 GO선택
- 9M Interactive, 트리노드, 원더피플, 블루홀스콜, 컴투스 등등 Go언어를 사용한다.
    - 구인하는데 유명한 언어 써있으면 쓸 수 있고 안 쓸 수 있는데, 덜 유명한 언어가 써 있으면 쓴다는 뜻.
- Go언어를 모바일에서 쓸 수 있는데 실험 단계이다. (18년 7월 기준)

<br />
<br />

# Go 잘하는것

<br />


- 메모리 자동관리
- 네트워크 프로그래밍
- 병렬 프로그래밍
- Go의 메모리 자동 관리 때문에 오해를 하는데 VM 쓰는 언어는 아니다.
    - 그래서 VM 쓰는 언어보다 성능이 좋다.
- GC는 있다. 
- Thread를 막 만들어도 괜찮다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/GO/Golang%EC%9C%BC%EB%A1%9C_%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC_%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D%ED%95%98%EA%B8%B0/img/channel.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<center>Source. geeksforgeeks.com</center>
<br />

- 채널
    - 고루틴과 고루틴이 서로 통신을 해야할 경우가 있다. 채널은 고루틴과 통신하기 위한 통로로 생각해라.
- 클라이언트 접속마다 고루틴을 만드어도 10k 문제는 전혀 없다.
- C++에서 IOCP, epoll을 사용해야하지만 GO는 BSD Socket 사용하듯이 사용하면 된다.
    - IOCP: 강제 멀티스레드 프로그래밍
    - epoll: 멀티코어를 사용하려면 IOCP와 같은 기능을 구현해야 한다. 
    - Go언어를 사용하면 고성능 네트워크 개발이 쉬워진다.
- 크로스 플랫폼 개발이 쉽다. 
- 오픈소스 혹은 Linux 계열어세 시작한 Product는 윈도우 지원 시 네트워크 부분에서 IOCP를 잘 지원하지 않는다. GO는 지원하는 플랫폼의 최신 기능을 지원한다. 네트워크에서 IOCP 지원한다. 

<br />
<br />


# 쓸만한 프로그래밍 언어 조건

<br />

- 언어가 윈도우를 지원하고, IDE가 있어야 상용 프로그램 개발에 사용할 수 있어야 한다고 생각한다. 
    - 회사 PC는 대부분 윈도우, 게임 개발할 때 IDE가 없으면 골치아프기 때문.
- VSCode, GoLand로 개발. 
    - VSCode는 Code lock이 많이지면 디버깅이 어렵다는 말이 있다. 
    - 돈이 있다면 GoLand가 좋다.

<br />
<br />

# GO로 게임 서버를 개발하려면?

<br />

- GO로 게임서버를 개발하긴 쉽지 않다.
    - OOP 개발자는 개발 시작하려면 클래스부터 만들어야하는데 GO는 없다.
    - GO는 struct는 있는데 C언어에 가깝다.

<br />
<br />

# GO는 빨리 배울 수 있다.

<br />


- 배울것이 적다. 다른 말로 기능이 적다.
- 뭐가 없냐면,
    - 함수 내의 변수, 파라미터 변수에 const를 줄 수 없다.
    - Generic이 없다. 
    - STL도 없다.

<br />
<br />

# 실시간 통신 게임 서버

<br />

- 비 실시간 통신에서는 고루틴이 최고 좋다.
- 그러나 실시간 통신에서 고루틴은 네트워크 통신에서만 도움이 된다. 
- stateless가 아닌 statefull에서는 Lock이 필요하다. 

<br />
<br />

# Go는 빠르지만..

<br />

- C++보다는 느리다.
- CPU 연산 처리 속도는 C++이 더 빠르다.
- C++은 세밀하게 하나 내가 괸라한다. Go는 세밀하게 관리할 수 없다.


<br />
<br />

# 공부는 어떻게?

<br />

- 물어볼 곳이 별로 없다.
- 그러나 stack overflow에 답이 다 있다. 
- 책 조금씩 나오고 있다. 
    - 가장 빨리 만나는 Go언어 주말에 책 보고 바로 코딩 시작하면 된다.
    - [golangkorea Github](https://github.com/golangkorea)
        - effective-go 좋은 문서 한글로 번역해두었다.
    - [golang korea facebook](https://www.facebook.com/groups/golangko/) 
        - 한국인 모임
    - [A Tour of Go](https://go-tour-kr.appspot.com/#1)
        - 웹 사이트에서 바로 실행해 볼 수 있다.
    

<br />
<br />
