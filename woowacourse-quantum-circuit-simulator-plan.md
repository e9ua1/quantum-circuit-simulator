# 우아한테크코스 오픈미션 기획서
## 양자 회로 시뮬레이터 with 거시적 양자 터널링

---

## 1. 미션 배경 및 동기

### 1.1 개인적 의미
- 자소서에서 언급한 양자역학과의 인연
- 『퀀텀 스토리』를 통해 발견한 인생의 방향
- 양자 컴퓨팅 분야로의 편입 결심
- 편입 후 코딩 능력과 소프트웨어 공학 역량을 갖추어 온 과정
- **원점 회귀**: 이제는 나를 이 길로 이끌어준 양자역학 도메인을, 내가 갖춘 개발 실력으로 직접 구현해보고 싶은 열망

### 1.2 시의성
2025년 노벨 물리학상은 1984-1985년 조셉슨 접합을 이용한 초전도 전기 회로에서 거시적 양자 터널링과 에너지 양자화를 발견한 John Clarke, Michel Devoret, John Martinis에게 수여되었습니다.

이 연구는 초전도 양자 비트(qubit) 개발의 토대가 되었으며, 연구자들은 마이크로파 입력을 사용하여 초전도 전자를 여기시키고 터널링에 미치는 영향을 관찰했습니다.

### 1.3 도전 과제
- **도메인 복잡성**: 양자역학의 비직관적 개념을 코드로 표현
- **객체지향 설계**: 추상적 개념의 명확한 책임과 협력 관계 정의
- **테스트 전략**: 확률적 결과의 검증 방법
- **확장 가능성**: 새로운 게이트와 알고리즘 추가

---

## 2. 미션 목표

### 2.1 핵심 목표
**"양자 회로를 객체지향적으로 설계하여, 양자 게이트의 조합과 측정을 시뮬레이션한다"**

### 2.2 학습 목표
1. **도메인 주도 설계**: 양자역학 개념을 도메인 객체로 표현
2. **SOLID 원칙 적용**: 단일 책임, 개방-폐쇄 원칙 등
3. **디자인 패턴**: Strategy, Composite, Builder 패턴 활용
4. **TDD 실천**: 확률적 결과의 테스트 전략 수립
5. **일급 컬렉션**: 게이트 목록, 큐비트 상태 관리

---

## 3. 도메인 분석

### 3.1 핵심 개념

#### Qubit (큐비트)
```
- 양자 정보의 기본 단위
- 상태: |0⟩, |1⟩의 중첩 (superposition)
- 측정 시 확률에 따라 0 또는 1로 붕괴
- 비트와의 차이: 측정 전까지는 확률적 중첩 상태
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
- Step 단위로 구성 (동시에 적용되는 게이트들)
- 측정(Measurement)을 통한 결과 획득
```

### 3.2 Strange 라이브러리 구조 분석

Strange의 핵심 구조는 Program, Step, Gate, Qubit, QuantumExecutionEnvironment로 구성됩니다.

```java
// Strange 라이브러리의 기본 사용 패턴
Program p = new Program(2);  // 2개 큐비트
Step step1 = new Step(new X(0));  // 첫 번째 큐비트에 X 게이트
Step step2 = new Step(new Hadamard(0), new X(1));  // 병렬 게이트
p.addStep(step1);
p.addStep(step2);

SimpleQuantumExecutionEnvironment env = new SimpleQuantumExecutionEnvironment();
Result result = env.runProgram(p);
Qubit[] qubits = result.getQubits();
```

---

## 4. 기능 요구사항 (1차 작성)

### 4.1 필수 기능

#### 회로 초기화
- [ ] n개의 큐비트로 초기화된 양자 회로를 생성할 수 있다 (1 ≤ n ≤ 10)
- [ ] 모든 큐비트는 초기 상태 |0⟩으로 시작한다

#### 단일 큐비트 게이트
- [ ] X(Pauli-X) 게이트를 특정 큐비트에 적용할 수 있다
- [ ] H(Hadamard) 게이트를 특정 큐비트에 적용할 수 있다
- [ ] Z(Pauli-Z) 게이트를 특정 큐비트에 적용할 수 있다

