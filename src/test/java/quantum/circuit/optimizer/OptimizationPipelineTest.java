package quantum.circuit.optimizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;
import quantum.circuit.domain.gate.PauliZGate;

class OptimizationPipelineTest {

    @Test
    @DisplayName("단일 최적화기를 실행한다")
    void runSingleOptimizer() {
        OptimizationPipeline pipeline = new OptimizationPipeline(
                List.of(new RedundantGateRemover())
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = pipeline.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("여러 최적화기를 순차적으로 실행한다")
    void runMultipleOptimizers() {
        OptimizationPipeline pipeline = new OptimizationPipeline(
                List.of(
                        new RedundantGateRemover(),
                        new IdentityGateRemover()
                )
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = pipeline.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("빈 최적화기 리스트는 원본 회로를 반환한다")
    void emptyOptimizerList() {
        OptimizationPipeline pipeline = new OptimizationPipeline(List.of());
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = pipeline.optimize(circuit);

        assertThat(optimized.getSteps()).hasSize(1);
    }

    @Test
    @DisplayName("3개의 최적화기를 순차 실행한다")
    void runThreeOptimizers() {
        OptimizationPipeline pipeline = new OptimizationPipeline(
                List.of(
                        new RedundantGateRemover(),
                        new IdentityGateRemover(),
                        new GateFusionOptimizer()
                )
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = pipeline.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("최적화 파이프라인 이름을 반환한다")
    void returnPipelineName() {
        OptimizationPipeline pipeline = new OptimizationPipeline(
                List.of(new RedundantGateRemover())
        );

        String name = pipeline.getOptimizationName();

        assertThat(name).isEqualTo("Optimization Pipeline");
    }

    @Test
    @DisplayName("복잡한 회로를 다단계로 최적화한다")
    void optimizeComplexCircuit() {
        OptimizationPipeline pipeline = new OptimizationPipeline(
                List.of(
                        new RedundantGateRemover(),
                        new IdentityGateRemover()
                )
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(1)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new PauliZGate(new QubitIndex(0)))))
                .build();

        QuantumCircuit optimized = pipeline.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }

    @Test
    @DisplayName("최적화기 개수를 반환한다")
    void returnOptimizerCount() {
        OptimizationPipeline pipeline = new OptimizationPipeline(
                List.of(
                        new RedundantGateRemover(),
                        new IdentityGateRemover(),
                        new GateFusionOptimizer()
                )
        );

        int count = pipeline.getOptimizerCount();

        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("빈 회로는 빈 회로로 반환한다")
    void emptyCircuitRemainsEmpty() {
        OptimizationPipeline pipeline = new OptimizationPipeline(
                List.of(new RedundantGateRemover())
        );
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .build();

        QuantumCircuit optimized = pipeline.optimize(circuit);

        assertThat(optimized.getSteps()).isEmpty();
    }
}
