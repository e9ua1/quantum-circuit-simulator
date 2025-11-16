package quantum.circuit.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import camp.nextstep.edu.missionutils.Console;
import quantum.circuit.domain.circuit.CircuitStep;

class CircuitStepBuilderTest {

    @AfterEach
    void tearDown() {
        Console.close();
    }

    @Test
    @DisplayName("단일 게이트 Step을 생성한다")
    void buildSingleGateStep() {
        String input = "H\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        List<CircuitStep> steps = CircuitStepBuilder.buildSteps(2);

        assertThat(steps).hasSize(1);
        assertThat(steps.get(0).isSingleGateStep()).isTrue();
    }

    @Test
    @DisplayName("여러 게이트 Step을 생성한다")
    void buildMultipleGateSteps() {
        String input = "H\n0\ny\nX\n1\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        List<CircuitStep> steps = CircuitStepBuilder.buildSteps(2);

        assertThat(steps).hasSize(2);
    }

    @Test
    @DisplayName("CNOT 게이트 Step을 생성한다")
    void buildCNOTGateStep() {
        String input = "CNOT\n0\n1\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        List<CircuitStep> steps = CircuitStepBuilder.buildSteps(2);

        assertThat(steps).hasSize(1);
        assertThat(steps.get(0).getSingleGate().getName()).isEqualTo("CNOT");
    }

    @Test
    @DisplayName("잘못된 입력 후 재입력으로 Step을 생성한다")
    void buildStepsWithRetry() {
        String input = "INVALID\nH\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        List<CircuitStep> steps = CircuitStepBuilder.buildSteps(2);

        assertThat(steps).hasSize(1);
    }

    @Test
    @DisplayName("계속 입력 여부를 확인한다")
    void checkContinueInput() {
        String input = "H\n0\ny\nX\n1\ny\nZ\n0\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        List<CircuitStep> steps = CircuitStepBuilder.buildSteps(2);

        assertThat(steps).hasSize(3);
    }
}
