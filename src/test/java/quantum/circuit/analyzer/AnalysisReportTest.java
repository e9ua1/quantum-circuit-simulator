package quantum.circuit.analyzer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnalysisReportTest {

    @Test
    @DisplayName("분석 리포트를 생성한다")
    void createAnalysisReport() {
        AnalysisReport report = new AnalysisReport(3, 5, 7, 2);

        assertThat(report).isNotNull();
    }

    @Test
    @DisplayName("회로 깊이를 반환한다")
    void getDepth() {
        AnalysisReport report = new AnalysisReport(3, 5, 7, 2);

        assertThat(report.depth()).isEqualTo(3);
    }

    @Test
    @DisplayName("게이트 개수를 반환한다")
    void getGateCount() {
        AnalysisReport report = new AnalysisReport(3, 5, 7, 2);

        assertThat(report.gateCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("복잡도를 반환한다")
    void getComplexity() {
        AnalysisReport report = new AnalysisReport(3, 5, 7, 2);

        assertThat(report.complexity()).isEqualTo(7);
    }

    @Test
    @DisplayName("얽힘 정도를 반환한다")
    void getEntanglementDegree() {
        AnalysisReport report = new AnalysisReport(3, 5, 7, 2);

        assertThat(report.entanglementDegree()).isEqualTo(2);
    }

    @Test
    @DisplayName("분석 리포트를 문자열로 출력한다")
    void toStringOutput() {
        AnalysisReport report = new AnalysisReport(3, 5, 7, 2);

        String output = report.toString();

        assertThat(output).contains("깊이: 3");
        assertThat(output).contains("게이트 개수: 5");
        assertThat(output).contains("복잡도: 7");
        assertThat(output).contains("얽힘 정도: 2");
    }

    @Test
    @DisplayName("빈 회로의 분석 리포트를 생성한다")
    void emptyCircuitReport() {
        AnalysisReport report = new AnalysisReport(0, 0, 0, 0);

        assertThat(report.depth()).isEqualTo(0);
        assertThat(report.gateCount()).isEqualTo(0);
        assertThat(report.complexity()).isEqualTo(0);
        assertThat(report.entanglementDegree()).isEqualTo(0);
    }

    @Test
    @DisplayName("복잡한 회로의 분석 리포트를 생성한다")
    void complexCircuitReport() {
        AnalysisReport report = new AnalysisReport(10, 20, 25, 5);

        assertThat(report.depth()).isEqualTo(10);
        assertThat(report.gateCount()).isEqualTo(20);
        assertThat(report.complexity()).isEqualTo(25);
        assertThat(report.entanglementDegree()).isEqualTo(5);
    }
}
