<!--

개발자를 위한 SEO 실전 가이드 1부 - 검색엔진은 내 페이지를 어떻게 찾고 읽을까

-->

## 시작하며

<br />

서비스를 만들고 배포한 뒤에는 이상하게 답답한 순간이 있습니다. 주소를 직접 입력하면 잘 열리고, 목록에도 콘텐츠가 보이는데 검색 결과에서는 아무것도 찾을 수 없는 경우입니다.

<br />

이때 가장 먼저 떠올리는 것은 보통 제목, 키워드, 설명문입니다. 물론 중요합니다. 하지만 그보다 앞에 확인할 일이 있습니다. 검색엔진이 그 URL의 존재를 알고 있는지, 페이지를 읽을 수 있는지, 그리고 읽은 결과를 색인으로 저장했는지입니다.

<br />

이번 글은 SEO를 처음 접하는 개발자를 위해 이 과정을 정리한 첫 번째 글입니다. 순위 비법을 이야기하기보다, 검색엔진이 웹 페이지를 만나는 순서를 먼저 살펴보려고 합니다. 다음 글부터 sitemap, robots.txt, canonical, 메타데이터를 각각 다루겠지만, 이 흐름을 이해하면 설정 하나하나가 왜 필요한지 훨씬 분명해집니다.

<br />
<br />
<br />

## SEO는 검색 결과의 순위만 높이는 일이 아니다

<br />

SEO는 Search Engine Optimization의 약자입니다. 흔히 "검색 순위를 올리는 기술"이라고 설명하지만, 개발자 입장에서는 조금 더 넓게 보는 편이 정확합니다. 검색엔진이 사이트를 발견하고, 읽고, 이해하고, 적절한 검색어에 연결할 수 있게 만드는 작업입니다.

<br />

검색 결과에 노출되기까지의 흐름은 다음처럼 나눌 수 있습니다.

<br />

```text
URL 발견
  -> 크롤링
  -> 렌더링과 내용 해석
  -> 색인
  -> 검색어에 맞는 결과로 노출
```

<br />

