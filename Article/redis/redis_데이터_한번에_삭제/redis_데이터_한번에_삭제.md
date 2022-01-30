# Redis 데이터 한번에 삭제하기

<br />
<br />

# 상황

<br />

```cmd
$ redis-cli --bigkeys
```

레디스에 메모리 사용량이 내려가지 않아서 어디에서 메모리를 사용하는지 확인하기 위해서 위의 명령어를 사용하였습니다.

<br />

bigkeys 명령어는 scan하기에 운영에서 사용해도 됩니다.

<br />

```text
Biggest set found so far 'spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:userId' has 555 members
```

<br />

결과를 보니 스프링 세션을 레디스에 저장되도록 설정하여 생기는 문제로 보입니다. 데이터가 많기에 하나씩 삭제할 수는 없으며 키 패턴에 맞추어서 한 번에 삭제를 해야합니다.

<br />
<br />
<br />

# 해결

<br />

레디스는 복수개의 키를 한 번에 삭제하는(벌크 삭제) 방법을 제공하지 않습니다. 

<br />

```text
redis-cli --scan --pattern 'key1:key2:*' | xargs redis-cli unlink
```

<br />

del 로 지우면 키를 지우는동안 아무것도 사용할 수 없습니다. unlink를 사용하면 키를 백그라운드로 지우기에 벌크 연산에는 del보다 unlink를 사용하는것이 적절합니다. 

<br />

앞에서 키 패턴에 맞는 스캔 결과를 unlink 값으로 보냅니다. 이를 통해서 하위의 키를 한번에 지울 수 있습니다.

<br />
<br />

```text
$ redis-cli --scan --pattern 'spring:session:*' | xargs redis-cli unlink
```

<br />

앞서 본 문제의 레디스에 저장된 스프링 세션 정보를 삭제하려면 위의 명령어를 사용하면 됩니다.

<br />
<br />
<br />
