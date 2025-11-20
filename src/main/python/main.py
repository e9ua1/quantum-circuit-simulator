import json
import sys
from pathlib import Path


def load_circuit_data(json_path):
    with open(json_path, 'r') as f:
        return json.load(f)


def visualize_all(data):
    from visualizer.bloch_sphere import BlochSphereVisualizer
    from visualizer.histogram import StateHistogramVisualizer

    bloch_viz = BlochSphereVisualizer()
    hist_viz = StateHistogramVisualizer()

    qubit_probs = data.get('qubit_probabilities', {})
    system_state = data.get('system_state', {})

    if qubit_probs and '0' in qubit_probs:
        prob_one = qubit_probs['0']
        print(f"Visualizing Bloch Sphere (Qubit 0, P(|1⟩) = {prob_one:.3f})")
        bloch_viz.visualize_static(prob_one, save_path='output/bloch_sphere.png')

    if system_state:
        print(f"Visualizing State Histogram")
        hist_viz.visualize_static(system_state, save_path='output/histogram.png')

    print("Visualization complete!")
    print("  - output/bloch_sphere.png")
    print("  - output/histogram.png")


def main():
    if len(sys.argv) < 2:
        print("Usage: python main.py <circuit_result.json>")
        sys.exit(1)

    json_path = sys.argv[1]

    if not Path(json_path).exists():
        print(f"Error: {json_path} not found")
        sys.exit(1)

    data = load_circuit_data(json_path)
    visualize_all(data)


if __name__ == '__main__':
    main()
