<!--

개발자를 위한 SEO 실전 가이드 5부 - 구조화 데이터로 검색엔진에 의미 전달하기

-->

## 시작하며

<br />

검색 결과에 별점, 일정, 가격, 이동 경로처럼 본문보다 풍부한 정보가 보일 때가 있습니다. 이런 결과를 보면 구조화 데이터만 넣으면 노출이 보장된다고 생각하기 쉽습니다.

<br />

구조화 데이터는 검색 결과를 꾸미는 장치가 아닙니다. 페이지에 있는 정보가 무엇을 뜻하는지 기계가 헷갈리지 않도록 알려 주는 데이터입니다. 일정 소개 페이지라면 제목과 날짜, 장소가 각각 무엇인지 명시하는 식입니다.

<br />

이번 글에서는 구조화 데이터를 넣기 전에 확인할 조건, JSON-LD를 안전하게 만드는 방법, 배포 뒤 검증하는 방법을 정리해 봅니다.

<br />
<br />
<br />

## 구조화 데이터는 페이지 내용을 번역하는 작업이다

<br />

검색엔진은 HTML 본문을 읽어 의미를 추론합니다. 구조화 데이터는 이 추론을 돕는 추가 단서입니다. Google은 구조화 데이터를 페이지의 정보를 설명하고 분류하는 표준 형식으로 정의합니다.

<br />

가장 중요한 원칙은 간단합니다. 구조화 데이터는 **현재 페이지에서 사용자에게 실제로 보이는 사실**을 설명해야 합니다. 페이지에 없는 가격, 이미 끝난 행사 일정, 존재하지 않는 평점처럼 보이지 않는 정보를 넣으면 안 됩니다.

<br />

리치 결과는 구조화 데이터가 유효하다고 해서 반드시 노출되는 기능이 아닙니다. 검색엔진이 페이지 품질, 검색어와의 관련성, 기능 지원 여부 등을 종합해 결정합니다. 따라서 목표는 "리치 결과를 강제로 만들기"가 아니라 "정확한 데이터를 안정적으로 제공하기"여야 합니다.

<br />

Google은 JSON-LD, Microdata, RDFa를 지원하지만, 운영과 유지 보수 관점에서는 JSON-LD를 권장합니다. 화면 HTML과 데이터 모델을 섞지 않아도 되고, 템플릿으로 만들기도 쉽기 때문입니다.

<br />
<br />
<br />

## 먼저 페이지의 데이터 모델을 확인한다

<br />

마크업부터 복사해 붙이는 일은 가장 흔한 실수입니다. 예를 들어 행사 페이지에 `Event`를 넣으려면 최소한 아래 값의 출처가 분명해야 합니다.

<br />

- 행사 이름
- 시작 일시와 시간대
- 실제 개최 장소 또는 온라인 주소
- 주최자 정보
- 가격 또는 무료 여부
- 대표 이미지
- 행사 상세 페이지의 대표 URL

<br />

이 값이 데이터베이스에는 있지만 화면에 노출되지 않는다면, 먼저 화면에서 사용자에게 보여 줄지 결정해야 합니다. 화면에 없는 사실을 검색엔진에만 전달하는 방식은 좋은 해결책이 아닙니다.

<br />

특히 "추후 공지", "일정 미정", "가격 문의"처럼 값이 불완전한 콘텐츠는 억지로 완성된 스키마를 만들기보다 필요한 속성을 빼거나 해당 기능을 적용하지 않는 편이 낫습니다. 적은 속성이라도 정확한 데이터가 더 안전합니다.

<br />
<br />
<br />

## Event JSON-LD는 상세 페이지에서 만든다

<br />

다음 예시는 Next.js App Router의 행사 상세 페이지를 기준으로 합니다. `event` 객체의 값은 화면에 표시하는 값과 같은 데이터에서 가져온다고 가정합니다.

<br />

```tsx
import type { Metadata } from 'next'

const SITE_URL = 'https://example.com'

function toAbsoluteUrl(value: string) {
  return new URL(value, SITE_URL).toString()
}

export default async function EventPage({
  params,
}: {
  params: Promise<{ slug: string }>
}) {
  const { slug } = await params
  const event = await getEventBySlug(slug)

  const canonical = `${SITE_URL}/events/${encodeURIComponent(event.slug)}`
  const schema = {
    '@context': 'https://schema.org',
    '@type': 'Event',
    name: event.title,
    startDate: event.startsAt,
    endDate: event.endsAt ?? undefined,
    eventAttendanceMode: event.onlineUrl
      ? 'https://schema.org/OnlineEventAttendanceMode'
      : 'https://schema.org/OfflineEventAttendanceMode',
    eventStatus: 'https://schema.org/EventScheduled',
    location: event.onlineUrl
      ? {
          '@type': 'VirtualLocation',
          url: event.onlineUrl,
        }
      : {
          '@type': 'Place',
          name: event.venueName,
          address: event.address,
        },
    image: [toAbsoluteUrl(event.imagePath)],
    description: event.summary,
    organizer: {
      '@type': 'Organization',
      name: event.organizerName,
      url: event.organizerUrl,
    },
    offers: event.isFree
      ? {
          '@type': 'Offer',
          price: '0',
          priceCurrency: 'KRW',
          availability: 'https://schema.org/InStock',
          url: canonical,
        }
      : undefined,
  }

  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(schema) }}
      />
      <EventDetail event={event} />
    </>
  )
}
```

