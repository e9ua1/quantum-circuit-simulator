package quantum.circuit.gui.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GatePalette 테스트")
class GatePaletteTest {

    @Test
    @DisplayName("GatePalette 클래스가 존재한다")
    void GatePalette_클래스가_존재한다() {
        // given & when & then
        assertThat(GatePalette.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = GatePalette.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("getRoot 메서드가 VBox를 반환한다")
    void getRoot_메서드가_VBox를_반환한다() throws Exception {
        // given
        Class<?> clazz = GatePalette.class;

        // when
        Method method = clazz.getDeclaredMethod("getRoot");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("VBox");
    }

    @Test
    @DisplayName("setOnGateDragDetected 메서드가 존재한다")
    void setOnGateDragDetected_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = GatePalette.class;

        // when
        Method method = clazz.getDeclaredMethod("setOnGateDragDetected", 
                java.util.function.BiConsumer.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(void.class);
    }
}