#### 다중 큐비트 게이트
- [ ] CNOT 게이트를 두 큐비트에 적용할 수 있다 (제어 큐비트, 타겟 큐비트)

#### 회로 실행
- [ ] 구성된 회로를 실행하여 각 큐비트의 상태를 계산할 수 있다
- [ ] 특정 큐비트를 측정하여 0 또는 1의 결과를 얻을 수 있다
- [ ] 측정 전 각 큐비트가 |1⟩ 상태일 확률을 계산할 수 있다

#### 회로 시각화
- [ ] 구성된 회로를 텍스트 형태로 출력할 수 있다
- [ ] 각 Step에서 어떤 게이트가 어느 큐비트에 적용되는지 확인할 수 있다
- [ ] 큐비트별로 적용된 게이트를 시간 순서대로 볼 수 있다
- [ ] ASCII 아트를 활용한 회로 다이어그램 출력 (예: ─H─●─ 형태)

#### 상태 시각화
- [ ] 각 큐비트의 |0⟩ 상태 확률과 |1⟩ 상태 확률을 표시할 수 있다
- [ ] 확률을 막대그래프 형태로 시각화할 수 있다 (예: |0⟩: ████████░░ 80%)
- [ ] 여러 번 측정 시 결과의 히스토그램을 출력할 수 있다

### 4.2 선택 기능 (시간 여유 시)
- [ ] Bell State 생성 및 검증
- [ ] Quantum Teleportation 구현
- [ ] 거시적 양자 터널링 시뮬레이션 (노벨상 실험 재현)
- [ ] 대화형 시뮬레이터 (사용자가 게이트를 직접 추가하며 실험)

---

## 5. 프로그래밍 요구사항

### 5.1 코드 스타일
- [ ] Java 코드 컨벤션 준수
- [ ] 메서드 길이 15라인 이하
- [ ] indent(인덴트, 들여쓰기) depth를 2 이하로 제한
- [ ] 3항 연산자 지양
- [ ] else 예약어 지양

### 5.2 객체지향
- [ ] 원시값 포장 (QubitIndex, Probability 등)
- [ ] 일급 컬렉션 사용 (Gates, Qubits, Steps)
- [ ] Enum 활용 (GateType, MeasurementResult)
- [ ] 도메인 로직과 UI 로직 분리

### 5.3 테스트
- [ ] 단위 테스트 작성 (JUnit 5)
- [ ] 테스트 커버리지 80% 이상
- [ ] 확률적 결과 테스트 전략 수립
  - 예: 100회 측정 시 |1⟩ 확률이 50%라면, 40~60회 범위 허용
- [ ] 경계값 테스트 (큐비트 인덱스 범위, 게이트 조합)

### 5.4 설계
- [ ] Strategy 패턴: 각 게이트를 전략으로 표현
- [ ] Composite 패턴: Step과 Circuit의 계층 구조
- [ ] Builder 패턴: 복잡한 회로 구성
- [ ] 불변 객체 활용 (Qubit 상태, Gate 설정)

---

## 6. 예상 클래스 설계 (초안)

### 6.1 핵심 도메인

```java
// 값 객체
public class QubitIndex {
    private final int value;
    // 생성자에서 유효성 검증 (0 이상)
}

public class Probability {
    private final double value;
    // 생성자에서 유효성 검증 (0.0 ~ 1.0)
}

// 도메인 객체
public interface QuantumGate {
    void apply(QuantumState state);
    int getQubitCount();  // 이 게이트가 작용하는 큐비트 개수
    String getName();
}

public class PauliXGate implements QuantumGate {
    private final QubitIndex target;
    // X 게이트 로직
}

public class HadamardGate implements QuantumGate {
    private final QubitIndex target;
    // Hadamard 게이트 로직
}

public class CNOTGate implements QuantumGate {
    private final QubitIndex control;
    private final QubitIndex target;
    // CNOT 게이트 로직
}

// 일급 컬렉션
public class CircuitStep {
    private final List<QuantumGate> gates;
    
    public void applyTo(QuantumState state) {
        gates.forEach(gate -> gate.apply(state));
    }
}

public class QuantumCircuit {
    private final int qubitCount;
    private final List<CircuitStep> steps;
    
    public QuantumState execute() {
        QuantumState state = QuantumState.initialize(qubitCount);
        steps.forEach(step -> step.applyTo(state));
        return state;
    }
}

// 상태 관리
public class QuantumState {
    private final ComplexVector stateVector;  // 내부적으로 Strange 활용
    
    public Probability getProbabilityOfOne(QubitIndex index) {
        // |1⟩ 상태 확률 계산
    }
    
    public MeasurementResult measure(QubitIndex index) {
        // 측정 수행
    }
}
```

