## 내가 보려고 만든 코틀린 부트

### 1. ; 대신 ,
java 에선 모든 문장 끝에 ; 가 붙어야 함.

```java
private String name;
private String email;
private String password;
```

하지만 코틀린에선 , 로 붙임

```kotlin
var name: String? = "",
var email: String? = "",
val password: String
```

### 2. private 대신 val
코틀린에선 변수 선언 시 var, val 로 선언

- var: 가변 변수
- val: 불변 변수 (== final)

### 3. 생성자 어노테이션 불필요
java 에선 @AllArgsConstructor 를 통해 생성자 생성
```java 
@AllArgsConstructor
public class User {
    private String name;
    private String email;
    // ... → User(name, email, ...) 생성자 자동 생성
}
```

kotlin 에선 클래스의 () 안에 있으면 생성자 기능이 내장 돼 있어 편리하다.
단, 자동으로 생성되는 필드들 (ex. id) 은 {} 에 넣어야 한다.
```kotlin
class User (
    var name: String? = "알 수 없음",
    var email: String? = "알 수 없음",
){
    @id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
```

### 4. Dto 는 data 를 붙이면 좋다

원래 java 에서는 dto 생성 시 getter, 생성자 메서드, toString 메서드를 직접 구현하던가 어노테이션을 붙여야 했다.

```java

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
class UserDto{
    private String name;
    private String email;
    private Long uid;
}
```

kotlin 에서는 3번에서 기재했다시피 () 안에선 자동으로 생성자로 인식된다. 그래서 @NoArgsConstructor, @AllArgsConstructor 
를 쓸일이 없다. 그리고 class 앞에 data 를 붙이면 toString/equals/hashCode 메서드가 자동으로 내장된다.
코드 수가 현저히 줄어든다.

추가로 dto 클래스는 읽기 
```kotlin
data class UserDto (
    val name: String,
    val email: String,
    val uid: Long
)
```