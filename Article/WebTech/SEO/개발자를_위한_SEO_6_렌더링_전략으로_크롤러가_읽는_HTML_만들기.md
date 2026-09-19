<!--

개발자를 위한 SEO 실전 가이드 6부 - 렌더링 전략으로 크롤러가 읽는 HTML 만들기

-->

## 시작하며

<br />

브라우저에서는 정상인데 검색엔진에서는 페이지가 비어 보이는 문제가 있습니다. 대개 화면을 만드는 JavaScript가 실패한 것이 아니라, 검색엔진이 최초로 받은 HTML에 본문과 링크가 없기 때문에 생깁니다.

<br />

Google은 JavaScript를 실행할 수 있습니다. 그렇다고 모든 크롤러가 같은 방식으로 렌더링하는 것은 아니며, JavaScript 실행을 기다리는 과정도 추가됩니다. 중요한 페이지라면 최초 응답부터 사람이 읽을 수 있는 정보를 담는 편이 훨씬 안전합니다.

<br />

이번 글에서는 CSR, SSR, SSG를 검색 관점에서 비교하고, 실제 HTML을 기준으로 점검하는 방법을 정리합니다.

<br />
<br />
<br />

## 렌더링 방식은 첫 HTML의 내용으로 구분한다

<br />

이름보다 중요한 질문은 하나입니다. `curl`로 받아 본 최초 HTML에 제목, 본문, 링크가 들어 있는가입니다.

<br />

```text
CSR(Client-Side Rendering)
서버는 앱 껍데기와 JavaScript를 보낸다.
브라우저가 JavaScript를 실행한 뒤 본문을 만든다.

SSR(Server-Side Rendering)
요청마다 서버가 본문이 포함된 HTML을 만든다.
사용자는 응답을 받자마자 주요 내용을 볼 수 있다.

SSG(Static Site Generation)
빌드 시점에 본문이 포함된 HTML을 미리 만든다.
변경이 드문 콘텐츠에 특히 잘 맞는다.
```

<br />

Google의 JavaScript 처리 과정은 크롤링, 렌더링, 색인의 단계로 나뉩니다. 최초 HTML에서 링크를 발견한 뒤 렌더링 큐를 거쳐 JavaScript를 실행한 결과도 다시 읽습니다. 따라서 CSR 페이지도 색인될 수 있지만, 렌더링에만 기대면 오류와 지연이 들어갈 여지가 커집니다.

<br />

검색 유입이 중요한 상세 페이지, 문서, 카테고리 페이지는 SSR 또는 SSG를 우선 검토하는 편이 좋습니다. 로그인 후 개인화된 화면처럼 공개 검색 대상이 아닌 페이지까지 억지로 서버 렌더링할 필요는 없습니다.

<br />
<br />
<br />

## JavaScript가 있어도 링크는 HTML 링크여야 한다

<br />

검색엔진은 일반적으로 `href`가 있는 `<a>` 요소에서 URL을 발견합니다. 카드 전체를 클릭 가능하게 만들더라도 `div`의 클릭 이벤트만 두지 말고, 실제 링크를 넣어야 합니다.

<br />

```tsx
// 좋지 않은 예: 주소가 HTML에 드러나지 않는다.
<article onClick={() => router.push(`/posts/${post.slug}`)}>
  <h2>{post.title}</h2>
</article>

// 권장: 사용자와 크롤러가 같은 주소를 본다.
<article>
  <h2>
    <Link href={`/posts/${post.slug}`}>{post.title}</Link>
  </h2>
</article>
```

<br />

`#/posts/hello`처럼 해시 조각으로 화면을 바꾸는 라우팅도 피하는 편이 좋습니다. 해시 뒤의 값은 서버에 요청되는 경로가 아니므로, 각 화면에 고유한 URL과 상태 코드를 제공하기 어렵습니다.

<br />

링크를 JavaScript로 나중에 삽입하더라도 최종 DOM에 정상 `<a href>`로 나타나면 Google은 읽을 수 있습니다. 그러나 메뉴와 목록의 핵심 링크는 최초 HTML에도 있는 편이 디버깅과 호환성 면에서 유리합니다.

