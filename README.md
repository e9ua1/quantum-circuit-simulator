# 양자 회로 시뮬레이터

> 📘 **처음 오셨나요?** 양자역학이 처음이라면 먼저 [양자 회로 시뮬레이터 입문 가이드](./docs/QUANTUM_GUIDE.md)를 읽어보세요!  
> 큐비트, 중첩, 얽힘 같은 핵심 개념부터 프로그램 사용법까지 자세히 설명되어 있습니다.

## 목차

- [학습 목표](#학습-목표)
- [왜 이 프로젝트인가?](#왜-이-프로젝트인가)
- [모드 구성](#모드-구성)
- [기능 요구사항](#기능-요구사항)
- [예외 상황](#예외-상황)
- [실행 결과 예시](#실행-결과-예시)
- [실행 방법](#실행-방법)
- [기술 스택](#기술-스택)
- [패키지 구조](#패키지-구조)
- [디자인 패턴 활용](#디자인-패턴-활용)
- [아키텍처 설계](#아키텍처-설계)
- [프로그래밍 요구사항](#프로그래밍-요구사항)
- [참고 자료](#참고-자료)

## 학습 목표

- **객체지향 설계**: 복잡한 양자역학 도메인을 명확한 책임과 협력 관계로 표현한다
- **TDD 실천**: 클래스와 함수에 대한 단위 테스트를 통해 의도한 대로 정확하게 작동하는 영역을 확보한다
- **Red-Green-Refactor**: TDD 사이클을 체화한다
- **디자인 패턴 활용**: 10가지 디자인 패턴을 실전에 적용한다
- **도메인 주도 설계**: 추상적인 양자역학 개념을 구체적인 도메인 객체로 구현한다
- **클린 아키텍처**: Port-Adapter 패턴으로 도메인과 인프라를 분리한다

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
- **전체 아키텍처**: Port-Adapter 패턴으로 DIP 완성 (도메인과 인프라 분리)

특히 **Port-Adapter 아키텍처**는 제가 가장 고민한 부분입니다. Domain 레이어가 Strange 라이브러리에 직접 의존하는 문제를 해결하기 위해, QuantumExecutor 인터페이스(Port)를 Domain에 정의하고 StrangeQuantumExecutor(Adapter)로 구현했습니다. 이를 통해 라이브러리를 쉽게 교체할 수 있고, Mock 객체로 테스트할 수 있으며, 진정한 도메인 중심 설계를 달성했습니다.

### 1-3주차 미션과의 연결

1-3주차 미션에서 저는 코드리뷰를 통해 많은 것을 배웠습니다. 단순히 동작하는 코드를 넘어, "**왜 이렇게 설계했는가?**"에 대한 깊은 고민이 필요하다는 것을요. 설계에는 정답이 없고, 항상 트레이드오프가 존재한다는 것도 배웠습니다.

이 프로젝트는 그 배움의 연장선입니다:
- **로또 미션**의 일급 컬렉션 → CircuitStep의 게이트 관리
- **자동차 경주**의 전략 패턴 → 최적화 전략, 분석 메트릭

하지만 단순한 확장이 아닙니다. 양자역학이라는 **비직관적이고 추상적인 도메인**을 명확한 객체로 표현하는 과정에서, 책임과 협력의 본질을 더 깊이 이해할 수 있었습니다. 큐비트의 중첩 상태, 얽힘, 측정에 따른 붕괴 같은 개념들을 QubitIndex, Probability, QuantumState 같은 도메인 객체로 구체화하며, "**좋은 도메인 설계란 무엇인가?**"에 대한 답을 찾아가는 과정이었습니다.

### 목표: 본질을 이해하는 개발자

이 프로젝트를 통해 증명하고 싶은 것은 세 가지입니다:

1. **복잡한 도메인도 명확한 설계로 풀어낼 수 있다**
- 양자역학의 추상적 개념을 구체적인 객체로 표현
- 4-5단계 깊이의 협력 구조를 명확한 책임으로 구성

2. **설계 원칙과 패턴을 실전에서 적용할 수 있다**
- 10가지 디자인 패턴의 적절한 사용 시기 판단
- 트레이드오프를 이해하고 맥락에 맞는 선택
- DIP를 통한 진정한 계층 분리

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

**시각화 기능:**
- 알고리즘 실행 시 자동으로 시각화 파일 생성
- **단계별 상태 변화** 추적 및 시각화
- **정확한 얽힘 확률** 계산 및 표시
- **2큐비트 얽힘 시각화** - 두 블로흐 구면 동시 표시
- **애니메이션 GIF** 자동 생성으로 극적인 시각화
- Python 기반 블로흐 구면과 히스토그램 생성

**생성 파일 (8개):**

정적 이미지 (PNG):
- `bloch_sphere.png` - 최종 상태의 블로흐 구면
- `histogram.png` - 최종 상태 분포
- `bloch_steps.png` - 단계별 궤적 비교
- `histogram_steps.png` - 단계별 상태 비교
- `entanglement_steps.png` - 2큐비트 얽힘 단계별 비교

애니메이션 (GIF):
- `bloch_evolution.gif` - 블로흐 구면 벡터가 부드럽게 회전
- `histogram_evolution.gif` - 상태 분포가 부드럽게 변화
- `entanglement_evolution.gif` - 얽힘 형성 과정 애니메이션

**협력 구조:**
```
AlgorithmFactory → QuantumAlgorithm (Template Method)
    ↓                  ↓
AlgorithmType Enum   CircuitBuilder
    ↓
ExecutionEngine → CircuitResultExporter (단계별 JSON)
    ↓
PythonVisualizer (자동 시각화)
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
CircuitAnalyzer → [CircuitMetric 구현체들]
    ↓
ValidationChain → [CircuitValidator 구현체들]
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
- Enum 기반 등록 시스템으로 타입 안전성을 보장한다
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

#### 2.5 자동 시각화
- 알고리즘 실행 시 자동으로 시각화 파일 생성
- **단계별 상태 JSON 출력**
  - 초기 상태 (Step 0)
  - 각 게이트 적용 후 상태 (Step 1, 2, ...)
  - 각 단계의 큐비트 확률 및 시스템 상태
- **정확한 얽힘 확률 계산**
  - Strange 라이브러리의 amplitude 직접 접근
  - |amplitude|² 계산으로 정확한 확률
  - Bell State: |00⟩ = 50%, |11⟩ = 50% (정확)
- **2큐비트 얽힘 시각화**
  - 두 블로흐 구면을 나란히 배치
  - 얽힘 강도 계산 및 색상 표시
  - CNOT 전후 얽힘 상태 변화 극적 표현
- **Python 시각화 자동 실행**
  - 블로흐 구면: 단일 큐비트 상태 시각화
  - 히스토그램: 전체 시스템 상태 분포
  - 얽힘 시각화: 2큐비트 상관관계
  - 단계별 궤적: 상태 변화 추적
  - 애니메이션: 부드러운 전환 효과

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

#### 4.1 알고리즘 성능 비교
- 여러 알고리즘의 실행 시간 측정
- 리소스 사용량 비교
- 게이트 수 및 깊이 비교

#### 4.2 최적화 전후 비교
- 동일 회로의 최적화 전후 성능 측정
- 개선 효과 정량화
- 비교 리포트 생성

#### 4.3 성능 모니터링
- Observer 패턴으로 실행 추적
- 실시간 성능 지표 수집
- 벤치마크 결과 시각화

## 예외 상황

### 1. 입력 검증
- 큐비트 개수가 범위를 벗어난 경우 (1 ≤ n ≤ 10)
- 존재하지 않는 큐비트 인덱스 접근
- 잘못된 게이트 파라미터
- 지원하지 않는 알고리즘 이름

### 2. 회로 구성
- CNOT 게이트의 제어/타겟 큐비트가 동일한 경우
- 빈 회로 실행 시도
- 게이트 적용 실패

### 3. 최적화
- 최적화할 수 없는 회로 구조
- 검증 실패 시 적절한 에러 메시지

### 4. 시각화
- Python 환경 미설정
- 필요 패키지 누락
- JSON 파일 생성 실패

**예외 처리 원칙:**
- 사용자에게 명확한 에러 메시지 제공
- 프로그램 비정상 종료 방지
- 재시도 가능한 구조

## 실행 결과 예시

### 알고리즘 모드 - Bell State

```
=== 알고리즘 라이브러리 ===
사용 가능한 알고리즘:

1. Bell State (2큐비트) - 최대 얽힘 상태 생성
2. GHZ State (3큐비트) - 3큐비트 얽힘 상태
3. QFT (2큐비트) - 양자 푸리에 변환
4. Grover's Search (2큐비트) - 양자 검색 알고리즘
5. Deutsch-Jozsa (2큐비트) - 함수 판별 알고리즘

알고리즘을 선택하세요 (예: BELL_STATE):
BELL_STATE

=== Bell State Algorithm ===
설명: 2큐비트 최대 얽힘 상태를 생성합니다. H 게이트로 중첩 상태를 만든 후 CNOT 게이트로 얽힘 상태를 생성합니다.
===================================
Q0: ─H──●─
Q1: ────X─


Qubit 0 → |0⟩: 50.0% |1⟩: 50.0%
Qubit 1 → |0⟩: 50.0% |1⟩: 50.0%
===================================

🎨 단계별 시각화 생성 중...

Loading circuit result from: output/circuit_result.json
Circuit: Bell State
Qubits: 2

=== Creating Static Images ===
Found 3 steps
Visualizing Bloch Sphere Steps (Qubit 0, 3 steps)
  - output/bloch_steps.png
  Steps: Initial State → After H(Q0) → After CNOT(Q0→Q1)
Visualizing Histogram Steps (3 steps)
  - output/histogram_steps.png
  Steps: Initial State → After H(Q0) → After CNOT(Q0→Q1)
Visualizing Bloch Sphere (P(|1⟩) = 0.500)
  - output/bloch_sphere.png
Visualizing State Histogram
  - output/histogram.png

=== Creating Animations ===
  Creating Bloch animation: 50 frames, 2.5s
    ✅ output/bloch_evolution.gif
  Creating histogram animation: 50 frames, 2.5s
    ✅ output/histogram_evolution.gif
  Creating entanglement steps: 3 steps
    ✅ output/entanglement_steps.png
  Creating entanglement animation: 50 frames, 2.5s
    ✅ output/entanglement_evolution.gif

✅ Visualization complete!
  Static images:
    - output/bloch_sphere.png
    - output/histogram.png
    - output/bloch_steps.png
    - output/histogram_steps.png
    - output/entanglement_steps.png
  Animations:
    - output/bloch_evolution.gif
    - output/histogram_evolution.gif
    - output/entanglement_evolution.gif

✅ 시각화 완료!
📊 생성된 파일:
  - output/bloch_sphere.png (최종 상태)
  - output/histogram.png (최종 상태)
  - output/bloch_steps.png (단계별 궤적)
  - output/histogram_steps.png (단계별 비교)
  - output/entanglement_steps.png (얽힘 상태)
  - output/bloch_evolution.gif (애니메이션)
  - output/histogram_evolution.gif (애니메이션)
  - output/entanglement_evolution.gif (얽힘 애니메이션)
💡 확인: open output/*.png output/*.gif
```

**생성된 시각화:**

![Bell State Entanglement](assets/demo/entanglement_evolution.gif)

*두 블로흐 구면이 나란히 배치되어 얽힘 형성 과정을 극적으로 표현합니다. CNOT 게이트 적용 후 ⚡ Entanglement: 1.00 표시와 함께 벡터가 빨간색으로 변합니다.*

![Bloch Evolution](assets/demo/bloch_evolution.gif)

*블로흐 구면 위의 벡터가 북극(|0⟩)에서 적도(|+⟩)로 부드럽게 회전하는 모습을 보여줍니다.*

![Histogram Evolution](assets/demo/histogram_evolution.gif)

*양자 상태 분포가 |00⟩ 100%에서 |00⟩ 50% + |11⟩ 50%로 부드럽게 변화합니다.*

## 실행 방법

### 환경 설정

#### 1. Java 21 설치

**macOS:**
```bash
brew install openjdk@21
java -version
```

**Ubuntu/Linux:**
```bash
sudo apt update
sudo apt install openjdk-21-jdk
java -version
```

**Windows:**
1. [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) 다운로드
2. 설치 후 환경 변수 설정
3. `java -version` 확인


#### 2. Python 환경 설정 (시각화용)

Python 3.9 이상 필요:
```bash
python3 --version
```

**자동 설치 (권장):**
```bash
chmod +x install.sh
./install.sh
```

**수동 설치:**

필요한 패키지 설치:

macOS/Linux:
```bash
pip3 install -r src/main/python/requirements.txt --break-system-packages
```

Windows:
```bash
pip install -r src/main/python/requirements.txt
```

**또는 setup.py 사용:**
```bash
pip3 install -e .
```

**src/main/python/requirements.txt:**
```
matplotlib==3.8.0
qutip==5.2.2
numpy==1.26.4
plotly==5.9.0
scipy==1.11.4
pillow>=9.0.0
```

### 프로젝트 실행
```bash
# 1. 저장소 클론
git clone https://github.com/e9ua1/quantum-circuit-simulator.git
cd quantum-circuit-simulator

# 2. Python 환경 설정 (시각화를 위해 필수!)
./install.sh

# 3. Java 빌드 및 실행
./gradlew clean build
./gradlew run
```

### 테스트 실행

**모든 테스트:**
```bash
./gradlew test
```

**특정 패키지:**
```bash
./gradlew test --tests quantum.circuit.domain.*
```

**테스트 결과 확인:**

macOS/Linux:
```bash
open build/reports/tests/test/index.html
```

Windows:
```bash
start build/reports/tests/test/index.html
```

## 기술 스택

### 개발 환경
- **언어**: Java 21
- **빌드 도구**: Gradle 8.14
- **테스트**: JUnit 5, AssertJ

### 라이브러리
- **Strange** (`org.redfx:strange:0.1.3`): 양자 컴퓨팅 시뮬레이션
  - Port-Adapter 패턴으로 완전 격리
  - `StrangeQuantumExecutor`로만 접근
- **JUnit 5**: 단위 테스트 프레임워크
- **AssertJ**: assertion 라이브러리

### 시각화 도구
- **Python 3.8+**: 시각화 스크립트
- **QuTiP 4.7+**: 양자 상태 시각화 (블로흐 구면)
- **Matplotlib 3.5+**: 그래프 및 애니메이션
- **NumPy 1.21+**: 수치 계산
- **Pillow 9.0+**: GIF 저장

## 패키지 구조

```
quantum.circuit
├── domain                      # 핵심 도메인 로직
│   ├── circuit                 # 회로 구성
│   │   ├── QuantumCircuit      # 회로 실행
│   │   ├── QuantumCircuitBuilder # Fluent Builder
│   │   ├── CircuitStep         # 게이트 그룹 (일급 컬렉션)
│   │   └── QubitIndex          # 원시값 포장
│   ├── gate                    # 게이트
│   │   ├── QuantumGate         # 인터페이스
│   │   ├── SingleQubitGate     # 추상 클래스
│   │   ├── HadamardGate, PauliXGate, PauliZGate
│   │   └── CNOTGate            # 다중 큐비트 게이트
│   └── state                   # 양자 상태
│       ├── QuantumState        # Port-Adapter 패턴
│       ├── Probability         # 원시값 포장
│       ├── MeasurementResult   # Enum
│       └── executor            # Port 정의
│           └── QuantumExecutor # 인터페이스
├── infrastructure              # 기술적 구현
│   └── executor                # Adapter 구현
│       └── StrangeQuantumExecutor # Strange 연동
├── algorithm                   # 알고리즘 라이브러리
│   ├── QuantumAlgorithm        # Template Method
│   ├── AlgorithmFactory        # Factory 패턴
│   ├── AlgorithmType           # Enum
│   ├── BellStateAlgorithm
│   ├── GHZStateAlgorithm
│   ├── QFTAlgorithm
│   ├── GroverAlgorithm
│   └── DeutschJozsaAlgorithm
├── optimizer                   # 회로 최적화
│   ├── CircuitOptimizer        # Strategy 인터페이스
│   ├── RuleBasedOptimizer
│   ├── OptimizationPipeline    # Composite 패턴
│   ├── RedundantGateRemover
│   ├── IdentityGateRemover
│   ├── GateFusionOptimizer
│   └── rule
│       ├── OptimizationRule
│       └── ConsecutiveSameGateRule
├── analyzer                    # 회로 분석
│   ├── CircuitAnalyzer         # Facade 패턴
│   ├── CircuitMetric           # Strategy 인터페이스
│   ├── CircuitDepthMetric
│   ├── GateCountMetric
│   ├── ComplexityMetric
│   ├── EntanglementMetric
│   ├── AnalysisReport          # VO
│   └── facade                  # 정적 유틸리티
│       ├── CircuitDepth
│       ├── GateCount
│       ├── CircuitComplexity
│       └── EntanglementDegree
├── validator                   # 회로 검증
│   ├── CircuitValidator        # Strategy 인터페이스
│   ├── ValidationChain         # Chain of Responsibility
│   ├── QubitRangeValidator
│   ├── DepthLimitValidator
│   ├── GateCompatibilityValidator
│   ├── ResourceValidator
│   ├── ValidationResult        # VO
│   └── ValidationReport        # VO
├── benchmark                   # 벤치마크
│   ├── BenchmarkRunner
│   ├── CircuitComparator
│   ├── PerformanceMonitor      # Observer 인터페이스
│   ├── ResultCollector
│   ├── PerformanceMetrics      # VO
│   ├── BenchmarkReport         # VO
│   └── ComparisonReport        # VO
├── exporter                    # 회로 출력
│   └── CircuitResultExporter   # JSON 변환
├── visualizer                  # 시각화
│   ├── CircuitVisualizer       # ASCII 회로
│   ├── StateVisualizer         # 확률 표시
│   └── PythonVisualizer        # Python 스크립트 실행
├── mode                        # 실행 모드
│   ├── AlgorithmMode
│   ├── OptimizationMode
│   ├── BenchmarkMode
│   └── (FreeMode는 QuantumCircuitSimulator)
├── view                        # 콘솔 I/O
│   ├── InputView
│   └── OutputView
├── util                        # 유틸리티
│   ├── CircuitStepBuilder
│   ├── SingleQubitGateFactory
│   └── InputRetryHandler
└── Application                 # 메인 진입점
```

## 디자인 패턴 활용

### 1. Builder Pattern (회로 구성)

```java
QuantumCircuit circuit = new QuantumCircuitBuilder()
    .withQubits(2)
    .addStep(step -> step
        .addGate(new HadamardGate(new QubitIndex(0))))
    .addStep(step -> step
        .addGate(new CNOTGate(new QubitIndex(0), new QubitIndex(1))))
    .build();
```

**적용 위치**: `QuantumCircuitBuilder`

### 2. Template Method Pattern (알고리즘 공통 흐름)

```java
public abstract class QuantumAlgorithm {
    public final QuantumCircuit build(int qubitCount) {
        QuantumCircuitBuilder builder = initializeCircuit(qubitCount);
        applyAlgorithmLogic(builder);
        prepareMeasurement(builder);
        return builder.build();
    }
    
    protected abstract void applyAlgorithmLogic(QuantumCircuitBuilder builder);
}
```

**적용 위치**: `QuantumAlgorithm`

### 3. Factory Pattern (객체 생성)

```java
public class AlgorithmFactory {
    public QuantumAlgorithm create(String algorithmName) {
        return AlgorithmType.fromString(algorithmName).createAlgorithm();
    }
}

public enum AlgorithmType {
    BELL_STATE(BellStateAlgorithm::new),
    GHZ_STATE(GHZStateAlgorithm::new);
}
```

**적용 위치**: `AlgorithmFactory`, `AlgorithmType`

### 4. Strategy Pattern (최적화 전략)

```java
public interface CircuitOptimizer {
    QuantumCircuit optimize(QuantumCircuit circuit);
}

public class RedundantGateRemover implements CircuitOptimizer {
    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
    }
}
```

**적용 위치**: `CircuitOptimizer`, `CircuitMetric`, `CircuitValidator`

### 5. Chain of Responsibility (검증 체인)

```java
public class ValidationChain {
    private final List<CircuitValidator> validators;
    
    public ValidationResult validate(QuantumCircuit circuit) {
        for (CircuitValidator validator : validators) {
            ValidationResult result = validator.validate(circuit);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.valid();
    }
}
```

**적용 위치**: `ValidationChain`

### 6. Composite Pattern (최적화 파이프라인)

```java
public class OptimizationPipeline implements CircuitOptimizer {
    private final List<CircuitOptimizer> optimizers;
    
    @Override
    public QuantumCircuit optimize(QuantumCircuit circuit) {
        QuantumCircuit result = circuit;
        for (CircuitOptimizer optimizer : optimizers) {
            result = optimizer.optimize(result);
        }
        return result;
    }
}
```

**적용 위치**: `OptimizationPipeline`

### 7. Facade Pattern (회로 분석)

```java
public class CircuitAnalyzer {
    private final List<CircuitMetric> metrics;
    
    public AnalysisReport analyze(QuantumCircuit circuit) {
        Map<String, Object> results = new HashMap<>();
        for (CircuitMetric metric : metrics) {
            results.put(metric.getName(), metric.calculate(circuit));
        }
        return new AnalysisReport(results);
    }
}
```

**적용 위치**: `CircuitAnalyzer`

### 8. Observer Pattern (벤치마크 추적)

```java
public interface PerformanceMonitor {
    void onExecutionStart(String algorithmName);
    void onExecutionEnd(String algorithmName, long duration);
}

public class BenchmarkRunner {
    private final List<PerformanceMonitor> monitors;
    
    public void run(QuantumAlgorithm algorithm) {
        notifyStart(algorithm.getName());
        long start = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - start;
        notifyEnd(algorithm.getName(), duration);
    }
}
```

**적용 위치**: `BenchmarkRunner`, `PerformanceMonitor`

### 9. Adapter Pattern (라이브러리 격리)

```java
public interface QuantumExecutor {
    void applyHadamardGate(QubitIndex target);
}

public class StrangeQuantumExecutor implements QuantumExecutor {
    private final Program program;
    
    @Override
    public void applyHadamardGate(QubitIndex target) {
        Step step = new Step();
        step.addGate(new Hadamard(target.getValue()));
        program.addStep(step);
    }
}
```

**적용 위치**: `StrangeQuantumExecutor`

### 10. Port-Adapter Pattern (DIP 달성)

```
Domain Layer (Port 소유)
    ↑
    │ 구현
    │
Infrastructure Layer (Adapter 구현)
```

**핵심**:
- Domain이 인터페이스(Port)를 정의
- Infrastructure가 구현(Adapter) 제공
- 의존성 방향: Infrastructure → Domain
- 진정한 DIP 달성

## 아키텍처 설계

### Port-Adapter Architecture

```
┌────────────────────────────────────────────────────────┐
│                  Application Layer                     │
│    (Mode: AlgorithmMode, OptimizationMode, etc.)       │
└────────────────────────────────────────────────────────┘
                       ↓
┌────────────────────────────────────────────────────────┐
│                   Domain Layer                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │QuantumCircuit│  │ QuantumState │  │ QuantumGate  │  │
│  │              │  │              │  │              │  │
│  │              │  │              │  │              │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
│                           ↓                            │
│                  ┌──────────────────┐                  │
│                  │QuantumExecutor   │  (Port)          │
│                  │  (Interface)     │                  │
│                  └──────────────────┘                  │
└────────────────────────────────────────────────────────┘
                           ↑ 구현
┌────────────────────────────────────────────────────────┐
│              Infrastructure Layer                      │
│                  ┌──────────────────┐                  │
│                  │StrangeQuantum    │  (Adapter)       │
│                  │   Executor       │                  │
│                  └──────────────────┘                  │
│                           ↓                            │
│                  ┌──────────────────┐                  │
│                  │Strange Library   │                  │
│                  └──────────────────┘                  │
└────────────────────────────────────────────────────────┘
```

### 레이어별 책임

#### 1. Domain Layer (핵심 비즈니스 로직)
- **QuantumCircuit**: 회로 구성 및 실행
- **QuantumState**: 양자 상태 관리
- **QuantumGate**: 게이트 연산
- **QuantumExecutor (Port)**: 실행기 인터페이스 정의

**원칙**:
- Infrastructure에 의존하지 않음
- 순수한 비즈니스 로직만 포함
- Port 인터페이스를 소유

#### 2. Infrastructure Layer (기술적 구현)
- **StrangeQuantumExecutor (Adapter)**: Strange 라이브러리 연동
- **외부 라이브러리 격리**: Strange 의존성 캡슐화

**원칙**:
- Domain의 Port를 구현
- 라이브러리 변경 시 이 레이어만 수정

#### 3. Application Layer (유스케이스)
- **Mode 클래스들**: 사용자 시나리오 구현
- **Analyzer, Optimizer, Validator**: 부가 기능

### DIP (의존성 역전 원칙) 달성

**Before (문제)**:
```java
public class QuantumState {
    private final Program program;
}
```

**After (해결)**:
```java
package quantum.circuit.domain.state.executor;
public interface QuantumExecutor {
    void applyXGate(QubitIndex target);
    Map<String, Double> getStateProbabilities();
}

package quantum.circuit.infrastructure.executor;
public class StrangeQuantumExecutor implements QuantumExecutor {
    private final Program program;
    
    @Override
    public Map<String, Double> getStateProbabilities() {
        Complex[] amplitudes = getAmplitudesFromResult(result);
        return calculateProbabilities(amplitudes);
    }
}

public class QuantumState {
    private final QuantumExecutor executor;
    
    public Map<String, Double> getStateProbabilities() {
        return executor.getStateProbabilities();
    }
}
```

**효과**:
- ✅ **라이브러리 독립성**: Strange → Qiskit 교체 가능
- ✅ **테스트 용이성**: Mock 객체 주입 가능
- ✅ **진정한 계층 분리**: Domain이 Infrastructure를 모름
- ✅ **정확한 확률 계산**: amplitude 기반 얽힘 상태 정확도

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
- Enum을 적용한다 (MeasurementResult, AlgorithmType)
- 도메인 로직과 UI 로직을 분리한다
- 각 객체는 단일 책임을 가진다 (SRP)
- 확장에는 열려있고 수정에는 닫혀있다 (OCP)
- 인터페이스 분리 원칙을 따른다 (ISP)
- 의존성 역전 원칙을 따른다 (DIP)

### SOLID 원칙 적용

#### SRP (단일 책임 원칙)
- 각 클래스는 하나의 책임만 가진다
- 예: `CircuitAnalyzer`는 분석만, `CircuitOptimizer`는 최적화만

#### OCP (개방-폐쇄 원칙)
- Strategy 패턴으로 확장에 열려있음
- 새로운 게이트, 알고리즘, 최적화 규칙 추가 시 기존 코드 수정 불필요

#### LSP (리스코프 치환 원칙)
- `SingleQubitGate`의 하위 클래스들은 상위 클래스로 치환 가능
- `QuantumExecutor` 구현체들은 인터페이스로 치환 가능

#### ISP (인터페이스 분리 원칙)
- `CircuitOptimizer`, `CircuitValidator`, `CircuitMetric` 등 역할별로 인터페이스 분리

#### DIP (의존성 역전 원칙)
- Domain이 Infrastructure에 의존하지 않음
- `QuantumExecutor` 인터페이스를 Domain이 정의
- `StrangeQuantumExecutor`가 Infrastructure에서 구현

### 테스트
- JUnit 5와 AssertJ를 이용하여 테스트 코드를 작성한다
- 단위 테스트 작성 (각 클래스와 메서드)
- Mock 테스트로 DIP 검증
- TDD Red-Green-Refactor 사이클을 따른다

### 디자인 패턴
- Builder 패턴: 복잡한 회로 구성
- Template Method 패턴: 알고리즘 공통 흐름
- Factory 패턴: 알고리즘 생성, 게이트 생성
- Strategy 패턴: 최적화 전략, 분석 메트릭, Executor
- Chain of Responsibility: 검증 체인
- Composite 패턴: 최적화 파이프라인
- Facade 패턴: 회로 분석
- Observer 패턴: 벤치마크 실행 추적
- Adapter 패턴: 라이브러리 격리
- Port-Adapter: DIP 달성

### 라이브러리
- Strange 양자 컴퓨팅 라이브러리 (`org.redfx:strange:0.1.3`)
- Port-Adapter 패턴으로 완전히 격리
- `StrangeQuantumExecutor`로만 접근
- 다른 라이브러리로 교체 가능 (Qiskit, Cirq 등)

## 참고 자료

- [『퀀텀 스토리』 - 짐 배것 (반니)](https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=35000923)
- [Nobel Prize 2025 Physics - 공식 발표](https://www.nobelprize.org/prizes/physics/2025/)
- [Strange 라이브러리 - GitHub](https://github.com/redfx-quantum/strange)
- [Quantum Computing in Action - Manning Publications](https://www.manning.com/books/quantum-computing-in-action)
- [Quantum Algorithm Zoo](https://quantumalgorithmzoo.org/)
- [Qiskit Textbook](https://qiskit.org/textbook/)
- [QuTiP - Quantum Toolbox in Python](https://qutip.org/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
