# 가상 스타트업으로 알아보는 MSA


# 가상의 스타트업을 가정합시다

<br />
<img src="./img/amazon.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

10년전 새롭게 amazon이라는 스타트업이 시작한다고 가정해 봅시다. 오픈마켓으로 개발자 3명이 서비스를 개발할 것입니다. 

<br />

<br />
<img src="./img/1.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

- 개발자가 로컬에서 톰캣(WAS)을 이용해서 실행할 것입니다. 
- 애플리케이션은 상태가 중요하니 DB에 amazon 비즈니스의 모든 데이터가 저장 될 것입니다. (상품정보, 주문정보 등등..)

<br />
<br />

<img src="./img/2.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

- 개발자가 3명이 되었을 때 각자가 로컬에서 톰캣으로 실행합니다.
- 개발DB로 로컬에 공유하여 저장할 것입니다.
- 여러명의 개발자인 경우 형상관리를 위해서 SVN, Github과 같은 SCM(Source Code Management)로 관리합니다.

<br />
<br />
<img src="./img/3.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

- 상용 오픈을 할 때 배포 스크립트 혹은 톰캣매니저를 통해서 배포할 것입니다. 
- 톰캣매니저는 메모리 문제가 있어서 클린배포(코드를 올리고 톰캣을 내리고 배포 후 다시 올림)를 합니다.
- 상용으로 톰캣서버와 DB를 구입합니다. 
- 이 경우 __무중단 배포__ 가 불가능합니다. 새벽에 배포해야합니다..


<br />
<br />

<img src="./img/4.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

- 톰캣을 한대 더 구매합니다. 비용을 더 들여서 L4를 통해서 로드 벨런싱을 했을 것입니다.
- 간단하게 Ngnix를 통해서 
- 이런 구성을 HA(High Ability)구성이라고 합니다. 즉 지속적으로 구동(uptime)되는 시스템입니다.
- L4 다음에 apache, Ngnix를 넣놔서 정적파일도 캐쉬가 되어서 빠르게 서비스가 되게 할 수 있습니다.
- 요즘의 경우 load balancer가 ELB(Elastic Load Balancing)이 되었을 것입니다.


<br />
<br />
<img src="./img/5.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

- 그러다가 동접이 1000명을 넘겼습니다. 
- 서버가 많아지니 빠짐없이 배포하기 우해서 Jenkins, Ansible, Chef를 사용했을 것입니다. 
- 전체 배포시간이 늘어나지만 서비스에는 문제가 없을 것입니다.
- 이쯤되면 서비스 유지를 위해서 신규채용을 해야합니다.


<br />
<br />
<img src="./img/6.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />


- 조직 개편을 해서 주문팀과 상품팀으로 나누었습니다. 보통 팀이 많아져도 단일 저장소 정책을 유지합니다.
- 이때는 여러 문제가 발생합니다.
    - 문제 1. Branch Merge시 Conflict
    - 문제 2. QA를 어디까지 하지? --> 정기 배포일을 정합니다.
    - 문제 3. 주문팀과 상품팀의 각자 다른 일정이 있습니다.
    - 문제 4. 주문팀과 같이 운영하기에 상품팀이 서버를 마음대로 재시작 할 수 없습니다.


<br />
<br />
<img src="./img/7.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />


- 주문팀과 상품팀이 두 가지 서버군으로 분류했습니다. 
- 두 개의 도메인을 만들어서 각자의 서버군으로 가도록 만들었습니다.
- 주문쪽에서 상품 정보를 가져와야하는데 도메인이 Product에 있으니 가지고 올 수 없습니다. 이를 해결하기 위해 공통 코드가 있는 share.jar를 만들어서 product없이 가져오고 싶으면 share.jar의 특정 매소드를 호출하세요 합니다.
- 90% 이상의 회사가 이렇게 개발을 진행합니다.
- 이런 경우 잘 되지 않습니다.. 실제 대부분의 개발은 share.jar에서 합니다. 
    - 웬지 다른데서 사용할것 같은데 해서 order에서 개발하지 않습니다.
    - 이 필드 하나만 추가하면 되는데? 하며 share.jar의 코드를 복사 붙여넣기해서 조금 수정합니다.

<br />
<br />


# MSA를 적용하지 않은 최후

<br />

