# DFS BFS란? 백준 문제추천

> 그래프의 모든 노드를 방문 하는 알고리즘으로 DFS와 BFS가 있습니다. 어려운 코딩테스트를 통과하고 나면 만나게 될 기업 기술 면접의 단골 주제입니다. 본 알고리즘에 대해서 알아봅시다.


## [1] DFS(깊이 우선 탐색)란?

### 도입
<p style="text-align: center;">
<img src="./img/maze.jpg" align="center" width="800px" >
</p>
<center> 끝을 알 수 없는 미로를 빠져 나가는 방법은 무엇일까요? </center>
위의 사진은 영화 메이즈 러너의 미로입니다. 이렇게 복잡한 미로에 갇혔다면 여러분은 어떻게 미로를 빠져나올건가요? 아마 더 이상 막혀서 깊이 들어갈 수 없는 길을 만날 때까지 깊이 깊이 들어갈 것입니다. 한 모퉁이만 돌면 출구가 나올 수 있기에 한 번 깊이 들어가면 더 이상 들어갈 수 없을 때까지 나오지 않을 것입니다.
DFS는 미로 탐색과 같습니다. 미로에서 끝이 나올때까지 깊이 깊이 들억나느 것처럼 DFS 또한 더 깊이 들어갈 수 없을때까지 탐색합니다. 


### [1-2] 백문이 불여일견



### [1-1] 구현

__Step 1:__ 스택에 시작 노드를 넣습니다.

__Step 2:__ 스택이 비어있으면 실행을 멈추고 False를 반환합니다.  

__Step 3:__ 스택의 맨 위 노드가 찾고자 하는 노드라면 탐색을 종료하고 True를 반환합니다.  

__Step 4:__ Step 3에서 스택의 맨 위 노드가 찾고자 하는 노드가 아니라면 해당 노드를 POP합니다. 스택에 들어온 적이 없는 POP한 노드의 모든 이웃 노드를 찾아서 순서대로 스택에 넣습니다.  

__Step 5:__ Step 3로 돌아갑니다. 



``` python
DFS(G, u)
    u.visited = true
    for each v ∈ G.Adj[u]
        if v.visited == false
            DFS(G,v)
     
init() 
    For each u ∈ G
        u.visited = false
     For each u ∈ G
       DFS(G, u)
```


### [1-3] DFS 장점
- 현 경로상의 노드를 기억하기 때문에 적은 메모리를 사용합니다. 
- 찾으려는 노드가 깊은 단계에 있는 경우 BFS 보다 빠르게 찾을 수 있습니다. 

### [1-4] DFS 단점
- 해가 없는 경로를 탐색 할 경우 단계가 끝날 때까지 탐색합니다. 효율성을 높이기 위해서 미리 지정한 임의 깊이까지만 탐색하고 해를 발견하지 못하면 빠져나와 다른 경로를 탐색하는 방법을 사용합니다.
- DFS를 통해서 얻어진 해가 최단 경로라는 보장이 없습니다. DFS는 해에 도착하면 탐색을 종료하기 때문입니다.



## BFS(너비 우선 탐색)란?

### 사용 
- 웹 크롤링 - 동적으로  생성되는 무한한 인터넷 페이지를 구글이 크롤링 하기 위해서는 BFS를 합니다. 
- 네트워크 브로드캐스트
- 가비지 컬렉션 - [Article](https://www.quora.com/What-kind-of-graph-theory-is-used-in-automatic-garbage-collection-How-is-it-used)

자세한 설명은 [MIT BFS 강의 6:00](https://www.youtube.com/watch?v=s-CYnVz-uh4) 에서 확인 하실 수 있습니다.


## DFS vs BFS 탐색 차이
<p style="text-align: center;">
<img src="https://ww.namu.la/s/1fe9246903b78fae07577b243a0b22791e02cb39640d5cbaae10d9849343b4ea6f162a9a677a5892fbf7819abd4ef7221ebd3608849cfb66793411fb5e643951201c95c3f1912cad515f593c9c12fb7c63fe3def22e2c1a095fccf06f322a3cd" align="center" width="800px" >
</p>
<center> 트리에서 DFS, BFS 탐색 차이 </center>

- DFS는 스택(혹은 재귀), BFS 큐를 사용합니다.
- BFS는 재귀적으로 동작하지 않습니다.

But 문제를 푸는 입장에서는 다음과 같은 구분점이 필요합니다.
- 최단 거리 문제를 푼다면 BFS를 사용합니다.
- 이동할 때마다 가중치가 붙어서 이동한다거나, 이동 과정에서 여러 제약이 있을 경우, DFS로 구현하는 것이 좋습니다.


## 🎁 백준 문제 추천

DFS, BFS에 대해 알아보았으니 백준 문제를 추천해 드리겠습니다.
난이도는 주관적인 기준입니다 : )


| No. | 난이도 | 백준 링크 | 문제 풀이 |
|:--------:|:--------:|:--------:|:--------:|
| 1 | 🌕🌑🌑🌑🌑 | [](https://www.acmicpc.net/problem/) | []()  | 
| 2 | 🌕🌗🌑🌑🌑 | [](https://www.acmicpc.net/problem/) | []()  | 
| 3 | 🌕🌗🌑🌑🌑 | [](https://www.acmicpc.net/problem/) | []()  | 
| 4 | 🌕🌗🌑🌑🌑 | [](https://www.acmicpc.net/problem/) | []()  |
| 5 | 🌕🌗🌑🌑🌑 | [](https://www.acmicpc.net/problem/) | []()  | 


<br>

### 사족


----------------------

### 참고
- 이미지 출처 Figure . [MAZE RUNNER](https://www.youtube.com/watch?time_continue=47&v=EQWqsdOjvG8&feature=emb_title)
- 이미지 출처 Figure . [나무위키 BFS](https://namu.wiki/w/BFS)
- [Depth First Search (DFS): Concept, Implementation, Advantages, Disadvantages](https://www.brainkart.com/article/Depth-First-Search-(DFS)--Concept,-Implementation,-Advantages,-Disadvantages_8877/)
- [DFS algorithm - www.programiz.com](www.programiz.com)
- [[자료구조 알고리즘] Graph 검색 DFS, BFS 구현 in Java (by. 엔지니어대한민국)](https://www.youtube.com/watch?v=_hxFgg7TLZQ)
- [Python｜탐색 알고리즘 뿌시기 (1) DFS, BFS 의 개념과 구현](https://jeinalog.tistory.com/18)
- [HI-ARC 정기모임 #7 BFS (Slide Share)](https://www.slideshare.net/JaeyeolLee4/hiarc-7-bfs)









BFS vs DFS #
그렇다면 BFS(너비우선탐색)과 DFS(깊이 우선탐색)은 언제 쓰는것이 옳을까? BFS와 DFS모두 Time Complexity는 O(N)으로 같으나, Space Complexity는 DFS 의 경우 Tree의 최대 깊이( O(log(N)) ), BFS의 경우 큐에 쌓이는 용량 O(logN)만큼의 공간복잡도를 활용하므로 DFS가 좀더 유리하다. 하지만, DFS는 웹페이지, SNS의 연결망 같은 무한대에 가까운 깊이를 가지는 Tree(혹은 Graph)의 경우 정답이 되는 Path를 찾아가지 못하는 경우에는 정답을 찾을 수 있다는 보장을 할 수 없다. 이러한 경우에는 컴퓨터의 자원만 보장된다면, 얼마의 시간이 걸릴지는 모르지만 BFS를 이용해 정답을 찾을 수 있다.
http://www.incodom.kr/BFS