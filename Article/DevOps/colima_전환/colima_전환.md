
<!--

MacOS에서 Colima 사용 완벽정리!

-->


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/DevOps/colima_%EC%A0%84%ED%99%98/img/cover.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

## 1. 시작하며 

<br />

많은 MacOS 개발자에게 Docker Desktop 환경에서 Docker를 사용해 왔습니다. 그러나 기업 사용자를 대상으로 유료화 정책을 발표하였습니다. 제작년부터 비용 효율화 측면에서 전사적으로 Docker Desktop이 아닌 다른 방법으로 사용하도록 가이드가 되었습니다. 

<br />

라이선스 비용 문제를 해결하고, 더 효율적인 개발 환경을 구축하기 위한 대안을 모색하던 중, Colima를 도입하였습니다.

<br />
<br />
<br />

## 2. Colima란 무엇인가?

<br />

Colima는 macOS에서 컨테이너를 실행하기 위한 최소한의 환경을 제공하는 오픈소스 도구입니다. 가벼운 리눅스 가상 머신(VM)을 기반으로 Docker 컨테이너 런타임을 제공하며, Docker Desktop의 핵심 기능을 대체할 수 있습니다.

<br />

macOS에는 컨테이너 기술의 기반이 되는 리눅스 커널이 없기 때문에, __Colima는 lima (Linux on Mac)라는 기술을 사용하여 작고 빠른 리눅스 VM을 생성하고 그 위에서 Docker 환경을 구동합니다.__

<br />
<br />
<br />

## 3. Colima 특징 및 장점

<br />

Docker Desktop의 훌륭한 대안으로 Colima를 선택하게 된 이유는 다음과 같습니다.

<br />

__무료 오픈소스__
개인은 물론 기업 사용자까지 라이선스 비용 걱정 없이 자유롭게 사용할 수 있어, 유료화 정책의 완벽한 해결책이 됩니다.

<br />

__가볍고 빠른 성능__
CLI명령줄 인터페이스 기반으로 동작하여 Docker Desktop에 비해 메모리와 CPU 점유율이 현저히 낮습니다.

<br />

__완벽한 Docker CLI 호환성__
기존에 사용하던 docker build, docker run, docker-compose 등 모든 Docker CLI 명령어를 아무런 변경 없이 그대로 사용할 수 있습니다.

<br />

__Apple Silicon 완벽 지원__
Apple Silicon (M1/M2/M3..) 칩셋이 탑재된 최신 Mac 환경에서도 안정적으로 동작합니다. 

<br />

__간단한 사용법과 유연성__
colima start, colima stop 같은 몇 가지 간단한 명령어로 가상 환경을 쉽게 관리할 수 있습니다.
Docker뿐만 아니라 Containerd와 같은 다른 컨테이너 런타임도 지원합니다.

<br />
<br />
<br />

## 4. Docker Desktop 삭제 

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/DevOps/colima_%EC%A0%84%ED%99%98/img/docker_desktop.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

Docker Desktop이 설치된 상태에서 Docker Desktop을 종료하고 Colima를 실행하면 credential 문제가 있어서 Docker Desktop을 제거하고 Colima를 설치하였습니다. 

<br />

Docker Desktop → Preference → Troubleshoot → Uninstall Docker Desktop 에서 Uninstall 클릭하여 제거해줍시다.

<br />
<br />
<br />


## 5. Colima 설치

<br />

qemu와 colima를 설치합시다. qemu를 설치하지 않은 상태에서 colima를 실행하려면 실행하라고 알림이 나옵니다.

```sh
$ brew install qemu
$ brew install colima
```

혹시 도커를 설치하지 않았다면 docker를 설치해 줍시다.

```sh
$ brew install docker
```

<br />
<br />
<br />

## 6. Colima 사용법

<br />

### Colima 실행 

<br />

__Colima 실행__
```
$ colima start 
```

<br />

__Colima 실행 상태 확인__
```
$ colima status

>> 
INFO[0000] colima is running using QEMU                 
INFO[0000] arch: aarch64                                
INFO[0000] runtime: docker                              
INFO[0000] mountType: sshfs                             
INFO[0000] socket: unix:///Users/user/.colima/default/docker.sock
```

<br />

