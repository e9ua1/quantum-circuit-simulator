package quantum.circuit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import camp.nextstep.edu.missionutils.Console;

class QuantumCircuitSimulatorTest {

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
        Console.close();
    }

    @Test
    @DisplayName("시뮬레이터를 시작한다")
    void startSimulator() {
        String input = "1\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuantumCircuitSimulator simulator = new QuantumCircuitSimulator();

        assertThatCode(simulator::start).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회로를 구성하고 시뮬레이션한다")
    void buildAndSimulateCircuit() {
        String input = "1\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuantumCircuitSimulator simulator = new QuantumCircuitSimulator();
        simulator.start();

        String output = outputStream.toString();
        assertThat(output).contains("Quantum Circuit");
    }

    @Test
    @DisplayName("여러 게이트를 추가한다")
    void addMultipleGates() {
        String input = "2\nX\n0\ny\nH\n1\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuantumCircuitSimulator simulator = new QuantumCircuitSimulator();
        simulator.start();

        String output = outputStream.toString();
        assertThat(output).contains("Step 1:");
        assertThat(output).contains("Step 2:");
    }

    @Test
    @DisplayName("잘못된 입력 시 재입력을 받는다")
    void retryOnInvalidInput() {
        String input = "abc\n1\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuantumCircuitSimulator simulator = new QuantumCircuitSimulator();

        assertThatCode(simulator::start).doesNotThrowAnyException();
        String output = outputStream.toString();
        assertThat(output).contains("[ERROR]");
    }

    @Test
    @DisplayName("CNOT 게이트를 추가하여 얽힘을 생성한다")
    void addCNOTGate() {
        String input = "2\nCNOT\n0\n1\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        QuantumCircuitSimulator simulator = new QuantumCircuitSimulator();
        simulator.start();

        String output = outputStream.toString();
        assertThat(output).contains("CNOT");
    }
}
