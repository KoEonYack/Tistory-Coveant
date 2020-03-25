# 👀 한 눈에 확인하는 컴퓨터 구조
-------
* 본 포스트는 지금은 사라져버린 교육기관인 아이엘아이티 네트워크 강의를 수강후 재구성하였습니다.

<br/>


### 도입

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/1.JPG?raw=true" align="center" >
</p>

* [이미지 출처 unsplash.com](https://unsplash.com/photos/8n9npBvQUWg)
* 여러 층(Layer)로 이루어진 멋진 건물이 있다.
* 컴퓨터에서 층(Layer)이 그림이 나오면 **존립 의존**과 **상호 독립** 2가지를 생각해야 한다.  
  * 사옥이 존재한다. L1, L2, L3(연구개발 부서), L4(경영지원부)가 있다고 하자. 이렇게 다 모여서 기업을 이루고 있다.
  * 윗층과 아랫층은 **존립의 의존적**이다.
    2층 없이 3층이 존재할 수 없다. 바꿔 말하면 전재조건이다. 1층이 없으면 2층이 존재 할 수 없기 때문이다.
  * 내용적 측면에서 **상호 독집적**이다.
    같은 회사의 부서라고해서 경영지원부에서 생긴 문제는 연구부와 별 관계가 없다.

<br/>


### 비유

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/2.JPG?raw=true" align="center">
</p>

* 나라는 3가지로 나눌 수 있는데 **(1)영토**  **(2)정부** **(3)국민**으로 나눌 수 있다.

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/3.JPG?raw=true" align="center"  >
</p>

* 한 가족이 살아가기 위해서 집이라는 공간이 필요하다. 한 나라에는 이런 집이 괭장히 무수히 많을 것이다.
* 영희가 급한 볼일이 생겨서 철수네 집에 화장실을 갈 수 없다. 그런 짓을 하면 안된다고 법으로 주거침입으로 정해놨다.
* 집과 정부 사이에 공간을 띄워놨는데, 이 사이 공간은 에매함을 표현하기 위함다. 정부와 민간인 역할의 중간 사이에 있는 경우가 있다. 이를 공무원이라고 한다. 이들은 민간에 속하지만 하는 일은 정부의 일이기 때문이다.
* 남의 집에 진입하면 안되지만 정부로 부터 권한을 이양받은, 거기서 물건을 합법적으로 싹 들고 나와버릴 수도 있는 '검,경'이 있다.


<br/>


### 유저, 커널, 하드웨어

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/4.JPG?raw=true" align="center" >
</p>

* **영토**는 **하드웨**어(=물리적)로 볼 수 있다. **정부**는 __소프트웨어__(=논리적, virtual)로 볼 수 있다. 소프트웨어에서 virtual이 논리적이라는 말과 거의 똑같이 사용한다. 논리적이라는 말은 진짜가 아니라는 뜻이다. 그렇다라고 믿는 일종의 신앙적인 의미이다. 이 사실은 보안을 공부할 사람에게 중요하다.
* **정부** 영역은 **커널**이다. **민간**은 **유저** 영역이다. 점선 위로 올라가면 제대로 민간 영역이다.
* **개인**은 쓰레드로 볼 수 있다. 쓰레드는 프로세스 안으로 행동반경이 제한된다.
  * 메모리공간이라는 것은 스레드가 여럿 있을 때 하나의 공간을 나누어 쓸 것이다.
  * 그런데 쓰레드는 여러 개이다. 여기서 싱크로나이제이션(Synchronization) 즉 동기화 문제가 발생한다.
  * 화장실이라는 공간을 누군가 들어간다. 들어가기 전에 노트하고 아무도 없으면 문 열고 들어간다. 들어가서 문을 잠그고 볼 일을 볼 것이다. 누가 있으면 문이 열리지 않을 것이다. 그러면 밖에서 기다려야 한다. 노크하고 들어가서 문잠구는 것. 이것을 동기화라고 한다.
  * 자취하는 분은 노크를 하지 않는다. 즉, 싱글 프로세스에서 싱글 스레딩을 할 때 동기화를 고민할 이유는 없다.
  * 동기화에서 모든 프로세스 스레드간에 논리적 문제가 많이 일어난다. 하나의 공간에 하나가 와야 하는데 두개가 동시에 진입하는 문제, 경쟁 상황(race condition)이 발생하면서 시스템이 오동작한다. 즉 동기화가 가장 중요하다.
* **공무원**은 서비스(Service)이다. 윈도우 서비스 프로세서, 리눅스의 데몬 프로세서가 이에 속한다.
* 한 **집**은 하나의 **프로세스**로 볼 수 있다. 프로세스는 개별화된 공간인 메모리(virtual memory)를 가지고 있다. 다른 프로세스에 의해서 침범 받지 않도록 운영체제가 돕는다.
* 운영체제는 SW 영역 ~ User영역의 점선부분이다. HW ~ User영역의 점선부분까지를 Platform이라고 한다. CPU는 machine이라는 말을 사용한다.
* 검경 = 디버거. 디버거라는 것은 소프트웨어가 가지고 있는 결함을 해결하기 위해서 만들어진 또 다른 체계이다.
  * 이것을 잘 쓰면 결함을 찾는게 되고 나쁘게 사용하면 메모리를 위변조하는 메모리 해킹도구가 된다.


<br/>


<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/5.JPG?raw=true" align="center"  >
</p>

* 컴퓨터에 하드웨어 장치(Device)를 설치하면, 이를 구동하기 위해서 소프트웨어가 필요한데 커널영역에서 필요하다.
  * 참고적로 커널 영역에서 fault가 나면 블루스크린이 나온다.
* 장치를 구동시키기 위한 소프트웨어가 있는데, **디바이스 드라이버**라고 한다. 장치가 무엇이냐에 따라 상황이 달라지겠지만 특별한 일이 없다면 운영체제의 구성요소가 Driver안에 잇을 것이다.
* 우리가 흔히 어플리케이션에서 부르는게 윈도우 창 모양으로 User영역 점선 위에 있다. 프로세스가 구성요소와 연계되서 구성요소에 정보를 주면 Driver가 작동하고 Driver에 의해 장치가 작동하는 방식이다.
* 유저모드와 커널모드를 나누는 경계가 얼마나 서로 다른 영역 이나면 커널이 신의 영역이라고 하면 유저영역은 인간의 영역이다.
  * 신의 영역에서 인간의 영역에 갈 수 있다. 그런데 인간의 영역에서 신의 영역으로 갈 수 없다.
  * 대신 프로세서가 커널 영역으로 가지 못하면 상호작동을 할 수 없다. 신의 영역에서 인간의 영역으로 진입할 수 있도록 다리를 놔 줬다. 이 다리의 정체는 **파일**이다.
  * 파일은 대상체, 프로세스는 주체이다. 주체가 대상체를 상대로 무엇인가를 한다.
  * 프로세서가 파일에 대해서 하는 흔한 일이 (1) 생성(열기) (2) 읽기 / 쓰기 (3) 삭제 / 닫기 쉽게 말하면 IO가 될 것이다. 그러면 입출을 하면 IO에 따른 형식이 있을 것이다. 이런 Format을 읽기 형식, 쓰기형식을, 프로토콜이라고 한다. 내가 아는 프로토콜과 정의가 다른데? 내 말이 맞다. 구성요소와 Driver하고 공간이 있는데 Filter가 들어갈 수 있다.

<br/>


<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/6.JPG?raw=true" align="center"  >
</p>

* 하드 디스크와 SSD가 있다. 이를 움직이기 위한 Device Drivere가 있다. 그 위에 파일 시스템이 있다.(NTFS) 파일 시스템이 있으면 여기하고 연결된 다리가 파일의 형태로 있을 것이다.
* 윈도우 탐색기가 있는데, 어떤 파일을 클릭을 하면 파일을 거쳐서 Disk로 아래로 내려가는 것이다. 이때 말하는 파일은 두 종류이다. 커널과 인터페이싱을 하기 위한 파일과 디스크 안에 파일이 들어있다.
즉 파일은 정보의 단위이다. 그 파일이나 저 파일이나 본질은 파일이다.
* **유저 모드 어플리케이션이 커널에 진입할 수 있도록 추상화된 인터페이스가 파일**이다.
  * fopen("CON", "w"); 파일 포인터가 콘솔로 간다. printf한 것 처럼 콘솔 창에 찍힌다.
  * fopen("RRN", "w"); 프린터로 출력
* 필터가 삽입되면서 파일 시스템으로 운영되는 어떤 파일이라는 것을 연다던가, 누가 뭘 여는지 감시하게 만들 수 있다. 여기서 바이러스인지 아닌지 감시하면 **실시간 감시 엔진**이라고 한다. 이를 통해 실행되는 파일이 바이러스인지 아닌지 탐지한다.
* 백신은 걸렸는데 치료하자는것, 안티바이러스는 처음부터 걸리지 말자는 것이다.
  * V3를 하면 실시간 감시를 한다는 것이다.
  * 입출력하는 과정에서 필터가 들어가고 감시하니깐 속도가 떨어질 수 밖에 없다.


<br/>


<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/7.JPG?raw=true" align="center"  >
</p>


 * 하드웨어 레이어에 NIC(Network Interface Card, LAN카드)가 있다.
    * 여기서 네트워크선이 쭉 빠져서 L2스위치로 간다. 라우터를 거쳐 인터넷으로 갈 것이다.
    * 이 장치를 작동하기 위한 디바이스 드라이버가 있다.
    * NIC를 동작시키기 위해서 이 위에 붙는 애들이 **TCP/IP** 같은 프로토콜이다.
    * 인터넷 익스플로어를 이용해서 인터넷에 연결시키려고 한다면 파일을 이용해서 연결한다. 그러나 여기서는 파일이라고 안하고 **소켓**이라고 한다.
    * **소켓의 본질은 파일**이다.
    * lsof명령어를 수행하면 어떤 프로세스가 어떤 소켓을 열었는지 나온다.
  * 파일은 인터페이스이다. 프로세스가 입출력을 수행한다. 그 규칙을 프로토콜이라고 한다. 와이어샥은 WinPcap을 설치해야한다. 드라이버와 프로토콜 사이에 오는 것이 WinPcap이다.
  * WinPCap과 맞닿아서 작동하는 소프트웨어가 와이어샥크이다.
    * 와이어샤크는 정체성이 Analyzer이다.
    * WinPcap은 센서이다.
    * 왓다갔다 하는 데이터를 WinPcap이 카피를 해서 와이어 샤크로 보낸다.


<br/>


### 발상의 전환

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Network/Data/8.JPG?raw=true" align="center" >
</p>

<br/>

* 컴퓨터 구조 책에 등장하는 원형 모양의 3개의 레이어를 쭉 피면 위에서 설명한 3개의 층(유저, 커널, 하드웨어)이 나온다.

### 참고 문헌 및 이미지 출처
- [건물 이미지 https://unsplash.com/photos/8n9npBvQUWg](https://unsplash.com/photos/8n9npBvQUWg)
- [사람 아이콘 https://www.flaticon.com/free-icon/standing-up-man_10522#term=man&page=1&position=7](https://www.flaticon.com/free-icon/standing-up-man_10522#term=man&page=1&position=7)
- [PDF 아이콘 https://www.flaticon.com/free-icon/pdf_136440](https://www.flaticon.com/free-icon/pdf_136440)
- Computer Organization and Design 5th DAVID A. PATTERSON
