<!-- 

npm, yarn에서 pnpm으로 넘어가세요!

-->


## 시작하며 


<br />



<br />
<br />
<br />


## 패키지 매니저란?

<br />

현대적인 개발 환경에서는 프로그래밍 언어를 막론하고 패키지 매니저(Package Manager) 사용합니다. 패키지 매니저는 이름 그대로 프로젝트에 필요한 각종 패키지(라이브러리, 프레임워크 등)를 효율적으로 관리해 주는 도구입니다.

<br />

개발에 필요한 모든 코드를 직접 작성할 수 없기에 우리는 수많은 오픈소스 라이브러리를 사용합니다. 이때 패키지 매니저가 없다면, 필요한 라이브러리를 하나씩 수동으로 다운로드하고, 버전별로 충돌이 나지 않도록 관리하는 복잡하고 번거로운 작업을 반복해야 할 것입니다.

<br />

패키지 매니저는 이러한 불편함을 해결하고 개발자가 핵심 비즈니스 로직에만 집중할 수 있도록 다음과 같은 중요한 역할들을 수행합니다.

<br />

__의존성 관리 (Dependency Management)__
하나의 라이브러리는 종종 다른 여러 라이브러리를 필요로 합니다. 패키지 매니저는 이러한 복잡한 의존성 관계를 자동으로 파악하여, 필요한 모든 프로그램을 알아서 설치해 줍니다.

<br />

__버전 관리 (Version Management)__
"프로젝트 A에서는 X 라이브러리 1.0 버전을, 프로젝트 B에서는 2.0 버전을 사용"하는 것처럼, 각 프로젝트에 맞는 정확한 버전의 라이브러리를 설치하고 관리할 수 있게 해줍니다.

<br />

__설치 및 업데이트 자동화 (Automation)__
install, update와 같은 간단한 명령어 하나만으로 수십 개의 패키지를 한 번에 설치하거나 최신 상태로 업데이트할 수 있습니다.

<br />

__보안 관리 (Security)__
설치하려는 패키지가 신뢰할 수 있는지, 중간에 코드가 손상되지는 않았는지 등을 검증하여 잠재적인 보안 위협을 줄여줍니다.

<br />

__일관성 유지 (Consistency)__
package-lock.json과 같은 lock 파일을 통해, 여러 개발자가 협업하는 환경에서 모두가 동일한 버전의 패키지를 사용하도록 보장하여 "내 컴퓨터에서는 됐는데, 왜 팀원 자리에서는 안 되지?"와 같은 문제를 방지합니다.

<br />

각 프로그래밍 언어 생태계는 저마다 활발하게 사용되는 대표적인 패키지 매니저를 가지고 있습니다.

| 언어	| 대표 패키지 매니저 |
---|--------
| Java	| Maven, Gradle |
| JavaScript |	npm, Yarn, pnpm |
Python |	pip, Conda | 
Ruby | 	Gem
Go | 	Go Modules

<br />
<br />
<br />

## 단점이 있는 npm

<br />
<img src="./img/raddit.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<center>
<a href="https://www.reddit.com/r/ProgrammerHumor/comments/6s0wov/heaviest_objects_in_the_universe/"> Heaviest Objects In The Universe </a>
</center>
<br />

우주에서 가장 무거운 천체는 무엇일까요? 태양, 중성자별, 그리고 블랙홀을 떠올릴 겁니다. 하지만 개발자들 사이에서는 유머로 우주의 행성보다 더 무겁고 깊은 존재가 있는데 node_modules 디렉터리입니다.

<br />

간단한 웹 프로젝트 하나를 시작했을 뿐인데, 어느새 수백 MB, GB에 육박하는 node_modules 폴더를 경험하게 됩니다.

<br />

npm은 자바스크립트 생태계를 풍성하게 만들어주었지만 그 이면에는 이처럼 디스크 공간을 무한정 차지하고, 수많은 파일로 인해 설치 속도를 저하시키는 고질적인 문제가 존재합니다

<br />
<br />
<br />

### npm 단점. 유령 의존성

<br />

__v3에서 도입된 '플랫 의존성(Flattened Dependency)'이란?__

<br />

pnpm의 필요성을 이해하려면, 먼저 npm이 걸어온 길을 살펴볼 필요가 있습니다. 특히 npm v2에서 v3으로 넘어가면서 적용된 플랫 의존성(Flattened Dependency)이 도입되었습니다. 

<br />

__npm v2의 문제점: 중첩된 node_modules의 지옥__

<br />

초기 npm v2 시절에는 node_modules가 매우 직관적인 트리 구조로 설치되었습니다. 내 프로젝트가 A 라이브러리를 사용하고, A가 B를 사용한다면 아래와 같은 구조가 만들어졌죠.

