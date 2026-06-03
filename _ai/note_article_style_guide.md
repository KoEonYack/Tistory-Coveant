# Tistory-Coveant Note 글쓰기 스타일 가이드

이 문서는 `Article/Note`에 있는 기존 글의 형식과 문체를 기준으로, 다른 세션에서도 같은 스타일의 글을 요청할 수 있도록 정리한 재사용 지침입니다.

## 요청 프롬프트

아래 내용을 새 세션에서 그대로 붙여 넣어 사용합니다.

````markdown
이 저장소의 `Article/Note` 글 스타일에 맞춰 글을 작성하거나 다듬어줘.

작성 규칙:

1. 글 맨 위에는 실제 제목을 HTML 주석으로 둔다.

   ```markdown
   <!--

   글 제목

   -->
   ```

2. 본문은 티스토리 업로드를 고려해 `<br />`로 여백을 만든다.
   - 큰 섹션 사이에는 `<br />` 세 줄을 둔다.
   - `##`, `###` 제목 바로 아래에는 `<br />`를 한 줄 둔 뒤 본문을 시작한다.
   - 문단과 문단 사이에는 가능한 한 `<br />`를 한 줄 둔다.
   - 하나의 큰 주제가 끝나고 다음 `##` 섹션으로 넘어갈 때는 `<br />`를 세 줄 둔다.
   - `###` 하위 섹션 사이도 시각적으로 멀어야 하면 `<br />`를 세 줄 둔다.
   - 이미지, 표, 코드 블록 앞뒤에는 `<br />`를 둔다.
   - 코드 블록 앞에는 설명 문단 뒤에 한 줄을 비우고 코드 블록을 배치하되, 코드 블록 뒤에는 `<br />`를 둔다.
   - HTML 표 앞뒤에는 `<br />`를 둔다.
   - 본문을 다듬은 뒤 `<br />` 배치가 섹션마다 들쭉날쭉하지 않은지 마지막에 한 번 더 확인한다.

3. 글은 기술 설명만 나열하지 말고, 먼저 "왜 이 글을 쓰는지", "어떤 상황에서 마주쳤는지"를 짧게 연다.
   - 추천 도입 섹션명: `## 시작하며`
   - 기술 글이라도 독자가 겪을 만한 상황을 먼저 말한다.
   - 예: "Spring Boot 3.0으로 올리면서 가장 먼저 보이는 변경점은 패키지 이름입니다."

4. 제목 체계는 단순하게 유지한다.
   - 글 전체 제목은 주석으로만 둔다.
   - 주요 흐름은 `# 큰 섹션` 또는 `## 번호. 섹션명` 중 기존 글에 맞는 하나를 선택한다.
   - 하위 항목은 `##`, `###`를 사용하되 깊게 중첩하지 않는다.

5. 문체는 개인 블로그 회고/정리 톤으로 쓴다.
   - 과하게 딱딱한 매뉴얼 문체보다, 직접 겪고 정리해 주는 느낌을 둔다.
   - "정리해봅니다", "생각보다", "실무에서는", "주의해야 합니다" 같은 자연스러운 표현을 사용할 수 있다.
   - 독자를 낮추어 보지 말고, 함께 배경을 따라가는 톤으로 쓴다.

6. 기술 글은 역사, 배경, 실무 영향 순서로 전개한다.
   - 먼저 현상: 지금 무엇이 바뀌었는가
   - 다음 배경: 왜 이런 일이 생겼는가
   - 다음 영향: 실무에서 무엇을 조심해야 하는가
   - 마지막 정리: 핵심 메시지를 짧게 다시 말한다.

7. 이미지가 있는 경우 아래 HTML 형식을 사용한다.
   - 티스토리 업로드용 글에서는 `./img/cover.png` 같은 로컬 상대경로를 그대로 두지 않는다.
   - 저장소 기준 경로를 GitHub raw URL로 바꾼다.
   - 변환 규칙: `./img/cover.png` → `https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/{현재_문서가_있는_Article_하위_디렉토리}/img/cover.png`
   - 한글 디렉토리명이나 파일명은 URL 인코딩된 경로를 사용한다.
   - 예: `Article/AI/싱글에이전트_멀티에이전트/img/cover.png`는 아래처럼 작성한다.

   ```html
   <img src="https://raw.githubusercontent.com/KoEonYack/Tistory-Coveant/refs/heads/master/Article/AI/%EC%8B%B1%EA%B8%80%EC%97%90%EC%9D%B4%EC%A0%84%ED%8A%B8_%EB%A9%80%ED%8B%B0%EC%97%90%EC%9D%B4%EC%A0%84%ED%8A%B8/img/cover.png" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
   ```

   ```html
   <br />
   <img src="이미지_URL" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
   <br />
   <center>
   이미지 설명
   </center>
   <br />
   ```

8. 코드 블록은 설명을 먼저 쓰고, 바로 아래에 배치한다.
   - 코드만 던지지 않는다.
   - 전후 문단에서 왜 이 코드가 필요한지 설명한다.
   - 명령어, 흐름도, 예시 대화, 요약 문장, 강조 박스처럼 본문과 구분해서 보여주고 싶은 내용은 `text` 코드 블록을 사용한다.
   - 블로그에서 `>` 인용문은 본문과 조화롭게 보이지 않을 수 있으므로 사용하지 않는다.
   - 기존 글에 `>` 인용문이 있으면 의미를 유지하되 아래처럼 `text` 코드 블록으로 바꾼다.

   ```markdown
   ```text
   참고로, 아래 배경도 함께 보면 Vector DB를 이해하기 쉽습니다.

   첫 번째는 ...
   두 번째는 ...
   ```
   ```

