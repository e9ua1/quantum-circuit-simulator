package quantum.circuit.gui.renderer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BlochSphereRenderer 테스트")
class BlochSphereRendererTest {

    @Test
    @DisplayName("BlochSphereRenderer 클래스가 존재한다")
    void BlochSphereRenderer_클래스가_존재한다() {
        // given & when & then
        assertThat(BlochSphereRenderer.class).isNotNull();
    }

    @Test
    @DisplayName("render 메서드가 존재하고 SubScene을 반환한다")
    void render_메서드가_존재하고_SubScene을_반환한다() throws Exception {
        // given
        Class<?> clazz = BlochSphereRenderer.class;

        // when
        Method method = clazz.getDeclaredMethod("render", double.class);

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("SubScene");
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = BlochSphereRenderer.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }
}
