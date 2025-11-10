# 우아한테크코스 오픈미션 기획서
## 양자 회로 시뮬레이터 - 하이브리드 아키텍처

---

## 1. 미션 배경 및 동기

### 1.1 개인적 의미
- 군 복무 중 『퀀텀 스토리』를 읽으며 양자역학에 매료되어 소프트웨어학부로 편입
- 편입 후 코딩 능력과 소프트웨어 공학 역량을 쌓아온 과정
- **원점 회귀**: 나를 이 길로 이끌어준 양자역학 도메인을, 내가 갖춘 개발 실력으로 직접 구현

### 1.2 시의성
2025년 노벨 물리학상은 1984-1985년 조셉슨 접합을 이용한 초전도 전기 회로에서 거시적 양자 터널링과 에너지 양자화를 발견한 John Clarke, Michel Devoret, John Martinis에게 수여되었습니다.

이 연구는 초전도 양자 비트(qubit) 개발의 토대가 되었으며, 연구자들은 마이크로파 입력을 사용하여 초전도 전자를 여기시키고 터널링에 미치는 영향을 관찰했습니다.

### 1.3 도전 과제
- **도메인 복잡성**: 양자역학의 비직관적 개념을 코드로 표현
- **객체지향 설계**: 추상적 개념의 명확한 책임과 협력 관계 정의
- **테스트 전략**: 확률적 결과의 검증 방법
- **확장 가능성**: 새로운 게이트와 알고리즘 추가
- **시스템 통합**: 4가지 모드의 유기적 협력

---

## 2. 미션 목표

### 2.1 핵심 목표
**"양자 회로 시뮬레이터를 통해 디자인 패턴의 실전 활용과 복잡한 도메인의 객체지향 설계를 체득한다"**

### 2.2 학습 목표
1. **도메인 주도 설계**: 양자역학 개념을 도메인 객체로 표현
2. **SOLID 원칙 적용**: 단일 책임, 개방-폐쇄 원칙 등
3. **디자인 패턴 6종 실전 적용**:
    - Strategy: 최적화 전략
    - Template Method: 알고리즘 공통 흐름
    - Chain of Responsibility: 검증 체인
    - Observer: 실행 추적
    - Factory: 알고리즘 생성
    - Composite: 최적화 파이프라인
4. **TDD 실천**: 확률적 결과의 테스트 전략 수립
5. **일급 컬렉션**: 게이트 목록, 큐비트 상태 관리
6. **복잡한 협력 구조**: 4-5단계 깊이의 객체 협력 설계

---

## 3. 시스템 아키텍처

### 3.1 하이브리드 구조 개요

본 프로젝트는 **4가지 모드**로 구성된 하이브리드 아키텍처를 채택합니다.

```
┌─────────────────────────────────────────────┐
│           Application                       │
│         (Mode Selector)                     │
└─────────────┬───────────────────────────────┘
              │
    ┌─────────┼─────────┬─────────┬──────────┐
    │         │         │         │          │
┌───▼───┐ ┌──▼──┐  ┌───▼────┐ ┌──▼────────┐│
│ Free  │ │Algo │  │Optimi  │ │Benchmark  ││
│ Mode  │ │Mode │  │zation  │ │  Mode     ││
└───┬───┘ └──┬──┘  └───┬────┘ └──┬────────┘│
    │        │         │         │          │
    └────────┴─────────┴─────────┴──────────┘
                      │
           ┌──────────▼──────────┐
           │   Domain Layer      │
           │  (Shared)           │
           └─────────────────────┘
```

### 3.2 모드별 책임

#### 1) 자유 모드 (Free Mode)
- **책임**: 사용자가 자유롭게 회로를 구성하고 실험
- **핵심 객체**: QuantumCircuitSimulator, CircuitBuilder
- **협력 깊이**: 2-3단계

#### 2) 알고리즘 라이브러리 모드 (Algorithm Library Mode)
- **책임**: 대표 양자 알고리즘의 템플릿 기반 실행
- **핵심 패턴**: Template Method, Factory
- **핵심 객체**: QuantumAlgorithm, AlgorithmFactory
- **협력 깊이**: 3-4단계
- **구현 알고리즘**: Bell State, GHZ State, QFT, Grover, Deutsch-Jozsa

#### 3) 최적화 모드 (Optimization Mode)
- **책임**: 회로 최적화, 분석, 검증
- **핵심 패턴**: Strategy, Chain of Responsibility
- **핵심 객체**: OptimizationPipeline, CircuitAnalyzer, ValidationChain
- **협력 깊이**: 4-5단계
- **기능**: 중복 제거, 게이트 융합, 깊이 최소화, 회로 분석

#### 4) 벤치마크 모드 (Benchmark Mode)
- **책임**: 알고리즘/회로 성능 비교 및 측정
- **핵심 패턴**: Observer, Composite
- **핵심 객체**: BenchmarkRunner, PerformanceMonitor, CircuitComparator
- **협력 깊이**: 4단계
- **기능**: 성능 비교, 최적화 효과 측정

