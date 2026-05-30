
# 청킹 전략: 검색 품질을 결정하는 문서 분할 전략

# 전체 흐름

<br />

<img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/AI/RAG-%EC%8A%A4%ED%84%B0%EB%94%94-02-chunk/img/1779697633853_fb67afc7_0.jpeg" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >

<br />


## 시작하며

RAG 시스템을 설계할 때 많은 사람들이 임베딩 모델이나 벡터 데이터베이스 선택에 집중합니다. 하지만 실제 검색 품질을 크게 좌우하는 요소 중 하나는 문서를 어떻게 나누는지, 즉 **Chunking Strategy**입니다.

Chunking은 큰 문서를 작은 단위로 나누는 과정입니다. RAG에서는 사용자의 질문과 가장 관련 있는 chunk를 검색한 뒤, 그 내용을 LLM에 전달해 답변을 생성합니다. 따라서 chunk가 어떻게 만들어졌는지에 따라 검색 결과의 품질과 최종 답변의 정확도가 달라집니다.

좋은 chunking은 단순히 문서를 잘게 자르는 작업이 아닙니다. 문서의 의미와 구조를 최대한 보존하면서, 검색하기 좋은 크기와 단위로 재구성하는 과정입니다.

---

## 왜 Chunking이 중요한가요?

RAG에서 문서는 보통 그대로 검색되지 않습니다. 긴 문서는 여러 개의 chunk로 나뉘고, 각 chunk는 embedding으로 변환되어 벡터DB에 저장됩니다. 사용자가 질문을 입력하면 질문 역시 임베딩으로 변환되고, 이 질문과 의미적으로 가까운 chunk가 검색됩니다.

**즉, 실제 검색 대상은 문서 전체가 아니라 chunk입니다.**

이 구조에서는 chunk 품질이 검색 품질을 결정합니다. 관련 정보가 하나의 chunk 안에 잘 담겨 있으면 검색 결과가 정확해집니다. 반대로 chunk가 잘못 나뉘면 관련 정보가 흩어지거나, 불필요한 내용이 함께 검색되거나, 문맥이 끊긴 상태로 LLM에 전달될 수 있습니다.



## Chunk가 너무 크면 어떤 문제가 생기나요?

Chunk가 너무 크면 하나의 검색 결과 안에 너무 많은 정보가 들어갑니다. 이 경우 사용자의 질문과 직접 관련 없는 내용까지 함께 LLM에 전달될 가능성이 높아집니다.

예를 들어 제품 매뉴얼에서 로그인 오류 해결 방법을 찾고 싶은데, chunk 안에 로그인, 결제, 계정 삭제, 알림 설정 내용이 모두 섞여 있다면 검색 결과의 초점이 흐려질 수 있습니다.

Chunk가 너무 클 때 발생하는 문제는 다음과 같습니다.

- 불필요한 정보가 함께 검색됩니다.
- LLM 입력 토큰을 많이 사용합니다.
- 질문과 관련된 핵심 정보가 묻힐 수 있습니다.
- 검색 결과의 정확도가 낮아질 수 있습니다.



## Chunk가 너무 작으면 어떤 문제가 생기나요?

반대로 chunk가 너무 작으면 문맥이 부족해집니다. 하나의 chunk만으로는 의미를 이해하기 어려워지고, LLM이 답변에 필요한 배경 정보를 얻지 못할 수 있습니다.

예를 들어 다음과 같은 문장이 있다고 가정해보겠습니다.

이 기능은 관리자 권한이 있는 사용자만 사용할 수 있습니다.

이 문장만 따로 검색되면 이 기능이 무엇인지 알기 어렵습니다. 앞뒤 문맥이 함께 있어야 정확한 의미를 이해할 수 있습니다.

Chunk가 너무 작을 때 발생하는 문제는 다음과 같습니다.

- 문맥이 부족합니다.
- 지시어와 참조 관계를 이해하기 어렵습니다.
- 여러 chunk를 함께 검색해야 답변이 가능해집니다.
- LLM이 잘못된 추론을 할 가능성이 높아집니다.

따라서 chunking의 핵심은 너무 크지도, 너무 작지도 않은 적절한 단위를 찾는 것입니다.


## 좋은 Chunking의 기준은 무엇인가요?

좋은 chunk는 다음 조건을 만족해야 합니다.

- 하나의 chunk가 의미적으로 자연스럽습니다.
- 질문에 답할 수 있는 충분한 문맥을 포함합니다.
- 불필요한 정보가 과도하게 섞이지 않습니다.
- 문서의 구조를 가능한 한 보존합니다.
- 검색 결과로 사용했을 때 출처와 맥락을 추적할 수 있습니다.

