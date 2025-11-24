package quantum.circuit.analyzer;

public record AnalysisReport(int depth, int gateCount, int complexity, int entanglementDegree) {

    private static final String REPORT_FORMAT = """
            === 회로 분석 결과 ===
            깊이: %d
            게이트 개수: %d
            복잡도: %d
            얽힘 정도: %d
            """;

    @Override
    public String toString() {
        return String.format(REPORT_FORMAT, depth, gateCount, complexity, entanglementDegree);
    }
}
