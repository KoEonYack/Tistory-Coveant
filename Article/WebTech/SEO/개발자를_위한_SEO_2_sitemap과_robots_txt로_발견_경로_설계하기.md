<!--

개발자를 위한 SEO 실전 가이드 2부 - sitemap과 robots.txt로 발견 경로 설계하기

-->

## 시작하며

<br />

1부에서는 검색엔진 노출이 URL 발견, 크롤링, 색인, 노출의 순서로 진행된다고 정리했습니다. 이번 글은 그 첫 단계인 발견을 실제로 설계하는 방법입니다.

<br />

서비스가 커질수록 URL은 생각보다 빠르게 늘어납니다. 상품 상세, 게시글, 행사, 채용 공고, 사용자 프로필처럼 데이터에서 만들어지는 페이지가 대표적입니다. 화면에서 잘 열리는 것과 검색엔진이 그 페이지를 빠짐없이 발견하는 것은 별개의 문제입니다.

<br />

이때 주로 등장하는 파일이 sitemap.xml과 robots.txt입니다. 이름은 익숙하지만 역할을 반대로 이해하면 SEO를 오히려 어렵게 만들 수 있습니다. sitemap은 중요한 URL을 알려주는 목록이고, robots.txt는 크롤러의 요청 범위를 관리하는 규칙입니다. 둘 다 검색 결과에서 페이지를 지우는 만능 스위치는 아닙니다.

<br />
<br />
<br />

## sitemap은 사이트의 전체 주소록이 아니다

<br />

sitemap.xml은 검색엔진에 "이 URL들은 내가 검색 결과에 남기고 싶은 대표 페이지입니다"라고 알려주는 XML 문서입니다. 모든 URL을 기계적으로 넣는 파일이 아니라, 색인 후보를 선별한 목록에 가깝습니다.

<br />

가장 작은 sitemap은 아래처럼 생겼습니다.

<br />

```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>https://example.com/posts/seo-basics</loc>
    <lastmod>2026-09-14</lastmod>
  </url>
</urlset>
```

<br />

여기서 가장 중요한 값은 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">loc</code>입니다. 상대 경로가 아니라 <strong>HTTPS를 포함한 절대 URL</strong>을 넣어야 합니다. Google은 sitemap에 적힌 URL을 그대로 크롤링하려고 시도하므로, 테스트 도메인이나 리다이렉트 URL이 섞이지 않도록 주의해야 합니다. [Google sitemap 문서](https://developers.google.com/search/docs/crawling-indexing/sitemaps/build-sitemap)도 색인하고 싶은 canonical URL을 sitemap에 넣으라고 안내합니다.

<br />

아래 기준으로 URL을 나누면 판단이 쉬워집니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:32%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">URL 유형</th>
      <th style="width:22%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">sitemap 포함</th>
      <th style="width:46%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">이유</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">게시글, 상품, 행사처럼 독립적인 상세 페이지</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>포함</strong></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">검색 사용자가 직접 찾을 수 있고, 고유한 내용을 가진 대표 URL입니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">소개, 이용 안내, 카테고리 허브</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>대개 포함</strong></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">사용자에게 독립적인 가치를 주고, 내부 링크의 중심 역할을 합니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">리다이렉트되는 이전 주소</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>제외</strong></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">최종 목적지 URL만 대표로 둡니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">404, 410, 5xx를 반환하는 주소</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>제외</strong></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">색인 후보 목록에 실패한 주소를 섞으면 운영 신호가 모순됩니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">검색, 정렬, 필터 조합 URL</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>대개 제외</strong></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">조합 수가 끝없이 늘고, 서로 비슷한 결과를 만들기 쉽습니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">noindex</code> 페이지</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><strong>제외</strong></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">"색인해 달라"와 "색인하지 말아 달라"는 서로 충돌하는 신호입니다.</td>
    </tr>
  </tbody>
</table>

<br />

예를 들어 상세 페이지가 3,000개인데 sitemap에는 목록과 소개 페이지 두 개만 있다면, 검색엔진은 대부분의 상세 페이지를 내부 링크나 외부 링크를 통해 우연히 발견해야 합니다. 이것이 곧바로 색인 실패를 뜻하지는 않지만, 새로운 콘텐츠를 발견하는 경로가 느리고 불확실해집니다.

