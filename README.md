# 양자 회로 시뮬레이터

## 학습 목표

- **객체지향 설계**: 복잡한 양자역학 도메인을 명확한 책임과 협력 관계로 표현한다
- **TDD 실천**: 클래스와 함수에 대한 단위 테스트를 통해 의도한 대로 정확하게 작동하는 영역을 확보한다
- **Red-Green-Refactor**: TDD 사이클을 체화한다
- **디자인 패턴 활용**: Strategy, Template Method, Chain of Responsibility, Observer, Factory, Composite, Facade, Builder 패턴을 실전에 적용한다
- **도메인 주도 설계**: 추상적인 양자역학 개념을 구체적인 도메인 객체로 구현한다

## 왜 이 프로젝트인가?

### 원점으로의 회귀

군 복무 중 읽은 『퀀텀 스토리』 한 권이 제 진로를 바꿨습니다. 양자역학의 100년 역사를 따라가며, 특히 아인슈타인과 보어의 논쟁을 읽을 때 가장 몰입했습니다. 그 과정에서 제가 정답을 찾는 것보다 논리를 쌓아가는 과정 자체를 더 좋아하는 사람이라는 것을 알게 되었고, 양자 컴퓨팅이라는 분야를 발견하며 소프트웨어학부로의 편입을 결심했습니다.

편입 후 알고리즘 대회 본선 진출, 다양한 프로젝트 경험을 거치며 코딩 능력은 성장했지만, 작은 프로젝트를 진행하며 한계를 느꼈습니다. 코드 규모가 커지자 예상치 못한 오류가 늘고, 요구사항 변경에 대처하기 어려웠습니다. 제가 가진 것은 '코딩 능력'이지, 제대로 된 소프트웨어를 만드는 '공학적 역량'이 아니라는 것을 깨달았습니다.

우아한테크코스의 1-3주차 미션을 통해 변경에 유연한 설계, 명확한 책임과 협력 관계, 테스트 가능한 구조 같은 설계 원칙들을 처음 제대로 접했습니다. 이제 나를 이 길로 이끌어준 양자역학 도메인으로 돌아가, 지금까지 배운 설계 역량으로 직접 구현해보고 싶었습니다.

### 시의성: 2025 노벨 물리학상

2025년 노벨 물리학상이 초전도 양자 비트(qubit) 개발의 토대가 된 연구에 수여되었습니다.
- **수상자**: John Clarke, Michel Devoret, John Martinis
- **업적**: 조셉슨 접합을 이용한 초전도 전기 회로에서 거시적 양자 터널링과 에너지 양자화 발견 (1984-1985)
- **의의**: 현대 양자 컴퓨터의 핵심 기술인 초전도 큐비트 개발의 과학적 기반

이 소식은 큰 충격이었습니다. 40년 전의 발견이 오늘날 양자 컴퓨팅 혁명의 토대가 되었다는 사실이, 제가 읽었던 『퀀텀 스토리』의 100년 역사가 현재진행형임을 다시 한번 일깨워주었기 때문입니다. 이 시의성은 이 프로젝트를 시작하는 또 하나의 강력한 동기가 되었습니다.

### 기술적 도전: 복잡한 도메인, 깊은 협력

이 프로젝트가 단순한 시뮬레이터를 넘어 **4가지 모드**를 구현하는 이유는, 1-3주차 미션에서 배운 설계 원칙들을 더 복잡하고 현실적인 상황에서 적용해보고 싶었기 때문입니다.

**1-3주차 미션에서 배운 것:**
- Builder 패턴으로 복잡한 객체 생성
- Strategy 패턴으로 알고리즘 교체
- 일급 컬렉션으로 책임 분리
- TDD로 안정적인 리팩토링

**오픈미션에서 도전하는 것:**
- **자유 모드**: Builder 패턴의 심화 (회로 구성)
- **알고리즘 모드**: Template Method + Factory 패턴 (공통 흐름 추상화)
- **최적화 모드**: Strategy + Chain of Responsibility + Composite + Facade (4-5단계 협력)
- **벤치마크 모드**: Observer 패턴 (이벤트 기반 설계)