<br />
<br />
<br />

## Next.js에서는 서버 컴포넌트부터 사용한다

<br />

App Router의 페이지와 레이아웃은 기본적으로 서버 컴포넌트입니다. 데이터 조회와 본문 렌더링을 서버 컴포넌트에 두고, 클릭이나 입력처럼 상호작용이 필요한 작은 영역만 클라이언트 컴포넌트로 분리하면 됩니다.

<br />

```tsx
// app/posts/[slug]/page.tsx
import { notFound } from 'next/navigation'

export default async function PostPage({
  params,
}: {
  params: Promise<{ slug: string }>
}) {
  const { slug } = await params
  const post = await getPublishedPost(slug)

  if (!post) {
    notFound()
  }

  return (
    <article>
      <h1>{post.title}</h1>
      <p>{post.summary}</p>
      <PostBody html={post.html} />
    </article>
  )
}
```

<br />

이 페이지에 댓글 버튼, 목차 펼치기, 좋아요 같은 기능이 필요해도 문서 본문까지 `'use client'`로 바꿀 이유는 없습니다. 상호작용 컴포넌트만 분리하면 최초 HTML에는 제목과 본문이 남습니다.

<br />

동적 상세 페이지에서 존재하지 않는 슬러그는 화면만 "없음"으로 바꾸지 말고 실제 `404` 응답을 보내야 합니다. SPA가 모든 URL에 `200`을 반환하면 검색엔진은 빈 오류 화면을 정상 페이지로 오해할 수 있습니다.

<br />
<br />
<br />

## 렌더링 결과가 아니라 응답을 확인한다

<br />

개발자 도구의 Elements 탭은 JavaScript 실행 뒤의 DOM을 보여 줍니다. SEO 문제를 확인할 때는 먼저 View Source 또는 HTTP 응답을 봐야 합니다.

<br />

```bash
# 최초 응답 HTML에 제목, 본문, 링크가 있는지 확인한다.
curl -sL https://example.com/posts/seo-basics > /tmp/page.html

rg -n '<title>|<h1|SEO의 기본|href="/posts/' /tmp/page.html

# 존재하지 않는 페이지가 실제 404인지 확인한다.
curl -sI https://example.com/posts/this-slug-does-not-exist
```

<br />

아래 현상은 렌더링 전략을 다시 봐야 한다는 신호입니다.

<br />

- 응답 HTML에 `<div id="root"></div>`만 있고 본문이 없다.
- 목록 화면에는 카드가 보이지만 실제 `<a href>` 링크가 없다.
- 존재하지 않는 URL도 `200 OK`로 응답한다.
- 서버의 `<title>`과 클라이언트에서 바꾼 `<title>`이 다르다.
- 검색 대상 페이지가 로그인 API나 브라우저 전용 API에 의존한다.

<br />

렌더링을 바꾼 뒤에는 Search Console의 URL 검사 도구로 Google이 가져온 페이지도 확인하세요. 로컬 브라우저에서만 정상인 문제를 구분하는 데 도움이 됩니다.

<br />
<br />
<br />

## 정리

<br />

Google이 JavaScript를 실행할 수 있다는 사실은 CSR만으로 충분하다는 뜻이 아닙니다. 검색 대상 페이지의 제목, 본문, 링크, 대표 URL은 가능한 한 최초 HTML에 포함시키는 것이 좋습니다.

<br />

렌더링 전략을 논의할 때는 프레임워크 이름보다 실제 응답을 확인하세요. `curl`로 받은 HTML에 사용자가 읽을 정보와 크롤러가 따라갈 링크가 있다면, 검색엔진이 페이지를 이해할 수 있는 기반을 갖춘 것입니다.

<br />

### 참고 자료

<br />

- [Google Search Central - JavaScript SEO 기본](https://developers.google.com/search/docs/crawling-indexing/javascript/javascript-seo-basics)
- [Google Search Central - 크롤링 가능한 링크](https://developers.google.com/search/docs/crawling-indexing/links-crawlable)
- [Google Search Central - JavaScript 문제 해결](https://developers.google.com/search/docs/crawling-indexing/javascript/javascript-seo-basics)
