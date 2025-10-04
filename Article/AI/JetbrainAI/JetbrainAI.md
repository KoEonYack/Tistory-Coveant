<!-- 

인텔리제이에서 AI를 JetBrains AI Pro(AI Assistant + Junie)에 대하여

-->

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/cover.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<br />
<br />

## 시작하며

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/news.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<center>
<a href="https://www.asiae.co.kr/article/2025061808243900876">네이버, 전사 AI 코딩 도구 '커서' 쓴다…이해진과 만난 커서 CEO</a>
</center>
<br />

작년 말부터 조금씩 생성형AI를 활용한 업무가 가능하게 되더니 커서 도입을 시작으로 회사에서도 생성형 AI를 적극적으로 지원하였습니다.

커서를 열심히 사용하였지만 인텔리제이 환경과 통합할 수 없고 별도의 커서 앱에서 작업하고, 인텔리제이에서 확인의 과정을 반복하였습니다.

이 과정이 번거로웠던차에 회사에서 젯브레인 AI를 지원해주어서 사용해보고 글을 작성해 봅니다. 

<br />
<br />
<br />

## JetBrains AI란?

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/home.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<center>
https://www.jetbrains.com/ai-ides/
</center>
<br />

젯브레인에서 제공하는 AI의 정식 명칭은 JetBrains AI Pro이며 백엔드 개발자에게는 AI Assistant과 Junie이라는 두 가지 제품이 있다고 생각하시면 됩니다. 

<br />

각각의 역할은 아래와 같습니다.

<br />

- AI Assistant: 개발 워크플로에서 기본 제공되는 지능형 코딩 어시스턴트
- Junie: 자율적으로 또는 개발자와 협력하여 작업을 처리하도록 설계된 AI 기반 코딩 에이전트

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/price.jpg?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<center>
<a href="https://www.jetbrains.com/ko-kr/ai-ides/buy/?section=personal&billing=yearly">JetBrains AI 요금제 및 가격</a>
</center>
<br />

다른 생성형 AI보다 상품이 좀 더 복잡하며, 가격이 다소 비쌉니다. 회사 지원이 없다면 사용하지는 않았을 것 같습니다.

<br />
<br />
<br />

## 사용하는 법

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/market.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<center>
https://plugins.jetbrains.com/plugin/22282-jetbrains-ai-assistant
</center>
<br />

AI Assistant를 사용하려면 IntelliJ IDEA 2024.3 버전 이상이 필요합니다. 구 버전을 사용하고 있다면 인텔리제이 최신 버전으로 업데이트를 해줍시다.

<br />

JetBrains 마켓플레이스에서 AI Assistant의 평점은 2점 초반대로 다소 낮은 편입니다. 리뷰를 살펴보면 주로 다음과 같은 불만 사항이 많습니다.

<br />

- 크레딧 부족 현상
- 응답 속도 느림

<br />

이러한 부정적인 평가는 구독 플랜의 차이에서 발생하는 문제로 보입니다. 크래딧을 다 사용한 경우 다른 생성형AI와 다르게 AI를 사용할 수 없습니다. All Products Pack 구독자보다 낮은 등급의 플랜을 사용하는 사용자들이 상대적으로 부족한 크레딧으로 인해 불편을 겪는 경우가 많은 것으로 추정됩니다.

<br />
<br />
<br />

<br />

## 데이터 학습

<br />

엔터프라이스 환경에서 AI 코딩 도구를 도입하기 전 가장 먼저 드는 고민은 "우리가 작성한 코드가 AI 모델 학습에 사용되지는 않을까?" 하는 보안 문제일 것입니다.

<br />

JetBrains AI Assistant는 안심하고 엔터프라이즈 환경에서 사용해도 좋습니다.

<br />

JetBrains는 공식 FAQ를 통해 사용자의 데이터 처리 방식에 대해 명확히 밝히고 있습니다.

<br />

__Q. JetBrains 또는 LLM 서비스 제공업체는 사용자가 제출한 데이터를 저장하나요?__

A. JetBrains는 AI 플랫폼을 통해 전송되는 입력 및 출력 데이터는 물론, JetBrains가 직접 호스팅하는 LLM이 처리하는 데이터에 대해서도 **'제로 데이터 보존 정책(Zero Data Retention Policy)'**을 적용합니다. 사용자가 제출한 데이터는 **그 어떤 것도 저장되지 않습니다.**

<br />

JetBrains나 외부 LLM 제공업체의 서버에 남아있지 않으며, AI 모델을 학습시키는 데에도 사용되지 않는다는 의미입니다. 보안 걱정 없이 JetBrains AI Assistant의 강력한 기능들을 활용할 수 있습니다.

<br />
<br />
<br />

## 사용량

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/quota.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="50%" >
<br />

JetBrains AI Pro는 크레딧 기반으로 서비스 사용량을 측정합니다. 현재 공식 문서에 정확한 크레딧 할당량이 명시되어 있지는 않지만, 커뮤니티를 찾아보았더니 각 플랜별 크레딧은 다음과 같은 비율로 추정됩니다.

<br />

- AI Pro: AI Free 플랜의 약 10배
- AI Ultimate: AI Pro 플랜의 약 4배

<br />

실제 사용 시 기능에 따라 크레딧 소모량에 큰 차이가 있었습니다.

<br />

