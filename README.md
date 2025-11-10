# 양자 회로 시뮬레이터

## 학습 목표

- 객체지향 설계: 양자역학의 복잡한 도메인을 명확한 책임과 협력 관계로 표현한다
- TDD 실천: 클래스와 함수에 대한 단위 테스트를 통해 의도한 대로 정확하게 작동하는 영역을 확보한다
- Red-Green-Refactor: TDD 사이클을 체화한다
- 도메인 주도 설계: 추상적인 양자역학 개념을 구체적인 도메인 객체로 구현한다

## 프로젝트 배경

### 개인적 의미
- 군 복무 중 『퀀텀 스토리』를 읽으며 양자역학에 매료되어 소프트웨어학부로 편입
- 편입 후 코딩 능력과 소프트웨어 공학 역량을 쌓아온 과정
- **원점 회귀**: 나를 이 길로 이끌어준 양자역학 도메인을, 내가 갖춘 개발 실력으로 직접 구현

### 시의성
2025년 노벨 물리학상은 John Clarke, Michel Devoret, John Martinis에게 수여되었습니다.
- 업적: 조셉슨 접합을 이용한 초전도 전기 회로에서 거시적 양자 터널링과 에너지 양자화 발견 (1984-1985)
- 의의: 초전도 양자 비트(qubit) 개발의 토대

## 기능 요구사항

### 1. 양자 회로 초기화
- n개의 큐비트로 초기화된 양자 회로를 생성할 수 있다 (1 ≤ n ≤ 10)
- 모든 큐비트는 초기 상태 |0⟩으로 시작한다

### 2. 단일 큐비트 게이트 적용
- X(Pauli-X) 게이트를 특정 큐비트에 적용할 수 있다
    - |0⟩ ↔ |1⟩ 상태 반전
- H(Hadamard) 게이트를 특정 큐비트에 적용할 수 있다
    - 중첩 상태 생성 (superposition)
- Z(Pauli-Z) 게이트를 특정 큐비트에 적용할 수 있다
    - 위상 변화

### 3. 다중 큐비트 게이트 적용
- CNOT 게이트를 두 큐비트에 적용할 수 있다
    - 제어 큐비트(control)와 타겟 큐비트(target) 지정
    - 제어 큐비트가 |1⟩일 때만 타겟 큐비트 반전

### 4. 회로 실행 및 측정
- 구성된 회로를 실행하여 각 큐비트의 양자 상태를 계산한다
- 특정 큐비트를 측정하여 0 또는 1의 결과를 얻는다
- 측정 전 각 큐비트가 |1⟩ 상태일 확률을 계산할 수 있다

### 5. 회로 시각화
- 구성된 회로를 ASCII 아트 형태로 출력한다
  ```
  Q0: ─H─●─
         │
  Q1: ───X─
  ```
- 각 Step에서 어떤 게이트가 어느 큐비트에 적용되는지 확인한다
- 큐비트별로 적용된 게이트를 시간 순서대로 볼 수 있다

### 6. 상태 시각화
- 각 큐비트의 |0⟩, |1⟩ 상태 확률을 표시한다
- 확률을 막대그래프 형태로 시각화한다
  ```
  State |00⟩: ██████████████████████████████ 50.0%
  State |11⟩: ██████████████████████████████ 50.0%
  ```
- 여러 번 측정 시 결과의 히스토그램을 출력한다

### 7. Bell State 생성 및 검증 (선택)
- Hadamard + CNOT 조합으로 얽힘(entanglement) 상태 생성
- 두 큐비트가 항상 같은 값으로 측정됨을 확인

## 예외 상황

### 입력 검증
- 큐비트 개수가 1~10 범위를 벗어나면 예외 발생
- 큐비트 인덱스가 범위를 벗어나면 예외 발생
- CNOT 게이트의 제어 큐비트와 타겟 큐비트가 같으면 예외 발생
- 게이트가 null이면 예외 발생

### 에러 메시지
- 모든 에러 메시지는 [ERROR]로 시작한다
- IllegalArgumentException을 사용한다

