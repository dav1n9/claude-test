# Claude Test

Spring Boot 기반 CRUD REST API 프로젝트입니다.

## 기술 스택

- Java 17
- Spring Boot 3.2.x
- Spring Data JPA
- H2 Database (개발환경)
- MySQL (운영환경)
- Lombok
- Gradle

## 브랜치 전략

```
main (프로덕션)
 ↑
stage (스테이징)
 ↑
develop (개발)
 ↑
feature/* (기능 개발)
```

### 브랜치 설명

| 브랜치 | 용도 |
|--------|------|
| `main` | 프로덕션 배포용 |
| `stage` | 스테이징 환경 테스트 |
| `develop` | 개발 통합 브랜치 |
| `feature/*` | 기능 개발 브랜치 |

### 브랜치 워크플로우

1. **기능 개발**: `develop`에서 `feature/기능명` 브랜치 생성
2. **개발 완료**: `feature/*` → `develop` 병합
3. **테스트 준비**: `develop` → `stage` 병합
4. **배포 준비**: `stage` → `main` 병합

## 빌드 및 실행

```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun

# 테스트
./gradlew test
```
