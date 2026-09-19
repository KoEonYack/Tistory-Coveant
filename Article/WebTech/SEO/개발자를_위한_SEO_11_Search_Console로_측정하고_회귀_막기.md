<!--

개발자를 위한 SEO 실전 가이드 11부 - Search Console로 측정하고 회귀 막기

-->

## 시작하며

<br />

SEO 작업은 배포로 끝나지 않습니다. sitemap을 추가하고 canonical을 고쳤다고 해서 바로 검색 성과가 바뀌지는 않으며, 나중에 템플릿을 수정하다가 메타데이터를 다시 깨뜨릴 수도 있습니다.

<br />

그래서 SEO는 한 번의 감사보다 관찰과 회귀 방지에 가깝습니다. 무엇을 바꿨는지 기록하고, 검색엔진이 실제로 읽었는지 확인하고, 다음 배포에서 같은 문제가 되돌아오지 않게 해야 합니다.

<br />

마지막 글에서는 Search Console에서 봐야 할 보고서와 배포 후 자동 점검의 최소 구성을 정리합니다.

<br />
<br />
<br />

## Search Console은 검색엔진 관점의 1차 데이터다

<br />

외부 SEO 도구는 편리하지만 Google 내부의 색인 상태나 검색 성과를 직접 알 수는 없습니다. Google 검색을 기준으로 판단할 때는 Search Console이 가장 먼저 볼 도구입니다.

<br />

처음 설정했다면 다음 순서로 시작하면 됩니다.

<br />

1. 소유권을 확인합니다.
2. sitemap을 제출하고, Google이 읽은 URL 수와 오류를 봅니다.
3. URL 검사 도구로 핵심 페이지의 색인 상태를 확인합니다.
4. 실적 보고서에서 검색어와 페이지별 노출, 클릭 추이를 봅니다.
5. 색인 문제와 리치 결과, Core Web Vitals 보고서를 정기적으로 확인합니다.

<br />

매일 모든 보고서를 볼 필요는 없습니다. Google도 새 문제가 생기면 이메일 알림을 보내며, 보통은 한 달에 한 번 또는 큰 변경 뒤에 확인하는 방식을 안내합니다. 다만 대량 URL 변경, 마이그레이션, 템플릿 배포 직후에는 더 짧은 간격으로 봐야 합니다.

<br />
<br />
<br />

## URL 검사 도구는 한 페이지의 사실을 확인한다

<br />

트래픽이 줄었다고 해서 사이트 전체 문제라고 단정하지 마세요. 먼저 대표 페이지 하나를 골라 URL 검사 도구에서 확인합니다.

<br />

```text
확인할 질문

- 이 URL은 Google 색인에 있는가
- 색인되지 않았다면 제외 사유는 무엇인가
- Google이 선택한 canonical은 무엇인가
- robots 또는 noindex 때문에 막히지 않았는가
- 마지막 크롤링은 언제였는가
- 라이브 테스트에서 서버가 정상 응답하는가
```

<br />

URL 검사 결과와 브라우저 화면이 다를 수 있습니다. 예를 들어 브라우저에는 로그인 쿠키가 있어 보이는 내용이 Google에는 보이지 않을 수 있습니다. 색인 문제는 항상 공개 URL을 기준으로 확인하세요.

<br />

색인 요청은 새 콘텐츠나 중요한 수정 뒤에 사용할 수 있지만, 즉시 색인이나 순위를 보장하는 버튼은 아닙니다. 페이지가 발견 가능하고, 크롤링 가능하고, 색인할 만한 상태인지가 먼저입니다.

<br />
<br />
<br />

## 변화는 하나의 지표로 결론 내리지 않는다

<br />

실적 보고서의 클릭, 노출, 평균 게재순위는 함께 읽어야 합니다.

<br />

```text
노출 증가 + 클릭 증가
더 많은 검색에 보이고 실제 방문도 늘었을 수 있다.

노출 증가 + 클릭 감소
새 검색어에 많이 노출되었거나 제목·설명과 검색 의도가 어긋났을 수 있다.

특정 URL만 급감
리다이렉트, canonical, noindex, 서버 응답을 먼저 확인한다.

여러 URL이 함께 급감
배포, 사이트 접근성, robots.txt, 수동 조치, 계절성 등을 넓게 확인한다.
```

<br />

