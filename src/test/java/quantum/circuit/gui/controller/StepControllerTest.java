package quantum.circuit.gui.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StepController 테스트")
class StepControllerTest {

    @Test
    @DisplayName("StepController 클래스가 존재한다")
    void StepController_클래스가_존재한다() {
        // given & when & then
        assertThat(StepController.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = StepController.class.getDeclaredConstructor(
                quantum.circuit.gui.view.MainWindow.class
        );

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("loadCircuit 메서드가 존재한다")
    void loadCircuit_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepController.class;

        // when
        Method method = clazz.getDeclaredMethod("loadCircuit", quantum.circuit.domain.circuit.QuantumCircuit.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("nextStep 메서드가 존재한다")
    void nextStep_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepController.class;

        // when
        Method method = clazz.getDeclaredMethod("nextStep");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("previousStep 메서드가 존재한다")
    void previousStep_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepController.class;

        // when
        Method method = clazz.getDeclaredMethod("previousStep");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("reset 메서드가 존재한다")
    void reset_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepController.class;

        // when
        Method method = clazz.getDeclaredMethod("reset");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("runToEnd 메서드가 존재한다")
    void runToEnd_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepController.class;

        // when
        Method method = clazz.getDeclaredMethod("runToEnd");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("getCurrentStep 메서드가 int를 반환한다")
    void getCurrentStep_메서드가_int를_반환한다() throws Exception {
        // given
        Class<?> clazz = StepController.class;

        // when
        Method method = clazz.getDeclaredMethod("getCurrentStep");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(int.class);
    }

    @Test
    @DisplayName("getTotalSteps 메서드가 int를 반환한다")
    void getTotalSteps_메서드가_int를_반환한다() throws Exception {
        // given
        Class<?> clazz = StepController.class;

        // when
        Method method = clazz.getDeclaredMethod("getTotalSteps");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(int.class);
    }
}
