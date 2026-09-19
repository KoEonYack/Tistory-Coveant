<!--

개발자를 위한 SEO 실전 가이드 3부 - canonical과 리다이렉트로 대표 URL 정리하기

-->

## 시작하며

<br />

같은 게시글인데 주소가 여러 개로 열리는 일은 생각보다 흔합니다. HTTP와 HTTPS, www 유무, 마지막 슬래시, SNS에서 붙인 UTM 파라미터, 정렬 옵션이 그 원인입니다.

<br />

사람에게는 같은 페이지여도 검색엔진에게는 서로 다른 URL입니다. 검색엔진은 그중 하나를 대표 URL, 즉 canonical URL로 고르려고 합니다. 이 선택을 서비스가 전혀 안내하지 않으면, 검색 결과와 Search Console 데이터가 의도와 다르게 분산될 수 있습니다.

<br />

이번 글에서는 같은 콘텐츠의 URL을 하나로 정리하는 세 가지 도구를 다룹니다. 아직 유효한 이전 주소는 영구 리다이렉트로 보내고, 접근은 유지해야 하는 중복 페이지에는 canonical을 지정하고, 검색 결과에 보여서는 안 되는 공개 페이지에는 noindex를 사용합니다.

<br />
<br />
<br />

## canonical URL은 대표 페이지를 고르는 기준이다

<br />

