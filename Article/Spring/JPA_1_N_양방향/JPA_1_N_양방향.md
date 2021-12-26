<!-- 

JPA 1대 N 양방향 연관관계 매핑 및 API 설계

-->


# 0. 시작하며

<br />

프로젝트를 하다가 검색했을 때 1:N 매핑관련 글은 많지만, API 전체 시나리오를 바탕으로 작성한 글은 많이 없기도 하고 객체지향 관점으로 간단하게 생각해볼 포인트가 있어서 함께 글을 작성해봅니다.

<br />

본 글에 사용한 스프링부트 전체 코드는 [Github](https://github.com/KoEonYack/Tistory-Covenant-Code/tree/main/spring-jpa-one-to-mamy-mapping)에서 확인하실 수 있습니다.

<br />
<br />
<br />

# 1. 1-N 모델

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/JPA_1_N_%EC%96%91%EB%B0%A9%ED%96%A5/img/total.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="100%" >
<br />
<center>
좌. 오징어게임 - 구슬치기 장면 <br />
우. 주머니와 돌멩이의 1:N 스키마
</center>
<br />
<br />

하나의 주머니에 N개의 돌멩이를 넣을 수 있는 간단한 스키마를 설계하였습니다.

<br />

API에 주머니, 그리고 N개의 돌멩이 정보를 json으로 보내면 DB에 저장해보겠습니다.

```text
{
    "pocketName": "우아한 주머니",
    "pocketColor": "파랑색",
    "stones": [
        { "stoneName": "돌멩이A" },
        { "stoneName": "돌멩이B" }
    ]
}
```

<br />
<br />
<br />

# 2. 엔티티 구현

<br />

## 2-1. 연관관계의 주인

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/JPA_1_N_%EC%96%91%EB%B0%A9%ED%96%A5/img/schema.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

일(1)-다(N) 관계에서 외래키는 연관관계의 다 쪽에 외래키가 있습니다. 이때 객체 양방향 관계에서 연관관계의 주인은 N(다) 쪽입니다. 본 예제에서는 N(다)에 해당하는 돌멩이가 연관관계의 주인입니다.

<br />
<br />
<br />

## 2-2. 돌멩이 엔티티 (1:`N`)

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/JPA_1_N_%EC%96%91%EB%B0%A9%ED%96%A5/img/stone.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stone_id")
    private int id;

    private String stoneName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pocket_id")
    private Pocket pocket;

    @Builder
    public Stone(String stoneName, Pocket pocket) {
        this.stoneName = stoneName;
        this.pocket = pocket;
    }

    public static Stone createStone(String stoneName, Pocket pocket) {
        return Stone.builder()
                .stoneName(stoneName)
                .pocket(pocket)
                .build();
    }
}
```

<br />

__(1) @Entity__ 
JPA가 테이블과 매핑할 클래스로 해당 클래스가 JPA가 관리하는 엔티티가 됩니다.

<br />

__(2) @NoArgsConstructor(access = AccessLevel.PROTECTED)__
- JPA가 리플랙션을 사용하기 위해서 엔티티는 기본적으로 생성자가 필요합니다.
- 접근지시자는 public 혹은 protected가 가능합니다. 저는 엔티티가 접근을 최소화하기 위해서 AccessLevel.PROTECTED를 쓰는 편입니다.

<br />

__(3) @JoinColumn(name="pocket_id")__
- 지정된 name으로 왜래키 매핑을 합니다. 주머니 테이블의 키인 pocket_id을 name에 작성합니다.

<br />

__(4) @Column(name = "stone_id")__
- 실제 DB에 매핑할 칼럼이름입니다.
- @Column(name = "stone_id")면 db의 stone_id 칼럼에 매핑됩니다.
- PK의 경우 stone처럼 특정 이름이 붙는 경우가 있습니다. 이렬경우 join할때 명확합니다.

<br />

__(5) @ManyToOne__ (fetch = FetchType.LAZY)
- 연관관계에서 N에 해당하기에 @ManyToOne을 사용합니다.

<br />

(6) @ManyToOne __(fetch = FetchType.LAZY)__
- N+1문제를 피하기 위해서 FetchType.LAZY를 선택하였습니다.
- 예를 들어 주머니 N개를 호출하면 이를 위해서 각각 주머니가 소유한 돌멩이를 보여주기 위해서 돌멩이 조회 쿼리가 N개 실행되게 됩니다. FetchType.LAZY를 선택하면 주머니가 소유한 돌멩이를 조회할 때만 실행됩니다.

<br />

(7) 생성자, createStone 메서드에 __Pocket__ 인자
- 양방향 연관관계이기에 주머니, 돌멩이 엔티티 모두 서로의 정보를 알아야 합니다.
- 돌멩이 생성 시점에 주머니의 정보를 추가합니다.

<br />

(8) 엔티티에 __@Getter, @Setter 미사용__
- 엔티티에 Setter를 열어두는 것은 위험합니다. 
- 비즈니스 로직에 중간에 setter가 있으면 왜 Setter를 사용했는지 의미를 파악해야 합니다.
- Update쿼리가 나가면 어디에서 setter가 나갔는지 확인이 어려워집니다. 따라서 changeName과 같은 의미 있는 이름의 메서드를 사용하는 것이 좋습니다.
- Getter의 경우 사용해도 되나 setter처럼 의미 있는 showStoneName()와 같이 의미 있는 메서드 이름을 사용하는 것이 좋습니다.

<br />
<br />
<br />

## 2-3. 주머니 엔티티 (`1`:N)

<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/JPA_1_N_%EC%96%91%EB%B0%A9%ED%96%A5/img/pocket.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="80%" >
<br />

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pocket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pocket_id")
    private int id;

    private String pocketName;
    private String pocketColor;

    @OneToMany(mappedBy = "pocket", 
              cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stone> stones = new ArrayList<>();

    @Builder
    public Pocket(String pocketName, String pocketColor) {
        this.pocketName = pocketName;
        this.pocketColor = pocketColor;
    }

    public static Pocket createPocket(String pocketName, String pocketColor) {
        return Pocket.builder()
                .pocketName(pocketName)
                .pocketColor(pocketColor)
                .build();
    }

    public void putStone(Stone stone) {
        this.stones.add(stone);
    }
}
```