<br />

`JSON.stringify` 결과에는 사용자 입력에서 온 `<` 문자가 들어갈 수 있습니다. Next.js에서 사용자 작성 내용을 구조화 데이터에 넣는다면 `<`를 유니코드 이스케이프로 바꾸는 방어 처리를 추가하는 편이 좋습니다.

<br />

```ts
function serializeJsonLd(value: unknown) {
  return JSON.stringify(value).replace(/</g, '\\u003c')
}
```

<br />

이 코드는 스키마가 검색에 보이게 만드는 기능이 아니라, JSON-LD 스크립트 안에서 의도하지 않은 HTML 해석이 일어나는 것을 막기 위한 처리입니다.

<br />
<br />
<br />

## 전역 데이터와 페이지 데이터의 역할을 나눈다

<br />

사이트 전체에는 `Organization`이나 `WebSite`처럼 사이트 자체를 설명하는 데이터를 둘 수 있습니다. 반면 `Event`, `Article`, `Product`는 해당 상세 페이지의 내용을 설명해야 합니다.

<br />

모든 페이지에 모든 유형의 데이터를 한꺼번에 넣을 필요는 없습니다. 예를 들어 행사 목록 페이지에 개별 행사의 `Event` 데이터를 수십 개 넣는 방식은 유지하기 어렵고, 사용자에게 보이는 내용과 어긋나기 쉽습니다. 상세 페이지는 상세 페이지답게, 목록 페이지는 목록 페이지답게 설명하는 편이 낫습니다.

<br />

탐색 경로가 사용자에게 실제로 보인다면 `BreadcrumbList`도 도움이 됩니다. 다만 화면에 없는 경로를 임의로 만들거나, 모든 페이지에 같은 경로를 넣어서는 안 됩니다.

<br />

```tsx
const breadcrumbSchema = {
  '@context': 'https://schema.org',
  '@type': 'BreadcrumbList',
  itemListElement: [
    {
      '@type': 'ListItem',
      position: 1,
      name: '행사',
      item: `${SITE_URL}/events`,
    },
    {
      '@type': 'ListItem',
      position: 2,
      name: event.title,
      item: canonical,
    },
  ],
}
```

<br />

이 경우에도 화면의 빵 부스러기와 스키마의 제목, URL이 같은지 확인해야 합니다. 데이터가 두 곳에서 다르게 관리되면 언젠가는 어긋납니다.

<br />
<br />
<br />

## 검증은 개발 환경과 배포 환경에서 모두 한다

<br />

로컬에서 JSON 문법이 맞는 것만으로는 충분하지 않습니다. 실제 배포된 페이지에서 템플릿 값이 비어 있지 않은지, 검색엔진이 데이터를 읽을 수 있는지 확인해야 합니다.

<br />

검증 순서는 다음과 같습니다.

<br />

1. 개발 중에는 [Rich Results Test](https://search.google.com/test/rich-results)에서 URL 또는 HTML을 검사합니다.
2. 배포 뒤에는 Search Console의 리치 결과 보고서에서 오류와 경고를 확인합니다.
3. 값이 자주 바뀌는 행사나 상품은 종료, 품절, 취소 상태가 화면과 JSON-LD에 함께 반영되는지 확인합니다.

<br />

명령줄에서도 JSON-LD가 실제 HTML에 들어갔는지 빠르게 확인할 수 있습니다.

<br />

```bash
curl -sL https://example.com/events/spring-conference \
  | rg -n 'application/ld\+json|"@type":"Event"|"startDate"'
```

<br />

이 검사는 존재 여부만 알려 줍니다. 스키마의 의미와 지원되는 속성은 반드시 Rich Results Test와 Google의 해당 기능 문서에서 다시 확인해야 합니다.

<br />
<br />
<br />

## 정리

<br />

구조화 데이터는 검색엔진에게 페이지의 의미를 명확히 전달하는 방법입니다. 리치 결과를 약속하지는 않지만, 정확한 데이터 모델과 화면의 사실을 일관되게 유지하면 검색엔진과 사용자 모두가 페이지를 이해하기 쉬워집니다.

<br />

먼저 화면에 보이는 데이터를 정리하고, 그 데이터를 JSON-LD로 표현한 뒤, 배포된 URL에서 검증하세요. 마크업의 양보다 사실성, 정확성, 유지 보수 가능성이 우선입니다.

<br />

### 참고 자료

<br />

- [Google Search Central - 구조화 데이터 소개](https://developers.google.com/search/docs/appearance/structured-data/intro-structured-data)
- [Google Search Central - Event 구조화 데이터](https://developers.google.com/search/docs/appearance/structured-data/event)
- [Rich Results Test](https://search.google.com/test/rich-results)