### 6.2 Builder 패턴 활용

```java
public class QuantumCircuitBuilder {
    private int qubitCount;
    private List<CircuitStep> steps = new ArrayList<>();
    
    public QuantumCircuitBuilder withQubits(int count) {
        this.qubitCount = count;
        return this;
    }
    
    public QuantumCircuitBuilder addStep(CircuitStep step) {
        steps.add(step);
        return this;
    }
    
    public QuantumCircuit build() {
        validate();
        return new QuantumCircuit(qubitCount, steps);
    }
}
```

### 6.3 시각화 관련 클래스

```java
// 회로 시각화
public class CircuitVisualizer {
    private final QuantumCircuit circuit;
    
    public String toAsciiArt() {
        // ASCII 아트 형태의 회로 다이어그램 생성
        // Q0: ─H─●─
        //        │
        // Q1: ───X─
    }
    
    public String toStepByStep() {
        // Step별 상세 설명
        // Step 1: H(Q0)
        // Step 2: CNOT(Q0→Q1)
    }
}

// 상태 시각화
public class StateVisualizer {
    private final QuantumState state;
    
    public String visualizeProbabilities() {
        // 막대그래프 형태의 확률 표시
        // |0⟩: ████████████████ 50.0%
        // |1⟩: ████████████████ 50.0%
    }
    
    public String visualizeQubitStates() {
        // 개별 큐비트 상태 표시
        // Qubit 0 → |0⟩: 50.0% |1⟩: 50.0%
    }
}

// 측정 통계
public class MeasurementHistogram {
    private final Map<String, Integer> results;
    
    public void measure(int trials) {
        // trials 횟수만큼 측정 수행
    }
    
    public String visualize() {
        // 히스토그램 형태로 출력
        // |00⟩: ████████████ 250 (25.0%)
    }
}
```

---

## 7. 구현 계획 (총 2주, 14일)

### Day 1-4: 기획 및 준비
```
Day 1-2: Strange 라이브러리 실습
  - Bell State 예제 실행
  - Quantum Teleportation 예제 분석
  - 도메인 개념 깊이 이해

Day 3-4: 설계 및 기획 마무리
  - 클래스 다이어그램 작성
  - 기능 요구사항 세부화
  - 프로젝트 초기 세팅
```

### Day 5-8: 핵심 기능 구현
```
Day 5-6: 기본 구조 및 단일 큐비트 게이트
  - 값 객체 구현 (QubitIndex, Probability)
  - X, H, Z 게이트 구현
  - 단위 테스트 작성

Day 7-8: 다중 큐비트 및 회로 실행
  - CNOT 게이트 구현
  - 회로 실행 로직
  - 측정 시스템 구현
```

### Day 9-11: 확장 및 리팩토링
```
Day 9: 회로 시각화
  - ASCII 아트 회로 다이어그램
  - Step별 상세 출력
  - 회로 정보 요약 (depth, gate count 등)

Day 10: 상태 시각화 및 측정 통계
  - 확률 막대그래프 출력
  - 측정 히스토그램 구현
  - Bell State 구현 및 검증

Day 11: 리팩토링 및 통합 테스트
  - 코드 리뷰 및 개선
  - 시각화 품질 개선
  - 테스트 커버리지 확인
```

### Day 12-14: 마무리 및 문서화
```
Day 12: 선택 기능 구현 (시간 여유 시)
  - Quantum Teleportation
  - 또는 터널링 시뮬레이션

Day 13: 문서화
  - README 작성
  - 실행 방법 정리
  - 설계 의도 문서화

Day 14: 최종 점검
  - 전체 기능 테스트
  - 요구사항 체크
  - 제출 준비
```

---

## 8. 테스트 전략