<br />

__@OneToMany__ (mappedBy = "pocket", cascade = CascadeType.ALL, orphanRemoval = true)
- 주머니 엔티티는 1:N관계에서 1이므로 @OneToMany를 사용합니다.

<br />

__@OneToMany__ (mappedBy = "pocket") 속성
- mappedBy는 연관관계 주인(Stone 엔티티)에 작성하지 않습니다.
- mappedBy를 이용하여 Stone에 Pocket이 연관관계가 있다고 알려줍니다.
- 널 포인트 예외가 터지지 않도록 ArrayList<>로 초기화합니다.

<br />

__@OneToMany__ (cascade = CascadeType.ALL) 속성
- 부모 엔티티가 영속 상태로 만들 때 연관 엔티티도 함께 영속화합니다.
- 부모 엔티티인 주머니 저장하면 자식 엔티티인 돌멩이도 함께 영속화합니다.

<br />

__putStone(Stone stone)__
- 주머니 엔티티와 마찬가지로 setter 에노테이션을 사용하지 않았습니다. 
- setter를 이용하여 돌멩이를 집어넣는 것이 아닌 의미 있는 메서드를 사용하여 돌멩이를 집어넣었습니다.

<br />
<br />
<br />

# 3. DTO를 영속화

```java
@Service
@RequiredArgsConstructor
public class PocketService {

    private final PocketRepository pocketRepository;

    public void createPocketAndStones(PocketDTO pocketDTO) {
        Pocket pocket = Pocket.createPocket(
                pocketDTO.getPocketName(), pocketDTO.getPocketColor()
        );

        List<StoneDTO> stones = pocketDTO.getStones();
        for (StoneDTO stoneDTO : stones) {
            Stone stone = Stone.createStone(stoneDTO.getStoneName(), pocket);
            pocket.putStone(stone);
        }
        pocketRepository.save(pocket);
    }
```

