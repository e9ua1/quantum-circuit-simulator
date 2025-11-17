package quantum.circuit.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import quantum.circuit.domain.circuit.CircuitStep;
import quantum.circuit.domain.circuit.QubitIndex;
import quantum.circuit.domain.circuit.QuantumCircuit;
import quantum.circuit.domain.circuit.QuantumCircuitBuilder;
import quantum.circuit.domain.gate.PauliXGate;
import quantum.circuit.domain.state.MeasurementResult;
import quantum.circuit.domain.state.QuantumState;

class OutputViewTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("회로 구성을 출력한다")
    void printCircuit() {
        QuantumCircuit circuit = new QuantumCircuitBuilder()
                .withQubits(1)
                .addStep(new CircuitStep(List.of(new PauliXGate(new QubitIndex(0)))))
                .build();

        OutputView.printCircuit(circuit);

        String output = outputStream.toString();
        assertThat(output).contains("Q0:");
    }

    @Test
    @DisplayName("양자 상태를 출력한다")
    void printState() {
        QuantumState state = QuantumState.initialize(1);

        OutputView.printState(state);

        String output = outputStream.toString();
        assertThat(output).contains("Qubit");
    }

    @Test
    @DisplayName("측정 결과를 출력한다")
    void printMeasurementResult() {
        MeasurementResult result = MeasurementResult.ZERO;

        OutputView.printMeasurementResult(0, result);

        String output = outputStream.toString();
        assertThat(output).contains("Qubit 0");
        assertThat(output).contains("0");
    }

    @Test
    @DisplayName("에러 메시지를 출력한다")
    void printErrorMessage() {
        String errorMessage = "테스트 에러";

        OutputView.printErrorMessage(errorMessage);

        String output = outputStream.toString();
        assertThat(output).contains("[ERROR]");
        assertThat(output).contains("테스트 에러");
    }

    @Test
    @DisplayName("구분선을 출력한다")
    void printSeparator() {
        OutputView.printSeparator();

        String output = outputStream.toString();
        assertThat(output).contains("=");
    }

    @Test
    @DisplayName("빈 줄을 출력한다")
    void printNewLine() {
        OutputView.printNewLine();

        String output = outputStream.toString();
        assertThat(output).isEqualTo(System.lineSeparator());
    }
}