<br />

반대로 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">?sort=latest&amp;page=4</code> 같은 모든 필터 조합을 넣으면 중요한 상세 페이지보다 의미가 약한 URL이 sitemap을 채우게 됩니다. sitemap을 많이 채우는 것보다, 신뢰할 수 있는 대표 URL만 넣는 편이 중요합니다.

<br />
<br />
<br />

## lastmod는 실제로 바뀐 시각만 기록한다

<br />

<code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">lastmod</code>는 해당 URL의 의미 있는 내용이 마지막으로 변경된 시각입니다. 게시글 본문을 고쳤거나, 상품 정보가 바뀌었거나, 행사 일정이 변경됐다면 그 시각을 넣는 것이 맞습니다.

<br />

반면 sitemap을 요청할 때마다 현재 시각을 찍으면 안 됩니다.

<br />

```ts
// 잘못된 예시: sitemap을 만드는 시각을 모든 URL의 수정 시각으로 사용한다.
lastModified: new Date()
```

<br />

이 구현은 콘텐츠가 전혀 바뀌지 않았어도 모든 페이지가 방금 수정된 것처럼 보이게 합니다. 검색엔진 입장에서는 변경 신호를 믿기 어려워지고, 정작 갱신된 페이지를 구별하는 데 도움이 되지 않습니다.

<br />

데이터베이스의 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">updated_at</code>처럼 실제 데이터 변경과 연결된 값을 쓰는 편이 좋습니다.

<br />

```ts
// 적절한 예시: 콘텐츠의 실제 수정 시각을 사용한다.
lastModified: article.updatedAt
```

<br />