<br />

- DTO에서 Pocket 엔티티를 만듭니다. 
- 최종적으로 pocketRepository.save(pocket);를 호출하면 Pocket, Stone들 전부 DB에 저장합니다. 
- pocket만 영속화했는데 Stone들 또한 영속화되는 이유는 Pocket 엔티티에서 cascade = CascadeType.ALL로 설정했기에 자식 엔티티도 함께 영속화되기 때문입니다.


<br />

```java
List<StoneDTO> stones = pocketDTO.getStones();
for (StoneDTO stoneDTO : stones) {
    Stone stone = Stone.createStone(stoneDTO.getStoneName(), pocket);
    pocket.putStone(stone);
}
```
- 위의 예제에서 핵심은 이 부분입니다. 
- DTO에서 List로 넘어온 N개의 돌멩이 정보를 반복문을 돌면서 createStone 메서드를 호출하며 함께 pocket의 정보를 넘겨줍니다.
- 양방향 연관관계이므로 putStone 메서드를 호출하여 pocket에 stone의 정보를 넣어줍니다.
- 마지막에 pocketRepository.save(pocket)를 호출하여 영속화하여 DB에 저장합니다.

<br />
<br />


```java
List<StoneDTO> stones = pocketDTO.getStones();
stones.forEach(stone -> pocket.putStone(
        Stone.createStone(stone.getStoneName(), pocket)
));
```

forEach를 사용하면 위의 코드를 조금 더 줄일 수 있습니다.


<br />
<img src="https://github.com/KoEonYack/Tistory-Coveant/blob/master/Article/Spring/JPA_1_N_%EC%96%91%EB%B0%A9%ED%96%A5/img/table_v2.png?raw=true" align="center" style="display: block; margin: 0px auto; display: block; height: auto; border:1px solid #eaeaea; padding: 0px;" width="50%" >
<br />

요청한 json에 맞추어 Pocket, Stone의 값이 저장된 것을 확인할 수 있스빈다.


<br />
<br />
<br />


# 4. 서비스레이어 리팩터링

<br />

## 4-1. 요구사항의 추가

지금까지 1:N 양방향 연관관계를 살펴보았습니다. 여기서 한 걸음 나아가 엔티티 생성을 고민해봅시다.

<br />

```java
List<StoneDTO> stones = pocketDTO.getStones();
stones.forEach(stone -> pocket.putStone(
        Stone.createStone(stone.getStoneName(), pocket)
));
```

요구사항이 변경되어 주머니에 돌멩이 뿐만 아니라 N개의 구슬, 연필을 추가해야 한다고 해봅니다. 그러면 영속화하는 코드가 증가하여 비즈니스 로직의 복잡도가 높아질 것입니다. 여기서 객체지향의 역할과 책임을 고민할 필요가 있습니다.

<br />
<br />
<br />

## 4-2. 엔티티에 변환 로직 추가 

```java
pocketRepository.save(createPocketDto.toEntity(pocket));    
```

createPocketDto를 만들어서 엔티티 변환 로직을 __toEntity__ 에 위임하는 것입니다. (참고. [병아리 개발자의 걸음마 한 발짝 (feat. 파일럿 프로젝트)](https://techblog.woowahan.com/2644/))

<br />
<br />

혹은 엔티티 Pocket에서 putStone() 메서드를 호출할때 다음과 같이 변경할 수 있습니다.

```java
public void putStone(Stone stone) {
    this.stones.add(stone);
    if (stone.getPocket()) != this) { // 추가
        stone.setPocket(this);
    }
}
```

<br />
<br />
<br />