특히 **최적화 모드**는 제가 가장 고민한 부분입니다. 단순히 최적화 알고리즘을 구현하는 것을 넘어, 여러 최적화 전략을 조합하는 파이프라인(Composite), 순차적으로 검증하는 체인(Chain of Responsibility), 복잡한 분석을 단순한 인터페이스로 제공하는 Facade까지. 이 과정에서 **"어떻게 하면 변경에 유연하면서도 명확한 책임을 가진 설계를 할 수 있을까?"**라는 1-3주차 코드리뷰에서 고민했던 질문들을 직접 실천했습니다.

### 1-3주차 미션과의 연결

1-3주차 미션에서 저는 코드리뷰를 통해 많은 것을 배웠습니다. 단순히 동작하는 코드를 넘어, **"왜 이렇게 설계했는가?"**에 대한 깊은 고민이 필요하다는 것을요. 설계에는 정답이 없고, 항상 트레이드오프가 존재한다는 것도 배웠습니다.

이 프로젝트는 그 배움의 연장선입니다:
- **로또 미션**의 일급 컬렉션 → CircuitStep의 게이트 관리
- **자동차 경주**의 전략 패턴 → 최적화 전략들
- **페어 매칭**의 검증 로직 → 회로 검증 체인

하지만 단순한 확장이 아닙니다. 양자역학이라는 **비직관적이고 추상적인 도메인**을 명확한 객체로 표현하는 과정에서, 책임과 협력의 본질을 더 깊이 이해할 수 있었습니다. 큐비트의 중첩 상태, 얽힘, 측정에 따른 붕괴 같은 개념들을 QubitIndex, Probability, QuantumState 같은 도메인 객체로 구체화하며, **"좋은 도메인 설계란 무엇인가?"**에 대한 답을 찾아가는 과정이었습니다.

### 목표: 본질을 이해하는 개발자

이 프로젝트를 통해 증명하고 싶은 것은 세 가지입니다:

1. **복잡한 도메인도 명확한 설계로 풀어낼 수 있다**
  - 양자역학의 추상적 개념을 구체적인 객체로 표현
  - 4-5단계 깊이의 협력 구조를 명확한 책임으로 구성

2. **설계 원칙과 패턴을 실전에서 적용할 수 있다**
  - 8가지 디자인 패턴의 적절한 사용 시기 판단
  - 트레이드오프를 이해하고 맥락에 맞는 선택

3. **TDD로 안정적인 리팩토링이 가능하다**
  - 확률적 결과를 테스트하는 전략 수립
  - Red-Green-Refactor 사이클을 통한 점진적 개선

단순히 작동하는 코드를 넘어, **동료들과 명확하게 소통하고 깊은 신뢰를 주는 견고한 소프트웨어**를 만들고 싶습니다. 그것이 제가 우아한테크코스에서 배우고 싶은, 소프트웨어 공학의 본질입니다.

## 모드 구성

### 1. 자유 모드 (Free Mode)
사용자가 원하는 대로 양자 회로를 구성하고 실험할 수 있는 샌드박스 모드

**기능:**
- 제약 없이 회로 구성
- 게이트 조합 자유롭게 테스트
- 실시간 상태 확인

### 2. 알고리즘 라이브러리 모드 (Algorithm Library Mode)
대표적인 양자 알고리즘을 선택하여 실행하는 모드

**구현 알고리즘:**
- Bell State: 2큐비트 얽힘 상태 생성
- GHZ State: 3큐비트 얽힘 상태 생성
- Quantum Fourier Transform (QFT): 양자 푸리에 변환
- Grover's Algorithm: 양자 검색 알고리즘
- Deutsch-Jozsa Algorithm: 양자 오라클 문제

**협력 구조:**
```
AlgorithmFactory → QuantumAlgorithm (Template Method)
    ↓                  ↓
ParameterValidator   CircuitBuilder
    ↓
ExecutionEngine
```