평균 게재순위는 여러 검색어와 위치를 평균 낸 값입니다. 한두 자리의 변화만 보고 작업 성공이나 실패를 판단하기보다, 같은 유형의 페이지와 충분한 기간을 비교하세요.

<br />

변경 기록도 같이 남겨야 합니다. "9월 17일 sitemap 생성 규칙 변경", "10월 2일 목록 페이지 서버 렌더링 적용"처럼 날짜, 대상 URL, 변경 내용, 예상 효과를 기록하면 나중에 데이터와 연결하기 쉽습니다.

<br />
<br />
<br />

## 배포마다 최소한의 SEO 검사를 자동화한다

<br />

모든 SEO 문제를 테스트로 막을 수는 없지만, title 누락, 잘못된 canonical, `noindex` 유입, sitemap 오류처럼 반복되는 사고는 자동 검사로 줄일 수 있습니다.

<br />

```bash
#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${1:?usage: $0 https://example.com}"
URL="$BASE_URL/posts/seo-basics"

html="$(curl -fsSL "$URL")"

rg -q '<title>[^<].*</title>' <<<"$html"
rg -q 'rel="canonical" href="https://example.com/posts/seo-basics"' <<<"$html"

if rg -q 'name="robots" content="[^"]*noindex' <<<"$html"; then
  echo "Unexpected noindex: $URL" >&2
  exit 1
fi

curl -fsSL "$BASE_URL/sitemap.xml" | rg -q '<urlset|<sitemapindex'
curl -fsSL "$BASE_URL/robots.txt" | rg -q 'Sitemap:'

echo "SEO smoke check passed"
```

<br />

이 스크립트는 예시입니다. 실제 프로젝트에서는 대표 URL 목록을 설정 파일로 분리하고, 도메인과 경로를 하드코딩하지 않도록 바꾸는 편이 좋습니다. 인증이 필요한 스테이징 환경이라면 공개 페이지를 확인할 별도 방법도 필요합니다.

<br />

스모크 테스트가 통과해도 콘텐츠 품질이나 검색 의도까지 증명하지는 못합니다. 대신 사람이 실수하기 쉬운 기술적 회귀를 초기에 발견하는 역할을 합니다.

<br />
<br />
<br />

## 우선순위는 의존 관계를 따라 정한다

<br />

SEO 항목은 모두 같은 순서로 처리할 수 없습니다. 색인되지 않는 페이지에 구조화 데이터를 추가해도 효과를 보기 어렵고, 빈 페이지의 LCP만 개선해도 독자에게 줄 가치는 늘지 않습니다.

<br />

```text
1. 접근과 발견
상태 코드, robots.txt, sitemap, 내부 링크

2. 중복과 대표 URL
canonical, 리다이렉트, noindex

3. 페이지 이해
제목, 본문, 메타데이터, 렌더링, 구조화 데이터

4. 경험과 가치
속도, 콘텐츠 품질, 사이트 구조

5. 측정과 회귀 방지
Search Console, 변경 기록, 배포 검사
```

<br />

어떤 항목이 가장 급한지는 사이트 상황에 따라 달라집니다. 하지만 이 순서를 기준으로 보면 "지금 무엇을 고쳐야 하는가"를 설명하기 쉬워집니다.

<br />
<br />
<br />

## 정리

<br />

SEO는 한 번의 점검표를 채우는 일이 아니라, 검색엔진이 사이트를 읽는 방식과 사용자가 사이트를 이용하는 방식을 지속적으로 확인하는 운영입니다.

<br />

Search Console로 색인과 성과를 확인하고, 변경 기록으로 원인과 결과를 연결하세요. 배포 단계에서는 대표 URL의 title, canonical, robots, sitemap을 자동으로 검사해 같은 문제가 다시 생기지 않게 만드는 것이 좋습니다.

<br />

이 시리즈의 핵심도 같습니다. 검색 결과를 위한 별도 장식을 더하기보다, 발견 가능하고, 이해 가능하며, 실제로 도움이 되는 웹페이지를 꾸준히 만드는 일입니다.

<br />

### 참고 자료

<br />

- [Google Search Central - Search Console 시작하기](https://developers.google.com/search/docs/monitor-debug/search-console-start)
- [Google Search Central - URL 검사 도구](https://support.google.com/webmasters/answer/9012289)
- [Google Search Central - Search Console 실적 보고서](https://support.google.com/webmasters/answer/7576553)
