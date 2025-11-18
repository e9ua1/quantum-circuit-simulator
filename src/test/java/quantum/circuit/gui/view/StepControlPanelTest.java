package quantum.circuit.gui.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import quantum.circuit.gui.view.StepControlPanel;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StepControlPanel 테스트")
class StepControlPanelTest {

    @Test
    @DisplayName("StepControlPanel 클래스가 존재한다")
    void StepControlPanel_클래스가_존재한다() {
        // given & when & then
        assertThat(StepControlPanel.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = StepControlPanel.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("getRoot 메서드가 HBox를 반환한다")
    void getRoot_메서드가_HBox를_반환한다() throws Exception {
        // given
        Class<?> clazz = StepControlPanel.class;

        // when
        Method method = clazz.getDeclaredMethod("getRoot");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("HBox");
    }

    @Test
    @DisplayName("setOnNextStep 메서드가 존재한다")
    void setOnNextStep_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepControlPanel.class;

        // when
        Method method = clazz.getDeclaredMethod("setOnNextStep", Runnable.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("setOnPreviousStep 메서드가 존재한다")
    void setOnPreviousStep_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepControlPanel.class;

        // when
        Method method = clazz.getDeclaredMethod("setOnPreviousStep", Runnable.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("updateStepInfo 메서드가 존재한다")
    void updateStepInfo_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = StepControlPanel.class;

        // when
        Method method = clazz.getDeclaredMethod("updateStepInfo", int.class, int.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }
}