### 3. 최적화 모드 (Optimization Mode)
사용자가 구성한 회로를 분석하고 최적화하는 모드

**기능:**
- 회로 최적화
  - 중복 게이트 제거 (H-H, X-X 상쇄)
  - 게이트 융합 (연속 게이트 결합)
  - Identity 게이트 제거

- 회로 분석
  - 회로 깊이(Depth) 계산
  - 게이트 개수 통계
  - 복잡도 분석
  - 얽힘 정도 측정

- 회로 검증
  - 큐비트 범위 검증
  - 게이트 호환성 검증
  - 깊이 제한 검증
  - 리소스 제한 검증

**협력 구조:**
```
OptimizationPipeline → [RedundantGateRemover, IdentityGateRemover, GateFusionOptimizer]
    ↓
CircuitAnalyzer → [CircuitDepth, GateCount, Complexity, EntanglementDegree]
    ↓
ValidationChain → [QubitRangeValidator, GateCompatibilityValidator, DepthLimitValidator, ResourceValidator]
    ↓
OptimizationReport
```

### 4. 벤치마크 모드 (Benchmark Mode)
여러 알고리즘 또는 최적화 전후를 비교하는 모드

**기능:**
- 알고리즘 성능 비교
- 최적화 전후 비교
- 실행 시간 측정
- 리소스 사용량 분석

**협력 구조:**
```
BenchmarkRunner → PerformanceMonitor (Observer)
    ↓                  ↓
CircuitComparator   ResultCollector
    ↓
ComparisonReport / BenchmarkReport
```

## 기능 요구사항

### 1. 기본 회로 구성 (자유 모드)

#### 1.1 양자 회로 초기화
- n개의 큐비트로 초기화된 양자 회로를 생성할 수 있다 (1 ≤ n ≤ 10)
- 모든 큐비트는 초기 상태 |0⟩으로 시작한다

#### 1.2 단일 큐비트 게이트 적용
- X(Pauli-X) 게이트를 특정 큐비트에 적용할 수 있다
  - |0⟩ ↔ |1⟩ 상태 반전
- H(Hadamard) 게이트를 특정 큐비트에 적용할 수 있다
  - 중첩 상태 생성 (superposition)
- Z(Pauli-Z) 게이트를 특정 큐비트에 적용할 수 있다
  - 위상 변화

#### 1.3 다중 큐비트 게이트 적용
- CNOT 게이트를 두 큐비트에 적용할 수 있다
  - 제어 큐비트(control)와 타겟 큐비트(target) 지정
  - 제어 큐비트가 |1⟩일 때만 타겟 큐비트 반전

#### 1.4 회로 실행 및 측정
- 구성된 회로를 실행하여 각 큐비트의 양자 상태를 계산한다
- 특정 큐비트를 측정하여 0 또는 1의 결과를 얻는다
- 측정 전 각 큐비트가 |1⟩ 상태일 확률을 계산할 수 있다

#### 1.5 회로 시각화
- 구성된 회로를 ASCII 아트 형태로 출력한다
- 각 Step에서 어떤 게이트가 어느 큐비트에 적용되는지 확인한다
- 큐비트별로 적용된 게이트를 시간 순서대로 볼 수 있다

#### 1.6 상태 시각화
- 각 큐비트의 |0⟩, |1⟩ 상태 확률을 표시한다
- 확률을 백분율로 표시한다

### 2. 알고리즘 라이브러리 (알고리즘 모드)

#### 2.1 알고리즘 팩토리
- 알고리즘 이름으로 적절한 알고리즘 객체를 생성한다
- 지원하지 않는 알고리즘에 대해 예외를 발생시킨다

#### 2.2 양자 알고리즘 템플릿
- 모든 알고리즘은 공통 실행 흐름을 따른다
  - 초기 상태 준비
  - 메인 알고리즘 적용
  - 측정 준비
- 각 알고리즘은 메인 로직만 구현한다 (Template Method)

