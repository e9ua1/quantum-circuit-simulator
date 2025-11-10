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

class ApplicationTest {

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
    @DisplayName("애플리케이션이 정상적으로 실행된다")
    void applicationRuns() {
        String input = "1\n1\nX\n0\nn\n";  // 모드 1 추가
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertThatCode(() -> Application.main(new String[]{}))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("애플리케이션 시작 시 환영 메시지를 출력한다")
    void printsWelcomeMessage() {
        String input = "1\n1\nX\n0\nn\n";  // 모드 1 추가
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("Quantum Circuit Simulator");
    }

    @Test
    @DisplayName("Bell State를 생성하고 시뮬레이션한다")
    void simulateBellState() {
        String input = "1\n2\nH\n0\ny\nCNOT\n0\n1\nn\n";  // 모드 1 추가
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("H");
        assertThat(output).contains("CNOT");
    }

    @Test
    @DisplayName("단일 큐비트 회로를 시뮬레이션한다")
    void simulateSingleQubitCircuit() {
        String input = "1\n1\nH\n0\nn\n";  // 모드 1 추가
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("Qubit 0");
    }
}
