package quantum.circuit;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import camp.nextstep.edu.missionutils.Console;

class ApplicationWithOptimizationTest {

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
    @DisplayName("모드 선택 메뉴에 최적화 모드가 표시된다")
    void showOptimizationModeInMenu() {
        String input = "1\n1\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("3. 최적화 모드");
    }

    @Test
    @DisplayName("모드 3 선택 시 최적화 모드를 실행한다")
    void selectOptimizationMode() {
        String input = "3\n1\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("최적화 모드");
    }

    @Test
    @DisplayName("모드 1 선택 시 자유 모드를 실행한다")
    void selectFreeMode() {
        String input = "1\n1\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("큐비트 개수");
    }

    @Test
    @DisplayName("모드 2 선택 시 알고리즘 모드를 실행한다")
    void selectAlgorithmMode() {
        String input = "2\nBELL_STATE\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("알고리즘 라이브러리");
    }

    @Test
    @DisplayName("잘못된 모드 선택 시 재입력을 받는다")
    void retryOnInvalidMode() {
        String input = "99\n1\n1\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("[ERROR]");
    }
}