결국 좋은 chunking은 검색하기 좋은 단위와 LLM이 이해하기 좋은 단위 사이의 균형을 찾는 일입니다.


## 1. Fixed-size Chunking

<br />

<img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/AI/RAG-%EC%8A%A4%ED%84%B0%EB%94%94-02-chunk/img/1779521829742_26c170c6_0.jpeg" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >

<br />

Fixed-size chunking은 가장 단순한 chunking 방식입니다. 문서를 정해진 길이로 일정하게 나눕니다.

예를 들어 다음과 같이 설정할 수 있습니다.

- chunk size: 500 tokens
- chunk overlap: 50 tokens

이 경우 문서는 500 tokens 단위로 잘리고, 인접한 chunk끼리는 50 tokens 정도를 겹쳐 문맥 단절을 줄입니다.

### 장점

Fixed-size chunking의 가장 큰 장점은 단순함입니다. 구현이 쉽고, 처리 속도가 빠르며, 문서 형식에 크게 의존하지 않습니다. 대량의 문서를 빠르게 처리해야 하거나 RAG 초기 실험을 진행할 때 유용합니다.

### 단점

하지만 문서의 의미나 구조를 고려하지 않기 때문에 문맥이 중간에서 잘릴 수 있습니다. 제목과 본문이 분리되거나, 표의 일부만 chunk에 포함되거나, 하나의 설명이 여러 chunk로 흩어질 수 있습니다.

### 언제 적합한가요?

Fixed-size chunking은 빠른 프로토타입이나 대량 문서의 1차 처리에 적합합니다. 다만 높은 검색 정확도가 필요한 경우에는 다른 전략과 함께 사용하거나 overlap을 적절히 설정하는 것이 좋습니다.


## 2. Recursive Chunking

<br />

<img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/AI/RAG-%EC%8A%A4%ED%84%B0%EB%94%94-02-chunk/img/1779522146281_6b2c6d8a_0.jpeg" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >

<br />

Recursive chunking은 문서를 큰 단위부터 나누고, chunk가 너무 크면 더 작은 단위로 다시 나누는 방식입니다.

예를 들어 다음과 같은 순서로 나눌 수 있습니다.

```
문서 전체
↓
섹션
↓
문단
↓
문장
↓
단어
```

처음에는 문단 단위로 나누고, 문단이 너무 길면 문장 단위로 나눕니다. 문장도 너무 길다면 더 작은 단위로 나누는 식입니다.

### 장점

Recursive chunking은 fixed-size 방식보다 문서 구조를 더 잘 보존합니다. 무조건 길이 기준으로 자르는 것이 아니라, 문단이나 문장 같은 자연스러운 경계를 우선적으로 사용하기 때문입니다.

또한 chunk 크기를 일정 범위 안에서 관리할 수 있어 실무에서 활용하기 좋습니다.

### 단점

문서의 구분자가 불규칙하면 품질이 낮아질 수 있습니다. 예를 들어 줄바꿈이 엉망인 PDF에서 문단 경계를 제대로 찾지 못하면 recursive chunking의 장점이 줄어듭니다.

### 언제 적합한가요?

일반적인 텍스트 문서, 사내 문서, 매뉴얼, 정책 문서처럼 문단 구조가 어느 정도 있는 데이터에 적합합니다. 실무 RAG에서는 기본 전략으로 고려하기 좋은 방식입니다.


## 3. Document-based Chunking

<br />

<img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/AI/RAG-%EC%8A%A4%ED%84%B0%EB%94%94-02-chunk/img/1779521843016_fbb74737_0.jpeg" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >

<br />

Document-based chunking은 문서의 자연스러운 구조를 기준으로 chunk를 만드는 방식입니다.

문서에는 사람이 읽기 쉽도록 만들어진 구조가 있습니다. 제목, 소제목, 섹션, 표, 코드 블록, 리스트 등이 대표적입니다. Document-based chunking은 이런 구조를 기준으로 chunk를 나눕니다.

### 예시 기준
- 제목
- 소제목
- heading
- 섹션
- 표
- 코드 블록
- 리스트
- 문단

### 장점

Document-based chunking은 문서의 논리 구조를 잘 보존합니다. 제목과 본문이 함께 유지될 가능성이 높고, 사람이 읽는 단위와 검색 단위가 비슷해집니다.

예를 들어 기술 문서에서 "설치 방법"이라는 섹션이 있다면, 해당 섹션을 하나의 chunk 또는 여러 개의 하위 chunk로 나눌 수 있습니다. 이렇게 하면 검색 결과가 더 자연스럽고 설명 단위도 명확해집니다.

### 단점

구조가 명확하지 않은 문서에는 적용하기 어렵습니다. 또한 HTML, Markdown, PDF, 코드 파일 등 문서 형식마다 구조를 해석하는 방식이 다르기 때문에 파서가 중요합니다.