canonicalization은 중복되거나 매우 비슷한 페이지 묶음에서 대표 URL을 선택하는 과정입니다. Google은 HTTP와 HTTPS 변형, 정렬과 필터 결과, 데스크톱과 모바일 변형처럼 다양한 이유로 중복 URL이 생긴다고 설명합니다. [Google의 canonicalization 문서](https://developers.google.com/search/docs/crawling-indexing/canonicalization)도 canonical을 중복 콘텐츠 묶음의 대표 URL로 정의합니다.

<br />

예를 들어 다음 주소들은 모두 같은 글을 열 수 있습니다.

<br />

```text
https://example.com/posts/seo-basics
https://example.com/posts/seo-basics/
http://example.com/posts/seo-basics
https://www.example.com/posts/seo-basics
https://example.com/posts/seo-basics?utm_source=newsletter
```

<br />

이때 서비스가 선택한 대표 주소가 아래라면, 가능한 모든 신호가 이 주소를 향하도록 맞추는 것이 목표입니다.

<br />

```text
https://example.com/posts/seo-basics
```

<br />

대표 URL을 고르는 데는 세 가지 신호를 함께 사용합니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:28%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">신호</th>
      <th style="width:40%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">역할</th>
      <th style="width:32%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">적합한 상황</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>영구 리다이렉트</strong></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">이전 URL로 온 사용자와 크롤러를 대표 URL로 바로 보냅니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">이전 주소를 더 이상 유지할 이유가 없을 때</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">rel="canonical"</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">열람은 유지하되, 대표로 삼고 싶은 URL을 알려줍니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">UTM 파라미터, 출력용 보기처럼 거의 같은 페이지가 남아야 할 때</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">noindex</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">공개 페이지를 검색 결과에서 제외합니다.</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">개인화 화면, 내부 검색 결과처럼 검색 가치가 없는 페이지</td>
    </tr>
  </tbody>
</table>

<br />

이 셋은 대체 관계가 아닙니다. URL의 성격에 맞는 방법을 골라야 합니다. 특히 canonical은 검색엔진에 보내는 <strong>선호 신호</strong>이지 강제 명령이 아닙니다. Google이 내용 품질, 내부 링크, sitemap, 리다이렉트 같은 다른 신호를 함께 보고 다른 URL을 canonical로 선택할 수 있습니다. 그래서 한 가지 태그만 추가하기보다, 선택한 대표 URL을 서비스 전반에서 일관되게 사용하는 편이 중요합니다.

<br />
<br />
<br />

## self-canonical부터 정확히 넣는다

<br />

대부분의 색인 대상 페이지는 자기 자신을 canonical로 가리키는 self-canonical을 가져야 합니다.

<br />

```html
<link rel="canonical" href="https://example.com/posts/seo-basics" />
```

<br />

이 한 줄은 반드시 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">head</code> 안에 있어야 하며, 파라미터 없는 절대 URL을 사용합니다. 아래처럼 요청 URL을 그대로 넣으면 UTM 파라미터가 붙은 변형 URL이 또 하나의 대표 후보가 되어 버릴 수 있습니다.

<br />

```text
피해야 할 예시
https://example.com/posts/seo-basics?utm_source=newsletter
```

<br />

Next.js App Router에서는 metadata API로 canonical을 함께 관리할 수 있습니다.

<br />

```ts
// app/posts/[slug]/page.tsx
import type { Metadata } from 'next'

const SITE_URL = 'https://example.com'

export async function generateMetadata({
  params,
}: {
  params: Promise<{ slug: string }>
}): Promise<Metadata> {
  const { slug } = await params
  const post = await getPublishedPostBySlug(slug)

  if (!post) {
    return {}
  }

  const path = `/posts/${encodeURIComponent(post.slug)}`
  const canonical = `${SITE_URL}${path}`

  return {
    title: `${post.title} | Example Blog`,
    alternates: {
      canonical,
    },
  }
}
```

<br />

중요한 점은 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">metadataBase</code> 또는 절대 URL 생성 규칙을 프로젝트에서 하나로 정해 두는 것입니다. 예시처럼 완성된 절대 URL을 넣거나, 프로젝트 전역에서 metadataBase를 올바르게 설정해야 합니다. 어떤 방식을 택하든 최종 HTML에는 운영 도메인을 기준으로 한 canonical URL이 렌더되는지 반드시 확인해야 합니다.

<br />

Pages Router나 프레임워크를 직접 쓰지 않는 사이트에서는 head에 같은 값을 직접 넣습니다.

<br />

```tsx
import Head from 'next/head'

const SITE_URL = 'https://example.com'

export function PostSeo({ slug }: { slug: string }) {
  const canonical = `${SITE_URL}/posts/${encodeURIComponent(slug)}`

  return (
    <Head>
      <link rel="canonical" href={canonical} />
    </Head>
  )
}
```

<br />

사이트의 모든 상세 페이지에 같은 값이 들어가거나, 개발 서버 주소가 들어가거나, <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">undefined/posts/123</code>처럼 환경 변수 누락 문자열이 들어가는 사고가 가장 위험합니다. canonical URL을 만드는 함수를 한 곳에 두고, 배포된 HTML을 샘플링해 확인하는 습관이 필요합니다.

<br />
<br />
<br />

## 서로 같은 페이지에만 canonical을 사용한다

<br />

canonical은 "이 페이지를 검색 결과에서 숨기고 싶다"는 용도로 쓰는 태그가 아닙니다. canonical 대상은 현재 페이지와 내용이 같거나 아주 유사해야 합니다.

<br />

아래 예시를 비교해 보겠습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:32%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">상황</th>
      <th style="width:34%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">권장 처리</th>
      <th style="width:34%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">이유</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">UTM 파라미터만 다른 상세 페이지</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">파라미터 없는 self-canonical</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">사용자에게 보이는 핵심 내용이 같습니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">`/products?page=2`처럼 다른 상품을 보여 주는 페이지네이션</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">각 페이지 self-canonical</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">2페이지도 별도의 상품 목록이라는 고유한 내용을 가집니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">`/search?q=...` 내부 검색 결과</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">대개 noindex, follow</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">검색어 조합이 무한히 늘며, 각 결과가 목록과 동등한 문서가 아닙니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">`/events/tag/ai`처럼 고유한 소개와 목록이 있는 태그 랜딩</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">self-canonical 또는 색인 여부를 별도 판단</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">검색 의도가 목록 페이지와 다르고, 독립적인 랜딩 페이지가 될 수 있습니다.</td>
    </tr>
  </tbody>
</table>

<br />

특히 페이지네이션을 모두 1페이지로 canonical 처리하는 실수가 자주 보입니다. 2페이지 이후에도 사용자에게 새로운 항목이 보인다면, 그 페이지들은 중복이 아닙니다. 마찬가지로 AI 행사만 모은 태그 랜딩이 일반 행사 목록과 다른 설명과 콘텐츠를 가진다면, 일반 목록을 canonical로 가리키지 말아야 합니다.

<br />

canonical로 통합하고 싶은 변형은 내부 링크, sitemap, Open Graph URL도 같은 주소를 가리키도록 맞추는 편이 좋습니다. Google은 영구 리다이렉트와 canonical 링크를 강한 신호로, sitemap 포함을 약한 신호로 보며, 이 신호들이 서로 일치할수록 대표 URL 선택 가능성이 높아진다고 안내합니다. [중복 URL 통합 가이드](https://developers.google.com/search/docs/crawling-indexing/consolidate-duplicate-urls)를 참고할 수 있습니다.

<br />
<br />
<br />

## URL을 없앨 때는 영구 리다이렉트를 사용한다

<br />

이전 주소를 앞으로도 열어 둘 필요가 없다면 canonical보다 서버 측 영구 리다이렉트가 더 분명한 선택입니다. 사용자가 이전 URL을 열어도 즉시 새 URL로 이동하고, 검색엔진도 이전 주소가 이동했다는 신호를 받습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:20%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">상태 코드</th>
      <th style="width:30%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">이동 성격</th>
      <th style="width:50%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">주로 쓰는 상황</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">301</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">영구 이동</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">일반적인 서버 설정, 오래된 URL 이전</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">308</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">영구 이동, HTTP 메서드 유지</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">Next.js 설정, POST 메서드 보존이 필요한 경우</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">302</code> / <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">307</code></td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">임시 이동</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">점검, A/B 테스트, 짧은 기간의 안내 페이지</td>
    </tr>
  </tbody>
</table>

<br />

Google은 301과 308을 영구 이동, 302와 307을 임시 이동으로 해석합니다. 영구 이동은 대상 URL이 canonical이 되어야 한다는 신호가 되지만, 임시 이동은 기존 URL을 검색 결과에 유지하는 데 사용됩니다. [Google의 리다이렉트 가이드](https://developers.google.com/search/docs/crawling-indexing/301-redirects)를 기준으로, 되돌릴 계획이 없는 URL 변경에는 301 또는 308을 선택합니다.

<br />

Next.js에서는 다음처럼 영구 리다이렉트를 선언할 수 있습니다.

<br />

```ts
// next.config.ts
import type { NextConfig } from 'next'

const nextConfig: NextConfig = {
  async redirects() {
    return [
      {
        source: '/blog/:slug',
        destination: '/posts/:slug',
        permanent: true,
      },
    ]
  },
}

export default nextConfig
```

<br />

Next.js의 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">permanent: true</code>는 308 응답을 만듭니다. 중요한 것은 상태 코드 자체보다, 이전 URL이 한 번에 최종 URL로 가는지입니다.

<br />

```text
좋은 흐름
http://example.com/blog/seo-basics
  -> https://example.com/posts/seo-basics

피해야 할 흐름
http://example.com/blog/seo-basics
  -> https://www.example.com/blog/seo-basics
  -> https://www.example.com/posts/seo-basics
  -> https://example.com/posts/seo-basics
```

<br />

리다이렉트 체인은 사용자 로딩 시간과 크롤링 요청을 늘립니다. 도메인 통합, 경로 변경, 슬래시 정책을 정했다면 가능한 한 첫 응답에서 최종 URL로 보내는 것이 좋습니다.

<br />
<br />
<br />

## noindex는 검색 결과 제외를 위한 별도 도구다

<br />

canonical과 noindex는 함께 언급되지만 목적이 다릅니다. canonical은 비슷한 페이지 중 대표를 고르는 신호이고, noindex는 해당 페이지를 검색 결과에서 제외하는 지시입니다.

<br />

```html
<meta name="robots" content="noindex, follow" />
```

<br />

내부 검색 결과, 로그인 후 개인화 화면, 조합이 지나치게 많은 필터 화면처럼 검색 결과에서 독립적으로 보여 줄 가치가 낮은 HTML 페이지에는 noindex가 적합할 수 있습니다. 반면 사용자에게 유용한 태그 랜딩과 페이지네이션을 단지 URL이 많다는 이유로 noindex 처리하면, 실제로 유입될 수 있는 검색 기회를 버릴 수 있습니다.

<br />

2부에서 다룬 것처럼 noindex가 동작하려면 크롤러가 페이지를 읽을 수 있어야 합니다. 같은 URL을 robots.txt에서 막으면 크롤러가 noindex 메타 태그를 확인하지 못합니다. [Google의 noindex 문서](https://developers.google.com/search/docs/crawling-indexing/block-indexing)도 이 조합을 피하라고 안내합니다.

<br />

검색 결과 URL을 noindex 처리한다고 해서 그 URL의 canonical을 무조건 목록 페이지로 지정할 필요는 없습니다. canonical은 페이지 간의 내용 유사성으로 결정하고, noindex는 색인 여부로 결정해야 합니다. 두 판단을 분리하면 서로 다른 콘텐츠를 잘못 합치는 실수를 줄일 수 있습니다.

<br />
<br />
<br />

## 배포 전에 확인할 항목

<br />

canonical과 리다이렉트는 브라우저 화면만으로는 놓치기 쉽습니다. HTML과 HTTP 응답을 직접 확인하는 편이 정확합니다.

<br />

```bash
# 최종 페이지의 canonical을 확인한다.
curl -sL https://example.com/posts/seo-basics \
  | rg -o '<link rel="canonical"[^>]*>'

# 첫 응답의 리다이렉트 코드와 목적지를 확인한다.
curl -s -D - -o /dev/null https://example.com/blog/seo-basics \
  | rg -i '^(HTTP/|location:)'

# 리다이렉트를 끝까지 따라가며 체인이 있는지 확인한다.
curl -sL -D - -o /dev/null https://example.com/blog/seo-basics \
  | rg -i '^(HTTP/|location:)'

# noindex가 의도한 화면에만 있는지 확인한다.
curl -sL 'https://example.com/search?q=seo' \
  | rg -i 'name="robots"'
```

<br />

Search Console의 URL 검사에서는 사용자가 지정한 canonical과 Google이 선택한 canonical을 함께 볼 수 있습니다. 둘이 다르면 태그만 의심하지 말고, 실제 내용이 같은지, sitemap과 내부 링크가 어디를 가리키는지, 리다이렉트가 있는지를 한 묶음으로 확인해야 합니다.

<br />
<br />
<br />

## 정리

<br />

대표 URL을 정리하는 일은 검색엔진을 설득하는 일이라기보다, 서비스가 어떤 주소를 공식 주소로 사용할지 명확히 선언하는 일입니다. sitemap, 내부 링크, canonical, Open Graph URL, 리다이렉트가 같은 주소를 향해야 이 선언이 힘을 가집니다.

<br />

없애도 되는 이전 주소는 301 또는 308으로 한 번에 보냅니다. 접근을 유지해야 하는 거의 같은 페이지는 self-canonical 또는 적절한 canonical을 사용합니다. 검색 결과에 보여서는 안 되는 페이지는 noindex로 처리합니다.

<br />

다음 글에서는 대표 URL 위에 올라가는 한 페이지의 정보를 다룹니다. title, description, h1, Open Graph를 어떻게 함께 설계해야 검색 결과와 공유 미리보기 모두에서 문서의 목적이 선명해지는지 살펴보겠습니다.

<br />

### 참고 자료

<br />

- [What is URL Canonicalization](https://developers.google.com/search/docs/crawling-indexing/canonicalization)
- [How to Specify a Canonical URL](https://developers.google.com/search/docs/crawling-indexing/consolidate-duplicate-urls)
- [Redirects and Google Search](https://developers.google.com/search/docs/crawling-indexing/301-redirects)
- [Block Search Indexing with noindex](https://developers.google.com/search/docs/crawling-indexing/block-indexing)
