package quantum.circuit.gui.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AlgorithmSelectionDialog 테스트")
class AlgorithmSelectionDialogTest {

    @Test
    @DisplayName("AlgorithmSelectionDialog 클래스가 존재한다")
    void AlgorithmSelectionDialog_클래스가_존재한다() {
        // given & when & then
        assertThat(AlgorithmSelectionDialog.class).isNotNull();
    }

    @Test
    @DisplayName("public 생성자를 가진다")
    void public_생성자를_가진다() throws Exception {
        // given & when
        var constructor = AlgorithmSelectionDialog.class.getDeclaredConstructor();

        // then
        assertThat(constructor).isNotNull();
        assertThat(Modifier.isPublic(constructor.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("show 메서드가 Optional을 반환한다")
    void show_메서드가_Optional을_반환한다() throws Exception {
        // given
        Class<?> clazz = AlgorithmSelectionDialog.class;

        // when
        Method method = clazz.getDeclaredMethod("show");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Optional");
    }

    @Test
    @DisplayName("getSelectedAlgorithm 메서드가 존재한다")
    void getSelectedAlgorithm_메서드가_존재한다() throws Exception {
        // given
        Class<?> clazz = AlgorithmSelectionDialog.class;

        // when
        Method method = clazz.getDeclaredMethod("getSelectedAlgorithm");

        // then
        assertThat(method).isNotNull();
        assertThat(method.getReturnType().getSimpleName()).isEqualTo("Optional");
    }
}
