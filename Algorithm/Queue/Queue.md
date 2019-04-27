#Queue  
----

* Queue(큐)란?


* 큐 구현의 세 가지 방법
- 원형 배열 큐
- 동적 원형 배열 큐
- 링크드 리스트 기반 구현

* ADT
- 큐의 주 기능
  - enQueue(int data): 큐의 맨 마지막에 노드를 집어 넣는다.
  - int deQueue(): 큐의 맨 앞의 노드를 제거하고 값을 반환한다.
- 큐의 부가 기능
  - int Front(): 큐의 맨 앞에 있는 노드의 값을 반환한다.
  - int QueueSize(): 큐에 저장된 노드의 갯수를 반환한다.
  - int IsEmptyQueue(): 큐가 비었는지 아닌지 알려준다.

* 큐는 언제 사용할까?
- 운영체제에서의 스케줄 알고리즘
- 멀티프로그래밍
- 비동기 데이터 전송(File IO, Socks, Pips)


* 성능
- 다음은 큐에 저장된 노드의 갯수가 n개 일 때의 성능을 나타낸다. 
| 설명 | 복잡도  |
|---|:---:|
| 공간복잡도(n번의 enQueue 수행) | O(n) |
| enQueue의 시간복잡도 | O(1) |
| deQueue의 시간복잡도 | O(1) |
| isEmpty의 시간복잡도 | O(1) |
| isFull의 시간복잡도 |  O(1) |
| getQueueSize의 시간복잡도  | O(1) |




[고정 크기 큐 구현 Code(Git hub)](www.hashcode.co.kr)
[링크드리스트 큐 구현 Code(Git hub)](www.hashcode.co.kr)



#### Reference
* Data Structures and Algorithms Made Easy in Java - Narasimha Karumanchi
