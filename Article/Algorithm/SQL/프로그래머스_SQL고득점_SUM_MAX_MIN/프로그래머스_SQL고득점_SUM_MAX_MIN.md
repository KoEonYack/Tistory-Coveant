# 프로그래머스 SQL고득점kit 검색결과 💉 SUM, MAX, MIN 해답


<br /><br />


## 1. 최댓값 구하기

<br />


- 문제 링크: [최댓값 구하기](https://programmers.co.kr/learn/courses/30/lessons/59415)


``` sql
SELECT 
    DISTINCT ANIMAL_TYPE 
FROM 
    ANIMAL_INS
```

<br /><br />

## 2. 최솟값 구하기

<br />


- 문제 링크: [최솟값 구하기](https://programmers.co.kr/learn/courses/30/lessons/59038)


``` sql
SELECT 
    MIN(DATETIME)
FROM 
    ANIMAL_INS
```

<br /><br />

## 3. 동물 수 구하기

<br />


- 문제 링크: [동물 수 구하기](https://programmers.co.kr/learn/courses/30/lessons/59406)


``` sql
SELECT 
    COUNT(*) 
FROM 
    ANIMAL_INS
```

<br /><br />

## 4. 중복 제거하기


<br />


- 문제 링크: [중복 제거하기](https://programmers.co.kr/learn/courses/30/lessons/59408)


``` sql
SELECT 
	COUNT(DISTINCT NAME)
FROM 
	ANIMAL_INS
WHERE
	ANIMAL_INS.NAME IS NOT NULL
```

<br /><br />