package quantum.circuit.validator;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationReport {

    private static final String REPORT_HEADER = "=== 검증 결과 ===";

    private final List<ValidationResult> results;

    public ValidationReport(List<ValidationResult> results) {
        this.results = List.copyOf(results);
    }

    public boolean isAllValid() {
        return results.stream().allMatch(ValidationResult::isValid);
    }

    public List<String> getErrors() {
        return results.stream()
                .filter(result -> !result.isValid())
                .map(ValidationResult::getMessage)
                .collect(Collectors.toList());
    }

    public int getValidationCount() {
        return results.size();
    }

    public int getSuccessCount() {
        return (int) results.stream()
                .filter(ValidationResult::isValid)
                .count();
    }

    public int getFailureCount() {
        return (int) results.stream()
                .filter(result -> !result.isValid())
                .count();
    }

    @Override
    public String toString() {
        if (results.isEmpty()) {
            return REPORT_HEADER + "\n(검증 없음)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(REPORT_HEADER).append("\n");
        sb.append("총 검증: ").append(getValidationCount()).append("\n");
        sb.append("성공: ").append(getSuccessCount()).append("\n");
        sb.append("실패: ").append(getFailureCount()).append("\n");

        List<String> errors = getErrors();
        if (!errors.isEmpty()) {
            sb.append("\n에러 목록:\n");
            for (String error : errors) {
                sb.append("- ").append(error).append("\n");
            }
        }

        return sb.toString();
    }
}
