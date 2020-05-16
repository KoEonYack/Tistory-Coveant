# Line Developers Meetup#5 LINE 개발자, 그리고 개발 문화


<br />
<img src="https://github.com/KoEonYack/PracticeCoding/tree/master/Article/Note/Line_dev_meetup_5/img/line_logo.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

LINE 개발자, 그리고 개발 문화라는 주제로 LINE Tech Evangelist Minwoo Park님이 진행하셨습니다. Zoom 웨비나로 진행되었으며 약 100명 이상 시청하였습니다.

<br />


# 라인 서비스

<br />

<br />
<img src="https://github.com/KoEonYack/PracticeCoding/tree/master/Article/Note/Line_dev_meetup_5/img/2.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

- 라인에 개발자 1000명이 있으며 많은 일을 하고 어떤 일을 하는지 소개하는 자리
- 라인은 한국보다는 해외에서 많은 서비스를 하고 있다.
- 핀테크, 블록체인(LINK), AI(Clova), 라인 게임, 라인 뮤직(일본에서는 2위) , 라인 자체 광고 플랫폼(Display AD, Official Account), 라인페이, 라인뱅크(일본, 대만, 태국 등등), 라인 보험 등등...
- 라인파이넨셜을 집중하고 있다. 한국, 일본에 별도 법인으로 독립되어 있다. 라인 파이넨셜은 판교역 근처에 별도의 오피스를 가지고 별도로 일을 한다. 

<br />


# 라인 개발

<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Note/Line_dev_meetup_5/img/3.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />


- 라인 전체는 8천명 이중에서 엔지니어 2천 5백명, 한국 1천명, 일본 1천명 기타 국가에서 500명 
- 한국(서현, 판교), 일본에 등 개발센터가 있다.  
- 자체 클라우드 인프라 Verda에서 VM, Object Storage, CDN, Load Balancer, Managed Kafka, Kubernetes, 등등 있다. 개발자들이 요구에 맞는 서비스를 제공하고 있다. 
- 어느정도 서비스 규모가 예상이 되면 플랫폼을 직접 만드는 경우가 많다. 아르메리아 비동기 프레임워크 직접 만들어서 쓰고 있다. API 게이트웨이, 프론트엔드, 서버 CI 관련 프레임워크, 플랫폼을 사내 환경에 맞게 구현되어 있다. 이런게 준비 되어있기 때문에 서비스들이 만들어질 때 완전히 처음부터 만드는 것 보다 빨리, 효율적으로 만들 수 있다. 

- 글로벌 서비스를 많이 한다. 
  - 타임존, 언어를 맞추어 개발할 수 있는 문화가 되어있다. 
  - 라마단 처럼 해외의 문화에 맞추기 때문에 이해를 할 수 있다.
  - 코로나로 인하여 VoIP를 제공하면서 글로벌 스케일 이슈가 있을 때 빠르게 대응할 수 있다. 
  - 해외에서 라인을 사용해보면 다른 국내 메신저보다 잘 설계 되어있다. 

- 데이터 플랫폼팀이 빠르게 분석하고 결정할 수 있기에 중요하다. data goverance, 보안을 중요하게 생각한다. 이런 플랫폼에서 최적화 하는 조직이 잘 되어있다.
- Clova AI랑 협업하거나 자체 ML 조직과 협업하여 이미지 인식, 라인에도 카메라 인식과 글자 인식이 잘 되어있다. 협업해서 잘 구현된 서비스를 만들 수 있다. 

<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Note/Line_dev_meetup_5/img/4.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

- 라인 스피커도 클로바와 협업해서 만든 작품이다.
- 레스토랑에서 AI가 주문하고 주문 받는 실제 B2B 서비스가 나와있다. 
- 12월에 라인 Developer Day를 했을 때 입장을 Face Sign을 사용했다. 


<br />
<br />



# 라인의 문화

<br />



<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Note/Line_dev_meetup_5/img/5.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

- 사용자에 도움이 되는가?
- 데이타 플랫폼팀 기준으로 A/B 테스트, 시각화 도구가 있다. 
- 도전을 즐기는 문화를 CEO 님이 강조한다. 그래서 라인의 다양한 도전하는 서비스가 있다. 하고 싶다는게 있으면 수용성이 높다.