```text
node_modules
└── A
    └── node_modules
        └── B
```
이 방식은 두 가지 심각한 문제를 낳았습니다.

<br />

__엄청난 디스크 공간 낭비__

<br />

만약 다른 라이브러리 C도 B를 사용한다면, 내 node_modules 안에는 B의 복사본이 두 개나 생기게 됩니다. 프로젝트가 커질수록 동일한 패키지가 수십, 수백 개씩 중복으로 설치되어 디스크를 낭비하게 됩니다. 

<br />
<br />

__파일 경로 길이 제한 문제__

<br />

의존성 단계가 깊어질수록 파일 경로는 계속해서 길어졌습니다. 파일 경로 길이에 제한이 있는 윈도우 환경에서 npm install 자체가 실패하는 치명적인 오류를 유발했습니다.

<br />
<br />
<br />

__npm v3의 해결책: 플랫 의존성__

<br />

이 문제를 해결하기 위해 npm v3는 의존성을 최대한 끌어올려 평평하게 만드는 플랫 의존성 구조를 도입했습니다.

<br />

```text
node_modules
├── A
├── B  <-- A와 C가 사용하는 B를 최상단으로 끌어올림(Hoisting)
└── C
```

<br />

A와 C가 공통으로 사용하는 B를 node_modules의 최상단에 설치함으로써, 중복 설치와 파일 경로 문제를 한 번에 해결한 것입니다.

<br />
<br />
<br />

__새로운 문제의 탄생: 유령 의존성 (Phantom Dependencies)__

<br />

하지만 이 해결책은 또 다른 문제를 낳았습니다. 바로 **'유령 의존성(Phantom Dependencies)'**입니다.

<br />

위 구조에서 B 라이브러리는 내 프로젝트의 package.json에 명시적으로 추가한 적이 없습니다. 오직 A와 C의 의존성일 뿐이죠. 하지만 B가 최상단에 설치되면서, 정작 내 프로젝트 코드에서도 B를 마음대로 import하여 사용할 수 있게 되었습니다.

<br />
<br />
<br />

__이것이 왜 문제일까요?__

<br />

예를들어서 express만 설치했는데, express가 내부적으로 사용하는 debug라는 라이브러리를 내 코드에서 require('debug')로 불러 쓸 수 있습니다. 만약 나중에 express가 debug를 더 이상 사용하지 않게 되면, 내 코드는 갑자기 오류를 일으키며 망가집니다.

<br />
<br />
<br />

### npm 단점. 거대한 node_modules

<br />

위에서 보았던 행성보다 무거운 node_moduels 이미지를 다시 생각해보세요. npm은 프로젝트마다 필요한 모든 패키지를 node_modules 폴더 안에 전부 복사해서 저장합니다. 이 방식은 여러 프로젝트에서 동일한 패키지를 사용하더라도 매번 중복된 사본을 만들어내어 심각한 디스크 공간 낭비를 유발합니다.

<br />

예를 들어, Next.js 공식 문서에서 안내하는 create-next-app으로 간단한 프로젝트를 생성하면, package.json에는 10여 개의 의존성만 보입니다. 하지만 실제 node_modules에는 수백 개의 패키지가 설치되고, 그 용량은 수백 메가바이트(MB)를 훌쩍 넘기기 일쑤입니다. 이 때문에 node_modules는 버전 관리 대상에서 언제나 git 버전 관리에서 제외하는(gitignore) 첫 번째 폴더가 되었습니다.

<br />
<br />
<br />

## npm 단점. 복잡하고 느린 의존성 해석 (평탄화 알고리즘)

<br />

npm v3부터 도입된 플랫 의존성(Flattened Dependency) 구조는 과거의 중첩 경로 문제를 해결했지만, 새로운 비용을 발생시켰습니다. 바로 의존성을 해석하는 시간입니다.

<br />

npm install을 실행하면, npm은 프로젝트의 모든 의존성을 분석하여 어떤 패키지를 node_modules 최상단으로 끌어올릴지(hoisting) 결정하는 복잡한 계산을 수행합니다. 프로젝트의 규모가 커지고 의존성이 많아질수록, 이 평탄화 알고리즘은 눈에 띄게 느려지며 전체 설치 시간을 지연시키는 원인이 됩니다.

<br />
<br />
<br />

## pnpm이란?

<br />

2017년, Zoltan Kochan이라는 개발자는 기존 패키지 매니저의 비효율성을 개선하기 위해 **performant npm(고성능 npm)** 의 약자인 pnpm을 공개하였습니다.

<br />