---

## 4. 도메인 분석

### 4.1 핵심 도메인 개념 (Phase 1 - 완료)

#### Qubit (큐비트)
```
- 양자 정보의 기본 단위
- 상태: |0⟩, |1⟩의 중첩 (superposition)
- 측정 시 확률에 따라 0 또는 1로 붕괴
```

#### Quantum Gate (양자 게이트)
```
- 큐비트 상태를 변환하는 연산
- 기본 게이트:
  - X (NOT): |0⟩ ↔ |1⟩ 반전
  - H (Hadamard): 중첩 상태 생성
  - Z (Phase): 위상 변화
  - CNOT: 2큐비트 게이트 (제어-NOT)
```

#### Quantum Circuit (양자 회로)
```
- 여러 게이트의 순차적 조합
- Step 단위로 구성
- Builder 패턴으로 구성
```

### 4.2 확장 도메인 (Phase 2-6)

#### Quantum Algorithm (양자 알고리즘)
```
- 특정 문제를 해결하는 양자 게이트의 조합
- Template Method로 공통 흐름 정의
- 각 알고리즘별 핵심 로직 구현
```

#### Circuit Optimization (회로 최적화)
```
- 동일 기능을 더 효율적으로 수행
- Strategy 패턴으로 다양한 최적화 규칙
- Pipeline으로 순차 적용
```

#### Circuit Analysis (회로 분석)
```
- 깊이, 게이트 수, 복잡도 계산
- 얽힘 정도 측정
- 개선 가능성 탐지
```

#### Circuit Validation (회로 검증)
```
- 큐비트 범위, 게이트 호환성 검증
- Chain of Responsibility로 연쇄 검증
- 리소스 제한 확인
```

---

## 5. 구현 계획

### Phase 1: 기본 도메인 [완료] ✅

**구현 항목**:
- QubitIndex, Probability (원시값 포장)
- QuantumGate 인터페이스 및 구현체 (X, H, Z, CNOT)
- CircuitStep (일급 컬렉션)
- QuantumCircuit, QuantumCircuitBuilder
- QuantumState, MeasurementResult
- CircuitVisualizer, StateVisualizer
- QuantumCircuitSimulator

**협력 구조**:
```
QuantumCircuitSimulator → QuantumCircuitBuilder → CircuitStep → QuantumGate
                       → QuantumCircuit → QuantumState
                       → CircuitVisualizer
```

---

### Phase 2: 알고리즘 라이브러리

**구현 항목**:
- QuantumAlgorithm (abstract class - Template Method)
- AlgorithmFactory (Factory 패턴)
- BellStateAlgorithm, GHZStateAlgorithm
- QFTAlgorithm, GroverAlgorithm, DeutschJozsaAlgorithm
- AlgorithmParameter, AlgorithmValidator
- AlgorithmResult

**협력 구조**:
```
AlgorithmFactory → QuantumAlgorithm (Template Method)
                → AlgorithmValidator → AlgorithmParameter
                → CircuitBuilder → Circuit
                → AlgorithmResult
```

---

### Phase 3: 최적화 시스템

**구현 항목**:
- CircuitOptimizer (interface - Strategy)
- OptimizationPipeline (Composite)
- RedundantGateRemover, GateFusionOptimizer
- DepthReducer, IdentityGateRemover
- OptimizationResult, OptimizationReport
- ImprovementMetrics

**협력 구조**:
```
OptimizationPipeline → [RedundantGateRemover, GateFusionOptimizer, DepthReducer]
                    → Circuit (before/after)
                    → OptimizationResult
                    → ImprovementMetrics
                    → OptimizationReport
```

---

### Phase 4: 분석 시스템

**구현 항목**:
- CircuitAnalyzer
- CircuitDepth, GateCount
- CircuitComplexity, EntanglementDegree
- AnalysisReport

**협력 구조**:
```
CircuitAnalyzer → [CircuitDepth, GateCount, Complexity, Entanglement]
               → Circuit
               → AnalysisReport
```

---

### Phase 5: 검증 시스템

**구현 항목**:
- CircuitValidator (interface - Chain of Responsibility)
- ValidationChain
- QubitRangeValidator, GateCompatibilityValidator
- DepthLimitValidator, ResourceValidator
- ValidationResult, ValidationReport

**협력 구조**:
```
ValidationChain → [QubitRangeValidator, GateCompatibilityValidator, DepthLimitValidator]
               → Circuit
               → ValidationResult
               → ValidationReport
```

---

### Phase 6: 비교 및 벤치마크

**구현 항목**:
- CircuitComparator, ComparisonReport
- BenchmarkRunner
- PerformanceMonitor (Observer)
- ResultCollector (Observer)
- BenchmarkReport

**협력 구조**:
```
BenchmarkRunner → [Algorithm1, Algorithm2, ...]
               → PerformanceMonitor (Observer)
               → ResultCollector (Observer)
               → CircuitComparator
               → BenchmarkReport
```

