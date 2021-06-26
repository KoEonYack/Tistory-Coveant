<!-- 

스프링에서 prototype은 언제 사용할까?

-->

<br />
<br />

## 이유 1. 쓸일이 없다.

<br />

> As someone who previously worked at SpringSource ... I have also found in my use over the years that I cannot thing of any other place where prototype makes sense in any real world production application. If your object holds state, it typically shouldn't be a Spring bean. I have found in all the applications I have worked on that all beans are Services, Repositories, and Singleton non state holding objects where I need to add features like Transactionality, JPA, JMS and the likes that give us the enterprise features that POJOs don't have.

[Stackoverflow Spring prototype scope - Use Cases?](https://stackoverflow.com/questions/9664810/spring-prototype-scope-use-cases)의 답변 일부를 가져왔습니다. 답변자가 일했다고 한 SpringSource는 Spring 및 관련 프로젝트를 지원하고 개발하기 위해 Spring Framework의 설립자가 만든 회사입니다. 

<br />

prototype을 실제 프로덕션 애플리케이션에 사용할 이유가 없다고 합니다. 

스택오버플로우에 prototype을 사용했다는 여러 글을 보았지만, 다른 방식으로 스프링에서 지원하거나 굳이 그렇게 해야하나.. 이런 생각이 드는 사례가 주였습니다.

<br />
<br />
<br />

## 이유 2. Stateful Pollution

<br />

싱글톤 빈에서 프로토타입 빈을 참조하는 경우, 싱글톤 빈의 인스턴스는 단 한번만 생성되고 그 때 프로토타입 빈의 주입도 이미 완료됩니다. 싱글톤 빈을 사용할때 주입받은 프로토타입 빈이 변경(업데이트) 되지 않습니다.

<br />

> Stateful Pollution: All stateless Bean will be polluted when they are injected by the prototype bean.

<br />



<br />
<br />
<br />