Google은 sitemap의 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">changefreq</code>와 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">priority</code>를 사용하지 않는다고 명시했습니다. 그러므로 "매일 갱신"이나 "우선순위 1.0" 같은 값을 정성 들여 조정하기보다, 올바른 URL과 정확한 lastmod를 유지하는 데 시간을 쓰는 편이 낫습니다. [Google Search Central의 안내](https://developers.google.com/search/blog/2023/06/sitemaps-lastmod-ping)를 참고할 수 있습니다.

<br />

파일 하나에는 최대 50,000개의 URL 또는 압축하지 않은 상태로 50MB까지만 담을 수 있습니다. 이 한도를 넘을 때만 sitemap index로 분할하면 됩니다. 3,000개 정도의 상세 페이지를 가진 서비스라면, 처음부터 연도별 파일을 여러 개로 나눌 이유가 거의 없습니다.

<br />

```xml
<?xml version="1.0" encoding="UTF-8"?>
<sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <sitemap>
    <loc>https://example.com/sitemaps/posts-1.xml</loc>
  </sitemap>
  <sitemap>
    <loc>https://example.com/sitemaps/posts-2.xml</loc>
  </sitemap>
</sitemapindex>
```

<br />

분할이 필요한 시점에는 콘텐츠 유형이나 데이터 범위를 기준으로 나누면 운영하기 편합니다. 예를 들어 게시글, 상품, 문서처럼 유형별로 분리하거나, 데이터가 매우 많다면 ID 구간별로 나눌 수 있습니다. URL의 표시 순서는 Google의 우선순위를 결정하지 않습니다.

<br />
<br />
<br />

## Next.js App Router에서 동적 sitemap 만들기

<br />

정적 사이트라면 직접 작성한 sitemap.xml 파일 하나로 충분합니다. 하지만 게시글이나 상품처럼 데이터가 계속 추가되는 서비스라면 애플리케이션이 sitemap을 생성하는 편이 안전합니다.

<br />

Next.js App Router에서는 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">app/sitemap.ts</code> 파일을 이용할 수 있습니다. 아래 예시는 게시글 목록에서 slug와 실제 수정 시각만 가져와 sitemap을 만듭니다.

<br />

```ts
// app/sitemap.ts
import type { MetadataRoute } from 'next'

const SITE_URL = 'https://example.com'

type PostForSitemap = {
  slug: string
  updatedAt: Date
}

async function getPublishedPosts(): Promise<PostForSitemap[]> {
  // DB에서는 공개 상태인 글의 slug와 updatedAt만 조회한다.
  return []
}

export default async function sitemap(): Promise<MetadataRoute.Sitemap> {
  const posts = await getPublishedPosts()

  return [
    {
      url: SITE_URL,
      lastModified: new Date('2026-09-01'),
    },
    {
      url: `${SITE_URL}/posts`,
      lastModified: new Date('2026-09-01'),
    },
    ...posts.map((post) => ({
      url: `${SITE_URL}/posts/${encodeURIComponent(post.slug)}`,
      lastModified: post.updatedAt,
    })),
  ]
}
```

<br />

이 방식의 장점은 XML 문자열을 직접 조합하지 않아도 된다는 점입니다. Next.js가 반환한 배열을 sitemap 형식으로 변환해 줍니다. [Next.js sitemap 파일 규칙](https://nextjs.org/docs/app/api-reference/file-conventions/metadata/sitemap)에서는 대규모 사이트를 위한 분할 생성 방법도 함께 제공합니다.

<br />

다만 코드 자체보다 데이터 선택이 더 중요합니다. sitemap 쿼리에서는 본문 전체나 이미지 목록을 읽을 필요가 없습니다. 공개 여부, URL을 만들 식별자, 마지막 수정 시각 정도만 가져오면 됩니다. 비공개 글, 임시 저장 글, 삭제 예정 글은 쿼리 단계에서 제외해야 합니다.

<br />

Pages Router를 사용 중이라면 sitemap 요청을 처리하는 페이지에서 XML을 응답으로 직접 내려줄 수 있습니다. 오래된 프로젝트를 유지보수하는 상황에서는 아래 방식이 실용적입니다.

<br />

```tsx
// pages/sitemap.xml.tsx
import type { GetServerSideProps } from 'next'

const SITE_URL = 'https://example.com'

export const getServerSideProps: GetServerSideProps = async ({ res }) => {
  const posts = await getPublishedPostsForSitemap()

  const urls = posts
    .map(
      (post) =>
        `<url>` +
        `<loc>${SITE_URL}/posts/${encodeURIComponent(post.slug)}</loc>` +
        `<lastmod>${post.updatedAt.toISOString()}</lastmod>` +
        `</url>`,
    )
    .join('')

  const xml =
    `<?xml version="1.0" encoding="UTF-8"?>` +
    `<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">` +
    `<url><loc>${SITE_URL}/</loc></url>` +
    urls +
    `</urlset>`

  res.setHeader('Content-Type', 'application/xml; charset=utf-8')
  res.setHeader('Cache-Control', 'public, s-maxage=3600, stale-while-revalidate=86400')
  res.write(xml)
  res.end()

  return { props: {} }
}

export default function Sitemap() {
  return null
}
```

<br />

실무에서는 URL에 들어가는 slug가 데이터베이스에서 온 값이라도 XML 이스케이프와 URL 인코딩을 고려해야 합니다. 예제처럼 slug를 URL 경로로 쓴다면 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">encodeURIComponent</code>를 적용하고, 제목이나 설명문을 XML에 추가한다면 별도의 XML escape 함수를 두는 편이 안전합니다.

<br />

동적 sitemap을 만들었다면, 같은 경로의 정적 파일이 남아 있지 않은지도 확인해야 합니다. 예를 들어 Pages Router에 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">pages/sitemap.xml.tsx</code>를 추가하면서 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">public/sitemap.xml</code>을 그대로 두면 프레임워크 라우팅과 충돌할 수 있습니다. 생성 방식은 하나만 선택하는 것이 좋습니다.

<br />
<br />
<br />

## robots.txt는 크롤러의 출입 규칙이다

<br />

robots.txt는 도메인 루트에 두는 텍스트 파일입니다. 크롤러가 어떤 경로를 요청해도 되는지 알려주는 역할을 합니다.

<br />

```text
https://example.com/robots.txt
```

<br />

가장 단순한 설정은 다음과 같습니다.

<br />

```text
User-agent: *
Allow: /

Disallow: /account/
Disallow: /api/

Sitemap: https://example.com/sitemap.xml
```

<br />

<code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">User-agent: *</code>는 모든 크롤러를 뜻합니다. <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">Allow: /</code>는 기본적으로 사이트 전체의 크롤링을 허용하고, <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">Disallow</code>는 특정 경로의 요청을 피하도록 알립니다. 마지막 Sitemap 줄은 sitemap 위치를 발견시키는 보조 경로입니다.

<br />

robots.txt에는 비밀번호, 관리자 URL, 민감한 경로를 숨기는 보안 기능이 없습니다. 파일이 공개되어 있고, 규칙을 따르지 않는 클라이언트도 존재합니다. 진짜 비공개 데이터는 인증과 권한 검사로 보호해야 합니다.

<br />

또한 CSS, JavaScript, 이미지처럼 페이지를 이해하는 데 필요한 리소스를 습관적으로 막으면 안 됩니다. 렌더링에 필요한 파일을 막으면 검색엔진이 페이지 내용을 제대로 해석하지 못할 수 있습니다. [Google robots.txt 가이드](https://developers.google.com/search/docs/crawling-indexing/robots/intro)는 robots.txt의 주된 용도를 크롤 요청 관리로 설명합니다.

<br />

robots.txt를 적용할 만한 경로는 다음과 같습니다.

<br />

```text
- 인증이 필요한 개인 설정 화면처럼 검색엔진이 방문할 이유가 없는 경로
- 외부 사용자를 위한 JSON API처럼 HTML 페이지가 아닌 대량 응답 경로
- 내용이 거의 같은 파라미터 조합이 매우 많이 생기는 URL
- 서버 부하가 크고, 검색 노출 가치가 없는 내부 도구 경로
```

<br />

하지만 "검색 결과에서 이 페이지를 빼고 싶다"는 요구는 robots.txt가 아니라 다음 절의 noindex를 먼저 검토해야 합니다.

<br />
<br />
<br />

## noindex와 robots.txt를 동시에 걸면 안 되는 이유

<br />

검색 결과에는 보이면 안 되지만 사용자 링크로는 열 수 있어야 하는 페이지가 있습니다. 검색 결과, 장바구니, 사용자별 설정 화면이 대표적입니다. 이런 HTML 페이지에는 noindex를 사용합니다.

<br />

```html
<meta name="robots" content="noindex, follow">
```

<br />

<code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">noindex</code>는 검색엔진이 페이지를 읽은 뒤 검색 색인에 저장하지 않도록 하는 지시입니다. 따라서 크롤러가 페이지에 접근할 수 있어야 이 태그를 확인할 수 있습니다.

<br />

같은 URL을 robots.txt에서 막으면 문제가 생깁니다.

<br />

```text
robots.txt
Disallow: /search/

페이지 HTML
<meta name="robots" content="noindex, follow">
```

<br />

이 경우 크롤러는 <code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">/search/</code>의 HTML을 요청하지 않으므로 noindex도 읽지 못합니다. 다른 사이트가 그 URL로 링크를 걸었다면, 내용 없이 URL만 검색 결과에 남을 가능성도 있습니다.

<br />

두 수단의 차이를 다시 정리하면 다음과 같습니다.

<br />

<table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
  <thead>
    <tr>
      <th style="width:28%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">목적</th>
      <th style="width:36%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">적절한 선택</th>
      <th style="width:36%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">주의할 점</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">검색 결과에서 HTML 페이지를 제외</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">noindex</code></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">robots.txt로 동시에 막지 않아야 크롤러가 태그를 읽습니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">크롤 요청 수를 줄이기</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:3px 6px; border-radius:6px; font-size:14px;">robots.txt</code></td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">검색 결과 제외를 보장하는 수단은 아닙니다.</td>
    </tr>
    <tr>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">민감한 내용을 외부에서 완전히 차단</td>
      <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;">인증, 권한 검사, 삭제</td>
      <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;">robots.txt와 noindex는 접근 제어 기능이 아닙니다.</td>
    </tr>
  </tbody>
</table>

<br />

Google도 noindex가 동작하려면 크롤러가 해당 페이지에 접근할 수 있어야 하며, robots.txt로 막힌 페이지에서는 noindex를 읽을 수 없다고 설명합니다. [Google의 noindex 문서](https://developers.google.com/search/docs/crawling-indexing/block-indexing)를 기준으로 두 설정을 분리해서 관리하는 편이 안전합니다.

<br />
<br />
<br />

## 배포 뒤에는 URL 수와 응답을 같이 확인한다

<br />

sitemap을 배포하고 Search Console에 제출하는 것으로 끝내면 안 됩니다. sitemap이 정상 XML인지, 실제 콘텐츠 수와 URL 수가 비슷한지, 색인하면 안 되는 URL이 섞이지 않았는지를 먼저 확인해야 합니다.

<br />

```bash
# sitemap이 정상 응답하는지와 URL 수를 확인한다.
curl -sL https://example.com/sitemap.xml | rg -c '<loc>'

# XML 문법이 깨지지 않았는지 확인한다.
curl -sL https://example.com/sitemap.xml \
  | python3 -c 'import sys, xml.dom.minidom; xml.dom.minidom.parseString(sys.stdin.read()); print("sitemap XML is valid")'

# robots.txt에 sitemap 경로가 알려져 있는지 확인한다.
curl -sL https://example.com/robots.txt | rg '^Sitemap:'

# 대표 URL이 정상 상태 코드로 응답하는지 확인한다.
curl -sL -o /dev/null -w '%{http_code}\n' https://example.com/posts/seo-basics
```

<br />

동적 사이트라면 데이터베이스의 공개 글 수와 sitemap의 URL 수를 비교하는 테스트를 만들어 두는 것도 좋습니다. 정확히 같은 값일 필요는 없습니다. 홈페이지, 목록, 소개 같은 고정 URL이 더해질 수 있기 때문입니다. 다만 상세 페이지가 1,000개인데 sitemap URL이 10개라면, 구현이나 데이터 조회 범위를 다시 살펴봐야 합니다.

<br />

그다음에는 Search Console의 Sitemaps 보고서에서 처리 오류와 발견된 URL 수를 확인합니다. URL 검사 도구에서는 특정 상세 페이지가 sitemap으로 발견됐는지, Google이 선택한 canonical이 의도한 주소와 같은지도 함께 확인할 수 있습니다.

<br />

sitemap 제출은 색인 보장이나 즉시 반영 요청이 아닙니다. 새 사이트나 중요도가 낮은 URL은 크롤링과 색인에 시간이 걸릴 수 있습니다. 하지만 sitemap이 정확하면, "검색엔진이 중요한 URL을 몰랐을 가능성"을 줄이고 이후의 품질 문제를 더 명확하게 진단할 수 있습니다.

<br />
<br />
<br />

## 정리

<br />

sitemap은 검색 결과에 남기고 싶은 대표 URL의 목록입니다. 공개 상세 페이지, 고유한 카테고리 허브, 서비스 소개처럼 사용자가 직접 찾을 수 있는 주소를 넣고, 리다이렉트·오류·noindex·무한 필터 조합 URL은 제외하는 것이 기본입니다.

<br />

robots.txt는 크롤 요청을 관리하는 파일입니다. 검색 결과에서 페이지를 제거하거나 민감한 내용을 보호하는 수단으로 쓰면 안 됩니다. 공개는 유지하되 검색 결과에서는 빼고 싶은 HTML 페이지는 noindex를 사용하고, 그 페이지는 robots.txt에서 막지 않아야 합니다.

<br />

다음 글에서는 같은 콘텐츠가 여러 URL로 열릴 때 생기는 문제를 다룹니다. canonical, 301·308 리다이렉트, noindex를 함께 사용해 대표 URL을 하나로 정리하는 방법을 살펴보겠습니다.

<br />

### 참고 자료

<br />

- [Build and Submit a Sitemap](https://developers.google.com/search/docs/crawling-indexing/sitemaps/build-sitemap)
- [Sitemaps ping endpoint is going away](https://developers.google.com/search/blog/2023/06/sitemaps-lastmod-ping)
- [Robots.txt Introduction and Guide](https://developers.google.com/search/docs/crawling-indexing/robots/intro)
- [Block Search Indexing with noindex](https://developers.google.com/search/docs/crawling-indexing/block-indexing)
- [Next.js sitemap.xml File Convention](https://nextjs.org/docs/app/api-reference/file-conventions/metadata/sitemap)
