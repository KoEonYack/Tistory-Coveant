# Github CLI 시작하기 (gh pr)


# gh명령어로  Pull request 해보기


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_4/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

# PR 목록 보기

<br />


```text
# 현재 열려있는 PR 목록을 보여줍니다.
~/repo$ gh pr list

# 현재 디렉토리의 github PR 창을 브라우저에 띄웁니다.
~/repo$ gh pr list --web 
```

repo, issue와 같은 명령어와 마찬가지로 —web 플래그를 사용하면 브라우저가 열리며 Github PR 페이지로 이동합니다. 

<br />


다양한 PR 필터가 있습니다. 

```text
# 닫혀있는 PR 목록을 보여줍니다.
~/repo$ gh pr list --status closed

# bug 라벨이 붙은 PR 목록을 보여줍니다. 
~/repo$ gh pr list  --label "bug"

# 최대 목록에 보여줄 PR 갯수를 지정합ㄴ디ㅏ. (기본 30개 항목을 보여줍니다.)
~/repo$ gh pr list  --limit 5

# 클론한 디렉토리가 아니더라도 OWNER/REPO를 입력하면 PR목록을 가져올 수 있습니다. 
~$ gh pr list --repo cli/cli
```


<br />
<br />


# 등록한 PR 확인

<br />


```text
# 3번 PR을 텍스트 에디터로 엽니다. 
~/repo$ gh pr view 3

# 3번 PR을 웹 페이지로 엽니다. 
~/repo$ gh pr view 3 --web

# 자신의 PR을 보입니다. 
~/repo$ gh pr view
```

<br />
<br />


# PR 생성하기

<br />


`gh pr create`명령어를 통해서 현재 디렉토리에 클론한 저장소에 PR을 남길 수 있습니다. 

```text
# PR로 남길 제목과 내용을 차례대로 입력합니다. 
~/repo$ gh pr create 
? Title 새로운 PR
? Body  PR 내용이 들어갑니다. 

# flag를 지정하면 바로 PR이 생성됩니다. 
~/repo$ gh pr create --title "새로운 PR" --body "PR 내용이 들어갑니다."
http://github.com/owner/repo/pull/1

# PR을 작성하는 웹 페이지를 엽니다.  
~/repo$ gh pr create --web
```

<br />
<br />


# PR 닫기

<br />


```text
~/repo$ gh pr close {PR Number}
```

PR Number에 해당하는 PR을 닫습니다. 

PR 닫는 명령어에 `-d` or `--delete-branch` 옵션을 넣으면 PR을 닫으며, 로컬과 리모트 브랜치를 삭제합니다.

```text
~/repo$ gh pr close -d 
```

<br />
<br />

# PR 다시 열기

<br />


```text
~/repo$ gh pr reopen {Closed PR Number}
```

닫힌  PR 숫자를 입력하면 다시 PR을 엽니다. 

<br />
<br />


# PR 상태 확인

<br />


```text
~/repo$ gh pr status

Current branch
  #12 Remove the test feature [user:patch-2]
   - All checks failing - Review required

Created by you
  You have no open pull requests

Requesting a code review from you
  #13 Fix tests [branch]
  - 3/4 checks failing - Review required
  #15 New feature [branch]
   - Checks passing - Approved
```

현재 브랜치에서 PR, 내가 생성한 PR 그리고 내가 코드리뷰를 요청한 PR 세 가지 항목으로 보여줍니다.
