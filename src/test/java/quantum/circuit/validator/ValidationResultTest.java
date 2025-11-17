package quantum.circuit.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationResultTest {

    @Test
    @DisplayName("성공 결과를 생성한다")
    void createSuccessResult() {
        ValidationResult result = ValidationResult.success();

        assertThat(result.isValid()).isTrue();
        assertThat(result.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("실패 결과를 생성한다")
    void createFailureResult() {
        ValidationResult result = ValidationResult.failure("에러 메시지");

        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).isEqualTo("에러 메시지");
    }

    @Test
    @DisplayName("성공 결과는 메시지가 비어있다")
    void successHasEmptyMessage() {
        ValidationResult result = ValidationResult.success();

        assertThat(result.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("실패 결과는 메시지를 포함한다")
    void failureHasMessage() {
        ValidationResult result = ValidationResult.failure("검증 실패");

        assertThat(result.getMessage()).isEqualTo("검증 실패");
    }

    @Test
    @DisplayName("성공 결과는 유효하다")
    void successIsValid() {
        ValidationResult result = ValidationResult.success();

        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName("실패 결과는 유효하지 않다")
    void failureIsInvalid() {
        ValidationResult result = ValidationResult.failure("에러");

        assertThat(result.isValid()).isFalse();
    }
}
