<!-- 

완벽정리! 자바 string을 배열로 분리하기

-->


<br />
<br />

# 방법 1. String.split()

<br />

자바에서 문자열을 배열로 분리하기 위해 많이 사용하는 방법으로 String.split()이 있습니다. 인자로 구분자 혹은 정규식을 넣을 수 있습니다.

<br />
<br />

## 방법 1-1. String.split() 구분자
```java
String str = "Apple,Amazone,Google,Microsoft";
String[] list = str.split(",");

System.out.println(Arrays.toString(list));
```
```text
[Apple, Amazon, Google, Microsoft]
```

, 구분자를 넣었을 때 문자열을 배열로 분리해줍니다.

<br />
<br />

```java
String str = "Apple Amazon Google Microsoft";
String[] list = str.split(" ");
System.out.println(Arrays.toString(list));
```
```text
[Apple, Amazon, Google, Microsoft]
```

공백을 기준으로 구분하려고 한다면 공백을 넣어주면 됩니다. 


<br />
<br />

## 방법 1-2. String.split() 정규식

```java
String str = "a p, p, l.e c, o r.p";
String[] list = str.split("\\s+|,\\s*|\\.\\s*");

System.out.println(Arrays.toString(list));
```
```text
[a, p, p, l, e, c, o, r, p]
```

정규식을 넣을 수 있기에 복잡한 문자열도 정규식을 이용하여 구분할 수 있습니다. 

<br />
<br />

## 방법 1-3. String.split() 공백제거

<br />

분리하려는 문자열 구분자 사이에 공백이 있는 경우 처리하는 방법입니다.

```java
String str = "Apple  , Amazon , Google , Microsoft";
String[] list = str.trim().split("\\s*,\\s*");

System.out.println(Arrays.toString(list));
```
```text
[Apple, Amazon, Google, Microsoft]
```

trim() 메소드는 문자열의 선행 및 후행 공백을 제거하며 `"\\s*,\\s*"` 정규식은 구분 기호 주변의 추가 공백을 처리합니다. 

<br />

```java
String str = "Apple  , Amazon , Google , Microsoft";
String[] list = Arrays.stream(str.split(","))
                .map(String::trim)
                .toArray(String[]::new);

System.out.println(Arrays.toString(list));
```
```text
[Apple, Amazon, Google, Microsoft]
```

위의 코드를 stream을 이용하여 구현할 수 있습니다.

<br />
<br />
<br />

# 방법 2. Pattern.split()

```java
String str = "Apple Amazon Google Microsoft";
Pattern pattern = Pattern.compile("\\s");
String[] list = pattern.split(str);

System.out.println(Arrays.toString(list));
```
```text
[Apple, Amazon, Google, Microsoft]
```

Java의 Pattern 클래스를 이용하여 정규식을 만들어 문자열을 분리할 수 있습니다.

<br />

먼저 패턴을 만들려면 Pattern.compile() 메소드를 이용하여 분리할 문자열의 패턴을 만듭니다. 

<br />

split() 메소드를 이용하여 지정된 패턴에 따라 문자열을 배열로 분리합니다.

<br />
<br />
<br />

# 방법 3. Apache Commons

<br />

문자를 다루는 편리한 라이브러리로 [Apache Common Lang](https://commons.apache.org/proper/commons-lang/)가 있습니다. StringUtils.split을 이용하여 배열 문자열을 문자열로 변환할 수 있습니다.

<br />

```java
String str = "Apple Amazon Google Microsoft";
String[] list = StringUtils.split(str, " ");

System.out.println(Arrays.toString(list));
```
```text
[Apple, Amazon, Google, Microsoft]
```

split의 첫 번째 인자로 배열로 분리할 문자열, 두 번째 인자로 구분 문자를 넣으면 됩니다. 또한 null safe한 메서드이므로 null 문자열이 들어가도 됩니다.

<br />
<br />

```java
String str = "Apple Amazon   Google Microsoft";
String[] list = StringUtils.split(str);

System.out.println(Arrays.toString(list));
```
```text
[Apple, Amazon, Google, Microsoft]
```

split의 장점으로 문자열에 불규칙한 길이의 화이트 스페이스를 배열로 분리한다면 구분문자를 생략하면 됩니다. 그러면 추가 공백이 있더라도 공백 기준으로 문자열을 분리해줍니다.

<br />
<br />
<br />
<br />

본 글 작성을 위해서 다음 글을 참고하였습니다. [baeldung.com](https://www.baeldung.com/java-split-string), [attacomsian.com](https://attacomsian.com/blog/java-convert-string-to-array)


<br />
<br />


<!-- 
```java

```
```text

```
-->