Google은 이 흐름을 크롤링, 색인, 검색 결과 제공의 세 단계로 설명합니다. 자바스크립트로 만든 페이지는 그 사이에 렌더링 단계가 한 번 더 중요해집니다. 원본 HTML만으로 내용을 알 수 없다면, 검색엔진은 자바스크립트를 실행해 최종 DOM을 만든 뒤에야 페이지 내용을 이해할 수 있기 때문입니다. [Google의 검색 동작 설명](https://developers.google.com/search/docs/fundamentals/how-search-works)과 [JavaScript SEO 가이드](https://developers.google.com/search/docs/crawling-indexing/javascript/javascript-seo-basics)에 이 과정이 정리되어 있습니다.

<br />

각 단계에서 무엇이 실패하는지 정리하면 다음과 같습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:24%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">단계</th>
      <th style="width:38%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">검색엔진이 확인하는 일</th>
      <th style="width:38%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">실패했을 때 보이는 현상</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>발견</strong></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">이 URL이 웹에 존재한다는 사실을 알게 됩니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">좋은 페이지여도 크롤링 후보에 들어가지 못합니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>크롤링</strong></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">HTTP 요청으로 HTML과 연결된 리소스를 가져옵니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">서버 오류, 접근 제한, 잘못된 robots 규칙 때문에 읽지 못합니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>색인</strong></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">본문과 제목, 링크를 분석해 검색용 데이터베이스에 저장합니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">중복, 빈 내용, <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">noindex</code> 설정 때문에 검색 대상에서 빠집니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>노출</strong></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">사용자의 검색어와 페이지의 관련성을 비교합니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">색인은 됐지만 검색 의도와 맞지 않거나 더 좋은 문서에 밀립니다.</td>
    </tr>
  </tbody>
</table>

<br />

여기서 중요한 순서는 발견, 크롤링, 색인, 노출입니다. 네 번째 문제를 해결하려고 제목만 계속 바꿔도, 첫 번째나 두 번째 단계가 막혀 있으면 결과는 달라지지 않습니다.

<br />

검색엔진이 sitemap을 받아도 반드시 색인하거나 높은 순위를 보장하지는 않습니다. sitemap은 어디까지나 "이 URL들을 우선 확인해 주세요"라고 알려주는 힌트입니다. 그래도 검색엔진이 중요한 페이지를 빠짐없이 발견하도록 돕는 가장 기본적인 신호입니다. [Google의 sitemap 문서](https://developers.google.com/search/docs/crawling-indexing/sitemaps/build-sitemap)도 이 점을 명확히 설명합니다.

<br />
<br />
<br />

## 검색엔진은 URL을 어디서 발견할까

<br />

웹에는 전체 URL을 등록해 둔 중앙 목록이 없습니다. 검색엔진은 이미 알고 있는 페이지에서 링크를 따라가거나, 사이트가 제공한 sitemap을 읽거나, 다른 사이트의 링크를 통해 새 URL을 발견합니다.

<br />

개발자가 주로 관리할 수 있는 발견 경로는 두 가지입니다.

<br />

```text
1. 내부 링크
   목록 페이지, 카테고리, 관련 글, 페이지네이션에서 상세 페이지로 연결한다.

2. sitemap.xml
   색인되기를 원하는 대표 URL 목록을 검색엔진에 제공한다.
```

<br />

내부 링크는 사용자 경험과 SEO가 만나는 지점입니다. 사용자가 목록에서 상세 페이지로 이동할 수 있어야 검색엔진도 그 링크를 따라갈 수 있습니다. 반대로 클릭 이벤트가 실행된 뒤에만 만들어지는 링크, 무한 스크롤 끝에만 나오는 목록, 로그인 후에만 보이는 링크는 발견 경로로 기대하기 어렵습니다.

<br />

실제 프로젝트를 점검하다 보면 자주 보는 패턴이 있습니다. 상세 페이지는 수천 개 있지만 sitemap에는 서비스 소개와 목록 페이지 두 개만 들어 있는 경우입니다. 사람은 목록을 통해 상세 페이지를 열 수 있으니 문제가 없어 보입니다. 하지만 검색엔진은 상세 페이지를 발견하기 위해 자바스크립트 렌더링이나 외부 링크 같은 우회 경로에 의존하게 됩니다.

<br />

특히 행사, 채용, 상품, 공고처럼 유효 기간이 있는 콘텐츠에서는 이 지연이 더 치명적입니다. 검색 결과에 늦게 나타났을 때는 이미 신청 기간이나 판매 기간이 끝났을 수 있습니다. 콘텐츠 품질 이전에 "제때 발견되게 하는 구조"가 필요한 이유입니다.

<br />

다음 두 주소를 나란히 생각해 보면 sitemap이 무엇을 위한 파일인지 쉽게 구분할 수 있습니다.

<br />

```text
색인 후보로 적합한 URL
https://example.com/events/2026-spring-conference

색인 후보로 부적합한 URL
https://example.com/search?tag=ai&sort=latest&page=4
```

<br />

첫 번째는 특정 콘텐츠를 가리키는 안정적인 대표 주소입니다. 두 번째는 사용자의 필터 조합에 따라 끝없이 늘어날 수 있는 검색 결과 주소입니다. 둘을 같은 방식으로 sitemap에 넣거나 색인을 허용하면, 검색엔진이 중요하지 않은 조합 페이지를 반복해서 살펴보게 될 수 있습니다.

<br />

이 문제는 다음 글에서 다룰 canonical과 noindex까지 연결됩니다. 지금은 sitemap이 단순히 "사이트의 모든 URL 목록"이 아니라, 검색 결과에 남기고 싶은 대표 URL의 목록이라는 점만 기억하면 충분합니다.

<br />
<br />
<br />

## 브라우저에서 보인다고 검색엔진도 바로 보는 것은 아니다

<br />

React, Vue, Next.js 같은 프레임워크를 쓰면 브라우저에서는 자연스럽게 콘텐츠가 보입니다. 그렇다고 최초 HTTP 응답에 같은 내용이 들어 있다는 뜻은 아닙니다.

<br />

아래처럼 목록 데이터가 브라우저에서 자바스크립트 요청 뒤에만 채워지는 페이지를 생각해 보겠습니다.

<br />

```html
<main id="app">
  <p>불러오는 중입니다...</p>
</main>
<script src="/assets/app.js"></script>
```

<br />

사용자의 브라우저는 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">app.js</code>를 실행하고 API를 호출한 뒤 화면에 카드 목록을 채웁니다. Googlebot은 자바스크립트를 렌더링할 수 있지만, 원본 HTML을 받은 뒤 렌더링 과정을 별도로 거칩니다. 모든 크롤러가 이 과정을 동일하게 지원하는 것도 아닙니다.

<br />

그래서 검색 유입이 중요한 목록, 상세 본문, 제목, canonical, 구조화 데이터는 가능하면 최초 HTML에 포함하는 편이 좋습니다. 서버 렌더링(SSR), 정적 생성(SSG), 사전 렌더링이 자주 SEO와 함께 언급되는 이유가 여기에 있습니다. 이것은 자바스크립트를 쓰지 말자는 이야기가 아닙니다. 콘텐츠의 핵심 경로를 자바스크립트 실행 하나에만 맡기지 말자는 이야기입니다.

<br />

다음 명령으로 원본 HTML에 정말 필요한 내용이 있는지 빠르게 확인할 수 있습니다.

<br />

```bash
# 페이지의 최초 HTML을 파일로 저장한다.
curl -sL https://example.com/events > /tmp/events.html

# 상세 페이지 링크가 원본 HTML에 있는지 찾는다.
rg -o 'href="/events/[^"]+"' /tmp/events.html | sort -u | wc -l

# 사람이 읽을 수 있는 제목이나 카드 텍스트가 들어 있는지 확인한다.
rg -n '개발자 컨퍼런스|행사 제목 예시' /tmp/events.html
```

<br />

첫 번째 명령은 브라우저가 자바스크립트를 실행하기 전의 응답을 보여줍니다. 두 번째와 세 번째 명령의 결과가 0이라면, 사용자는 목록을 볼 수 있어도 크롤러 관점에서는 내용이 빈 페이지일 가능성을 점검해야 합니다.

<br />

다만 이 명령 하나로 "색인이 안 된다"고 단정하면 안 됩니다. Google은 렌더링 뒤의 HTML도 분석합니다. 이 검사는 검색엔진 최종 상태를 증명하는 도구가 아니라, 렌더링 의존도를 빠르게 발견하는 개발자용 점검 방법입니다. 최종 확인은 Search Console의 URL 검사 도구에서 해야 합니다.

<br />
<br />
<br />

## 색인과 순위는 다른 문제다

<br />

검색 결과에 없을 때 "순위가 낮다"고 말하기 쉽습니다. 하지만 색인 자체가 되지 않은 페이지는 순위 경쟁에 아직 참가하지 못한 상태입니다.

<br />

예를 들어 제목과 본문을 열심히 다듬은 상세 페이지가 있다고 해도, 다음 중 하나면 검색 결과에 거의 보이지 않을 수 있습니다.

<br />

```text
- sitemap과 내부 링크 어디에도 URL이 없다.
- 서버가 5xx 오류를 반환하거나 robots.txt가 크롤을 막는다.
- <meta name="robots" content="noindex">가 설정되어 있다.
- 대표 URL이 아닌 중복 페이지로 판단된다.
- 본문이 너무 얇거나, 다른 페이지와 실질적으로 같은 내용이다.
```

<br />

앞의 네 항목은 주로 개발과 설정의 문제이고, 마지막 항목은 콘텐츠와 정보 구조의 문제입니다. 둘은 분리해서 봐야 합니다. 설정을 완벽하게 맞춰도 사용자가 찾을 이유가 없는 빈 페이지를 좋은 결과로 만들 수는 없습니다. 반대로 좋은 콘텐츠도 검색엔진이 읽지 못하면 평가받을 기회가 없습니다.

<br />

Google은 페이지가 가이드라인을 따른다고 해서 크롤링, 색인, 노출을 보장하지는 않는다고 안내합니다. 따라서 SEO는 단발성 설정 작업보다, 발견 가능성·내용 품질·운영 상태를 계속 확인하는 과정에 가깝습니다. [Google의 크롤링과 색인 FAQ](https://developers.google.com/search/help/crawling-index-faq)를 함께 읽어두면 과도한 약속을 피하는 데 도움이 됩니다.

<br />
<br />
<br />

## robots.txt와 noindex를 같은 것으로 생각하면 생기는 문제

<br />

SEO를 처음 적용할 때 가장 많이 헷갈리는 설정이 robots.txt와 noindex입니다. 둘은 비슷해 보이지만, 검색엔진에게 전달하는 메시지가 다릅니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:30%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">수단</th>
      <th style="width:35%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">검색엔진에 보내는 뜻</th>
      <th style="width:35%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">주로 쓰는 상황</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">robots.txt</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">이 경로는 크롤링하지 말아 달라는 요청입니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">대량 API 경로, 의미 없는 파라미터 조합처럼 크롤 예산을 쓰기 싫은 경로</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">noindex</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">읽어도 되지만 검색 결과에는 넣지 말아 달라는 요청입니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">검색 결과, 개인 설정, 중복성이 높은 필터 페이지처럼 공개는 유지하되 색인은 원하지 않는 화면</td>
    </tr>
  </tbody>
</table>

<br />

검색 결과에서 빼고 싶은 HTML 페이지에는 대개 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">noindex</code>가 맞습니다. 검색엔진이 페이지를 읽어야 그 메타 태그를 확인할 수 있기 때문입니다.

<br />

```html
<meta name="robots" content="noindex, follow">
```

<br />

여기서 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">follow</code>는 페이지 자체는 검색 결과에 넣지 않되, 그 안의 상세 페이지 링크는 계속 따라갈 수 있게 하겠다는 뜻입니다. 검색 결과 화면처럼 자체로는 검색 가치가 낮지만 내부 링크를 제공하는 화면에서 유용할 수 있습니다.

<br />

반대로 같은 URL을 robots.txt에서 막으면 크롤러는 페이지 요청 자체를 하지 않을 수 있습니다. 그러면 페이지 안의 noindex도 보지 못합니다. 외부 링크로 알려진 주소라면 내용 없이 URL만 검색 결과에 남는 상황도 생길 수 있습니다. Google도 robots.txt를 검색 결과에서 페이지를 숨기는 수단으로 사용하지 말라고 안내합니다. [robots.txt 소개](https://developers.google.com/search/docs/crawling-indexing/robots/intro)와 [noindex 문서](https://developers.google.com/search/docs/crawling-indexing/block-indexing)를 함께 보면 이 차이를 정확히 이해할 수 있습니다.

<br />

이 주제는 sitemap과 한 덩어리로 설계해야 합니다. sitemap에는 색인시키고 싶은 대표 URL만 넣고, 색인에서 제외할 페이지는 noindex 여부와 내부 링크 역할을 따져 결정합니다. 다음 글에서 예제 프로젝트를 기준으로 실제 파일을 만들어 보겠습니다.

<br />
<br />
<br />

## 오늘 바로 해볼 수 있는 5분 점검

<br />

아직 Search Console을 연결하지 않았더라도, 다음 항목은 개발 환경이나 운영 환경에서 바로 확인할 수 있습니다.

<br />

```bash
# 1. 홈페이지와 핵심 상세 페이지의 상태 코드를 확인한다.
curl -s -o /dev/null -w '%{http_code}\n' https://example.com/
curl -s -o /dev/null -w '%{http_code}\n' https://example.com/events/example-slug

# 2. sitemap이 있다면 실제 URL 수를 확인한다.
curl -s https://example.com/sitemap.xml | rg -c '<loc>'

# 3. robots.txt에서 sitemap 위치와 차단 규칙을 확인한다.
curl -s https://example.com/robots.txt

# 4. 원본 HTML에 제목과 canonical이 들어 있는지 확인한다.
curl -sL https://example.com/events/example-slug | rg -n '<title>|rel="canonical"'

# 5. 원본 HTML에 noindex가 의도치 않게 들어 있지 않은지 확인한다.
curl -sL https://example.com/events/example-slug | rg -n 'name="robots"|X-Robots-Tag'
```

<br />

명령 결과가 비어 있다고 해서 모두 오류는 아닙니다. 예를 들어 작은 개인 블로그는 sitemap URL 수가 많지 않아도 자연스럽습니다. 다만 실제 상세 페이지가 500개인데 sitemap에 3개만 있다거나, 모든 상세 페이지의 원본 HTML이 로딩 문구만 반환한다면 다음 작업의 우선순위를 높여야 합니다.

<br />

운영 중인 사이트라면 Google Search Console도 함께 연결해 두는 편이 좋습니다. URL 검사 도구는 Google이 마지막으로 본 페이지의 색인 상태, Google이 선택한 canonical, robots 설정, 렌더링된 HTML을 확인할 수 있는 가장 중요한 출발점입니다. 로컬의 curl 결과와 Search Console 결과가 다를 때는 후자를 기준으로 원인을 좁혀가면 됩니다.

<br />
<br />
<br />

## 정리

<br />

SEO의 시작점은 키워드가 아니라 발견 가능성입니다. 검색엔진이 URL을 발견하고, 정상 응답을 받고, 페이지를 이해해 색인에 넣어야 그다음에야 검색 순위 이야기를 할 수 있습니다.

<br />

개발자가 먼저 챙길 것은 의외로 단순합니다. 중요한 상세 페이지가 내부 링크와 sitemap으로 연결되는지, 원본 HTML에 핵심 콘텐츠가 있는지, robots.txt와 noindex가 서로 충돌하지 않는지부터 확인하면 됩니다.

<br />

다음 글에서는 이 출발점 위에서 sitemap.xml과 robots.txt를 직접 설계해 보겠습니다. 어떤 URL을 sitemap에 넣어야 하는지, 동적 콘텐츠가 많은 서비스에서는 어떻게 생성하는지, 그리고 검색 결과에서 제외할 화면을 어떻게 구분하는지를 다룰 예정입니다.

<br />

### 참고 자료

<br />

- [How Google Search Works](https://developers.google.com/search/docs/fundamentals/how-search-works)
- [Understand JavaScript SEO Basics](https://developers.google.com/search/docs/crawling-indexing/javascript/javascript-seo-basics)
- [Build and Submit a Sitemap](https://developers.google.com/search/docs/crawling-indexing/sitemaps/build-sitemap)
- [Robots.txt Introduction and Guide](https://developers.google.com/search/docs/crawling-indexing/robots/intro)
- [Block Search Indexing with noindex](https://developers.google.com/search/docs/crawling-indexing/block-indexing)