---

### Phase 7: 통합 및 UI

**구현 항목**:
- ModeSelector
- AlgorithmMode, OptimizationMode, BenchmarkMode
- ModeView
- Application (모드 선택 통합)

**협력 구조**:
```
Application → ModeSelector → [FreeMode, AlgorithmMode, OptimizationMode, BenchmarkMode]
```

---

## 6. 디자인 패턴 적용 계획

### 6.1 Strategy Pattern (전략 패턴)
**적용 위치**: 최적화 시스템

```java
interface CircuitOptimizer {
    QuantumCircuit optimize(QuantumCircuit circuit);
}

class RedundantGateRemover implements CircuitOptimizer
class GateFusionOptimizer implements CircuitOptimizer
class DepthReducer implements CircuitOptimizer
```

**목적**: 다양한 최적화 전략을 동적으로 교체 가능하게 설계

---

### 6.2 Template Method Pattern (템플릿 메서드 패턴)
**적용 위치**: 알고리즘 라이브러리

```java
abstract class QuantumAlgorithm {
    public final QuantumCircuit build(int qubits) {
        QuantumCircuitBuilder builder = createBuilder(qubits);
        prepareInitialState(builder);
        applyMainAlgorithm(builder);  // 추상 메서드
        prepareMeasurement(builder);
        return builder.build();
    }
    
    protected abstract void applyMainAlgorithm(QuantumCircuitBuilder builder);
}
```

**목적**: 알고리즘의 공통 흐름은 상위 클래스에서, 세부 로직은 하위 클래스에서 구현

---

### 6.3 Chain of Responsibility (책임 연쇄 패턴)
**적용 위치**: 검증 시스템

```java
interface CircuitValidator {
    ValidationResult validate(QuantumCircuit circuit);
}

class ValidationChain {
    private List<CircuitValidator> validators;
    
    public ValidationResult validateAll(QuantumCircuit circuit) {
        for (CircuitValidator validator : validators) {
            ValidationResult result = validator.validate(circuit);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.success();
    }
}
```

**목적**: 여러 검증 규칙을 순차적으로 적용하며 체인 구성

---

### 6.4 Observer Pattern (관찰자 패턴)
**적용 위치**: 벤치마크 시스템

```java
interface PerformanceMonitor {
    void onStepComplete(CircuitStep step, QuantumState state);
    void onExecutionComplete(ExecutionResult result);
}

class BenchmarkRunner {
    private List<PerformanceMonitor> monitors;
    
    public void run(QuantumCircuit circuit) {
        // 실행 중 이벤트 발생 시 모든 모니터에 통지
        for (PerformanceMonitor monitor : monitors) {
            monitor.onStepComplete(step, state);
        }
    }
}
```

**목적**: 실행 과정을 여러 모니터가 독립적으로 추적

---

### 6.5 Factory Pattern (팩토리 패턴)
**적용 위치**: 알고리즘 생성

```java
class AlgorithmFactory {
    public QuantumAlgorithm create(String algorithmName) {
        return switch (algorithmName) {
            case "BELL_STATE" -> new BellStateAlgorithm();
            case "GHZ_STATE" -> new GHZStateAlgorithm();
            case "QFT" -> new QFTAlgorithm();
            // ...
        };
    }
}
```

**목적**: 알고리즘 객체 생성 로직을 캡슐화

---

### 6.6 Composite Pattern (복합체 패턴)
**적용 위치**: 최적화 파이프라인

```java
class OptimizationPipeline implements CircuitOptimizer {
    private List<CircuitOptimizer> optimizers;
    
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        QuantumCircuit current = circuit;
        for (CircuitOptimizer optimizer : optimizers) {
            current = optimizer.optimize(current);
        }
        return current;
    }
}
```

**목적**: 여러 최적화 단계를 하나의 파이프라인으로 조합

---

## 7. 기대 효과

### 7.1 기술적 성장
- 복잡한 도메인의 객체지향 설계 경험
- 6가지 디자인 패턴의 실전 적용
- 4-5단계 깊이의 객체 협력 구조 설계
- TDD 기반 개발 프로세스 체득

### 7.2 도메인 이해
- 양자역학 개념의 프로그래밍적 표현
- 양자 알고리즘의 구조 이해
- 회로 최적화 기법 학습

---

## 8. 참고 자료

### 8.1 양자 컴퓨팅
- [Nobel Prize 2025 Physics](https://www.nobelprize.org/prizes/physics/2025/)
- [Quantum Algorithm Zoo](https://quantumalgorithmzoo.org/)
- [Qiskit Textbook](https://qiskit.org/textbook/)
- 『퀀텀 스토리』 - 짐 배것
- 『Quantum Computing in Action』 - Manning Publications

### 8.2 라이브러리
- [Strange - GitHub](https://github.com/redfx-quantum/strange)

---
