# 우아한테크코스 오픈미션 기획서
## 양자 회로 시뮬레이터 - 하이브리드 아키텍처

---

## 1. 미션 배경 및 동기

### 1.1 개인적 의미
- 군 복무 중 『퀀텀 스토리』를 읽으며 양자역학에 매료되어 소프트웨어학부로 편입
- 편입 후 코딩 능력과 소프트웨어 공학 역량을 쌓아온 과정
- **원점 회귀**: 나를 이 길로 이끌어준 양자역학 도메인을, 내가 갖춘 개발 실력으로 직접 구현

### 1.2 시의성
양자 컴퓨팅은 현재 가장 주목받는 기술 분야 중 하나입니다. IBM, Google, Amazon 등 주요 기업들이 양자 컴퓨터 개발에 막대한 투자를 하고 있으며, 우리나라도 양자 기술 개발에 적극 나서고 있습니다.

2019년 Google의 "양자 우월성(Quantum Supremacy)" 달성 이후, 실용적인 양자 알고리즘 개발과 양자 시뮬레이터의 중요성이 더욱 커지고 있습니다. 특히 초전도 큐비트 기반의 양자 컴퓨터가 실용화 연구 단계로 진입하면서, IBM Quantum Experience, AWS Braket 등 클라우드 기반 양자 컴퓨팅 플랫폼이 등장했고, 이에 따라 양자 회로 설계와 최적화 기술이 핵심 역량으로 부상하고 있습니다.

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
3. **디자인 패턴 10종 실전 적용**:
    - Builder: 복잡한 회로 구성
    - Template Method: 알고리즘 공통 흐름
    - Factory: 알고리즘 생성
    - Strategy: 최적화 전략
    - Chain of Responsibility: 검증 체인, 최적화 파이프라인
    - Composite: 최적화 파이프라인
    - Facade: 회로 분석
    - Observer: 벤치마크 실행 추적
    - Port-Adapter: Strange 라이브러리 격리
    - Adapter: Python 시각화 통합
4. **TDD 실천**: 확률적 결과의 테스트 전략 수립 (Red-Green-Refactor)
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
- **핵심 객체**: QuantumCircuitSimulator, QuantumCircuitBuilder
- **핵심 패턴**: Builder
- **협력 깊이**: 2-3단계

#### 2) 알고리즘 라이브러리 모드 (Algorithm Library Mode)
- **책임**: 대표 양자 알고리즘의 템플릿 기반 실행
- **핵심 패턴**: Template Method, Factory
- **핵심 객체**: QuantumAlgorithm, AlgorithmFactory
- **협력 깊이**: 3-4단계
- **구현 알고리즘**: Bell State, GHZ State, QFT, Grover, Deutsch-Jozsa

#### 3) 최적화 모드 (Optimization Mode)
- **책임**: 회로 최적화, 분석, 검증
- **핵심 패턴**: Strategy, Chain of Responsibility, Composite, Facade
- **핵심 객체**: OptimizationPipeline, CircuitAnalyzer, ValidationChain
- **협력 깊이**: 4-5단계
- **기능**: 중복 제거, 게이트 융합, Identity 제거, 회로 분석, 검증

#### 4) 벤치마크 모드 (Benchmark Mode)
- **책임**: 알고리즘/회로 성능 비교 및 측정
- **핵심 패턴**: Observer
- **핵심 객체**: BenchmarkRunner, PerformanceMonitor, CircuitComparator
- **협력 깊이**: 4단계
- **기능**: 성능 비교, 최적화 효과 측정, 실행 시간 측정

---

## 4. 도메인 분석

### 4.1 핵심 도메인 개념 (Phase 1)

#### Qubit (큐비트)
```
- 양자 정보의 기본 단위
- 상태: |0⟩, |1⟩의 중첩 (superposition)
- 측정 시 확률에 따라 0 또는 1로 붕괴
- 구현: QubitIndex (원시값 포장)
```

#### Quantum Gate (양자 게이트)
```
- 큐비트 상태를 변환하는 연산
- 기본 게이트:
  - X (Pauli-X): |0⟩ ↔ |1⟩ 반전
  - H (Hadamard): 중첩 상태 생성
  - Z (Pauli-Z): 위상 변화
  - CNOT: 2큐비트 게이트 (제어-NOT)
- 구현: QuantumGate 인터페이스
```

