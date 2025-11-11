package quantum.circuit.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationReportTest {

    @Test
    @DisplayName("검증 리포트를 생성한다")
    void createValidationReport() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.success()
        );

        ValidationReport report = new ValidationReport(results);

        assertThat(report).isNotNull();
    }

    @Test
    @DisplayName("모든 검증이 성공하면 유효하다")
    void validWhenAllSuccess() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.success()
        );
        ValidationReport report = new ValidationReport(results);

        assertThat(report.isAllValid()).isTrue();
    }

    @Test
    @DisplayName("하나라도 실패하면 유효하지 않다")
    void invalidWhenAnyFails() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.failure("에러")
        );
        ValidationReport report = new ValidationReport(results);

        assertThat(report.isAllValid()).isFalse();
    }

    @Test
    @DisplayName("실패한 검증의 에러 메시지를 반환한다")
    void returnErrorMessages() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.failure("에러 1"),
                ValidationResult.failure("에러 2")
        );
        ValidationReport report = new ValidationReport(results);

        List<String> errors = report.getErrors();

        assertThat(errors).hasSize(2);
        assertThat(errors).contains("에러 1", "에러 2");
    }

    @Test
    @DisplayName("성공한 검증만 있으면 에러 메시지가 비어있다")
    void emptyErrorsWhenAllSuccess() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.success()
        );
        ValidationReport report = new ValidationReport(results);

        List<String> errors = report.getErrors();

        assertThat(errors).isEmpty();
    }

    @Test
    @DisplayName("검증 개수를 반환한다")
    void returnValidationCount() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.failure("에러")
        );
        ValidationReport report = new ValidationReport(results);

        int count = report.getValidationCount();

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("성공한 검증 개수를 반환한다")
    void returnSuccessCount() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.success(),
                ValidationResult.failure("에러")
        );
        ValidationReport report = new ValidationReport(results);

        int successCount = report.getSuccessCount();

        assertThat(successCount).isEqualTo(2);
    }

    @Test
    @DisplayName("실패한 검증 개수를 반환한다")
    void returnFailureCount() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.failure("에러 1"),
                ValidationResult.failure("에러 2")
        );
        ValidationReport report = new ValidationReport(results);

        int failureCount = report.getFailureCount();

        assertThat(failureCount).isEqualTo(2);
    }

    @Test
    @DisplayName("빈 검증 리포트를 생성한다")
    void createEmptyReport() {
        ValidationReport report = new ValidationReport(List.of());

        assertThat(report.isAllValid()).isTrue();
        assertThat(report.getValidationCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("리포트 문자열을 생성한다")
    void generateReportString() {
        List<ValidationResult> results = List.of(
                ValidationResult.success(),
                ValidationResult.failure("에러 메시지")
        );
        ValidationReport report = new ValidationReport(results);

        String reportString = report.toString();

        assertThat(reportString).contains("검증 결과");
        assertThat(reportString).contains("에러 메시지");
    }
}