### 언제 적합한가요?

Markdown 문서, HTML 페이지, 기술 문서, API 문서, 코드 파일, 구조화된 매뉴얼에 적합합니다.

---

## 4. Semantic Chunking

<br />

<img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/AI/RAG-%EC%8A%A4%ED%84%B0%EB%94%94-02-chunk/img/1779522957503_83efc910_0.jpeg" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >

<br />

Semantic chunking은 의미의 흐름을 기준으로 chunk를 나누는 방식입니다.

이 방식은 단순히 길이나 문서 구조만 보지 않습니다. 문장이나 문단의 embedding을 비교해 의미가 비슷한 내용은 묶고, 의미가 달라지는 지점에서 chunk를 나눕니다.

쉽게 말하면 같은 주제끼리는 하나로 묶고, 주제가 바뀌면 나누는 방식입니다.

### 예시

하나의 문서 안에 다음과 같은 내용이 순서대로 있다고 가정해보겠습니다.

1. 로그인 기능 설명
2. 로그인 오류 해결 방법
3. 결제 수단 등록 방법
4. 결제 실패 시 확인 사항

Semantic chunking은 1번과 2번을 로그인 관련 chunk로 묶고, 3번과 4번을 결제 관련 chunk로 묶는 식으로 동작할 수 있습니다.

### 장점

Semantic chunking은 의미적으로 일관된 chunk를 만들 수 있습니다. 검색 결과가 질문의 의도와 더 잘 맞을 가능성이 높고, chunk 내부의 문맥도 자연스럽게 유지됩니다.

### 단점

embedding 계산이 필요하기 때문에 비용이 추가됩니다. 구현도 fixed-size나 recursive 방식보다 복잡합니다. 또한 의미 변화 지점을 판단하는 임계값을 어떻게 설정하느냐에 따라 결과가 달라질 수 있습니다.

### 언제 적합한가요?

문서 안에서 주제 전환이 자주 발생하거나, 단순 구조만으로는 의미 단위를 나누기 어려운 문서에 적합합니다. 고품질 검색이 중요한 RAG 시스템에서도 고려할 만한 방식입니다.

---

## 5. LLM-based Chunking

<br />

<img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/AI/RAG-%EC%8A%A4%ED%84%B0%EB%94%94-02-chunk/img/1779521852813_7c8622f3_0.jpeg" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >

<br />

LLM-based chunking은 LLM을 사용해 문서를 의미적으로 분석하고, 적절한 chunk를 만드는 방식입니다.

LLM은 문장 간 관계, 의미 단위, 독립적인 주장이나 proposition을 파악할 수 있습니다. 이를 활용하면 단순한 규칙 기반 chunking보다 더 정교하게 문서를 나눌 수 있습니다.

### 장점

LLM-based chunking은 복잡한 문서에서도 의미적으로 자연스러운 chunk를 만들 수 있습니다. 특히 하나의 문서 안에 여러 주제가 섞여 있거나, 문맥 이해가 필요한 경우 강점이 있습니다.

예를 들어 긴 정책 문서에서 조항별 의미를 분리하거나, 보고서에서 주장과 근거를 구분하는 데 활용할 수 있습니다.

### 단점

가장 큰 단점은 비용입니다. 모든 문서를 LLM으로 분석해야 하므로 처리 시간이 오래 걸리고, 대량 문서에는 부담이 큽니다.

또한 LLM 출력이 항상 일관적이지 않을 수 있기 때문에, 운영 환경에서는 결과 검증과 품질 관리가 필요합니다.

### 언제 적합한가요?

중요도가 높은 문서, 양은 많지 않지만 정확도가 중요한 문서, 또는 복잡한 의미 구조를 가진 문서에 적합합니다. 대량 문서 전체에 적용하기보다는 핵심 문서에 선택적으로 사용하는 것이 현실적입니다.



## Chunk Overlap은 왜 필요한가요?

Chunking을 이야기할 때 overlap도 함께 고려해야 합니다.

Overlap은 인접한 chunk 사이에 일부 내용을 겹치게 만드는 방식입니다. 예를 들어 chunk size가 500 tokens이고 overlap이 50 tokens라면, 앞 chunk의 마지막 50 tokens가 다음 chunk의 시작 부분에 다시 포함됩니다.

Overlap을 사용하는 이유는 문맥 단절을 줄이기 위해서입니다. 문서가 특정 지점에서 잘리면 앞뒤 문맥이 분리될 수 있습니다. Overlap은 이 문제를 완화해줍니다.

다만 overlap이 너무 크면 중복 데이터가 많아지고 저장 비용과 검색 비용이 증가합니다. 검색 결과에 비슷한 chunk가 반복해서 나타날 수도 있습니다.