#### 2.3 알고리즘별 파라미터 검증
- 각 알고리즘은 필요한 큐비트 개수가 다르다
- 파라미터가 유효하지 않으면 예외를 발생시킨다

#### 2.4 알고리즘 실행 결과
- 생성된 회로를 반환한다
- 알고리즘 설명을 제공한다
- 예상 결과를 제공한다

### 3. 회로 최적화 및 분석 (최적화 모드)

#### 3.1 회로 최적화
- **중복 게이트 제거**: 연속된 동일 게이트 상쇄 (H-H, X-X)
- **게이트 융합**: 연속 회전 게이트를 하나로 결합
- **Identity 게이트 제거**: 효과 없는 게이트 제거

#### 3.2 최적화 파이프라인
- 여러 최적화 규칙을 순차적으로 적용한다
- 각 단계의 결과를 추적한다
- 최적화 전후 비교 리포트를 생성한다

#### 3.3 회로 분석
- **회로 깊이**: 가장 긴 게이트 체인의 길이
- **게이트 개수**: 총 게이트 수 및 종류별 통계
- **복잡도**: 회로의 계산 복잡도 추정
- **얽힘 정도**: 큐비트 간 얽힘 수준 측정

#### 3.4 회로 검증
- **큐비트 범위**: 모든 게이트가 유효한 큐비트에 적용되는지 확인
- **게이트 호환성**: 게이트 조합이 물리적으로 가능한지 확인
- **깊이 제한**: 회로 깊이가 제한을 초과하지 않는지 확인
- **리소스 제한**: 회로가 하드웨어 제약을 만족하는지 확인

#### 3.5 최적화 결과 리포트
- 최적화 전후 회로 비교
- 게이트 수 감소율
- 깊이 감소율
- 예상 성능 향상

### 4. 벤치마크 및 비교 (벤치마크 모드)

#### 4.1 알고리즘 벤치마크
- 여러 알고리즘의 성능을 비교한다
- 실행 시간, 게이트 수, 깊이 등을 측정한다
- 벤치마크 결과를 시각화한다

#### 4.2 최적화 효과 측정
- 최적화 전후 회로를 비교한다
- 각 최적화 규칙의 효과를 측정한다
- 최적화 파이프라인의 효율성을 평가한다

#### 4.3 실행 추적
- 회로 실행 과정을 단계별로 추적한다 (Observer)
- 성능 병목 지점을 식별한다

#### 4.4 비교 리포트
- 여러 회로/알고리즘의 성능을 표 형태로 비교한다
- 최적 선택을 추천한다
- 개선 방향을 제시한다

## 예외 상황

### 입력 검증
- 큐비트 개수가 1~10 범위를 벗어나면 예외 발생
- 큐비트 인덱스가 범위를 벗어나면 예외 발생
- CNOT 게이트의 제어 큐비트와 타겟 큐비트가 같으면 예외 발생
- 게이트가 null이면 예외 발생
- 지원하지 않는 알고리즘 이름이면 예외 발생
- 알고리즘에 필요한 큐비트 개수가 부족하면 예외 발생

### 에러 메시지
- 모든 에러 메시지는 [ERROR]로 시작한다
- IllegalArgumentException을 사용한다

## 실행 결과 예시