#### Quantum Circuit (양자 회로)
```
- 여러 게이트의 순차적 조합
- CircuitStep 단위로 구성 (일급 컬렉션)
- Builder 패턴으로 구성
```

#### Quantum State (양자 상태)
```
- 회로 실행 후 각 큐비트의 상태
- Probability (원시값 포장)
- MeasurementResult (Enum)
```

### 4.2 확장 도메인 (Phase 2-6)

#### Quantum Algorithm (양자 알고리즘)
```
- 특정 문제를 해결하는 양자 게이트의 조합
- Template Method로 공통 흐름 정의
- 각 알고리즘별 핵심 로직 구현
- Factory로 생성 관리
```

#### Circuit Optimization (회로 최적화)
```
- 동일 기능을 더 효율적으로 수행
- Strategy 패턴으로 다양한 최적화 규칙
- Composite 패턴으로 파이프라인 구성
- 최적화 전후 성능 측정
```

#### Circuit Analysis (회로 분석)
```
- 깊이, 게이트 수, 복잡도 계산
- 얽힘 정도 측정
- Facade 패턴으로 간단한 인터페이스 제공
- 개선 가능성 탐지
```

#### Circuit Validation (회로 검증)
```
- 큐비트 범위, 게이트 호환성 검증
- Chain of Responsibility로 연쇄 검증
- 깊이 제한, 리소스 제한 확인
- ValidationReport로 결과 종합
```

#### Performance Benchmark (성능 벤치마크)
```
- 여러 알고리즘/회로 성능 비교
- Observer 패턴으로 실행 추적
- 실행 시간, 리소스 사용량 측정
- 최적 선택 추천
```

---

## 5. 구현 계획 (7단계)

### Phase 1: 기본 도메인 [완료]

**구현 항목**:
- QubitIndex, Probability (원시값 포장)
- QuantumGate 인터페이스 및 구현체 (PauliXGate, HadamardGate, PauliZGate, CNOTGate)
- CircuitStep (일급 컬렉션)
- QuantumCircuit, QuantumCircuitBuilder (Builder 패턴)
- QuantumState, MeasurementResult (Enum)
- CircuitVisualizer, StateVisualizer
- InputView, OutputView
- QuantumCircuitSimulator

**협력 구조**:
```
QuantumCircuitSimulator → QuantumCircuitBuilder → CircuitStep → QuantumGate
                       → QuantumCircuit → QuantumState → Probability
                       → CircuitVisualizer
                       → StateVisualizer
```

**핵심 패턴**: Builder

---

### Phase 2: 알고리즘 라이브러리 [완료]

**구현 항목**:
- QuantumAlgorithm (abstract class - Template Method)
- AlgorithmFactory (Factory 패턴)
- BellStateAlgorithm (2큐비트)
- GHZStateAlgorithm (3큐비트)
- QFTAlgorithm (양자 푸리에 변환)
- GroverAlgorithm (양자 검색)
- DeutschJozsaAlgorithm (양자 오라클)
- AlgorithmMode (모드 UI)
- CircuitResultExporter (단계별 JSON 출력)
- Python 시각화 시스템 (Java-Python 하이브리드)
    - main.py: 통합 시각화 스크립트
    - bloch_animation.py: SLERP 보간 블로흐 구면
    - histogram_animation.py: 선형 보간 히스토그램
    - entanglement_visualizer.py: 2큐비트 얽힘 시각화
    - requirements.txt: matplotlib, qutip, numpy, scipy, pillow

**협력 구조**:
```
AlgorithmMode → AlgorithmFactory → QuantumAlgorithm (Template Method)
                                 → QuantumCircuitBuilder
                                 → QuantumCircuit
                                 → CircuitResultExporter (JSON 출력)
                                 → Python subprocess (시각화 실행)
                                       ↓
                                 [8개 파일 자동 생성]
                                 - bloch_sphere.png, histogram.png
                                 - bloch_steps.png, histogram_steps.png
                                 - bloch_evolution.gif, histogram_evolution.gif
                                 - entanglement_steps.png (2큐비트+)
                                 - entanglement_evolution.gif (2큐비트+)
```