__Colima 중지__
```sh
$ colima stop
```

<br />

__Colima 환경 삭제__
```sh
$ colima delete
```

<br />

MacBook 재시작시 colima를 자동 실행 시킬 수 있습니다.

```sh
brew services start colima
```

<br />

사용중에 credential 문제가 있으면 현재 도커를 로그아웃하고 재로그인해 줍시다.

```sh
$ docker logout 
$ docker login 
```

<br />
<br />
<br />

## 7. Colima 옵션

<br />

Colima는 colima start 명령어만으로도 동작하지만, 몇 가지 옵션을 추가하면 훨씬 더 강력하고 개인화된 개발 환경을 구축할 수 있습니다. 

```sh
colima start \
  --profile default \
  --activate \
  --arch aarch64 \
  --cpu 4 \
  --memory 8 \
  --disk 40 \
  --mount ${HOME}:w \
  --mount-inotify \
  --ssh-agent \
  --vm-type vz \
  --vz-rosetta \
  --verbose
```

<br />

### 기본 설정
__--profile default__
Colima는 여러 개의 가상 머신(프로필)을 동시에 관리할 수 있습니다. 이 옵션은 default라는 이름의 프로필을 생성하고 시작하겠다는 의미입니다.

<br />

__--activate__
Colima 가상 머신이 시작된 후, Docker 컨텍스트를 해당 프로필로 자동으로 활성화(전환)합니다. 이 옵션으로 별도의 docker context use colima 명령어 없이 바로 docker 명령어를 사용할 수 있습니다.

<br />
<br />
<br />

### 리소스 할당

<br />

__--arch aarch64__
가상 머신의 아키텍처를 지정합니다. aarch64는 Apple Silicon (M1/M2/M3 등)을 위한 설정입니다. (인텔 Mac의 경우 x86_64)

<br />

__--cpu 4__
가상 머신에 할당할 CPU 코어 수를 4개로 설정합니다.

<br />

__--memory 8__
가상 머신에 할당할 메모리를 8GB로 설정합니다.

<br />

__--disk 40__
가상 머신이 사용할 디스크의 최대 크기를 40GB로 설정합니다.

<br />
<br />
<br />

### 파일 시스템 연동

<br />

__--mount ${HOME}:w:__
macOS의 홈 디렉터리(~)를 가상 머신 내부에 읽고 쓰기 가능한(w) 상태로 마운트합니다. 이를 통해 컨테이너에서 macOS의 파일에 직접 접근할 수 있어 매우 편리합니다.

<br />

__--mount-inotify__
마운트된 디렉터리에서 파일 변경이 발생했을 때, 이를 감지하는 inotify 이벤트를 활성화합니다. Next.js, Vite 등 Hot-Reloading 기능이 있는 프레임워크를 사용할 때 필수적인 옵션입니다.

<br />
<br />
<br />

### 고급 기능

<br />

__--ssh-agent__

macOS의 SSH Agent를 가상 머신과 공유합니다. 이를 통해 컨테이너 내부에서 git clone 등 SSH 키가 필요한 작업을 별도의 키 복사 없이 수행할 수 있습니다.

__--vm-type vz__
가상화 프레임워크로 macOS에 내장된 고성능 Virtualization.framework를 사용하도록 지정합니다. (Apple Silicon 전용)

__--vz-rosetta__
vz 타입 VM에서 Rosetta 2를 활성화합니다. 이를 통해 aarch64 아키텍처의 가상 머신에서도 x86_64 아키텍처의 도커 이미지를 실행할 수 있습니다.

__--verbose__
Colima가 시작되는 동안 상세한 로그를 출력합니다. 문제가 발생했을 때 원인을 파악하는 데 도움이 됩니다.

<br />
<br />
<br />

## 8. 마치며

<br />

Colima로의 전환은 단순히 비용을 절감하는 것을 넘어, 더 가볍고 효율적인 개발 환경을 구축하는 계기가 되었습니다.

<br />

Docker Desktop 유료화로 인해 대안을 찾고 있는 macOS 개발자라면, Colima는 가장 먼저 고려해야 할 선택지입니다.

<br />

각자의 개발 환경에 맞는 도구를 잘 선택하시기를 바라겠습니다^^

<br />

<><

<br />
<br />

