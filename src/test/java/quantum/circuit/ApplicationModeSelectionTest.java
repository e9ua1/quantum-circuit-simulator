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

class ApplicationModeSelectionTest {

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
    @DisplayName("모드 선택 메뉴를 출력한다")
    void printModeSelectionMenu() {
        String input = "1\n2\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertThatCode(() -> Application.main(new String[]{}))
                .doesNotThrowAnyException();

        String output = outputStream.toString();
        assertThat(output).contains("모드를 선택하세요");
        assertThat(output).contains("1. 자유 모드");
        assertThat(output).contains("2. 알고리즘 라이브러리");
    }

    @Test
    @DisplayName("모드 1 선택 시 자유 모드를 실행한다")
    void selectFreeMode() {
        String input = "1\n2\nX\n0\nn\n";
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
        String input = "99\n1\n2\nX\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Application.main(new String[]{});

        String output = outputStream.toString();
        assertThat(output).contains("[ERROR]");
    }
}