**핵심 패턴**: Template Method, Factory, Adapter (Python 통합)

---

### Phase 3: 최적화 시스템 [완료]

**구현 항목**:
- CircuitOptimizer (interface - Strategy)
- OptimizationPipeline (Composite + Chain of Responsibility)
- RedundantGateRemover (중복 게이트 제거)
- GateFusionOptimizer (게이트 융합)
- IdentityGateRemover (Identity 게이트 제거)
- OptimizationMode (모드 UI)

**협력 구조**:
```
OptimizationMode → OptimizationPipeline (Composite)
                → [RedundantGateRemover, GateFusionOptimizer, IdentityGateRemover] (Strategy)
                → QuantumCircuit (before/after)
```

**핵심 패턴**: Strategy, Composite, Chain of Responsibility

---

### Phase 4: 분석 시스템 [완료]

**구현 항목**:
- CircuitAnalyzer (Facade 패턴)
- CircuitDepth (회로 깊이 계산)
- GateCount (게이트 개수 통계)
- CircuitComplexity (복잡도 분석)
- EntanglementDegree (얽힘 정도 측정)
- AnalysisReport (분석 결과 리포트)

**협력 구조**:
```
CircuitAnalyzer (Facade) → [CircuitDepth, GateCount, CircuitComplexity, EntanglementDegree]
                         → QuantumCircuit
                         → AnalysisReport
```

**핵심 패턴**: Facade

---

### Phase 5: 검증 시스템 [완료]

**구현 항목**:
- CircuitValidator (interface - Chain of Responsibility)
- ValidationChain (체인 구성)
- QubitRangeValidator (큐비트 범위 검증)
- GateCompatibilityValidator (게이트 호환성 검증)
- DepthLimitValidator (깊이 제한 검증)
- ResourceValidator (리소스 제한 검증)
- ValidationResult (검증 결과)
- ValidationReport (검증 결과 종합)

**협력 구조**:
```
ValidationChain → [QubitRangeValidator, GateCompatibilityValidator, 
                   DepthLimitValidator, ResourceValidator] (Chain of Responsibility)
                → QuantumCircuit
                → ValidationResult
                → ValidationReport
```

**핵심 패턴**: Chain of Responsibility

---

### Phase 6: 비교 및 벤치마크 [완료]

**구현 항목**:
- BenchmarkRunner (벤치마크 실행 관리)
- PerformanceMonitor (interface - Observer)
- ResultCollector (결과 수집 - Observer 구현체)
- CircuitComparator (회로 비교)
- PerformanceMetrics (성능 지표)
- BenchmarkReport (벤치마크 결과 리포트)
- ComparisonReport (회로 비교 리포트)

**협력 구조**:
```
BenchmarkRunner (Subject) → [ResultCollector, ...] (Observer)
                          → CircuitComparator
                          → PerformanceMetrics
                          → BenchmarkReport / ComparisonReport
```

**핵심 패턴**: Observer

---

### Phase 7: Port-Adapter 리팩토링 [완료]

**구현 항목**:
- QuantumExecutor (interface - Port, Domain에 위치)
- StrangeQuantumExecutor (Adapter 구현체, Infrastructure에 위치)
- 전체 계층 분리 완성
- DIP(의존성 역전 원칙) 달성

**협력 구조**:
```
Domain Layer:
    QuantumState → QuantumExecutor (Port)
                        ↑
                        | 구현 (의존성 역전)
Infrastructure Layer:
    StrangeQuantumExecutor (Adapter) → Strange Library
```

**핵심 패턴**: Port-Adapter, Adapter

**효과**:
- Domain이 Infrastructure에 의존하지 않음
- Strange 라이브러리 교체 가능 (Qiskit, Cirq 등)
- Mock 객체로 테스트 가능
- 진정한 도메인 중심 설계

---

## 6. 디자인 패턴 상세

### 6.1 Builder Pattern (빌더 패턴)
**적용 위치**: Phase 1 - 회로 구성