- 회사가 점점 더 성장해서 개발자가 150명 이상이 되었습니다.
- 회원팀, 상품팀, 주문팀, 정산팀, 셀러팀 등등... 
- DB는 하나만 사용합니다. share.jar라는 거대한 공통 코드에서 접근해야하기 때문입니다. 
- 이쯤 되면 share.jar는 소스 코드만 100MB가 넘는 수백만 라인의 코드가 됩니다.
- copy & paste 에서 오는 중복과 복잡도는 엄청납니다.
- 누가 코드를 어떻게 다른데서 사용하는지 모르기 때문에 share.jar코드를 절대 삭제할 수 없습니다.


<br />
<br />


# 콘웨이의 법칙(Conway's Law)

<br />
<img src="https://res.cloudinary.com/dzawgnnlr/image/upload/q_auto/f_auto/w_auto/conways_law_cornet.png" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

- (광범위하게 정의하면) 모든 조직은 조직의 의사소통 구조(communication structure)와 똑같은 구조를 갖는 시스템을 설계한다. - Melvin Edward Conway
- MIT나 하버드처럼 잘 알려진 일부 조직이 이 법칙을 신충하게 생각했다. 그들은 콘웨이의 법칙에 관한 연구를 수행하면서, 개발 조직의 구조와 그 조직이 개발 중인 코드를 비교했다. 그리고 콘웨이의 법칙이 믿을만하다는 사실을 발견했다! 연구를 위해 다양한 영역에서 선택한 90%의 코드는 콘웨이의 법칙을 완벽하게 따른다. (마이크로서비스 아키텍처, 우메쉬 램 샤르마 p.42)

<br />
<br />


# 모놀리틱 아키텍처 정리

- 대부분 IT 회사의 시작은 모놀리틱으로 시작했습니다.(아마존, 11번가, 쿠팡, 배달의 민족 ...)
- 하나의 공통코드를 import 해서 하나의 어플리케이션에 띄웁니다. 하나의 어플리케이션에서 다양한 서비스가 있어서 서비스를 담당하는 팀들과 의사소통을 해야합니다.

- 장점
    - 개발이 단순합니다. (repository 하나만 체크아웃 바당서 띄우면 되니깐..)
    - 배포가 단순합니다. (war 하나만 배포하면 되니깐)
    - Scale-out이 단순합니다.(서버 하나만 복사하면 되니깐)

- 단점
    - 무겁습니다.
    - 어플리케이션 시작이 오래 걸립니다.
    - 기술 스택이 바꾸기가 어렵습니다.
    - 높은 결합도.
    - 코드베이스의 책임 한계와 소유권이 불투명합니다.
        - 전체 코드를 파악하는 사람이 없습니다.

<br />
<br />

# 회사의 새로운 결정

- 차세대 프로젝트, 내부적으로 속칭 빅뱅을 합니다.
    - 은행권에서 차세대 프로젝트로 진행합니다.
    - 내부적으로 새롭게 만듭니다.
    - 그러나 만드는 순간부터 래거시가 됩니다.
    - Netscape가 이러다가 망했습니다.
- MSA 플랫폼을 구축하여 기존 래거시를 고사시킵니다.
    - 아마존, 넷플릭스, 11번가 .. 등등 이러한 방식을 사용합니다.

    
<br />
<br />


# MSA란?

<br />

> MSA란 시스템을 여러개의 독립된 서비스로 나눠서, 이 서비스를 조합함으로서 기능을 제공하는 아키텍쳐 디자인 패턴 - 조대협

- 독립된 서비스라는 말은 이 서버스에서 다른 서비스를 모르고 나만 혼자서 배포하는 것입니다.
- API 등을 통해서 조합해 기능을 제공합니다.


<br />
<img src="./img/twitter.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="50%" >
<br />

- 트위터는 500개가 넘는 마이크로서비스를 통해서 독립된 배포를 하고 있습니다. 

<br />
<br />


# MSA의 시작 아마존의 선택

<br />
<img src="https://techrecipe.co.kr/wp-content/uploads/2020/02/200223_Amazon-Empire_001.jpg
" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />


아마존 역사에서 제일 중요한 제프 베조스의 2002년 메일 내용입니다.

