# 프로그래머스 SQL고득점kit 💉 GROUP BY 해답


<br /><br />


## 1. 고양이와 개는 몇 마리 있을까

<br />


- 문제 링크: [고양이와 개는 몇 마리 있을까](https://programmers.co.kr/learn/courses/30/lessons/59040)

<br />

``` sql
SELECT 
    ANIMAL_TYPE, COUNT(*)  
FROM 
    ANIMAL_INS
GROUP BY 
    ANIMAL_TYPE
```

<br /><br />

## 2. 동명 동물 수 찾기

<br />

- 문제 링크: [동명 동물 수 찾기](https://programmers.co.kr/learn/courses/30/lessons/59041)


``` sql
SELECT NAME, COUNT(*) AS COUNT
FROM 
    ANIMAL_INS
WHERE 
    NAME IS NOT NULL
GROUP BY 
    NAME
HAVING 
    COUNT >= 2
```

<br /><br />

## 3. 입양 시각 구하기(1)

<br />


- 문제 링크: [입양 시각 구하기(1)](https://programmers.co.kr/learn/courses/30/lessons/59412)


``` sql
SELECT 
    SUBSTRING(DATETIME, 12, 2) AS HOUR, COUNT(DATETIME) 
FROM 
    ANIMAL_OUTS 
GROUP BY 
    HOUR
HAVING 
    HOUR BETWEEN 9 AND 19
```


<br /><br />

## 4. 입양 시각 구하기(2)

<br />


- 문제 링크: [입양 시각 구하기(2)](https://programmers.co.kr/learn/courses/30/lessons/59413)
- SET 변수를 사용하면 해결 할 수 있습니다. 
- WHERE 조건을 이용하여 0시 부터 23시까지 HOUR_LIST를 1씩 증가합니다.
- 기업 SQL 테스트에서 SET를 사용하는 것은 본 적이 없습니다. 이런게 있구나 하고만 넘어가면 되겠습니다.


``` sql
SET @HOUR_LIST = -1; 
SELECT 
    (@HOUR_LIST := @HOUR_LIST + 1) AS 'HOUR',
    (SELECT COUNT(*) 
     FROM ANIMAL_OUTS 
     WHERE HOUR(DATETIME) = @HOUR_LIST) AS 'COUNTS' 
FROM ANIMAL_OUTS 
WHERE @HOUR_LIST <23;
```

