# 💿 문제로 배우는 SQL - JOIN

<br />


## 1. JOIN이란?

<br />

<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Lecture/%EB%AC%B8%EC%A0%9C%EB%A1%9C_%EB%B0%B0%EC%9A%B0%EB%8A%94_SQL_join/img/1.PNG?raw=true" align="center"  style="display: block; margin: 0px auto; display: block; margin: 0px auto; max-width: 100%; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >

<center> JOIN의 기본아이디어</center>

<br />


- JOIN 연산은 다른 테이블을 가지고 와서 열을 늘리는 집합 연산입니다. 
- JOIN 연산은 원하는 데이터가 하나의 테이블이 아닌 복수의 테이블에 분산되어 있는 데이터를 선택할 수 있게 해줍니다.  

<br />


## 2. JOIN의 종류

<br />


<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Lecture/%EB%AC%B8%EC%A0%9C%EB%A1%9C_%EB%B0%B0%EC%9A%B0%EB%8A%94_SQL_join/img/join.PNG?raw=true" align="center"  style="display: block; margin: 0px auto; display: block; margin: 0px auto; max-width: 100%; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >

<br />


- (INNER) JOIN: 두 테이블에 일치하는 값이 있는 레코드를 반환합니다.
- LEFT (OUTER) JOIN: 왼쪽 테이블에서 모든 레코드를 반환하고 오른쪽 테이블에서 일치하는 레코드를 반환합니다.
- RIGHT (OUTER) JOIN: 오른쪽 테이블에서 모든 레코드를 반환하고 왼쪽 테이블에서 일치하는 레코드를 반환합니다.
- FULL (OUTER) JOIN: 왼쪽 또는 오른쪽 테이블에 일치 항목이 있을 때 모든 레코드를 반환합니다.


<br />


## 3. 내부결합(INNER JOIN) 연산 사용법

<br />


- JOIN을 사용하기 위해서 두 가지 결합 포인트를 지정해 주어야 합니다.

<br />

### 3-1. 결합 지점 1. FROM

<br />


FROM 다음에 결합 하고자 하는 테이블들을 지정해야합니다. 두 테이블을 내부결합할 것이기 때문에 `INNER JOIN` 을 씁니다.


``` sql
FROM CITY INNER JOIN COUNTRY 
```

<br />


필수는 아니지만  `AS` 로 테이블의 별명을 지정하기도 합니다.

``` sql
FROM CITY AS CI INNER JOIN COUNTRY AS CR
```



<br />

### 3-2. 결합 지점 2. ON

<br />


두 개의 테이블을 연결할 열(결합 키)를 지정합니다. 

``` sql
ON CITY.COUNTRYCODE = COUNTRY.CODE 
```

<br />

### 3-3. SQL 연습문제 1

<br />


