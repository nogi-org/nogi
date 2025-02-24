## 코드 작성 규칙

### 패키지구조는 사용하여 4계층으로 나눈다.
- **interfaces** : `Consumer(kafka)`, `Controller(api)`, `EventListener`, `Schedule`)
- **application** : `Facade`
- **domain** : `Entity`, `Service(비즈니스 로직)`, `EventService(이벤트 관련 로직)`, `Repository(I/F)`, `EventHandler(I/F)`,
- **infra** : `RepositoryImpl`, `EventHandlerImpl`, `JpaRepository(I/F)`

### DTO 는 계층별로 정해진 DTO 네이밍이 있고 계층별로 dto 패키지를 두어 관리한다.

- **~~Dto** : presentaion 계층에서 받을 때 사용하고 `Consumer, Controller 의 매개변수로 사용`한다..
- **~~Command** : 비즈니스 로직을 동작할 때 사용하고 `Facade와 Service 의 매개변수로 사용`한다..
- **~~EventCommand** : 이벤트를 전송할 때 사용하고 EventListener 와 ApplicationEventPublisher 의 publishEvent() 메서드에서 사용됩니다.
발행하는 객체와 같아야 Listener 에서 받을 수 있어서 예외적으로 presentation 계층에서 ~~EventCommand 로 받는다..
- **~~Info** : `Service 단 또는 Facade 계층에서 반환 값`으로 사용되는 클래스이다..
- **~~Result** : Service 의 반환 값이 `Facade 에서 재사용`되어야할 때, Info 대신 Result 를 `Service의 반환 값`으로 사용한다..

### Controller 에서 Facade 와 Service 호출 모두 허용한다.
- controller 이외의 다른 계층간의 이동은 모두 한번에 1계층으로만 이동이 가능하다..
```java
// 아래 2가지 case 만 존재
interfaces -> application -> domain -> infra
interfaces -> domain -> infra
```
