package quantum.circuit.gui.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CircuitController 테스트")
class CircuitControllerTest {

    @Test
    @DisplayName("CircuitController 클래스가 존재한다")
    void CircuitController_클래스가_존재한다() {
        // given & when & then
        assertThat(CircuitController.class).isNotNull();
    }

    @Test
    @DisplayName("MainWindow를 받는 public 생성자를 가진다")
    void MainWindow를_받는_public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = CircuitController.class.getDeclaredConstructor(
                quantum.circuit.gui.view.MainWindow.class
        );

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("loadAlgorithm 메서드가 존재한다")
    void loadAlgorithm_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = CircuitController.class;

        // when
        Method method = clazz.getDeclaredMethod("loadAlgorithm", quantum.circuit.algorithm.AlgorithmType.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("getCurrentCircuit 메서드가 QuantumCircuit을 반환한다")
    void getCurrentCircuit_메서드가_QuantumCircuit을_반환한다() throws Exception {
        // given
        Class<?> clazz = CircuitController.class;

        // when
        Method method = clazz.getDeclaredMethod("getCurrentCircuit");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("QuantumCircuit");
    }
}
