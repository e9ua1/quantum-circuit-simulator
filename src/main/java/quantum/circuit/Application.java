package quantum.circuit;

public class Application {

    private static final String WELCOME_MESSAGE = """
            ===================================
            Quantum Circuit Simulator
            ===================================
            """;

    public static void main(String[] args) {
        System.out.println(WELCOME_MESSAGE);

        QuantumCircuitSimulator simulator = new QuantumCircuitSimulator();
        simulator.start();
    }
}
