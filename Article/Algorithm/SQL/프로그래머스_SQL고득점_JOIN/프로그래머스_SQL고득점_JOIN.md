# 프로그래머스 SQL고득점kit 💉 JOIN 해답

<br />
<br />

# 0. 공략법

<br />

- [💿 문제로 배우는 SQL - JOIN](https://covenant.tistory.com/149) 글을 참고해주세요.
- 코딩테스트에 SQL이 나온다고 하면 JOIN이 나올 확률이 높습니다.
- 테이블 2개가 나왔다 싶으면 문제를 읽으면서 두 테이블 사이 공통된 column이 무엇인지 파악합니다.
- 부족한 데이터라는 키워드가 나오면 Left(Right) Join을 사용할 마음을 갖어야 합니다.
- SUM, COUNT 내장함수, DISTINCT, ORDER BY, HAVING과 같이 등장합니다.
- 프로그래머스 JOIN문제는 최근 나오는 SQL 최고 난이도를 커버하기에는 2% 부족합니단. 본 문제를 다 풀고 Hacker Rank문제를 추가적으로 풀기를 바랍니다. 


<br />
<br />


## 1. 없어진 기록 찾기

<br />

- 문제 링크: [없어진 기록 찾기](https://programmers.co.kr/learn/courses/30/lessons/59042)


``` sql
SELECT 
	ANIMAL_OUTS.ANIMAL_ID, ANIMAL_OUTS.NAME
FROM 
	ANIMAL_OUTS LEFT JOIN ANIMAL_INS
ON
	ANIMAL_OUTS.ANIMAL_ID = ANIMAL_INS.ANIMAL_ID
WHERE
	ANIMAL_INS.ANIMAL_ID IS NULL
```

<br />
<br />

## 2. 있었는데요 없었습니다

<br />

- 문제 링크: [있었는데요 없었습니다](https://programmers.co.kr/learn/courses/30/lessons/59043)

``` sql
SELECT 
	ANIMAL_OUTS.ANIMAL_ID, ANIMAL_OUTS.NAME
FROM 
	ANIMAL_OUTS INNER JOIN ANIMAL_INS
ON
	ANIMAL_OUTS.ANIMAL_ID = ANIMAL_INS.ANIMAL_ID
WHERE
	ANIMAL_OUTS.DATETIME < ANIMAL_INS.DATETIME
ORDER BY
    ANIMAL_INS.DATETIME ASC
```


<br />
<br />

## 3. 오랜 기간 보호한 동물(1)

<br />

- 문제 링크: [오랜 기간 보호한 동물(1)](https://programmers.co.kr/learn/courses/30/lessons/59044)

``` sql
SELECT 
	ANIMAL_INS.NAME, ANIMAL_INS.DATETIME
FROM 
	ANIMAL_INS LEFT JOIN ANIMAL_OUTS 
ON
	ANIMAL_INS.ANIMAL_ID = ANIMAL_OUTS.ANIMAL_ID
WHERE
	ANIMAL_OUTS.ANIMAL_ID IS NULL
ORDER BY
	ANIMAL_INS.DATETIME ASC LIMIT 3
```



<br />
<br />

## 4. 보호소에서 중성화한 동물

<br />

- 문제 링크: [보호소에서 중성화한 동물](https://programmers.co.kr/learn/courses/30/lessons/59045)

``` sql
SELECT  ANIMAL_OUTS.ANIMAL_ID, 
        ANIMAL_OUTS.ANIMAL_TYPE, 
        ANIMAL_OUTS.NAME 
FROM 
    ANIMAL_OUTS LEFT JOIN ANIMAL_INS 
ON 
    ANIMAL_OUTS.ANIMAL_ID=ANIMAL_INS.ANIMAL_ID 
WHERE ANIMAL_INS.SEX_UPON_INTAKE LIKE 'Intact%' AND 
        (ANIMAL_OUTS.SEX_UPON_OUTCOME LIKE 'Spayed%' OR 
         ANIMAL_OUTS.SEX_UPON_OUTCOME LIKE 'Neutered%')
```

<br />