### 8.1 결정론적 테스트
```java
@Test
void X게이트를_적용하면_0과_1이_반전된다() {
    QuantumCircuit circuit = new QuantumCircuitBuilder()
        .withQubits(1)
        .addStep(new CircuitStep(new PauliXGate(new QubitIndex(0))))
        .build();
    
    QuantumState result = circuit.execute();
    
    assertThat(result.getProbabilityOfOne(new QubitIndex(0)))
        .isEqualTo(new Probability(1.0));
}
```

### 8.2 확률적 테스트
```java
@Test
void Hadamard게이트_적용_후_측정하면_50퍼센트_확률로_0과_1이_나온다() {
    QuantumCircuit circuit = new QuantumCircuitBuilder()
        .withQubits(1)
        .addStep(new CircuitStep(new HadamardGate(new QubitIndex(0))))
        .build();
    
    int oneCount = 0;
    int trials = 1000;
    
    for (int i = 0; i < trials; i++) {
        QuantumState result = circuit.execute();
        if (result.measure(new QubitIndex(0)) == MeasurementResult.ONE) {
            oneCount++;
        }
    }
    
    // 허용 오차 범위: 450~550회
    assertThat(oneCount).isBetween(450, 550);
}
```

---

## 9. 예상 실행 예시

### 9.1 기본 실행 예시 (Bell State)

```java
public class Application {
    public static void main(String[] args) {
        // Bell State 생성
        QuantumCircuit bellCircuit = new QuantumCircuitBuilder()
            .withQubits(2)
            .addStep(new CircuitStep(new HadamardGate(new QubitIndex(0))))
            .addStep(new CircuitStep(new CNOTGate(
                new QubitIndex(0), 
                new QubitIndex(1)
            )))
            .build();
        
        System.out.println("=== Bell State Circuit ===");
        System.out.println(bellCircuit.visualize());
        
        QuantumState result = bellCircuit.execute();
        
        System.out.println("\n=== Quantum State Probabilities ===");
        System.out.println(result.visualizeProbabilities());
        
        System.out.println("\n=== Single Measurement ===");
        System.out.println("Qubit 0: " + result.measure(new QubitIndex(0)));
        System.out.println("Qubit 1: " + result.measure(new QubitIndex(1)));
        System.out.println("Entangled: " + (result.isEntangled() ? "Yes" : "No"));
    }
}
```

**예상 출력:**
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

### 9.2 측정 통계 예시

```java
public class MeasurementStatistics {
    public static void main(String[] args) {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
            .withQubits(1)
            .addStep(new CircuitStep(new HadamardGate(new QubitIndex(0))))
            .build();
        
        MeasurementHistogram histogram = new MeasurementHistogram(circuit, 1000);
        System.out.println(histogram.visualize());
    }
}
```

**예상 출력:**
```
=== Measurement Results (1000 trials) ===

|0⟩: ████████████████████████████ 487 (48.7%)
|1⟩: ████████████████████████████ 513 (51.3%)

Expected: 50% / 50%
Deviation: -2.6% / +2.6%
```

### 9.3 복잡한 회로 시각화 예시

```java
public class ComplexCircuitExample {
    public static void main(String[] args) {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
            .withQubits(3)
            .addStep(new CircuitStep(
                new HadamardGate(new QubitIndex(0)),
                new HadamardGate(new QubitIndex(1))
            ))
            .addStep(new CircuitStep(
                new CNOTGate(new QubitIndex(0), new QubitIndex(2))
            ))
            .addStep(new CircuitStep(
                new CNOTGate(new QubitIndex(1), new QubitIndex(2))
            ))
            .build();
        
        System.out.println(circuit.visualize());
    }
}
```

**예상 출력:**
```
=== Quantum Circuit (3 qubits, 3 steps) ===

Q0: ─H─●───
       │
Q1: ─H─┼─●─
       │ │
Q2: ───X─X─

Step 1: H(Q0), H(Q1)
Step 2: CNOT(Q0→Q2)
Step 3: CNOT(Q1→Q2)

Total Gates: 4 (2 single-qubit, 2 two-qubit)
Circuit Depth: 3
```

### 9.4 대화형 실행 예시 (선택 기능)

