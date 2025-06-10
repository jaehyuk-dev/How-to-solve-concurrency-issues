# 재고시스템으로 알아보는 동시성 이슈 해결방법

인프런 강의 [**재고시스템으로 알아보는 동시성이슈 해결방법**](https://www.inflearn.com/course/%EB%8F%99%EC%8B%9C%EC%84%B1%EC%9D%B4%EC%8A%88-%EC%9E%AC%EA%B3%A0%EC%8B%9C%EC%8A%A4%ED%85%9C)을 수강하며 실습한 코드입니다.

---

## 📌 프로젝트 개요

동시성 이슈가 무엇인지 알아보고, 이를 해결하는 다양한 방법들을 간단한 재고시스템을 통해 학습합니다.

---

## 🛠 기술 스택

- Java 17  
- Spring Boot 3.5.0  
- Spring Data JPA  
- MySQL  
- Redis  
- JUnit 5  

---

## ❓ 동시성 이슈란?

여러 개의 스레드가 동시에 하나의 **공유 자원**에 접근할 때 발생하는 문제입니다.  
예: 재고가 1개 남은 상품에 대해 동시에 2명이 주문을 하면 재고가 **음수**가 될 수 있음.

---

## ✅ 해결 방법들

### 1. `Synchronized` 사용

Java의 `synchronized` 키워드를 이용하여 메서드나 블록을 동기화합니다.

- **장점:** 구현이 간단함  
- **단점:** 서버가 여러 대인 경우 동작하지 않음  

---

### 2. Database Lock 활용

#### 🔒 Pessimistic Lock (비관적 락)

실제로 데이터에 Lock을 걸어 정합성을 유지합니다.

- **장점:** 충돌이 빈번한 경우에 적합하며 안정적  
- **단점:** 별도의 Lock으로 인해 성능 저하 가능성  

#### 🔓 Optimistic Lock (낙관적 락)

버전 필드를 이용한 충돌 감지 방식. Lock을 사용하지 않음.

- **장점:** Lock이 없으므로 성능 우수  
- **단점:** 충돌 발생 시 재시도 로직을 개발자가 작성해야 함  

---

### 3. Redis 기반 분산 Lock

#### 🍃 Lettuce 사용

`SETNX` 명령어를 활용한 Spin Lock 방식

- **장점:** 분산 환경에서도 동작  
- **단점:** Spin 방식으로 Redis 부하 가능성  

#### 🟥 Redisson 사용

`pub/sub` 기반 Lock 제공

- **장점:** Lettuce보다 효율적이며 구현 편의성 높음  
- **단점:** 외부 라이브러리에 의존  

---

## ⚙ 환경 설정

### MySQL 설정 (application.yml)

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/stock_example
    username: root
    password: 1234
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
```

### Redis 설정 (application.yml)

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

---

## ▶ 실행 방법

### 1. MySQL 실행 및 데이터베이스 생성

```bash
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=1234 -d -p 3306:3306 mysql:latest

# MySQL 접속 후
mysql -u root -p
CREATE DATABASE stock_example;
```

### 2. Redis 실행

```bash
docker run --name redis-container -d -p 6379:6379 redis
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 4. 테스트 실행

```bash
./gradlew test
```

100개의 스레드가 동시에 재고를 1씩 감소시키는 테스트가 포함되어 있습니다.

---

## 📊 성능 비교

| 방법               | 처리 시간 | 장점                     | 단점                       |
|------------------|-----------|--------------------------|----------------------------|
| Synchronized     | 빠름      | 구현 간단                | 단일 서버에서만 동작       |
| Pessimistic Lock | 보통      | 안정적                   | 성능 저하 가능성           |
| Optimistic Lock  | 빠름      | 성능 우수                | 재시도 로직 필요           |
| Lettuce          | 느림      | 분산 환경 지원           | Redis 부하                 |
| Redisson         | 보통      | 효율적, 구현 편의성 높음 | 라이브러리 의존성 존재     |

---

## 📚 학습 내용 정리

이 프로젝트를 통해 다음을 학습할 수 있습니다:

- 동시성 이슈가 발생하는 상황과 원인  
- Java의 `synchronized`를 이용한 동시성 제어  
- 데이터베이스 Lock을 활용한 동시성 제어 (`Pessimistic`, `Optimistic`)  
- Redis를 활용한 분산 Lock 구현 (`Lettuce`, `Redisson`)  
- 각 방법의 장단점과 적용 상황 비교  

---

## 🔗 참고 자료

- 인프런 강의 링크: [재고시스템으로 알아보는 동시성이슈 해결방법](https://www.inflearn.com/course/%EB%8F%99%EC%8B%9C%EC%84%B1%EC%9D%B4%EC%8A%88-%EC%9E%AC%EA%B3%A0%EC%8B%9C%EC%8A%A4%ED%85%9C)  
- 강사: **최상용**  

> 이 프로젝트는 학습 목적으로 작성되었습니다.