```java
class QuantumCircuitBuilder {
    private int qubits;
    private List<CircuitStep> steps;
    
    public QuantumCircuitBuilder(int qubits) {
        this.qubits = qubits;
        this.steps = new ArrayList<>();
    }
    
    public QuantumCircuitBuilder addGate(QuantumGate gate) {
        // 게이트 추가
        return this;
    }
    
    public QuantumCircuit build() {
        return new QuantumCircuit(qubits, List.copyOf(steps));
    }
}
```

**목적**: 복잡한 회로 객체를 단계적으로 구성

**효과**:
- 불변 객체 생성 지원
- 유창한 인터페이스 (Fluent Interface)
- 생성 과정의 명확성

---

### 6.2 Template Method Pattern (템플릿 메서드 패턴)
**적용 위치**: Phase 2 - 알고리즘 공통 흐름

```java
abstract class QuantumAlgorithm {
    // 템플릿 메서드
    public final QuantumCircuit build(int qubits) {
        validateParameters(qubits);
        QuantumCircuitBuilder builder = createBuilder(qubits);
        prepareInitialState(builder);
        applyMainAlgorithm(builder);  // 추상 메서드
        prepareMeasurement(builder);
        return builder.build();
    }
    
    // 하위 클래스에서 구현
    protected abstract void applyMainAlgorithm(QuantumCircuitBuilder builder);
    
    // 공통 메서드들
    protected void validateParameters(int qubits) { }
    protected QuantumCircuitBuilder createBuilder(int qubits) { }
}
```

**목적**: 알고리즘의 골격을 정의하고, 세부 단계는 하위 클래스에서 구현

**효과**:
- 코드 중복 제거
- 공통 로직의 일관성 보장

---

### 6.3 Factory Pattern (팩토리 패턴)
**적용 위치**: Phase 2 - 알고리즘 생성

```java
class AlgorithmFactory {
    public QuantumAlgorithm create(String algorithmName) {
        String upperName = algorithmName.toUpperCase();
        return switch (upperName) {
            case "BELL_STATE" -> new BellStateAlgorithm();
            case "GHZ_STATE" -> new GHZStateAlgorithm();
            case "QFT" -> new QFTAlgorithm();
            case "GROVER" -> new GroverAlgorithm();
            case "DEUTSCH_JOZSA" -> new DeutschJozsaAlgorithm();
            default -> throw new IllegalArgumentException("지원하지 않는 알고리즘: " + algorithmName);
        };
    }
}
```

**목적**: 알고리즘 객체 생성 로직을 캡슐화

**효과**:
- 객체 생성과 사용의 분리
- 새로운 알고리즘 추가 시 한 곳만 수정
- 의존성 역전 원칙 준수

---

### 6.4 Strategy Pattern (전략 패턴)
**적용 위치**: Phase 3 - 최적화 시스템

```java
interface CircuitOptimizer {
    QuantumCircuit optimize(QuantumCircuit circuit);
}

class RedundantGateRemover implements CircuitOptimizer {
    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        // H-H, X-X 같은 중복 게이트 제거
    }
}

class GateFusionOptimizer implements CircuitOptimizer {
    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        // 연속된 게이트 융합
    }
}
```

**목적**: 다양한 최적화 전략을 동적으로 교체 가능하게 설계

**효과**:
- 알고리즘의 독립적 변경 가능
- 런타임 전략 교체 지원
- 개방-폐쇄 원칙 준수

---

### 6.5 Chain of Responsibility (책임 연쇄 패턴)
**적용 위치**: Phase 5 - 검증 시스템

```java
interface CircuitValidator {
    ValidationResult validate(QuantumCircuit circuit);
}

class ValidationChain {
    private List<CircuitValidator> validators;
    
    public ValidationReport validateAll(QuantumCircuit circuit) {
        List<ValidationResult> results = new ArrayList<>();
        for (CircuitValidator validator : validators) {
            ValidationResult result = validator.validate(circuit);
            results.add(result);
        }
        return new ValidationReport(results);
    }
}

class QubitRangeValidator implements CircuitValidator {
    @Override
    public ValidationResult validate(QuantumCircuit circuit) {
        // 큐비트 범위 검증
    }
}
```

**목적**: 여러 검증 규칙을 순차적으로 적용하며 체인 구성

**효과**:
- 검증 규칙의 독립적 추가/제거
- 검증 순서 조정 가능
- 단일 책임 원칙 준수