1) 모든 팀은 서비스 인터페이스로 데이터와 기능을 공개하세요.
2) 팀들은 이 인터페이스로 통신 하세요.
3) 직접 링킹, 다른팀 저장소에 직접 억세스, 공유메모리, 백도어 등, 다른 어떤 통신방법도 허용되지 않습니다. 네트워크를 통한 서비스 인터페이스 호출만 허용합니다.
4) 어떤 기술을 사용하는가는 중요하지 않습니다. HTTP, Corba, Pubsub, 커스텀 프로토콜 다 괜찮습니다.
5) 모든 서비스 인터페이스는 예외없이 기초부터 모두 외부에서 사용 가능하도록 설계되어야 합니다. 즉, 팀들은 인터페이스를 외부 개발자가 이용가능하도록 계획하고 설계해야 한다는 것입니다. 예외는 없습니다.
6) 이를 지키지 않는 사람은 해고 될것입니다.
7) 고맙습니다. 좋은 하루 되세요!

- MSA의 가장 본질적인 부분이 나왔습니다. 
- 서로에 관여함 없이 독립된 배포를 합니다.
- 내부적으로 사용한 똑같은 플랫폼을 2006년 공개하였고 이것이 아마존 웹 서비스(AWS)입니다.


<br />
<br />

# MSA란?

- 아직 공식적인 정의는 없으며 공감대가 있습니다. 
- 각 서비스간 네트워크를 통해서, 보통 HTTP를 통해서 통신을 합니다.
- 각 서비스는 독립된 배포 단위입니다.
- 각 서비스는 쉽게 교체 가능합니다.
- 각 서비스는 기능 중심으로 구성됩니다. (ex. 프론트엔드, 추천, 정산 등등..) 
- 각 서비스에 적합한 프로그래밍 언어, 데이터베이스, 환경으로 만들어집니다.
- 서비스는 크기가 작고, 상황에 따라 경계를 정하고, 자율적으로 개발되고, 독립적으로 배포되고, 분산되고, 자동화된 프로세스로 구축되고 배포됩니다. 


<br />
<br />

# MSA 장점

<br />
<img src="./img/mono_msa.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

<center>Source. Slide Share 모놀리스에서 마이크로서비스 아키텍처로의 전환 전략::박선용</center>

<br />


- 장점 1: 장애 격리 및 복구가 쉽습니다.
- 장점 2: 비용 효율적으로 증설이 가능합니다.
    - 모놀리스는 서비스가 폭주했을 때 서비스를 증산하기 위해서 전체가 증설이 되어야하지만 MSA는 해당 서비스만 증설하면 됩니다.
- 장점 3: 빠른 배포가 가능합니다. 
    - 모놀리스는 전체 서비스에 영향을 파악해서 빌드해야 합니다. 
- 장점 4: 생산성이 향상될 수 있습니다. 
    - 코드 양이 적어서 수비게 수정이 가능합니다.
- 장점 5: 신기술 도입이 쉽습니다.
    - 모놀리스는 전체에 서비스를 적용해야합니다. 
    - 마이크로서비스는 작은 단위에 적용하기에 쉽게 적용할 수 있습니다.
- 장점 6: Polyglot을 적용할 수 있습니다. 서비스에 최적화된 개발 언어와 데이터베이스를 선택할 수 있습니다. 


<br />
<br />

# 참고

<br />

- [왜 우리는 마이크로서비스를 구현하고자 하는가? - 이준희 차장(GS SHOP)](https://youtu.be/ovqTX6GuG_g)
- [토크ON세미나 Spring Cloud를 활용한 MSA 기초 1강 - 모놀리틱 아키텍처 이해 | T아카데미](https://youtu.be/D6drzNZWs-Y)
- [토크ON세미나 Spring Cloud를 활용한 MSA 기초 2강 - 마이크로서비스 아키텍처(MSA) 이해 | T아카데미](https://youtu.be/mJMzV6GCmPw)
- [MSA 제대로 이해하기 -(1) MSA의 기본 개념](https://velog.io/@tedigom/MSA-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0-1-MSA%EC%9D%98-%EA%B8%B0%EB%B3%B8-%EA%B0%9C%EB%85%90-3sk28yrv0e)
- [모놀리스에서 마이크로서비스 아키텍처로의 전환 전략::박선용::AWS Summit Seoul 2018](https://www.slideshare.net/awskorea/architecture-conversion-strategy-from-monolith-to-microservice-seon-yong-park)