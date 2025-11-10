package quantum.circuit.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import camp.nextstep.edu.missionutils.Console;

class InputViewTest {

    @AfterEach
    void tearDown() {
        Console.close();
    }

    @Test
    @DisplayName("큐비트 개수를 입력받는다")
    void readQubitCount() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        int qubitCount = InputView.readQubitCount();

        assertThat(qubitCount).isEqualTo(2);
    }

    @Test
    @DisplayName("게이트 종류를 입력받는다")
    void readGateType() {
        String input = "X\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        String gateType = InputView.readGateType();

        assertThat(gateType).isEqualTo("X");
    }

    @Test
    @DisplayName("타겟 큐비트 인덱스를 입력받는다")
    void readTargetQubit() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        int target = InputView.readTargetQubit();

        assertThat(target).isEqualTo(0);
    }

    @Test
    @DisplayName("제어 큐비트 인덱스를 입력받는다")
    void readControlQubit() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        int control = InputView.readControlQubit();

        assertThat(control).isEqualTo(0);
    }

    @Test
    @DisplayName("숫자가 아닌 입력은 예외를 발생시킨다")
    void invalidNumberThrowsException() {
        String input = "abc\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertThatThrownBy(InputView::readQubitCount)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("숫자");
    }

    @Test
    @DisplayName("빈 입력은 예외를 발생시킨다")
    void emptyInputThrowsException() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertThatThrownBy(InputView::readGateType)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있습니다");
    }
}