---

### 6.6 Composite Pattern (복합체 패턴)
**적용 위치**: Phase 3 - 최적화 파이프라인

```java
class OptimizationPipeline implements CircuitOptimizer {
    private List<CircuitOptimizer> optimizers;
    
    public OptimizationPipeline(List<CircuitOptimizer> optimizers) {
        this.optimizers = List.copyOf(optimizers);
    }
    
    @Override
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

**효과**:
- 복잡한 최적화를 단순한 단계로 분해
- 파이프라인 구성의 유연성
- 재사용성 향상

---

### 6.7 Facade Pattern (파사드 패턴)
**적용 위치**: Phase 4 - 회로 분석

```java
class CircuitAnalyzer {
    private final CircuitDepth depthCalculator;
    private final GateCount gateCounter;
    private final CircuitComplexity complexityAnalyzer;
    private final EntanglementDegree entanglementMeasure;
    
    public AnalysisReport analyze(QuantumCircuit circuit) {
        int depth = depthCalculator.calculate(circuit);
        Map<String, Integer> gateCounts = gateCounter.count(circuit);
        double complexity = complexityAnalyzer.calculate(circuit);
        double entanglement = entanglementMeasure.measure(circuit);
        
        return new AnalysisReport(depth, gateCounts, complexity, entanglement);
    }
}
```

**목적**: 복잡한 분석 로직을 단순한 인터페이스로 제공

**효과**:
- 복잡한 하위 시스템을 간단하게 사용
- 클라이언트와 하위 시스템의 결합도 감소
- 사용 편의성 향상

---

### 6.8 Observer Pattern (관찰자 패턴)
**적용 위치**: Phase 6 - 벤치마크 시스템

```java
interface PerformanceMonitor {
    void onBenchmarkStart(String circuitName);
    void onBenchmarkComplete(String circuitName, PerformanceMetrics metrics);
}

class ResultCollector implements PerformanceMonitor {
    private Map<String, PerformanceMetrics> results = new HashMap<>();
    
    @Override
    public void onBenchmarkComplete(String circuitName, PerformanceMetrics metrics) {
        results.put(circuitName, metrics);
    }
}

class BenchmarkRunner {
    private List<PerformanceMonitor> monitors;
    
    public BenchmarkReport runBenchmark(Map<String, QuantumCircuit> circuits) {
        for (Map.Entry<String, QuantumCircuit> entry : circuits.entrySet()) {
            notifyStart(entry.getKey());
            PerformanceMetrics metrics = measure(entry.getValue());
            notifyComplete(entry.getKey(), metrics);
        }
        return generateReport();
    }
    
    private void notifyComplete(String name, PerformanceMetrics metrics) {
        for (PerformanceMonitor monitor : monitors) {
            monitor.onBenchmarkComplete(name, metrics);
        }
    }
}
```

**목적**: 벤치마크 실행 과정을 여러 모니터가 독립적으로 추적

**효과**:
- 실행 과정의 느슨한 결합
- 모니터의 독립적 추가/제거
- 확장 가능한 이벤트 시스템

---

### 6.9 Adapter Pattern (어댑터 패턴)
**적용 위치**: Phase 2 - Python 시각화 통합

```java
class PythonVisualizerAdapter {
    public void visualize(String jsonPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "python3", "src/main/python/main.py", jsonPath
            );
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                throw new VisualizationException("Python 시각화 실패");
            }
        } catch (Exception e) {
            throw new VisualizationException("시각화 프로세스 에러", e);
        }
    }
}
```

**목적**: Java와 Python 간의 인터페이스 불일치 해결

**효과**:
- 이질적인 시스템 간 통합
- JSON 기반 데이터 교환
- 프로세스 간 통신 캡슐화

---

### 6.10 Port-Adapter Pattern (포트-어댑터 패턴)
**적용 위치**: Phase 7 - 계층 분리 및 DIP

```java
// Domain Layer (Port)
package quantum.circuit.domain.state.executor;

public interface QuantumExecutor {
    void applyXGate(QubitIndex target);
    void applyHadamardGate(QubitIndex target);
    Map<String, Double> getStateProbabilities();
}

