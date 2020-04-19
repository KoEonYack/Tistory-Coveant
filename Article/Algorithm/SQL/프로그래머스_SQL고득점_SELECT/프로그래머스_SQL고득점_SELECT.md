# 프로그래머스 SQL고득점kit 검색결과 💉 SELECT 해답


<br /><br />


## 1. 모든 레코드 조회하기 

<br />


- 문제 링크: [모든 레코드 조회하기](https://programmers.co.kr/learn/courses/30/lessons/59034)


``` sql
SELECT * FROM ANIMAL_INS
```

<br /><br />


## 2. 역순 정렬하기

<br />

- 문제 링크: [역순 정렬하기](https://programmers.co.kr/learn/courses/30/lessons/59035)


``` sql
SELECT
	ANIMAL_INS.NAME, ANIMAL_INS.DATETIME
FROM 
	ANIMAL_INS
ORDER BY 
	ANIMAL_INS.ANIMAL_ID DESC
```

<br /><br />



## 3. 아픈 동물 찾기

<br />

- 문제 링크: [아픈 동물 찾기](https://programmers.co.kr/learn/courses/30/lessons/59036)


``` sql
SELECT ANIMAL_ID, NAME FROM ANIMAL_INS WHERE INTAKE_CONDITION Like 'Sick'
```

<br /><br />


## 4. 어린 동물 찾기

<br />

- 문제 링크: [어린 동물 찾기](https://programmers.co.kr/learn/courses/30/lessons/59037)


``` sql
SELECT 
    ANIMAL_ID, NAME 
FROM 
    ANIMAL_INS 
WHERE 
    INTAKE_CONDITION NOT LIKE "Aged"
```


<br /><br />


## 5. 동물의 아이디와 이름

<br />

- 문제 링크: [동물의 아이디와 이름](https://programmers.co.kr/learn/courses/30/lessons/59403)


``` sql
SELECT
    ANIMAL_INS.ANIMAL_ID, ANIMAL_INS.NAME
FROM 
    ANIMAL_INS
ORDER BY
    ANIMAL_ID
```

<br />
<br />



## 6. 여러 기준으로 정렬하기

<br />

- 문제 링크: [여러 기준으로 정렬하기](https://programmers.co.kr/learn/courses/30/lessons/59404)


``` sql
SELECT
    ANIMAL_INS.ANIMAL_ID, ANIMAL_INS.NAME, ANIMAL_INS.DATETIME
FROM
    ANIMAL_INS 
ORDER BY
    ANIMAL_INS.NAME, 
    ANIMAL_INS.DATETIME DESC
```

<br /><br />


## 7. 상위 n개 레코드

<br />

- 문제 링크: [상위 n개 레코드](https://programmers.co.kr/learn/courses/30/lessons/59405)


``` sql
SELECT
    ANIMAL_INS.NAME
FROM
    ANIMAL_INS 
ORDER BY
    ANIMAL_INS.DATETIME	ASC LIMIT 1
```

<br />

