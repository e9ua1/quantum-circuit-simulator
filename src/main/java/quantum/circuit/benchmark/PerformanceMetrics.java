package quantum.circuit.benchmark;

public class PerformanceMetrics {

    private final int gateCount;
    private final int depth;
    private final long executionTime;

    public PerformanceMetrics(int gateCount, int depth, long executionTime) {
        this.gateCount = gateCount;
        this.depth = depth;
        this.executionTime = executionTime;
    }

    public int getGateCount() {
        return gateCount;
    }

    public int getDepth() {
        return depth;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