// Infrastructure Layer (Adapter)
package quantum.circuit.infrastructure.executor;

public class StrangeQuantumExecutor implements QuantumExecutor {
    private final Program program;
    
    @Override
    public void applyHadamardGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new Hadamard(target.getValue()));
        program.addStep(step);
    }
    
    @Override
    public Map<String, Double> getStateProbabilities() {
        Complex[] amplitudes = getAmplitudesFromResult();
        return calculateProbabilities(amplitudes);
    }
}
```

**목적**: 도메인과 인프라의 완전한 분리, DIP 달성

**효과**:
- Domain이 Infrastructure를 모름
- Strange → Qiskit, Cirq 등 라이브러리 교체 가능
- Mock 객체로 독립적 테스트
- 진정한 도메인 중심 설계

---

## 7. 구현 성과

### 7.1 구현 완료 현황
- **Phase 완료**: 7/7 단계 완료 + Python 시각화 통합
- **디자인 패턴 적용**: 10종 (Builder, Template Method, Factory, Strategy, Chain of Responsibility, Composite, Facade, Observer, Port-Adapter, Adapter)
- **알고리즘 구현**: 5개 (Bell State, GHZ State, QFT, Grover, Deutsch-Jozsa)
- **모드 구현**: 4개 (자유, 알고리즘, 최적화, 벤치마크)
- **시각화 시스템**: Java-Python 하이브리드 (8개 파일 자동 생성)
- **테스트 커버리지**: 420+ 테스트 케이스

### 7.2 기술적 성과
- 복잡한 도메인의 객체지향 설계 완료
- 4-5단계 깊이의 협력 구조 구현
- TDD 기반 안정적 개발 프로세스 (Red-Green-Refactor)
- 확장 가능한 아키텍처 설계 (개방-폐쇄 원칙 준수)
- 10가지 디자인 패턴의 실전 적용 경험
- Java-Python 프로세스 간 통신 구현 (JSON 기반)
- 양자 상태 시각화 완성 (블로흐 구면, 히스토그램, 얽힘)
- 애니메이션 GIF 자동 생성 시스템 (SLERP 보간)
- Port-Adapter 패턴으로 외부 의존성 격리 (Strange 라이브러리)

---

## 8. 기대 효과

### 8.1 기술적 성장
- **복잡한 도메인 설계**: 양자역학의 추상적 개념을 구체적 코드로 구현
- **디자인 패턴 숙련도**: 10가지 패턴의 적절한 사용 시기 및 방법 체득
- **협력 구조 설계**: 4-5단계 깊이의 객체 간 협력 관계 설계 능력
- **TDD 실천**: Red-Green-Refactor 사이클 체화
- **확장 가능한 설계**: 새로운 기능 추가 시 기존 코드 수정 최소화

### 8.2 도메인 이해
- 양자역학 개념의 프로그래밍적 표현
- 양자 알고리즘 5종의 구조 이해
- 회로 최적화 기법 학습
- 성능 측정 및 벤치마크 방법론

### 8.3 소프트웨어 공학
- SOLID 원칙의 실전 적용
- 일급 컬렉션, 원시값 포장 등 객체지향 기법
- 인터페이스 분리를 통한 의존성 관리
- Facade를 통한 복잡도 은닉

---

## 9. 향후 확장 계획

### 9.1 단기 확장 (선택)
- 추가 양자 게이트 구현 (T, S, Toffoli)
- 추가 최적화 전략 (깊이 최소화, 병렬화)
- 추가 알고리즘 (Shor, Simon)

### 9.2 중기 확장 (선택)
- 양자 회로 저장/불러오기 (파일 I/O)
- 회로 시각화 고도화 (GUI)
- 에러 모델링 (노이즈, 디코히어런스)

### 9.3 장기 확장 (선택)
- 실제 양자 컴퓨터 연동 (IBM Quantum, AWS Braket)
- 클라우드 기반 시뮬레이션
- 분산 처리를 통한 대규모 회로 지원

---

## 10. 실행 방법

### 10.1 환경 설정

#### 1단계: 저장소 클론
```bash
git clone https://github.com/e9ua1/quantum-circuit-simulator
cd quantum-circuit-simulator
```

#### 2단계: Python 환경 설정 (필수)

**자동 설치 (권장):**
```bash
chmod +x install.sh
./install.sh
```

**수동 설치:**
```bash
# Python 3.9+ 필요
python3 --version

