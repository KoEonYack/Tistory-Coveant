# Github CLI 시작하기 (1. 설치, 초기 설정)

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CIL_1/img/cover.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

<br />
<br />

# 시작하며

<br />


[Gitblog]([https://github.blog/2020-09-17-github-cli-1-0-is-now-available/](https://github.blog/2020-09-17-github-cli-1-0-is-now-available/)) 20년 9월 17일 Github CLI 1.0이 릴리즈 되었다는 소식이 올라왔습니다. 

잘 알려지지 않았지만 비공식적인 Github CLI 도구로 [hub]([https://hub.github.com/](https://hub.github.com/))가 있습니다. Github팀은 앞으로 gh를 발전해 갈것이고 hub는 유지보수를 줄일 것이라고 합니다. [참고. gh vs hub]([https://github.com/cli/cli/blob/trunk/docs/gh-vs-hub.md](https://github.com/cli/cli/blob/trunk/docs/gh-vs-hub.md))

<br />


본 글에서 Github CLI 1.0 정식 버전을 다운로드 받고 로그인 하는 방법을 살펴보겠습니다. 

<br />
<br />


# Github CLI 설치

<br />


## 리눅스에서 Github CLI 설치

```text
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-key C99B11DEB97541F0
sudo apt-add-repository https://cli.github.com/packages
sudo apt update
sudo apt install gh
```

<br />
<br />


## Mac에서 Github CLI  다운로드

<br />


Homebrew를 이용해서 설치 할 수 있습니다. 

```text
brew install gh
```

<br />
<br />


## Window에서 Github CLI 다운로드

<br />


scoop를 통해서 다운로드 받을 수 있지만 간단하게 msi를 통해서 설치해 보겠습니다. [release]([https://github.com/cli/cli/releases/](https://github.com/cli/cli/releases)) 페이지로 이동합니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CIL_1/img/down.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="" >
<br />

스크롤을 내리면 하단에 msi가 있습니다. 다운로드 받아서 설치해 줍니다. 


<br />
<br />
<br />


# Github CLI 버전 확인

<br />


```text
$ gh --version
gh version 1.0.0 (2020-09-16)
https://github.com/cli/cli/releases/tag/v1.0.0
```

1.0.0 버전이 설치 된 것을 확인 할 수 있습니다. 

```text
gh auth login
? What account do you want to log into?  [Use arrows to move, type to filter]
> GitHub.com
  GitHub Enterprise Server
```

설치한 gh에 초기 인증을 해야합니다. 터미널에 `gh auth login`을 입력합니다. 

```text
? How would you like to authenticate?  [Use arrows to move, type to filter]
> Login with a web browser
  Paste an authentication token
```

`Login with a web browser`를 선택합니다. 

```text
! First copy your one-time code: 31C1-C3**
- Press Enter to open github.com in your browser...
```

`one-time code`가 터미널에 생성됩니다.

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CIL_1/img/auth_1.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100" >
<br />

웹 브라우저에 터미널에 나온 one-time code를 입력합니다. 

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CIL_1/img/auth_4.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="60%" >
<br />

`Authorize github`을 클릭합니다. 

<br />
<img src="http://t1.daumcdn.net/thumb/R1024x0/?fname=https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Github/Github_CIL_1/img/auth_3.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="70%" >
<br />

위와 같은 화면이 나오면 성공한 것입니다. 지금 보고 있는 웹 창을 종료합니다. 

```text
✓ Authentication complete. Press Enter to continue...
```

터미널에서는 위와 같은 메시지가 보입니다. 엔터를 눌러주세요. 

```text
? Choose default git protocol
> HTTPS
  SSH
```

HTTPS를 선택해 줍니다.  이제 모든 설정을 끝냈습니다.

<br />
<br />


# 로그아웃(gh auth logout)

<br />


```text
$ gh auth status
github.com
  ✓ Logged in to github.com as Covenant (~/.config/gh/hosts.yml)
  ✓ Git operations for github.com configured to use https protocol.
```

`gh auth status` 명령어를 통해서 현재 gh에 로그인한 계정을 확인할 수 있습니다. 

```text
$ gh auth logout
```

위의 명령어를 통해서 로그아웃 할 수 있습니다.

<br />
<br />