pnpm은 기존 npm과 유사한 구조를 유지하면서도, 몇 가지 핵심적인 차별점을 통해 디스크 공간 효율성과 설치 속도를 획기적으로 개선했습니다.

<br />
<br />
<br />

### pnpm 장점. 패키지 저장 방식의 혁신: 디스크 공간 효율화

<br />

npm의 가장 큰 단점 중 하나는, 모든 프로젝트마다 node_modules 폴더에 패키지 전체를 복사하여 저장한다는 점입니다. 이는 심각한 디스크 공간 낭비를 유발합니다.

<br />

pnpm은 이 문제를 **'전역 저장소(global store)'**와 **'심볼릭 링크(symbolic link)'**라는 두 가지 개념으로 해결합니다. 

<br />

이 내용을 쉽게 설명하기 위해서 AI의 도움을 받았는데 이렇게 설명합니다.

<br />

npm이 프로젝트마다 필요한 책을 일일이 복사해서 쌓아두는 '복사기'라면, pnpm은 중앙에 거대한 '도서관'을 짓고 각 프로젝트에는 책의 위치만 알려주는 '바로가기'를 만들어주는 방식입니다.

<br />

__전역 저장소__

<br />

pnpm은 모든 패키지를 컴퓨터의 특정 공간(전역 저장소)에 단 한 번만 저장합니다. 

<br />
<img src="./img/pnpm_global.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

```shell
$ pnpm store path 

ex. /Users/<사용자이름>/<중간 경로>/.pnpm-store/v3
```

위의 명령어로 pnpm의 앙 저장소의 전체 경로를 알 수 있습니다. 경로에 이상한 문자가 길게 있는것을 볼 수 있습니다. 궁금하신 분들을 위해서 조금 더 자세한 이야기를 작성해보겠습니다.


<br />

__좀 더 자세한 이야기__

<br />

pnpm이 어떻게 디스크 공간을 획기적으로 절약하는지 조금 더 자세하게 보겠습니다. 이를 위해서 콘텐츠 주소 지정(Content-Addressable Storage, CAS)를 살펴봐야 합니다.

<br />

기존의 파일 시스템은 파일의 이름이나 위치를 기반으로 파일을 찾아갑니다. 반면, CAS는 파일의 내용(content) 자체를 기반으로 주소를 지정하는 방식입니다.

<br />

상에 수많은 김철수라는 이름의 사람이 있지만, 모든 사람의 지문은 저마다 고유합니다. CAS는 파일의 이름을 김철수로 보지 않고, 파일의 내용을 분석하여 만들어낸 고유한 지문(해시 값)으로 파일을 식별하고 저장합니다.

<br />

pnpm은 설치하려는 모든 패키지 파일의 내용을 해시 함수(hash function)로 분석하여, 파일마다 고유한 암호화된 주소 값을 부여합니다.

<br />

이 방식의 가장 큰 장점은 파일의 내용이 100% 동일하다면, 파일 이름이나 버전이 달라도 항상 동일한 지문을 갖게 된다는 점입니다.

<br />

예시를 들어 보겠습니다.

<br />

프로젝트 A와 프로젝트 B가 lodash 라이브러리의 동일한 버전을 사용한다고 가정해 봅시다.

<br />

npm은 A와 B 프로젝트에 각각 lodash 파일 복사본을 만들어 동일한 파일이 생깁니다.

<br />

pnpm은 lodash 파일들의 '지문'을 확인하고, 이미 중앙 저장소에 동일한 지문의 파일이 있음을 인지합니다. 그 후, 파일을 복사하는 대신 해당 지문(주소)을 가리키는 링크만 생성합니다. (동일한 파일 1개 + 링크 2개)

<br />

pnpm은 CAS 방식을 통해 여러 프로젝트에서 동일한 패키지를 사용할 때 불필요한 사본을 만들지 않고 단 하나의 원본 파일로 모든 것을 관리합니다. pnpm이 디스크 공간을 압도적으로 효율적으로 사용하는 핵심 비결입니다.

<br />
<br />
<br />

__심볼릭 링크 참조__

<br />
<img src="./img/pnpm_local.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

여러 프로젝트에서 동일한 패키지를 필요로 할 경우, 파일을 복사하는 대신 전역 저장소에 있는 원본을 가리키는 바로가기(심볼릭 링크)만 생성합니다. 위의 스크린샷에서 화살표 표시가 심볼릭 링크 표시입니다.

<br />

이렇게 pnpm은 npm의 유령 의존성을 심볼릭 링크(Symbolic Link)를 활용한 비평탄화 구조로 해결하였습니다.

<br />
<br />
<br />

__설치 과정 생략__

