# Github CLI 시작하기 (gh issue)


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_3/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_3/img/issue_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />


명령어를 통해서 cli에 등록된 이슈 목록  [github.com/cli/cli/issues](https://github.com/cli/cli/issues) 을 볼 수 있습니다. cli를 클론한 디렉토리로 이동하여 아래 명령어를 입력합니다. 

```text
~/cli$ gh issue list
```

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_3/img/terminal.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

등록된 issue 목록을 볼 수 있습니다. 

<br />
<br />


# 등록된 이슈 보기

<br />


앞서 등록된 이슈 목록을 보았으니 어떤 이슈인지 읽어보겠습니다. 

```text
~/cli$ gh issue view {issue number}
```

issue number를 지정하면 해당 이슈를 보여줍니다. 저는 cli의 1777번 이슈를 보았습니다.


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_3/img/terminal_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

그러나 마크다운 문법이 적용된 텍스트를 텍스트 에디터로 읽기가 쉽지 않습니다. 그런 분들을 위해서 웹으로 열 수 있습니다.  

<br />

```text
~/cli$ gh issue view {issue number} --web
```

—web옵션을 사용하면 해당하는 번호의 이슈 웹 페이지를 보여줍니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_3/img/issue_2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

# 이슈 상태 분류해서 보기

```text
~/cli$ gh issue status

Relevant issues in cli/cli

Issues assigned to you
  There are no issues assigned to you

Issues mentioning you
  There are no issues mentioning you

Issues opened by you
  There are no issues opened by you
```

assigned, mentioning, opened 3가지 분류로 issue가 구분되어 나옵니다. 

<br />
<br />


# 새로운 이슈 등록하기

<br />



```text
~/repo$ gh issue create

Creating issue in owner/repo
? Title
? Body [(e) to launch nano, enter to skip]
http://github.com/owner/repo/issues/1
```

gh issue create명령어를 사용하여 Issue의 제목, 내용을 입력한다면  새로운 이슈를 등록할 수 있습니다. 

```text
~/repo$ gh issue create --title "Issue title" --body "Issue body"
http://github.com/owner/repo/issues/1
```

플래그를 지정하여 issue를 등록할 수 있습니다. 

```text
~/repo$ gh issue create --web
```

—web 플래그를 입력하면 아래와 같이 새로운 issue를 틍록하는 창이 열립니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_3/img/issue_4.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

<br />
<br />


# 등록된 이슈 닫기

<br />


```text
~/repo$ gh issue close 2
✔ Closed issue #2 (Issue title)
```

`gh issue reopen {closed issue number}` 입력한 issue 숫자가 open 상태라면 issue를 close 합니다. 

<br />
<br />

# 이슈 다시 열기

<br />


```text
~/repo$ gh issue reopen 2
✔ Reopened issue #2 (Issue Title)
```

`gh issue reopen {closed issue number}` 입력한 issue 숫자가 closed 상태라면 issue를 open 합니다.