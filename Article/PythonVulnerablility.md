# 파이썬 취약점(input 함수)
-------

* 본 글은 [Python Exploitation #1: Input()](
https://medium.com/@GallegoDor/python-exploitation-1-input-ac10d3f4491f) 글을 읽고 작성한 것입니다.
* **raw_input** 함수와 **input** 함수가 존재하는 Python2의 경우에 발생하는 취약점입니다.
* 파이썬2로 작성한 간단한 게임을 통하여 **input** 함수의 취약점을 소개하겠습니다.


##### 예제 코드
```python
import random
secret_number = random.randint(1,500)
print "Pick a number between 1 to 500"
while True:
   res = input("Guess the number: ")
   if res==secret_number:
       print "You win"
       break
   else:
       print "You lose"
       continue
```
* random.randbit를 이용해서 1~499 난수를 secret_number로 지정합니다.
* input함수를 실행해서 입력한 값을 저장합니다.
* secret_number와 res 값이 일치하지 않다면 계속 사용자의 입력을 반복할 것입니다.

##### 입력 예시(1)
```bash
> python input_test.py
Pick a number between 1 to 500
Guess the number: You lose
Guess the number:
```

##### 입력 예시(2)
```bash
> python input_test.py
Pick a number between 1 to 500
Guess the number: secret_number
You win
```
* 1~499사이의 수를 입력하는 것이 아닌 변수 이름인 secret_number를 입력하면 You win 출력을 볼 수 있습니다.
* input은 eval(raw_input())과 같다. 마치 숫자를 직접 입력하는 것처럼 변수를 평가하게 됩니다.
* 본 취약점을 이용하여 데이터베이스에 접근 할 수 있을 것입니다.

##### 보안을 위한 제안
* 파이썬2를 사용한다면 raw_input을 사용하세요.
