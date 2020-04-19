# 프로그래머스 SQL고득점kit 💉 "IS NULL" 해답

<br />
<br />


## 1. 이름이 없는 동물의 아이디

<br />

- 문제 링크: [이름이 없는 동물의 아이디](https://programmers.co.kr/learn/courses/30/lessons/59039)



``` sql
SELECT 
    ANIMAL_ID
FROM 
    ANIMAL_INS 
WHERE 
    NAME IS NULL
```

<br />
<br />


## 2. 이름이 있는 동물의 아이디

<br />


- 문제 링크: [이름이 있는 동물의 아이디](https://programmers.co.kr/learn/courses/30/lessons/59407)


``` sql
SELECT 
    ANIMAL_ID
FROM 
    ANIMAL_INS 
WHERE 
    NAME IS NOT NULL
```

<br />
<br />


## 3. NULL 처리하기

<br />

- 문제 링크: [NULL 처리하기](https://programmers.co.kr/learn/courses/30/lessons/59410)


``` sql
SELECT 
    ANIMAL_TYPE,	IFNULL(NAME, 'No name') , SEX_UPON_INTAKE
FROM ANIMAL_INS
```


<br />
