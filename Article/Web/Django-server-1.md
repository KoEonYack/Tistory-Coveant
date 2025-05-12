![cmd](https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/0.jpg?raw=true)

<br />

# Auzer를 이용하여 Django 배포하기

<br />

## 1편 Auzer 설정

<br />

* Auzer 19.08.26버전을 기준으로 작성하였습니다. 
* Django 2.2.4 버전을 기준으로 작성하였습니다. 
* 본 글에서 사용하는 예제는 [여기](https://github.com/KoEonYack/Everyone-Exercise)  주소입니다.

<br />
<br />
<br />


## 소개
- AWS를 이용한 배포에 관한 글은 많으나, Auzer를 이용한 배포는 거의 전무합니다. 
- Auzer를 이용하여 Django를 배포할 수 있습니다. 
- 사실상 가상 인스턴스 만드는 부분만 AWS EC2를 사용한 배포와 차이점입니다. 
- 가상 인스턴스 안에서 Django 프로젝트를 배포하는 부분은 AWS와 별반 다를바 없습니다.  
- 본 포스트가 Auzer를 이용해서 배포하려는 분들에게 가뭄에 단비 같은 존재가 되기를 바라고 있습니다.

<br />
<br />
<br />

## cmder 설치하기

<br />

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/1.JPG?raw=true)

<br />

- ssh연결을 위해서를 설치할 것입니다. [cmder.zip (클릭)](https://github.com/cmderdev/cmder/releases/download/v1.3.2/cmder.zip) 
- Linux, Ubuntu Bash(Window 10), Putty를 잘 사용할 줄 아신다면 본 프로그램은 설치하지 않아도 됩니다.
- 설치 중간에 창이 뜨면 __Unblock and Continue__ 를 클릭하면 됩니다. 

<br />
<br />
<br />

## Auzer 그룹 만들기

<br />

[AuzerPortal](https://portal.azure.com/)에서 로그인합니다.

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/2.JPG?raw=true)
왼쪽 메뉴에서 **리소스 그룹**을 클릭합니다.

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/3.jpg?raw=true)
- **+추가**를 클릭합니다.

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/4.jpg?raw=true)
- **구독**에는 자신의 구독 정로를 클릭합니다. 저는 무료 체험을 사용하므로 **무료 체험**을 선택하였습니다.
- **무료 체험**으로 리소스 그룹을 만들 경우 일부 기능을 사용을 못할 수 있습니다.
- **리소스 그룹**에는 자신이 만들고자 하는 그룹 이름을 작성합니다. 저는 Django 프로젝트를 관리하는 리소스가 될 것이므로 **DjangoProject**라고 이름을 지었습니다. 
- **검토 + 만들기**를 클릭합니다. 

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/5.JPG?raw=true)
- **유요성 검사를 통과했습니다.** 라고 나온 경우 **만들기**를 클릭합니다.

<br />
<br />
<br />

## Auzer 가상 컴퓨터 만들기
![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/6.jpg?raw=true)
- 우측에서 **가상머신** 탭을 선택합니다.

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/7.jpg?raw=true)

<br />

- **구독**에서 ** 무료 체험** 선택합니다.
- **리소스 그룹** 에서 방금 만든 리소스 그룹의 이름을 선택합니다. 
- **가상머신 이름** 에서 자신이 만들 가상 머신 이름을 정합니다. 저는 **AuzerServer** 로 정했습니다.
- ** 이미지** 에서 **Ubuntu Server 18.04 LTS** 를 선택합니다.
- **크기** 에서 B2s로 선택합니다. 본 서비스를 구동하기에 이정도면 충분합니다. 

<br />

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/8.JPG?raw=true)
- **암호**는 다소 복잡한 규칙을 적용해야하므로 잘 기억해주세요!

<br />

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/9.jpg?raw=true)
- **표준 SSD**를 선택합니다.
- 프리미어 SSD는 가격이 비싸며, HDD는 속도가 너무 느립니다.

<br />

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/10.jpg?raw=true)
- **인바운드 포트**에서 **HTTP, HTTPS, SSH**를 선택해줍니다.
- RDP는 원격 데스크톱 연결시 선택해 주어야 합니다. 하지만 SSH를 이용해서 서버 설정을 할 것이므로 RDP 포트(3389)는 열지 않겠습니다. 

<br />

![cmd](http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/11.jpg?raw=true)
- **유효성 검사 통과** 한다면 **만들기**를 클릭합니다.

<br />

![cmd](https://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Web/12.jpg?raw=true)
- **배포 진행중**에서 몇 분 기다리면 **배포 완료**로 변합니다.

<br />

지금까지 Auzer에서 그룹을 만들고, 가상 머신만들기까지 헀습니다.
다음편에서 가상머신에서 Django 서비스를 배포할 수 있도록 서버 설정을 살펴보도록 하겠습니다. 

<br />
<br />
<br />
