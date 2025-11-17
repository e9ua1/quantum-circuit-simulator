package quantum.circuit.domain.circuit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.gate.HadamardGate;
import quantum.circuit.domain.gate.PauliXGate;

class QuantumCircuitBuilderTest {

    @Test
    @DisplayName("빌더로 양자 회로를 생성한다")
    void buildCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        assertThat(circuit.getQubitCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("여러 Step을 추가하여 회로를 생성한다")
    void buildCircuitWithMultipleSteps() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        assertThat(circuit.getStepCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("큐비트 개수를 지정하지 않으면 예외가 발생한다")
    void buildWithoutQubitsThrowsException() {
        QuantumCircuitBuilder builder = new QuantumCircuitBuilder()
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))));

        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("Step이 없어도 회로를 생성할 수 있다")
    void buildWithoutSteps() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(2)
                .build();

        assertThat(circuit.getStepCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("큐비트 개수가 0 이하면 예외가 발생한다")
    void invalidQubitCountThrowsException() {
        QuantumCircuitBuilder builder = new QuantumCircuitBuilder()
                .withQubits(0);

        assertThatThrownBy(builder::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR]");
    }

    @Test
    @DisplayName("빌더는 메서드 체이닝을 지원한다")
    void methodChaining() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .addStep(new CircuitStep(List.of(new HadamardGate(new QubitIndex(0)))))
                .build();

        assertThat(circuit).isNotNull();
    }
}
