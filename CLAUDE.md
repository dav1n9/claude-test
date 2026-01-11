# CLAUDE.md

# 프로젝트 개요

간단한 CRUD(Create, Read, Update, Delete) 기능을 제공하는 Spring Boot 기반 REST API 프로젝트입니다.

## 기술 스택
- Java 17
- Spring Boot 3.2.x
- Spring Data JPA
- H2 Database (개발환경)
- MySQL (운영환경)
- Lombok
- Gradle

## 주요 기능
- 사용자(User) 관리 CRUD
- 게시글(Post) 관리 CRUD
- RESTful API 제공

# 아키텍처

## 레이어드 아키텍처
프로젝트는 3-Tier 레이어드 아키텍처를 따릅니다:
```
Controller (Presentation Layer)
    ↓
Service (Business Logic Layer)
    ↓
Repository (Data Access Layer)
    ↓
Database
```

### 각 레이어 역할
- **Controller**: HTTP 요청/응답 처리, 요청 검증
- **Service**: 비즈니스 로직 처리, 트랜잭션 관리
- **Repository**: 데이터베이스 CRUD 작업
- **Entity**: JPA 엔티티, 데이터베이스 테이블 매핑
- **DTO**: 계층 간 데이터 전송 객체

## 패키지 구조
```
src/main/java/com/example/demo/
├── controller/
│   ├── UserController.java
│   └── PostController.java
├── service/
│   ├── UserService.java
│   └── PostService.java
├── repository/
│   ├── UserRepository.java
│   └── PostRepository.java
├── entity/
│   ├── User.java
│   └── Post.java
├── dto/
│   ├── request/
│   │   ├── UserCreateRequest.java
│   │   └── UserUpdateRequest.java
│   └── response/
│       └── UserResponse.java
└── exception/
    ├── GlobalExceptionHandler.java
    └── ResourceNotFoundException.java
```

# 코딩 컨벤션

## 네이밍 규칙
- **클래스명**: PascalCase (예: UserController, UserService)
- **메서드명**: camelCase (예: findUserById, createUser)
- **변수명**: camelCase (예: userId, userName)
- **상수명**: UPPER_SNAKE_CASE (예: MAX_LENGTH, DEFAULT_PAGE_SIZE)
- **패키지명**: lowercase (예: controller, service, repository)

## 코드 스타일
- 들여쓰기: 스페이스 4칸
- 중괄호는 K&R 스타일 (같은 줄에 시작)
- 한 줄 최대 길이: 120자
- 메서드는 한 가지 역할만 수행 (Single Responsibility)

## Lombok 사용 규칙
- Entity: `@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- DTO: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`
- `@Data`는 사용하지 않음 (명시적 선언 선호)

# REST API 규칙

## URL 설계
- 리소스는 복수형 명사 사용: `/api/users`, `/api/posts`
- 계층 구조 표현: `/api/users/{userId}/posts`
- 소문자 사용, 단어 구분은 하이픈(-) 사용

## HTTP 메서드
- **GET**: 조회
- **POST**: 생성
- **PUT**: 전체 수정
- **PATCH**: 부분 수정
- **DELETE**: 삭제

## 응답 코드
- **200 OK**: 성공 (조회, 수정)
- **201 Created**: 생성 성공
- **204 No Content**: 삭제 성공
- **400 Bad Request**: 잘못된 요청
- **404 Not Found**: 리소스 없음
- **500 Internal Server Error**: 서버 오류

## API 엔드포인트 예시
```
GET    /api/users          - 전체 사용자 조회
GET    /api/users/{id}     - 특정 사용자 조회
POST   /api/users          - 사용자 생성
PUT    /api/users/{id}     - 사용자 수정
DELETE /api/users/{id}     - 사용자 삭제
```

# 데이터베이스 규칙

## 테이블 네이밍
- 소문자, 단수형 사용 (예: user, post)
- 복합어는 언더스코어(_) 사용 (예: user_profile)

## 컬럼 네이밍
- 소문자, 스네이크 케이스 사용 (예: user_name, created_at)
- Primary Key: `id` (Long 타입)
- 생성일시: `created_at`
- 수정일시: `updated_at`

## JPA 규칙
- Entity 클래스명과 테이블명 매핑: `@Table(name = "user")`
- 필드명은 camelCase, 컬럼명은 snake_case 자동 변환
- 연관관계는 명시적으로 설정 (`@ManyToOne`, `@OneToMany` 등)

# 예외 처리

## 공통 예외 처리
- `@RestControllerAdvice`로 전역 예외 처리
- 커스텀 예외 클래스 생성 (예: `ResourceNotFoundException`)
- 일관된 에러 응답 형식 사용

## 에러 응답 형식
```json
{
  "timestamp": "2024-01-11T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 1",
  "path": "/api/users/1"
}
```

# 트랜잭션 관리

- Service 계층에서 `@Transactional` 사용
- 읽기 전용 작업: `@Transactional(readOnly = true)`
- 예외 발생 시 자동 롤백

# 테스트 규칙

## 테스트 구조
- 단위 테스트: Service 계층 로직 테스트
- 통합 테스트: Controller + Service + Repository 테스트
- 테스트 메서드명: `should_ExpectedBehavior_When_StateUnderTest` 형식

## 사용 도구
- JUnit 5
- Mockito (모킹)
- MockMvc (Controller 테스트)
- @SpringBootTest (통합 테스트)

# 보안 규칙

- 민감한 정보는 환경 변수 또는 application.yml에서 관리 (application.yml은 .claudeignore에 포함)
- SQL Injection 방지: JPA 파라미터 바인딩 사용
- 입력값 검증: `@Valid`, `@NotNull`, `@NotBlank` 등 활용

## 설정 파일 관리
- `application.yml`: 실제 설정 (Git 및 Claude 접근 금지)
- `application.yml.example`: 설정 예시 템플릿 (샘플 값만 포함)

# 기타 규칙

## 주석
- 복잡한 비즈니스 로직에만 주석 작성
- JavaDoc은 public API에만 작성
- 코드로 설명 가능한 부분은 주석 지양

## Git 커밋 메시지
- feat: 새로운 기능 추가
- fix: 버그 수정
- refactor: 리팩토링
- docs: 문서 수정
- test: 테스트 코드 추가

## 의존성 관리
- 필요한 라이브러리만 추가
- 버전 명시 (Spring Boot 관리 의존성 제외)
- 사용하지 않는 의존성은 제거

## Build Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "com.dav1n9.claudetest.ClaudeTestApplicationTests"

# Run a specific test method
./gradlew test --tests "com.dav1n9.claudetest.ClaudeTestApplicationTests.contextLoads"

# Clean build
./gradlew clean build
```

## Architecture

This is a Spring Boot 4.0.1 web application using Gradle.

- **Java version**: 17
- **Base package**: `com.dav1n9.claudetest`
- **Main class**: `ClaudeTestApplication`
- **Framework**: Spring Boot with Spring MVC (webmvc starter)
- **Lombok**: Available for reducing boilerplate code

### Project Structure

- `src/main/java` - Application source code
- `src/main/resources` - Configuration files (application.properties)
- `src/test/java` - Test classes using JUnit 5 and Spring Boot Test