# 개발자를 위한 SEO 실전 가이드 — 실제 사이트를 뜯어보며 배우는 검색 최적화

> 이 글은 SEO를 전혀 모르는 개발자를 독자로 상정합니다. 마케팅 용어보다 HTTP 헤더, HTML 태그, 코드 조각으로 설명합니다.
>
> 예시로 쓰는 사이트는 한국 개발자 행사 큐레이션 서비스 **데브이벤트(dev-event.vercel.app)** 입니다. 2026년 9월에 `claude-seo`라는 오픈소스 감사 도구로 실측한 결과를 기반으로 하며, 점수는 100점 만점에 **36점**이었습니다. 무엇이 왜 깎였고, 어떻게 고치는지를 따라가면 SEO의 핵심을 대부분 지나가게 됩니다.
>
> 분량이 많습니다. 목차를 보고 필요한 장부터 읽어도 됩니다. 각 장은 독립적으로 읽히도록 썼습니다.

---

## 목차

- [0장. 들어가며 — SEO는 마케팅이 아니라 배관 공사다](#0장-들어가며)
- [1장. 검색엔진은 내 사이트를 어떻게 보는가](#1장-검색엔진은-내-사이트를-어떻게-보는가)
- [2장. 발견되게 하기 — sitemap과 robots.txt](#2장-발견되게-하기)
- [3장. 중복을 정리하기 — canonical, 리다이렉트, noindex](#3장-중복을-정리하기)
- [4장. 페이지 한 장을 제대로 만들기 — title, description, heading, Open Graph](#4장-페이지-한-장을-제대로-만들기)
- [5장. 기계가 읽는 데이터 — 구조화 데이터(JSON-LD)](#5장-기계가-읽는-데이터)
- [6장. 렌더링 전략 — SSR, CSR, 그리고 JS를 안 돌리는 크롤러들](#6장-렌더링-전략)
- [7장. 속도 — Core Web Vitals, 캐싱, 이미지](#7장-속도)
- [8장. 콘텐츠 품질 — 빈 페이지가 사이트 전체를 끌어내린다](#8장-콘텐츠-품질)
- [9장. 사이트 구조 — 목록과 상세 사이에 있어야 할 것](#9장-사이트-구조)
- [10장. AI 검색 시대의 SEO — GEO는 새로운 것인가](#10장-ai-검색-시대의-seo)
- [11장. 측정과 운영 — 고쳤는지 어떻게 아나](#11장-측정과-운영)
- [12장. 도구 — claude-seo로 직접 감사하기](#12장-도구)
- [부록 A. 배포 전 SEO 체크리스트](#부록-a-배포-전-seo-체크리스트)
- [부록 B. 용어집](#부록-b-용어집)
- [부록 C. 1차 출처 모음](#부록-c-1차-출처-모음)

---

## 0장. 들어가며

### SEO는 "검색 순위 올리기"가 아니다

SEO(Search Engine Optimization)를 "키워드를 잘 넣어서 구글 1등 하는 기술"로 알고 있다면, 이 글에서 그 인상을 지우는 것이 첫 목표입니다.

개발자 관점에서 SEO는 세 단계로 나뉩니다.

| 단계 | 질문 | 실패하면 |
|---|---|---|
| **발견(Discovery)** | 검색엔진이 이 URL의 존재를 아는가? | 아무 일도 일어나지 않음 |
| **색인(Indexing)** | 검색엔진이 이 페이지를 읽고 저장했는가? | 검색 결과에 안 나옴 |
| **순위(Ranking)** | 다른 페이지보다 먼저 보여줄 이유가 있는가? | 3페이지 뒤에 묻힘 |

대부분의 SEO 글은 세 번째 단계만 다룹니다. 그런데 실제 사이트를 뜯어보면 **첫 두 단계에서 이미 실패한 경우가 훨씬 많습니다.** 데브이벤트가 정확히 그런 사례였습니다. 상세 페이지가 약 3,000건 있는데, 검색엔진에 "여기 이런 페이지들이 있습니다"라고 알려주는 sitemap에는 단 2개만 등재돼 있었습니다. 등재율 0.07%. 아무리 좋은 콘텐츠를 써도 검색엔진이 존재를 모르면 소용이 없습니다.

그래서 이 글은 **배관 공사(plumbing)** 라는 비유를 계속 씁니다. 콘텐츠가 물이라면, sitemap·canonical·구조화 데이터·렌더링·캐싱은 그 물을 검색엔진까지 흘려보내는 배관입니다. 배관이 막혀 있으면 물의 품질은 평가받을 기회조차 없습니다.

### 이 글에서 다루는 사례: 데브이벤트 36점

감사 결과를 먼저 보여드립니다. 각 영역이 무엇을 뜻하는지는 뒤에서 하나씩 설명합니다.

| 영역 | 가중치 | 점수 | 한 줄 진단 |
|---|---:|---:|---|
| 기술 SEO | 22% | 38 | 상세 페이지 약 3,046건이 sitemap에 0건 |
| 콘텐츠 품질 | 23% | 36 | 표본의 75%가 본문 0자 |
| 온페이지 SEO | 20% | 50 | canonical 전무, 페이지 간 title 중복 |
| 구조화 데이터 | 10% | 0 | 행사 사이트인데 Event 스키마 0개 |
| 성능 (CWV) | 10% | 40 | 핵심 페이지만 CDN 캐싱 차단 → TTFB 16배 |
| AI 검색 대응 | 10% | 35 | 목록 페이지가 JS 없이는 행사 0건 |
| 이미지 | 5% | 30 | 59개 중 width/height 명시 0개 |

이 표에서 눈여겨볼 점이 하나 있습니다. **점수가 낮은 항목 대부분이 "콘텐츠가 나빠서"가 아니라 "설정이 빠져서"입니다.** sitemap 동적 생성, canonical 태그 한 줄, JSON-LD 블록, 캐시 헤더. 전부 코드 몇십 줄 규모의 작업이고, 마케터가 아니라 개발자가 고치는 일입니다.

이게 이 글을 쓰는 이유입니다. SEO의 절반 이상은 개발자의 영역인데, 개발자 대상으로 이 배관을 설명하는 자료가 드뭅니다.

### 한 가지 미리 정리할 것: 점수는 절대 지표가 아니다

감사 도구가 내는 점수는 Google 내부 신호가 아닙니다. Google은 2026년 6월 "제3자 SEO 도구는 Google의 내부 랭킹 데이터에 접근할 수 없다"고 공식 문서로 명시했습니다. 도구 점수는 **개선 여지의 크기를 보는 휴리스틱**입니다. 이 글에서 인용하는 36점도 그렇게 읽어주세요.

진짜 1차 소스는 Google Search Console입니다. 이 글 후반(11장)에서 무엇을 봐야 하는지 다룹니다.

---

## 1장. 검색엔진은 내 사이트를 어떻게 보는가

### 크롤링 → 렌더링 → 색인 → 순위

Googlebot이 페이지 하나를 처리하는 과정은 대략 이렇습니다.

```
[URL 발견] → [크롤 큐] → [HTML 요청/수신] → [렌더 큐] → [JS 실행 후 DOM 확정]
          → [색인 여부 판단] → [색인 저장] → [질의 시 순위 결정]
```

개발자에게 중요한 사실 몇 가지가 여기 숨어 있습니다.

**1. URL 발견 경로는 세 가지뿐입니다.** sitemap, 다른 페이지의 링크, 외부 사이트의 링크. 이 중 개발자가 통제할 수 있는 것은 앞의 둘입니다. sitemap이 비어 있고 목록 페이지가 JS로만 링크를 그리면(6장), Googlebot은 외부 링크라는 우회로에만 의존하게 됩니다. 느리고 불확실합니다.

**2. HTML 수신과 JS 실행은 별개 단계입니다.** Googlebot은 JS를 실행합니다. 하지만 "렌더 큐"라는 별도 대기열에 들어가고, 이 대기가 며칠 걸릴 수 있습니다. 그리고 Googlebot을 제외한 대부분의 크롤러(AI 크롤러 포함)는 JS를 실행하지 않습니다. 원본 HTML에 없는 내용은 그들에게 존재하지 않습니다.

**3. HTML은 처음 2MB만 읽습니다.** Googlebot은 HTML의 첫 2MB(비압축 기준)까지만 가져옵니다. base64 인라인 이미지, 거대한 인라인 CSS/JS, 비대한 네비게이션이 앞부분을 채우면 본문이나 JSON-LD가 그 뒤로 밀려 색인에서 빠질 수 있습니다. 오래된 규칙이지만 SPA 프레임워크의 초기 상태 덤프(`__NEXT_DATA__` 같은 것)가 커지면 실제로 문제가 됩니다.

**4. 크롤 속도는 서버가 정합니다.** Googlebot은 5xx나 느린 응답을 만나면 스스로 속도를 낮춥니다. 예전에 있던 Search Console의 크롤 속도 수동 설정은 2024년 1월에 제거됐습니다. 크롤을 더 받고 싶으면 sitemap을 정확히 하고, 서버를 빠르게 하고, robots로 불필요한 경로를 잘라내는 것 외에 방법이 없습니다.

**5. 200이 아닌 페이지에서는 JS를 실행하지 않습니다.** 2025년 12월 Google JS SEO 문서 갱신에서 명시된 내용입니다. 404나 500 페이지에 JS로 넣은 메타 태그나 콘텐츠는 Googlebot에게 보이지 않습니다.

### "크롤 예산"이라는 개념

Googlebot이 한 사이트에 쓰는 시간과 요청 수는 무한하지 않습니다. 이걸 **크롤 예산(crawl budget)** 이라고 부릅니다. 페이지가 1만 건 미만인 사이트는 보통 신경 쓸 필요가 없다고 Google이 말하지만, 예외가 있습니다.

**무한 URL 생성 구조**입니다. 검색 결과 페이지에 `?tag=AI`, `?tag=클라우드`, `?keyword=…` 같은 파라미터가 붙고, 그 조합이 전부 200을 반환하며 색인 허용 상태라면, 크롤러는 실질적으로 같은 내용의 URL을 무한히 방문합니다. 이걸 **크롤 트랩(crawl trap)** 이라고 합니다. 데브이벤트의 `/search` 라우트가 정확히 이 상태였습니다. 크롤 예산이 여기로 새면, 정작 색인돼야 할 상세 페이지 3,000건이 뒤로 밀립니다.

### 색인이 되지 않으면 그 뒤는 전부 무의미하다

Google의 AI 최적화 가이드(2026년 5월)는 이걸 **자격 하한선(eligibility floor)** 이라고 표현합니다. AI Overview든 AI Mode든, 어떤 검색 기능에 나오려면 페이지가 **색인되어 있고 스니펫으로 표시 가능한 상태**여야 합니다. 별도의 "AI 색인"은 없습니다.

즉, 10장에서 다룰 AI 검색 대응은 1~3장의 기초가 돼 있지 않으면 시작할 수 없습니다. 순서가 있습니다.

### 직접 확인하는 방법

지금 내 사이트가 어떤 상태인지 5분 안에 보는 명령들입니다. 이 글 전체에서 반복해서 쓰게 됩니다.

```bash
# 1. 원본 HTML에 본문이 있는가? (JS 실행 전 상태)
curl -s https://example.com/some-page | wc -c

# 2. 원본 HTML에 상세 페이지 링크가 몇 개 있는가?
curl -s https://example.com/list | grep -o 'href="/item/[0-9]*"' | sort -u | wc -l

# 3. 응답 헤더 확인 (캐시, 상태 코드, 리다이렉트)
curl -sI https://example.com/ | head -20

# 4. sitemap이 있고, 몇 개의 URL이 들어 있는가?
curl -s https://example.com/sitemap.xml | grep -c '<loc>'

# 5. robots.txt
curl -s https://example.com/robots.txt

# 6. Google이 색인한 페이지 수 (대략치) — 브라우저에서
#    site:example.com
```

**주의**: `curl -I`는 HEAD 요청을 보내는데, 일부 서버는 HEAD와 GET에 다른 헤더를 돌려줍니다. 헤더를 정확히 보려면 `curl -s -D - -o /dev/null URL`로 GET을 보내고 헤더만 출력하는 편이 안전합니다.

이 명령들은 전부 **읽기 전용**입니다. 대상 사이트에 아무 영향을 주지 않습니다.

---

## 2장. 발견되게 하기

### sitemap.xml — 검색엔진에게 주는 URL 목록

sitemap은 형식이 단순합니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>https://example.com/item/123</loc>
    <lastmod>2026-09-01T09:30:00+09:00</lastmod>
  </url>
</urlset>
```

단순한데도 틀리는 지점이 정해져 있습니다.

#### 규칙 1. `lastmod`는 진짜 수정 시각이어야 한다

Google은 `<lastmod>`를 **일관되게, 검증 가능하게 정확할 때만** 신뢰합니다. 매 요청마다 `new Date()`를 넣어서 모든 URL이 "지금"으로 찍히면, Google은 이 사이트의 lastmod가 무의미하다고 판단하고 전체를 무시합니다. 그러면 정말 갱신된 페이지도 재크롤 우선순위를 잃습니다.

반드시 DB의 `updated_at` 같은 실제 갱신 시각을 쓰세요. 그리고 "본문·구조화 데이터·링크가 바뀐 시각"이어야 합니다. 저작권 연도만 바뀐 것은 갱신이 아닙니다.

#### 규칙 2. `priority`와 `changefreq`는 Google이 무시한다

넣어도 해롭지는 않지만 아무 효과가 없습니다. 감사 도구들은 Info 레벨로 "제거 가능"이라고 표시합니다. 데브이벤트의 기존 sitemap에도 `<changefreq>daily</changefreq>`가 들어 있었는데, 이건 있어도 없어도 같습니다.

#### 규칙 3. 파일당 50,000 URL 또는 50MB(비압축)

둘 중 먼저 걸리는 쪽이 상한입니다. 넘으면 **sitemap index**로 분할합니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <sitemap>
    <loc>https://example.com/sitemap-2025.xml</loc>
    <lastmod>2026-01-01</lastmod>
  </sitemap>
  <sitemap>
    <loc>https://example.com/sitemap-2026.xml</loc>
    <lastmod>2026-09-01</lastmod>
  </sitemap>
</sitemapindex>
```

다만 **과잉 설계를 경계**하세요. 3,000건은 단일 파일로 충분합니다. 1만 건을 넘어가면 그때 연도별이나 타입별로 나누면 됩니다.

#### 규칙 4. sitemap에 넣지 말아야 할 URL

| 넣으면 안 되는 것 | 이유 |
|---|---|
| `noindex` 페이지 | "색인해줘"와 "색인하지 마"를 동시에 말하는 모순 |
| 리다이렉트되는 URL | 최종 목적지를 넣어야 함 |
| canonical이 다른 곳을 가리키는 URL | 대표 URL만 넣음 |
| 404/5xx URL | 신뢰도 하락 |
| `http://` URL | HTTPS만 |

이 규칙은 3장의 canonical·noindex와 직결됩니다. sitemap은 "내가 색인되기를 원하는 대표 URL의 목록"이어야 합니다.

#### Next.js에서 동적 sitemap 만들기 (Pages Router)

데브이벤트 감사에서 가장 먼저 권한 조치입니다. `pages/sitemap.xml.ts`라는 파일을 만들면 `/sitemap.xml` 요청이 이 파일로 라우팅됩니다.

```ts
// pages/sitemap.xml.ts
import type { GetServerSideProps } from 'next'

const SITE = 'https://example.com'

export const getServerSideProps: GetServerSideProps = async ({ res }) => {
  // id와 updatedAt만 있으면 충분합니다. 본문을 전부 가져오지 마세요.
  const items = await fetchAllItemsForSitemap() // [{ id, updatedAt }]

  const urls = [
    `<url><loc>${SITE}/list</loc></url>`,
    `<url><loc>${SITE}/about</loc></url>`,
    ...items.map(
      (it) =>
        `<url>` +
        `<loc>${SITE}/item/${it.id}</loc>` +
        `<lastmod>${new Date(it.updatedAt).toISOString()}</lastmod>` +
        `</url>`,
    ),
  ]

  const xml =
    `<?xml version="1.0" encoding="UTF-8"?>` +
    `<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">` +
    urls.join('') +
    `</urlset>`

  res.setHeader('Content-Type', 'application/xml')
  // 1시간 CDN 캐시, 그 뒤 24시간은 오래된 것을 주면서 백그라운드 갱신
  res.setHeader('Cache-Control', 'public, s-maxage=3600, stale-while-revalidate=86400')
  res.write(xml)
  res.end()

  return { props: {} }
}

// 페이지 컴포넌트는 렌더링되지 않지만 export가 필요합니다
export default function Sitemap() {
  return null
}
```

App Router라면 `app/sitemap.ts`에서 `MetadataRoute.Sitemap` 배열을 반환하는 방식이 공식 지원됩니다.

```ts
// app/sitemap.ts
import type { MetadataRoute } from 'next'

export default async function sitemap(): Promise<MetadataRoute.Sitemap> {
  const items = await fetchAllItemsForSitemap()
  return [
    { url: 'https://example.com/list' },
    { url: 'https://example.com/about' },
    ...items.map((it) => ({
      url: `https://example.com/item/${it.id}`,
      lastModified: new Date(it.updatedAt),
    })),
  ]
}
```

#### 배포 후 확인

```bash
# URL 수가 실제 콘텐츠 수와 비슷한가?
curl -s https://example.com/sitemap.xml | grep -c '<loc>'

# lastmod가 전부 같은 값은 아닌가? (같으면 규칙 1 위반)
curl -s https://example.com/sitemap.xml | grep -o '<lastmod>[^<]*' | sort | uniq -c | sort -rn | head
```

그리고 Search Console > Sitemaps에서 "검색된 URL" 수를 봅니다. 배포 후 며칠 안에 실제 페이지 수 근처로 올라가야 정상입니다. 1주일이 지나도 "색인 생성됨" 수가 늘지 않으면 sitemap 문제가 아니라 품질 평가 문제(8장)입니다.

### robots.txt — 크롤러에게 주는 출입 규칙

```
User-agent: *
Allow: /
Disallow: /myinfo
Disallow: /api/

Sitemap: https://example.com/sitemap.xml
```

#### 문법 주의

`Allow:/`처럼 콜론 뒤에 공백이 없어도 관대한 파서는 통과시키지만, 표준 형식은 `Allow: /`입니다. 데브이벤트의 robots.txt에 이 사소한 오류가 있었습니다. 순위에 영향은 없지만 고쳐두는 게 맞습니다.

#### robots.txt Disallow와 noindex는 다르다 — 그리고 충돌한다

이건 많이 틀리는 지점이라 따로 강조합니다.

| 수단 | 의미 | 결과 |
|---|---|---|
| `robots.txt`의 `Disallow` | "이 경로는 **요청하지 마**" | 크롤러가 페이지를 안 읽음 |
| `<meta name="robots" content="noindex">` | "읽어도 되지만 **색인은 하지 마**" | 크롤러가 읽고, 색인에서 제외 |

문제는 둘을 같은 경로에 동시에 걸었을 때입니다. `Disallow`로 막으면 크롤러가 페이지를 **읽지 못하므로** 그 안에 있는 `noindex` 메타 태그도 볼 수 없습니다. 그러면 외부 링크 등으로 URL을 알게 됐을 때, 내용은 모르지만 URL은 색인하는 "제목 없는 색인" 상태가 됩니다.

**결론**: 색인에서 빼고 싶은 페이지는 `noindex`만 쓰고, robots.txt에서는 막지 마세요. robots.txt의 `Disallow`는 크롤 예산을 절약해야 하는 대량 API 경로나 정말로 크롤러가 접근할 필요가 없는 곳에만 씁니다.

#### AI 크롤러 정책은 SEO가 아니라 정책 선택이다

2025~2026년 기준으로 AI 회사들이 별도의 크롤러로 웹을 수집합니다. robots.txt에서 개별 지정이 가능합니다.

| 토큰 | 회사 | 용도 | robots.txt 준수 |
|---|---|---|---|
| `GPTBot` | OpenAI | 모델 학습 | 예 |
| `OAI-SearchBot` | OpenAI | 검색 기능 | 예 |
| `ChatGPT-User` | OpenAI | 사용자 요청 실시간 브라우징 | **아니오** (사용자 트리거) |
| `ClaudeBot` | Anthropic | 웹 기능 | 예 |
| `PerplexityBot` | Perplexity | 검색 색인 | 예 |
| `CCBot` | Common Crawl | 공개 데이터셋 | 예 |
| `Google-Extended` | Google | Gemini 학습 (**검색과 무관**) | 예 |
| `Bytespider` | ByteDance | 모델 학습 | 예 |

핵심 구분 두 가지입니다.

- `Google-Extended`를 막아도 **Google 검색 색인과 AI Overview에는 영향이 없습니다.** 둘은 `Googlebot`을 씁니다.
- `GPTBot`을 막아도 ChatGPT가 사용자 요청으로 페이지를 가져오는 `ChatGPT-User`는 막히지 않습니다. 사용자 트리거 fetcher는 robots.txt를 무시하도록 설계돼 있습니다. 정말 막으려면 서버 측 접근 제어가 필요합니다.

학습 이용은 막고 검색 노출은 유지하고 싶다면 이렇게 씁니다.

```
# AI 학습 크롤러만 차단
User-agent: GPTBot
Disallow: /

User-agent: Google-Extended
Disallow: /

User-agent: CCBot
Disallow: /

# 나머지(검색용 Googlebot 포함)는 허용
User-agent: *
Allow: /

Sitemap: https://example.com/sitemap.xml
```

**그런데 이건 SEO 문제가 아닙니다.** 콘텐츠가 AI 답변에 인용되어 유입이 생기길 원한다면 열어두는 게 맞고, 학습 이용을 막고 싶다면 막는 게 맞습니다. 감사 리포트는 현재 상태(전부 허용)를 보고하고 선택은 운영자에게 넘겨야 합니다. 데브이벤트 리포트도 그렇게 썼습니다.

### IndexNow — Google 외 검색엔진용 즉시 알림

Bing, Yandex, 그리고 한국에서 중요한 **네이버**가 지원하는 프로토콜입니다. URL이 생기거나 바뀌면 API로 즉시 알립니다. Google은 지원하지 않습니다.

```bash
# 키 파일을 사이트 루트에 두고
# https://example.com/<key>.txt  (내용: 키 문자열)

# URL 변경 알림
curl -X POST "https://api.indexnow.org/indexnow" \
  -H "Content-Type: application/json; charset=utf-8" \
  -d '{
    "host": "example.com",
    "key": "<key>",
    "urlList": ["https://example.com/item/3212"]
  }'
```

한국 사용자 대상 서비스라면 네이버 서치어드바이저 등록과 함께 검토할 가치가 있습니다. 이 글은 Google 중심으로 쓰지만, 국내 트래픽에서 네이버 비중이 큰 서비스는 별도 확인이 필요합니다.

---

## 3장. 중복을 정리하기

### 같은 내용, 여러 URL — 검색엔진의 골칫거리

아래 URL들은 사람에게는 전부 "같은 페이지"입니다.

```
https://example.com/item/123
https://example.com/item/123/
https://example.com/item/123?utm_source=instagram
https://example.com/item/123?utm_source=threads&utm_campaign=weekly
http://example.com/item/123
```

검색엔진에게는 다섯 개의 다른 URL입니다. 어느 것을 대표로 삼아야 하는지 스스로 판단해야 하고, 그 판단이 틀리면 랭킹 신호가 여러 URL로 분산됩니다.

특히 **SNS로 링크를 배포하는 서비스는 UTM 파라미터가 붙은 URL이 실제로 유통됩니다.** 데브이벤트는 Instagram과 Threads로 행사 링크를 배포하는 파이프라인을 운영하고 있어서, 이 문제가 이론이 아니라 현실이었습니다.

### canonical — "이 페이지의 대표 URL은 이것입니다"

```html
<link rel="canonical" href="https://example.com/item/123" />
```

`<head>` 안에 이 한 줄을 넣으면 위의 다섯 변형이 전부 하나로 모입니다. 규칙은 이렇습니다.

| 페이지 | canonical이 가리킬 곳 |
|---|---|
| 상세 페이지 | **자기 자신** (파라미터 없는 절대 URL) |
| 목록 페이지 | 자기 자신 |
| 검색 결과 `/search?tag=AI` | **목록 페이지** (자기 자신이 아님) |
| 페이지네이션 `/list?page=2` | 내용이 다르면 자기 자신, 사실상 같은 내용이면 1페이지 |

검색 결과 페이지가 목록을 가리키는 이유는, 검색 결과는 색인 대상이 아니고(아래 noindex 참조) 그 랭킹 신호는 목록 페이지에 모이는 게 맞기 때문입니다.

#### Next.js 구현

```tsx
// components/Seo.tsx
import Head from 'next/head'

const SITE = 'https://example.com'

type Props = {
  title: string
  description: string
  path: string           // '/item/123' 처럼 파라미터 없는 경로
  canonicalPath?: string // 검색 페이지처럼 다른 곳을 가리켜야 할 때
  noindex?: boolean
  ogImage?: string       // 상대경로여도 됨, 아래에서 절대화
}

const toAbsolute = (p: string) =>
  /^https?:\/\//i.test(p) ? p : `${SITE}${p.startsWith('/') ? '' : '/'}${p}`

export function Seo({ title, description, path, canonicalPath, noindex, ogImage }: Props) {
  const url = toAbsolute(path)
  const canonical = toAbsolute(canonicalPath ?? path)
  const image = toAbsolute(ogImage ?? '/default/og.png')

  return (
    <Head>
      <title>{title}</title>
      <meta name="description" content={description} />
      <link rel="canonical" href={canonical} />
      {noindex && <meta name="robots" content="noindex, follow" />}

      <meta property="og:type" content="website" />
      <meta property="og:title" content={title} />
      <meta property="og:description" content={description} />
      <meta property="og:url" content={url} />
      <meta property="og:image" content={image} />

      <meta name="twitter:card" content="summary_large_image" />
      <meta name="twitter:title" content={title} />
      <meta name="twitter:description" content={description} />
      <meta name="twitter:image" content={image} />
    </Head>
  )
}
```

```tsx
// pages/item/[id].tsx
<Seo
  title={`${item.title} | 데브이벤트`}
  description={buildDescription(item)}
  path={`/item/${item.id}`}
  ogImage={item.thumbnail}
/>

// pages/search.tsx — 자기 자신이 아니라 목록을 canonical로, 그리고 noindex
<Seo
  title="행사 검색 | 데브이벤트"
  description={`${label} 행사를 찾아보세요`}
  path={router.asPath}
  canonicalPath="/list"
  noindex
/>
```

이 컴포넌트 하나로 canonical, robots, Open Graph, Twitter 카드, 절대 URL 변환(4장)이 한 곳에서 관리됩니다. 페이지마다 `<Head>`를 따로 쓰면 어느 페이지에서 무엇이 빠졌는지 추적이 안 됩니다.

#### canonical에서 자주 틀리는 것

1. **상대 경로** — `href="/item/123"`은 안 됩니다. 절대 URL이어야 합니다.
2. **서버 HTML과 JS 렌더 결과가 다른 canonical** — 2025년 12월 Google 문서가 명시했습니다. 원본 HTML의 canonical과 JS가 나중에 바꾼 canonical이 다르면 Google은 **둘 중 아무 것이나** 쓸 수 있습니다. 반드시 서버 렌더 HTML에 최종값이 있어야 합니다.
3. **noindex와 canonical을 동시에 다른 곳으로** — "이 페이지는 색인하지 마, 그런데 대표는 저 페이지야"는 모순은 아니지만 신호가 약해집니다. noindex 페이지의 canonical은 위 표처럼 랭킹 신호를 모을 곳을 가리키면 됩니다.
4. **고친 직후 확인** — canonical 수정 후 Google이 중복 클러스터를 재평가하는 데 **최대 2주**가 걸립니다. 배포 다음 날 Search Console에서 "Google이 선택한 표준 URL"이 안 바뀌었다고 실패로 판단하면 안 됩니다.

#### 실패 판정

Search Console > URL 검사에서 "사용자가 선택한 표준 URL"과 "Google이 선택한 표준 URL"이 2주 후에도 불일치하면 무언가 잘못된 것입니다.

### 리다이렉트 — 301/308과 302/307의 차이

| 코드 | 의미 | 메서드 유지 | 검색엔진 해석 |
|---|---|---|---|
| 301 | 영구 이동 | 아니오 (POST→GET 가능) | 신호를 새 URL로 넘김 |
| **308** | 영구 이동 | **예** | 신호를 새 URL로 넘김 |
| 302 | 임시 이동 | 아니오 | 원래 URL을 유지 |
| 307 | 임시 이동 | 예 | 원래 URL을 유지 |

**구조적으로 영구적인 이동은 301 또는 308**이어야 합니다. `/`에서 `/events`로 항상 보내는 홈 리다이렉트가 307이면, 검색엔진은 "임시니까 `/`가 언젠가 돌아오겐가" 하고 `/`를 대표로 유지하려 합니다. 데브이벤트가 이 상태였습니다. Next.js의 `redirects()`에서 `permanent: true`를 주면 308이 나갑니다.

```js
// next.config.js
module.exports = {
  async redirects() {
    return [
      { source: '/', destination: '/events', permanent: true }, // 308
    ]
  },
}
```

리다이렉트 체인(A→B→C)은 1홉으로 줄이세요. Googlebot은 최대 몇 홉까지 따라가긴 하지만 매 홉마다 신호가 약해지고 크롤 예산이 듭니다.

#### 확인

```bash
# -L 없이 첫 응답만 보면 리다이렉트 코드가 그대로 보입니다
curl -s -o /dev/null -w '%{http_code} %{redirect_url}\n' https://example.com/
curl -s -o /dev/null -w '%{http_code} %{redirect_url}\n' https://example.com/events/
curl -s -o /dev/null -w '%{http_code} %{redirect_url}\n' http://example.com/events
```

데브이벤트에서는 홈 리다이렉트 307을 제외한 나머지(http→https, 트레일링 슬래시 제거)가 모두 308로 정상이었고, 대소문자 다른 경로는 404, 존재하지 않는 ID도 404였습니다. 존재하지 않는 페이지에 200을 주면서 "없습니다" 화면을 보여주는 **소프트 404**가 아닌 점은 잘 된 부분입니다.

### noindex — "이건 색인하지 마세요"

```html
<meta name="robots" content="noindex, follow" />
```

`follow`를 함께 두는 이유는, 이 페이지는 색인하지 않되 **이 페이지에서 나가는 링크는 계속 따라가라**는 뜻이기 때문입니다. 검색 결과 페이지에서 상세 페이지로 가는 링크는 여전히 발견 경로로 쓸 수 있습니다.

#### noindex를 걸어야 하는 페이지

- 내부 검색 결과 (`/search?q=…`) — 조합이 무한하고 내용이 얇음
- 로그인·마이페이지 등 개인화 페이지
- 필터 조합 페이지 중 2축 이상 (9장)
- 태그·카테고리 중 항목이 너무 적어 얇은 페이지

#### noindex를 걸기 전에 반드시 확인할 것

**noindex는 원본 HTML에 있어야 합니다.** 2025년 12월 Google 문서에 따르면, 원본 HTML에 `noindex`가 있는데 JS가 그것을 제거해도 Google은 원본의 noindex를 **그대로 존중할 수 있습니다.** 반대로 JS로 noindex를 넣는 것도 신뢰할 수 없습니다. 서버에서 결정하세요.

그리고 **실수로 상세 페이지에 걸리지 않았는지** 배포 후 확인이 필수입니다. 3,000건이 색인에서 빠지면 복구에 몇 주가 걸립니다. 11장의 drift 모니터링이 정확히 이 사고를 잡기 위한 도구입니다.

```bash
# 상세 페이지에 noindex가 없는지
curl -s https://example.com/item/123 | grep -i 'name="robots"'
# 출력이 없거나 index 계열이어야 정상. noindex가 나오면 사고.

# 검색 페이지에는 있는지
curl -s 'https://example.com/search?tag=AI' | grep -i 'name="robots"'
```

### 크롤 트랩 사례: `/search` 페이지

데브이벤트의 검색 라우트를 직접 요청한 결과입니다.

| 항목 | 값 |
|---|---|
| HTTP | 200 |
| title | 모든 태그에서 동일 |
| description | `undefined 행사, 데브이벤트에서 찾아보세요!` — 리터럴 `undefined` 노출 |
| robots 메타 | 없음 → 색인 허용 |
| canonical | 없음 |
| 서버 렌더 본문 | 296자, 행사 카드 0건 |

세 가지가 겹쳐 있습니다. 색인 허용 + 무한 파라미터 + 빈 내용. 이것이 크롤 트랩의 전형입니다. 그리고 `undefined`는 검색 결과 스니펫에 그대로 노출될 수 있는 버그입니다.

조치는 두 단계입니다.

**즉시**: noindex와 undefined 수정. 30분 작업입니다.

```tsx
// pages/search.tsx
const label = (router.query.tag ?? router.query.keyword ?? '개발자') as string

<Seo
  title={`${label} 행사 검색 | 데브이벤트`}
  description={`${label} 관련 개발자 행사를 데브이벤트에서 찾아보세요.`}
  path={router.asPath}
  canonicalPath="/events"
  noindex
/>
```

**중기**: 색인시킬 가치가 있는 필터(예: "AI 행사")는 `/search?tag=AI`가 아니라 `/events/tag/ai` 같은 **정적 경로의 랜딩 페이지**로 따로 만듭니다. 9장에서 다룹니다. `/search`는 계속 noindex로 두고 사용자 편의 기능으로만 씁니다.

**실패 판정**: 배포 2주 후 `site:example.com/search` 결과가 0으로 수렴하지 않으면 noindex가 제대로 나가지 않은 것입니다.

### robots.txt로 `/search`를 막으면 안 되는 이유

앞에서 설명한 충돌입니다. `noindex`를 넣은 상태에서 robots.txt에 `Disallow: /search`를 추가하면, 크롤러가 페이지를 읽지 못해 noindex를 못 보고, 이미 색인된 URL이 그대로 남습니다. **noindex를 걸었으면 robots.txt는 건드리지 마세요.**

---

## 4장. 페이지 한 장을 제대로 만들기

이 장은 "온페이지 SEO"라고 불리는 영역입니다. 한 페이지의 `<head>`와 본문 구조가 대상입니다. 규칙 자체는 단순한데, **템플릿으로 수천 페이지를 찍어내는 사이트에서는 한 곳의 실수가 전체로 복제된다**는 점이 중요합니다.

### title — 검색 결과의 파란 글씨

```html
<title>2026 금융 AI Challenge | 데브이벤트</title>
```

| 항목 | 기준 |
|---|---|
| 길이 | 30~60자 (Google이 약 60자에서 자름) |
| 핵심 키워드 | 앞쪽에 |
| 브랜드명 | 뒤쪽에, 구분자(`|`, `-`)로 |
| 고유성 | **페이지마다 달라야 함** |

마지막 줄이 실전에서 가장 자주 깨집니다. 데브이벤트의 `/search?tag=…`는 모든 태그에서 title이 같았습니다. 검색엔진에게는 "다른 URL인데 제목이 같은 페이지가 무한히 있다"로 보이고, 이건 중복 콘텐츠 신호입니다.

한글은 영문보다 글자당 폭이 넓어서 한글 기준 30자 안팎이면 잘리지 않습니다. 정확한 컷은 픽셀 폭 기준이라 글자 수는 근사치입니다.

### meta description — 검색 결과의 회색 설명글

```html
<meta name="description" content="OpenAI가 주최하는 'OpenAI DevDay Exchange 2026 - 서울'. 2026년 8월 19일~9월 4일 참가 신청을 받습니다. 자세한 프로그램과 참가 방법을 확인하세요." />
```

| 항목 | 기준 |
|---|---|
| 길이 | 120~160자 (약 155~160자에서 잘림) |
| 내용 | 이 페이지가 무엇인지 + 행동 유도 |
| 고유성 | 페이지마다 달라야 함 |

description은 **순위 요소가 아닙니다.** 하지만 클릭률(CTR)에 직접 영향을 줍니다. 그리고 Google은 description이 부실하면 본문에서 스니펫을 직접 뽑아 쓰기 때문에, 본문이 충실한 페이지에서는 description 개선 효과가 작고 **본문이 빈 페이지에서 효과가 큽니다.**

#### 템플릿 리터럴 노출 사고

데브이벤트 목록 페이지의 실제 description입니다.

```html
<meta name="description" content="데브이벤트 웹에서 개발자 행사를 놓치지 마세요!
개발자를 위한 {웨비나, 컨퍼런스, 해커톤, 네트워킹} 소식을 알려드립니다."/>
```

중괄호가 그대로 들어 있습니다. 의도한 표현일 수 있지만 검색 결과에서는 **치환에 실패한 템플릿 변수**로 보입니다. 앞 장의 `undefined`와 같은 부류의 문제입니다.

이런 사고는 코드 리뷰로 잡기 어렵습니다. 실제 배포된 HTML을 파싱해서 아래 패턴을 검사하는 스모크 테스트를 CI에 넣는 게 확실합니다.

```bash
#!/usr/bin/env bash
# seo-smoke.sh — 배포 후 메타 태그 사고 검사
set -euo pipefail

URLS=(
  "https://example.com/list"
  "https://example.com/item/3212"
  "https://example.com/search?tag=AI"
)

fail=0
for url in "${URLS[@]}"; do
  html=$(curl -s "$url")
  desc=$(printf '%s' "$html" | grep -o '<meta name="description" content="[^"]*"' | head -1)
  title=$(printf '%s' "$html" | grep -o '<title>[^<]*' | head -1)

  for bad in 'undefined' 'null' 'NaN' '{' '}' '\[object' '%7B'; do
    if printf '%s%s' "$desc" "$title" | grep -qF -- "$bad"; then
      echo "FAIL $url : '$bad' 노출 → $desc $title"
      fail=1
    fi
  done
done
exit $fail
```

### heading — h1은 하나, 계층은 건너뛰지 않기

```html
<h1>2026 금융 AI Challenge</h1>
  <h2>행사 요약</h2>
  <h2>이런 분께 추천해요</h2>
  <h2>다루는 내용</h2>
  <h2>참가 안내</h2>
    <h3>일정</h3>
    <h3>참가비</h3>
```

- **h1은 페이지당 하나.** 페이지의 주제입니다.
- **h1 → h2 → h3 순서로, 단계를 건너뛰지 않습니다.** h1 다음에 h4가 오면 구조 신호가 약해집니다.
- heading은 **스타일이 아니라 구조**입니다. 글씨를 크게 만들려고 `<h2>`를 쓰면 안 되고, 구조상 소제목인데 `<div class="title">`로 만들면 안 됩니다.

#### 요약은 앞에

데브이벤트의 신규 상세 페이지는 h2 5개("이런 분께 추천해요" / "다루는 내용" / "참가 안내" / "개최 도시" / "행사 요약") 구조를 이미 갖추고 있어 재설계가 필요 없었습니다. 한 가지 권한 것은 **"행사 요약" h2를 맨 마지막에서 맨 앞으로 옮기라**는 것이었습니다.

이유는 검색 스니펫 추출과 AI 답변 인용이 **문서 앞부분을 우선 스캔**하기 때문입니다. AI 인용의 약 44%가 페이지 상위 30% 구간에서 나온다는 연구가 있습니다(SE Ranking). 결론을 앞에 두는 것은 사람에게도 기계에게도 유리합니다.

### Open Graph — 카카오톡·인스타그램·Threads에서 보이는 미리보기

```html
<meta property="og:type" content="website" />
<meta property="og:title" content="2026 금융 AI Challenge" />
<meta property="og:description" content="금융보안원 주최, 7월 13일~9월 7일 접수" />
<meta property="og:url" content="https://example.com/item/3131" />
<meta property="og:image" content="https://example.com/thumb/3131.png" />
```

Open Graph는 검색 순위와 무관합니다. 하지만 **링크를 공유했을 때 이미지와 제목이 뜨는지**를 결정하고, 그게 클릭률입니다.

#### og:image는 절대 URL이어야 한다

Open Graph 명세는 절대 URL을 요구합니다. 카카오톡·Facebook·Threads 크롤러는 상대경로를 해석하지 못하고 이미지를 빈 값으로 처리합니다.

데브이벤트 상세 페이지 12건을 표본으로 보니 3건(25%)이 상대경로였습니다. 썸네일이 없는 행사가 기본 이미지로 폴백할 때 `/default/event-thumbnail-light.png`처럼 상대경로가 됐습니다. 하필 가장 최근 등록분이 그 상태였고, 이 서비스는 Instagram·Threads로 링크를 배포하는 파이프라인을 운영 중이라 실제 피해가 발생하는 버그였습니다.

3장의 `Seo` 컴포넌트에 넣은 `toAbsolute` 헬퍼가 이 문제를 구조적으로 막습니다. 페이지마다 `startsWith('http')`를 검사하는 대신, **모든 URL이 한 함수를 거쳐 나가게** 하세요.

#### Twitter 카드도 함께

```html
<meta name="twitter:card" content="summary_large_image" />
<meta name="twitter:title" content="..." />
<meta name="twitter:description" content="..." />
<meta name="twitter:image" content="절대 URL" />
```

X(Twitter)뿐 아니라 Slack·Discord·Notion 등 여러 서비스가 이 태그를 폴백으로 읽습니다.

#### 확인

- Facebook 공유 디버거: https://developers.facebook.com/tools/debug/
- 카카오톡: 실제로 나에게 링크를 보내보는 게 가장 확실합니다. 카카오는 캐시가 있어서 수정 후 바로 안 바뀔 수 있습니다.

### 링크 — 내부 링크, 앵커 텍스트, rel 속성

#### 내부 링크는 발견 경로이자 구조 신호

- 모든 페이지가 다른 페이지에서 최소 하나의 링크를 받아야 합니다. 어디서도 링크되지 않는 **고아 페이지(orphan page)** 는 sitemap에만 의존하게 됩니다.
- 중요한 페이지는 홈에서 **3클릭 이내**에 도달 가능해야 합니다.
- 앵커 텍스트는 목적지를 설명해야 합니다. "여기를 클릭"은 아무 정보가 없습니다.
- 같은 페이지로 가는 앵커 텍스트가 전부 동일 문구이면 부자연스럽습니다. 변형을 섞으세요.

#### 외부 링크의 rel

```html
<a href="https://external-event-site.example" target="_blank" rel="noopener noreferrer">참여하기</a>
```

- `target="_blank"`를 쓰면 `rel="noopener"`가 사실상 필수입니다. 새 창이 원래 창의 `window.opener`에 접근하는 보안 문제를 막습니다. 데브이벤트는 `target="_blank"` 6개 중 4개가 `noopener` 누락이었습니다. 최신 브라우저는 `_blank`에 noopener를 기본 적용하지만 명시하는 게 안전합니다.
- `rel="nofollow"`는 "이 링크에 랭킹 신호를 넘기지 마라"는 뜻입니다. 사용자 생성 콘텐츠나 광고 링크에 씁니다. **디렉터리·큐레이션 사이트는 외부 링크가 본질적 가치**이므로 nofollow를 일괄 적용하는 것은 신중해야 합니다. 신뢰할 수 있는 행사 사이트로 나가는 링크라면 follow로 두는 게 자연스럽습니다.

### 이미지 alt

```html
<img src="/thumb/3131.png" alt="2026 금융 AI Challenge 포스터" width="600" height="315" loading="lazy" />
```

- 장식용이 아닌 모든 이미지에 alt를 넣습니다. 장식용은 `alt=""`로 비워둡니다.
- 10~125자, 이미지 내용을 설명합니다. 파일명(`image.jpg`)이나 키워드 나열은 안 됩니다.
- 데브이벤트는 59개 이미지 전부 alt가 채워져 있었습니다. 이건 잘하고 있는 부분이었고, 리포트에 그렇게 썼습니다. 감사는 못한 것만 나열하는 일이 아닙니다.

`width`/`height`와 `loading`은 7장(속도)에서 다룹니다.

### 메타데이터 템플릿 설계 — 수천 페이지를 한 번에 좋게 만들기

데이터로 페이지를 찍어내는 사이트에서는 title/description 템플릿이 곧 SEO 품질입니다. 데브이벤트 상세 페이지의 기존 템플릿은 `{주최사}에서 주최하는 {행사명}` 한 문장, 46~49자였습니다. 본문이 충실한 페이지에도 똑같이 적용되고 있었습니다.

이미 보유한 필드(`organizer`, `title`, `start_date_time`, `end_date_time`)만으로 이렇게 바꿀 수 있습니다.

```ts
// lib/seo/description.ts
const fmt = (iso: string) =>
  new Date(iso).toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric' })

export function buildDescription(e: EventRecord): string {
  const period =
    e.applyStart && e.applyEnd
      ? `${fmt(e.applyStart)}~${fmt(e.applyEnd)} 참가 신청을 받습니다.`
      : e.eventStart
        ? `${fmt(e.eventStart)} 개최.`
        : ''

  const text = `${e.organizer}가 주최하는 '${e.title}'. ${period} 자세한 프로그램과 참가 방법을 확인하세요.`

  // 160자 근처에서 단어 단위로 자름
  return text.length <= 158 ? text : text.slice(0, 155).replace(/\s\S*$/, '') + '…'
}
```

적용 예시:

> OpenAI가 주최하는 'OpenAI DevDay Exchange 2026 - 서울'. 2026년 8월 19일~9월 4일 참가 신청을 받습니다. 자세한 프로그램과 참가 방법을 확인하세요.

템플릿을 설계할 때 확인할 것은 **"이 템플릿으로 만들어진 두 페이지를 나란히 놓았을 때 구별되는가"** 입니다. 주최사와 행사명만 바뀌는 템플릿은 검색엔진 눈에 "같은 문장의 변수만 치환한 페이지"로 보입니다. 날짜, 형식(온라인/오프라인), 비용처럼 **실제로 페이지마다 다른 정보**를 넣을수록 좋습니다.

---

## 5장. 기계가 읽는 데이터

### 구조화 데이터란

사람은 "7월 13일(월) 오전 12:00~오전 10:00"을 읽고 이게 행사 일시라는 걸 압니다. 검색엔진은 그 문자열이 날짜인지, 시작인지 종료인지, 접수 기간인지 개최 일시인지 확신하지 못합니다.

**구조화 데이터(structured data)** 는 이 정보를 기계가 오해 없이 읽을 수 있는 형식으로 페이지에 함께 싣는 것입니다. 어휘는 schema.org가 정의하고, 형식은 **JSON-LD**를 씁니다. Google이 명시적으로 권장하는 형식이고, Microdata나 RDFa보다 HTML과 분리돼 있어 유지보수가 쉽습니다.

```html
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "Event",
  "name": "2026 금융 AI Challenge",
  "startDate": "2026-07-13T00:00:00+09:00",
  "endDate": "2026-09-07T10:00:00+09:00",
  "location": { "@type": "VirtualLocation", "url": "https://..." },
  "organizer": { "@type": "Organization", "name": "금융보안원" }
}
</script>
```

### 왜 넣는가 — 리치 결과

구조화 데이터가 맞게 들어가면 검색 결과가 일반 파란 링크가 아니라 **리치 결과(rich result)** 로 표시될 자격을 얻습니다. 행사라면 날짜·장소가 붙은 이벤트 카드, 상품이라면 가격·평점, 기사라면 썸네일과 날짜입니다.

"자격을 얻는다"는 표현에 주의하세요. 넣었다고 반드시 표시되는 게 아니라, 표시될 **후보**가 되는 것입니다. 표시 여부는 Google이 결정합니다. 그래도 넣지 않으면 후보조차 아닙니다.

데브이벤트는 행사 사이트인데 Event 스키마가 0개였습니다. 날짜·장소·주최·가격 데이터를 전부 보유하면서 기계가 읽을 형태로 내보내지 않은 상태라, 구조화 데이터 영역 점수가 0점이었습니다.

### Event 스키마 — 전체 예시

```json
{
  "@context": "https://schema.org",
  "@type": "Event",
  "name": "2026 금융 AI Challenge",
  "description": "금융보안원에서 주최하는 2026 금융 AI Challenge",
  "url": "https://example.com/item/3131",
  "image": ["https://example.com/thumb/3131.png"],
  "startDate": "2026-07-13T00:00:00+09:00",
  "endDate": "2026-09-07T10:00:00+09:00",
  "eventStatus": "https://schema.org/EventScheduled",
  "eventAttendanceMode": "https://schema.org/MixedEventAttendanceMode",
  "location": [
    {
      "@type": "Place",
      "name": "행사장 이름",
      "address": { "@type": "PostalAddress", "addressCountry": "KR", "streetAddress": "..." }
    },
    {
      "@type": "VirtualLocation",
      "url": "https://외부-행사-사이트"
    }
  ],
  "organizer": { "@type": "Organization", "name": "금융보안원" },
  "offers": {
    "@type": "Offer",
    "price": "0",
    "priceCurrency": "KRW",
    "availability": "https://schema.org/InStock",
    "url": "https://외부-행사-사이트",
    "validFrom": "2026-07-13T00:00:00+09:00"
  }
}
```

#### 필드 해설

| 필드 | 의미 | 주의 |
|---|---|---|
| `startDate` / `endDate` | **행사가 열리는** 일시 | ISO 8601. 접수 기간이 아님 |
| `offers.validFrom` / `validThrough` | **접수(판매)** 기간 | 위와 구분 |
| `eventAttendanceMode` | 온라인 / 오프라인 / 혼합 | 아래 표 |
| `location` | 장소 | 오프라인은 `Place`, 온라인은 `VirtualLocation`, 혼합은 배열 |
| `eventStatus` | 예정 / 취소 / 연기 / 온라인 전환 | 기본 `EventScheduled` |
| `offers.price` | 가격 | 무료는 `"0"`. **모르면 offers 자체를 생략** |
| `image` | 절대 URL 배열 | 상대경로 금지 |

| 사이트 표기 | `eventAttendanceMode` | `location` |
|---|---|---|
| 온라인만 | `OnlineEventAttendanceMode` | `VirtualLocation` + url |
| 오프라인만 | `OfflineEventAttendanceMode` | `Place` + `PostalAddress` |
| 둘 다 | `MixedEventAttendanceMode` | 배열로 둘 다 |

### 틀린 스키마는 없는 스키마보다 나쁘다

이 문장을 이 장의 핵심으로 기억하세요.

페이지에 보이는 내용과 구조화 데이터가 **불일치**하면 Google 스팸 정책상 "구조화 데이터 스팸"으로 취급될 수 있습니다. 화면에는 9월 7일까지라고 써 있는데 스키마에는 7월 13일 하루짜리로 들어가 있으면, 리치 결과를 못 받는 정도가 아니라 사이트 신뢰가 깎입니다.

그래서 **스키마 생성 코드에는 방어 로직이 필수**입니다.

```ts
// lib/schema/event.ts
const SITE = 'https://example.com'

const toAbsolute = (p: string) =>
  /^https?:\/\//i.test(p) ? p : `${SITE}${p.startsWith('/') ? '' : '/'}${p}`

// "부산일보 / 비온미디어 / 안암145" 같은 복수 주최 문자열 분해
const buildOrganizers = (name: string) =>
  name.split('/').map((s) => s.trim()).filter(Boolean)
      .map((n) => ({ '@type': 'Organization' as const, name: n }))

export function buildEventJsonLd(e: EventRecord) {
  const detailUrl = `${SITE}/item/${e.id}`

  const attendanceMode =
    e.isOnline && e.isOffline ? 'https://schema.org/MixedEventAttendanceMode'
    : e.isOnline              ? 'https://schema.org/OnlineEventAttendanceMode'
    :                           'https://schema.org/OfflineEventAttendanceMode'

  const locations: object[] = []
  if (e.isOffline) locations.push({
    '@type': 'Place',
    name: e.venueName || e.organizerName,
    address: { '@type': 'PostalAddress', addressCountry: 'KR',
               ...(e.venueAddress ? { streetAddress: e.venueAddress } : {}) },
  })
  if (e.isOnline) locations.push({
    '@type': 'VirtualLocation', url: e.applyUrl || detailUrl,
  })

  // 방어 1: 날짜를 믿을 수 없으면 채우지 않는다 (endDate는 선택 속성)
  const dates = e.eventStartAt
    ? { startDate: e.eventStartAt, ...(e.eventEndAt ? { endDate: e.eventEndAt } : {}) }
    : {}

  // 방어 2: 가격을 모르는 유료 행사는 offers 자체를 생략
  const offers =
    e.isFree                      ? { '@type': 'Offer', price: '0', priceCurrency: 'KRW',
                                      availability: 'https://schema.org/InStock', url: e.applyUrl }
    : typeof e.price === 'number' ? { '@type': 'Offer', price: String(e.price), priceCurrency: 'KRW',
                                      availability: 'https://schema.org/InStock', url: e.applyUrl }
    :                               undefined

  const organizers = buildOrganizers(e.organizerName)

  return {
    '@context': 'https://schema.org',
    '@type': 'Event',
    name: e.name,
    description: e.description,
    ...dates,
    eventAttendanceMode: attendanceMode,
    eventStatus: 'https://schema.org/EventScheduled',
    location: locations.length === 1 ? locations[0] : locations,
    image: [toAbsolute(e.thumbnailUrl)],
    organizer: organizers.length === 1 ? organizers[0] : organizers,
    offers,
    url: detailUrl,
  }
}
```

```tsx
// pages/item/[id].tsx
// 방어 3: 시작일이 없으면 Event 블록 자체를 넣지 않는다
const jsonLd = event.eventStartAt ? buildEventJsonLd(event) : null

<Head>
  {jsonLd && (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }}
    />
  )}
</Head>
```

세 가지 방어를 정리하면:

1. **`startDate`를 신뢰할 수 없으면 Event 블록을 생략**한다. 필수 속성이 없는 스키마는 검증 실패이고, 틀린 날짜를 넣는 것보다 없는 게 안전합니다.
2. **가격을 모르는 유료 행사는 `offers`를 생략**한다. 빈 문자열이나 0을 넣으면 거짓 정보입니다.
3. **시간을 모르면 날짜만** 넣는다. `"2026-07-13"`은 유효한 값입니다. 틀린 시간보다 없는 시간이 낫습니다.

### 스키마 이전에 데이터 모델을 확인해야 한다

데브이벤트 감사에서 발견한 **선행 조건**이 있습니다. 같은 행사(ID 3131)의 일시가 목록과 상세에서 달랐습니다.

| 위치 | 표기 |
|---|---|
| 목록 카드 | `26.07.13 (월) 00:00 ~ 26.09.07 (월) 10:00` |
| 상세 페이지 | `7월 13일(월) 오전 12:00~오전 10:00` |

상세에서 종료일이 사라지고 시작일에 종료 시각만 붙어 있었습니다. 원인은 데이터 모델에서 **접수 기간**과 **행사 일시**가 한 필드에 섞여 있는 것으로 추정됐습니다.

이 상태로 스키마를 넣으면 `endDate < startDate` 같은 논리 오류나 화면-스키마 불일치가 대량 발생합니다. 그래서 리포트의 실행 순서는 **"H-2 일시 데이터 분리 → 그 다음 H-1 Event 스키마"** 였습니다. 스키마는 데이터 품질을 드러내는 렌즈라서, 스키마 작업을 시작하면 데이터 모델의 결함이 함께 튀어나옵니다.

또 하나. 서버 응답의 `__NEXT_DATA__`를 파싱해 확인한 데이터 최상위 키에는 온라인/오프라인 구분과 무료/유료 필드가 보이지 않았습니다. 화면에는 표시되므로 어딘가 배열 안에 있거나 다른 필드에 인코딩돼 있을 것입니다. **스키마 구현 전에 실제 데이터를 덤프해서 필드 존재를 확인하는 단계**를 건너뛰면 안 됩니다.

### 사이트 전역 스키마 — Organization, WebSite, BreadcrumbList

상세 페이지 스키마와 별개로, 사이트가 누구인지 알려주는 스키마를 넣습니다.

```json
{
  "@context": "https://schema.org",
  "@type": "Organization",
  "name": "데브이벤트",
  "alternateName": "Dev Event",
  "url": "https://example.com",
  "logo": "https://example.com/logo.png",
  "description": "개발자 컨퍼런스, 밋업, 해커톤과 네트워킹 일정을 큐레이션하는 서비스",
  "sameAs": [
    "https://www.instagram.com/<계정>",
    "https://www.threads.com/@<계정>",
    "https://github.com/<조직>",
    "https://play.google.com/store/apps/details?id=<앱ID>"
  ]
}
```

`sameAs`는 "이 조직의 다른 공식 채널"입니다. 검색엔진이 브랜드를 하나의 엔티티로 묶는 데 쓰입니다. **반드시 실제 URL을 확인하고 넣으세요.** 추측한 URL이 실제 계정과 다르면 신뢰 신호가 오히려 나빠집니다. 감사 리포트에도 "정확한 URL은 이번 감사에서 수집하지 않았으므로 확인 후 채우라"고 명시했습니다.

```json
{
  "@context": "https://schema.org",
  "@type": "WebSite",
  "name": "데브이벤트",
  "url": "https://example.com",
  "inLanguage": "ko-KR",
  "publisher": { "@type": "Organization", "name": "데브이벤트" }
}
```

```json
{
  "@context": "https://schema.org",
  "@type": "BreadcrumbList",
  "itemListElement": [
    { "@type": "ListItem", "position": 1, "name": "홈", "item": "https://example.com/" },
    { "@type": "ListItem", "position": 2, "name": "행사", "item": "https://example.com/events" },
    { "@type": "ListItem", "position": 3, "name": "2026 금융 AI Challenge", "item": "https://example.com/item/3131" }
  ]
}
```

BreadcrumbList의 `position`은 1부터 연속이어야 하고 중복이 없어야 합니다.

### 폐기된 스키마 — 넣어도 소용없는 것들

Google은 2023~2026년 사이에 여러 리치 결과를 정리했습니다. 오래된 블로그를 보고 넣으면 헛일입니다.

| 타입 | 상태 | 시점 | 대안 |
|---|---|---|---|
| **HowTo** | 리치 결과 완전 제거 | 2023년 9월 | 없음. 본문을 h2 단계별로 명확하게 쓸 것 |
| **FAQPage** | 리치 결과 **전 사이트** 폐지 | **2026년 5월 7일** | 실제 사용자 Q&A면 `QAPage` |
| SpecialAnnouncement | 폐기 | 2025년 7월 | `Event` 또는 `Article` |
| CourseInfo(캐러셀) | 폐기 | 2025년 6월 | 단일 `Course`는 유효 |
| EstimatedSalary | 폐기 | 2025년 6월 | `JobPosting` + `baseSalary` |
| LearningVideo | 폐기 | 2025년 6월 | `VideoObject` |
| ClaimReview | 폐기 | 2025년 6월 | 없음 |
| VehicleListing | 폐기 | 2025년 6월 | `Product` |
| Practice Problem | 폐기 | 2026년 1월 | 없음 |

FAQPage는 특히 주의하세요. 2023년 8월에 정부·의료 사이트로 제한됐다가, 2026년 5월 7일에 **모든 사이트**에서 리치 결과가 사라졌습니다. 이미 넣어둔 것을 굳이 지울 필요는 없지만, 새로 넣어서 얻을 SERP 이득은 없습니다. "AI가 FAQ 스키마를 읽어서 인용에 유리하다"는 주장도 1차 출처로 확인된 바 없습니다.

`Dataset`은 반대 사례입니다. Google **검색**에는 리치 결과가 없지만 Google **Dataset Search**는 여전히 소비합니다. 폐기된 게 아닙니다.

### 목록 페이지에 `ItemList`를 넣어야 하나

데브이벤트 목록 페이지(`/events`)에는 **권장하지 않았습니다.** 이유가 둘입니다.

1. Google은 개별 상세 페이지의 Event 마크업을 크롤링해 이벤트 캐러셀을 **자체 구성**합니다. 목록 페이지 마크업이 직접적 이득을 주지 않습니다.
2. 그 목록 페이지는 클라이언트 렌더링이었습니다(6장). JSON-LD를 JS로 주입하면 원본 HTML에 없어서 신뢰도가 더 떨어집니다.

목록이 서버 렌더링으로 바뀐 뒤에 재검토하는 것이 순서입니다.

### 구조화 데이터는 서버 렌더 HTML에 있어야 한다

2025년 12월 Google JS SEO 문서: JS로 주입된 구조화 데이터는 **처리가 지연될 수 있습니다.** 특히 시간에 민감한 마크업(Product, Offer, 그리고 행사처럼 마감이 있는 Event)은 초기 서버 렌더 HTML에 포함시켜야 합니다. Next.js에서 `getServerSideProps`나 `getStaticProps`로 데이터를 받아 `<Head>`에 넣으면 서버 HTML에 들어갑니다. `useEffect`에서 넣으면 안 됩니다.

### 검증 — Rich Results Test

- Google Rich Results Test: https://search.google.com/test/rich-results
- Schema.org Validator: https://validator.schema.org/

배포 전에 반드시 돌려보세요. 감사 리포트에는 실패 판정 표를 이렇게 정리했습니다.

| 증상 | 원인 | 심각도 |
|---|---|---|
| "구조화된 데이터를 감지하지 못했습니다" | 스키마 미주입 또는 JS 주입 | Critical |
| "필수 항목 누락: startDate" | 날짜 미채움 | Critical |
| 한글 날짜 문자열 파싱 실패 | `"7월 13일(월)"`을 그대로 넣음 | Critical |
| `endDate < startDate` | 데이터 모델 미분리 상태로 매핑 | Critical |
| "필수 항목 누락: location" | Mixed인데 한쪽만 넣음 | Critical |
| "잘못된 URL 형식" | image에 상대경로 | Critical |
| "가격 형식이 올바르지 않습니다" | 모르는 가격을 빈 문자열로 | Critical |
| "권장 항목 누락: address" | Place에 주소 없음 | Warning |
| BreadcrumbList position 오류 | 비연속·중복 | Warning |

### 체크리스트

1. `@context`는 `"https://schema.org"` (http 아님)
2. `@type`은 유효하고 폐기되지 않은 타입
3. 필수 속성 전부 존재
4. 값 타입이 맞음 (날짜는 ISO 8601, 가격은 문자열 숫자)
5. 플레이스홀더 텍스트 없음 (`[Business Name]` 같은 것)
6. URL은 전부 절대 URL
7. 이미지 URL이 실제로 200을 반환
8. **화면에 보이는 내용과 일치**
9. 서버 렌더 HTML에 포함

---

## 6장. 렌더링 전략

### 세 가지 렌더링 방식과 검색엔진

| 방식 | 언제 HTML이 만들어지나 | 원본 HTML에 본문이 있나 |
|---|---|---|
| **SSG** (Static Site Generation) | 빌드 시 | 있음 |
| **SSR** (Server-Side Rendering) | 요청 시 서버에서 | 있음 |
| **ISR** (Incremental Static Regeneration) | 빌드 시 + 주기적 재생성 | 있음 |
| **CSR** (Client-Side Rendering) | 브라우저에서 JS 실행 후 | **없음** |

SEO 관점에서 결정적인 차이는 마지막 열입니다. CSR 페이지는 원본 HTML에 껍데기만 있고 본문은 JS가 API를 호출해 그립니다.

### Googlebot은 JS를 실행하지만, 다른 크롤러는 아니다

Googlebot은 JS를 실행합니다. 그래서 "CSR도 Google은 색인한다"는 말은 맞습니다. 하지만 두 가지 비용이 있습니다.

1. **렌더 큐 지연.** HTML을 가져온 뒤 JS 실행은 별도 대기열에서 처리되며 며칠 걸릴 수 있습니다. 행사처럼 마감이 있는 콘텐츠는 이 지연이 곧 손실입니다.
2. **다른 크롤러는 JS를 실행하지 않습니다.** GPTBot, ClaudeBot, PerplexityBot, CCBot 등 AI 크롤러 대부분과, 카카오톡·Facebook 등 SNS 미리보기 크롤러가 그렇습니다. 이들에게 CSR 페이지는 빈 페이지입니다.

### 사례: 목록 페이지가 JS 없이는 행사 0건

데브이벤트 `/events`를 두 방식으로 가져와 비교했습니다.

| 상태 | 본문 텍스트 | 행사 건수 | DOM 크기 |
|---|---:|---:|---:|
| 원본 HTML (서버 응답) | 400자 | **0건** | 84,363자 |
| 헤드리스 브라우저 렌더 후 | 8,243자 | 수십 건 | 306,227자 |

원본 HTML에는 서비스 표어와 푸터만 있었습니다. 렌더 후에야 상세 페이지 링크 55개가 DOM에 나타났습니다. Googlebot UA로 요청해도 응답 크기가 동일했으니 봇 전용 프리렌더링 설정도 없었습니다.

이게 뜻하는 것: "이번 주 개발자 컨퍼런스 알려줘"라는 질문에 AI 어시스턴트가 이 사이트를 인용할 근거가 원본 HTML에 없습니다. 상세 페이지는 SSR이 정상이라 개별 행사는 읽히지만, **목록 페이지는 발견 경로로도 콘텐츠로도 기능하지 못합니다.**

### 조치 — 첫 페이지만이라도 서버에서

전면 재작성이 필요한 게 아닙니다. 이 사이트는 이미 `getServerSideProps`에서 `fallbackData`를 pageProps로 내려보내고 있었습니다. 그 데이터로 **초기 목록 20~30건을 서버에서 렌더링**하고, 이후 필터링·무한 스크롤만 클라이언트에서 처리하면 됩니다.

```tsx
// pages/events.tsx (개념 예시)
export const getServerSideProps: GetServerSideProps = async () => {
  const firstPage = await fetchEvents({ page: 1, size: 24 })
  return { props: { firstPage } }
}

export default function Events({ firstPage }: Props) {
  // SWR/React Query의 fallbackData로 넘기면
  // 서버 HTML에는 firstPage가 렌더되고, 클라이언트는 그 위에서 이어받습니다
  const { data } = useEvents({ fallbackData: firstPage })
  return (
    <ul>
      {data.items.map((e) => (
        <li key={e.id}>
          <a href={`/item/${e.id}`}>{e.title}</a>
        </li>
      ))}
    </ul>
  )
}
```

핵심은 `<a href>`가 **서버 HTML에 실제 문자열로 존재**하는 것입니다. `onClick={() => router.push(...)}`로 만든 `<div>`는 크롤러에게 링크가 아닙니다.

#### 실패 판정

```bash
curl -s https://example.com/events | grep -c 'href="/item/'
```

이 값이 0이면 여전히 실패입니다. 20 이상이어야 합니다. 이 한 줄이 CI에 들어가면 회귀를 막을 수 있습니다.

### 2025년 12월 Google JS SEO 가이드 — 네 가지 명시 사항

이 글에서 여러 번 인용한 문서입니다. 한 곳에 정리합니다.

1. **canonical 충돌**: 원본 HTML의 canonical과 JS가 주입한 canonical이 다르면 Google은 **둘 중 아무 것**을 쓸 수 있다. 서버 HTML과 JS 렌더 결과가 동일해야 한다.
2. **noindex와 JS**: 원본 HTML에 `noindex`가 있고 JS가 제거해도 Google은 원본의 noindex를 존중할 수 있다. robots 지시어는 서버에서 확정하라.
3. **200이 아닌 상태 코드**: 4xx/5xx 페이지에서는 JS를 실행하지 않는다. 에러 페이지의 JS 콘텐츠는 보이지 않는다.
4. **구조화 데이터**: JS 주입 스키마는 처리가 지연될 수 있다. 시간 민감 마크업은 서버 HTML에 넣어라.

**한 줄 결론**: title, description, canonical, robots 메타, 구조화 데이터, 그리고 본문 링크는 **서버 렌더 HTML에 최종값이 있어야 합니다.** JS는 그 위에 상호작용을 더하는 역할입니다.

### SPA 프레임워크를 쓰고 있다면

React·Vue·Angular·Svelte로 만든 순수 SPA(빌드 결과가 빈 `index.html` + 번들 JS)는 위 문제를 전부 가집니다. 선택지는 이렇습니다.

| 선택지 | 설명 | 비용 |
|---|---|---|
| 메타 프레임워크로 이전 | Next.js, Nuxt, SvelteKit, Angular SSR | 큼 |
| 프리렌더링 | 빌드 시 주요 경로를 헤드리스 브라우저로 렌더해 정적 HTML 생성 | 중간. 동적 콘텐츠에 약함 |
| 동적 렌더링 | 봇 UA에게만 서버 렌더 HTML 제공 | Google이 공식 권장을 철회한 임시 방편 |
| 엣지 렌더링 | Cloudflare Workers 등에서 HTML 조립 | 중간 |

데브이벤트는 이미 Next.js Pages Router를 쓰고 있어서 상세 페이지는 SSR이 됐고, 목록 한 곳만 CSR이었습니다. 프레임워크가 SSR을 지원하는데 **특정 페이지에서만 CSR로 빠진** 경우는 흔합니다. 데이터 페칭 훅을 `useEffect`에 넣었거나, 로딩 스피너를 먼저 보여주려다 서버 데이터를 안 쓴 경우입니다.

### 확인하는 습관

배포된 페이지를 **브라우저가 아니라 curl로** 열어보는 습관이 이 장의 전부입니다. 브라우저는 JS를 돌린 결과를 보여주므로 "잘 나오는데?"라는 착각을 줍니다.

```bash
# 본문이 있는가
curl -s https://example.com/events | sed 's/<[^>]*>/ /g' | tr -s ' \n' | wc -c

# 링크가 있는가
curl -s https://example.com/events | grep -o 'href="/item/[0-9]*"' | sort -u | wc -l

# 메타 태그가 있는가
curl -s https://example.com/item/123 | grep -E '<title>|name="description"|rel="canonical"|name="robots"|application/ld\+json'
```

여기서 안 보이는 것은 AI 크롤러와 SNS 미리보기에도 안 보입니다.

---

## 7장. 속도

### Core Web Vitals — Google이 순위에 쓰는 유일한 "페이지 경험" 지표

Google은 여러 "페이지 경험" 요소를 언급하지만, **순위에 직접 들어가는 것은 Core Web Vitals(CWV)** 입니다. HTTPS는 확인된 신호지만 매우 가벼워서(전체 질의의 1% 미만에 영향) 사실상 무시해도 됩니다. 보안 헤더는 순위와 무관합니다.

그리고 CWV도 **타이브레이커**입니다. 콘텐츠 품질이 비슷한 경쟁자 사이에서 차이를 만듭니다. 콘텐츠가 부실한데 속도만 빠른 페이지가 순위를 얻는 일은 없습니다.

#### 세 지표

| 지표 | 무엇을 재나 | 좋음 | 개선 필요 | 나쁨 |
|---|---|---|---|---|
| **LCP** (Largest Contentful Paint) | 가장 큰 콘텐츠가 그려지기까지 | ≤ 2.5s | 2.5~4.0s | > 4.0s |
| **INP** (Interaction to Next Paint) | 클릭·입력 후 화면 반응까지 | ≤ 200ms | 200~500ms | > 500ms |
| **CLS** (Cumulative Layout Shift) | 로딩 중 레이아웃이 밀리는 정도 | ≤ 0.1 | 0.1~0.25 | > 0.25 |

몇 가지 확인된 사실:

- **INP가 FID를 대체**했습니다(2024년 3월 12일). FID는 2024년 9월에 CrUX와 PageSpeed Insights에서 제거됐습니다. 아직 FID를 언급하는 자료는 오래된 것입니다.
- 평가는 **실사용자 데이터의 75번째 백분위**로 합니다. 개발자 노트북에서 빠르다고 통과가 아닙니다.
- **기준값은 처음 정의 이후 바뀌지 않았습니다.** "기준이 강화됐다", "CWV 2.0", "Visual Stability Index" 같은 이야기는 SEO 블로그에서만 나오는 것이고 web.dev와 CrUX 릴리스 노트에 없습니다.
- 2026년 5월 CrUX 데이터 기준 전체 오리진의 약 56%가 세 지표를 모두 통과합니다. 절반 가까운 사이트가 통과하지 못한다는 뜻입니다.

#### 필드 데이터 vs 랩 데이터

| 구분 | 출처 | 용도 |
|---|---|---|
| **필드(Field)** | CrUX (Chrome 실사용자), PageSpeed Insights의 상단, Search Console CWV 리포트 | **Google이 순위에 쓰는 것** |
| **랩(Lab)** | Lighthouse, WebPageTest, DevTools | 디버깅 |

Lighthouse 점수가 95점인데 Search Console에서 "개선 필요"가 뜨는 일이 흔합니다. Lighthouse는 시뮬레이션이고, 실사용자는 느린 폰과 느린 네트워크를 씁니다. **필드 데이터가 진실**입니다. 랩은 원인을 찾을 때 씁니다.

트래픽이 적은 사이트는 CrUX 데이터가 집계되지 않아 필드 데이터 자체가 없습니다. 이 경우 Lighthouse로 대리 측정하되 한계를 알고 있어야 합니다.

### LCP 분해 — 어디서 시간이 새는가

2025년 2월부터 CrUX가 LCP를 네 구간으로 나눠 보여줍니다.

```
LCP = TTFB + 리소스 로드 지연 + 리소스 로드 시간 + 요소 렌더 지연
```

| 구간 | 의미 | 목표 |
|---|---|---|
| **TTFB** (Time to First Byte) | 서버가 첫 바이트를 보내기까지 | < 800ms |
| 리소스 로드 지연 | TTFB 이후 LCP 리소스 요청 시작까지 | 최소화 |
| 리소스 로드 시간 | LCP 리소스(보통 이미지) 다운로드 | 크기에 비례 |
| 요소 렌더 지연 | 다운로드 완료 후 그려지기까지 | 최소화 |

이 분해가 유용한 이유는 **어느 구간이 문제인지에 따라 해법이 완전히 다르기 때문**입니다. TTFB가 1초면 이미지를 아무리 압축해도 소용없고, 서버·캐싱을 봐야 합니다.

### 사례: 핵심 페이지만 CDN 캐싱이 꺼져 있었다

데브이벤트의 경로별 응답 헤더입니다.

| 경로 | cache-control | x-vercel-cache | TTFB |
|---|---|---|---|
| `/events` | `private, no-cache, no-store, max-age=0, must-revalidate` | MISS | **0.71~1.62초** |
| `/event/detail/{id}` | `private, no-cache, no-store, …` | MISS | — |
| `/about` | `public, max-age=0, must-revalidate` | HIT | **0.045초** |
| `/sitemap.xml` | `public, max-age=0, must-revalidate` | HIT | — |
| `/_next/static/*` | `public, max-age=31536000, immutable` | HIT | — |

**약 16배 차이**입니다. 그리고 정확히 트래픽이 몰리는 두 경로만 캐싱에서 빠져 있었습니다.

#### 원인 추정

서버 응답의 `pageProps`에 `isLoggedIn` 필드가 있었습니다. `getServerSideProps`에서 쿠키를 읽어 로그인 여부를 확인하면, Next.js는 응답이 사용자별로 다를 수 있다고 판단해 자동으로 `private, no-store`를 붙입니다. 개발자가 의도한 캐싱 정책이 아니라 **부수 효과**일 가능성이 높습니다.

이런 패턴은 흔합니다. 헤더에 "로그인" 버튼 대신 사용자 이름을 보여주려고 서버에서 세션을 읽었을 뿐인데, 그 순간 페이지 전체가 CDN에서 빠집니다.

### Cache-Control 헤더 읽는 법

```
Cache-Control: public, s-maxage=300, stale-while-revalidate=3600
```

| 지시어 | 의미 |
|---|---|
| `public` | 공유 캐시(CDN)에 저장 가능 |
| `private` | 브라우저에만 저장, CDN은 저장 금지 |
| `no-store` | 어디에도 저장 금지 |
| `max-age=N` | 브라우저 캐시 유효 시간(초) |
| `s-maxage=N` | **CDN** 캐시 유효 시간(초). max-age보다 우선 |
| `stale-while-revalidate=N` | 만료 후 N초 동안은 오래된 응답을 주면서 뒤에서 갱신 |
| `immutable` | 절대 안 바뀜. 해시 붙은 정적 파일용 |

`s-maxage=300, stale-while-revalidate=3600`은 "5분간은 캐시에서 바로 주고, 그 뒤 1시간 동안은 오래된 걸 즉시 주면서 백그라운드에서 새로 받아라"입니다. 사용자는 항상 캐시 속도를 경험하고, 콘텐츠는 최대 5분 지연으로 갱신됩니다. 목록·상세 페이지에 적합한 설정입니다.

### 조치 — 개인화를 분리한 뒤 public 캐싱

```ts
// pages/events.tsx
export const getServerSideProps: GetServerSideProps = async ({ res }) => {
  res.setHeader(
    'Cache-Control',
    'public, s-maxage=300, stale-while-revalidate=3600',
  )
  const firstPage = await fetchEvents({ page: 1, size: 24 })
  // isLoggedIn은 여기서 넘기지 않습니다. 클라이언트에서 별도 조회.
  return { props: { firstPage } }
}
```

```tsx
// components/Header.tsx — 로그인 상태는 클라이언트에서
export function Header() {
  const { data: me } = useSWR('/api/me', fetcher) // 이 요청만 private
  return <header>{me ? <UserMenu user={me} /> : <LoginButton />}</header>
}
```

상세 페이지는 변경 빈도가 낮으므로 `s-maxage=3600` 이상도 안전합니다.

#### 순서를 지키지 않으면 사고가 난다

**로그인 사용자 전용 정보가 HTML에 섞여 있는 상태에서 `public` 캐싱을 켜면, 그 HTML이 CDN에 저장되어 다른 사용자에게 노출됩니다.** A가 로그인해서 받은 페이지가 캐시되고, B가 같은 URL을 열면 A의 이름이 보입니다.

그래서 리포트의 실행 순서는 "개인화 분리 → 그 다음 캐시 헤더"였습니다. 순서가 바뀌면 성능 개선이 아니라 정보 유출 사고입니다.

#### 실패 판정

```bash
# 두 번 요청해서 두 번째가 HIT인지
curl -s -D - -o /dev/null https://example.com/events | grep -iE 'cache-control|x-vercel-cache|age:'
curl -s -D - -o /dev/null https://example.com/events | grep -iE 'cache-control|x-vercel-cache|age:'
```

두 번째 응답에서 `x-vercel-cache: HIT`(Vercel 기준. CDN마다 헤더 이름이 다릅니다)이 안 뜨면 실패입니다. 그리고 **로그인 상태로 접속했을 때 다른 사용자의 정보가 보이면 즉시 롤백**입니다.

### 이미지 — CLS와 LCP의 주범

데브이벤트 렌더 후 DOM의 이미지 59개를 분석한 결과입니다.

| 항목 | 값 |
|---|---:|
| `width` + `height` 명시 | **0 / 59** |
| `loading="lazy"` | 1 / 59 |
| `next/image` 최적화 경유 | 1 / 59 |
| PNG 포맷 | 55 / 59 |
| `alt` 누락 | 0 / 59 ✅ |

#### width/height가 없으면 CLS

브라우저는 이미지가 다운로드되기 전까지 그 크기를 모릅니다. `width`/`height`가 없으면 0 높이로 자리를 잡다가 이미지가 오면 아래 콘텐츠를 밀어냅니다. 이게 CLS입니다. 59개 전부 없으니 목록 페이지 스크롤 중 레이아웃이 계속 밀렸을 것입니다.

```html
<!-- 나쁨: 크기 없음 -->
<img src="/thumb.png" alt="...">

<!-- 좋음: 크기 명시. CSS로 width:100%; height:auto를 주면 반응형도 됨 -->
<img src="/thumb.png" alt="..." width="600" height="315">

<!-- 대안: CSS aspect-ratio -->
<img src="/thumb.png" alt="..." style="aspect-ratio: 40/21; width: 100%">
```

#### lazy loading — 단, 첫 화면 이미지는 제외

```html
<!-- 스크롤 아래 이미지: lazy -->
<img src="/thumb-30.png" alt="..." width="600" height="315" loading="lazy" decoding="async">

<!-- 첫 화면(LCP 후보) 이미지: eager + 우선순위 높임 -->
<img src="/hero.png" alt="..." width="1200" height="630" fetchpriority="high">
```

**LCP 이미지에 `loading="lazy"`를 붙이면 LCP가 나빠집니다.** 브라우저가 그 이미지를 뒤로 미루기 때문입니다. 첫 화면에 보이는 이미지는 eager(기본값)로 두고 `fetchpriority="high"`를 줍니다. 나머지는 lazy.

데브이벤트는 58개가 즉시 로딩이어서, 스크롤 한참 아래의 썸네일까지 초기 로드에 포함됐습니다.

#### 포맷

| 포맷 | 지원 | 용도 |
|---|---|---|
| **WebP** | 97%+ | 기본 권장 |
| **AVIF** | 92%+ | 최고 압축, 인코딩 느림 |
| JPEG | 100% | 사진 폴백 |
| PNG | 100% | 투명도 필요한 그래픽 |
| SVG | 100% | 아이콘·로고 |

PNG 55개는 대부분 WebP로 바꾸면 크기가 크게 줄어듭니다.

```html
<picture>
  <source srcset="thumb.avif" type="image/avif">
  <source srcset="thumb.webp" type="image/webp">
  <img src="thumb.jpg" alt="..." width="600" height="315" loading="lazy" decoding="async">
</picture>
```

#### Next.js라면 `next/image` 하나로 해결

`<img>`를 `next/image`로 바꾸면 크기 명시, lazy loading, 포맷 변환(AVIF/WebP), 반응형 srcset이 한 번에 처리됩니다. 외부 도메인(S3 등)은 `next.config.js`에 등록이 필요합니다.

```js
// next.config.js
module.exports = {
  images: {
    remotePatterns: [
      { protocol: 'https', hostname: '<버킷명>.s3.ap-northeast-2.amazonaws.com' },
    ],
    formats: ['image/avif', 'image/webp'],
  },
}
```

```tsx
import Image from 'next/image'

// 목록 썸네일: 위치에 따라 priority 여부 결정
<Image
  src={event.thumbnail}
  alt={`${event.title} 포스터`}
  width={600}
  height={315}
  priority={index < 4}   // 첫 화면에 보이는 몇 개만 eager
/>
```

#### 무의미한 preload 정리

데브이벤트 `<head>`에 SVG를 반응형 srcset으로 preload하는 지시가 있었습니다.

```html
<link rel="preload" as="image"
  imagesrcset="/icon/letter_icon.svg 640w, /icon/letter_icon.svg 750w, ...">
```

SVG는 벡터라 해상도별 소스가 무의미하고, 모든 후보가 같은 파일입니다. 아무 효과가 없는 코드입니다. 이런 건 프레임워크가 자동 생성한 것일 가능성이 높은데, 발견하면 단순 preload로 바꾸거나 제거합니다.

### INP — 클릭했는데 반응이 늦는 문제

| 원인 | 해법 |
|---|---|
| 메인 스레드의 긴 JS 작업 | 50ms 이하로 쪼개기, `scheduler.yield()` |
| 무거운 이벤트 핸들러 | debounce, `requestAnimationFrame` |
| DOM이 너무 큼 (1,500 요소 이상) | 가상 스크롤, 페이지네이션 |
| 서드파티 스크립트 | defer, 지연 로드 |
| 동기 XHR, localStorage 대량 접근 | 비동기화 |

데브이벤트 목록 페이지 렌더 후 DOM이 30만 자였습니다. 무한 스크롤로 계속 쌓이면 DOM 크기가 INP를 끌어내립니다.

### 측정 도구

```bash
# PageSpeed Insights API (필드 + 랩 데이터)
# 공용 할당량이 작아서 API 키 없이 반복 호출하면 곧 막힙니다
curl "https://www.googleapis.com/pagespeedonline/v5/runPagespeed?url=https://example.com&strategy=mobile"

# Lighthouse CLI (랩 데이터)
npx lighthouse https://example.com --output json --output-path report.json
```

- **PageSpeed Insights**: https://pagespeed.web.dev/ — 필드(CrUX)와 랩(Lighthouse)을 한 화면에
- **CrUX Vis**: https://cruxvis.withgoogle.com — 필드 데이터 추이. 예전 CrUX Dashboard(Looker Studio)는 2025년 11월에 종료됐습니다.
- **Search Console > Core Web Vitals**: 사이트 소유자라면 여기가 1차 출처

데브이벤트 감사에서는 PSI 공용 API 할당량 초과로 LCP·INP·CLS 실측을 못 했고, TTFB와 헤드리스 렌더 시간(4.8초)만 확보했습니다. 리포트에 이 한계를 명시했습니다. **못 측정한 것을 측정한 것처럼 쓰지 않는 것**이 감사의 기본입니다.

### 보안 헤더 — 순위와 무관하지만 위생 항목

데브이벤트는 HSTS만 잘 설정돼 있고(`max-age=63072000; includeSubDomains; preload`) 나머지는 없었습니다.

```
content-security-policy   ❌
x-content-type-options    ❌
referrer-policy           ❌
x-frame-options           ❌
permissions-policy        ❌
```

순위에 영향은 없습니다. 하지만 기술 감사 항목이고 한 번에 추가할 수 있습니다.

```js
// next.config.js
const securityHeaders = [
  { key: 'X-Content-Type-Options', value: 'nosniff' },
  { key: 'X-Frame-Options', value: 'SAMEORIGIN' },
  { key: 'Referrer-Policy', value: 'strict-origin-when-cross-origin' },
  { key: 'Permissions-Policy', value: 'camera=(), microphone=(), geolocation=()' },
  // CSP는 인라인 스크립트·외부 도메인을 전부 파악한 뒤 report-only로 먼저 배포하세요
]

module.exports = {
  async headers() {
    return [{ source: '/(.*)', headers: securityHeaders }]
  },
}
```

CSP(Content-Security-Policy)는 잘못 걸면 사이트가 깨지므로 `Content-Security-Policy-Report-Only`로 먼저 배포해 위반 리포트를 모은 뒤 적용합니다.

**측정 시 주의**: Python `urllib`로 헤더를 dict로 바꾸면 대소문자 처리 때문에 있는 헤더를 없다고 판정하는 실수가 나옵니다. 데브이벤트 감사에서 실제로 한 번 겪었고, curl로 재측정해 바로잡았습니다. 헤더 검사는 curl이 가장 믿을 만합니다.

---

## 8장. 콘텐츠 품질

### 배관을 다 놓아도 물이 더러우면

1~7장은 전부 배관입니다. 이 장은 물 자체입니다. 그리고 데브이벤트 감사에서 **가장 정밀하게 측정한 항목**이 이 장의 사례입니다.

### 사례: 상세 페이지의 75%가 본문 0자

ID 2900~3220 구간을 10 간격으로 38건 요청하고, HTML 텍스트가 아니라 서버 응답 데이터의 `description` **원본 필드를 직접 파싱**했습니다. 렌더링 노이즈가 섞이지 않은 값입니다.

| 지표 | 값 |
|---|---|
| 유효 응답 | 36건 (404 2건) |
| **설명 0자(placeholder)** | **27 / 36 = 75.0%** |
| 실콘텐츠 본문 길이 | 최소 410자 · 중앙값 1,253자 · 최대 1,759자 |
| 이미 종료된 행사 | 31 / 36 = 86.1% |
| 종료 ∩ 설명 0자 | 24 / 27 = 88.9% |

**분포에 중간이 없었습니다.** 0자 아니면 400자 이상. 이건 점진적 품질 향상이 아니라 **특정 시점에 운영 프로세스가 통째로 교체된 패턴**입니다.

전환점도 특정됐습니다.

| ID | 등록일 | 설명 길이 |
|---|---|---:|
| 3131 | 07-13 | 0자 |
| **3140** | **07-21** | **0자 — 마지막 placeholder** |
| **3150** | **07-28** | **410자 — 최초 실콘텐츠** |
| 3160 | 08-04 | 1,759자 |

2026년 7월 21일~28일 사이에 무언가 바뀌었습니다. 저장소 커밋 기록의 등록 일자와 서버 데이터의 생성 시각이 정확히 일치해 필드 신뢰도도 확인됐습니다.

이 발견이 중요한 이유: **앞으로 등록되는 행사는 이미 문제가 없습니다.** 남은 일은 (a) 과거 재고 정리와 (b) 새로 쌓이는 좋은 콘텐츠가 검색엔진에 제대로 전달되도록 배관을 놓는 것입니다. 진단이 "콘텐츠가 부실하다"에서 "과거 재고 정리"로 바뀌면 조치의 규모와 방향이 완전히 달라집니다.

### 왜 빈 페이지가 사이트 전체를 끌어내리나

Google `site:` 검색에서 이런 스니펫이 실제로 노출되고 있었습니다.

> "일시4월 24일(금) … 참여하기. **행사 상세 내용은 준비중입니다.** '참여하기' 버튼을 눌러서 상세 내용을 확인 …"

검색 결과에 "준비중입니다"가 보이면 클릭할 이유가 없습니다. 이것만으로도 문제인데, 더 큰 문제가 있습니다.

2024년 3월 코어 업데이트에서 Google은 별도로 운영하던 **Helpful Content System을 코어 랭킹에 통합**했습니다. 그 결과 도움이 되는 콘텐츠인지에 대한 평가가 개별 페이지를 넘어 **사이트 전체 품질 신호**로 작동합니다. 색인된 페이지의 다수가 빈 껍데기면, 충실한 신규 페이지까지 함께 끌어내려집니다.

그리고 Google은 2025년 12월부터 **대형 코어 업데이트 사이에도 작은 미공개 업데이트를 지속적으로** 한다고 문서화했습니다. 콘텐츠 품질 평가는 이제 "다음 업데이트 때"가 아니라 상시입니다.

### 얇은 콘텐츠(thin content)의 기준

"단어 수"로 판단하면 틀립니다. Google은 단어 수가 순위 요소가 아니라고 확인했습니다. 500자로 질문에 완전히 답하는 페이지가 2,000자로 빙빙 도는 페이지를 이깁니다.

그래도 감사 도구들이 쓰는 **주제 커버리지 하한선**은 있습니다. 목표가 아니라 "이보다 적으면 주제를 충분히 다뤘는지 의심해봐야 한다"는 바닥입니다.

| 페이지 유형 | 최소 단어(영문 기준) | 고유 콘텐츠 비율 |
|---|---:|---:|
| 홈 | 500 | 100% |
| 서비스·기능 소개 | 800 | 100% |
| 블로그 글 | 1,500 | 100% |
| 상품 페이지 | 400 | 80%+ |
| 카테고리 페이지 | 400 (목록 외 고유 소개문) | 100% |
| 소개(About) | 400 | 100% |

한글은 영문보다 정보 밀도가 높아 글자 수를 그대로 대응시킬 수 없습니다. 데브이벤트 실콘텐츠 중앙값 1,253자는 행사 안내 페이지로는 충분한 분량입니다.

**진짜 기준은 이 질문입니다: "이 페이지가 다른 유사 페이지가 하나도 없더라도 발행할 가치가 있는가?"** 행사명·주최·링크만 있고 본문이 "준비중"인 페이지는 이 질문에 답하지 못합니다.

### E-E-A-T — 경험, 전문성, 권위, 신뢰

Google 검색 품질 평가 가이드라인(QRG)의 핵심 개념입니다. 순위 점수가 아니라 **평가자가 콘텐츠를 판단하는 관점**이고, 이 관점이 랭킹 시스템 학습에 반영됩니다.

| 요소 | 뜻 | 신호 예시 |
|---|---|---|
| **Experience** (경험) | 직접 해봤는가 | 원본 사진·스크린샷, 구체적 사례, 과정 기록 |
| **Expertise** (전문성) | 아는 사람이 썼는가 | 작성자 소개, 정확한 용어, 근거 있는 주장 |
| **Authoritativeness** (권위) | 남들이 인정하는가 | 외부 인용, 언론 언급, 업계 인지도 |
| **Trustworthiness** (신뢰) | 믿을 수 있는가 | 연락처, 개인정보처리방침, HTTPS, 수정 이력 |

Google 스스로 **"신뢰가 가장 중요한 구성원"** 이라고 표현합니다. 나머지 셋은 신뢰를 뒷받침합니다.

#### Who / How / Why — Google이 제시한 자가 점검

Google의 "도움이 되는 콘텐츠 만들기" 문서가 제시하는 세 질문입니다.

| 질문 | 확인할 것 |
|---|---|
| **Who** — 누가 만들었나 | 바이라인, 작성자 소개, 조직 정보. 독자가 기대하는 곳에 있어야 함 |
| **How** — 어떻게 만들었나 | 제작 과정, 특히 AI 도구를 썼다면 그 사실. 1차 자료·직접 경험 |
| **Why** — 왜 존재하나 | "사람을 돕기 위해"인가, "검색 클릭을 끌기 위해"인가 |

Google이 나열한 **경고 신호**:

- 목표 단어 수에 맞춰 쓰기 (그런 목표는 없습니다)
- 전문성 없는 분야에 트래픽만 노리고 진입
- 발행일을 조작해 신선한 척하기
- "신선도 신호"를 위한 대량 콘텐츠 갈아엎기

#### 큐레이션 사이트의 E-E-A-T

데브이벤트 같은 디렉터리는 개인 저자가 없습니다. 이 경우 E-E-A-T는 **조직 수준**에서 만듭니다.

- `/about`에 운영 주체, 목적, 연락 채널을 명확히
- Organization 스키마의 `sameAs`로 공식 채널(Instagram, Threads, GitHub, 앱스토어)을 연결
- 행사 정보의 **출처(공식 링크)** 를 항상 명시 — 이게 큐레이션의 신뢰 근거
- 수정 이력이나 "마지막 확인" 날짜

### AI 생성 콘텐츠 — 문제는 AI가 아니라 가치 없음

2025년 9월 QRG 갱신에서 평가자들은 콘텐츠가 AI로 생성된 것으로 보이는지를 공식적으로 판단하게 됐습니다. 그러나 결론은 **"AI 사용 자체는 감점이 아니다"** 입니다.

| 허용되는 AI 콘텐츠 | 저품질 AI 콘텐츠 표식 |
|---|---|
| 진짜 E-E-A-T를 보여줌 | 구체성 없는 일반론 |
| 고유한 가치 제공 | 독창적 통찰 없음 |
| 사람이 검수·편집 | 페이지 간 반복 구조 |
| 원본 인사이트 포함 | 작성자 표시 없음, 사실 오류 |

Google 스팸 정책(2026년 5월 갱신)은 **"생성형 AI로 가치를 더하지 않고 많은 페이지를 만드는 것"** 을 명시적으로 **확장 콘텐츠 남용(scaled content abuse)** 으로 규정합니다. 동의어 치환이나 자동 번역만 한 것도 포함입니다.

데브이벤트 프로젝트처럼 AI로 행사 설명을 생성해 등록하는 파이프라인이라면, 이 기준이 직접 적용됩니다. 주최사 공식 페이지에서 확인한 사실(일정·장소·대상·비용·프로그램)을 구조화해 담는 것은 가치를 더하는 일이고, 행사명만 바꿔 같은 문장을 반복하는 것은 남용입니다. 7월 말 이후 등록분이 "이런 분께 추천해요 / 다루는 내용 / 참가 안내" 구조로 1,000자 이상을 담고 있다면 전자에 해당합니다.

### 조치 순서 — noindex보다 문구 교체가 먼저

빈 페이지 2,000여 건을 어떻게 할 것인가. 직관적으로는 "전부 noindex"가 떠오르지만, 리포트는 **반대**했습니다.

| 단계 | 조치 | 대상 | 비용 |
|---|---|---|---|
| **1 (즉시)** | "준비중입니다"를 **주최사·일시·링크 기반 자동 요약 한 문장**으로 교체 | placeholder 전량 | 낮음. DB 마이그레이션 불필요, 렌더링 로직만 |
| 2 (1~2주) | description 템플릿 정보량 확대 (4장) | 전체 | 낮음 |
| 3 (분기) | 종료 12개월 초과 **AND** 1단계 후에도 보강 없음 **AND** Search Console 유입 저조 — **세 조건 모두** 충족 시에만 `noindex, follow` | 소수 | 중간 (GSC 데이터 필요) |
| 4 (운영) | description 필수 입력을 등록 프로세스 표준으로 | 신규분 | 낮음. 이미 사실상 전환됨 |

일괄 noindex에 반대한 이유 두 가지:

1. **종료된 행사도 롱테일 재검색 수요가 있습니다.** "작년 PyCon Korea 일정"을 찾는 사람은 있습니다.
2. **1단계만으로 noindex 대상이 크게 줄어듭니다.** "빈 페이지"가 "짧지만 사실을 담은 페이지"로 바뀌면, 그건 얇은 콘텐츠가 아니라 짧은 콘텐츠입니다. 3단계는 1단계를 하고 나서 재평가해야 합니다.

1단계 구현은 이런 모양입니다.

```ts
// lib/content/fallbackSummary.ts
export function fallbackSummary(e: EventRecord): string {
  const when = e.eventStart ? `${fmt(e.eventStart)}${e.eventEnd ? `~${fmt(e.eventEnd)}` : ''}에 ` : ''
  const mode = e.isOnline && e.isOffline ? '온·오프라인 동시' : e.isOnline ? '온라인' : e.venueCity ? `${e.venueCity}에서 ` : ''
  const fee = e.isFree ? '무료로 ' : ''
  return `${e.organizer}가 ${when}${mode} ${fee}진행하는 '${e.title}'입니다. 세부 프로그램과 신청 방법은 주최사 페이지에서 확인할 수 있습니다.`
}

// 렌더링
const body = e.description?.trim() || fallbackSummary(e)
```

"준비중입니다"라는 **부정적 신호**를 **사실 기반 문장**으로 바꾸는 것만으로 SERP 스니펫이 달라집니다. 데이터베이스는 건드리지 않습니다.

**실패 판정**: 3개월 후 `site:` 검색에서 "준비중입니다" 스니펫이 여전히 나오면 실패입니다.

### 프로그래매틱 SEO — 데이터로 페이지를 찍어낼 때의 규칙

데브이벤트처럼 DB 레코드 하나가 페이지 하나가 되는 구조를 **프로그래매틱 SEO**라고 부릅니다. 규모가 나오는 대신 얇은 콘텐츠 위험이 항상 따라옵니다.

#### 안전한 것과 위험한 것

| 대규모로 안전 ✅ | 처벌 위험 ❌ |
|---|---|
| 통합 가이드 페이지 (실제 설정 문서) | 도시 이름만 바꾼 지역 페이지 |
| 템플릿·도구 페이지 (다운로드 가능) | "[업종]을 위한 최고의 [도구]" (업종별 가치 없음) |
| 용어집 (200자 이상 고유 정의) | "[경쟁사] 대안" (실제 비교 데이터 없음) |
| 상품 페이지 (고유 스펙·리뷰) | 검수 없는 AI 대량 생성 |
| 데이터 기반 페이지 (레코드별 고유 통계) | 템플릿 보일러플레이트가 60% 넘는 페이지 |

#### 품질 게이트

| 지표 | 기준 | 조치 |
|---|---|---|
| 검수 없는 페이지 | 100건 이상 | ⚠️ 발행 전 콘텐츠 감사 필요 |
| 근거 없는 페이지 | 500건 이상 | 🛑 명시적 승인 + 얇은 콘텐츠 감사 |
| 페이지당 고유 콘텐츠 | 40% 미만 | ❌ 얇은 콘텐츠 판정 |
| | 30% 미만 | 🛑 확장 콘텐츠 남용 위험 |

고유 콘텐츠 비율 = (이 페이지에만 있는 단어) ÷ (페이지 전체 단어). 헤더·푸터·네비게이션은 제외하고, **템플릿 보일러플레이트 텍스트는 포함**합니다.

#### 점진적 배포

- 50~100건 단위로 발행하고 2~4주 색인·순위를 관찰한 뒤 확장
- 발행 전 5~10% 표본을 사람이 검수
- 500건 이상을 한 번에 발행하지 않음

#### 각 페이지가 통과해야 하는 시험

> "다른 유사 페이지가 하나도 없더라도, 이 페이지는 발행할 가치가 있는가?"

행사 상세 페이지에 대입하면: 그 행사에 관심 있는 사람이 이 페이지 하나만 보고 "가야 할지" 판단할 수 있는가. 일정·장소·대상·비용·신청 방법·프로그램이 있으면 예, "준비중"이면 아니오.

### 신선도 — 언제 갱신하나

| 콘텐츠 유형 | 갱신 주기 |
|---|---|
| 뉴스·시사 | 수 시간~수 일 |
| 에버그린 블로그 | 연 1회 검토 |
| 상품 페이지 | 스펙 변경 시 |
| 서비스 페이지 | 분기 검토 |
| 회사 정보 | 변경 시 |

AI 검색 인용에서는 신선도가 더 중요합니다. 3개월 이내 콘텐츠가 인용 확률이 약 3배 높고, 6개월 이상 방치된 페이지는 인용 자격을 잃는다는 연구가 있습니다(SE Ranking). 행사처럼 시한이 있는 콘텐츠는 `eventStatus`를 갱신하고(취소·연기·종료), 종료 후에는 "종료된 행사입니다. 다음 회차는 …"처럼 상태를 명시하는 것이 신선도 신호이자 사용자 배려입니다.

**단, 발행일 조작은 경고 신호입니다.** 내용을 바꾸지 않고 날짜만 갱신하는 것은 Google이 명시한 나쁜 관행입니다.

---

## 9장. 사이트 구조

### 2계층 사이트의 한계

데브이벤트의 구조는 이랬습니다.

```
/events  →  /event/detail/{id}
(목록)      (상세 약 3,000건)
```

그 사이에 아무것도 없습니다. 카테고리·형식·지역·시기별 페이지가 없습니다. 필터는 `/search?tag=…`로 동작하지만 3장에서 본 대로 색인 대상이 될 수 없는 상태였습니다.

이게 왜 문제인지는 **검색 질의와 대응 페이지**를 나열하면 보입니다.

| 검색 질의 | 현재 대응 페이지 | 상태 |
|---|---|---|
| 개발자 컨퍼런스 2026 | `/events` | 범용 페이지로 부분 대응 |
| 무료 개발자 웨비나 | 없음 | ❌ |
| AI 해커톤 모집 | 없음 | ❌ |
| 온라인 개발자 세미나 | 없음 | ❌ |
| 부산 개발자 행사 | 없음 | ❌ |
| 클라우드 밋업 일정 | 없음 | ❌ |

각 질의는 실제 검색 의도입니다. 그 의도에 정확히 대응하는 페이지가 없으면, 상세 페이지 3,000건이 있어도 이 질의들에서 순위를 얻을 수 없습니다. 개별 행사 페이지는 그 행사 이름으로만 검색됩니다.

### 태그 랜딩 페이지 — 중간 계층 만들기

색인 가능한 **정적 경로**로 중간 계층을 만듭니다.

```
/events/tag/ai
/events/tag/cloud
/events/tag/security
/events/type/hackathon
/events/type/conference
/events/type/webinar
/events/free
/events/online
/events/city/busan
```

Next.js에서는 `pages/events/tag/[tag].tsx`에 `getStaticPaths`로 허용 태그 목록을 빌드 시 고정하면 됩니다.

```ts
// pages/events/tag/[tag].tsx
export const getStaticPaths: GetStaticPaths = async () => {
  const tags = await fetchTagsWithCount()
  return {
    // 규칙 2: 행사 10건 이상인 태그만 페이지 생성
    paths: tags.filter((t) => t.count >= 10).map((t) => ({ params: { tag: t.slug } })),
    fallback: 'blocking',
  }
}

export const getStaticProps: GetStaticProps = async ({ params }) => {
  const tag = await fetchTag(params!.tag as string)
  if (!tag || tag.count < 10) return { notFound: true }
  const events = await fetchEventsByTag(tag.slug, { limit: 50 })
  return { props: { tag, events }, revalidate: 3600 }
}
```

### 조합 폭발을 막는 네 가지 규칙

이 규칙이 없으면 태그 랜딩은 곧 크롤 트랩이 됩니다.

1. **단일 축만 허용.** `/events/tag/ai`는 만들되 `/events/tag/ai/free/online`은 만들지 않습니다. 태그 20개 × 형식 4개 × 무료 여부 2 × 온라인 여부 2 = 320페이지가 되고, 대부분 항목이 0~2건입니다.
2. **최소 콘텐츠 기준: 행사 10건 이상.** 3건짜리 태그 페이지는 얇은 페이지입니다.
3. **2축 이상 조합은 `/search`로.** noindex 상태로 두고 사용자 편의 기능으로만 씁니다.
4. **각 랜딩에 고유한 소개문 2~3문단.** 목록만 있는 페이지는 다른 태그 페이지와 구별되지 않습니다. "AI 행사 페이지"라면 이 카테고리에 어떤 행사가 주로 오는지, 어떤 사람에게 맞는지, 최근 흐름은 어떤지를 씁니다.

이 규칙을 지키면 생성되는 페이지는 15~20개 수준입니다. 관리 가능하고, 각각이 실질적인 검색 의도에 대응합니다.

**실패 판정**: 3개월 후 Search Console에서 이 URL들의 노출수가 0에 머물면 실패입니다. 페이지는 만들었는데 소개문 없이 목록만 있는 경우가 대부분의 실패 원인입니다.

### 허브 앤 스포크 — 콘텐츠 사이트의 구조

블로그·미디어처럼 글이 자산인 사이트는 **허브(pillar)와 스포크(spoke)** 구조를 씁니다.

```
                [스포크 1a] --- [스포크 1b]
                     \       /
                  [클러스터 1]
                       |
[스포크 2a] -- [클러스터 2] -- [허브] -- [클러스터 3] -- [스포크 3a]
[스포크 2b] /                                    \ [스포크 3b]
```

| 역할 | 분량 | 키워드 | 링크 |
|---|---|---|---|
| **허브** | 2,500~4,000 단어 | 가장 넓고 검색량 큰 것 | 모든 스포크로 링크 (필수) |
| **스포크** | 1,200~1,800 단어 | 세부 주제 (페이지마다 고유) | 허브로 링크 (필수) + 같은 클러스터 스포크 2~3개 |

규칙:

- 허브 1개당 클러스터 2~5개, 클러스터당 글 2~4개
- 모든 글은 최소 3개의 내부 링크를 받음
- 고아 페이지 없음 (허브에서 2클릭 안에 도달)
- **두 글이 같은 주 키워드를 노리지 않음** — 이걸 어기면 **키워드 카니발리제이션**(자기 페이지끼리 경쟁)이 일어납니다

### SERP 겹침으로 클러스터 나누기

"어떤 키워드들을 한 페이지에서 다루고, 어떤 것은 따로 만들어야 하나"를 텍스트 유사도로 판단하면 틀립니다. "강아지 훈련 팁"과 "강아지 훈련 교실"은 글자는 비슷하지만 검색 결과는 전혀 다를 수 있습니다.

기준은 **Google이 실제로 보여주는 결과의 겹침**입니다. 두 키워드를 검색해서 상위 10개 URL 중 몇 개가 겹치는지 셉니다.

| 겹치는 URL 수 | 관계 | 조치 |
|---|---|---|
| 7~10 | 같은 글 | 한 페이지로 합침 |
| 4~6 | 같은 클러스터 | 같은 클러스터 안의 별도 글 |
| 2~3 | 연결 | 인접 클러스터, 상호 링크 |
| 0~1 | 별개 | 다른 클러스터 또는 제외 |

Google이 같은 페이지들을 보여준다면 Google은 그 두 질의를 같은 의도로 보는 것이고, 그럼 페이지도 하나여야 합니다.

### SXO — 기술 점수 95점인데 순위가 안 나오는 이유

**Search Experience Optimization**은 이 질문을 던집니다. "이 페이지는 이 키워드에서 Google이 실제로 보상하는 **페이지 유형**인가?"

Google이 어떤 키워드에 상품 페이지 8개와 비교 페이지 2개를 보여주고 있다면, 그 키워드에 블로그 글로 진입하는 것은 아무리 잘 써도 어렵습니다. **페이지 유형 불일치**입니다.

| 내 페이지 | SERP가 기대하는 것 | 심각도 |
|---|---|---|
| 블로그 글 | 상품 페이지 | Critical |
| 블로그 글 | 비교 페이지 | High |
| 상품 페이지 | 정보성 콘텐츠 | High |
| 랜딩 페이지 | 도구·계산기 | High |
| 유형 일치 | — | 정상. 깊이와 UX에 집중 |

확인하는 방법은 단순합니다. **목표 키워드를 직접 검색해서 상위 10개가 어떤 유형의 페이지인지 분류**해보세요. 60% 이상이 한 유형이면 강한 합의이고, 그 유형이 아니면 진입이 어렵습니다. 40% 미만으로 갈라져 있으면 차별화 기회입니다.

데브이벤트에 대입하면: "개발자 컨퍼런스 2026"을 검색했을 때 상위에 개별 행사 공식 페이지가 많은지, 큐레이션 목록이 많은지, 블로그 정리글이 많은지에 따라 `/events` 페이지의 설계 방향이 달라집니다. 목록형이 많다면 목록 페이지의 소개문과 구조가 중요하고, 개별 행사가 많다면 상세 페이지의 스키마와 깊이가 중요합니다.

### 내부 링크 — 구조를 검색엔진에게 전달하는 방법

사이트 구조는 URL 경로만으로 전달되지 않습니다. **링크**가 전달합니다.

- 상세 페이지에서 소속 태그 랜딩으로 링크 (스포크 → 허브)
- 태그 랜딩에서 소속 상세 페이지로 링크 (허브 → 스포크)
- 상세 페이지에서 관련 행사 3~5건으로 링크 (같은 태그, 같은 주최사, 같은 시기)
- BreadcrumbList 스키마와 화면의 브레드크럼이 일치

```tsx
// 상세 페이지 하단
<nav aria-label="관련 행사">
  <h2>비슷한 행사</h2>
  <ul>
    {related.map((r) => (
      <li key={r.id}><a href={`/item/${r.id}`}>{r.title}</a></li>
    ))}
  </ul>
</nav>
```

`related`는 같은 태그 + 진행 중 + 최근 등록 순 같은 단순한 규칙으로 뽑아도 충분합니다. 없는 것보다 훨씬 낫습니다.

---

## 10장. AI 검색 시대의 SEO

### GEO, AEO — 새로운 것인가

2024년부터 "GEO(Generative Engine Optimization)", "AEO(Answer Engine Optimization)"라는 용어가 유행했습니다. AI Overview, ChatGPT 검색, Perplexity에 인용되기 위한 별도 최적화가 필요하다는 주장입니다.

Google은 2026년 5월 **AI 최적화 가이드**를 공식 문서로 발행해 이 논쟁을 정리했습니다. 요지는 한 문장입니다.

> "생성형 AI 검색을 위한 최적화는 Google 관점에서 **여전히 SEO**다. AEO와 GEO는 같은 일에 다른 이름을 붙인 것이다."

AI Overview와 AI Mode는 기존 검색과 **같은 랭킹·품질 시스템에 기반**합니다. 그 위에 두 기술이 얹힙니다.

1. **RAG(검색 증강 생성)**: 색인된 페이지를 검색해 답을 생성하고 출처 링크를 붙임
2. **질의 확장(query fan-out)**: 원래 질의에서 관련 하위 질의 여러 개를 만들어 추가 결과를 끌어옴

그리고 다시 **자격 하한선**: 페이지가 색인되어 있고 스니펫 표시 가능한 상태여야 어떤 AI 기능에도 나올 수 있습니다. 별도의 AI 색인은 없습니다.

### Google이 명시적으로 "필요 없다"고 한 것들

이 목록이 이 장에서 가장 실용적인 부분입니다. 시간과 돈을 아껴줍니다.

| Google이 거부한 주장 | 실제 |
|---|---|
| `llms.txt` 또는 AI 전용 마크업 파일을 만들어라 | Google 검색은 **무시**함. 있어도 없어도 순위에 영향 없음 |
| 콘텐츠를 AI가 읽기 좋게 작은 조각으로 "청킹"하라 | 불필요 |
| AI를 위한 특정 문구나 롱테일 변형으로 다시 써라 | 불필요 |
| 블로그·포럼·영상에서 브랜드 언급을 인위적으로 늘려라 | 도움 안 됨 |
| AI 기능을 위해 구조화 데이터에 과잉 투자하라 | 불필요 |

대신 Google이 말하는 것은 **"고유하고, 범용적이지 않고, 직접 경험에 기반한 콘텐츠"** 입니다. Google이 든 대조 예시: "첫 주택 구매자를 위한 7가지 팁"(범용)과 "우리가 점검을 생략하고 돈을 아낀 이유: 하수관 안을 들여다본 기록"(경험). 후자가 인용됩니다.

#### llms.txt에 대한 증거

`/llms.txt`는 사이트 루트에 마크다운으로 주요 페이지를 나열하는 제안 표준입니다. 개발자 도구 문서 사이트에서 유행했습니다. 증거를 정리하면:

| 출처 | 시점 | 내용 |
|---|---|---|
| Google AI 최적화 가이드 | 2026-06 | "Google 검색은 이 파일을 무시한다" |
| John Mueller (Google) | 2025~2026 | "어떤 AI 시스템도 현재 llms.txt를 쓰지 않는다", 메타 키워드와 비교 |
| Gary Illyes (Google) | 2025-07 | 지원 계획 없음 |
| SE Ranking 30만 도메인 연구 | 2025-11 | AI 인용 상위 50 도메인 중 llms.txt 보유는 **1개** |
| OtterlyAI 서버 로그 | 2025 | AI 봇 트래픽의 **0.1%** 만 llms.txt 요청 |

예외가 하나 있습니다. **AI 코딩 에이전트**(Cursor, Claude Code 등)는 라이브러리 문서를 로드할 때 llms.txt를 실제로 읽습니다. 개발자 도구 문서 사이트라면 만들 가치가 있습니다. 일반 서비스 사이트에는 비용 0의 선택 사항이고, 우선순위는 낮습니다. 데브이벤트 리포트에서도 "404이지만 우선순위 낮음"으로 분류했습니다.

### 그래도 AI 검색에서 다른 점

"전부 SEO다"가 결론이라면 이 장은 여기서 끝나야 합니다. 하지만 **같은 시스템 위에서도 AI 표면이 선택하는 방식에는 차이**가 있고, 그건 알아둘 가치가 있습니다.

#### 두 개의 Google AI 엔진

| 표면 | 인용 성향 |
|---|---|
| **AI Overview** | 기존 순위와 강하게 상관. 이미 잘 랭크되는 페이지를 인용 |
| **AI Mode** | 순위와 약하게 상관. 더 넓은 풀(질의당 약 9개 도메인)에서 신선도·엔티티 권위 중심 |

Ahrefs의 54만 질의 쌍 연구에 따르면 둘은 같은 결론에 86% 도달하지만 **같은 URL을 인용하는 비율은 13.7%** 에 그칩니다. 2026년 5월 Google I/O에서 UX는 하나로 통합됐지만, 인용 엔진은 기술적으로 별개입니다.

실용적 의미: AI Overview는 기존 SEO를 잘하면 따라옵니다. AI Mode는 5위 밖의 페이지에도 기회를 주는데, 그 기준이 신선도와 엔티티 명확성입니다.

#### 플랫폼별 인용 출처

| 플랫폼 | 주요 인용 출처 |
|---|---|
| ChatGPT | Wikipedia (47.9%), Reddit (11.3%) |
| Perplexity | Reddit (46.7%), Wikipedia |
| Bing Copilot | Bing 색인, IndexNow |

ChatGPT와 Google AI Overview가 **같은 질의에 같은 도메인을 인용하는 비율은 11%** 입니다. 한 플랫폼에서의 노출이 다른 플랫폼을 보장하지 않습니다.

#### 브랜드 언급이 백링크보다 3배 중요

Ahrefs의 7만 5천 브랜드 연구(2025년 12월)에서 AI 가시성과의 상관관계:

| 신호 | 상관계수 |
|---|---|
| YouTube 언급 | ~0.737 (가장 강함) |
| Reddit 언급 | 높음 |
| Wikipedia 존재 | 높음 |
| LinkedIn 존재 | 중간 |
| 백링크 기반 Domain Rating | ~0.266 (약함) |

전통 SEO의 백링크 구축 예산을 **브랜드가 실제로 언급되는 곳**(커뮤니티, 영상, 위키)으로 재배치하라는 뜻입니다. 단, Google이 "인위적 언급 늘리기는 도움 안 된다"고 했으니 이건 홍보와 커뮤니티 참여의 영역이고, 조작의 영역이 아닙니다.

### 인용되기 쉬운 문단의 형태

이건 Google이 "필요 없다"고 한 "AI를 위한 재작성"과 다릅니다. **사람에게도 좋은 글의 형태**이고, 그게 기계에게도 좋은 것입니다.

- **답을 먼저.** 각 섹션 첫 40~60단어에 직접 답이 있어야 합니다.
- **자기 완결적 블록.** 앞뒤 문맥 없이 그 문단만 뽑아도 뜻이 통해야 합니다. 최적 길이는 134~167단어(영문 기준)라는 연구가 있습니다.
- **정의 패턴.** "X는 …이다", "X란 …를 가리킨다".
- **구체적 수치와 출처.** "많은 사이트가"보다 "CrUX 기준 56%의 오리진이".
- **질문형 소제목.** 사람들이 실제로 묻는 형태.
- **표와 목록.** 비교 데이터는 표로.
- **앞부분에 핵심.** AI 인용의 약 44%가 페이지 상위 30%에서 나옵니다.

데브이벤트에 대입하면 4장에서 말한 **"행사 요약" h2를 맨 위로** 옮기는 것이 정확히 이 원칙입니다.

### AI 크롤러는 JS를 실행하지 않는다 — 다시

6장에서 다뤘지만 이 장의 맥락에서 다시 강조합니다. **AI 크롤러는 대부분 JS를 실행하지 않습니다.** 서버 렌더 HTML에 없는 내용은 AI에게 존재하지 않습니다.

데브이벤트 목록 페이지가 서버 HTML에 행사 0건이었다는 것은, "이번 주 개발자 컨퍼런스"를 묻는 사용자에게 AI 어시스턴트가 이 사이트를 인용할 근거가 없다는 뜻입니다. 상세 페이지는 SSR이 정상이니 개별 행사명으로 물으면 나올 수 있지만, 목록 성격의 질문에서는 빠집니다.

AI 검색 대응 영역이 35점이었던 이유는 llms.txt가 없어서가 아니라 **목록 페이지가 CSR이어서**입니다.

### 에이전트 친화적 페이지 — 다음 단계

Google의 AI 최적화 가이드는 후반에서 **요약이 아니라 행동하는 AI 에이전트**로 초점을 옮깁니다. 검색하고, 비교하고, 예약하고, 구매하는 에이전트입니다. 에이전트는 세 채널로 사이트를 읽습니다.

1. 스크린샷 + 비전 모델
2. 원본 HTML/DOM
3. **접근성 트리(accessibility tree)** — 가장 깨끗한 신호

Lighthouse 13.3부터 **Agentic Browsing** 카테고리가 기본으로 켜져 있습니다(Chrome 150+). 점검 항목은 결국 **접근성 기본**입니다.

| 통과 | 실패 |
|---|---|
| `<button>` | `<div onclick>` |
| `<a href>` | `<div onclick="location.href=…">` |
| `<label for>` 연결된 입력 | 레이블 없는 입력 |
| 24×24px 이상 클릭 대상 | 그보다 작은 것 |
| 페이지 간 동일 위치의 동일 기능 | 템플릿마다 위치가 바뀌는 CTA |
| 실제 `<nav>`, `<main>`, `<article>` | 전부 `<div>` |

이건 새 기술이 아닙니다. **시맨틱 HTML과 접근성을 제대로 하면 에이전트 대응은 따라옵니다.** 스크린 리더 사용자를 위한 작업이 곧 AI 에이전트를 위한 작업입니다.

WebMCP(사이트가 에이전트에게 사용 가능한 액션을 선언하는 제안 표준)도 등장했지만 아직 origin trial 단계이고, 없는 것이 결함은 아닙니다.

### AI 기능 노출을 제어하려면

AI Overview에 내 콘텐츠가 요약되는 것을 원치 않는다면, **별도의 AI 전용 옵트아웃 파일은 없습니다.** 기존 스니펫 제어 지시어를 씁니다.

```html
<!-- 스니펫 전체 금지 (AI Overview 포함) -->
<meta name="robots" content="nosnippet">

<!-- 스니펫 길이 제한 -->
<meta name="robots" content="max-snippet:50">

<!-- 특정 영역만 스니펫 제외 -->
<p data-nosnippet>이 문단은 인용되지 않습니다.</p>
```

이건 2장의 AI **크롤러**(GPTBot 등) 제어와 다른 층입니다. 크롤러 제어는 "누가 가져가느냐", 스니펫 제어는 "Google이 어떻게 보여주느냐"입니다.

---

## 11장. 측정과 운영

### 고쳤는지 어떻게 아나

SEO 작업의 가장 나쁜 패턴은 "고쳤다, 끝"입니다. 배포했다고 검색엔진이 즉시 반응하지 않고, 반응했는지 확인하는 방법을 모르면 무엇이 효과가 있었는지 영원히 알 수 없습니다.

감사 리포트의 모든 권고에 **"실패 판정"** 줄을 붙인 이유가 이것입니다. 각 조치마다 "이게 안 됐다면 무엇을 보고 알 수 있는가"를 미리 정합니다. 반증 가능하지 않은 권고는 권고가 아니라 의견입니다.

### Search Console — 유일한 1차 출처

Google Search Console(GSC)은 사이트 소유자만 볼 수 있는 Google의 공식 데이터입니다. 제3자 도구 점수는 전부 추정이고, GSC만 사실입니다.

| 리포트 | 무엇을 보나 | 이 글의 어느 장과 연결 |
|---|---|---|
| **페이지(색인 생성)** | 색인된 페이지 수, 제외 이유별 분류 | 1, 2, 3, 8장 |
| **Sitemaps** | 제출한 sitemap의 "검색된 URL" 수 | 2장 |
| **URL 검사** | 특정 URL의 색인 상태, Google이 선택한 canonical, 렌더 결과 | 3, 6장 |
| **개선사항 > 이벤트 등** | 구조화 데이터 유효/오류 건수 | 5장 |
| **Core Web Vitals** | 필드 데이터 기준 URL 그룹별 상태 | 7장 |
| **실적(성과)** | 노출·클릭·CTR·평균 순위. 질의별·페이지별 | 4, 9장 |
| **HTTPS** | HTTPS 커버리지 | 7장 |

2025~2026년 추가된 기능:

- 실적 리포트에 **시간 단위 데이터**(2025년 4월)
- **브랜드 vs 비브랜드 질의 필터**(2025년 11월~) — "내 사이트 이름을 검색해서 온 사람"과 "주제를 검색해서 온 사람"을 분리. SEO 효과는 후자에서 봐야 합니다.
- 예전 "페이지 경험" 리포트는 **제거**됐습니다. CWV와 HTTPS 리포트로 봅니다.

### 재감사 없이 볼 수 있는 선행 지표

데브이벤트 리포트의 마지막 표입니다. 매주 확인하도록 설계했습니다.

| 지표 | 확인 위치 | 정상 신호 |
|---|---|---|
| 색인된 페이지 수 | GSC > 페이지 | 3,000 방향으로 증가 |
| sitemap 검색된 URL | GSC > Sitemaps | 3,000 근처 |
| 신규 행사 색인 소요일 | GSC > URL 검사 | 7일 → 2일 이내로 단축 |
| Event 리치 결과 | GSC > 개선사항 | 항목 자체가 생성됨 |
| `/search` 색인 수 | `site:example.com/search` | 0으로 수렴 |
| "준비중" 스니펫 | `site:example.com 준비중입니다` | 감소 |

각 줄이 1~8장의 조치 하나씩에 대응합니다. 이 표가 있으면 "SEO 좋아졌어?"라는 질문에 감으로 답하지 않게 됩니다.

### 실행 순서는 의존 그래프다

권고를 심각도 순으로 나열하는 것과 **실행 순서**는 다릅니다. 어떤 작업은 다른 작업이 끝나야 안전하게 시작할 수 있습니다. 데브이벤트 리포트의 실행 순서입니다.

```
1주차 ─┬─ /search noindex + undefined 수정      (독립, 30분)
       ├─ canonical 추가                         (독립, 1시간)
       ├─ og:image 절대 URL + Twitter 카드       (독립, 1시간)
       ├─ "준비중입니다" → 자동 요약 문장 교체    (독립, 반나절)
       └─ sitemap 동적 생성                       (독립, 반나절)
                    ↓
2주차 ─┬─ 일시 데이터 분리                ← Event 스키마의 선행 조건
       │   └─ 온·오프라인 / 무료·유료 필드 존재 확인도 함께
       └─ 목록 첫 페이지 SSR
                    ↓
3주차 ─┬─ Event JSON-LD                  ← 일시 분리 완료 후에만
       └─ 캐시 헤더                       ← 개인화 분리가 선행돼야 안전
                    ↓
1개월 ─┬─ 태그 랜딩 (10건 이상 태그만)
       ├─ description 템플릿 확대
       └─ next/image 전환
                    ↓
분기  ── 선별 noindex 재평가              ← 문구 교체 이후 GSC 데이터로만
```

세 종류의 의존이 보입니다.

- **데이터 의존**: Event 스키마는 일시 데이터가 정리돼야 만들 수 있습니다. 순서를 어기면 틀린 스키마가 대량 생성됩니다.
- **안전 의존**: 캐시 헤더는 개인화 분리 후에만 켤 수 있습니다. 순서를 어기면 정보 유출입니다.
- **판단 의존**: 선별 noindex는 문구 교체 후 GSC 데이터를 봐야 대상을 정할 수 있습니다. 순서를 어기면 살려야 할 페이지를 죽입니다.

1주차 다섯 항목은 서로 독립이라 **병렬로 진행**할 수 있습니다. 이걸 알려주는 것도 리포트의 역할입니다.

### 가장 먼저 하나만 고른다면

리포트는 **sitemap**을 꼽았습니다. 이유: URL 3,000건이 이미 존재하고, 신규 등록분 품질은 충분한데, 발견 경로만 없는 상태였습니다. 투입 대비 회수가 가장 큽니다.

두 번째는 **"준비중입니다" 문구 교체**. DB 마이그레이션 없이 렌더링 로직만 바꾸면 되는데, SERP에 노출되는 가장 나쁜 신호를 한 번에 제거합니다.

이 판단 기준을 일반화하면: **"이미 가진 자산이 검색엔진에 전달되지 않는 지점"** 을 먼저 뚫습니다. 새 콘텐츠를 만드는 것보다 있는 콘텐츠가 보이게 하는 것이 항상 먼저입니다.

### 회귀 방지 — 배포마다 SEO가 깨지지 않게

SEO 설정은 한 번 고치면 끝이 아닙니다. 다음 배포에서 누군가 `<Head>`를 리팩터링하다 canonical을 지우거나, 검색 페이지의 noindex 로직이 조건문 실수로 상세 페이지에 걸리는 일이 실제로 일어납니다. 그리고 그 사고는 **화면에는 아무 변화가 없어서** 아무도 모릅니다. 색인이 빠지고 트래픽이 떨어진 뒤에야 알게 됩니다.

#### CI 스모크 테스트

4장과 6장에서 보인 curl 검사들을 하나로 모은 스크립트입니다. 배포 후 실행합니다.

```bash
#!/usr/bin/env bash
# seo-check.sh — 배포 후 SEO 회귀 검사
set -uo pipefail

BASE="${1:-https://example.com}"
fail=0
ok()   { echo "  ok   $1"; }
bad()  { echo "  FAIL $1"; fail=1; }

echo "[sitemap]"
n=$(curl -s "$BASE/sitemap.xml" | grep -c '<loc>')
(( n >= 100 )) && ok "URL $n개" || bad "URL $n개 (100 미만)"

echo "[목록 페이지 SSR]"
n=$(curl -s "$BASE/events" | grep -c 'href="/event/detail/')
(( n >= 20 )) && ok "상세 링크 $n개" || bad "상세 링크 $n개 (20 미만)"

echo "[상세 페이지 메타]"
html=$(curl -s "$BASE/event/detail/3212")
printf '%s' "$html" | grep -q 'rel="canonical" href="https://' && ok "canonical 절대 URL" || bad "canonical 없음/상대경로"
printf '%s' "$html" | grep -qi 'name="robots"[^>]*noindex' && bad "상세에 noindex!" || ok "noindex 없음"
printf '%s' "$html" | grep -q 'application/ld+json' && ok "JSON-LD 존재" || bad "JSON-LD 없음"
printf '%s' "$html" | grep -q 'property="og:image" content="https://' && ok "og:image 절대 URL" || bad "og:image 상대경로"
for lit in 'undefined' 'null' '{' '}'; do
  printf '%s' "$html" | grep -o '<meta name="description" content="[^"]*"' | grep -qF -- "$lit" && bad "description에 '$lit'"
done

echo "[검색 페이지]"
s=$(curl -s "$BASE/search?tag=AI")
printf '%s' "$s" | grep -qi 'name="robots"[^>]*noindex' && ok "noindex 있음" || bad "noindex 없음 (크롤 트랩)"

echo "[캐시]"
curl -s -o /dev/null "$BASE/events"
cc=$(curl -s -D - -o /dev/null "$BASE/events" | grep -i '^cache-control' | tr -d '\r')
printf '%s' "$cc" | grep -q 'public' && ok "$cc" || bad "$cc"

echo "[리다이렉트]"
code=$(curl -s -o /dev/null -w '%{http_code}' "$BASE/")
[[ "$code" == "308" || "$code" == "301" ]] && ok "/ → $code" || bad "/ → $code (영구 리다이렉트 아님)"

exit $fail
```

이 스크립트는 15초 안에 끝나고, 3장부터 7장까지의 핵심 회귀를 잡습니다. 실제 사이트에서 검사할 URL과 임계값만 바꾸면 됩니다.

#### drift 모니터링 — SEO용 Git

더 체계적인 방법은 **기준점(baseline)을 찍어두고 diff를 뜨는** 것입니다. `claude-seo`의 drift 기능이 이 방식입니다.

```
/seo drift baseline <url>   # 현재 상태를 "정상"으로 저장
/seo drift compare <url>    # 지금 상태를 기준점과 비교
/seo drift history <url>    # 변화 이력
```

baseline이 기록하는 것: title, meta description, canonical, robots 지시어, h1/h2/h3 전체, JSON-LD, Open Graph, Core Web Vitals, HTTP 상태 코드, 그리고 HTML 본문과 스키마의 SHA-256 해시. 페이지당 13개 항목이 로컬 SQLite에 쌓입니다.

compare는 17개 규칙을 3단계 심각도로 적용합니다.

| 심각도 | 규칙 예시 |
|---|---|
| **Critical** | 스키마 전체 삭제 · canonical 변경/삭제 · **noindex 추가** · h1 삭제 · h1 50% 이상 변경 · title 삭제 · 2xx → 4xx/5xx |
| **Warning** | title 변경 · description 변경 · CWV 20% 이상 악화 · Lighthouse 10점 이상 하락 · OG 태그 삭제 · 스키마 내용 변경 |
| **Info** | 스키마 신규 추가 · h2 구조 변경 · 본문 해시 변경 |

**수정 작업 전에 baseline을 찍어두면**, 수정 후 compare 한 번으로 "의도한 것이 바뀌었고 의도하지 않은 것은 안 바뀌었는지"가 표로 나옵니다. canonical 추가는 Critical "canonical 변경"으로 뜨는데 이건 의도한 것이니 정상이고, 상세 페이지에서 "noindex 추가"가 뜨면 그건 사고입니다.

baseline은 URL 단위이므로 대표 페이지 몇 개(목록, 상세 1~2건, 소개, 검색)를 골라 찍습니다.

### 감사 방법론 — 결과를 어떻게 신뢰하나

이 글의 사례 리포트는 `claude-seo`가 문서화한 **10원칙 프레임워크**를 따라 작성됐습니다. 도구와 무관하게 SEO 감사 일반에 적용되는 원칙이라 소개합니다.

| 단계 | 원칙 | 뜻 |
|---|---|---|
| **PERCEIVE** | 관찰(외부) | 점수 매기지 말고 데이터만 모은다 |
| | 관찰(내부) | 내 가정을 점검한다. "홈이 사이트를 대표한다"는 가정은 자주 틀린다 |
| | 경청 | 사이트의 기존 문구, SERP, 사용자 커뮤니티를 먼저 읽는다 |
| **ANALYZE** | 사고 | 첫 원리로 환원. 지금 사이트를 묶고 있는 **가장 큰 제약** 하나는 무엇인가 |
| | 연결(수평) | 서로 다른 영역의 발견을 잇는다. "콘텐츠 없음"의 원인이 "JS 렌더링"일 수 있다 |
| | 연결(체계) | 권고를 의존 그래프로 배열한다 |
| **VALIDATE** | 감각 | 이 권고가 사용자·브랜드·운영팀에게 어떤 비용인가 |
| | 수용 | **"이게 실패했다면 무엇으로 알 수 있나"** 를 모든 권고에 붙인다 |
| **ACT** | 생성 | 분석을 멈추고 산출물을 만든다 |
| | 성장 | baseline을 찍고, 선행 지표를 정하고, 재감사 주기를 잡는다 |

이 중 실전에서 가장 자주 빠지는 것이 **"관찰(내부)"** 입니다. 데브이벤트 감사에서 실제로 겪은 일입니다. 처음 표본으로 가장 오래된 ID 3개를 뽑았고 전부 "준비중"이어서 "대다수가 빈 페이지"로 결론 내리려 했습니다. 그런데 Google `site:` 검색에서 충실한 스니펫이 여러 개 보였습니다. 표본이 편향됐던 것입니다. 최신 ID로 재표본하고, 결국 ID 구간을 균등 간격으로 38건 훑어 75%라는 수치와 7월 말 전환점을 찾았습니다. **첫 결론이 데이터와 충돌하면 결론이 아니라 표본을 의심**해야 합니다.

### 측정하지 못한 것을 명시하라

리포트의 마지막 절은 "한계"였습니다.

1. PageSpeed Insights 공용 API 할당량 초과로 LCP·INP·CLS 실측 실패. 성능 항목은 TTFB와 렌더 시간에만 근거.
2. CrUX 필드 데이터 미확인. 데이터가 없는지 트래픽 부족으로 집계가 안 되는지 구분 못함.
3. Search Console 데이터 없음. 색인 규모는 `site:` 기반 추정.
4. 백링크 프로필 미조사.
5. 페이지 수 3,046건은 94% 존재율 기반 추정치.

이걸 쓰는 이유는 겸손이 아닙니다. **다음 감사가 무엇을 봐야 하는지**를 정하고, 지금 리포트의 어느 숫자를 얼마나 믿어야 하는지를 독자가 알게 하기 위해서입니다. 측정 못 한 것을 측정한 것처럼 쓴 리포트는 다음 결정을 잘못된 근거 위에 세웁니다.

---

## 12장. 도구

### claude-seo — 이 글의 사례를 만든 도구

이 글의 데이터는 **claude-seo**(https://github.com/AgriciDaniel/claude-seo, MIT 라이선스)로 수집했습니다. Claude Code용 플러그인으로, 25개 하위 스킬과 18개 전문 에이전트로 구성돼 있고, 격리된 Python 런타임과 Playwright Chromium을 내장해 헤드리스 렌더링과 HTTP 계측을 합니다.

#### 설치

Claude Code 안에서:

```bash
# 마켓플레이스 등록
claude plugin marketplace add AgriciDaniel/claude-seo

# 프로젝트 범위로 설치 (해당 저장소에서만 활성화)
claude plugin install claude-seo@agricidaniel-claude-seo --scope project

# 런타임 준비 (Python venv + Chromium)
/seo setup
/seo doctor
```

`--scope project`를 쓰면 저장소의 `.claude/settings.json`에만 기록되고 다른 프로젝트에는 영향이 없습니다. `--scope user`는 모든 프로젝트에서 켜집니다. 플러그인은 활성화된 세션마다 약 5천 토큰의 컨텍스트를 상시 차지하므로, SEO 작업을 하는 프로젝트에만 켜는 것을 권합니다.

#### 주요 명령

| 명령 | 하는 일 | 이 글의 장 |
|---|---|---|
| `/seo audit <url>` | 전체 감사. 에이전트 병렬 실행, 100점 점수, 우선순위 계획 | 전체 |
| `/seo page <url>` | 단일 페이지 심층 분석 | 4, 5 |
| `/seo technical <url>` | 크롤·색인·보안·URL·모바일·CWV·JS 렌더링·IndexNow | 1, 2, 3, 6, 7 |
| `/seo schema <url>` | 구조화 데이터 검출·검증·생성 | 5 |
| `/seo sitemap <url>` | sitemap 검증 또는 생성 | 2 |
| `/seo content <url>` | E-E-A-T, 얇은 콘텐츠, AI 인용 준비도 | 8 |
| `/seo images <url>` | alt, 크기, 포맷, lazy, CLS | 7 |
| `/seo geo <url>` | AI 크롤러 접근, SSR, 인용 가능성 | 10 |
| `/seo programmatic <url>` | 대규모 생성 페이지 품질 게이트 | 8, 9 |
| `/seo cluster <keyword>` | SERP 겹침 기반 토픽 클러스터 | 9 |
| `/seo sxo <url> [keyword]` | 페이지 유형 불일치, 페르소나 점수 | 9 |
| `/seo drift baseline\|compare\|history <url>` | 변화 감시 | 11 |
| `/seo google <cmd> <url>` | Search Console·PageSpeed·CrUX·GA4 연동 (인증 필요) | 7, 11 |
| `/seo plan <business-type>` | 업종별 전략 템플릿 | — |

#### 내장 스크립트 직접 쓰기

플러그인 없이도 `claude-seo run <script>`로 개별 도구를 쓸 수 있습니다. 감사에서 실제로 쓴 것들입니다.

```bash
# 원본 HTML + 렌더 후 HTML + 텍스트 추출 + SPA 판정
claude-seo run render_page.py https://example.com/events --mode always --json > rendered.json

# 접근성 트리 캡처 (에이전트 친화성 점검)
claude-seo run render_page.py https://example.com --a11y-tree --json

# Agent-UX 휴리스틱 점수
claude-seo run agent_ux_check.py https://example.com --json

# sitemap 위치 탐색 (robots.txt 선언 + 일반 경로 프로브)
claude-seo run sitemap_discovery.py https://example.com --json

# PageSpeed (API 키 있으면 필드 데이터까지)
claude-seo run pagespeed_check.py https://example.com --json

# drift baseline (PSI 할당량 아끼려면 --skip-cwv)
claude-seo run drift_baseline.py https://example.com/events --skip-cwv
```

#### 에이전트가 실패할 때

전체 감사는 7개 이상의 에이전트를 병렬로 돌립니다. 데브이벤트 감사에서는 그중 5개가 턴 제한에 걸려 리포트 없이 종료됐습니다. 이런 일은 흔합니다. 에이전트가 너무 많은 도구 호출을 시도하다가 예산을 소진하는 것입니다.

대응은 두 가지였습니다. (1) 실패한 영역은 위의 스크립트로 **직접 측정**하고, (2) 재실행하는 에이전트에는 "도구 호출 3~4회 이내, 반드시 리포트를 출력"이라는 제약을 명시했습니다. 두 번째 시도는 성공했습니다.

교훈: **자동화 도구의 출력을 그대로 믿지 말고, 핵심 수치는 curl로 재확인**하세요. 7장에서 언급한 urllib 헤더 오판도 이 과정에서 잡혔습니다.

### 그 외 도구

| 도구 | 용도 | 비용 |
|---|---|---|
| Google Search Console | 1차 출처. 색인·실적·CWV·구조화 데이터 | 무료 (소유 확인) |
| PageSpeed Insights | 필드+랩 성능 | 무료 |
| Rich Results Test | 구조화 데이터 검증 | 무료 |
| CrUX Vis | 필드 데이터 추이 | 무료 |
| Lighthouse CLI | 랩 데이터, CI 통합 | 무료 |
| Facebook 공유 디버거 | OG 태그 확인 | 무료 |
| 네이버 서치어드바이저 | 네이버 색인·IndexNow | 무료 |
| curl + grep | 이 글의 모든 확인 명령 | 무료 |

유료 도구(Ahrefs, Semrush, DataForSEO 등)는 경쟁사 백링크·키워드 볼륨·SERP 추적에 유용하지만, **이 글에서 다룬 문제는 전부 무료 도구로 발견하고 고칠 수 있습니다.** 배관 공사에 유료 도구는 필요 없습니다.

---

## 마치며

### 다시, 36점

이 사이트의 점수가 낮은 이유를 한 문장으로 요약하면 이렇습니다.

> **URL 자산은 3,000건 규모로 쌓였는데, 그것을 검색엔진에 설명하는 층(sitemap · canonical · 구조화 데이터 · 서버 렌더링)이 통째로 비어 있었고, 그 자산의 75%는 2026년 7월 이전에 만들어진 빈 껍데기였다.**

두 문제는 성격이 다릅니다. 앞은 **배관**이고 개발자가 코드로 고칩니다. 뒤는 **재고**이고 운영 프로세스가 이미 바뀌어 신규 유입은 해결됐으니 과거분만 정리하면 됩니다.

이 구분이 되면 "SEO 컨설팅을 받아야 하나", "콘텐츠 마케터를 뽑아야 하나" 같은 질문이 사라집니다. 필요한 것은 sitemap 파일 하나, canonical 한 줄, JSON-LD 빌더 함수 하나, 캐시 헤더 한 줄, 그리고 "준비중입니다"를 사실 문장으로 바꾸는 폴백 함수 하나입니다. 전부 이 글에 코드로 들어 있습니다.

### 이 글에서 기억할 것 열 가지

1. SEO는 발견 → 색인 → 순위의 순서다. 대부분의 사이트는 첫 두 단계에서 이미 잃는다.
2. **sitemap의 lastmod는 진짜 수정 시각**이어야 한다. 아니면 전부 무시된다.
3. **canonical은 절대 URL로 서버 HTML에.** JS가 바꾸면 Google은 둘 중 아무 것을 쓴다.
4. **noindex를 걸었으면 robots.txt로 막지 마라.** 막으면 noindex를 못 읽는다.
5. **틀린 스키마는 없는 스키마보다 나쁘다.** 날짜를 모르면 Event 블록을 빼라.
6. **HowTo는 2023년, FAQPage는 2026년 5월에 리치 결과가 끝났다.** 넣어도 소용없다.
7. **AI 크롤러는 JS를 실행하지 않는다.** curl로 열어서 안 보이면 없는 것이다.
8. **개인화를 분리하기 전에 public 캐싱을 켜지 마라.** 성능 개선이 아니라 정보 유출이다.
9. **noindex보다 문구 교체가 먼저다.** 빈 페이지를 사실 문장으로 바꾸면 noindex할 대상이 줄어든다.
10. **모든 권고에 "실패했다면 무엇으로 아나"를 붙여라.** 없으면 의견이다.

### 다음 감사가 봐야 할 것

- Search Console 소유 확인 후 실제 색인 수·노출·클릭 확보
- PageSpeed API 키 발급 후 LCP·INP·CLS 필드 데이터 실측
- 수정 배포 전 `/seo drift baseline`으로 대표 URL 5개 기준점 확보
- 3개월 후 `site:` 검색에서 "준비중입니다" 스니펫 소멸 여부
- 태그 랜딩 15~20개의 노출수가 0에서 움직이는지

---

## 부록 A. 배포 전 SEO 체크리스트

배포 파이프라인에 넣을 수 있는 형태로 정리했습니다. 각 항목 뒤 괄호는 해당 장입니다.

### 발견
- [ ] `/sitemap.xml`이 200을 반환하고 실제 페이지 수와 비슷한 URL 수를 담는다 (2)
- [ ] `lastmod`가 실제 갱신 시각이고, 전부 같은 값이 아니다 (2)
- [ ] sitemap에 noindex·리다이렉트·404·http URL이 없다 (2)
- [ ] `robots.txt`에 `Sitemap:` 줄이 있다 (2)
- [ ] 목록 페이지 서버 HTML에 상세 링크가 `<a href>`로 존재한다 (6)

### 중복
- [ ] 모든 색인 대상 페이지에 절대 URL canonical이 있다 (3)
- [ ] 검색 결과·필터 조합 페이지는 `noindex, follow` + 목록으로 canonical (3)
- [ ] 영구 리다이렉트는 301/308, 체인 없음 (3)
- [ ] robots.txt Disallow와 noindex가 같은 경로에 겹치지 않는다 (2, 3)

### 페이지
- [ ] title 30~60자, 페이지마다 고유 (4)
- [ ] description 120~160자, 페이지마다 고유, `undefined`·`{}`·`null` 없음 (4)
- [ ] h1 하나, 계층 건너뛰기 없음 (4)
- [ ] og:image·og:url·twitter:image 절대 URL (4)
- [ ] `target="_blank"`에 `rel="noopener"` (4)
- [ ] 모든 이미지 alt, width, height (4, 7)

### 구조화 데이터
- [ ] JSON-LD가 서버 HTML에 있다 (5, 6)
- [ ] Rich Results Test 통과, 폐기 타입 없음 (5)
- [ ] 화면 표시 내용과 스키마 값이 일치한다 (5)
- [ ] 필수 값이 없으면 블록을 생략하는 방어 로직이 있다 (5)

### 속도
- [ ] 목록·상세 페이지 Cache-Control이 `public, s-maxage=…` (7)
- [ ] 로그인 정보가 서버 HTML에 섞여 있지 않다 (7)
- [ ] 첫 화면 이미지는 eager + `fetchpriority="high"`, 나머지는 lazy (7)
- [ ] 이미지 포맷 WebP/AVIF (7)

### 콘텐츠
- [ ] placeholder 문구("준비중", "Lorem")가 노출되는 페이지가 없다 (8)
- [ ] 생성 페이지 각각이 "단독으로 발행 가치가 있는가" 시험을 통과한다 (8)
- [ ] 태그·카테고리 페이지는 항목 10건 이상 + 고유 소개문 (9)

### 운영
- [ ] 배포 후 `seo-check.sh` 통과 (11)
- [ ] 큰 변경 전 drift baseline 확보 (11)
- [ ] Search Console에서 색인 수·sitemap 검색된 URL·구조화 데이터 오류를 주간 확인 (11)

---

## 부록 B. 용어집

| 용어 | 뜻 |
|---|---|
| **SERP** | Search Engine Results Page. 검색 결과 페이지 |
| **스니펫(snippet)** | 검색 결과에 표시되는 제목·설명·URL 묶음 |
| **리치 결과(rich result)** | 구조화 데이터로 확장된 검색 결과 (별점, 날짜, 이미지 등) |
| **크롤링(crawling)** | 검색엔진 봇이 페이지를 가져오는 것 |
| **색인(indexing)** | 가져온 페이지를 검색 가능한 저장소에 넣는 것 |
| **크롤 예산(crawl budget)** | 검색엔진이 한 사이트에 쓰는 요청량 |
| **크롤 트랩(crawl trap)** | 파라미터 조합 등으로 무한 URL이 생성돼 크롤 예산을 소진시키는 구조 |
| **canonical** | 중복 URL 중 대표를 지정하는 `<link>` 태그 |
| **noindex** | 이 페이지를 색인하지 말라는 robots 메타 지시어 |
| **소프트 404** | 없는 페이지인데 200을 반환하는 것 |
| **SSR / CSR / SSG / ISR** | 서버 렌더링 / 클라이언트 렌더링 / 정적 생성 / 증분 정적 재생성 |
| **JSON-LD** | 구조화 데이터를 담는 JSON 형식. `<script type="application/ld+json">` |
| **schema.org** | 구조화 데이터 어휘 표준 |
| **CWV** | Core Web Vitals. LCP·INP·CLS |
| **LCP** | Largest Contentful Paint. 가장 큰 콘텐츠 표시 시간 |
| **INP** | Interaction to Next Paint. 상호작용 후 화면 반응 시간. FID 대체 |
| **CLS** | Cumulative Layout Shift. 레이아웃 밀림 누적 |
| **TTFB** | Time to First Byte. 서버 첫 응답 시간 |
| **CrUX** | Chrome User Experience Report. 실사용자 필드 데이터 |
| **필드 데이터 / 랩 데이터** | 실사용자 측정 / 시뮬레이션 측정 |
| **E-E-A-T** | Experience, Expertise, Authoritativeness, Trustworthiness |
| **YMYL** | Your Money or Your Life. 건강·금융·법률·안전 등 고위험 주제 |
| **QRG** | Google 검색 품질 평가 가이드라인 |
| **얇은 콘텐츠(thin content)** | 고유 가치가 거의 없는 페이지 |
| **프로그래매틱 SEO** | 데이터로 대량 페이지를 생성하는 방식 |
| **도어웨이 페이지** | 도시명 등만 바꾼 중복 페이지 |
| **허브 앤 스포크** | 중심 글과 세부 글의 링크 구조 |
| **카니발리제이션** | 자기 사이트 페이지끼리 같은 키워드로 경쟁 |
| **SXO** | Search Experience Optimization. 페이지 유형과 검색 의도의 정합성 |
| **GEO / AEO** | AI 검색 대응. Google 입장: SEO의 다른 이름 |
| **AI Overview / AI Mode** | Google의 두 AI 검색 표면 |
| **RAG** | Retrieval-Augmented Generation. 검색 결과로 답을 생성하는 기법 |
| **llms.txt** | AI용 사이트 요약 파일 제안. Google은 무시 |
| **IndexNow** | Bing·네이버 등에 URL 변경을 즉시 알리는 프로토콜 |
| **GSC** | Google Search Console |
| **PSI** | PageSpeed Insights |
| **Open Graph** | SNS 미리보기용 메타 태그 규격 |
| **drift** | 배포 간 SEO 요소의 의도치 않은 변화 |

---

## 부록 C. 1차 출처 모음

이 글은 가능한 한 Google 공식 문서와 도구 문서에 근거했습니다. 커뮤니티 주장과 Google 문서가 충돌하면 Google 문서를 따랐습니다.

### Google 공식
- AI 최적화 가이드: https://developers.google.com/search/docs/fundamentals/ai-optimization-guide
- 도움이 되는 콘텐츠 만들기: https://developers.google.com/search/docs/fundamentals/creating-helpful-content
- 제3자 SEO 도구에 대해: https://developers.google.com/search/docs/fundamentals/third-party-seo
- AI 기능과 사이트: https://developers.google.com/search/docs/appearance/ai-features
- 크롤링·robots 참조: https://developers.google.com/crawling
- 리치 결과 정리 공지 (2025-06): https://developers.google.com/search/blog/2025/06/simplifying-search-results
- HowTo·FAQ 변경 (2023-08): https://developers.google.com/search/blog/2023/08/howto-faq-changes
- 구조화 데이터 일반 가이드: https://developers.google.com/search/docs/appearance/structured-data/intro-structured-data
- Event 구조화 데이터: https://developers.google.com/search/docs/appearance/structured-data/event
- JavaScript SEO 기본: https://developers.google.com/search/docs/crawling-indexing/javascript/javascript-seo-basics
- sitemap 작성: https://developers.google.com/search/docs/crawling-indexing/sitemaps/build-sitemap
- robots.txt 소개: https://developers.google.com/search/docs/crawling-indexing/robots/intro
- canonical: https://developers.google.com/search/docs/crawling-indexing/consolidate-duplicate-urls
- Core Web Vitals: https://web.dev/articles/vitals
- Rich Results Test: https://search.google.com/test/rich-results
- PageSpeed Insights: https://pagespeed.web.dev/
- CrUX Vis: https://cruxvis.withgoogle.com

### 표준
- schema.org: https://schema.org
- Sitemaps 프로토콜: https://www.sitemaps.org/protocol.html
- Open Graph: https://ogp.me
- IndexNow: https://www.indexnow.org
- llms.txt 제안: https://llmstxt.org

### 도구
- claude-seo (MIT, AgriciDaniel): https://github.com/AgriciDaniel/claude-seo
- Lighthouse: https://github.com/GoogleChrome/lighthouse
- Facebook 공유 디버거: https://developers.facebook.com/tools/debug/
- 네이버 서치어드바이저: https://searchadvisor.naver.com

### 이 글에서 인용한 제3자 연구 (Google 공식이 아님, 방법론 의존)
- Ahrefs, 브랜드 언급과 AI 가시성 상관 (75,000 브랜드, 2025-12)
- Ahrefs, AI Overview vs AI Mode 인용 URL 겹침 (540K 질의 쌍)
- SE Ranking, AI 인용 위치 분포 및 신선도 (1.3M 인용)
- SE Ranking, llms.txt 보유율 (300K 도메인, 2025-11)
- OtterlyAI, AI 봇 서버 로그 감사 (2025)

---

*이 글의 사례 데이터는 2026년 9월 7일에 수집한 실측값입니다. 사이트가 변경되면 결과가 달라집니다. 코드 예시는 Next.js Pages Router 기준이며, 다른 프레임워크에서는 같은 원칙을 해당 프레임워크의 서버 렌더링 훅에 적용하면 됩니다.*
