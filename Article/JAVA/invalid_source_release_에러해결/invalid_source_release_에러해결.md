<!-- 

인텔리제이 JDK 설치 및 JDK 버전 변경 방법

-->

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/invalid_source_release_%EC%97%90%EB%9F%AC%ED%95%B4%EA%B2%B0/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />

## 0. 시작하며

<br />

인텔리제이를 사용하면 복잡하게 JDK를 다운로드받고 환경변수를 설정하는 과정을 거치지 않아도 됩니다.

<br />

초기 인텔리제이 JDK를 설치하지 않았거나 별도의 설정을 하지 않은 상태에서 컴파일시 __Cause : error : invalid source release 17__ 에러를 마주하게 됩니다. 

<br />
<br />

## 1. Project Structure 설정

<br />

__[File] -> [Project Structure] -> [Project]__  선택합니다.
- 맥 단축키: Command + ;
- 윈도우 단축키: Shift + Ctrl + Alt + S

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/invalid_source_release_%EC%97%90%EB%9F%AC%ED%95%B4%EA%B2%B0/img/project_strusture_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

사진과 같이 클릭하여 JDK 다운로드 창으로 넘어갑니다. 

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/invalid_source_release_%EC%97%90%EB%9F%AC%ED%95%B4%EA%B2%B0/img/project_strusture_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

다운받고자 하는 JDK 버전을 선택해주고, JDK 제공 벤더를 선택합니다. 

[Amazon Corretto](https://aws.amazon.com/ko/corretto/)는 OpenJDK의 프로덕션용 무료 멀티 플랫폼 배포입니다. Corretto는 오픈 소스 라이선스를 통해 Amazon에서 배포합니다. 

<br />

애플 실리콘칩(M1 칩)을 사용하고 있다면 aarch64로 다운받아줍시다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/invalid_source_release_%EC%97%90%EB%9F%AC%ED%95%B4%EA%B2%B0/img/project_strusture_3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

인텔리제이 21.2월 버전 이후부터 JDK 17을 지원합니다. 만약 JDK 17 이상의 버전이 보이지 않는다면 인텔리제이 업데이트가 필요합니다. 

<br />

방금 설정한 Language Level은 인텔리제이에서 작업하는 코드와 자바 컴파일러가 사용할 언어 레벨을 설정하는 것입니다. 예를 들어서 JDK 17을 사용하고 있지만 인텔리제이에서 작업하는 자바 코드가 JDK8과 호환되도록 하려면 Language level을 JDK8로 설정하면 됩니다. 그러면 JDK 8이상의 최신 문법을 작성할 수 없습니다. 

<br />
<br />
<br />

## 2. Project Setting 설정

<br />

__앱 최상단의 [Intelij IDEA] -> [Preferences]__  선택합니다.
- 맥 단축키: Command + ,(콤마)
- 윈도우 단축키: Ctrl + Alt + S
CTRL+ALT+S

<br />
<br />

## 2-1. Java Compiler 설정

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/invalid_source_release_%EC%97%90%EB%9F%AC%ED%95%B4%EA%B2%B0/img/preferences_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="90%" >
<br />

__[Build, Execution, Deployment] -> [Compiler] -> [Java Compiler]__ 에서 Project bytecode version을 앞서 설정한 JDK과 일치시킵니다.


<br />
<br />

## 2-2. Gradle JVM 변경

<br />

스프링부트 그래들을 사용한다면 추가로 설정을 해야합니다. 

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/JAVA/invalid_source_release_%EC%97%90%EB%9F%AC%ED%95%B4%EA%B2%B0/img/preferences_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="90%" >
<br />

<br />

__[Build, Execution, Deployment] -> [Build Tools] -> [Gradle]__ 에서 Gradle JVM 버전을 앞서 설정한 JDK와 일치시킵니다. 

<br />
<br />
<br />
