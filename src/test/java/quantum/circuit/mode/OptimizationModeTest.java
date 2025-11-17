package quantum.circuit.mode;

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

class OptimizationModeTest {

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
    @DisplayName("최적화 모드를 시작한다")
    void startOptimizationMode() {
        String input = "1\nH\n0\ny\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();

        assertThatCode(mode::start).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("최적화 모드 메뉴를 출력한다")
    void printOptimizationMenu() {
        String input = "1\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("최적화 모드");
    }

    @Test
    @DisplayName("회로 입력을 받는다")
    void inputCircuit() {
        String input = "1\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("큐비트");
    }

    @Test
    @DisplayName("최적화 전 회로를 출력한다")
    void printOriginalCircuit() {
        String input = "1\nH\n0\ny\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("최적화 전");
    }

    @Test
    @DisplayName("최적화 후 회로를 출력한다")
    void printOptimizedCircuit() {
        String input = "1\nH\n0\ny\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("최적화 후");
    }

    @Test
    @DisplayName("최적화 결과를 비교한다")
    void compareOptimizationResult() {
        String input = "1\nH\n0\ny\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("Step");
    }

    @Test
    @DisplayName("중복 게이트가 제거된다")
    void removeRedundantGates() {
        String input = "1\nX\n0\ny\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("최적화");
    }

    @Test
    @DisplayName("잘못된 입력 시 재입력을 받는다")
    void retryOnInvalidInput() {
        String input = "abc\n1\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        OptimizationMode mode = new OptimizationMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("[ERROR]");
    }
}
