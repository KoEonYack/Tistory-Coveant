# 🏁 거침없는 코딩테스트 DFS BFS 

<br>


- Last Updated: 20. 04. 05.
- 🏅:  Solved.ac 플래티넘 · 🥇: Solved.ac 골드 · 🥈 Solved.ac 실버 · 🥉 Solved.ac 브론즈

<br>
<br>


## 시작하며. 거침없는 시리즈

<br>

코딩테스트를 빠르게 준비하는 방법은 하나의 주제에 대해서 관련 문제를 쭉 풀어보는 것입니다. `거침없는` 시리즈에서 문제들을 단계별로 풀면서 실전 난이도까지 실력을 쭉 끌어 올려보겠습니다.

DFS, BFS에 대해서 알고 있다는 전제하에 문제풀이가 병행돼야 합니다. DFS, BFS에 대해서 잘 모른다면 `거침없이` 문제를 풀 것이 아니라 제 글, [DFS BFS란? 백준 문제추천](https://covenant.tistory.com/132)을 보고 문제를 풀기 바랍니다.


<br>
<br>


## 백준1260번. DFS와 BFS

- 문제 링크: https://www.acmicpc.net/problem/1260
- 난이도: 🥈 1티어
- DFS, BFS 알고리즘의 Hello World 수준의 문제입니다.
- 본 문제를 통해서 DFS, BFS를 정확하게 구현할 수 있어야 합니다.

<div data-ke-type="moreLess" data-text-more="백준 1260번(DFS, BFS) 답 확인" data-text-less="백준 1260번(DFS, BFS)답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 
from collections import deque
import sys

sys.setrecursionlimit(1000000)

def DFS(v):
    print(str(v), end=" ")
    if v == M:
        return
    else:
        for i in range(1, N+1):
            if MAP[v][i] == 1 and check[i] is False:
                check[i] = True
                DFS(i)

def BFS(v):
    Q = deque([])
    Q.append(v)

    while Q:
        x = Q.popleft()
        if check_BFS[x] is False:
            check_BFS[x] = True
            print(x, end=" ")
            for i in range(1, N+1):
                if MAP[x][i] == 1 and check_BFS[i] is False:
                    Q.append(i)
 
N, M, V = map(int, input().split())  # 정점, 간선, 탐색 시작 vertex
MAP = [[0] * (N+1) for _ in range(N+1)]
check = [False] * (N+1)
check_BFS = [False] * (N+1)

for i in range(M):
    start, end = map(int, input().split())
    MAP[start][end] = 1
    MAP[end][start] = 1

check[V] = True
DFS(V)
print()
BFS(V)
```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>

<br>
<br>

## 백준 1303번. 전투 

- 문제 링크: https://www.acmicpc.net/problem/1303
- 난이도: 🥈 1티어
- 기본적인 BFS 문제이며, 입력값이 공백 없는 문자열로 입력값만 잘 처리하면 어렵지 않은 문제입니다.
- 상하좌우 탐색의 기본 코드를 확인하세요 (dx, dy 부분)

<div data-ke-type="moreLess" data-text-more="백준 1303번 전투(BFS) 답 확인" data-text-less="백준 1303번 전투(BFS) 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque

def BFS():
    q.append((i, j))
    check[i][j] = 1
    t = 1
    while q:
        x, y = q.popleft()
        for k in range(4):
            nx, ny = x + dx[k], y + dy[k]
            if 0 <= nx < M and 0 <= ny < N and MAP[nx][ny] == MAP[x][y] and check[nx][ny] == 0:
                check[nx][ny] = 1
                q.append((nx, ny))
                t += 1
    return t

# N: 가로 M : 세로
N, M = map(int, input().split())
MAP = [list(input()) for _ in range(M)]
check = [[0] * N for _ in range(M)]

dx,dy=[1,-1,0,0],[0,0,1,-1]
q = deque()
B, W = 0, 0

for i in range(M):
    for j in range(N):
        if check[i][j] == 0:
            ans = BFS()
            if MAP[i][j] == 'W': W += ans ** 2
            else: B += ans ** 2

print(W, B)

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>

</div>
</div>


<br>
<br>


## 백준 2178번. 미로 탐색


- 문제 링크: https://www.acmicpc.net/problem/2178
- 난이도: 🥈 1티어
- BFS에서 상하좌우 탐색에 익숙하지 않다면 보충으로 풀어볼 만한 문제입니다. 
- 노드에 한 번 방문했는지 기록하는 check 배열을 True, False가 아닌 정수를 두었으며, 방문 횟수를 기록하였습니다. 이를 통해 좀 더 간결한 코드 작성이 가능합니다.
- MAP = [[int(i) for i in list(input())] for _ in range(N)] 이 코드가 불편하다면 MAP=[[*map(int,[*input()])] for _ in range(n)] 라고 작성해도 됩니다.

<div data-ke-type="moreLess" data-text-more="백준 2178번 미로 탐색(BFS) 답" data-text-less="백준 2178번 미로 탐색(BFS) 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 
from collections import deque

N, M = map(int, input().split()) # N: 세로, M: 가로
MAP = [[int(i) for i in list(input())] for _ in range(N)]
check = [[0] * M for _ in range(N)]
dx = [0, 1, 0, -1]; dy = [-1, 0, 1, 0]

q = deque()
q.append([0, 0])
check[0][0] = 1
while q:
    x, y = q.popleft()
    for i in range(4):
        nx, ny = x + dx[i], y + dy[i]
        if 0 <= nx < N and 0 <= ny < M and check[nx][ny] == 0 and MAP[nx][ny] == 1:
            q.append([nx, ny])
            check[nx][ny] = check[x][y] + 1

print(check[-1][-1])

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>


<br>
<br>

## 백준 1743번. 음식물 피하기

- 문제 링크: https://www.acmicpc.net/problem/1743
- 난이도: 🥈 1티어
- 입력값이 인접행렬로 들어오는 것이 아닌 좌표로 주어집니다. 저는 인접행렬로 풀었지만 인접 리스트로 풀어도 좋습니다.  

<div data-ke-type="moreLess" data-text-more="백준 1743번 음식물 피하기(BFS) 답 확인" data-text-less="백준 1743번 음식물 피하기(BFS) 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque
dx, dy = [0, 0, 1, -1], [1, -1, 0, 0]

def BFS():
    q.append((i, j))
    t = 1
    check[i][j] = 1
    while q:
        x, y = q.popleft()
        for k in range(4):
            nx, ny = x + dx[k], y + dy[k]
            if 0 <= nx < N and 0 <= ny < M and MAP[nx][ny] == 1 and check[nx][ny] == 0:
                check[nx][ny] = 1
                q.append((nx, ny))
                t += 1
    return t

N, M, K = map(int, input().split()) # N: 세로, M: 가로 K: 음식물 갯수
MAP = [[0] * M for _ in range(N)]
check = [[0] * M for _ in range(N)]
for _ in range(K):
    x, y = map(int, input().split())
    MAP[x-1][y-1] = 1

q = deque()

ans = 0
for i in range(N):
    for j in range(M):
        if MAP[i][j] == 1 and check[i][j] == 0:
            res = BFS()
            if res > ans: ans = res

print(ans)

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>

<br>
<br>


## 백준 2606번. 바이러스  

- 문제 링크: https://www.acmicpc.net/problem/2606
- 난이도: 🥈 2티어
- 주로 인접 행렬로 문제를 해결하는데, 인접 리스트로 문제를 해결하였습니다.
- Union Find로 풀 수 있습니다.

<div data-ke-type="moreLess" data-text-more="백준 2606번 바이러스 답 확인" data-text-less="백준 2606번 바이러스 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque

N = int(input())
check = [0] * (N+1)
Computer = [[] for _ in range(N+1)]

for i in range(int(input())):
    a, b = map(int, input().split())
    Computer[a].append(b)
    Computer[b].append(a)

check[1] = 1
q = deque([1])
while q:
    x = q.popleft()
    for i in range(len(Computer[x])):
        if check[Computer[x][i]] == 0:
            q.append(Computer[x][i])
            check[Computer[x][i]] = 1

print(sum(check) - 1)

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>

<br>
<br>
 

## 백준 16953번. A → B

- 문제 링크: https://www.acmicpc.net/problem/16953
- 난이도: 🥇 5티어
- `exit()` 를 사용하면 python 3로 제출해야하며 pypy3로 제출하려면 `sys.exit()` 로 제출하면 됩니다. 

<div data-ke-type="moreLess" data-text-more="백준 16953번 A → B 답 확인" data-text-less="백준 16953번 A → B 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque

A, B = map(int, input().split())

q = deque()
q.append((A, 1))

while q:
    x, t = q.popleft()
    if x == B:
        print(t)
        exit()

    if x*2 <= B:
        q.append((x*2, t+1))
    x = int(str(x) + "1")
    if x <= B:
        q.append((x, t+1))

print(-1)

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>


<br>
<br>


## 백준 12851번. 숨바꼭질 2

- 문제 링크: https://www.acmicpc.net/problem/12851
- 난이도: 🥇 5티어
- 처음 문제를 보면 아이디어가 생각이 나지 않을 수 있습니다..!

<div data-ke-type="moreLess" data-text-more="백준 12851번 숨바꼭질 2 답 확인" data-text-less="백준 12851번 숨바꼭질 2 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque

N, K = map(int, input().split())
MAX_SIZE = 100001
MAP = [[MAX_SIZE, 0] for _ in range(MAX_SIZE)]

q = deque()
q.append(N)
MAP[N][0] = 0
MAP[N][1] = 1

while q:
    x = q.popleft()
    for nx in [x*2, x+1, x-1]:
        if 0 <= nx < MAX_SIZE:
            if MAP[nx][0] == MAX_SIZE:
                q.append(nx)
                MAP[nx][0] = MAP[x][0] + 1
                MAP[nx][1] = MAP[x][1]
            elif MAP[nx][0] == MAP[x][0]:
                MAP[nx][1] += MAP[x][1]

print(MAP[K][0])
print(MAP[K][1])

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>


<br>
<br>


## 백준 13549번. 숨바꼭질 3

- 문제 링크: https://www.acmicpc.net/problem/13549
- 난이도: 🥇 5티어
- 백준 12851번. 숨바꼭질 2를 해결하지 못한 아쉬운 분들을 위하여 준비했습니다.

<div data-ke-type="moreLess" data-text-more="백준 13549번 숨바꼭질 3 답 확인" data-text-less="백준 13549번 숨바꼭질 3 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque

q = deque()
MAX_SIZE = 100000
check = [False] * (MAX_SIZE+1)
dist = [-1] * (MAX_SIZE + 1)

N, K = map(int, input().split())
q.append(N)
dist[N] = 0
check[N] = True
while q:
    curr = q.popleft()
    if curr == K:
        break

    if curr*2 <= MAX_SIZE and check[curr*2] is False:
        q.appendleft(curr*2)
        dist[curr*2] = dist[curr]
        check[curr*2] = True

    for next in (curr-1, curr+1):
        if 0 <= next <= MAX_SIZE and check[next] is False:
            q.append(next)
            dist[next] = dist[curr] + 1
            check[next] = True

print(dist[K])

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>


<br>
<br>



## 백준 13913번. 숨바꼭질 4

- 문제 링크: https://www.acmicpc.net/problem/13913
- 난이도: 🥇 4티어

<div data-ke-type="moreLess" data-text-more="백준 13913번 숨바꼭질 4 답 확인" data-text-less="백준 13913번 숨바꼭질 4 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque
import sys

sys.setrecursionlimit(1000000000)
MAX_SIZE = 100000
check = [False] * (MAX_SIZE + 1)
dist = [0] * (MAX_SIZE + 1)
move_to = [0] * (MAX_SIZE + 1)
q = deque([])

N, K = map(int, input().split())
q.append(N)
check[N] = True
dist[N] = 0

while q:
    now = q.popleft()
    if now == K:
        break
    for curr in (now-1, now+1, now*2):
        if 0 <= curr <= MAX_SIZE and check[curr] is False:
            q.append(curr)
            check[curr] = True
            dist[curr] = dist[now] + 1
            move_to[curr] = now


def print_location(n, m):
    if n != m:
        print_location(n, move_to[m])
    print(m, end=" ")

print(dist[K])
print_location(N, K)

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>

<br>
<br>


## 백준 14226번. 이모티콘

- 문제 링크: https://www.acmicpc.net/problem/14226
- 난이도: 🥇 5티어

<div data-ke-type="moreLess" data-text-more="백준 14226번 이모티콘 답 확인" data-text-less="백준 14226번 이모티콘 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 
from collections import deque

MAX_SIZE = 1001
q = deque()
dist = [[-1] * MAX_SIZE for _ in range(MAX_SIZE)]
q.append((1, 0))
dist[1][0] = 0
N = int(input())

while q:
    s, c = q.popleft()
    if dist[s][s] == -1:
        dist[s][s] = dist[s][c] + 1
        q.append((s, s))
    if s+c <= N and dist[s+c][c] == -1:
        dist[s+c][c] = dist[s][c] + 1
        q.append((s+c, c))
    if s-1 >= 0 and dist[s-1][c] == -1:
        dist[s-1][c] = dist[s][c] + 1
        q.append((s-1, c))

ans = -1
for i in range(0, N+1):
    if dist[N][i] != -1:
        if ans == -1 or ans > dist[N][i]:
            ans = dist[N][i]

print(ans)

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>


<br>
<br>



## 백준 17086번. 아기 상어2

- 문제 링크: https://www.acmicpc.net/problem/17086
- 난이도: 🥇 5티어

<div data-ke-type="moreLess" data-text-more="백준 17086번 아기 상어2 답 확인" data-text-less="백준 17086번 아기 상어2 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque

N, M = map(int, input().split()) # N:세로, M: 가로 
MAP = [list(map(int, input().split())) for _ in range(N)]
check = [[-1] * M for _ in range(N)]
dx = (-1, -1, -1, 0, 1, 1, 1, 0)
dy = (-1, 0, 1, 1, 1, 0, -1, -1)

q = deque()
for i in range(N):
    for j in range(M):
        if MAP[i][j] == 1:
            q.append((i, j))
            check[i][j] = 0

ans = 0
while q:
    x, y = q.popleft()
    for k in range(8):
        nx, ny = x + dx[k], y + dy[k]
        if 0 <= nx < N and 0 <= ny < M and check[nx][ny] == -1:
            check[nx][ny] = check[x][y] + 1
            ans = max(ans, check[nx][ny])
            q.append((nx, ny))

print(ans)

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>

<br>
<br>

## 백준 16930번. 달리기

- 문제 링크: https://www.acmicpc.net/problem/16930
- 난이도: 🏅 3티어

<div data-ke-type="moreLess" data-text-more="백준 16930번 달리기 답 확인" data-text-less="백준 16930번 달리기 답 닫기" class=""><a class="btn-toggle-moreless"></a>
<div class="moreless-content">

``` python 

from collections import deque

dx = [0, 0, 1, -1]
dy = [1, -1, 0, 0]

N, M, K = map(int, input().split()) # N:세로 M:가로, K: step
D = [list(input()) for _ in range(N)]
sx, sy, ex, ey = map(int, input().split())
sx -= 1; sy -= 1; ex -= 1; ey -=1
check = [[float('inf')]*M for _ in range(N)]
q = deque()
q.append((sx, sy))
check[sx][sy] = 0

while q:
    x, y = q.popleft()
    for i in range(4):
        nx, ny = x + dx[i], y + dy[i]
        nk = 1
        while nk <= K and 0 <= nx < N and 0 <= ny < M and D[nx][ny] != '#' and check[nx][ny] > check[x][y]:
            if check[nx][ny] == float('inf'):
                q.append((nx, ny))
                check[nx][ny] = check[x][y] + 1
            nx += dx[i]
            ny += dy[i]
            nk += 1

if check[ex][ey] == float('inf'):
    print(-1)
else:
    print(check[ex][ey])

```

<hr contenteditable="false" data-ke-type="horizontalRule" data-ke-style="style8"></div>
</div>
</div>


<br>
<br>


---------------------------
광고 꽝고  