# 의존성 설치
pip3 install -r src/main/python/requirements.txt --break-system-packages

# 또는 setup.py 사용
pip3 install -e .
```

**필요한 라이브러리:**
- matplotlib==3.8.0
- qutip==5.2.2
- numpy==1.26.4
- plotly==5.9.0
- scipy==1.11.4
- pillow>=9.0.0

#### 3단계: Java 빌드 및 실행
```bash
# 빌드
./gradlew clean build

# 실행
./gradlew run
```

#### 4단계: 테스트 실행
```bash
# 전체 테스트
./gradlew test

# 특정 테스트
./gradlew test --tests "quantum.circuit.*"
```

### 10.2 실행 플로우

```
1. Clone Repository
   ↓
2. Install Python Dependencies (install.sh)
   ↓
3. Build Java Project (./gradlew build)
   ↓
4. Run Application (./gradlew run)
   ↓
5. Select Mode → Execute Algorithm
   ↓
6. Java generates JSON → Python creates visualizations
   ↓
7. Check output/ directory for results
   - *.png (static images)
   - *.gif (animations)
```

### 10.3 트러블슈팅

**Python 라이브러리 설치 실패 시:**
```bash
# pip 업그레이드
pip3 install --upgrade pip

# 가상 환경 사용 (권장)
python3 -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r src/main/python/requirements.txt
```

**Java 빌드 실패 시:**
```bash
# Gradle 캐시 정리
./gradlew clean --refresh-dependencies

# JDK 버전 확인 (Java 21 필요)
java -version
```

**시각화 파일이 생성되지 않을 때:**
```bash
# output/ 디렉토리 권한 확인
chmod -R 755 output/

# Python 스크립트 직접 실행 테스트
python3 src/main/python/main.py output/circuit_result.json
```

---

## 11. 참고 자료

### 11.1 양자 컴퓨팅
- [Quantum Algorithm Zoo](https://quantumalgorithmzoo.org/)
- [Qiskit Textbook](https://qiskit.org/textbook/)
- [『퀀텀 스토리』 - 짐 배것 (반니)](https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=35000923)
- [『Quantum Computing in Action』 - Manning Publications](https://www.manning.com/books/quantum-computing-in-action)

### 11.2 라이브러리
- [Strange - GitHub](https://github.com/redfx-quantum/strange)
- [QuTiP - Quantum Toolbox in Python](https://qutip.org/)
- [mission-utils](https://github.com/woowacourse/mission-utils)

### 11.3 디자인 패턴
- [『Head First Design Patterns』](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

## 12. 프로젝트 정보

### 12.1 기술 스택

**Backend (Java)**:
- **언어**: Java 21
- **빌드 도구**: Gradle 8.14
- **테스트**: JUnit 5, AssertJ
- **라이브러리**:
    - Strange 0.1.3 (양자 컴퓨팅 시뮬레이션)
    - mission-utils 1.2.0 (랜덤, 콘솔)

**Visualization (Python)**:
- **언어**: Python 3.9+
- **핵심 라이브러리**:
    - matplotlib 3.8.0 (2D/3D 시각화)
    - qutip 5.2.2 (양자 상태 표현)
    - numpy 1.26.4 (수치 연산)
    - scipy 1.11.4 (SLERP 보간)
    - pillow 9.0.0+ (GIF 생성)
- **통신 방식**: JSON 기반 프로세스 간 통신 (subprocess)

### 12.2 개발 환경
- **IDE**: IntelliJ IDEA
- **버전 관리**: Git/GitHub
- **개발 방법론**: TDD (Red-Green-Refactor)

### 12.3 프로젝트 저장소
- **GitHub**: https://github.com/e9ua1/quantum-circuit-simulator
- **문서**:
    - [README.md](https://github.com/e9ua1/quantum-circuit-simulator/blob/main/README.md)
    - [QUANTUM_GUIDE.md](https://github.com/e9ua1/quantum-circuit-simulator/blob/main/docs/QUANTUM_GUIDE.md)

---
