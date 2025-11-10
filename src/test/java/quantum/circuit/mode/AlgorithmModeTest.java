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

class AlgorithmModeTest {

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
    @DisplayName("알고리즘 모드를 시작한다")
    void startAlgorithmMode() {
        String input = "BELL_STATE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        AlgorithmMode mode = new AlgorithmMode();

        assertThatCode(mode::start).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용 가능한 알고리즘 목록을 출력한다")
    void printAvailableAlgorithms() {
        String input = "BELL_STATE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        AlgorithmMode mode = new AlgorithmMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("알고리즘 라이브러리");
        assertThat(output).contains("Bell State");
        assertThat(output).contains("GHZ State");
    }

    @Test
    @DisplayName("선택한 알고리즘을 실행하고 회로를 출력한다")
    void executeSelectedAlgorithm() {
        String input = "BELL_STATE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        AlgorithmMode mode = new AlgorithmMode();

        assertThatCode(mode::start).doesNotThrowAnyException();

        String output = outputStream.toString();
        assertThat(output).contains("Bell State");
    }

    @Test
    @DisplayName("잘못된 알고리즘 선택 시 재입력을 받는다")
    void retryOnInvalidAlgorithm() {
        String input = "INVALID\nBELL_STATE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        AlgorithmMode mode = new AlgorithmMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("[ERROR]");
    }

    @Test
    @DisplayName("GHZ State 알고리즘을 실행한다")
    void executeGHZStateAlgorithm() {
        String input = "GHZ_STATE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        AlgorithmMode mode = new AlgorithmMode();
        mode.start();

        String output = outputStream.toString();
        assertThat(output).contains("GHZ State");
        assertThat(output).contains("3큐비트");
    }
}