9. 표는 비교가 필요할 때만 사용한다.
   - 표 앞에는 "정리하면 다음과 같습니다." 같은 연결 문장을 둔다.
   - 표 뒤에는 실무적으로 중요한 해석을 한 문단으로 덧붙인다.
   - 기본 마크다운 표는 티스토리에서 투박하게 보일 수 있으므로, 중요한 비교표는 HTML `<table>`과 인라인 CSS로 작성한다.
   - 표는 `border-collapse:collapse`, `border-top:2px solid #333`, 옅은 헤더 배경(`#f7f8fa`), 얇은 행 구분선(`#eeeeee`)을 기본으로 한다.
   - 2열 비교표는 가운데 세로 구분선이 없으면 열 구분이 어색하므로 첫 번째 열 오른쪽에 `border-right:1px solid #e2e2e2`를 넣는다.
   - 3열 이상 표는 마지막 열을 제외한 열 오른쪽에만 `border-right`를 넣고, 마지막 열 오른쪽에는 선을 넣지 않는다.
   - `javax.*`, `jakarta.*`처럼 코드 값은 `<code>`에 옅은 배경(`#eef3ea`), 작은 padding, `border-radius:6px`, `font-size:14px`를 적용해 배지처럼 보이게 한다.
   - 예시는 아래 형식을 따른다.

   ```html
   <table style="width:100%; border-collapse:collapse; margin:0 auto; font-size:15px; line-height:1.6; border-top:2px solid #333;">
     <thead>
       <tr>
         <th style="width:50%; padding:12px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">변경 전</th>
         <th style="width:50%; padding:12px 10px; border-bottom:1px solid #d9d9d9; background:#f7f8fa; text-align:left; font-weight:700;">변경 후</th>
       </tr>
     </thead>
     <tbody>
       <tr>
         <td style="padding:11px 10px; border-right:1px solid #e2e2e2; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">javax.persistence.*</code></td>
         <td style="padding:11px 10px; border-bottom:1px solid #eeeeee;"><code style="background:#eef3ea; color:#222; padding:4px 8px; border-radius:6px; font-size:14px;">jakarta.persistence.*</code></td>
       </tr>
     </tbody>
   </table>
   ```

10. 다음 요소는 피한다.
    - `---` 수평선으로 섹션을 나누기
    - GitHub 전용 admonition 문법(`[!TIP]`) 남발
    - `>` 마크다운 인용문 사용
    - 마케팅 문구처럼 과한 제목
    - AI가 쓴 것처럼 일반론만 반복하는 문장
    - 근거 없는 단정, 최신 버전 추측

11. 글을 수정할 때는 아래 형식 검사를 함께 수행한다.
    - 글 전체에서 `>`로 시작하는 인용문이 남아 있지 않은지 확인한다.
    - `---` 수평선이 남아 있지 않은지 확인한다.
    - `![이미지](./img/...)`, `![[...]]` 같은 로컬/옵시디언 이미지 문법이 남아 있지 않은지 확인한다.
    - `##`, `###` 제목 아래에 `<br />`가 있는지 확인한다.
    - 큰 섹션 사이가 `<br />` 세 줄로 구분되어 있는지 확인한다.
    - 코드 블록, 이미지, 표 뒤에 `<br />`가 있는지 확인한다.
    - 코드 블록의 시작과 끝 개수가 맞는지 확인한다.

12. 글의 마지막은 `정리` 또는 `마치며`로 닫는다.
    - 새로 알게 된 배경
    - 실무에서 조심할 점
    - 앞으로 글을 읽는 사람이 가져가면 좋을 핵심을 2~4문단으로 정리한다.

검토 기준:

- 기존 `Article/Note` 글처럼 티스토리에 바로 붙여 넣어도 어색하지 않은가?
- 도입부에 개인 블로그다운 문제의식이 있는가?
- 기술적 배경과 실무 영향이 자연스럽게 연결되는가?
- `<br />`, 이미지 캡션, 섹션 간 여백 형식이 일관되게 유지되는가?
- 제목 아래, 문단 사이, 큰 섹션 사이의 `<br />` 밀도가 글 전체에서 균일한가?
- 불필요한 수평선, `>` 인용문, 과한 문서형 말투, AI식 일반론이 제거되었는가?
````

## 스타일 요약

- 제목은 주석으로 숨기고, 본문은 바로 이미지나 `시작하며`로 시작한다.
- 문단 사이의 여백을 `<br />`로 명시한다.
- `##`, `###` 제목 아래에는 `<br />`를 두고, 큰 섹션 사이에는 `<br />` 세 줄을 사용해 여백을 통일한다.
- 독자에게 바로 결론을 던지기보다, 글쓴이가 그 주제를 왜 정리하게 되었는지 먼저 설명한다.
- 경험담이 있는 글은 시간 순서로, 기술 정리 글은 배경에서 실무 영향으로 이동한다.
- 이미지는 가운데 정렬 HTML 태그와 `<center>` 캡션을 사용한다.
- 로컬 이미지 경로는 티스토리에서 깨지지 않도록 GitHub raw URL로 변환한다.
- 비교표는 HTML 테이블과 인라인 CSS를 사용하고, 열 사이에는 얇은 세로 구분선을 둔다.
- `>` 인용문은 사용하지 않고, 본문과 구분하고 싶은 설명은 `text` 코드 블록으로 감싼다.
- 기술 글에서도 "단순히 A가 B로 바뀌었다"에서 끝내지 않고, 그 변경이 왜 생겼고 어떤 실수를 만들 수 있는지까지 다룬다.