## 실행 결과 예시

```
=== Bell State Circuit ===
Quantum Circuit (2 qubits, 2 steps)

Q0: ─H─●─
       │
Q1: ───X─

Step 1: H(Q0)
Step 2: CNOT(Q0→Q1)

=== Quantum State Probabilities ===
State |00⟩: ██████████████████████████████ 50.0%
State |01⟩: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░  0.0%
State |10⟩: ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░  0.0%
State |11⟩: ██████████████████████████████ 50.0%

Individual Qubit Probabilities:
Qubit 0 → |0⟩: 50.0% |1⟩: 50.0%
Qubit 1 → |0⟩: 50.0% |1⟩: 50.0%

=== Single Measurement ===
Qubit 0: 1
Qubit 1: 1
Entangled: Yes
```

## 구현할 기능 목록

### 도메인 기능

#### 1. 큐비트 인덱스 관리 (QubitIndex)
- [ ] 큐비트 인덱스는 0 이상이어야 한다
- [ ] 큐비트 인덱스 범위 검증 기능을 제공한다
- [ ] 동일한 인덱스인지 비교한다

#### 2. 확률 관리 (Probability)
- [ ] 확률은 0.0 이상 1.0 이하여야 한다
- [ ] 확률 값을 백분율로 변환한다
- [ ] 두 확률을 비교한다

#### 3. 양자 게이트 인터페이스 (QuantumGate)
- [ ] 양자 상태에 게이트를 적용한다
- [ ] 게이트가 작용하는 큐비트 개수를 반환한다
- [ ] 게이트 이름을 반환한다
- [ ] 게이트가 작용하는 큐비트 인덱스를 반환한다

#### 4. 단일 큐비트 게이트 (PauliXGate, HadamardGate, PauliZGate)
- [ ] X 게이트는 큐비트 상태를 반전시킨다
- [ ] H 게이트는 중첩 상태를 생성한다
- [ ] Z 게이트는 위상을 변경한다
- [ ] 각 게이트는 타겟 큐비트 인덱스를 가진다

#### 5. 다중 큐비트 게이트 (CNOTGate)
- [ ] CNOT 게이트는 제어 큐비트와 타겟 큐비트를 가진다
- [ ] 제어 큐비트와 타겟 큐비트는 서로 달라야 한다
- [ ] 제어 큐비트가 |1⟩일 때만 타겟 큐비트를 반전시킨다

#### 6. 회로 Step 관리 (CircuitStep)
- [ ] 하나의 Step은 여러 게이트를 포함할 수 있다
- [ ] Step의 게이트들은 동시에 적용된다
- [ ] 빈 Step은 허용하지 않는다
- [ ] Step의 게이트 개수를 반환한다

#### 7. 양자 회로 (QuantumCircuit)
- [ ] 회로는 큐비트 개수를 가진다
- [ ] 회로는 여러 Step으로 구성된다
- [ ] 회로를 실행하여 양자 상태를 계산한다
- [ ] 회로의 총 게이트 개수를 계산한다
- [ ] 회로의 깊이(depth)를 계산한다

#### 8. 양자 상태 (QuantumState)
- [ ] 양자 상태는 큐비트 개수를 가진다
- [ ] 특정 큐비트의 |1⟩ 확률을 계산한다
- [ ] 특정 큐비트를 측정하여 0 또는 1을 반환한다
- [ ] 모든 가능한 상태의 확률 분포를 반환한다

#### 9. 측정 결과 (MeasurementResult)
- [ ] 측정 결과는 0 또는 1이다
- [ ] 측정 결과를 정수로 변환한다
- [ ] 측정 결과를 문자열로 변환한다

#### 10. 회로 빌더 (QuantumCircuitBuilder)
- [ ] 큐비트 개수를 지정한다
- [ ] Step을 추가한다
- [ ] 회로를 생성한다
- [ ] 큐비트 개수가 지정되지 않으면 예외 발생
- [ ] Step이 없으면 예외 발생

### 시각화 기능

