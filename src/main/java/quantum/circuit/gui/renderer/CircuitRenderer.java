package quantum.circuit.gui.renderer;

import javafx.scene.layout.Pane;
import quantum.circuit.domain.circuit.QuantumCircuit;

public interface CircuitRenderer {

    Pane render(QuantumCircuit circuit);
}
