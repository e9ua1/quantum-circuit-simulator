package quantum.circuit.analyzer;

public class AnalysisReport {

    private static final String REPORT_FORMAT = """
            === 회로 분석 결과 ===
            깊이: %d
            게이트 개수: %d
            복잡도: %d
            얽힘 정도: %d
            """;

    private final int depth;
    private final int gateCount;
    private final int complexity;
    private final int entanglementDegree;

    public AnalysisReport(int depth, int gateCount, int complexity, int entanglementDegree) {
        this.depth = depth;
        this.gateCount = gateCount;
        this.complexity = complexity;
        this.entanglementDegree = entanglementDegree;
    }

    public int getDepth() {
        return depth;
    }

    public int getGateCount() {
        return gateCount;
    }

    public int getComplexity() {
        return complexity;
    }

    public int getEntanglementDegree() {
        return entanglementDegree;
    }

    @Override
    public String toString() {
        return String.format(REPORT_FORMAT, depth, gateCount, complexity, entanglementDegree);
    }
}