AI Assistant의 핵심 기능인 채팅, 코드 완성, 간단한 리팩터링 등은 크레딧 소모량이 매우 적어 거의 체감되지 않는 수준입니다. 일상적인 개발 과정에서는 크레딧 부족을 걱정하지 않고 자유롭게 사용이 가능했습니다.

<br />

반면, 프로젝트 단위의 컨텍스트를 깊이 있게 분석하고 코드를 생성하는 Junie 기능은 상당한 양의 크레딧을 소모합니다.

<br />

실제 테스트 결과, __Junie를 사용하여 간단한 CRUD API 생성을 요청했을 때 약 3~5%의 크레딧이 한 번에 차감되는 것을 확인할 수 있었습니다__ 이는 Junie가 단순 코드 생성을 넘어, 프로젝트 구조와 전체적인 맥락을 이해하는 복잡한 작업을 수행하기 때문으로 보입니다.

<br />

꼭 필요하지 않다면 Junie 사용을 안하는게 좋아보입니다.

<br />

로컬 환경이나 JetBrains가 자체적으로 호스팅하는 경량 모델을 사용하므로, AI 크레딧을 전혀 소모하지 않습니다.

<br />

- 코드 완성 (Code Completion): 코드를 입력할 때 자동으로 다음 코드를 추천해주는 기능
- 다음 편집 제안 (Next Edit Suggestions): 현재 코드에서 다음에 수정할 부분을 예측하여 제안하는 기능

<br />

눈여겨 볼것은 공식 <a href="https://lp.jetbrains.com/ai-ides-faq/">FAQ</a>의 내용입니다.

<br />

__Q. 할당량이 떨어지면 어떻게 되나요?__

<br />

A. 월간 클라우드 할당량이 소진되면 추가 보충 크레딧을 구매할 수 있습니다. 그렇지 않으면 __할당량이 재설정될 때까지 모든 클라우드 기반 기능이 일시 중지됩니다.__ AI 할당량 재설정은 무료 또는 유료로 JetBrains AI 라이선스를 처음 활성화한 날로부터 30일마다 이루어집니다. 이것이 반드시 청구 주기의 시작과 일치하지 않을 수도 있습니다.

<br />

🌟 __다른 생성형 AI와 다르게 크래딧을 사용하면 아에 사용이 안됩니다.__

<br />
<br />
<br />

## 모델 설정

<br />

### AI Assistant

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/model1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="50%" >
<br />

커서에서 많이본 방식으로 채팅창에서 변경하면 됩니다. Sonnet 4.5가 나온지 얼마 안되어서 그런지 아직은 4 버전이 최대입니다.

<br />
<br />
<br />

### Junie
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/AI/JetbrainAI/img/model2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />

Juine은 Settings에서 모델을 선택할 수 있습니다. GPT5가 무난무난한것 같습니다.

<br />
<br />
<br />

## 가이드라인

<br />

AI Assistant에게 사용자가 원하는 방향으로 작업을 수행하도록 규칙을 지정할 수 있는 강력한 기능을 제공합니다. Cursor처럼, AI의 실행 계획이나 작업 스타일에 대한 가이드라인을 직접 설정할 수 있습니다.

<br />

프로젝트의 루트(root) 디렉터리에 guidelines.md 라는 이름의 마크다운 파일을 생성하고, 그 안에 원하는 규칙을 텍스트로 입력하면 됩니다.

<br />

```text
- 테스트 코드는 반드시 JUnit 5와 AssertJ를 사용해서 작성해 주세요.
- 이 프로젝트에서는 Lombok 사용을 지양하고, getter/setter를 직접 생성해 주세요.
```

<br />

AI Assistant는 작업을 수행하기 전에 이 파일의 내용을 먼저 참고하여 사용자의 요구사항과 프로젝트의 컨벤션을 최대한 준수하려고 노력합니다.

<br />
<br />
<br />

## 상세 사용법

<br />

AI Assistant의 상세한 사용법을 공유해 드릴까 했지만, JetBrains에서 워낙 훌륭한 공식 문서를 제공하고 있어 그럴 필요를 느끼지 못했습니다.

<br />

AI Assistant의 기능과 활용법에 대해 궁금하시다면, 아래 공식 문서를 정독하시는 것을 강력히 추천합니다. 어떤 기술 블로그보다도 더 정확하고 상세한 정보를 얻으실 수 있습니다.

<br />

<a href="https://www.jetbrains.com/help/ai-assistant/code-completion.html "> JetBrains AI Assistant 공식 문서 바로가기 </a>

<br />
<br />
<br />

## 간단한 후기

<br />

2주 정도 사용해 본 결과, JetBrains AI Assistant의 핵심적인 특징은 다음과 같습니다.

<br />

JetBrains가 자체 개발한 모델이 아닌, OpenAI, Claude LLM을 기반으로 동작합니다.

<br />

이는 기존에 사용하던 Cursor와 사실상 동일한 기반 모델을 사용한다는 의미이기도 합니다. 실제로 동일한 질문을 했을 때, 두 도구는 거의 유사한 답변과 코드 생성 결과를 보여주었습니다.

<br />

따라서 기존에 Cursor를 사용해 보셨던 분이라면, JetBrains AI Assistant 역시 인텔리제이 상에서 익숙한 환경에서 AI의 도움을 경험하실 수 있을 것입니다.

<br />
<br />
<br />