만약 설치하려는 패키지가 전역 저장소에 이미 존재한다면, 다운로드 과정을 완전히 생략하고 링크만 만들기 때문에 설치가 매우 빨라집니다. 

<br />
<br />
<br />

### pnpm 장점 2. 획기적인 속도 향상: npm보다 약 2배 빠른 성능

<br />

pnpm은 설치 프로세스를 최적화하여 npm보다 약 2배 빠른 성능을 보여줍니다. 

<br />

__병렬 설치 지원__

<br />

npm과 달리 패키지별 설치 순서를 복잡하게 계산할 필요가 없어, 여러 패키지를 동시에 병렬로 설치할 수 있습니다. 

<br />

예를 들어 프로젝트에 react, lodash, axios라는 3개의 독립적인 패키지를 설치한다고 가정해 봅시다.

<br />

__npm의 동작 방식__

<br />

- react 다운로드 및 설치
- (react가 끝나면) lodash 다운로드 및 설치
- (lodash가 끝나면) axios 다운로드 및 설치

총 소요 시간 = react 설치 시간 + lodash 설치 시간 + axios 설치 시간

<br />
<br />

__pnpm의 동작 방식__

<br />

- react, lodash, axios를 동시에 다운로드 및 설치 시작

총 소요 시간 = 셋 중 가장 오래 걸리는 패키지의 설치 시간

<br />

__단순한 링크 작업__

<br />

모든 패키지를 설치한 후, 프로젝트의 의존성 트리에 맞게 링크만 걸어주는 간단한 방식으로 동작하기 때문에 전체 프로세스가 매우 빠릅니다.

<br />
<br />
<br />

## 패키지 매니저별 성능 비교

<br />

npm, pnpm, Yarn. 패키지 매니저 중에서 실제 어떤 것이 가장 빠르고 효율적인지 확인하기 위하여 패키지 매니저들의 다양한 상황을 가정하여 성능을 비교하는 벤치마크 테스트를 pnpm 사이트에 공개하고 있습니다. <a href="https://pnpm.io/ko/benchmarks"> (링크) </a>

<br />
<img src="./img/benchmark1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<img src="./img/benchmark2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

이미지에 작성된 테스트 항목은 아래의 상황입니다.

<br />

**1. clean install (클린 설치)**
- **상황**: **완전히 새로운 환경에서 프로젝트를 처음 시작**하는 경우입니다.
- **조건**: node_modules 폴더, package-lock.json과 같은 lock 파일, 그리고 패키지 매니저의 캐시까지 아무것도 없는 상태에서 install 명령어를 실행합니다.
- **의미**: 패키지 매니저의 순수한 다운로드 및 설치 성능을 측정하는 가장 기본적인 지표입니다.

<br />

**2. with lockfile (CI 서버 환경)**
- **상황**: **Jenkins, GitHub Actions와 같은 CI/CD 서버에서 빌드를 실행**하는 경우와 가장 유사합니다.
- **조건**: package-lock.json이나 pnpm-lock.yaml 같은 lock 파일은 있지만, 캐시나 node_modules는 없는 상태입니다.
- **의미**: lock 파일을 통해 정해진 버전의 패키지들을 얼마나 빨리 설치하는지 측정합니다.

<br />

**3. with cache, with lockfile (동료 개발자 환경)**
- **상황**: **팀 동료가 Git에서 프로젝트를 클론받아 처음 install 명령어를 실행**하는 경우입니다.
- **조건**: lock 파일이 있으며, 이전에 다른 프로젝트를 통해 설치했던 패키지들이 캐시에 남아있는 상태입니다.
- **의미**: 캐시를 얼마나 효율적으로 활용하여 설치 속도를 높이는지 측정합니다.

<br />

**4. with cache, with lockfile, with node_modules (반복 설치)**
- **상황**: **이미 설치가 완료된 프로젝트에서 install 명령어를 다시 실행**하는 경우입니다.
- **조건**: lock 파일, 캐시, node_modules가 모두 존재하는 완벽한 상태입니다.
- **의미**: 변경 사항이 없을 때, 얼마나 빠르게 "변경 없음"을 확인하고 작업을 마치는지를 측정합니다. (이상적으로는 0초에 가까워야 합니다.)

<br />

**5. update (의존성 업데이트)**
- **상황**: **라이브러리 버전을 올리는 등 package.json을 수정한 후 install을 실행**하는 경우입니다.
- **조건**: 기존 설치 상태에서 package.json의 버전 정보만 변경됩니다.
- **의미**: 변경된 의존성을 얼마나 빠르고 정확하게 감지하고 업데이트하는지를 측정합니다.

<br />
<br />
<br />

## pnpm 전환 사례

<br />



<br />
<br />
<br />