#### 11. 회로 시각화 (CircuitVisualizer)
- [ ] 회로를 ASCII 아트로 변환한다
- [ ] Step별 상세 설명을 생성한다
- [ ] 회로 요약 정보를 생성한다 (큐비트 수, Step 수, 깊이 등)

#### 12. 상태 시각화 (StateVisualizer)
- [ ] 각 상태의 확률을 막대그래프로 표시한다
- [ ] 개별 큐비트의 확률을 표시한다
- [ ] 확률 분포를 시각화한다

#### 13. 측정 통계 (MeasurementHistogram)
- [ ] 여러 번 측정하여 통계를 수집한다
- [ ] 각 결과의 출현 횟수를 계산한다
- [ ] 히스토그램 형태로 시각화한다
- [ ] 기대값과 실제 측정값의 편차를 표시한다

### 입출력 기능

#### 14. 입력 처리 (InputView)
- [ ] 큐비트 개수를 입력받는다
- [ ] 게이트 종류를 입력받는다
- [ ] 게이트가 적용될 큐비트 인덱스를 입력받는다

#### 15. 출력 처리 (OutputView)
- [ ] 회로 구성을 출력한다
- [ ] 양자 상태를 출력한다
- [ ] 측정 결과를 출력한다
- [ ] 에러 메시지를 출력한다

## 패키지 구조

```
quantum.circuit
├── Application.java
├── QuantumCircuitGame.java
├── domain
│   ├── gate
│   │   ├── QuantumGate.java (interface)
│   │   ├── PauliXGate.java
│   │   ├── HadamardGate.java
│   │   ├── PauliZGate.java
│   │   └── CNOTGate.java
│   ├── circuit
│   │   ├── QubitIndex.java
│   │   ├── CircuitStep.java
│   │   ├── QuantumCircuit.java
│   │   └── QuantumCircuitBuilder.java
│   ├── state
│   │   ├── QuantumState.java
│   │   ├── Probability.java
│   │   └── MeasurementResult.java (enum)
│   └── statistics
│       └── MeasurementHistogram.java
├── visualizer
│   ├── CircuitVisualizer.java
│   └── StateVisualizer.java
└── view
    ├── InputView.java
    └── OutputView.java
```

## 프로그래밍 요구사항

### 코딩 컨벤션
- Java 코드 컨벤션을 지키며 프로그래밍한다
- indent(인덴트, 들여쓰기) depth를 2 이하로 제한한다
- 3항 연산자를 쓰지 않는다
- else 예약어를 쓰지 않는다
- 함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 만든다
- 함수(또는 메서드)의 길이가 15라인을 넘어가지 않도록 구현한다

### 객체지향 설계
- 원시값을 포장한다 (QubitIndex, Probability)
- 일급 컬렉션을 사용한다 (CircuitStep의 gates)
- Enum을 적용한다 (MeasurementResult)
- 도메인 로직과 UI 로직을 분리한다

### 테스트
- JUnit 5와 AssertJ를 이용하여 테스트 코드를 작성한다
- 단위 테스트 작성 (각 클래스와 메서드)
- 테스트 커버리지 80% 이상
- 확률적 결과 테스트 전략 수립
    - 예: 1000회 측정 시 허용 오차 범위 설정

### 디자인 패턴
- Strategy 패턴: 각 게이트를 전략으로 표현
- Composite 패턴: Step과 Circuit의 계층 구조
- Builder 패턴: 복잡한 회로 구성
- 불변 객체 활용

### 라이브러리
- Strange 양자 컴퓨팅 라이브러리 (`org.redfx:strange:0.1.3`)를 활용한다
- 내부 구현은 Strange를 활용하되, 도메인 객체는 직접 설계한다

## 참고 자료

- [Nobel Prize 2025 Physics - 공식 발표](https://www.nobelprize.org/prizes/physics/2025/)
- [Strange 라이브러리 - GitHub](https://github.com/redfx-quantum/strange)
- [Quantum Computing in Action - Manning Publications](https://www.manning.com/books/quantum-computing-in-action)
- [『퀀텀 스토리』 - 짐 배것 (반니)](https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=35000923)
