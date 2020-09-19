# Github CLI 시작하기 (gh repo)

<br />
<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_2/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

# gh repo clone

<br />


git 명령어를 이용해서 클론하려고 하면 다음 명령어를 사용해서 클론을 했습니다. 

<br />
<br />


```text
$ git clone https://github.com/cli/cli.git
```

gh 명령어를 통해서 [cil/cil]([https://github.com/cli/cli](https://github.com/cli/cli))을 클론해 보겠습니다. 다음과 같이 클론할 수 있습니다. 

```text
gh repo clone OWNER/REPO
```

[cil/cil]([https://github.com/cli/cli](https://github.com/cli/cli))을 클론해 보겠습니다.

```text
$ gh repo clone cli/cli
Cloning into 'cli'...
```

https:// 를 통한 클론도 지원합니다. 

```text
$ gh repo clone https://github.com/cli/cli
```

<br />
<br />


# gh repo view

<br />


gh repo view는 README.md를 보여주는 명령어입니다. 

방금 clone 한 cli 디렉토리로 이동해서 아래 명령어를 입력합니다. 

```text
 ~/cli$ gh repo view

cli/cli
GitHub’s official command line tool

   GitHub CLI

   gh  is GitHub on the command line. It brings pull requests, issues, and
  other GitHub concepts to the terminal next to where you are already working
```

cli/cli의 README.md를 기본 텍스트 편집기(vim or nano)로 보입니다. 

<br />


gh repo view OWNER/REPO 명령어는 클론한 저장소가 아니더라도 README.md를 보여줍니다. 개발자 행사를 보여주는 [Github. Dev-Event]([https://github.com/brave-people/Dev-Event](https://github.com/brave-people/Dev-Event))의 README.md를 다음의 명령어를 입력하면 터미널에서 볼 수 있습니다. 

<br />

```text
$ gh repo view brave-people/Dev-Event

brave-people/Dev-Event
🎉🎈 개발자 대회, 컨퍼런스, 모임 소식을 알려드립니다. (PR환영합니다.)
```

`--web`옵션을 붙이면 해당 저장소로 Github브라우저가 열립니다. 

```text
$ gh repo view brave-people/Dev-Event --web
```

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_2/img/github.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />
<br />

# gh repo create

<br />


gh repo create를 이용하면 저장소를 생성할 수 있습니다. 

```text
# 현재 디렉토리 이름으로 저장소를 생성합니다. 
$ gh repo create

# my-project라는 저장소를 생성합니다. 
$ gh repo create my-project

# cli그룹에 my-project를 생성합니다. 
$ gh repo create cli/my-project
```

<br />


두 번째 명령어를 예로 test-cli 저장소를 만들어 보곘습니다. 

```text
$ gh repo create test-cli

? Visibility  [Use arrows to move, type to filter]
> Public
  Private
  Internal

? This will create 'test-cli' in your current directory. Continue?  (Y/n)
y
```

상하 화살표와 엔터를 눌러서 Visibility 설정, 그리고 현재 디렉토리에 새 repo를 생성할 것인지 선택할 수 있습니다.

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_2/img/repo.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />


Github에 터미널에 입력한 test-cli 저장소가 생긴 것을 볼 수 있습니다. 

<br />


CLI를 사용하지 않았다면 Github에서 저장소를 만들고 git init, remote 명령어를 이용해서 설정해야 했습니다. 하지만 CLI를 입력하면 자동으로 이 모든 것을 해줍니다.


<br />
<br />

# gh repo fork

<br />


fork를 하고자 하는 clone 저장소로 이동합니다. 

```text
~/cli$ gh repo fork 

- Forking cli/cli...
✓ Created fork koeonyack/cli
? Would you like to add a remote for the fork? Yes
✓ Renamed origin remote to upstream
✓ Added remote origin
```

로컬에 fork하고자 하는 저장소가 없더라도 다음 명령어를 통해서 fork할 수 있습니다. [Github. Dev-Event]([https://github.com/brave-people/Dev-Event](https://github.com/brave-people/Dev-Event)) 를 fork 해보겠습니다. 

```text
~$ gh repo fork [brave-people/Dev-Event](https://github.com/brave-people/Dev-Event)
```

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CLI_2/img/repo3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />


Github의 저장소에 성공적으로 fork 한 것을 볼 수 있습니다. 

<br />
<br />