- [HackerRank. Asian Population](https://www.hackerrank.com/challenges/asian-population/problem) 문제를 보겠습니다.
- CONTINENT가 Asia인 COUNTRY의 POPULATION 합을 구해야합니다.


<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Lecture/%EB%AC%B8%EC%A0%9C%EB%A1%9C_%EB%B0%B0%EC%9A%B0%EB%8A%94_SQL_join/img/2.PNG?raw=true" align="center"  style="display: block; 2margin: 0px auto; display: block; margin: 0px auto; max-width: 100%; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >

<br />

- 문제의 Note에 CITY.CountryCode와 COUNTRY.Code가 매칭 Key라고 합니다. 
- 즉, CITY.CountryCode에 속하는 열을 다리로 사용해서 COUNTRY.Code 열을 하나의 결과에 포함시키는 것입니다.
- 이렇게 두 개의 테이블을 결합할 수 있습니다. 결합한 테이블에서 CONTINENT가 Asia인 조건을 주어야 합니다. 이는 `WHERE` 다음에 조건을 작성하면 됩니다. 

<br />


``` sql
SELECT 
    SUM(CITY.POPULATION) 
FROM 
    CITY INNER JOIN COUNTRY  
ON 
    CITY.COUNTRYCODE = COUNTRY.CODE 
WHERE 
    COUNTRY.CONTINENT = 'Asia';
```

<center> HackerRank: Asian Population 답 </center>

<br />

- 주의! ON은 FROM과 WHERE 사이에 작성해야 합니다.


<br />


### 3-4. SQL 연습문제 2

<br />


- [프로그래머스. 있었는데요 없었습니다](https://programmers.co.kr/learn/courses/30/lessons/59043) 문제를 보겠습니다.
- 문제 요약을 하면 일부 동물의 입양일이 잘못 입력되었습니다. 보호 시작일보다 입양일이 더 빠른 동물의 아이디와 이름을 조회하는 SQL문을 작성해야합니다. 또한 보호 시작일이 빠른 순으로 조회해야합니다.

<br />


<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Lecture/%EB%AC%B8%EC%A0%9C%EB%A1%9C_%EB%B0%B0%EC%9A%B0%EB%8A%94_SQL_join/img/3.PNG?raw=true" align="center"  style="display: block; 2margin: 0px auto; display: block; margin: 0px auto; max-width: 100%; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >

<br />


- 문제에 ANIMAL_OUTS 테이블의 ANIMAL_ID는 ANIMAL_INS의 ANIMAL_ID의 외래 키라고 합니다. ANIMAL_ID를 이용해서 INNER JOIN 하면 됩니다.


<br />

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

<center> 프로그래머스: 있었는데요 없었습니다. 답 </center>

- FROM 구에서 ANIMAL_OUTS와 ANIMAL_INS를 INNER JOIN할 것이라고 표현했습니다.
- ON 구에서 ANIMAL_ID를 이용하여 두 테이블을 결합합니다.
- WHERE구 DATETIME은 부등호를 이용해서 대소 관계를 비교하면 됩니다.
- ORDER BY를 이용해서 DATETIME을 정렬하면 됩니다. ASC를 쓰지 않더라도 오름차순으로 정렬되지만, 저는 혼돈을 막기 위해서 ASC라고 표현하는 편입니다.

<br />


## 4. 왼쪽 결합(LEFT JOIN) 연산 문제

<br />

- [프로그래머스. 없어진 기록 찾기](https://programmers.co.kr/learn/courses/30/lessons/59042) 문제를 보겠습니다.
- 본 문제에서 입양을 간 기록은 있는데, 보호소에 들어온 기록이 없는 동물의 ID와 이름을 ID 순으로 조회하는 SQL문을 작성해야 합니다.

<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Lecture/%EB%AC%B8%EC%A0%9C%EB%A1%9C_%EB%B0%B0%EC%9A%B0%EB%8A%94_SQL_join/img/3.PNG?raw=true" align="center"  style="display: block; 2margin: 0px auto; display: block; margin: 0px auto; max-width: 100%; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >

<br />


- 문제에 ANIMAL_OUTS 테이블의 ANIMAL_ID는 ANIMAL_INS의 ANIMAL_ID의 외래 키라고 합니다. ANIMAL_ID를 이용해서 INNER JOIN 하면 됩니다.

<br />

<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Lecture/%EB%AC%B8%EC%A0%9C%EB%A1%9C_%EB%B0%B0%EC%9A%B0%EB%8A%94_SQL_join/img/leftjoin.PNG?raw=true" align="center"  style="display: block; 2margin: 0px auto; display: block; margin: 0px auto; max-width: 100%; height: auto; border:1px solid #eaeaea; padding: 0px;" width="30%" >

<br />

- ANIMAL_OUTS에 기록은 있지만, ANIMAL_INS 기록이 없는 동물을 찾으려고 합니다.
- ANIMAL_OUTS을 기준으로 JOIN 하려고 합니다.

``` sql
SELECT 
	ANIMAL_OUTS.ANIMAL_ID, ANIMAL_OUTS.NAME, ANIMAL_INS.ANIMAL_ID
FROM 
	ANIMAL_OUTS LEFT JOIN ANIMAL_INS
ON
	ANIMAL_OUTS.ANIMAL_ID = ANIMAL_INS.ANIMAL_ID	
```

<br />


<img src="https://github.com/KoEonYack/PracticeCoding/blob/master/Article/Lecture/%EB%AC%B8%EC%A0%9C%EB%A1%9C_%EB%B0%B0%EC%9A%B0%EB%8A%94_SQL_join/img/leftjoin_res.PNG?raw=true" align="center"  style="display: block; 2margin: 0px auto; display: block; margin: 0px auto; max-width: 100%; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >

<br />


- JOIN 하듯이 ON에 두 테이블을 연결할 조건을 작성하여 LEFT JOIN을 하면 위와 같이 결과가 나옵니다.
- ANIMAL_ID에 NULL 값이 있는 열도 불려와 지는 것을 볼 수 있습니다.
- LEFT(RIGHT) JOIN을 하면 필수적으로 WHERE절에서 ANIMAL_INS.ANIMAL_ID IS NULL 이렇게 NULL 조건을 지정해 주어야 합니다.

<br />

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

<center> 프로그래머스: 없어진 기록 찾기 답 </center>


<br />
<br />

## 출처
- [W3School. SQL Joins: Join image](https://www.w3schools.com/sql/sql_join.asp)

