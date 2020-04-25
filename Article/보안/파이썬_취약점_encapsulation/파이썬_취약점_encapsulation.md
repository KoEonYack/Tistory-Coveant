# 🔏 파이썬 취약점 Encapsulation


<br />


 Java와 같은 다른 프로그래밍 언어는 private 변수를 만들 수 있습니다. 하지만, 파이썬은 그렇지 않습니다. 일부는 CPython을 위해 C에서 private 타입을 구현했지만 자주 사용되지는 않습니다. 파이썬으로 캡슐화하기 위해서 속성을 선언할 때 두 개의 밑줄 (_)을 추가하면 됩니다. 그러나 파이썬은 캡슐화를 하더라도 `Exploitation`이 가능합니다.

<br />

``` python
class Character:
    name = "John"
    age = 20
    weight = "40kg"
    __real_name = "Kim"
    __real_age = 30
    __real_weight = "60kg"
```

Character 클래스를 만들었습니다. 이 클래스의 attributes로 이름, 나이, 몸무게가 있고 진짜 이름, 진짜 나이 그리고 진짜 몸무게는 _(밑줄)을 이용하여 감추었습니다.


<br />


``` python
character = Character()
print(character.name)
```

``` text
John
```

일반적으로 파이썬에서 선언한 클래스의 name(normal attribute)의 값을 불러오는 방법은 위와 같을 것입니다. 


<br />

``` python
print(character.__real_name)
```

``` text
Traceback (most recent call last):
  File "C:/XXXXXXXXXXX", line XX, in <module>
AttributeError: 'Character' object has no attribute '__real_name'
```

그러나 _(밑줄)로 캡슐화(encapsulation)가 된 hidden attribute를 출력하려고 하면 오류를 볼 수 있습니다.


<br />


``` python
print(dir(character))
```

``` text
['_Character__real_age', '_Character__real_name', '_Character__real_weight', '__class__', '__delattr__', '__dict__', ...
```

`dir()` 내장 함수는 객체를 인자로 넣어주면 인자로 들어온 객체가 어떤 변수와 메소드를 가지고 있는지 나열해줍니다. 캡슐화된 변수가 클래스에서 선언한 이름과 다르게 긴 이름을 가지고 있다는 것을 알 수 있습니다.

<br />

``` python
print(character._Character__real_name)
```

``` text
Kim
```

`dir()` 내장함수에서 알아낸 private 변수명으로 접근을 하면 감추어졌던 Kim이라는 이름을 출력할 수 있습니다. 캡슐화된 private 변수에(encapsulated private variables) 접근 할 수 있습니다.


사실 이러한 점이 취약점으로 작용하는 상황은 극히 제한적일 것입니다. 파이썬 웹 프레임워크에서 객체로 private 값을 관리하는 경우 외부의 값 주입(input injection)을 통해서 private 값을 읽어올 수 있습니다. python2 부터 있었던 issue이며 이에 유의해서 개발해야합니다.

<br />
<br />


### 참고

<br />

- [Python Exploitation #2: Encapsulation](https://medium.com/@GallegoDor/python-exploitation-2-encapsulation-e9a91e7f8e0e)