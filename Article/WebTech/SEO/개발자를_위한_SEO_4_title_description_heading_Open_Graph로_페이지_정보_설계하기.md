<!--

개발자를 위한 SEO 실전 가이드 4부 - title, description, heading, Open Graph로 페이지 정보 설계하기

-->

## 시작하며

<br />

검색 결과에 표시되는 제목이 내가 작성한 title과 다르거나, 링크를 카카오톡에 붙였는데 엉뚱한 이미지가 나오는 경험은 누구나 한 번쯤 합니다. 이런 문제는 메타 태그 몇 줄의 누락으로 보이지만, 실제로는 페이지가 무엇을 말하는지 여러 시스템에 일관되게 전달하지 못했을 때 생깁니다.

<br />

브라우저 탭, 검색 결과, 공유 미리보기는 모두 페이지의 정보를 읽지만 같은 태그만 보지는 않습니다. Google은 title, 페이지의 큰 제목, 링크 텍스트 등 여러 신호를 바탕으로 검색 결과 제목을 만들 수 있습니다. 공유 서비스는 주로 Open Graph 태그를 읽습니다.

<br />

이번 글에서는 한 페이지의 주제를 한 문장으로 정리한 뒤, 그것을 title, description, h1, Open Graph로 일관되게 표현하는 방법을 다룹니다. 목표는 검색엔진을 속이는 문구가 아니라, 사용자가 클릭하기 전에 페이지 내용을 정확히 이해하도록 돕는 정보 구조입니다.

<br />
<br />
<br />

## 한 페이지에는 하나의 분명한 약속이 있어야 한다

<br />

메타데이터를 작성하기 전에 먼저 답해야 할 질문은 하나입니다.

<br />

```text
이 페이지가 검색 결과에 단독으로 나타났을 때,
사용자는 무엇을 얻을 수 있는가?
```

<br />

예를 들어 "SEO 가이드"라는 제목은 너무 넓습니다. 반면 "개발자를 위한 sitemap과 robots.txt 가이드"는 독자가 무엇을 배울지 예측할 수 있습니다. 이 문장을 기준으로 title, h1, description을 만들면 서로 다른 말이 충돌할 가능성이 줄어듭니다.

<br />

아래처럼 역할을 분리해 두면 좋습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:24%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">요소</th>
      <th style="width:38%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">주된 독자</th>
      <th style="width:38%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">답해야 할 질문</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">title</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">검색 결과와 브라우저 탭</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">이 페이지는 무엇에 관한 문서인가?</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">meta description</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">검색 결과의 설명문</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">왜 이 문서가 지금 찾는 내용에 맞는가?</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">h1</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">본문을 읽는 사용자와 보조 기술</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">이 문서의 중심 주제는 무엇인가?</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">og:*</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">카카오톡, Slack, Discord 등 공유 서비스</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">링크를 열기 전 어떤 콘텐츠인지 한눈에 알 수 있는가?</td>
    </tr>
  </tbody>
</table>

<br />

네 요소의 문구가 글자 단위로 같을 필요는 없습니다. 다만 서로 다른 페이지를 가리키면 안 됩니다. title은 "React 렌더링 전략"인데 h1은 "Vue 입문"이고 Open Graph 제목은 "프런트엔드 뉴스"라면, 검색엔진과 사용자는 이 페이지의 정체를 판단하기 어렵습니다.

<br />
<br />
<br />

## title은 검색 결과 제목의 가장 중요한 재료다

<br />

HTML의 title은 head 안에 들어갑니다.

<br />

```html
<title>개발자를 위한 sitemap과 robots.txt 가이드 | Example Blog</title>
```

<br />