```java
public class InteractiveSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QuantumCircuitBuilder builder = new QuantumCircuitBuilder();
        
        System.out.print("Number of qubits: ");
        int qubits = scanner.nextInt();
        builder.withQubits(qubits);
        
        while (true) {
            System.out.println("\n1. Add H gate");
            System.out.println("2. Add X gate");
            System.out.println("3. Add CNOT gate");
            System.out.println("4. Show circuit");
            System.out.println("5. Run & measure");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            
            int choice = scanner.nextInt();
            // ... 게이트 추가 로직
            
            if (choice == 4) {
                System.out.println(builder.preview());
            }
        }
    }
}
```

**예상 출력:**
```
Number of qubits: 2

1. Add H gate
2. Add X gate
3. Add CNOT gate
4. Show circuit
5. Run & measure
0. Exit
Choice: 1
Target qubit: 0
✓ Hadamard gate added to Q0

Choice: 3
Control qubit: 0
Target qubit: 1
✓ CNOT gate added (Q0→Q1)

Choice: 4

Current Circuit:
Q0: ─H─●─
       │
Q1: ───X─

Choice: 5

Measuring 100 times...
Results:
|00⟩: ████████████████ 48 times
|11⟩: ████████████████ 52 times
```

---

## 10. 리스크 및 대응 방안

### 10.1 수학적 복잡도
**리스크**: 양자 상태는 복소수 벡터로 표현되어 수학적으로 복잡함

**대응**:
- Strange 라이브러리의 내부 구현 활용
- 도메인 객체는 수학적 세부사항 감추고 개념만 노출
- 필요한 경우 Apache Commons Math 사용

### 10.2 테스트의 어려움
**리스크**: 확률적 결과의 테스트가 불안정할 수 있음

**대응**:
- 충분한 시행 횟수 확보 (최소 1000회)
- 허용 오차 범위 설정 (통계적 신뢰구간 활용)
- Seed 고정 가능한 랜덤 생성기 사용

### 10.3 시간 부족
**리스크**: 2주 안에 모든 기능 구현이 어려울 수 있음

**대응**:
- 필수 기능과 선택 기능 명확히 구분
- MVP(Minimum Viable Product) 먼저 완성
- 선택 기능은 시간 여유 시 추가

---

## 11. 회고 포인트

### 미션 완료 후 작성할 회고
1. **도메인 이해**: 양자역학 개념을 얼마나 명확히 이해했는가?
2. **객체 설계**: 책임과 협력이 명확했는가?
3. **테스트 전략**: 확률적 결과를 어떻게 검증했는가?
4. **리팩토링**: 어떤 부분을 개선했고, 왜 개선했는가?
5. **학습 효과**: 이 미션을 통해 무엇을 배웠는가?

---

## 12. 참고 자료

- [Nobel Prize 2025 Physics - 공식 발표](https://www.nobelprize.org/prizes/physics/2025/)
- [Strange 라이브러리 - GitHub](https://github.com/redfx-quantum/strange)
- [Quantum Computing in Action - Manning Publications](https://www.manning.com/books/quantum-computing-in-action)
- [『퀀텀 스토리』 - 짐 배것 (반니)](https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=35000923)

---

## 부록: 2025 노벨 물리학상 핵심 내용

### 거시적 양자 터널링 실험
- **시기**: 1984-1985년
- **연구진**: John Clarke, Michel Devoret, John Martinis (UC Berkeley)
- **핵심 발견**: 초전도 전기 회로에서 수십억 개의 Cooper 쌍이 마치 단일 입자처럼 행동하며 양자 터널링을 보임
- **의의**: 양자역학 효과가 거시적 규모에서도 관찰될 수 있음을 최초로 입증
- **응용**: 초전도 양자 비트(qubit) 개발의 토대가 되어 현대 양자 컴퓨팅의 기초를 마련

### 조셉슨 접합 (Josephson Junction)
- 두 개의 초전도체를 얇은 절연체로 분리한 구조
- 전압이 없는 상태에서 전류가 흐를 수 있음
- 양자 터널링 현상을 거시적 규모에서 관찰 가능하게 함
- 마이크로파를 사용하여 에너지 준위를 측정하고 제어 가능

### 현대적 응용
- 초전도 양자 비트 (Superconducting Qubits)
- 양자 암호화 (Quantum Cryptography)
- 양자 센서 (Quantum Sensors)
- 초저온 MRI 기술