### 자유 모드
```
===================================
Quantum Circuit Simulator
===================================
모드를 선택하세요:
1. 자유 모드 (Free Mode)
2. 알고리즘 라이브러리 (Algorithm Library)
3. 최적화 모드 (Optimization Mode)
4. 벤치마크 모드 (Benchmark Mode)
선택:
> 1

큐비트 개수를 입력하세요:
2
(큐비트 인덱스: 0부터 1까지 사용 가능)
게이트 종류를 입력하세요 (X, H, Z, CNOT):
H
타겟 큐비트 인덱스를 입력하세요 (0부터 시작):
0
게이트를 더 추가하시겠습니까? (y/n):
y
게이트 종류를 입력하세요 (X, H, Z, CNOT):
CNOT
제어 큐비트 인덱스를 입력하세요 (0부터 시작):
0
타겟 큐비트 인덱스를 입력하세요 (0부터 시작):
1
게이트를 더 추가하시겠습니까? (y/n):
n

===================================
=== Quantum Circuit ===
Quantum Circuit (2 qubits, 2 steps)

Q0: ─H─●─
       │
Q1: ───X─

Step 1: H(Q0)
Step 2: CNOT(Q0→Q1)

=== Quantum State ===
Qubit 0 → |0⟩: 50.0% |1⟩: 50.0%
Qubit 1 → |0⟩: 50.0% |1⟩: 50.0%
===================================
```

### 알고리즘 라이브러리 모드
```
=== 알고리즘 라이브러리 ===
사용 가능한 알고리즘:
1. Bell State (2 qubits)
2. GHZ State (3 qubits)
3. Quantum Fourier Transform (2-4 qubits)
4. Grover's Algorithm (2-3 qubits)
5. Deutsch-Jozsa Algorithm (2 qubits)

알고리즘을 선택하세요:
> 1

큐비트 개수를 입력하세요:
2

=== Bell State Algorithm ===
설명: 2큐비트 최대 얽힘 상태를 생성합니다.
예상 결과: |00⟩과 |11⟩이 각각 50% 확률

===================================
=== Generated Circuit ===
Q0: ─H─●─
       │
Q1: ───X─

Step 1: H(Q0)
Step 2: CNOT(Q0→Q1)

=== Execution Result ===
Qubit 0 → |0⟩: 50.0% |1⟩: 50.0%
Qubit 1 → |0⟩: 50.0% |1⟩: 50.0%
✓ Bell State 생성 완료!
===================================
```

### 최적화 모드
```
=== 최적화 모드 ===
회로를 구성하거나 불러오세요.

큐비트 개수: 2
게이트: H-H-X-X-CNOT(0→1)

원본 회로:
Q0: ─H─H─X─X─●─
             │
Q1: ─────────X─

=== 최적화 시작 ===
[1/3] 중복 게이트 제거 중...
  - H-H 제거 (Q0)
  - X-X 제거 (Q0)
  게이트 수: 5 → 1

[2/3] 게이트 융합 중...
  변경 사항 없음

[3/3] Identity 게이트 제거 중...
  변경 사항 없음

=== 최적화 완료 ===
최적화된 회로:
Q0: ─●─
     │
Q1: ─X─

=== 최적화 리포트 ===
게이트 수: 5 → 1 (80% 감소)
회로 깊이: 4 → 1 (75% 감소)
예상 실행 시간: 100μs → 20μs (80% 단축)

적용된 최적화:
✓ 중복 게이트 제거 (4개 제거)
- 게이트 융합 (적용 불가)
- Identity 제거 (발견 없음)
===================================
```

### 벤치마크 모드
```
=== 벤치마크 모드 ===
비교할 알고리즘 개수를 입력하세요 (2-5):
> 2

큐비트 개수를 입력하세요:
> 2

알고리즘 1 이름을 입력하세요:
> QFT

알고리즘 2 이름을 입력하세요:
> GROVER

=== 벤치마크 실행 중 ===
[1/2] QFT 실행...
[2/2] GROVER 실행...

=== 벤치마크 결과 ===
┌──────────────┬───────┬──────┬────────┐
│ 알고리즘     │ 게이트│ 깊이 │ 시간   │
├──────────────┼───────┼──────┼────────┤
│ QFT          │   3   │  3   │  60μs  │
│ GROVER       │   5   │  4   │  80μs  │
└──────────────┴───────┴──────┴────────┘

가장 빠른 회로: QFT
가장 효율적인 회로: QFT
===================================
```

## 구현 완료 현황