Google은 title 요소만 기계적으로 복사하지 않습니다. 페이지의 h1, 눈에 잘 띄는 텍스트, 페이지를 가리키는 링크 문구, og:title 같은 여러 신호를 사용해 검색 결과의 title link를 생성할 수 있습니다. [Google의 title link 문서](https://developers.google.com/search/docs/appearance/title-link)에 따르면, title이 비어 있거나 반복적이거나 문서 내용과 맞지 않으면 검색 결과에서 다른 제목으로 다시 작성될 수 있습니다.

<br />

그러므로 title을 작성할 때는 글자 수 공식보다 다음 기준이 더 중요합니다.

<br />

```text
- 페이지마다 고유한 제목인가?
- 페이지의 실제 내용을 정확히 말하는가?
- 제목만 보고도 다른 페이지와 구별되는가?
- 사이트 이름은 필요할 때만 뒤에 자연스럽게 붙는가?
```

<br />

동일한 템플릿으로 수천 개의 상세 페이지를 만드는 서비스라면 고유성은 더욱 중요합니다. 아래 두 title을 비교해 보겠습니다.

<br />

```text
좋지 않은 예시
개발자 행사 | Example Service

더 나은 예시
2026 서울 AI 개발자 컨퍼런스 참가 안내 | Example Service
```

<br />

두 번째 제목은 행사명이라는 식별 정보와 문서의 목적을 함께 담습니다. 다만 실제 페이지에 없는 "최고", "공식", "무료" 같은 단어를 검색 유입만을 위해 추가하면 안 됩니다. title이 약속한 내용을 본문 첫 화면에서 확인할 수 있어야 합니다.

<br />

한글 title의 이상적인 길이를 특정 글자 수로 고정할 필요는 없습니다. 검색 결과에서 잘리는 지점은 글자 수가 아니라 화면 폭, 기기, 검색어에 따라 달라집니다. 핵심 주제는 앞쪽에 두고, 불필요한 반복을 제거하는 쪽이 더 안정적입니다.

<br />
<br />
<br />

## meta description은 클릭 전에 문서를 설명한다

<br />

description은 페이지를 짧게 설명하는 메타 태그입니다.

<br />

```html
<meta
  name="description"
  content="sitemap과 robots.txt의 역할, 동적 sitemap 생성 방법, noindex와의 차이를 개발자 관점에서 정리합니다."
/>
```

<br />

Google은 특정 검색어에 더 맞는 본문 문장이 있으면 meta description 대신 그 내용을 검색 결과 스니펫으로 사용할 수 있습니다. 따라서 description은 "반드시 이 문장이 노출된다"는 보장이 아니라, 페이지의 요약을 제안하는 태그로 이해하는 편이 맞습니다. [Google의 메타 태그 문서](https://developers.google.com/search/docs/crawling-indexing/special-tags)도 description을 검색 결과 스니펫에 사용될 수 있는 짧은 페이지 설명으로 소개합니다.

<br />

좋은 description은 title을 되풀이하지 않고, 페이지에서 얻을 수 있는 구체적인 정보를 한두 문장으로 덧붙입니다.

<br />

```text
title
개발자를 위한 sitemap과 robots.txt 가이드 | Example Blog

description
검색엔진이 중요한 URL을 발견하도록 sitemap을 구성하는 기준과,
robots.txt·noindex를 혼동하지 않는 방법을 예제로 설명합니다.
```

<br />

다음과 같은 템플릿 오류는 배포 후 실제 HTML에서 반드시 확인해야 합니다.

<br />

```text
- undefined, null, NaN 같은 미초기화 값
- {category}, {{title}}처럼 치환되지 않은 템플릿 변수
- 모든 상세 페이지에 반복되는 일반론적인 설명문
- 본문에 없는 가격, 날짜, 기능을 약속하는 문구
```

<br />

description이 순위 순서를 직접 결정하는 비밀 키는 아닙니다. 하지만 사용자가 검색 결과에서 클릭할지 판단하는 정보이므로, 내용이 빈 페이지에 반복되는 자동 문구보다 실제 페이지의 차이를 보여주는 설명이 훨씬 낫습니다.

<br />
<br />
<br />

## h1은 문서의 중심을 보여 주는 제목이다

<br />

h1은 글씨를 크게 만들기 위한 CSS 도구가 아니라 문서의 중심 제목입니다. 사용자와 보조 기술은 heading 구조를 통해 문서의 뼈대를 파악하고, Google도 h1을 포함한 heading을 title link의 후보 신호로 참고할 수 있습니다.

<br />

가장 이해하기 쉬운 구조는 명확한 h1 하나로 문서를 시작하고, 그 아래에서 h2와 h3로 내용을 나누는 방식입니다.

<br />

```html
<article>
  <h1>개발자를 위한 sitemap과 robots.txt 가이드</h1>

  <h2>sitemap에 넣어야 할 URL</h2>
  <h2>robots.txt가 하는 일</h2>

  <h3>noindex와 함께 쓸 때의 주의점</h3>
</article>
```

<br />

HTML에 h1이 정확히 하나여야만 색인되는 것은 아닙니다. 다만 화면에서 같은 크기와 중요도로 보이는 큰 제목이 여러 개 있으면 문서의 중심을 파악하기 어려워집니다. 한 페이지에는 하나의 주제를 두고, 그 주제를 가장 잘 나타내는 h1을 하나로 명확히 두는 편이 실무에서 안전합니다.

<br />

h1 다음에 h4를 놓는 식으로 제목 단계를 건너뛰는 것도 피하는 편이 좋습니다. 글씨 크기를 바꾸고 싶다면 CSS를 사용하고, h2와 h3는 문서 구조가 실제로 하위 주제일 때만 사용합니다. heading은 검색 순위 조작용 키워드 상자가 아니라, 긴 문서를 사람이 읽을 수 있게 만드는 목차입니다.

<br />

글을 작성한 뒤에는 title, h1, 본문 첫 문단을 나란히 읽어보면 좋습니다. 세 문장이 다른 주제를 말하고 있다면, 메타 태그를 다듬기 전에 글 자체의 초점을 먼저 정리해야 합니다.

<br />
<br />
<br />

## Open Graph는 공유될 때의 첫 화면이다

<br />

Open Graph는 링크를 메신저나 협업 도구에 붙였을 때 표시되는 제목, 설명, 이미지 정보를 전달하는 메타데이터입니다. 검색 순위를 직접 올리는 기능은 아니지만, 공유 미리보기가 제대로 보이는지는 클릭과 신뢰에 큰 영향을 줍니다.

<br />

```html
<meta property="og:type" content="article" />
<meta property="og:title" content="개발자를 위한 sitemap과 robots.txt 가이드" />
<meta property="og:description" content="동적 sitemap 생성과 robots.txt, noindex의 역할을 개발자 관점에서 정리합니다." />
<meta property="og:url" content="https://example.com/posts/sitemap-and-robots" />
<meta property="og:image" content="https://example.com/images/seo-cover.png" />
```

<br />

<code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">og:url</code>에는 canonical URL과 같은 대표 주소를 넣습니다. <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">og:image</code>는 HTTPS 절대 URL을 사용해야 공유 크롤러가 안정적으로 가져올 수 있습니다. 이미지 파일이 실제로 공개 접근 가능하고, 로그인이나 referer 검사 없이 내려오는지도 함께 확인해야 합니다. [Open Graph Protocol](https://ogp.me/)은 이 속성들의 기본 형태를 정의합니다.

<br />

페이지마다 다른 대표 이미지가 없다면 사이트 공통 이미지를 사용해도 됩니다. 중요한 것은 빈 값이나 상대 경로가 아니라, 실제로 열리는 한 장의 이미지를 일관되게 제공하는 것입니다.

<br />

X에도 별도 미리보기를 제공하고 싶다면 Twitter Card 메타 태그를 함께 둘 수 있습니다.

<br />

```html
<meta name="twitter:card" content="summary_large_image" />
<meta name="twitter:title" content="개발자를 위한 sitemap과 robots.txt 가이드" />
<meta name="twitter:description" content="동적 sitemap 생성과 robots.txt, noindex의 역할을 개발자 관점에서 정리합니다." />
<meta name="twitter:image" content="https://example.com/images/seo-cover.png" />
```

<br />

공유 미리보기는 서비스별로 캐시됩니다. 메타 태그를 수정했는데도 이전 이미지가 보인다면 배포 실패라고 단정하기 전에, 해당 서비스의 디버거 도구나 캐시 갱신 방법을 먼저 확인하는 편이 좋습니다.

<br />
<br />
<br />

## 메타데이터 생성은 한 곳으로 모은다

<br />

페이지마다 head 태그를 조금씩 복사해 작성하면 빠진 값과 잘못된 절대 URL이 쌓입니다. 특히 상세 페이지가 많다면, title·description·canonical·Open Graph를 만드는 규칙을 한 함수나 한 컴포넌트로 모으는 편이 좋습니다.

<br />

Next.js App Router의 동적 상세 페이지는 다음처럼 작성할 수 있습니다.

<br />

```ts
// app/events/[slug]/page.tsx
import type { Metadata } from 'next'

const SITE_URL = 'https://example.com'
const DEFAULT_OG_IMAGE = `${SITE_URL}/images/default-og.png`

function toAbsoluteUrl(url?: string | null) {
  if (!url) return DEFAULT_OG_IMAGE
  if (/^https?:\/\//i.test(url)) return url
  return `${SITE_URL}${url.startsWith('/') ? '' : '/'}${url}`
}

export async function generateMetadata({
  params,
}: {
  params: Promise<{ slug: string }>
}): Promise<Metadata> {
  const { slug } = await params
  const event = await getPublishedEventBySlug(slug)

  if (!event) {
    return {}
  }

  const path = `/events/${encodeURIComponent(event.slug)}`
  const canonical = `${SITE_URL}${path}`
  const title = `${event.title} | Example Events`
  const description = `${event.organizer}가 주최하는 ${event.title}. 일정과 참가 방법을 확인하세요.`
  const image = toAbsoluteUrl(event.imageUrl)

  return {
    title,
    description,
    alternates: {
      canonical,
    },
    openGraph: {
      type: 'website',
      url: canonical,
      title,
      description,
      images: [{ url: image }],
    },
    twitter: {
      card: 'summary_large_image',
      title,
      description,
      images: [image],
    },
  }
}
```

<br />

실제 프로젝트에서는 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">SITE_URL</code>, 이미지 절대 URL 변환, description 템플릿을 공용 모듈로 분리하는 편이 좋습니다. 이렇게 하면 특정 페이지에서만 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">undefined</code>가 들어가거나 og:image가 상대 경로로 나가는 문제를 한 곳에서 막을 수 있습니다.

<br />

데이터에서 만든 description은 특히 방어적으로 작성해야 합니다. 주최자나 날짜가 비어 있을 수 있다는 전제에서, 빈 문자열을 조합한 결과가 사용자에게 보이지 않도록 처리합니다.

<br />

```ts
export function buildEventDescription(event: {
  title: string
  organizer?: string | null
  startDate?: string | null
}) {
  const facts = [
    event.organizer ? `${event.organizer} 주최` : null,
    event.startDate ? `${event.startDate} 개최` : null,
  ].filter(Boolean)

  return facts.length > 0
    ? `${event.title}. ${facts.join(', ')}. 일정과 참가 방법을 확인하세요.`
    : `${event.title}의 일정과 참가 방법을 확인하세요.`
}
```

<br />

이 함수는 문장을 화려하게 만들기보다 사실이 없는 자리를 억지로 채우지 않는 데 목적이 있습니다. 행사 날짜를 모르면 날짜를 쓰지 않고, 주최자를 모르면 주최자라는 단어 자체를 빼는 편이 잘못된 정보보다 낫습니다.

<br />
<br />
<br />

## 배포된 HTML에서 확인해야 한다

<br />

로컬 개발 화면에서는 문제가 없어도, 운영 환경의 환경 변수나 서버 렌더링 경로 때문에 metadata가 깨질 수 있습니다. 대표 페이지 몇 개를 골라 배포된 HTML을 검사하는 간단한 스모크 테스트를 두면 실수를 빨리 발견할 수 있습니다.

<br />

```bash
URL='https://example.com/events/spring-conference'

# title, description, canonical, Open Graph가 실제 HTML에 있는지 확인한다.
curl -sL "$URL" \
  | rg -o '<title>[^<]*</title>|<meta name="description"[^>]*>|<link rel="canonical"[^>]*>|<meta property="og:(title|url|image)"[^>]*>'

# 배포용 태그에 미초기화 값이 없는지 확인한다.
curl -sL "$URL" \
  | rg -i 'undefined|null|nan|\{\{[^}]+\}\}'
```

<br />

두 번째 명령은 HTML 전체에서 문자열을 찾으므로 본문에 "null" 같은 단어가 있는 문서에서는 오탐이 날 수 있습니다. 운영에서는 head 영역만 추출하거나, title과 description 속성만 파싱하도록 조금 더 정교하게 다듬는 편이 좋습니다. 여기서는 배포 직후 명백한 템플릿 오류를 찾는 용도로 충분합니다.

<br />

Search Console의 URL 검사 도구에서는 Google이 받은 페이지의 색인 상태와 렌더링 결과를 추가로 확인할 수 있습니다. 공유 미리보기는 실제 메신저에 링크를 보내기 전에, 해당 서비스가 제공하는 링크 디버거로 점검하면 좋습니다.

<br />
<br />
<br />

## 정리

<br />

title, description, h1, Open Graph는 서로 다른 화면을 위한 중복 작업처럼 보이지만, 실제로는 한 페이지의 정체성을 여러 독자에게 전달하는 방법입니다. 먼저 페이지가 제공하는 가치를 한 문장으로 정하고, 그 문장을 각 요소의 역할에 맞게 풀어 쓰는 방식이 가장 안정적입니다.

<br />

title은 고유하고 사실에 맞아야 하며, description은 클릭 전에 내용을 설명해야 합니다. h1은 본문의 중심을 명확히 보여 주고, Open Graph는 공유될 때도 같은 페이지가 전달되도록 canonical URL과 절대 이미지 URL을 사용합니다.

<br />

다음 글에서는 title과 본문만으로는 표현하기 어려운 정보를 다룹니다. JSON-LD를 이용해 행사, 상품, 글 목록 같은 데이터를 검색엔진이 기계적으로 이해할 수 있게 만드는 구조화 데이터를 살펴보겠습니다.

<br />

### 참고 자료

<br />

- [Influencing Title Links in Google Search](https://developers.google.com/search/docs/appearance/title-link)
- [Meta Tags and Attributes that Google Supports](https://developers.google.com/search/docs/crawling-indexing/special-tags)
- [Valid Page Metadata for Google Search](https://developers.google.com/search/docs/crawling-indexing/valid-page-metadata)
- [Get Started with Search: a Developer's Guide](https://developers.google.com/search/docs/fundamentals/get-started-developers)
- [Open Graph Protocol](https://ogp.me/)
