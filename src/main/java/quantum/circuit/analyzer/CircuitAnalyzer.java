package quantum.circuit.analyzer;

import quantum.circuit.domain.circuit.QuantumCircuit;

public class CircuitAnalyzer {

    public static AnalysisReport analyze(QuantumCircuit circuit) {
        int depth = CircuitDepth.calculate(circuit);
        int gateCount = GateCount.calculate(circuit);
        int complexity = CircuitComplexity.calculate(circuit);
        int entanglementDegree = EntanglementDegree.calculate(circuit);

        return new AnalysisReport(depth, gateCount, complexity, entanglementDegree);
    }
}