### Phase 1: 기본 도메인
- QubitIndex
- Probability
- QuantumGate 인터페이스
- 단일 큐비트 게이트 (X, H, Z)
- 다중 큐비트 게이트 (CNOT)
- CircuitStep
- QuantumCircuit
- QuantumCircuitBuilder
- QuantumState
- MeasurementResult
- CircuitVisualizer
- StateVisualizer
- InputView / OutputView
- QuantumCircuitSimulator

### Phase 2: 알고리즘 라이브러리
- QuantumAlgorithm (abstract, Template Method)
- AlgorithmFactory
- BellStateAlgorithm
- GHZStateAlgorithm
- QFTAlgorithm
- GroverAlgorithm
- DeutschJozsaAlgorithm
- AlgorithmMode

### Phase 3: 최적화 시스템
- CircuitOptimizer (interface, Strategy)
- OptimizationPipeline (Composite + Chain of Responsibility)
- RedundantGateRemover
- GateFusionOptimizer
- IdentityGateRemover
- OptimizationMode

### Phase 4: 분석 시스템
- CircuitAnalyzer (Facade)
- CircuitDepth
- GateCount
- CircuitComplexity
- EntanglementDegree
- AnalysisReport

### Phase 5: 검증 시스템
- CircuitValidator (interface, Chain of Responsibility)
- ValidationChain
- QubitRangeValidator
- GateCompatibilityValidator
- DepthLimitValidator
- ResourceValidator
- ValidationResult
- ValidationReport

### Phase 6: 비교 및 벤치마크
- CircuitComparator
- ComparisonReport
- BenchmarkRunner (Observer)
- PerformanceMonitor (Observer)
- PerformanceMetrics
- ResultCollector
- BenchmarkReport

### Phase 7: 통합 및 UI
- BenchmarkMode
- Application (4가지 모드 완전 통합)

## 실행 방법

### 1. 프로젝트 클론 및 빌드

```bash
git clone https://github.com/your-username/quantum-circuit-simulator.git
cd quantum-circuit-simulator
./gradlew build
```

### 2. 애플리케이션 실행

```bash
./gradlew run
```

### 3. 테스트 실행

```bash
./gradlew test
```

## 기술 스택

- **언어**: Java 21
- **빌드 도구**: Gradle 8.14
- **테스트**: JUnit 5, AssertJ
- **라이브러리**:
  - Strange 0.1.3 (양자 컴퓨팅)
  - mission-utils 1.2.0 (Console)

## 패키지 구조

```
quantum.circuit
├── Application.java
├── QuantumCircuitSimulator.java
├── mode
│   ├── AlgorithmMode.java
│   ├── OptimizationMode.java
│   └── BenchmarkMode.java
├── algorithm
│   ├── QuantumAlgorithm.java (abstract, Template Method)
│   ├── AlgorithmFactory.java (Factory)
│   ├── BellStateAlgorithm.java
│   ├── GHZStateAlgorithm.java
│   ├── QFTAlgorithm.java
│   ├── GroverAlgorithm.java
│   └── DeutschJozsaAlgorithm.java
├── optimizer
│   ├── CircuitOptimizer.java (interface, Strategy)
│   ├── OptimizationPipeline.java (Composite + Chain of Responsibility)
│   ├── RedundantGateRemover.java
│   ├── GateFusionOptimizer.java
│   └── IdentityGateRemover.java
├── analyzer
│   ├── CircuitAnalyzer.java (Facade)
│   ├── CircuitDepth.java
│   ├── GateCount.java
│   ├── CircuitComplexity.java
│   ├── EntanglementDegree.java
│   └── AnalysisReport.java
├── validator
│   ├── CircuitValidator.java (interface, Chain of Responsibility)
│   ├── ValidationChain.java
│   ├── QubitRangeValidator.java
│   ├── GateCompatibilityValidator.java
│   ├── DepthLimitValidator.java
│   ├── ResourceValidator.java
│   ├── ValidationResult.java
│   └── ValidationReport.java
├── benchmark
│   ├── CircuitComparator.java
│   ├── ComparisonReport.java
│   ├── BenchmarkRunner.java (Observer)
│   ├── PerformanceMonitor.java (Observer)
│   ├── PerformanceMetrics.java
│   ├── ResultCollector.java
│   └── BenchmarkReport.java
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
│   │   └── QuantumCircuitBuilder.java (Builder)
│   └── state
│       ├── QuantumState.java
│       ├── Probability.java
│       └── MeasurementResult.java (enum)
├── visualizer
│   ├── CircuitVisualizer.java
│   └── StateVisualizer.java
└── view
    ├── InputView.java
    └── OutputView.java
```

