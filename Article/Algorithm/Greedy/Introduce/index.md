# 그리디 알고리즘(Greedy Algorithm) 및 백준 문제 추천


## 조감도

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Algorithm/Greedy/Introduce/img/mindmap.png?raw=true" align="center" width="800px" >
</p>

탐욕 알고리즘 아이디어를 활용한 알고리즘(문제들) 입니다.

<br/>


## 도입

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Algorithm/Greedy/Introduce/img/kakao.jpg?raw=true" align="center" width="800px" >
</p>

제주 카카오에서 일하고 있던 무지는 판교 카카오에 있는 라이언이 빨리 오라는 카톡을 보고 판교 카카오로 이동하려고 합니다. 마음이 급한 무지는 당황하며 어떻게 하면 빨리 이동할 수 있을지 고민을 합니다. <br>
무지는 크게 두 가지 방법으로 판교 카카오로 이동할 수 있습니다.


1. 지도에서 최단 경로를 검색해서 각각 이동 경로마다 최단 비행기, 최단 버스, 최단 택시를 탄다. 
2. 일단 가장 빠르게 가능 방법을 선택한다. 

그리디 알고리즘은 2번 방법입니다. 일단 가장 빠르게 갈 수 있는 택시를 타고, 제주 공항에서 가장 빠르게 갈 수 있는 비행기를 타고, 인천 공항에서 가장 빠르게 판교로 가는 버스를 타는 것입니다.(경험상 9007번이 빠릅니다.) 

<br/>

## 그리디 알고리즘이란?

현재 시점에서 최적의 답을 찾기 위한 선택을 하는 방법입니다. 가장 직관적인 문제해결이지만 제한 조건을 만족하는 적합한 해를 찾는 방법이지 최적의 해를 찾는 방법은 아닙니다. 최적의 해를 찾기 위해서는 동적계획법(Dynamic Programming)을 사용합니다. 

<br/>

## 예시. 외판원(Traveling Salesman) 문제

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Algorithm/Greedy/Introduce/img/graph2.png?raw=true" align="center" width="350px" >
</p>

A, B, C, D 도시가 있습니다. 각 도시의 다리를 건너갈 때 숫자 만큼의 비용이 듭니다. 최소의 비용으로 __모든 도시를 방문__ 하려고 하면 어떻게 해야할까요?

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Algorithm/Greedy/Introduce/img/graph1.png?raw=true" align="center" width="350px" >
</p>

탐욕 알고리즘을 이용하면 A->B->D->C->A 총 33 비용이 듭니다. 

<p style="text-align: center;">
<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Algorithm/Greedy/Introduce/img/graph3.png?raw=true" align="center" width="350px" >
</p>

그러나 잘 생각해보면 A->B->C->D->A 총 29 비용으로 모든 도시를 방문할 수 있습니다. 탐욕 알고리즘은 항상 최적의 해를 주지 않습니다.

<br/>

## 백준 문제 추천
| No. | 난이도 | 백준 링크 | 문제 풀이 |
|:--------:|:--------:|:--------:|:--------:|
| 1 | 🌕🌑🌑🌑🌑 | [동전 0](https://www.acmicpc.net/problem/11047) | [문제해설](https://covenant.tistory.com/125) | 
| 2 | 🌕🌗🌑🌑🌑 | [ATM](https://www.acmicpc.net/problem/1012) | [문제해설](https://covenant.tistory.com/127) | 
| 3 | 🌕🌗🌑🌑🌑 | [로프](https://www.acmicpc.net/problem/2583) | [문제해설](https://covenant.tistory.com/128)|
| 4 | 🌕🌗🌑🌑🌑 | [강의실 배정](https://www.acmicpc.net/problem/11000) | [문제해설](https://covenant.tistory.com/129) | 
| 5 | 🌕🌗🌑🌑🌑 | [시험 감독](https://www.acmicpc.net/problem/13458) | [문제해설](https://covenant.tistory.com/130)| 
| 6 | 🌕🌗🌑🌑🌑 | [신입 사원](https://www.acmicpc.net/problem/4796) | [문제해설-TODO]() | 
| 7 | 🌕🌗🌑🌑🌑 | [캠핑 문제](https://www.acmicpc.net/problem/1946) | [문제해설-TODO]() | 
| 8 | 🌕🌗🌑🌑🌑 | [모두의 마블](https://www.acmicpc.net/problem/12845) | [문제해설-TODO]() | 
| 9 | 🌕🌕🌑🌑🌑| [회의실 배정](https://www.acmicpc.net/problem/1931) | [문제해설](https://covenant.tistory.com/126) | 
| 10 | 🌕🌕🌗🌑🌑 | [멀티탭 스케줄링](https://www.acmicpc.net/problem/1700) | [문제해설-TODO]() | 
| 11 | 🌕🌕🌗🌑🌑 | [DNA](https://www.acmicpc.net/problem/1969) | [문제해설-TODO]() | 

<br/>

난이도는 주관적인 기준으로 작성했습니다.

<br/>


## 그리디 알고리즘 백준 문제집
- 더 많은 백준 문제를 보고 싶다면! [백준 탐욕알고리즘 문제집 링크](https://www.acmicpc.net/problem/tag/%EA%B7%B8%EB%A6%AC%EB%94%94%20%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98)

<br>

---------- 

## 참고
- [29.1 Greedy Method (Youtube)](https://www.youtube.com/watch?v=lvtxSqxpXkM)
- [탐욕 알고리즘](https://ratsgo.github.io/data%20structure&algorithm/2017/11/22/greedy/)
- 문제 참고 :  [[SCCC 스터디] 8일차 그리디](https://ssu-gongdoli.tistory.com/68)
- 이미지 : [제주 카카오 이미지](https://www.kakaocorp.com/kakao/travelDay/inJejuTourB)