따라서 overlap은 문서 유형과 chunk size에 맞게 조절해야 합니다.



## Chunking 전략은 하나만 선택해야 할까요?

실무에서는 하나의 전략만 고집하기보다 문서 유형에 따라 다른 전략을 조합하는 것이 좋습니다.

예를 들어 다음과 같은 방식이 가능합니다.

- 일반 텍스트 문서는 recursive chunking을 사용합니다.
- Markdown이나 HTML 문서는 document-based chunking을 사용합니다.
- 주제 전환이 많은 긴 문서는 semantic chunking을 사용합니다.
- 중요도가 높은 정책 문서는 LLM-based chunking을 선택적으로 적용합니다.
- 초기 프로토타입에서는 fixed-size chunking으로 빠르게 검증합니다.

RAG 시스템에서 가장 좋은 chunking 전략은 항상 하나로 정해져 있지 않습니다. 문서의 형식, 데이터 규모, 검색 정확도 요구사항, 비용 제약에 따라 달라집니다.



## 전략별 비교

|전략|핵심 아이디어|장점|단점|적합한 경우|
|---|---|---|---|---|
|Fixed-size|일정 길이로 나눕니다|단순하고 빠릅니다|문맥이 잘릴 수 있습니다|초기 실험, 대량 문서|
|Recursive|큰 단위부터 작은 단위로 나눕니다|구조와 크기의 균형이 좋습니다|구분자 품질에 의존합니다|일반 문서, 사내 문서|
|Document-based|문서 구조를 기준으로 나눕니다|논리 구조를 잘 보존합니다|구조 없는 문서에는 어렵습니다|HTML, Markdown, 기술 문서|
|Semantic|의미 변화 지점에서 나눕니다|의미 일관성이 높습니다|비용과 구현 복잡도가 증가합니다|주제 전환이 많은 문서|
|LLM-based|LLM이 의미 단위를 분석합니다|가장 정교합니다|비용이 큽니다|중요 문서, 복잡한 문서|



## 실무에서 Chunking을 설계할 때 확인할 질문

Chunking 전략을 선택할 때는 다음 질문을 먼저 확인하는 것이 좋습니다.

- 문서의 평균 길이는 어느 정도인가요?
- 문서에 제목, 섹션, 표 같은 구조가 있나요?
- 사용자의 질문은 짧은 사실 검색인가요, 긴 설명이 필요한 질문인가요?
- 답변에 필요한 문맥은 보통 어느 정도 길이인가요?
- 검색 결과에 출처를 명확히 보여줘야 하나요?
- 비용과 처리 속도 제약은 어느 정도인가요?
- 중복 검색 결과를 어느 정도 허용할 수 있나요?

이 질문에 대한 답에 따라 chunk size, overlap, chunking 방식, 메타데이터 설계가 달라집니다.



## 예시: 제품 매뉴얼을 Chunking한다면

제품 매뉴얼을 RAG 시스템에 넣는다고 가정해보겠습니다.

매뉴얼은 보통 장, 절, 소제목, 표, 주의사항 등으로 구성되어 있습니다. 이 경우 단순히 1,000자 단위로 자르면 제목과 설명이 분리되거나, 주의사항이 관련 기능 설명과 떨어질 수 있습니다.

더 나은 방식은 먼저 문서 구조를 활용하는 것입니다. 장과 절을 기준으로 나누고, 너무 긴 섹션은 recursive chunking으로 다시 나눕니다. 표가 있다면 표 제목과 함께 chunk에 포함되도록 처리합니다. 또한 제품명, 문서명, 섹션명, 버전, 작성일 같은 메타데이터를 함께 저장합니다.

이렇게 하면 사용자가 특정 기능에 대해 질문했을 때 관련 섹션이 더 정확히 검색되고, LLM은 충분한 문맥을 바탕으로 답변할 수 있습니다.



## 마치며

Chunking은 단순히 긴 문서를 작게 자르는 작업이 아닙니다. RAG 시스템에서 실제로 검색되는 단위를 설계하는 일입니다.

좋은 chunking은 다음을 가능하게 합니다.

- 관련 정보가 더 정확히 검색됩니다.
- LLM에 전달되는 문맥의 품질이 높아집니다.
- 불필요한 토큰 사용을 줄일 수 있습니다.
- 답변의 근거와 출처를 추적하기 쉬워집니다.

결국 chunking 전략은 RAG의 검색 품질과 답변 품질을 함께 결정합니다.

좋은 RAG를 만들고 싶다면 모델과 데이터베이스만 볼 것이 아니라, 문서를 어떤 단위로 나누고 어떤 문맥을 보존할지부터 설계해야 합니다.