## 디자인 패턴 활용

### 1. Builder Pattern
```java
class QuantumCircuitBuilder {
    public QuantumCircuitBuilder withQubits(int count);
    public QuantumCircuitBuilder addStep(CircuitStep step);
    public QuantumCircuit build();
}
```

### 2. Template Method Pattern
```java
abstract class QuantumAlgorithm {
    public final QuantumCircuit build(int qubits) {
        // 공통 흐름 정의
        // 하위 클래스가 세부 구현
    }
}
```

### 3. Factory Pattern
```java
class AlgorithmFactory {
    public QuantumAlgorithm create(String algorithmName);
}
```

### 4. Strategy Pattern
```java
interface CircuitOptimizer {
    QuantumCircuit optimize(QuantumCircuit circuit);
}
// 다양한 최적화 전략 구현
```

### 5. Chain of Responsibility
```java
interface CircuitValidator {
    ValidationResult validate(QuantumCircuit circuit);
}
// ValidationChain으로 연결
```

### 6. Composite Pattern
```java
class OptimizationPipeline {
    private List<CircuitOptimizer> optimizers;
    // 여러 최적화를 조합
}
```

### 7. Facade Pattern
```java
class CircuitAnalyzer {
    public AnalysisReport analyze(QuantumCircuit circuit);
    // 복잡한 분석 로직을 단순한 인터페이스로 제공
}
```

### 8. Observer Pattern
```java
interface PerformanceMonitor {
    void onBenchmarkStart(String circuitName);
    void onBenchmarkComplete(String circuitName, PerformanceMetrics metrics);
}
// BenchmarkRunner가 모니터들에게 이벤트 통지
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
- 각 객체는 단일 책임을 가진다 (SRP)
- 확장에는 열려있고 수정에는 닫혀있다 (OCP)
- 인터페이스 분리 원칙을 따른다 (ISP)
- 의존성 역전 원칙을 따른다 (DIP)

### 테스트
- JUnit 5와 AssertJ를 이용하여 테스트 코드를 작성한다
- 단위 테스트 작성 (각 클래스와 메서드)
- 테스트 커버리지 80% 이상
- TDD Red-Green-Refactor 사이클을 따른다

### 디자인 패턴
- Builder 패턴: 복잡한 회로 구성
- Template Method 패턴: 알고리즘 공통 흐름
- Factory 패턴: 알고리즘 생성
- Strategy 패턴: 최적화 전략
- Chain of Responsibility: 검증 체인, 최적화 파이프라인
- Composite 패턴: 최적화 파이프라인
- Facade 패턴: 회로 분석
- Observer 패턴: 벤치마크 실행 추적

### 라이브러리
- Strange 양자 컴퓨팅 라이브러리 (`org.redfx:strange:0.1.3`)를 활용한다
- 내부 구현은 Strange를 활용하되, 도메인 객체는 직접 설계한다

## 참고 자료

- [Nobel Prize 2025 Physics - 공식 발표](https://www.nobelprize.org/prizes/physics/2025/)
- [Strange 라이브러리 - GitHub](https://github.com/redfx-quantum/strange)
- [Quantum Computing in Action - Manning Publications](https://www.manning.com/books/quantum-computing-in-action)
- [『퀀텀 스토리』 - 짐 배것 (반니)](https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=35000923)
- [Quantum Algorithm Zoo](https://quantumalgorithmzoo.org/)
- [Qiskit Textbook](https://qiskit.org/textbook/)
