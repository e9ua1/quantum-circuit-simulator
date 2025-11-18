package quantum.circuit.gui.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SystemStateHistogram 테스트")
class SystemStateHistogramTest {

    @Test
    @DisplayName("SystemStateHistogram 클래스가 존재한다")
    void SystemStateHistogram_클래스가_존재한다() {
        // given & when & then
        assertThat(SystemStateHistogram.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = SystemStateHistogram.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("updateState 메서드가 존재한다")
    void updateState_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = SystemStateHistogram.class;

        // when
        Method method = clazz.getDeclaredMethod("updateState", quantum.circuit.domain.state.QuantumState.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }

    @Test
    @DisplayName("getRoot 메서드가 VBox를 반환한다")
    void getRoot_메서드가_VBox를_반환한다() throws Exception {
        // given
        Class<?> clazz = SystemStateHistogram.class;

        // when
        Method method = clazz.getDeclaredMethod("getRoot");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("VBox");
    }
}