- 소프트웨어가 가장 중요한 프로덕트이다. CEO님도 코딩하던 개발자이다.
- 화이트해커, 서비스 검수 잘 해주고 있다.
- 오픈소스를 이용해서 규칙에 맞게 잘 사용하고 있나? MIT 등등 규칙을 잘 지키고 있는지 검수한다. 
- 서비스 엔지니어링에서 작은 서비스를  만들 때 서비스 초반부터 설계, 릴리즈, CI 전체 모든 프로세스를 컨설링해주는, 물리서버 쓰세요, VM 쓰세요 해주는 분들이다. 
- 개발 지원 조직들이 잘 되어있다는 생각이 들었다. 입사전에 큰 조직에 있던 경험이 있지만 잘 되어있다.
- 라인, 라인 챗봇 API, Zoom, Github Enterprise, JET BRAINS, Jira, Confluence 등등을 사용하고 있다. 




<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Note/Line_dev_meetup_5/img/6.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

- 개발에 필요하면 소프트웨어, 하드웨어를 다 빠르게 지원해준다.


<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Note/Line_dev_meetup_5/img/7.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

- 회사가 커지면 누가 어떤 일을 하는지 알기 쉽지 않다. 한달에 한번으로 어떤 일을 하게 입력한다.
  - 안드로이드 개발자인데 Ios를 했다. 블로그를 했다. 등등 


<br />
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Note/Line_dev_meetup_5/img/9.PNG?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

- 라인 디벨로퍼데이 크게 하고 있다.
- 일본쪽 유튜브에도 잘 올라가있다.
- 서비스 뿐 아니라 기술에 대한 공유도 일어나고있다. 
- 사내 행사로 Tech Talk, 개발자 OJT 등 사내 시스템이 많이 있다. 
- 라인엔지니어링 블로그는 한국에서 가장 많은 글이 올라온다. 
- 블로그를 쓴다는게 많은 노력이 듦에도 하나의 서비스가 끝나면 글을 써야한다는 인식을 갖고 써주고 있다. 
- 현재 66개의 오픈소스로 공개되어있다.
  - 이중 많은 경우 SDK PHP, Python ...
  - armeria 비동기 서버 프레임워크..
  - ios 빌드시스템 등등..
- 작년 20개 행사 후원했다.

<br />


- 코드리뷰는 누구나 한다고 하지만 아무도 하지 않은것.. 이라고 하지만 실제로 라인에는 코드리뷰를 열심히 하는 회사이다. 
- 19년 11월 1일에 라인의 장애보고와 후속 절자 문화 글이 있다.
  - 같은 장애가 발생하지 않게 / 감지를 빨리 할지? / 등등..
- LINT(LINE Imporvement for Next Ten years) 장기적으로 신기술을 도입하는지 고민하는 조직이 있다.

<br />
<br />


# Q&A

<br />

- 어떤 사람과 일하는가?
  - 같이 일하고 싶은 느낌..
- 채용시 과제 전형은 어떤게 나오는가?
  - 팀마다 너무 다르다

- 프론트엔드 개발자가 하는 일 
  - 웹앱으로 되어있는 부분이 많이 있다.
- 에반젤리스트가 되기 위해서?
  - 개발자 경력 + 발표 능력
- 코드리뷰 받을 기회가 많이 없다. 
  - 협업 기회가 많이 있다. 같이 개발할 분을 만나라.
- 취준생에게 하고싶은말?
  - 개발 전문지식이 있어야한다. 개발 관련 블로그, 오픈소스 활동, 개발프로젝트를 해라.
- 개발자가 되기 위한 자세
  - 직업으로 개발자도 괜찮다. 개발을 좋아하면 제일 좋다. 프로젝트를 하면서 맞는 도메인이 있는지 확인해라.
- 잘하는 개발자?
  - 처음에는 코딩, 구현이 잘하는게  이후에는 요구사항을 준 사람이 말하지 않는 부분, 어떤 기술을 사용하는지 이런게 중요.
- 라인 개발자로 일하라면 어느정도?
  - 신입 테스트의 경우 지필, 실기 코딩테스트 잘해라. 경력 면접의 경우 스킬, 커뮤니케이션, 개발에 대한 열정
- 라인코딩테스트 난이도?
  - 6점 정도 되지 않을까..
  - 구글에 검색하면 공개된 문제도 있다.
- 라인 오픈소스 기여방법?
  - 풀 리퀘스트 받고 있다. clone 받아서 풀리퀘스트 날려줘라
- 신입 개발자가 개발 문화에 적응하기 위해서?
  - 멘토 의도를 잘 알아라
- 신기술을 프로덕션 환경에 도입하는 과정
  - 대규모 서비스이기에 조심스럽다.
- 회사업무를 통해 지식, 역량 쌓는 법?
  - 개인 블로그를 활용하라.



<br />
<br />
